#include "extcode.h"
#pragma pack(push)
#pragma pack(1)

#ifdef __cplusplus
extern "C" {
#endif

/*!
 * VIClose
 */
int32_t __cdecl VIClose(uintptr_t *FPGAInterfaceInput);
/*!
 * VIReadAnalog
 */
void __cdecl VIReadAnalog(uintptr_t *FPGAInterfaceInput, 
	int16_t AnalogInput[], int32_t len);
/*!
 * VISetup
 */
int32_t __cdecl VISetup(uintptr_t *FPGAInterfaceOutput);
/*!
 * VIReadDigital
 */
uint32_t __cdecl VIReadDigital(uintptr_t *FPGAInterfaceInput);
/*!
 * VISetDirectionDigital
 */
void __cdecl VISetDirectionDigital(uintptr_t *FPGAInterfaceInput, 
	uint32_t EnableDigitalOutput);
/*!
 * VIWriteDigital
 */
void __cdecl VIWriteDigital(uintptr_t *FPGAInterfaceInput, 
	uint32_t DigitalOutput);

long __cdecl LVDLLStatus(char *errStr, int errStrLen, void *module);

#ifdef __cplusplus
} // extern "C"
#endif

#pragma pack(pop)

