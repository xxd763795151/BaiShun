#include "stdafx.h"
#include "BootstrapClientWnd.h"
#include <thread>
#include <shellapi.h>

BootstrapClientWnd::BootstrapClientWnd()
{
}


BootstrapClientWnd::~BootstrapClientWnd()
{
	if (m_httpClient != NULL) {
		delete m_httpClient;
	}
}

BootstrapClientWnd::BootstrapClientWnd(CConfig config) {
	m_config = config;
	m_httpClient = new CHttpClient(config);
	int status = m_httpClient->init();
	if (status != 0) {
		MessageBox(NULL, m_httpClient->ErrorMsg(), _T("退出警告"), MB_OK | MB_ICONWARNING);
		ExitProcess(0);
	}
}

LPCTSTR BootstrapClientWnd::GetWindowClassName() const
{
	return BOOTSTRAP_WIN_CLASS;
}

void BootstrapClientWnd::Notify(TNotifyUI& msg)
{
	if (msg.sType == _T("click")) {
		if (msg.pSender->GetName() == _T("closebtn")) {
			// 关闭web服务器
			if (!m_bWebPortIsUsed && !m_bWebStarted) {
				ExitProcess(0); return;
			}
			if (m_httpClient->connect()) {
				char * szSendMsg = "GET /system/exit HTTP/1.1\r\nHost: 127.0.0.1\r\nCache-Control: no-cache\r\nAccept: */*\r\nConnection: close\r\n\r\n";
				bool done = m_httpClient->send(szSendMsg);
				if (!done) {
					MessageBox(NULL, m_httpClient->ErrorMsg(), _T("警告"), MB_OK | MB_ICONWARNING);
				}
				else {
					ExitProcess(0);
					return;
				}
			}
			else {
				MessageBox(NULL, m_httpClient->ErrorMsg(), _T("警告"), MB_OK | MB_ICONWARNING);
			}
		}
		if (msg.pSender == m_pStartBtn){
			std::thread thread(&BootstrapClientWnd::Start, this);
			thread.detach();
			//Start();
		}
		if (msg.pSender == m_pMinBtn)
		{
			SendMessage(WM_SYSCOMMAND, SC_MINIMIZE, 0); return;
		}
		if (msg.pSender == m_pCopyBtn) {
			SetClip(m_pUrlEdit->GetText().GetData());
		}
	}
	
}

LRESULT BootstrapClientWnd::HandleMessage(UINT uMsg, WPARAM wParam, LPARAM lParam)
{
	LRESULT lRes = 0;
	BOOL bHandled = TRUE;
	switch (uMsg) {
	case WM_CREATE:        lRes = OnCreate(uMsg, wParam, lParam, bHandled); break;
	case WM_NCACTIVATE:    lRes = OnNcActivate(uMsg, wParam, lParam, bHandled); break;
	case WM_NCCALCSIZE:    lRes = OnNcCalcSize(uMsg, wParam, lParam, bHandled); break;
	case WM_NCPAINT:       lRes = OnNcPaint(uMsg, wParam, lParam, bHandled); break;
	case WM_NCHITTEST:     lRes = OnNcHitTest(uMsg, wParam, lParam, bHandled); break;
	case WM_SIZE:          lRes = OnSize(uMsg, wParam, lParam, bHandled); break;
	case WM_GETMINMAXINFO: lRes = OnGetMinMaxInfo(uMsg, wParam, lParam, bHandled); break;
	default:
		bHandled = FALSE;
	}
	if (bHandled) return lRes;
	if (m_pmUI.MessageHandler(uMsg, wParam, lParam, lRes)) return lRes;

	return __super::HandleMessage(uMsg, wParam, lParam);
}

void BootstrapClientWnd::OnFinalMessage(HWND hWnd)
{
	delete this;
}

void BootstrapClientWnd::Init()
{
	m_pStartBtn = static_cast<CButtonUI*>(m_pmUI.FindControl(_T("startBtn")));
	m_pProgress = static_cast<CProgressUI*>(m_pmUI.FindControl(_T("startProgress")));
	m_pUrlHoriLayout = static_cast<CHorizontalLayoutUI*>(m_pmUI.FindControl(_T("UrlHorizontalLayout")));
	m_pCopyBtn = static_cast<CButtonUI*>(m_pmUI.FindControl(_T("copybtn")));
	m_pMinBtn = static_cast<CButtonUI*>(m_pmUI.FindControl(_T("minbtn")));
	m_pUrlEdit = static_cast<CEditUI*>(m_pmUI.FindControl(_T("urledit")));
}

void BootstrapClientWnd::OnPrepare()
{

}

