#ifndef PWM_H_
#define PWM_H_

#include <cstdint>
#include "FPGAInterface.h"

class PWM
{
private:
	class Signals
	{
	private:
		static const uint16_t dutyMax = 10000u;
		static const uint64_t microsecondsPerSecond = 1000000u;
		ModulationEnableRegister& mer;
		ModulationTickRegisters& mtr;
		uint64_t period; // in ticks
		uint8_t address;
		uint64_t getClock() { return FPGAInterface::getClock(); }
		uint64_t convertFromMicrosecondsToTicks(uint64_t microseconds) { return getClock()/microsecondsPerSecond*microseconds; }

	public:
		Signals() : mer(MER), mtr(MTR), period(0), address(0) {}
		void enable(bool turnOn);
		void setDuty(uint64_t high) { mtr.setTicks(high, period-high, address, address); }
		void setDutyMicroseconds(uint64_t high) { setDuty(convertFromMicrosecondsToTicks(high)); }
		void setPeriod(uint64_t period) { this->period = period; }
		void setPeriodMicroseconds(uint64_t period) { setPeriod(convertFromMicrosecondsToTicks(period)); }
		void setAddress(uint8_t address) { this->address = address; }
	}* signals;
	PWM();
	uint32_t getSignalTotal() { return FPGAInterface::getModulationTickLen(); }

public:
	~PWM();
	static PWM& getPWM()
	{
		static PWM pwm;
		return pwm;
	}
	void enable(uint8_t address, bool turnOn) { signals[address].enable(turnOn); }
	void setDuty(uint8_t address, uint64_t high) { signals[address].setDuty(high); }
	void setDutyMicroseconds(uint8_t address, uint64_t high) { signals[address].setDutyMicroseconds(high); }
	void setPeriod(uint8_t address, uint64_t period) { signals[address].setPeriod(period); }
	void setPeriodMicroseconds(uint8_t address, uint64_t period) { signals[address].setPeriodMicroseconds(period); }
};

inline PWM& getPWM() { return PWM::getPWM(); }

#endif