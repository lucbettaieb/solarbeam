#ifndef BH1750_H_
#define BH1750_H_

#include "I2C.h"
#include <stdint.h>
#include <Windows.h>

class BH1750
{
private:
	enum { DEFAULT_ADDRESS = 0x23u }; // or, the default address might be 0x23u
	enum { CMD_POWER_ON = 0x1u, CMD_POWER_DOWN = 0x0u, CMD_CON_H_MODE = 0x10u };
	enum { READ_WAIT_PERIOD_MS = 180 };
	I2C& i2c;
	uint8_t address;
	void writeFormat(uint8_t opcode);
	uint16_t readFormat();
public:
	BH1750(I2C& i2c, uint8_t address = DEFAULT_ADDRESS) : i2c(i2c), address(address << 1) { }
	~BH1750() { }
	bool isDevicePresent() { return i2c.isDevicePresent(address); }
	void powerOn() { writeFormat(CMD_POWER_ON); }
	void powerDown() { writeFormat(CMD_POWER_DOWN); }
	uint16_t read();
};

class EasyBH1750 : public BH1750
{
private:
	I2C i2c;
	uint16_t currentRead;
	volatile bool threadOn;
	HANDLE currentReadMutex;
	HANDLE readThreadHandle;
	uint16_t readLightSensor() { return BH1750::read(); }
	static DWORD WINAPI readThread(LPVOID lpParam);

public:
	EasyBH1750(uint32_t sdapin, uint32_t sclpin);
	~EasyBH1750();
	bool isDevicePresent();
	void powerOn();
	void powerDown();
	uint16_t read();
};

#endif
