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
/*!
 * VIEnableModulation
 */
void __cdecl VIEnableModulation(uintptr_t *FPGAInterface, 
	int8_t ModulationEnable);
/*!
 * VIWriteModulationTick
 */
void __cdecl VIWriteModulationTick(uintptr_t *FPGAInterface, 
	uint64_t ModulationHighTick[], uint64_t ModulationLowTick[], int32_t len, 
	int32_t len2);

long __cdecl LVDLLStatus(char *errStr, int errStrLen, void *module);

#ifdef __cplusplus
} // extern "C"
#endif

#pragma pack(pop)

