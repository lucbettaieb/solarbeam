#ifndef MAESTROSERIAL_H_
#define MAESTROSERIAL_H_

#include <Windows.h>
#include <cstdint>
#include "FPGAInterface.h"

class MaestroSerial
{
private:
	const static int baudRate = 9600;
	const static char* portName;
	static short portNumber;
	HANDLE port;
	MaestroSerial();
	HANDLE openPort(char* portName, int baudrate);
	BOOL maestroGetPosition(HANDLE port, unsigned char channel, unsigned short* position);
	BOOL maestroSetTarget(HANDLE port, unsigned char channel, unsigned short target);
public:
	static MaestroSerial& getMaestroSerial()
	{
		static MaestroSerial ms;
		return ms;
	}
	static void setPortNumber(short port)
	{
		portNumber = port;
	}
	~MaestroSerial();
	unsigned short getPosition(unsigned char channel);
	void setPosition(unsigned char channel, unsigned short target);
};

#endif
