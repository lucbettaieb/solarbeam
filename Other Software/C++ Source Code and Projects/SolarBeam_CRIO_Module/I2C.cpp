#include "I2C.h"

void I2C::start()
{	
	setHigh(ddir, sclpin);
	setHigh(dout, sclpin); 
	setHigh(ddir, sdapin);
	setHigh(dout, sdapin);
	setLow(dout, sdapin);
	setLow(dout, sclpin);
}

void I2C::stop()
{
	setHigh(dout, sclpin);
	setHigh(dout, sdapin);
	setLow(ddir, sclpin);
	setLow(ddir, sdapin);
}

uint8_t I2C::write(uint8_t data)
{
	uint8_t mask;
	uint8_t ackbit = 0;
	for (mask = 0x80; mask; mask >>= 1)
	{
		if (GET_PIN_STATE(mask, data)) { setHigh(dout, sdapin); }
		else			 { setLow(dout, sdapin); }
		setHigh(dout, sclpin); 
		setLow(dout, sclpin);
	}
	setLow(ddir, sdapin);
	setHigh(dout, sclpin); 
	if (GET_PIN_STATE(din, sdapin))	{ ackbit = 1; }
	setLow(dout, sclpin);
	setLow(dout, sdapin);
	setHigh(ddir, sdapin);
	return ackbit;
}

uint8_t I2C::read(uint8_t ackbit)
{
	uint8_t data = 0;
	uint8_t mask;
	setLow(ddir, sdapin);
	for (mask = 0x80; mask; mask >>= 1)
	{
		setHigh(dout, sclpin);
		if (GET_PIN_STATE(din, sdapin)) data |= mask;
		setLow(dout, sclpin);
	}
	if (ackbit) { setHigh(dout, sdapin); }
	else		{ setLow(dout, sdapin); }
	setHigh(ddir, sdapin);
	setHigh(dout, sclpin);
	setLow(dout, sclpin);
	setLow(dout, sdapin);
	return data;
}

void I2C::startBus()
{
	uint8_t i;
	setHigh(dout, sclpin);
	setHigh(ddir, sclpin);
	setLow(ddir, sdapin);
	for (i = 0; i < 9; i++)
	{
		setLow(dout, sclpin);
		setHigh(dout, sclpin);
		if (GET_PIN_STATE(din, sdapin))
			return;
	}
}

bool I2C::isDevicePresent(uint8_t address)
{
	uint8_t ackbit = 0;
	start();
	ackbit = write(address | 0);
	stop();
	if (ackbit) return true;
	return false;
}

void I2C::writeLocation(uint8_t address, uint8_t reg, uint8_t value)
{
	start();
	write(address);
	write(reg);
	write(value);
	stop();
}

uint8_t I2C::readLocation(uint8_t address, uint8_t reg)
{
	uint8_t value;
	start();
	write(address | 0);
	write(reg);
	stop();
	start();
	write(address | 1);
	value = read(NAK);
	stop();
	return value;
}