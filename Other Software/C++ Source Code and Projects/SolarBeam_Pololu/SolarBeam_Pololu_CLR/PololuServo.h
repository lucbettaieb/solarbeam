#ifndef POLOLUSERVO_H_
#define POLOLUSERVO_H_

#include <cstdint>
#include <iostream>
#include <Windows.h>
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

class PololuSingleton 
{
private:
	PololuSingleton() { SolarBeam_Pololu::TryToReconnect(); }
public:
	static PololuSingleton& getPololuSingleton()
	{
		static PololuSingleton ps;
		return ps;
	}

	~PololuSingleton() { SolarBeam_Pololu::TryToDisconnect(); }
	void setPostion(uint8_t channel, uint16_t position) { SolarBeam_Pololu::SetTarget(channel, position << 2); } 
};

class PololuServo
{
protected:
	const static uint16_t initialPosition = 1500;
	const static uint16_t minimum = 1000;
	const static uint16_t maximum = 2000;
private:
	PololuSingleton& ps;
	uint8_t channel;
	uint16_t position;
public:
	PololuServo(uint8_t channel, uint16_t initialPosition = initialPosition) : ps(PololuSingleton::getPololuSingleton()), channel(channel), position(initialPosition)
	{
		setPosition(position);
	}

	~PololuServo() 
	{
		disable();
	}

	void disable()
	{
		ps.setPostion(channel, 0);
	}

	void setPosition(uint16_t microseconds)
	{
		position = microseconds;
		ps.setPostion(channel, microseconds);
	}

	uint16_t getPosition()
	{
		return position;
	}
};

class PololuServoContinuous : PololuServo
{
private:
	const static DWORD delay = 100; // in milliseconds
	const static uint16_t minimumIncrement = 0;
	const static uint16_t maximumIncrement = 100;
	const static uint16_t defaultIncrement = 10;
	HANDLE setPositionMutex;
	HANDLE threadHandle;
	volatile bool threadOn;
	uint16_t targetPosition;
	uint16_t increment;
	static DWORD WINAPI setPositionContinuous(LPVOID lpParam);
	uint16_t checkPosition(uint16_t position);
public:
	PololuServoContinuous(uint8_t channel, uint16_t initialPosition = initialPosition);
	~PololuServoContinuous();
	void stop();
	void disable();
	void setPosition(uint16_t microseconds);
	void setIncrement(uint16_t increment);
	uint16_t getPosition();
};

#endif