LRESULT BootstrapClientWnd::OnCreate(UINT uMsg, WPARAM wParam, LPARAM lParam, BOOL& bHandled)
{
	LONG styleValue = ::GetWindowLong(*this, GWL_STYLE);
	styleValue &= ~WS_CAPTION;
	::SetWindowLong(*this, GWL_STYLE, styleValue | WS_CLIPSIBLINGS | WS_CLIPCHILDREN);

	m_pmUI.Init(m_hWnd);
	CDialogBuilder builder;
	CControlUI* pRoot = builder.Create(_T("client.xml"), (UINT)0, NULL, &m_pmUI);
	ASSERT(pRoot && "Failed to parse XML");
	m_pmUI.AttachDialog(pRoot);
	m_pmUI.AddNotifier(this);

	Init();
	return 0;
}

UINT BootstrapClientWnd::GetClassStyle()
{
	return UI_CLASSSTYLE_DIALOG;;
}

LRESULT BootstrapClientWnd::OnNcActivate(UINT uMsg, WPARAM wParam, LPARAM lParam, BOOL& bHandled)
{
	if (::IsIconic(*this)) bHandled = FALSE;
	return (wParam == 0) ? TRUE : FALSE;
}

LRESULT BootstrapClientWnd::OnNcCalcSize(UINT uMsg, WPARAM wParam, LPARAM lParam, BOOL& bHandled)
{
	return 0;
}

LRESULT BootstrapClientWnd::OnNcPaint(UINT uMsg, WPARAM wParam, LPARAM lParam, BOOL& bHandled)
{
	return 0;
}

LRESULT BootstrapClientWnd::OnNcHitTest(UINT uMsg, WPARAM wParam, LPARAM lParam, BOOL& bHandled)
{
	POINT pt; pt.x = GET_X_LPARAM(lParam); pt.y = GET_Y_LPARAM(lParam);
	::ScreenToClient(*this, &pt);

	RECT rcClient;
	::GetClientRect(*this, &rcClient);

	RECT rcCaption = m_pmUI.GetCaptionRect();
	if (pt.x >= rcClient.left + rcCaption.left && pt.x < rcClient.right - rcCaption.right
		&& pt.y >= rcCaption.top && pt.y < rcCaption.bottom) {
		CControlUI* pControl = static_cast<CControlUI*>(m_pmUI.FindControl(pt));
		if (pControl && _tcscmp(pControl->GetClass(), _T("ButtonUI")) != 0)
			return HTCAPTION;
	}

	return HTCLIENT;
}

LRESULT BootstrapClientWnd::OnSize(UINT uMsg, WPARAM wParam, LPARAM lParam, BOOL& bHandled)
{
	SIZE szRoundCorner = m_pmUI.GetRoundCorner();
	if (!::IsIconic(*this) && (szRoundCorner.cx != 0 || szRoundCorner.cy != 0)) {
		CDuiRect rcWnd;
		::GetWindowRect(*this, &rcWnd);
		rcWnd.Offset(-rcWnd.left, -rcWnd.top);
		rcWnd.right++; rcWnd.bottom++;
		HRGN hRgn = ::CreateRoundRectRgn(rcWnd.left, rcWnd.top, rcWnd.right, rcWnd.bottom, szRoundCorner.cx, szRoundCorner.cy);
		::SetWindowRgn(*this, hRgn, TRUE);
		::DeleteObject(hRgn);
	}

	bHandled = FALSE;
	return 0;
}


void BootstrapClientWnd::Start() {
	m_pStartBtn->SetText(_T("正在启动"));
	m_pStartBtn->SetEnabled(FALSE);
	m_pStartBtn->SetDisabledTextColor(0X008B00);

	// detect the server port is occupied
	if (m_httpClient->ServerPortIsOpen()) {
		TCHAR szBuf[0xff] = { 0 };
		lstrcat(szBuf, m_httpClient->ErrorMsg());
		lstrcat(szBuf, _T("，如要重启客户端，点击右上角退出按钮重新打开试一下"));
		m_bWebPortIsUsed = true;

		m_pProgress->SetText(_T(""));
		m_pProgress->SetVisible(FALSE);
		m_pStartBtn->SetText(_T("已启动"));
		MessageBox(NULL, szBuf, _T("警告"), MB_OK | MB_ICONWARNING);
		MergeUrl();
		//m_httpClient->Clean();
		//ExitProcess(0);
		return;
	}
	m_bWebPortIsUsed = false;
	TCHAR szPath[0xff];
	CombineFilePath(m_config.ExePath(), _T("start.bat"), szPath, 0xff);
	HINSTANCE instance = ShellExecute(NULL, _T("open"), szPath, NULL, NULL, SW_HIDE);
	//if (WinExec(szPath, SW_NORMAL) < 32) {
	if ((DWORD)instance <= 32) {
		MessageBox(NULL, _T("启动服务器失败，即将退出"), _T("警告"), MB_OK | MB_ICONWARNING);
		ExitProcess(0);
		return;
	}

	bool started = false;
	for (int i = 0; i <= 100; i++) {
		m_pProgress->SetValue(i);
		CDuiString text;
		text.Format(_T("服务器启动中，进度：%d%%"), i);
		m_pProgress->SetText(text);
		if (i < 70) {
			Sleep(150);
		}
		else if (!started && i == 99) {
			if (!m_httpClient->ServerPortIsOpen()) {
				i = 98;
				Sleep(1000);
			}
			else
			{
				started = true;
			}
		}
		else {
			if (!started) {
				if (m_httpClient->ServerPortIsOpen()) {
					started = true;
				}
				Sleep(100);
			}
			else {
				Sleep(10);
			}
		}
	}
	m_pProgress->SetText(_T(""));
	m_pProgress->SetVisible(FALSE);
	m_pStartBtn->SetText(_T("已启动"));
	m_bWebStarted = true;

	// merge url
	MergeUrl();

}

