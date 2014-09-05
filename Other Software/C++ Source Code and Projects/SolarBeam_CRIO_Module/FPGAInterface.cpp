#include "FPGAInterface.h"
#include "SolarBeam_LabVIEW_CRIO_Module.h"
#include <iostream>
using std::cerr;
using std::endl;

#define ANALOG_INPUT_SIZE 8

void FPGAInterface::setup()
{
	if (FPGA != 0)
		return;
	errorcode = VISetup(&FPGA);

	if(((digitalDirection.mutex = CreateMutex(NULL, FALSE, NULL)) == NULL) ||
		((digitalOutput.mutex = CreateMutex(NULL, FALSE, NULL)) == NULL) ||
		((modulationEnable.mutex = CreateMutex(NULL, FALSE, NULL)) == NULL) ||
		((modulationTick.mutex = CreateMutex(NULL, FALSE, NULL)) == NULL)) 
		REPORT_ERROR;

	setDirectionDigital(digitalDirection.data);
	writeDigital(digitalOutput.data);
	enableModulation(modulationEnable.data);
}

void FPGAInterface::close()
{
	if (FPGA == 0)
		return;
	errorcode = VIClose(&FPGA);
	FPGA = 0;

	// destroy all the mutex locks
	CloseHandle(modulationTick.mutex);
	CloseHandle(modulationEnable.mutex);
	CloseHandle(digitalDirection.mutex);
	CloseHandle(digitalOutput.mutex);
}

int16_t* FPGAInterface::readAnalog()
{
	static int16_t analogInput[ANALOG_INPUT_SIZE];
	VIReadAnalog(&FPGA, analogInput, ANALOG_INPUT_SIZE);
	return analogInput;
}

uint32_t FPGAInterface::readDigital()
{
	return VIReadDigital(&FPGA);
}

void FPGAInterface::setDirectionDigital(uint32_t enableDigitalOutput)
{
	MUTEX_BLOCK(digitalDirection.mutex, 
	{
		digitalDirection.data = enableDigitalOutput;
		//cerr << std::hex << digitalDirection.data << endl;
		VISetDirectionDigital(&FPGA, enableDigitalOutput);
	});
}

void FPGAInterface::writeDigital(uint32_t digitalOutput)
{
	MUTEX_BLOCK(this->digitalOutput.mutex,
	{
		this->digitalOutput.data = digitalOutput;
		VIWriteDigital(&FPGA, digitalOutput);
	});
}

uint32_t FPGAInterface::getCurrentDigitalOutput() 
{ 
	uint32_t returnValue = 0;
	MUTEX_BLOCK(digitalOutput.mutex, {returnValue = digitalOutput.data; Sleep(1);});
	return returnValue; 
}

uint32_t FPGAInterface::getCurrentDigitalDirection() 
{ 
	uint32_t returnValue = 0;
	MUTEX_BLOCK(digitalDirection.mutex, {returnValue = digitalDirection.data; Sleep(1);});
	return returnValue; 
}

void FPGAInterface::enableModulation(int8_t ModulationEnable)
{
	MUTEX_BLOCK(modulationEnable.mutex, 
	{
		modulationEnable.data = ModulationEnable;
		VIEnableModulation(&FPGA, ModulationEnable);
	});
}

void FPGAInterface::writeModulationTick(uint64_t ModulationHighTick, uint64_t ModulationLowTick, uint32_t address, uint32_t address2)
{
	static const uint64_t PIN_CHANGE_PERIOD = 4800; //5320 + 1320
	MUTEX_BLOCK(modulationTick.mutex,
	{
		//ModulationHighTick -= (ModulationHighTick > PIN_CHANGE_PERIOD) ? PIN_CHANGE_PERIOD : 0;
		//ModulationLowTick -= (ModulationLowTick > PIN_CHANGE_PERIOD) ? PIN_CHANGE_PERIOD : 0;
		modulationTick.high[address] = ModulationHighTick;
		modulationTick.low[address2] = ModulationLowTick;
		VIWriteModulationTick(&FPGA, modulationTick.high, modulationTick.low, modulationTickLen, modulationTickLen);
	});
}

int8_t FPGAInterface::getCurrentModulationEnable()
{
	int8_t returnValue = 0;
	MUTEX_BLOCK(modulationEnable.mutex, { returnValue = modulationEnable.data; });
	return returnValue;
}

uint64_t FPGAInterface::getCurrentModulationHighTick(uint32_t address)
{
	uint64_t returnValue = 0;
	MUTEX_BLOCK(modulationTick.mutex, { returnValue = modulationTick.high[address]; });
	return returnValue;
}

uint64_t FPGAInterface::getCurrentModulationLowTick(uint32_t address)
{
	uint64_t returnValue = 0;
	MUTEX_BLOCK(modulationTick.mutex, { returnValue = modulationTick.low[address]; });
	return returnValue;
}


