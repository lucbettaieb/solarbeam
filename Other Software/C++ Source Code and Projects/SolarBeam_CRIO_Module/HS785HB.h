#ifndef HS785HB_H_
#define HS785HB_H_

#include <cstdint>
#include "PWM.h"

class HS785HB
{
private:
	static const uint16_t positiveDegrees = 630;
	static const uint16_t positiveDutyMicroseconds = 400;
	static const uint16_t defaultNetDutyMicroseconds = 1500;
	static const uint16_t periodMicroseconds = 20000;
	PWM& pwm;
	uint8_t pwmAddress;
	uint16_t netDutyMicroseconds;
	uint16_t positionMicroseconds;
public:
	HS785HB(uint8_t pwmAddress, uint16_t netDutyMicroseconds = defaultNetDutyMicroseconds);
	void enable(bool turnOn) { pwm.enable(pwmAddress, turnOn); }
	void setPosition(uint16_t microseconds);
	void setPositionDegrees(int16_t degrees);
	uint16_t getPosition() { return positionMicroseconds; }
	uint16_t getPositionDegrees();
};

#endif