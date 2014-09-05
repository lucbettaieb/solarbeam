#ifndef I2C_H_
#define I2C_H_

#include <stdint.h>
#include <Windows.h>
#include "FPGAInterface.h"

class I2C
{
private:
	enum { NAK = 1 };
	DigitalWriteRegister& dout;
	DigitalReadRegister& din;
	DigitalDirectionRegister& ddir;
	uint32_t sclpin, sdapin;
	void setHigh(FPGARegister<uint32_t>& reg, uint32_t pin) { SET_PIN_HIGH(reg, pin); }
	void setLow(FPGARegister<uint32_t>& reg, uint32_t pin) { SET_PIN_LOW(reg, pin); }
public:	
	I2C(uint32_t sclpin, uint32_t sdapin) : 
		sclpin(sclpin), 
		sdapin(sdapin),
		dout(DOUT),
		din(DIN),
		ddir(DDIR) { startBus(); }
	~I2C() { }
	void start();
	void stop();
	uint8_t write(uint8_t data);
	uint8_t read(uint8_t ackbit);
	void startBus();
	bool isDevicePresent(uint8_t address);
	void writeLocation(uint8_t address, uint8_t reg, uint8_t value);
	uint8_t readLocation(uint8_t address, uint8_t reg);
};

#endif