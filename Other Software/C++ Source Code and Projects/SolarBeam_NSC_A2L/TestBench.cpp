#include <iostream>
#include <cstdio>
#include <Windows.h>
#include "NSC_A2L.h"
#define BUFF_SIZE 20

using namespace std;

inline bool cs(char* string1, char* string2) { return strcmp(string1, string2)==0; }
static void script1(NSC_A2L& na);

int main(int argv, char* argc[])
{
	char input[BUFF_SIZE];

	/* create the objects and set the pointer to the first object */
	NSC_A2L na1 = NSC_A2L();
	NSC_A2L na2 = NSC_A2L();

	/* the purpose of the pointer to allow the user to swapped between gimbals */
	NSC_A2L* na_ptr = &na1;

	/* configure the gimbal objects */
	cout << "Configuring gimbals\n";
	na1.setup(0);
	na2.setup(1);
	cout << "Gimbals have been configured\n";

	/* send commands to the gimbal */
	cout << "User can now send commands to gimbals.\n"
		"\t\"switch\" selects the gimbal\n"
		"\t\"s1\" plays the SolarBeam syphomony (script1)\n"
		"\t\"quit\" ends the loop\n";
	while (1) 
	{
		/* get input from user */
		cin.getline(input, BUFF_SIZE);

		/* quit if input is quit */
		if (cs("quit", input)) break;
		
		/* switch gimbals if input is switch */
		if (cs("switch", input))
		{
			cout << "na_ptr is has been switched\n";
			na_ptr = (na_ptr == &na1) ? &na2: &na1;
			continue;
		}

		/* execute script is input is s1 */
		if (cs("s1", input))
		{
			cout << "Running script 1\n";
			script1(*na_ptr);
			continue;
		}

		/* execute command */
		cout << "Host: " << input << endl;
		na_ptr->send(input);
		cout << "NSC_A2L: " << na_ptr->get_receive_buffer() << endl;
	}
	
	/* get input from user to prevent program from closing right away */
	cin.get();
	return 0;
}

static void script1(NSC_A2L& na)
{
	na.set_motorspeed(10000);
	na.set_motorposition(-36000, -36000);
	na.waittill_motorposition_stop();
	na.set_motorspeed(1000);
	na.set_motorposition(36000, 36000);
	Sleep(750);
	na.set_motorspeed(9000);
	Sleep(750);
	na.set_motorspeed(3000);
	Sleep(750);
	na.set_motorspeed(7000);
	Sleep(750);
	na.set_motorspeed(5000);
	Sleep(750);
	na.set_motorspeed(5000);
	Sleep(750);
	na.set_motorspeed(7000);
	Sleep(750);
	na.set_motorspeed(3000);
	Sleep(750);
	na.set_motorspeed(9000);
	Sleep(750);
	na.set_motorspeed(10000);
}

