/*
 * Generated with the FPGA Interface C API Generator 12.0.0
 * for NI-RIO 12.0.0 or later.
 */

#ifndef __NiFpga_VI1_h__
#define __NiFpga_VI1_h__

#ifndef NiFpga_Version
   #define NiFpga_Version 1200
#endif

#include "NiFpga.h"

/**
 * The filename of the FPGA bitfile.
 *
 * This is a #define to allow for string literal concatenation. For example:
 *
 *    static const char* const Bitfile = "C:\\" NiFpga_VI1_Bitfile;
 */
#define NiFpga_VI1_Bitfile "NiFpga_VI1.lvbitx"

/**
 * The signature of the FPGA bitfile.
 */
static const char* const NiFpga_VI1_Signature = "29A99B9126BA98FFE541F486089437BF";

typedef enum
{
   NiFpga_VI1_IndicatorI16_Mod2AI0 = 0x810E,
} NiFpga_VI1_IndicatorI16;

#endif
