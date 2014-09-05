#include "stdafx.h"
#include "PololuServo.h"

PololuServoContinuous::PololuServoContinuous(uint8_t channel, uint16_t initialPosition) : 
	PololuServo(channel, initialPosition), threadOn(true), increment(defaultIncrement)
{
	DWORD threadId;
	uint16_t correctedPosition;

	correctedPosition = checkPosition(initialPosition);
	targetPosition = correctedPosition;
	PololuServo::setPosition(correctedPosition);

	if ((setPositionMutex =	CreateMutex(NULL, FALSE, NULL)) == NULL) REPORT_ERROR;
	if((threadHandle = CreateThread(NULL, 0, setPositionContinuous, this, 0, &threadId)) == NULL) REPORT_ERROR;
}

PololuServoContinuous::~PololuServoContinuous()
{
	threadOn = false;
	WaitForSingleObject(setPositionContinuous, INFINITE);
	CloseHandle(threadHandle); 
	CloseHandle(setPositionMutex);
}

void PololuServoContinuous::stop()
{
	MUTEX_BLOCK(setPositionMutex, { targetPosition = PololuServo::getPosition(); });
}

void PololuServoContinuous::disable()
{
	MUTEX_BLOCK(setPositionMutex, { PololuServo::disable(); });
}

void PololuServoContinuous::setPosition(uint16_t microseconds)
{
	MUTEX_BLOCK(setPositionMutex, { targetPosition = checkPosition(microseconds); });
}

void PololuServoContinuous::setIncrement(uint16_t increment)
{
	MUTEX_BLOCK(setPositionMutex,
	{
		if (increment < minimumIncrement)
		{ increment = minimumIncrement; }
		else if (increment > maximumIncrement)
		{ increment = maximumIncrement; }
		this->increment = increment;
	});
}

uint16_t PololuServoContinuous::getPosition()
{
	uint16_t returnValue = 0;
	MUTEX_BLOCK(setPositionMutex, { returnValue = PololuServo::getPosition(); });
	return returnValue;
}

DWORD WINAPI PololuServoContinuous::setPositionContinuous(LPVOID lpParam)
{
	PololuServoContinuous* psc = (PololuServoContinuous*)lpParam;
	DWORD delay = psc->delay;
	uint16_t increment;
	uint16_t change;
	uint16_t position;
	uint16_t targetPosition;

	while (psc->threadOn) 
	{
		MUTEX_BLOCK(psc->setPositionMutex, 
		{
			increment = psc->increment;
			position = psc->PololuServo::getPosition();
			targetPosition = psc->targetPosition;

			if ( (targetPosition-(increment/2)) > position)
			{ change = increment; }
			else if ( (targetPosition+(increment/2)) < position)
			{ change = -increment; }
			else
			{ change = 0; }

			if (change != 0)
			{ 
				//cerr << (uint32_t)psc << ": locking on to " << (uint16_t)(change+psc->PololuServo::getPosition()) << endl;
				psc->PololuServo::setPosition(change+psc->PololuServo::getPosition()); 
			}
		});
		Sleep(delay);
	}

	cerr << "Thread in " << __FUNCTION__ << " has been terminated.\n";
	return FALSE;
}

uint16_t PololuServoContinuous::checkPosition(uint16_t position)
{
	if (position < minimum)
	{ position = minimum; }
	else if (position == maximum)
	{ position = maximum; }
	return position;
}