void BootstrapClientWnd::MergeUrl() {
	TCHAR szUrl[0xff] = { 0 };
#ifdef UNICODE
	swprintf_s(szUrl, _T("http://%s:%d/index.html"), m_config.ServerAddress(), m_config.Port());
#else
	sprintf_s(szUrl, "http://s%:%d/index.html", m_config.ServerAddress(), m_config.Port());
#endif
	m_pUrlEdit->SetText(szUrl);
	m_pUrlHoriLayout->SetVisible(TRUE);

	HINSTANCE instance = ShellExecute(NULL, _T("open"), szUrl, NULL, NULL, SW_NORMAL);
	int lastError = WSAGetLastError();
	//if (WinExec(szPath, SW_NORMAL) < 32) {
	if ((DWORD)instance <= 32) {
		MessageBox(NULL, _T("打开浏览器失败， 请手动打开"), _T("提示"), MB_OK | MB_ICONHAND);
	}
}

TCHAR * BootstrapClientWnd::CombineFilePath(TCHAR * first, TCHAR * second, TCHAR * out, int outsize) {
	//int len1 = lstrlen(first), len2 = lstrlen(second);
	//int len = len1 + len2 + 1;
	//TCHAR * out  = new TCHAR(len);
	memset(out, 0, outsize);
	lstrcat(out, first);
	lstrcat(out, second);
	//char * szOut = Tchar2Char(out);
	//delete out;
	return out;
}

CHAR * BootstrapClientWnd::Tchar2Char(TCHAR * src) {
#ifdef UNICODE
	return ConvertLPWSTRToLPSTR(src);
#else
	CHAR * szOut = new CHAR(lstrlen(src) + 1);
	lstrcpy(szOut, src);
	return szOut;
#endif
}


CHAR* BootstrapClientWnd::ConvertLPWSTRToLPSTR(LPWSTR lpwszStrIn)
{
	LPSTR pszOut = NULL;
	if (lpwszStrIn != NULL)
	{
		int nInputStrLen = wcslen(lpwszStrIn);

		// Double NULL Termination
		int nOutputStrLen = WideCharToMultiByte(CP_ACP, 0, lpwszStrIn, nInputStrLen, NULL, 0, 0, 0) + 2;
		pszOut = new char[nOutputStrLen];

		if (pszOut)
		{
			memset(pszOut, 0x00, nOutputStrLen);
			WideCharToMultiByte(CP_ACP, 0, lpwszStrIn, nInputStrLen, pszOut, nOutputStrLen, 0, 0);
		}
	}
	return pszOut;
}

LRESULT BootstrapClientWnd::OnGetMinMaxInfo(UINT uMsg, WPARAM wParam, LPARAM lParam, BOOL& bHandled)
{
	MONITORINFO oMonitor = {};
	oMonitor.cbSize = sizeof(oMonitor);
	::GetMonitorInfo(::MonitorFromWindow(*this, MONITOR_DEFAULTTOPRIMARY), &oMonitor);
	CDuiRect rcWork = oMonitor.rcWork;
	rcWork.Offset(-oMonitor.rcMonitor.left, -oMonitor.rcMonitor.top);

	//控制窗口大小
	LPMINMAXINFO lpMMI = (LPMINMAXINFO)lParam;
	lpMMI->ptMaxPosition.x = rcWork.left;
	lpMMI->ptMaxPosition.y = rcWork.top;
	lpMMI->ptMaxSize.x = rcWork.right;
	lpMMI->ptMaxSize.y = rcWork.bottom;

	bHandled = FALSE;
	return 0;
}

void BootstrapClientWnd::SetClip(LPCTSTR szText){
	OpenClipboard(0);
	EmptyClipboard();

	//MessageBox(NULL, szText, _T("title"), MB_OK | MB_ICONHAND);
	TCHAR tmp[0xff];
	lstrcpy(tmp, szText);
	char * szMsg = m_config.TChar2Char(tmp);
	int nSize = strlen(szMsg) + 1;
	HGLOBAL hMem = ::GlobalAlloc(GHND, nSize);
	byte* pData = (byte*)::GlobalLock(hMem);
	memset(pData, 0, nSize);
	memcpy(pData, szMsg, nSize - 1);
	pData[nSize - 1] = '\0';
	::GlobalUnlock(hMem);
	::SetClipboardData(CF_TEXT, hMem);
	::CloseClipboard();
	::GlobalFree(hMem);
}