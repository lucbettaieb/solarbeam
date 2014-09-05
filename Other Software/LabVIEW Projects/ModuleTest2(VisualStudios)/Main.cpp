#include <iostream>
#include "criomodule.h"
#define BUFF_SIZE 4
using std::cout;
using std::cin;
using std::endl;

int main(int argv, char* argc[])
{
	uintptr_t FPGAInterfaceOutput = NULL;
	int16_t AnalogInput[BUFF_SIZE];
	char input;
	int i;

	/* setup and load the bitfile onto the FPGA */
	VISetup(&FPGAInterfaceOutput);

	/* start reading data from the A2D converter */
	while (1)
	{
		/* check if user wants to quit program */
		input = cin.get();
		if (input == 'q')
			break;

		/* Read data from A2D module */
		VIReadAnalog(&FPGAInterfaceOutput, AnalogInput, BUFF_SIZE);

		/* print data */
		for (i = 0; i < BUFF_SIZE; i++)
		{
			cout << "AI:" << AnalogInput[i] << endl;
		}
	}

	VIClose(&FPGAInterfaceOutput);

	return 0;
}