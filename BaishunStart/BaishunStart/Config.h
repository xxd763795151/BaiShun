#pragma once
class CConfig
{
public:
	CConfig();
	~CConfig();

	int init();
	TCHAR * ErrorMsg();

	inline TCHAR * SkinPath() { return m_szSkinPath; }
	inline TCHAR * ServerAddress() { return m_szAddr; }
	inline TCHAR * ExePath() { return m_szExePath; }
	inline int Port() { return m_iPort; }
	char * TChar2Char(TCHAR * src);

private:
	TCHAR  m_szSkinPath[0xff];
	TCHAR  m_szAddr[0xff];
	int m_iPort;
	TCHAR *  m_szErrorMsg;
	TCHAR m_szExePath[0xff];
};

