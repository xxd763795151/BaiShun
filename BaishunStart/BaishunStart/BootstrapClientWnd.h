#pragma once
#include "stdafx.h"
#include "Config.h"
#include "HttpClient.h"

#define BOOTSTRAP_WIN_CLASS _T("BootstrapClientWnd")
class BootstrapClientWnd : public CWindowWnd, public INotifyUI
{
public:
	BootstrapClientWnd();
	BootstrapClientWnd(CConfig config);
	~BootstrapClientWnd();

public:
	virtual LPCTSTR GetWindowClassName() const;
	virtual void    Notify(TNotifyUI& msg);
	UINT GetClassStyle();
	virtual LRESULT HandleMessage(UINT uMsg, WPARAM wParam, LPARAM lParam);
	void OnFinalMessage(HWND hWnd);
	void Init();
	void OnPrepare();
	LRESULT OnCreate(UINT uMsg, WPARAM wParam, LPARAM lParam, BOOL& bHandled);

	LRESULT OnNcActivate(UINT uMsg, WPARAM wParam, LPARAM lParam, BOOL& bHandled);

	LRESULT OnNcCalcSize(UINT uMsg, WPARAM wParam, LPARAM lParam, BOOL& bHandled);
	LRESULT OnNcPaint(UINT uMsg, WPARAM wParam, LPARAM lParam, BOOL& bHandled);

	LRESULT OnNcHitTest(UINT uMsg, WPARAM wParam, LPARAM lParam, BOOL& bHandled);

	LRESULT OnSize(UINT uMsg, WPARAM wParam, LPARAM lParam, BOOL& bHandled);
	LRESULT OnGetMinMaxInfo(UINT uMsg, WPARAM wParam, LPARAM lParam, BOOL& bHandled);

	TCHAR * CombineFilePath(TCHAR * first, TCHAR * scend, TCHAR * out, int outsize);
	CHAR * Tchar2Char(TCHAR * src);
	CHAR* ConvertLPWSTRToLPSTR(LPWSTR lpwszStrIn);

private:
	void Start();

private:
	CPaintManagerUI m_pmUI;
	CButtonUI * m_pStartBtn;
	CProgressUI * m_pProgress;
	CHorizontalLayoutUI * m_pUrlHoriLayout;
	CButtonUI * m_pCopyBtn;
	CButtonUI * m_pMinBtn;
	CConfig m_config;
	CEditUI * m_pUrlEdit;

	CHttpClient * m_httpClient = NULL;
};

