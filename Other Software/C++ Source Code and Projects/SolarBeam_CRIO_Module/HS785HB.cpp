#include "HS785HB.h"

HS785HB::HS785HB(uint8_t pwmAddress, uint16_t netDutyMicroseconds) : pwm(getPWM()), pwmAddress(pwmAddress),
	netDutyMicroseconds(netDutyMicroseconds)
{
	pwm.setPeriodMicroseconds(pwmAddress, periodMicroseconds);
	pwm.setDutyMicroseconds(pwmAddress, netDutyMicroseconds);
}

void HS785HB::setPosition(uint16_t microseconds) 
{ 
	//cerr << "PositionMicro: " << microseconds << endl;
	positionMicroseconds = microseconds;
	pwm.setDutyMicroseconds(pwmAddress, microseconds); 
}

void HS785HB::setPositionDegrees(int16_t degrees) 
{ 
	setPosition((uint32_t)(degrees*positiveDutyMicroseconds/positiveDegrees)+netDutyMicroseconds); 
	//cerr << "Position: " <<  (uint32_t)(degrees*positiveDutyMicroseconds/positiveDegrees)+netDutyMicroseconds << endl;
}

uint16_t HS785HB::getPositionDegrees()
{
	return (((uint32_t)getPosition()-netDutyMicroseconds)*positiveDegrees/positiveDutyMicroseconds);
}

