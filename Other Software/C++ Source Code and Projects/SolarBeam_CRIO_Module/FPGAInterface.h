#ifndef FPGAINTERFACE_H_
#define FPGAINTERFACE_H_

#include <cstdint>
#include <Windows.h>
#include <iostream>
using std::cerr;
using std::endl;

#define REPORT_ERROR {cerr << "Error in file: " << __FILE__ "\nin function: " << __FUNCTION__ << "\nat line: " << __LINE__ << endl; exit(0);}
#define MUTEX_BLOCK(mutex, commands)\
	{\
		switch (WaitForSingleObject((mutex), INFINITE))\
		{\
		case WAIT_OBJECT_0:\
			__try { commands }\
			__finally {if (!ReleaseMutex((mutex))) REPORT_ERROR; }\
			break;\
		case WAIT_ABANDONED: REPORT_ERROR;\
		}\
	}


class FPGAInterface
{
private:
	static const uint32_t modulationTickLen = 4;
	static const uint64_t clock = 40000000;
	uintptr_t FPGA;
	int32_t errorcode;
	struct { uint32_t data; HANDLE mutex; } digitalOutput;
	struct { uint32_t data; HANDLE mutex; } digitalDirection;
	struct { int8_t data; HANDLE mutex; } modulationEnable;
	struct { uint64_t high[modulationTickLen]; uint64_t low[modulationTickLen]; HANDLE mutex; } modulationTick;
	FPGAInterface() : FPGA(0), errorcode(0) { setup(); };

public:
	static FPGAInterface& getFPGAInterface()
	{
		static FPGAInterface fi;
		return fi;
	}
	~FPGAInterface() { close(); }
	void setup();
	void close();
	int16_t* readAnalog();
	uint32_t readDigital();
	void setDirectionDigital(uint32_t enableDigitalOutput);
	void writeDigital(uint32_t digitalOutput);
	void enableModulation(int8_t ModulationEnable);
	void writeModulationTick(uint64_t ModulationHighTick, uint64_t ModulationLowTick, uint32_t address, uint32_t address2);
	uint32_t getCurrentDigitalOutput();
	uint32_t getCurrentDigitalDirection();
	int8_t getCurrentModulationEnable();
	uint64_t getCurrentModulationHighTick(uint32_t address);
	uint64_t getCurrentModulationLowTick(uint32_t address);
	static uint64_t getClock() { return clock; }
	static uint32_t getModulationTickLen() { return modulationTickLen; }
};

template <class T>
class FPGARegister
{
protected:
	FPGAInterface& fi;
	HANDLE registerMutex;
	HANDLE registerMutex2;
	FPGARegister();
	virtual T getRegister() { return 0; }
	virtual T setRegister(T value) { return 0; }

public:
	operator T();
	T operator=(T value);
	T operator&=(T value);
	T operator|=(T value);
	T operator<<=(T amountToShift);
	T operator>>=(T amountToShift);
	~FPGARegister() { CloseHandle(registerMutex); CloseHandle(registerMutex2); cerr << "Reached: " << __FUNCTION__ << endl; }
};

template <class T>
FPGARegister<T>::FPGARegister() : fi(FPGAInterface::getFPGAInterface())
{
	if (((registerMutex = CreateMutex(NULL, FALSE, NULL)) == NULL) ||
		((registerMutex2 = CreateMutex(NULL, FALSE, NULL)) == NULL)) 
		REPORT_ERROR;
}

template <class T>
FPGARegister<T>::operator T() 
{ 
	T returnValue = 0;
	MUTEX_BLOCK(registerMutex, returnValue = getRegister(););
	return returnValue; 
}

template <class T>
T FPGARegister<T>::operator=(T value) 
{ 
	T returnValue = 0;
	MUTEX_BLOCK(registerMutex, returnValue = setRegister(value););
	return returnValue; 
}

template <class T>
T FPGARegister<T>::operator&=(T value) 
{ 
	T returnValue = 0;
	MUTEX_BLOCK(registerMutex2, returnValue = (*this = (*this & value)););
	return returnValue; 
}

template <class T>
T FPGARegister<T>::operator|=(T value) 
{ 
	uint32_t returnValue = 0;
	MUTEX_BLOCK(registerMutex2, returnValue = (*this = (*this | value)););
	return returnValue; 
}

template <class T>
T FPGARegister<T>::operator<<=(T amountToShift) 
{ 
	T returnValue = 0;
	MUTEX_BLOCK(registerMutex2, returnValue = (*this = (*this << amountToShift)););
	return returnValue; 
}

template <class T>
T FPGARegister<T>::operator>>=(T amountToShift) 
{ 
	T returnValue = 0;
	MUTEX_BLOCK(registerMutex2, returnValue = (*this = (*this >> amountToShift)););
	return returnValue; 
}

class AnalogReadRegisters
{
protected:
	FPGAInterface& fi;
	AnalogReadRegisters() : fi(FPGAInterface::getFPGAInterface()) {}
public:
	static AnalogReadRegisters& getAnalogReadRegisters()
	{
		static AnalogReadRegisters arr;
		return arr;
	}
	int16_t operator[](uint8_t index) { return fi.readAnalog()[index]; }
};

