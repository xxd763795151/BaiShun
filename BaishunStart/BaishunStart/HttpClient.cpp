#include "stdafx.h"
#include "HttpClient.h"


CHttpClient::CHttpClient(CConfig config)
{
	m_config = config;
}


CHttpClient::~CHttpClient()
{
}

int  CHttpClient::init() {
	WSADATA stWsa;
	if (WSAStartup(0x0202, &stWsa) != 0) {
		m_szErrorMsg = _T("http client init error");
		return -1;
	}

	if ((m_sock = socket(AF_INET, SOCK_STREAM, IPPROTO_TCP)) < 0)  {
		WSACleanup();
		m_szErrorMsg = _T("init socket error");
		return -1;
	}

	return 0;
}


bool CHttpClient::send(CHAR * szMsg) 
{
	if (::send(m_sock, szMsg, strlen(szMsg) + 1, 0) < 0) {
		m_szErrorMsg = _T("send error");
		return false;
	}
	return true;
}

void CHttpClient::recv(char * szRecvMsg, int length) {
	memset(szRecvMsg, 0, length);
	::recv(m_sock, szRecvMsg, length - 1, 0);
}

bool CHttpClient::connect() {
	if (m_bIsConnected) return m_bIsConnected;
	// connect
	SOCKADDR_IN addr = { 0 };
	addr.sin_family = AF_INET;
	addr.sin_addr.s_addr = inet_addr(m_config.TChar2Char(m_config.ServerAddress()));
	addr.sin_port = htons(m_config.Port());
	if (::connect(m_sock, (SOCKADDR*)&addr, sizeof(SOCKADDR)) != 0) {
		CloseSocket();
		m_szErrorMsg = _T("connect error");
		return false;
	}
	m_bIsConnected = true;
	return m_bIsConnected;
}

bool CHttpClient::ServerPortIsOpen() {
	//SOCKET sock;

	//SOCKADDR_IN addr = { 0 };
	//addr.sin_family = AF_INET;
	//addr.sin_addr.s_addr = inet_addr(m_config.TChar2Char(m_config.ServerAddress()));
	//addr.sin_port = htons(m_config.Port());

	//if ((sock = socket(AF_INET, SOCK_STREAM, IPPROTO_TCP)) < 0)  {
	//	WSACleanup();
	//	m_szErrorMsg = _T("init socket error");
	//	return true;
	//}
	//if (::connect(sock, (SOCKADDR*)&addr, sizeof(SOCKADDR)) == 0) {
	//	closesocket(sock);
	//	WSACleanup();
	//	m_szErrorMsg = _T("server port may be has alerady been occupied");
	//	return true;
	//}

	SOCKADDR_IN addr = { 0 };
	addr.sin_family = AF_INET;
	addr.sin_addr.s_addr = inet_addr(m_config.TChar2Char(m_config.ServerAddress()));
	addr.sin_port = htons(m_config.Port());

	if (::connect(m_sock, (SOCKADDR*)&addr, sizeof(SOCKADDR)) == 0) {
		CloseSocket();
		m_szErrorMsg = _T("server port may be has open");
		return true;
	}
	return false;


}

void CHttpClient::Clean() {
	if (m_sock != NULL) {
		closesocket(m_sock);
	}
	WSACleanup();
}

void CHttpClient::CloseSocket() {
	if (m_sock != NULL) {
		closesocket(m_sock);
		m_sock = NULL;
	}
}