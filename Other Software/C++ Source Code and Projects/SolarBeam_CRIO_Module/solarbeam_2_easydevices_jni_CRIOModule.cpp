#include <cstdint>
#include "solarbeam_2_easydevices_jni_CRIOModule.h"
#include "FPGAInterface.h"

/*
 * Class:     solarbeam_2_easydevices_jni_CRIOModule
 * Method:    FPGASetup
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_solarbeam_12_easydevices_jni_CRIOModule_FPGASetup
  (JNIEnv *env, jobject obj)
{
	FPGAI.setup();
}

/*
 * Class:     solarbeam_2_easydevices_jni_CRIOModule
 * Method:    FPGAClose
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_solarbeam_12_easydevices_jni_CRIOModule_FPGAClose
  (JNIEnv *env, jobject obj)
{
	FPGAI.close();
}

/*
 * Class:     solarbeam_2_easydevices_jni_CRIOModule
 * Method:    readAnalog
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_solarbeam_12_easydevices_jni_CRIOModule_readAnalog
  (JNIEnv *env, jobject obj, jint analogInput)
{
	return (jint)AIN[(uint8_t)analogInput];
}

/*
 * Class:     solarbeam_2_easydevices_jni_CRIOModule
 * Method:    readDigital
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_solarbeam_12_easydevices_jni_CRIOModule_readDigital
  (JNIEnv *env, jobject obj)
{
	return DIN;
}

/*
 * Class:     solarbeam_2_easydevices_jni_CRIOModule
 * Method:    setDirectionDigital
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_solarbeam_12_easydevices_jni_CRIOModule_setDirectionDigital
  (JNIEnv *env, jobject obj, jint enableDigitalOutput)
{
	DDIR = enableDigitalOutput;
}

/*
 * Class:     solarbeam_2_easydevices_jni_CRIOModule
 * Method:    writeDigital
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_solarbeam_12_easydevices_jni_CRIOModule_writeDigital
  (JNIEnv *env, jobject obj, jint digitalOutput)
{
	DOUT = (uint32_t)digitalOutput;
}