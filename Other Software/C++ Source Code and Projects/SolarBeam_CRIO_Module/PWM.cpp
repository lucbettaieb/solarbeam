#include "PWM.h"

void PWM::Signals::enable(bool turnOn) 
{ 
	if (turnOn && period > 0)
	{
		SET_PIN_HIGH(mer, PIN(address));
		return;
	}
	SET_PIN_LOW(mer, PIN(address));
}

PWM::PWM() : signals(NULL) 
{ 
	uint32_t i;
	signals = new Signals[getSignalTotal()]; 
	for (i = 0; i < getSignalTotal(); i++) 
		signals[i].setAddress(i);
}

PWM::~PWM()
{
	if (signals != NULL)
		delete signals;
}