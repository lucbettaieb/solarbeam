#include "solarbeam_2_easydevices_jni_Gimbal.h"
#include "NSC_A2L.h"
#include <iostream>
/*
 * Class:     solarbeam_2_easydevices_jni_Gimbal
 * Method:    construct_gimbalPointer
 * Signature: (J)J
 */
JNIEXPORT jlong JNICALL Java_solarbeam_12_easydevices_jni_Gimbal_construct_1gimbalPointer
  (JNIEnv * env, jobject obj, jlong gimbalPointer)
{
	if (gimbalPointer != NULL)
		return gimbalPointer;
	return (jlong)new NSC_A2L();
}

/*
 * Class:     solarbeam_2_easydevices_jni_Gimbal
 * Method:    destroy_gimbalPointer
 * Signature: (J)J
 */
JNIEXPORT jlong JNICALL Java_solarbeam_12_easydevices_jni_Gimbal_destroy_1gimbalPointer
  (JNIEnv * env, jobject obj, jlong gimbalPointer)
{
	if (gimbalPointer != NULL)
		delete (NSC_A2L*)gimbalPointer;
	return NULL;
}

/*
 * Class:     solarbeam_2_easydevices_jni_Gimbal
 * Method:    connect
 * Signature: (JI)Z
 */
JNIEXPORT jboolean JNICALL Java_solarbeam_12_easydevices_jni_Gimbal_connect
  (JNIEnv * env, jobject obj, jlong gimbalPointer, jint devicenumber)
{
	return ((NSC_A2L*)gimbalPointer)->connect((BOOL)devicenumber);
}

/*
 * Class:     solarbeam_2_easydevices_jni_Gimbal
 * Method:    disconnect
 * Signature: (J)Z
 */
JNIEXPORT jboolean JNICALL Java_solarbeam_12_easydevices_jni_Gimbal_disconnect
  (JNIEnv * env, jobject obj, jlong gimbalPointer)
{
	return  ((NSC_A2L*)gimbalPointer)->disconnect();
}

/*
 * Class:     solarbeam_2_easydevices_jni_Gimbal
 * Method:    flush
 * Signature: (J)Z
 */
JNIEXPORT jboolean JNICALL Java_solarbeam_12_easydevices_jni_Gimbal_flush
  (JNIEnv * env, jobject obj, jlong gimbalPointer)
{
	return ((NSC_A2L*)gimbalPointer)->flush();
}

/*
 * Class:     solarbeam_2_easydevices_jni_Gimbal
 * Method:    setup
 * Signature: (JI)Z
 */
JNIEXPORT jboolean JNICALL Java_solarbeam_12_easydevices_jni_Gimbal_setup
  (JNIEnv * env, jobject obj, jlong gimbalPointer, jint devicenumber)
{
	return ((NSC_A2L*)gimbalPointer)->setup(devicenumber);
}

/*
 * Class:     solarbeam_2_easydevices_jni_Gimbal
 * Method:    setDeviceNumber
 * Signature: (JI)V
 */
JNIEXPORT void JNICALL Java_solarbeam_12_easydevices_jni_Gimbal_setDeviceNumber
  (JNIEnv * env, jobject obj, jlong gimbalPointer, jint devicenumber)
{
	((NSC_A2L*)gimbalPointer)->set_devicenumber(devicenumber);
}

/*
 * Class:     solarbeam_2_easydevices_jni_Gimbal
 * Method:    getDeviceNumber
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_solarbeam_12_easydevices_jni_Gimbal_getDeviceNumber
  (JNIEnv * env, jobject obj, jlong gimbalPointer)
{
	return ((NSC_A2L*)gimbalPointer)->get_devicenumber();
}

/*
 * Class:     solarbeam_2_easydevices_jni_Gimbal
 * Method:    setMotorPower
 * Signature: (JZ)V
 */
