#include "stdafx.h"
#include "Config.h"

#include "stdafx.h"
#include <Shlwapi.h>
#pragma comment (lib, "shlwapi.lib")
#define CONFIG_PATH_W _T("conf\\client.ini")
#define CONFIG_PATH_A "conf\\client.ini"
#ifdef _UNICODE
#	define CONFIG_PATH CONFIG_PATH_W
#else
#	define CONFIG_PATH CONFIG_PATH_A
#endif
CConfig::CConfig()
{
}


CConfig::~CConfig()
{
}

int CConfig::init() {
	::GetModuleFileName(NULL, m_szExePath, 0xff);
	(_tcsrchr(m_szExePath, _T('\\')))[1] = 0;
	TCHAR szExePath[0xff] = { 0 };
	lstrcat(szExePath, m_szExePath);
	lstrcat(szExePath, CONFIG_PATH);
	if (!PathFileExists(m_szExePath)) {
		m_szErrorMsg = _T("客户端配置文件不存在: ")CONFIG_PATH;
		return -1;
	}
	try
	{
		::GetPrivateProfileString(_T("server"), _T("address"), _T("127.0.0.1"), m_szAddr, 0xff, szExePath);
		m_iPort = ::GetPrivateProfileInt(_T("server"), _T("port"), 8080, szExePath);
		::GetPrivateProfileString(_T("skin"), _T("path"), _T("../skin"), m_szSkinPath, 0xff, szExePath);
	}
	catch (TCHAR* e)
	{
		m_szErrorMsg = e;
		return -1;
	}

	return 0;
}


TCHAR * CConfig::ErrorMsg() {
	if (m_szErrorMsg == NULL) {
		m_szErrorMsg = _T("no init error!");
	}
	return m_szErrorMsg;
}

char * CConfig::TChar2Char(TCHAR * src) {
#ifdef UNICODE
	LPSTR pszOut = NULL;
	if (src != NULL)
	{
		int nInputStrLen = wcslen(src);

		// Double NULL Termination
		int nOutputStrLen = WideCharToMultiByte(CP_ACP, 0, src, nInputStrLen, NULL, 0, 0, 0) + 2;
		pszOut = new char[nOutputStrLen];

		if (pszOut)
		{
			memset(pszOut, 0x00, nOutputStrLen);
			WideCharToMultiByte(CP_ACP, 0, src, nInputStrLen, pszOut, nOutputStrLen, 0, 0);
		}
	}
	return pszOut;
#else
	return src;
#endif
}