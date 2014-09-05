#include "NSC_A2L.h"
#include <stdio.h>
#include <math.h>

// changeable definitions
#define CALIBRATION_TOLERATION 5

#define GET_MOTORPOSITION_FAILCODE 36001
#define GET_MOTORSPEED_HIGHSPEED -1

// commands
#define CMD_EO			"EO"	// power motors
#define CMD_DN			"DN"	// set device number
#define CMD_DB			"DB"	// set the baudrate (refer to data sheet)
#define CMD_STORE		"STORE"	// store the device number
#define CMD_ABORT		"ABORT" // stop all movements!
#define CMD_X			"X"		// set motor x's position
#define CMD_Y			"Y"		// set motor y's position
#define CMD_HSPD		"HSPD"	// set the high speed
#define CMD_LSPD		"LSPD"	// set the low speed
#define CMD_ACC			"ACC"	// set the acceleration
#define CMD_ABS			"ABS"	// set driver to absolute mode
#define CMD_INC			"INC"	// set driver to increment mode
#define CMD_MST			"MST"	// return status of stepper motor (either x or y)  0 bit is accel - 1 bit decel - bit 2 is constant speed 
#define CMD_P			"P"		// either return of set position of y axis (needs X or Y)
#define CMD_H_PLUS		"H+"	// homes motors (from the positive direction?)
#define CMD_H_NEG		"H-"	// homes motors (from the negative direction?)
#define CMD_J_PLUS		"J+"	// homes motors to the maximum positive direction for both axes
#define CMD_J_NEG		"J-"	// homes motors to the maximum negative direction for both axes
#define CMD_E			"E"		// returns a motor's status (needs X or Y)
#define CMD_ERR_MOVING	"?Moving" // Moving error message
#define CMD_MM			"MM"	// return motor mode (0 == ABS), (1 == INC)
#define CMD_SCV			"SCV"	// used to either set and get whether the s-curve mode in on or off
#define CMD_SSPD		"SSPD"	// set the on-the-fly speed
#define CMD_SSPDM		"SSPDM"	// set the on-the-fly speed window (can range from 1 to 9; refer the manual!)


// other definitions
#define FALSE		0
#define TRUE		1
#define FALSE		0
#define NULL		0
#define TAG			"2ED"

/*---------------------------------------------------------------------
|  Method connect
|
|  Purpose:  The connect method causes the host computer to establish 
|	a connection with a USB-connected NCS-A2L Driver with the device 
|	number specified as the method's only parameter.
|
|  Parameters:
|      IN int devicenumber -- The devicenumber parameter corresponds 
|		to the NSC-A2L's device number, which is necessary to 
		differentiate between multiple NSC-A2L Driver modules.
|
|  Returns:  The connect method will always return the constants 
|	TRUE or FALSE, which respectively are 1 and 0. TRUE always 
	indicates a successful connection, whereas FALSE always indicates 
	a failed connection. If the connect method returns FALSE, an error 
	will be outputted to the console, describing the reason for the 
	connection's failure.
*-------------------------------------------------------------------*/
BOOL NSC_A2L::connect(IN int devicenumber)
{
	int status;
	DWORD total_usbdevices, i;
	enum { DEVICE_NUMBER_NOT_FOUND=100 };
	DWORD dwDeviceNum = DEVICE_NUMBER_NOT_FOUND;
	char _devicestring[BUFF_SIZE];

	// check if a connection is already made
	if (m_hUSBDevice != NULL)
	{
		cerr << "Connection already made\n";
		return FALSE;
	}

	// look for the correct device
	fnPerformaxComGetNumDevices(&total_usbdevices);
	for (i = 0; i < total_usbdevices; i++)
	{
		fnPerformaxComGetProductString(i, _devicestring, 0);

		// once the correct device is found, return the correct index
		if (atoi(&_devicestring[3]) == devicenumber) dwDeviceNum = i;
	}
	
	// if the correct device number is not found, quit
	if (dwDeviceNum == DEVICE_NUMBER_NOT_FOUND)
	{
		cerr << "Device number not found\n";
		return FALSE;
	}

	// make the connection
	if (fnPerformaxComSetTimeouts(10000, 10000))
	{
		status = fnPerformaxComOpen(dwDeviceNum, &m_hUSBDevice);
		if (status == -1 || status == 0)
		{
			cerr << "Error opening device " TAG << dwDeviceNum << ". Reset hardware and try again." << endl;
			return FALSE;
		}
		else
		{
			return TRUE;
		}
	}
	else
	{
		cerr << "Error to set timeouts. Reset hardware and try again." << endl;
		return FALSE;
	}
}

