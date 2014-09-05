#include <cstring>
#include <cstdint>
#include <cstdio>
#include <iostream>
#include <Windows.h>
#include "FPGAInterface.h"
#include "I2C.h"
#include "BH1750.h"
#include "PWM.h"
#define CASE(number, function) case number: function; break;

using namespace std;

static uint32_t sclpin2 = PIN(13u);
static uint32_t sdapin2 = PIN(28u);

static void testDigitalIO();
static void testI2C();
static void testAnalogI();
static void testResponse();
static void testBH1750();
static void testSynchronization1();
static void testModulation();
static void testPWM();

int main(int argv, char* argc[])
{
	int input;

	// get input from user
	cout << "Select the test program you would like to run" << endl;
	(cin >> input).get();

	switch (input)
	{
	CASE(0, testDigitalIO());
	CASE(1, testAnalogI());
	CASE(2, testResponse());
	CASE(3, testI2C());
	CASE(4, testBH1750());
	CASE(5, testSynchronization1());
	CASE(6, testModulation());
	CASE(7, testPWM());
	}	

	// prevent the window from closing prematurely
	cin.get();
	return 0;
}

static void testDigitalIO()
{
	const unsigned int BUF_SIZE = 30;
	char buffer[BUF_SIZE];

	/* the purpose of this test program is to first test whether or not the LabVIEW-based
		library and FPGA classes work as intended */
	DigitalWriteRegister& dout = DOUT;
	DigitalReadRegister& din = DIN;
	DigitalDirectionRegister& ddir = DDIR;

	// first test the digital out register 
	cout << "Set all digital io pins as outputs" << endl;
	ddir = 0xFFFFFFFFu;

	cout << "Starting with the least significant pin, set each pin high" << endl;
	for (dout = 1; dout; dout <<= 1u) 
	{

		cout << "Current digital out value: " << hex << dout << endl;
		cout << "Current direction value: " << hex << ddir << endl;
		cin.get();
	}

	// now, set the digital io direction to input and check and see if the pins still 
	// are outputting digital highs
	cout << "Setting digital io to inputs" << endl;
	ddir = 0u;
	cin.get();

	// Finally, test the inputs
	while (true)
	{
		cout << "Current digital in value: " << hex << din << endl;
		cin.getline(buffer, BUF_SIZE);
		if (strcmp(buffer, "quit") == 0) break;
	}
}

static void testI2C()
{
	I2C i2c = I2C(PIN(10), PIN(11));

	while (true)
	{
		i2c.start();
		i2c.write(0x00);
		i2c.stop();
	}
}
	
static void testAnalogI() 
{
	AnalogReadRegisters& ain = AIN;
	const unsigned int BUF_SIZE = 30;
	char input[BUF_SIZE];
	int i;

	while (true)
	{
		cin.getline(input, BUF_SIZE);
		if (strcmp(input, "quit") == 0)
			break;
		for (i = 0; i < 8; i++)
			cout << "AIN" << i << ": " << AIN[i] << endl;
	}
}

static void testResponse()
{
	DigitalWriteRegister& dout = DOUT;
	DigitalDirectionRegister& ddir = DDIR;
	DigitalReadRegister& drr = DigitalReadRegister::getDigitalReadRegister();
	
	SET_PIN_HIGH(ddir, (PIN(8) | PIN(9) | PIN(10) | PIN(11) | PIN(12) | PIN(13)));
	SET_PIN_HIGH(dout, (PIN(8) | PIN(9) | PIN(10) | PIN(11) | PIN(12) | PIN(13)));
	//while (true)
	//{
	//	for (uint32_t i = 1; i; i <<= 1) 
	//		cout << (bool)(i & drr);
	//	cout << endl;
	//}
	Sleep(5000);
	//while(true) cout << std::hex << (drr & (PIN(8) | PIN(9) | PIN(10) | PIN(11) | PIN(12) | PIN(13))); 
	//SET_PIN_HIGH(ddir, PIN(13u) | PIN(28u));
	/*while (true)
	{
		SET_PIN_HIGH(dout, PIN(13u));
		SET_PIN_HIGH(dout, PIN(28u));
		SET_PIN_LOW(dout, PIN(13u));
		SET_PIN_LOW(dout, PIN(28u));
	}*/
}

static void testBH1750()
{
	uint32_t sclpin = PIN(29u);
	uint32_t sdapin = PIN(14u);
	uint32_t sclpin2 = PIN(13u);
	uint32_t sdapin2 = PIN(28u);
	EasyBH1750 ebh1 = EasyBH1750(PIN(8), PIN(9));
	EasyBH1750 ebh2 = EasyBH1750(PIN(10), PIN(11));
	EasyBH1750 ebh3 = EasyBH1750(PIN(12), PIN(13));
	while (true)
	{
		cout << std::hex << "ebh1: " << ebh1.read() << "\tebh2: " << ebh2.read() << "\tebh3: " << ebh3.read() << endl;
		//cout << std::hex << "ebh2: " << ebh2.read() << endl;
		//cout << "ebh2: " << ebh2.read() << endl;
	}
}

static void testSynchronization1()
{
	uint32_t sclpin = PIN(29u);
	uint32_t sdapin = PIN(14u);
	FPGAInterface& fi = FPGAI;

	fi.setDirectionDigital(sclpin2 | sdapin2);
	while (true)
	{
		fi.writeDigital(sclpin2 | sdapin2);
		fi.writeDigital(sclpin2);
		fi.writeDigital(0);
		fi.writeDigital(sclpin2);
		fi.writeDigital(sclpin2 | sdapin2);
	}
}

static void testModulation()
{
	ModulationEnableRegister& mer = ModulationEnableRegister::getModulationEnableRegister();
	ModulationTickRegisters& mtr = ModulationTickRegisters::getModulationTickRegisters();
	//mtr.setTicks(4000, 4000, 0, 0);
	mtr.setTicks(4000, 4000, 0, 0);
	mer |= PIN(0);

}

static void testPWM()
{
	int i;
	char input[20];
	uint8_t duty = 10, address = 1;
	PWM& pwm = getPWM();
	pwm.setPeriodMicroseconds(address, 20000);
	pwm.setDutyMicroseconds(address, 1000);
	pwm.enable(address, true);
	for (i = 1000; i < 2000; i += 100)
	{
		cout << dec << (uint16_t)i << endl;
		pwm.setDutyMicroseconds(address, i);
		cout << "enable register: " << (uint16_t)getModulationEnableRegister() << 
			"\nHigh: " << getModulationTickRegisters().getHighTick(address) << 
			"\nLow: " << getModulationTickRegisters().getLowTick(address) << endl;
		cin.get();
	}

	while (true)
	{
		cin.getline(input, 20);
		if (strcmp(input, "quit") == 0)
			break;
		pwm.setDutyMicroseconds(address, atoi(input));
		cout << "enable register: " << (uint16_t)getModulationEnableRegister() << 
			"\nHigh: " << getModulationTickRegisters().getHighTick(address) << 
			"\nLow: " << getModulationTickRegisters().getLowTick(address) << endl;
	}
}


