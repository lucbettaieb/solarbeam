

//#include "extcode.h"
#include <cstdint>
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

long __cdecl LVDLLStatus(char *errStr, int errStrLen, void *module);

#ifdef __cplusplus
} // extern "C"
#endif

#pragma pack(pop)