/*---------------------------------------------------------------------
|  Method disconnect
|
|  Purpose:  The disconnect method is used to safely disconnect the 
	host computer from the NSC A2L Driver if a connection was once 
	successfully established.
|
|  Parameters:
|      None
|
|  Returns:  This function always returns the constants either TRUE or 
	FALSE, which are 1 and 0, respectively. If the disconnect method 
	returns TRUE, the connection has successfully been broken, whereas 
	a return of FALSE indicates no connection was made in the first 
	place.
*-------------------------------------------------------------------*/
BOOL NSC_A2L::disconnect()
{
	if (m_hUSBDevice != NULL)
	{
		set_motorpower(OFF);
		fnPerformaxComClose(m_hUSBDevice);
		m_hUSBDevice = NULL;
		return TRUE;
	}
	return FALSE;
}

/*---------------------------------------------------------------------
|  Method send
|
|  Purpose: The send method sends commands from the host machine to the 
	NSC-A2L Driver and then also stores the NSC-A2L Driver's responses 
	in the receive_buffer buffer, which is located as one of the 
	NSC_A2L class's private members.
|
|  Parameters:
|      IN const char* send_buffer -- The send_buffer parameter is a 
		pointer to where the commands are located. Provided that the 
		commands are correctly written, the user can send his or her 
		own commands to the NSC-A2L Driver by storing the commands in 
		a character array as string and then using the character array's 
		pointer as an input to the send method. Please refer to 
		NSC-A2L_Manual_Rev_1.117 for more information on how to build 
		commands for the NSC-A2L Driver.
|
|  Returns: The send method will always return the constants either TRUE 
	or FALSE, which are 1 and 0, respectively. A return of TRUE indicates 
	the command has successfully been transmitted, whereas a return value 
	of FALSE indicates the host computer is not connected to the NSC-A2L 
	Driver.
*-------------------------------------------------------------------*/
BOOL NSC_A2L::send(IN const char* send_buffer)
{
	char _send_buffer[BUFF_SIZE];
	char _receive_buffer[BUFF_SIZE];

	if (m_hUSBDevice == NULL)
	{
		cerr << "Not connected" << endl;
		return FALSE;
	}

	memset(_send_buffer, 0, BUFF_SIZE);
	strcpy(_send_buffer, send_buffer);

	if (strlen(_send_buffer)>0 &&
		fnPerformaxComSendRecv(m_hUSBDevice, _send_buffer, BUFF_SIZE, BUFF_SIZE, _receive_buffer))			
	{
		strcpy(receive_buffer, _receive_buffer);
	}
	else
	{
		strset(receive_buffer, 0);
	}

	return TRUE;

}

/*---------------------------------------------------------------------
|  Method flush
|
|  Purpose:  The flush method causes the USB buffer to empty itself for 
	a connected Performax USB device, e.g. the NSC-A2L Driver.
|
|  Parameters:
|      None
|
|  Returns:  The flush method always returns the constants either TRUE 
	or FALSE, which are 1 and 0, respectively. A return value of TRUE 
	indicates the USB buffer has successfully been emptied, whereas 
	FALSE indicates the USB buffer has not been emptied. The reason for 
	a returned value of FALSE could simply be a connection was not 
	established between the host computer and the NSC-A2L Driver.
*-------------------------------------------------------------------*/
BOOL NSC_A2L::flush()
{
	return (fnPerformaxComFlush(m_hUSBDevice) == 1) ? TRUE : FALSE;
}

/*---------------------------------------------------------------------
|  Method set_motorpower
|
|  Purpose:  The set_motorpower method causes the NSC-A2L Driver to 
	either turn the gimbal's motors on or off.

|  Parameters:
|      IN BOOL on -- The on parameter accepts the symbolic constants 
		either ON or OFF, which are 1 and 0, respectively. If the on 
		parameter is set to ON, the NSC-A2L Driver will turn the 
		Gimbal's motors on. If the on parameter is set to OFF, the 
		Gimbal's motors will turn off. Take note that, if the on 
		parameter is set to a value apart from ON or OFF, the 
		method will not function.
|
|  Returns:  None
*-------------------------------------------------------------------*/
void NSC_A2L::set_motorpower(IN BOOL on) // on = TRUE, motors are on; on = FALSE, motors are off
{
	if (on)
	{
		send(CMD_EO "=3");
	}
	else
	{
		send(CMD_EO "=0");
	}
}

