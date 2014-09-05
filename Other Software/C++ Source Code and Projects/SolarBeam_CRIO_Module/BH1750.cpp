#include "BH1750.h"

void BH1750::writeFormat(uint8_t opcode)
{
	i2c.start();
	i2c.write(address | 0);
	i2c.write(opcode);
	i2c.stop();
}

uint16_t BH1750::readFormat()
{
	uint8_t highByte, lowByte;
	i2c.start();
	i2c.write(address | 1);
	highByte = i2c.read(0);
	lowByte = i2c.read(0);
	i2c.stop();
	return (highByte << 8) | lowByte;
}

uint16_t BH1750::read()
{
	writeFormat(CMD_CON_H_MODE);
	Sleep(READ_WAIT_PERIOD_MS);
	return readFormat();
}

EasyBH1750::EasyBH1750(uint32_t sdapin, uint32_t sclpin) : i2c(I2C(sclpin, sdapin)), BH1750(i2c), currentRead(0), threadOn(true)
{ 
	DWORD threadId;
	cerr << "Construct: EasyBH1750 pointer: " << (uint32_t)this << endl;
	if ((currentReadMutex =	CreateMutex(NULL, FALSE, NULL)) == NULL) REPORT_ERROR;
	if((readThreadHandle = CreateThread(NULL, 0, readThread, this, 0, &threadId)) == NULL) REPORT_ERROR;
}

EasyBH1750::~EasyBH1750()
{
	cerr << "Destroy: EasyBH1750 pointer: " << (uint32_t)this << endl;
	threadOn = false;
	WaitForSingleObject(readThreadHandle, INFINITE);
	CloseHandle(readThreadHandle); 
	CloseHandle(currentReadMutex);
}

bool EasyBH1750::isDevicePresent()
{
	bool returnValue = false;
	MUTEX_BLOCK(currentReadMutex, { returnValue = BH1750::isDevicePresent(); });
	return returnValue;
}

void EasyBH1750::powerOn() 
{
	MUTEX_BLOCK(currentReadMutex, { BH1750::powerOn(); });
}

void EasyBH1750::powerDown()
{
	MUTEX_BLOCK(currentReadMutex, { BH1750::powerDown(); });
}

uint16_t EasyBH1750::read()
{
	uint16_t returnValue = 0;
	MUTEX_BLOCK(currentReadMutex, { returnValue = currentRead; });
	return returnValue;
}

DWORD WINAPI EasyBH1750::readThread(LPVOID lpParam)
{
	EasyBH1750* easyBH1750 = (EasyBH1750*)lpParam;

	while (easyBH1750->threadOn) MUTEX_BLOCK(easyBH1750->currentReadMutex, 
	{ 
		easyBH1750->currentRead = easyBH1750->readLightSensor(); 
	});
	cerr << "thread killed in" << __FUNCTION__ << endl;

	return FALSE;
}