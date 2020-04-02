#pragma once
#include "stdafx.h"
#define BOOTSTRAP_WIN_CLASS _T("BootstrapClientWnd")
class BootstrapClientWnd : public CWindowWnd, public INotifyUI
{
public:
	BootstrapClientWnd();
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

private:
	void Start();

private:
	CPaintManagerUI m_pmUI;
	CButtonUI * m_pStartBtn;
	CProgressUI * m_pProgress;
	CHorizontalLayoutUI * m_pUrlHoriLayout;
	CButtonUI * m_pCopyBtn;
	CButtonUI * m_pMinBtn;
};