/*---------------------------------------------------------------------
|  Method set_motorspeed_abort
|
|  Purpose:  The set_motorspeed_abort causes the NSC-A2L Driver to stop
	the Gimbal's servo motors from moving.

|  Parameters:
|      IN int select -- The select parameter only accepts the constants
		X, Y, or BOTH, which are 1, 2, and 3, respectively. A select
		parameter set to X causes the NSC-A2L Driver to stop movement
		on the connected Gimbal's X motor (i.e. rotation). The Y 
		constant corresponds to the Gimbal's Y motor (i.e. pitch). BOTH 
		corresponds to both axes.
|
|  Returns:  None
*-------------------------------------------------------------------*/
void NSC_A2L::set_motorspeed_abort(IN int select)
{	
	if (select & 0x01) send(CMD_ABORT CMD_X);
	if (select & 0x02) send(CMD_ABORT CMD_Y);
}

/*---------------------------------------------------------------------
|  Method set_motorposition
|
|  Purpose:  In ABS mode, the set_motorposition method causes the 
	Gimbal's servo motors to move to a position. In INC mode, the 
	motors instead increment by an amount. Take note both the Gimbal's 
	servo motors are only designed to move +/- 90 degrees from their 
	centered position; in other words, the Gimbal's motors cannot be 
	told to continuously rotate in one direction.
|
|  Parameters:
	   xposition and yposition parameter range -- +36000 to -36000

|      IN long xposition -- The xposition parameter corresponds to the
		Gimbal's X servo motor (i.e. rotation). In ABS mode, the 
		Gimbal's X motor will turn to the position specified as the 
		xparameter, whereas in INC mode, the X motor will increment 
		by the xposition parameter, instead. 

	  IN long yposition -- The yposition parameter corresponds to the
		Gimbal's Y servo motor (i.e. pitch). In ABS mode, the 
		Gimbal's Y motor will turn to the position specified as the 
		yparameter, whereas in INC mode, the Y motor will increment 
		by the yposition parameter, instead.

	  IN int select -- The select parameter only accepts the constants
		X, Y, or BOTH, which are 1, 2, and 3, respectively. If the 
		select parameter is set to X, the X motor will move. If the 
		select parameter is set to Y, the Y motor will move. BOTH will
		cause both motors to move.
|
|  Returns:  None
*-------------------------------------------------------------------*/
void NSC_A2L::set_motorposition(IN long xposition, IN long yposition, IN int select) // 1 = x, y = 2, both = 3
{
	char send_buffer[BUFF_SIZE];
	if (select & 0x01)
	{
		sprintf(send_buffer, CMD_X "%d", xposition);
		send(send_buffer);
	}
	if (select & 0x02)
	{
		sprintf(send_buffer, CMD_Y "%d", yposition);
		send(send_buffer);
	}
}

/*---------------------------------------------------------------------
|  Method set_devicenumber
|
|  Purpose:  The set_devicenumber methods changes the NSC-A2L Driver's
	device number. If multiple NSC-A2L Drivers are in use, each driver
	needs to have its default device number changed to an unique device
	number between the values 0 - 99, inclusively. 
|
|  Parameters:
|      IN int devicenumber -- The devicenumber is the new device number.
|
|  Returns:  None
*-------------------------------------------------------------------*/
void NSC_A2L::set_devicenumber(IN int devicenumber)
{
	char  send_buffer[BUFF_SIZE];
	sprintf(send_buffer, CMD_DN "=" TAG "%02d", devicenumber);
	send(send_buffer);
	send(CMD_STORE);
}

/*---------------------------------------------------------------------
|  Method get_devicenumber
|
|  Purpose:  The get_devicenumber method causes the NSC-A2L to return
	its device number.
|
|  Parameters:
|      None
|
|  Returns:  The device number is returned on success. A failure will
	cause the get_devicenumber method to return -1.
*-------------------------------------------------------------------*/
int NSC_A2L::get_devicenumber()
{
	if (send(CMD_DN))
		return atoi(receive_buffer);
	return -1;
}

