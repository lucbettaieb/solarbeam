/*+----------------------------------------------------------------------
 ||
 ||  Class NSC_A2L
 ||
 ||         Author:  Andrew Powell
 ||
 ||        Purpose:  The NSC_A2L class directly interacts with Arcus 
 ||						Technology's Performax USB driver which is 
 ||						necessary to operate the NSC-A2L 2-axis Servo 
 ||						Motor Driver. The PerformaxCom lib file is 
 ||						utilized within the NSC_A2L class's implementation
 ||						in order to use the PerformaxCom.dll. The 
 ||						PerformaxCom.dll is the library that communicates
 ||						with the Performax USB drivers. 
 ||						
 ||						Please refer the NSC-A2L_Manual_Rev_1.17.pdf for 
 ||						more information on how to control the NSC-A2L 
 ||						driver.
 ||
 ||  Inherits From:  None
 ||
 |+-----------------------------------------------------------------------
 ||
 ||      Constants:  X, Y, and BOTH - Refer to the X and Y axes of the 
 ||						Gimbal. BOTH refer to both axes at once 
 ||					OFF and ON - Refer to the power states of the servo
 ||						and are only used with the set_motorpower method
 ||					ABS and INC - Refer to the NSC_A2L's mode of 
 ||						operation and are only used with the set_mode
 ||						method
 ||					B9600, B19200, B38400, B57600, and B115200 - Refer to
 ||						the possible baudrates for transmission of 
 ||						commands between the host computer and the NSC_A2L
 ||
 ++-----------------------------------------------------------------------*/

#if !defined(NSC_A2L_H_)
#define NSC_A2L_H_

#include <iostream>
#include <stdint.h>
#include <string.h>
#include <Windows.h>
using std::cerr;
using std::endl;

/* accessing the Performax USB drivers using dynamically linked libraries */
extern "C" __declspec(dllimport) BOOL _stdcall fnPerformaxComWrite(IN HANDLE pHandle, LPVOID Buffer, DWORD dwNumBytesToWrite, OUT LPDWORD lpNumBytesWritten);
extern "C" __declspec(dllimport) BOOL _stdcall fnPerformaxComRead(IN HANDLE pHandle, LPVOID Buffer, DWORD dwNumBytesToRead, OUT LPDWORD lpNumBytesReturn);
extern "C" __declspec(dllimport) BOOL _stdcall fnPerformaxComClose(IN HANDLE pHandle);
extern "C" __declspec(dllimport) BOOL _stdcall fnPerformaxComSetTimeouts(IN DWORD dwReadTimeout, DWORD dwWriteTimeout);

extern "C" __declspec(dllimport) BOOL _stdcall fnPerformaxComGetNumDevices(OUT LPDWORD lpNumDevices);
extern "C" __declspec(dllimport) BOOL _stdcall fnPerformaxComGetProductString(IN DWORD dwNumDevices, OUT LPVOID lpDeviceString, IN DWORD dwOptions);	
extern "C" __declspec(dllimport) BOOL _stdcall fnPerformaxComOpen(IN DWORD dwDeviceNum, OUT HANDLE* pHandle);
extern "C" __declspec(dllimport) BOOL _stdcall fnPerformaxComSendRecv(IN HANDLE pHandle, 
													IN LPVOID wBuffer, 
													IN DWORD dwNumBytesToWrite,
													IN DWORD dwNumBytesToRead,
													OUT LPVOID rBuffer);

extern "C" __declspec(dllimport) BOOL _stdcall fnPerformaxComFlush(IN HANDLE pHandle);

class NSC_A2L
{
/* CONSTANTS */
public:
	enum { X=1, Y, BOTH };
	enum { OFF, ON };
	enum { ABS, INC };
	enum { B9600=1, B19200, B38400, B57600, B115200 };
	enum
	{
		MAX_SPEED = 10000,	// this may be 20000
		MIN_SPEED = 1000
	};
private:
	enum { BUFF_SIZE = 64 };
	enum 
	{ 
		D_HIGH_SPEED = MAX_SPEED, 
		D_LOW_SPEED = MIN_SPEED,
		D_ACCELERATION = 300,
		D_MODE = ABS
	};

private:
	HANDLE	m_hUSBDevice;
	char receive_buffer[BUFF_SIZE];
	int previous_sspdx, previous_sspdy;

public:
	// 1st level
	BOOL connect(IN int devicenumber);
	BOOL disconnect();
	NSC_A2L() : m_hUSBDevice(NULL), previous_sspdx(-1), previous_sspdy(-1) { }
	~NSC_A2L() { disconnect(); }
	BOOL send(IN const char* send_buffer);
	inline char* get_receive_buffer() { return receive_buffer; }
	BOOL flush();
	void waittill_motorposition_stop(int select = BOTH);

public:
	// 2nd level
	bool setup(IN int devicenumber);
	void set_devicenumber(IN int devicenumber);
	int get_devicenumber();
	void set_motorpower(IN BOOL on); // on = TRUE, motors are on; on = FALSE, motors are off
	void set_motorspeed(IN long highspeed = D_HIGH_SPEED, IN long lowspeed = D_LOW_SPEED, IN long accelerate = D_ACCELERATION, IN int select = BOTH); // 1 = x, y = 2, both = 3 // needs completion
	void set_motorspeed_abort(IN int select); // 1 = x, y = 2, both = 3
	void set_motorposition(IN long xposition = 0, IN long yposition = 0, IN int select = BOTH); // 1 = x, y = 2, both = 3
	void set_mode(IN int mode = D_MODE); // 1 = absolute, 2 = increment
	void set_baudrate(IN int select = B115200);
	void calibrate_motors(); 
	long get_motorposition(IN int select);
	long get_motorspeed_highspeed(IN int select);
	void set_scurve(IN bool on = false, IN int select = BOTH); // 1 = X, 2 = Y, 3 = BOTH
	void set_ontheflyspeed_window(IN int window = 1);
	
};

#endif

