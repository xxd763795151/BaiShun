#include "stdafx.h"
#include "BootstrapClientWnd.h"
#include <thread>

BootstrapClientWnd::BootstrapClientWnd()
{
}


BootstrapClientWnd::~BootstrapClientWnd()
{
}

LPCTSTR BootstrapClientWnd::GetWindowClassName() const
{
	return BOOTSTRAP_WIN_CLASS;
}

void BootstrapClientWnd::Notify(TNotifyUI& msg)
{
	if (msg.sType == _T("click")) {
		if (msg.pSender->GetName() == _T("closebtn")) { ExitProcess(0); return; }
		if (msg.pSender == m_pStartBtn){
			std::thread thread(&BootstrapClientWnd::Start, this);
			thread.detach();
			//Start();
		}
		if (msg.pSender == m_pMinBtn)
		{
			SendMessage(WM_SYSCOMMAND, SC_MINIMIZE, 0); return;
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
	for (int i = 0; i <= 100; i++) {
		m_pProgress->SetValue(i);
		CDuiString text;
		text.Format(_T("服务器启动中，进度：%d%%"), i);
		m_pProgress->SetText(text);
		Sleep(10);
	}
	m_pProgress->SetText(_T(""));
	m_pProgress->SetVisible(FALSE);
	m_pStartBtn->SetText(_T("已启动"));
	m_pUrlHoriLayout->SetVisible(TRUE);

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