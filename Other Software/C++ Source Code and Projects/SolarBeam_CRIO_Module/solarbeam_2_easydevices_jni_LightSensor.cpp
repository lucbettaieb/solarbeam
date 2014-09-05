#include "BH1750.h"
#include "solarbeam_2_easydevices_jni_LightSensor.h"
#include "FPGAInterface.h"
#include <cstdint>

/*
 * Class:     solarbeam_2_easydevices_jni_LightSensor
 * Method:    constructLightSensorJni
 * Signature: (JII)J
 */
JNIEXPORT jlong JNICALL Java_solarbeam_12_easydevices_jni_LightSensor_constructLightSensorJni
  (JNIEnv * env, jobject obj, jlong lightSensorPointer, jint sdapin, jint sclpin)
{
	if (lightSensorPointer != 0)
		return lightSensorPointer;
	return (jlong)new EasyBH1750(PIN((uint32_t)sdapin), PIN((uint32_t)sclpin));
}

/*
 * Class:     solarbeam_2_easydevices_jni_LightSensor
 * Method:    destroyLightSensorJni
 * Signature: (J)J
 */
JNIEXPORT jlong JNICALL Java_solarbeam_12_easydevices_jni_LightSensor_destroyLightSensorJni
  (JNIEnv * env, jobject obj, jlong lightSensorPointer)
{
	if (lightSensorPointer == 0)
		return 0;
	delete (EasyBH1750*)lightSensorPointer;
	return 0;
}

/*
 * Class:     solarbeam_2_easydevices_jni_LightSensor
 * Method:    isDevicePresentJni
 * Signature: (J)Z
 */
JNIEXPORT jboolean JNICALL Java_solarbeam_12_easydevices_jni_LightSensor_isDevicePresentJni
  (JNIEnv * env, jobject obj, jlong lightSensorPointer)
{
	return (jboolean)((EasyBH1750*)lightSensorPointer)->isDevicePresent();
}

/*
 * Class:     solarbeam_2_easydevices_jni_LightSensor
 * Method:    powerOnJni
 * Signature: (J)V
 */
JNIEXPORT void JNICALL Java_solarbeam_12_easydevices_jni_LightSensor_powerOnJni
  (JNIEnv * env, jobject obj, jlong lightSensorPointer)
{
	((EasyBH1750*)lightSensorPointer)->powerOn();
}

/*
 * Class:     solarbeam_2_easydevices_jni_LightSensor
 * Method:    powerOffJni
 * Signature: (J)V
 */
JNIEXPORT void JNICALL Java_solarbeam_12_easydevices_jni_LightSensor_powerOffJni
  (JNIEnv * env, jobject obj, jlong lightSensorPointer)
{
	((EasyBH1750*)lightSensorPointer)->powerDown();
}

/*
 * Class:     solarbeam_2_easydevices_jni_LightSensor
 * Method:    readJni
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_solarbeam_12_easydevices_jni_LightSensor_readJni
  (JNIEnv * env, jobject obj, jlong lightSensorPointer)
{
	return ((EasyBH1750*)lightSensorPointer)->read();
}