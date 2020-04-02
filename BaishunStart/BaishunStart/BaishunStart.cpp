// BaishunStart.cpp : ����Ӧ�ó������ڵ㡣
//

#include "stdafx.h"
#include "BaishunStart.h"
#include "BootstrapClientWnd.h"

int APIENTRY _tWinMain(_In_ HINSTANCE hInstance,
	_In_opt_ HINSTANCE hPrevInstance,
	_In_ LPTSTR    lpCmdLine,
	_In_ int       nCmdShow)
{
	HANDLE hMutex = CreateMutex(NULL, false, BOOTSTRAP_WIN_CLASS);
	if (GetLastError() == ERROR_ALREADY_EXISTS)
	{
		MessageBox(NULL, _T("����������"), _T("��ʾ"), MB_OK | MB_ICONWARNING);
		return -1;
	}
	CPaintManagerUI::SetInstance(hInstance);
	CPaintManagerUI::SetResourcePath(CPaintManagerUI::GetInstancePath() + _T("../skin"));
	//CPaintManagerUI::SetResourceZip(_T("EmsClientRes.zip"));

	//CoInitialize��Windows�ṩ��API�������������� Windows�Ե��̵߳ķ�ʽ����com����Ӧ�ó������com�⺯������CoGetMalloc���ڴ���亯����֮ǰ�����ʼ��com�⡣
	HRESULT Hr = ::CoInitialize(NULL);
	if (FAILED(Hr)) return 0;
	//g_clientMain.Init();

	BootstrapClientWnd * pClientWnd = new BootstrapClientWnd;
	pClientWnd->Create(NULL, _T(""), UI_WNDSTYLE_DIALOG, 0, 0, 0, 0, 0, NULL);
	
	//HICON hIcon = ::LoadIcon(hInstance, MAKEINTRESOURCE(IDI_ICON_LOVE));
	//::SendMessage(pClientWnd->GetHWND(), STM_SETICON, IMAGE_ICON, (LPARAM)(UINT)hIcon);
	
	pClientWnd->CenterWindow();
	pClientWnd->SetIcon(IDI_ICON_LOVE);

	pClientWnd->ShowModal();
	CPaintManagerUI::MessageLoop();

	delete pClientWnd;
	::CoUninitialize();
	//_CrtDumpMemoryLeaks;
	return 0;
}
