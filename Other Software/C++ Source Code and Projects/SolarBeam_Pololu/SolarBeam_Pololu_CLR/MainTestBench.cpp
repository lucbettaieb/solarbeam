#include <iostream>
#include <cstdint>
#include "PololuServo.h"
using namespace std;

void main(int argv, char* argc[])
{
	uint16_t input = 0;
	PololuServoContinuous ps0 = PololuServoContinuous(0, 1900);
	PololuServoContinuous ps1 = PololuServoContinuous(1); // needs initial starting position
	PololuServoContinuous* ps = &ps1;

	while (true)
	{
		cout << "Enter a value: " << endl;
		if ((cin >> input).fail())
			break;
		if (input == 0) 
		{
			ps = &ps0;
			continue;
		} 
		else if (input == 1)
		{
			ps = &ps1;
			continue;
		} 
		else if (input == 2)
		{
			ps->stop();
			continue;
		}
		ps->setPosition(input);
	}

	cin.clear();
	while (cin.get() != '\n')
		continue;
	ps0.~PololuServoContinuous();
	ps1.~PololuServoContinuous();
	cin.get();
}