JNIEXPORT void JNICALL Java_solarbeam_12_easydevices_jni_Gimbal_setMotorPower
  (JNIEnv * env, jobject obj, jlong gimbalPointer, jboolean on)
{
	((NSC_A2L*)gimbalPointer)->set_motorpower((BOOL)on);
}

/*
 * Class:     solarbeam_2_easydevices_jni_Gimbal
 * Method:    setMotorSpeed
 * Signature: (JJJJI)V
 */
JNIEXPORT void JNICALL Java_solarbeam_12_easydevices_jni_Gimbal_setMotorSpeed
  (JNIEnv * env, jobject obj, jlong gimbalPointer, jlong highspeed, jlong lowspeed, jlong accelerate, jint select)
{
	((NSC_A2L*)gimbalPointer)->set_motorspeed(
		(long)highspeed,
		(long)lowspeed,
		(long)accelerate,
		(int)select);
}

/*
 * Class:     solarbeam_2_easydevices_jni_Gimbal
 * Method:    setMotorSpeedAbort
 * Signature: (JI)V
 */
JNIEXPORT void JNICALL Java_solarbeam_12_easydevices_jni_Gimbal_setMotorSpeedAbort
  (JNIEnv * env, jobject obj, jlong gimbalPointer, jint select)
{
	((NSC_A2L*)gimbalPointer)->set_motorspeed_abort(select);
}

/*
 * Class:     solarbeam_2_easydevices_jni_Gimbal
 * Method:    setMotorPosition
 * Signature: (JJJI)V
 */
JNIEXPORT void JNICALL Java_solarbeam_12_easydevices_jni_Gimbal_setMotorPosition
  (JNIEnv * env, jobject obj, jlong gimbalPointer, jlong xposition, jlong yposition, jint select)
{
	((NSC_A2L*)gimbalPointer)->set_motorposition((long)xposition, (long)yposition, (int)select);
}

/*
 * Class:     solarbeam_2_easydevices_jni_Gimbal
 * Method:    setMode
 * Signature: (JI)V
 */
JNIEXPORT void JNICALL Java_solarbeam_12_easydevices_jni_Gimbal_setMode
  (JNIEnv * env, jobject obj, jlong gimbalPointer, jint mode)
{
	((NSC_A2L*)gimbalPointer)->set_mode((jint)mode);
}

/*
 * Class:     solarbeam_2_easydevices_jni_Gimbal
 * Method:    setBaudrate
 * Signature: (JI)V
 */
JNIEXPORT void JNICALL Java_solarbeam_12_easydevices_jni_Gimbal_setBaudrate
  (JNIEnv * env, jobject obj, jlong gimbalPointer, jint select)
{
	((NSC_A2L*)gimbalPointer)->set_baudrate((int)select);
}

/*
 * Class:     solarbeam_2_easydevices_jni_Gimbal
 * Method:    calibrateMotors
 * Signature: (J)V
 */
JNIEXPORT void JNICALL Java_solarbeam_12_easydevices_jni_Gimbal_calibrateMotors
  (JNIEnv * env, jobject obj, jlong gimbalPointer)
{
	((NSC_A2L*)gimbalPointer)->calibrate_motors();
}

/*
 * Class:     solarbeam_2_easydevices_jni_Gimbal
 * Method:    getMotorPosition
 * Signature: (J)J
 */
JNIEXPORT jlong JNICALL Java_solarbeam_12_easydevices_jni_Gimbal_getMotorPosition
  (JNIEnv * env, jobject obj, jlong gimbalPointer, jint select)
{
	return ((NSC_A2L*)gimbalPointer)->get_motorposition((int)select);
}

/*
 * Class:     solarbeam_2_easydevices_jni_Gimbal
 * Method:    getMotorSpeedHighspeed
 * Signature: (JI)J
 */
JNIEXPORT jlong JNICALL Java_solarbeam_12_easydevices_jni_Gimbal_getMotorSpeedHighspeed
  (JNIEnv * env, jobject obj, jlong gimbalPointer, jint select)
{
	return ((NSC_A2L*)gimbalPointer)->get_motorspeed_highspeed((int)select);
}