/*---------------------------------------------------------------------
|  Method set_motorspeed
|
|  Purpose: The set_motorspeed method changes the high speed, low speed, 
	and acceleration of the Gimbal's servo motors. Low speed refers to 
	the starting speed when a motor is told to move to a position. The 
	speed of the motor will then accelerate up to the high speed, 
	depending on the acceleration. Once the motor moves close enough to 
	its stopping position, the motor will decelerate and then stop at 
	the low speed. The deceleration is equal to the negative of 
	acceleration. Please refer to the NSC-A2L_Manual_Rev_1.17.pdf 
	manual for more information.
|
|  Parameters:
|      IN long highspeed -- The high speed parameter is the Gimbal's
		motors cruising speed.
	   IN long lowspeed -- The low speed parameter is the Gimbal's 
		motors starting and stopping speed.
	   IN long accelerate -- 
|
|  Returns:  None
*-------------------------------------------------------------------*/
void  NSC_A2L::set_motorspeed(IN long highspeed, IN long lowspeed, IN long accelerate, IN int select)
{
	char send_buffer[BUFF_SIZE];

	/* set high speed, low speed, and acceleration */
	if (select & 0x01)
	{
		/* prevent a slow acceleration / deceleration */
		if (highspeed < get_motorspeed_highspeed(X))
		{
			sprintf(send_buffer, CMD_HSPD CMD_X "=%d", MAX_SPEED); send(send_buffer);
		}
		else
		{
			sprintf(send_buffer, CMD_HSPD CMD_X "=%d", MIN_SPEED); send(send_buffer);
		}

		/* set the on-the-fly speed */
		sprintf(send_buffer, CMD_SSPD CMD_X "=%d", highspeed); send(send_buffer);

		/* set other parameters */
		sprintf(send_buffer, CMD_HSPD CMD_X "=%d", highspeed); send(send_buffer);
		sprintf(send_buffer, CMD_LSPD CMD_X "=%d", lowspeed); send(send_buffer);
		sprintf(send_buffer, CMD_ACC CMD_X "=%d", accelerate); send(send_buffer);
	}

	if (select & 0x02)
	{
		/* prevent a slow acceleration / deceleration */
		if (highspeed < get_motorspeed_highspeed(Y))
		{
			sprintf(send_buffer, CMD_HSPD CMD_Y "=%d", MAX_SPEED); send(send_buffer);
		}
		else
		{
			sprintf(send_buffer, CMD_HSPD CMD_Y "=%d", MIN_SPEED); send(send_buffer);
		}

		/* set the on-the-fly speed */
		sprintf(send_buffer, CMD_SSPD CMD_Y "=%d", highspeed); send(send_buffer);

		sprintf(send_buffer, CMD_HSPD CMD_Y "=%d", highspeed); send(send_buffer);
		sprintf(send_buffer, CMD_LSPD CMD_Y "=%d", lowspeed); send(send_buffer);
		sprintf(send_buffer, CMD_ACC CMD_Y "=%d", accelerate); send(send_buffer);
	}
}

/*---------------------------------------------------------------------
|  Method set_mode
|
|  Purpose: The set_mode method is used to set the NSC-A2L's mode of 
	operation to either ABS (i.e. absolute) or INC (i.e. increment). 
	The absolute mode is the default mode of operation. In ABS, the
	set_motorposition method will cause the Gimbal's motors to move to
	a speicposition, whereas in INC, set_motorposition will causes the 
	Gimbal motors to increment by a specified amount. Please refer to 
	the NSC-A2L_Manual_Rev_1.17.pdf manual for more information.
|
|  Parameters:
|      IN int mode -- The mode parameter is set to either ABS or INC,
		which are constants that correspond to the values 0 and 1, 
		respectively.
|
|  Returns:  None
*-------------------------------------------------------------------*/
void NSC_A2L::set_mode(IN int mode)
{
	if (mode == ABS) send(CMD_ABS);
	if (mode == INC) send(CMD_INC);
}

/*---------------------------------------------------------------------
|  Method set_baudrate
|
|  Purpose:  The set_baudrate method changes the buad rate for 
	transmission between the host computer and the NSC-A2L driver.
|
|  Parameters:
|      IN int select -- The select parameter sets one of the possible
		baud rates, which are as follows.
			Constant -- Actual Value -- Baud Rate (in bps)
			B9600		1				9600
			B19200		2				19200
			B38400		3				38400
			B57600		4				57600
			B115200		5				115200 (default)
|
|  Returns:  None
*-------------------------------------------------------------------*/
void NSC_A2L::set_baudrate(IN int select)
{
	char send_buffer[BUFF_SIZE];
	sprintf(send_buffer, CMD_DB "=%d", select);
	send(send_buffer);
	send(CMD_STORE);
}

