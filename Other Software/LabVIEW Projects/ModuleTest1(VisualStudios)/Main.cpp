#include "NiFpga_VI1.h"
#include <cstdint>
#include <iostream>
#include <cstdio>
using std::cout;
using std::cin;
using std::endl;
#define BUFF_SIZE 64
#define DIRECTORY "c:\\Users\\user\\Documents\\LabVIEW Projects\\ModuleTest1(VisualStudios)\\"

inline void err() { cout << "There IS an error!" << endl; }
inline void ner() { cout << "There is NO error!" << endl; }

int main(int argv, char* argc[])
{
	int16_t Mod2AI0;
	NiFpga_Session session;
	char input[BUFF_SIZE];

	/* must be called before any other calls */
	NiFpga_Status status = NiFpga_Initialize();

	if (NiFpga_IsNotError(status))
	{

		/* opens a session, downloads the bitstream, and runs the FPGA */
		NiFpga_MergeStatus(&status, NiFpga_Open(DIRECTORY NiFpga_VI1_Bitfile ,
                                              NiFpga_VI1_Signature,
                                              "RIO0",
                                              0,
                                              &session));

		/* check load status */
		if (NiFpga_IsNotError(status))
		{
			ner();

			/* must close if we successfully opened */
			NiFpga_MergeStatus(&status, NiFpga_Close(session, 0));

			/* get values */
			while (1)
			{
				/* get data from console */
				cin.getline(input, BUFF_SIZE);

				/* quit when quit is typed */
				if (strcmp(input, "quit") == 0) break;

				/* read data from Mod2AIO */
				cout << "Reading Mod2AI0" << endl;
				NiFpga_MergeStatus(&status, NiFpga_ReadI16(session, NiFpga_VI1_IndicatorI16_Mod2AI0, &Mod2AI0));
				cout << "Value: " << Mod2AI0 << endl;
			}

			/* must close if we successfully opened */
			cout << "Session closed" << endl;
			NiFpga_MergeStatus(&status, NiFpga_Close(session, 0));

		} else err();

		/* must be called after ALL other calls if we succesfully initialized */
		NiFpga_MergeStatus(&status, NiFpga_Finalize());
	}

	cin.get();
	return 0;
}