#pragma once
#include <winsock.h>
#include "Config.h"
#pragma comment (lib, "ws2_32.lib")
class CHttpClient
{
public:
	CHttpClient(CConfig config);
	~CHttpClient();

	int  init();
	bool connect();
	bool send(CHAR * szMsg);
	void  recv(char * szRecvMsg, int length);

	bool ServerPortIsOpen();
	void Clean();
	inline TCHAR * ErrorMsg() {
		if (m_szErrorMsg == NULL) {
			m_szErrorMsg = _T("no error");
		}
		return m_szErrorMsg;
	}


private:
	bool m_bIsConnected;
	SOCKET m_sock;
	TCHAR * m_szErrorMsg;
	CConfig m_config;


private:
	void CloseSocket();
};