/*---------------------------------------------------------------------
|  Method calibrate_motors
|
|  Purpose:  The calibrate_motors method causes the NSC-A2L to 
	calibrate. The NSC-A2L Driver must be calibrated upon start-up to 
	ensure the Gimbal's servo motors are properly centered.

|  Parameters:
|      None
|
|  Returns:  None
*-------------------------------------------------------------------*/
void NSC_A2L::calibrate_motors()
{
	send(CMD_J_PLUS);
	waittill_motorposition_stop();
	send(CMD_H_PLUS);
	waittill_motorposition_stop();
	waittill_motorposition_stop();
}

/*---------------------------------------------------------------------
|  Method waittill_motorposition_stop
|
|  Purpose:  The waittill_motorposition_stop method will execute until
	one of the Gimbal's selected motors either stops moving or is 
	already not moving. The purpose of the waittill_motorposition_stop
	method are for applications during which action needs to be taken 
	after the Gimbal finishes its movement. For an example, please see
	the calibrate_motors method's definition above.
|
|  Parameters:
|      parameter_name -- None
|
|  Returns:  None
*-------------------------------------------------------------------*/
void NSC_A2L::waittill_motorposition_stop(int select)
{
	short previous_mode;

	// save current mode
	send(CMD_MM);
	previous_mode = atoi(receive_buffer);

	// set mode to INC to prevent movement while poling
	send(CMD_INC);

	if (select & 0x01)
	{
		do 
		{ send(CMD_X "0"); } 
		while (strcmp(CMD_ERR_MOVING, receive_buffer) == 0);
	}

	if (select & 0x02)
	{
		do 
		{ send(CMD_Y "0"); } 
		while (strcmp(CMD_ERR_MOVING, receive_buffer) == 0);
	}

	// return mode
	if (previous_mode == 0) send(CMD_ABS);
}

/*---------------------------------------------------------------------
|  Method get_motorposition
|
|  Purpose:  Get_motorposition returns the position of one of the 
	Gimbal's servo motors. 
|
|  Parameters:
|      IN int select -- The select parameter can be set to the 
		constants either X or Y, which are 1 and 2, respectively. X 
		corresponds to the Gimbal's X motor, whereas Y corresponds to
		the Y motor.
|
|  Returns:  On success, get_motorposition will return a value 
	corresponding to a motor's position, ranging from +36000 to 
	-36000, inclusively. On a failure, the error will be reported 
	via the console. 36001 will be returned if values, a part from X 
	or Y, are used as inputs to the select parameter.
*-------------------------------------------------------------------*/
long NSC_A2L::get_motorposition(IN int select)
{
	if (select == 1) { send(CMD_P CMD_X); return atoi(receive_buffer); }
	if (select == 2) { send(CMD_P CMD_Y); return atoi(receive_buffer); }
	return GET_MOTORPOSITION_FAILCODE;
}

/*---------------------------------------------------------------------
|  Method get_motorspeed_highspeed
|
|  Purpose:  The get_motorspeed returns the high speed of the Gimbal's
	motors.
|
|  Parameters:
|      parameter_name -- None
|
|  Returns:  The get_motorspeed_highspeed method returns the NSC_A2L's
	high speed on success. On failure, a -1 is returned.
*-------------------------------------------------------------------*/
long NSC_A2L::get_motorspeed_highspeed(IN int select)
{
	if (select == 0x01) { send(CMD_HSPD CMD_X); return atoi(receive_buffer); }
	if (select == 0x02) { send(CMD_HSPD CMD_Y); return atoi(receive_buffer); }
	return -1;
}