class DigitalReadRegister : public FPGARegister<uint32_t>
{
private:
	DigitalReadRegister() : FPGARegister() {}
protected:
	uint32_t getRegister() { return fi.readDigital(); }
	uint32_t setRegister(uint32_t value) { return (*this); }
public:
	static DigitalReadRegister& getDigitalReadRegister()
	{
		static DigitalReadRegister drr;
		return drr;
	}
};

class DigitalWriteRegister : public FPGARegister<uint32_t>
{
private:
	DigitalWriteRegister() : FPGARegister() {}
protected:
	uint32_t getRegister() { return fi.getCurrentDigitalOutput(); }
	uint32_t setRegister(uint32_t value) { fi.writeDigital(value); return value; }
public:
	static DigitalWriteRegister& getDigitalWriteRegister()
	{
		static DigitalWriteRegister dwr;
		return dwr;
	}
	uint32_t operator=(uint32_t value) { return FPGARegister::operator=(value); }
};

class DigitalDirectionRegister : public FPGARegister<uint32_t>
{
private:
	DigitalDirectionRegister() : FPGARegister() {}
protected:
	uint32_t getRegister() { return fi.getCurrentDigitalDirection(); }
	uint32_t setRegister(uint32_t value) { fi.setDirectionDigital(value); return value; }
public:
	static DigitalDirectionRegister& getDigitalDirectionRegister()
	{
		static DigitalDirectionRegister ddr;
		return ddr;
	}
	uint32_t operator=(uint32_t value) { return FPGARegister::operator=(value); }
};

class ModulationEnableRegister : public FPGARegister<int8_t>
{
private:
	ModulationEnableRegister() : FPGARegister() {}
protected:
	int8_t getRegister() { return fi.getCurrentModulationEnable(); }
	int8_t setRegister(int8_t value) { fi.enableModulation(value); return value; }
public:
	static ModulationEnableRegister& getModulationEnableRegister()
	{
		static ModulationEnableRegister mer;
		return mer;
	}
	uint32_t operator=(uint32_t value) { return FPGARegister::operator=(value); }
};

class ModulationTickRegisters
{
protected:
	FPGAInterface& fi;
	ModulationTickRegisters() : fi(FPGAInterface::getFPGAInterface()) {}

public:
	static ModulationTickRegisters& getModulationTickRegisters()
	{
		static ModulationTickRegisters mtr;
		return mtr;
	}
	void setTicks(uint64_t highTick, uint64_t lowTick, uint32_t address, uint32_t address2)
	{
		fi.writeModulationTick(highTick, lowTick, address, address2);
	}
	uint64_t getHighTick(uint32_t address) { return fi.getCurrentModulationHighTick(address); }
	uint64_t getLowTick(uint32_t address) { return fi.getCurrentModulationLowTick(address); }
};

inline FPGAInterface&				getFPGAInterface()				{ return FPGAInterface::getFPGAInterface(); }
inline DigitalWriteRegister&		getDigitalWriteRegister()		{ return DigitalWriteRegister::getDigitalWriteRegister(); }
inline DigitalReadRegister&			getDigitalReadRegister()		{ return DigitalReadRegister::getDigitalReadRegister(); }
inline DigitalDirectionRegister&	getDigitalDirectionRegister()	{ return DigitalDirectionRegister::getDigitalDirectionRegister(); }
inline AnalogReadRegisters&			getAnalogReadRegisters()		{ return AnalogReadRegisters::getAnalogReadRegisters(); }
inline ModulationEnableRegister&	getModulationEnableRegister()	{ return ModulationEnableRegister::getModulationEnableRegister(); }
inline ModulationTickRegisters&		getModulationTickRegisters()	{ return ModulationTickRegisters::getModulationTickRegisters(); }

template <class T> inline T			setPinHigh(FPGARegister<T>& reg, T pin)		{ return reg |= pin; }
template <class T> inline T			setPinLow(FPGARegister<T>& reg, T pin)		{ return reg &= ~pin; }
template <class T> inline T			getPinState(FPGARegister<T>& reg, T pin)	{ return reg & pin; }

#define FPGAI	FPGAInterface::getFPGAInterface()
#define DOUT	DigitalWriteRegister::getDigitalWriteRegister()
#define DIN		DigitalReadRegister::getDigitalReadRegister()
#define DDIR	DigitalDirectionRegister::getDigitalDirectionRegister()
#define AIN		AnalogReadRegisters::getAnalogReadRegisters()
#define MER		ModulationEnableRegister::getModulationEnableRegister()
#define MTR		ModulationTickRegisters::getModulationTickRegisters()

#define SET_PIN_HIGH(reg, pin)	((reg) |= (pin))
#define SET_PIN_LOW(reg, pin)	((reg) &= ~(pin))
#define GET_PIN_STATE(reg, pin)	((reg) & (pin))
#define PIN(pin)				(1 << (pin))



#endif