/*---------------------------------------------------------------------
|  Method setup
|
|  Purpose: The setup method first attempts to establish connection 
	between the host computer and the NSC_A2L Driver. If connection
	is successfully established, calibration is called and the default
	parameters are set. The setup method should be the first method to
	be called after the NSC_A2L class's constructors. Take note, the
	setup method calls important methods such as the setup and 
	calibrate_motors method.

|  Parameters:
|      IN int devicenumber -- The device number selects the NSC_A2L
		driver.

|  Returns: The setup will always return either true or false, which
	are 1 and 0, respectively. A return value of true signifies 
	connection has successfully been made, motors are on, motors are
	calibrated, and all the default parameters have been set. A 
	return value of false indicates connection was not established.
*-------------------------------------------------------------------*/
bool NSC_A2L::setup(IN int devicenumber)
{
	if (connect(devicenumber))
	{
		memset(receive_buffer, 0, BUFF_SIZE);
		set_motorpower(ON);
		set_motorspeed();
		calibrate_motors();
		set_mode();
		set_baudrate();
		set_scurve();
		set_ontheflyspeed_window();
		waittill_motorposition_stop();
		set_motorposition();
		waittill_motorposition_stop();
		return true;
	}
	return false;
}

/*---------------------------------------------------------------------
|  Method set_scurve
|
|  Purpose: This method changes how the x and y axes of the gimbal 
	accelerate and decelerate at the beginning and end of the Gimbal's
	movement. By default, when movement is initiated -- which is 
	executed with the set_motorposition method -- the axis (or axes) 
	start moving the at the low speed, accelerate to the high speed, 
	stay at the high speed until the axis's position is close to its
	destination, decelerate back down to its low speed, and then 
	stop. From NSC-A2L_Manual_Rev_1.17.pdf, the default movement is 
	simply referred to as trapezoidal movement. Alternatively, if the
	set_scurve is called with a parameter of true, the axes will move 
	using s-curve movement. S-curve is similar to trapezoidal movement
	except the edges of the trapezoid are rounded for smoother 
	transitions from low speed to high speed and vice versa. Please 
	refer to section "6. Motion Control Feature Overview" in 
	NSC-A2L_Manual_Rev_1.17.pdf for more information.

	PLEASE NOTE that the s-curve is set to off by default. On-fly 
	changing of speed is only functional during trapezoidal movement.

|  Parameters:
|		IN bool on -- If the on parameter is set to ON, then s-curve 
			will be enabled for the selected axis or axes. Otherwise, 
			s-curve will be disabled. The on parameter should be used 
			with either the ON or OFF constants, which are 1 and 0, 
			respectively. 
	
		IN int select -- The select parameter determines which axis 
			(or axes) will have its s-curve enabled or disabled. The 
			select parameter should be used with the constants X, Y, 
			or BOTH, which are 1, 2, and 3, respectively.

|  Returns: None
*-------------------------------------------------------------------*/
void NSC_A2L::set_scurve(IN bool on, IN int select) // 1 = X, 2 = Y, 3 = BOTH
{
	/* true for both axes! */
	/* if on is equal to true, then set s-curve to on, otherwise set s-curve to off */

	/*
	Tip:
	( BOOL ) ? A : B, where BOOL is a boolean expression, and A and B are statements
	The expression is equivalent to the following:
	if ( BOOL ) { A } else { B }
	*/

	if (select & 0x01)
	{
		(on == true) ? send(CMD_SCV CMD_X "=1") : send(CMD_SCV CMD_X "=0");
	}

	if (select & 0x02)
	{
		(on == true) ? send(CMD_SCV CMD_Y "=1") : send(CMD_SCV CMD_Y "=0");
	}
}

/*---------------------------------------------------------------------
|  Method set_ontheflyspeed_window
|
|  Purpose:  The set_ontheflyspeed_window method either disables or 
	enables on-the-fly speed changing for both axes. The method also 
	sets the SSPDM register in the NSC-A2L driver for certain ranges 
	of speed. Please see "Appendix A: Speed Settings" in 
	NSC-A2L_Manual_Rev_1.17.pdf for more information on the available
	windows of operation.

	PLEASE NOTE that the default SSPDM register value is 1 for a 
		window (i.e. high speed window) from 1 to 16,000 pulses per 
		second.
|
|  Parameters:
|		IN int window -- The window parameter changes the window of the
			or disables on-the-fly speed change mode. A window equaled
			to 0 disables the on-the-fly speed chang mode. Values from
			1 to 9 correspond to different windows.
|
|  Returns:  None
*-------------------------------------------------------------------*/
void NSC_A2L::set_ontheflyspeed_window(IN int window)
{
	char send_buffer[BUFF_SIZE >> 2];
	sprintf(send_buffer, CMD_SSPDM CMD_X "=%d", window); send(send_buffer);
	sprintf(send_buffer, CMD_SSPDM CMD_Y "=%d", window); send(send_buffer);
}
