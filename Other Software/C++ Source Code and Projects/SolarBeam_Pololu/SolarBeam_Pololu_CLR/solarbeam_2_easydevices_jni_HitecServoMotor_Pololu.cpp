#include "stdafx.h"
#include "solarbeam_2_easydevices_jni_HitecServoMotor_Pololu.h"
#include "PololuServo.h"

/*
 * Class:     solarbeam_2_easydevices_jni_HitecServoMotor_Pololu
 * Method:    constructPololuServoJni
 * Signature: (JSI)J
 */
JNIEXPORT jlong JNICALL Java_solarbeam_12_easydevices_jni_HitecServoMotor_00024Pololu_constructPololuServoJni
  (JNIEnv *env, jobject obj, jlong pointer, jshort channel, jint initialPosition)
{
	if (pointer != NULL)
		return pointer;
	return (jlong)(new PololuServoContinuous((uint8_t)channel, (uint16_t)initialPosition));
}

/*
 * Class:     solarbeam_2_easydevices_jni_HitecServoMotor_Pololu
 * Method:    destructPololuServoJni
 * Signature: (J)J
 */
JNIEXPORT jlong JNICALL Java_solarbeam_12_easydevices_jni_HitecServoMotor_00024Pololu_destructPololuServoJni
  (JNIEnv *env, jobject obj, jlong pointer)
{
	if (pointer == NULL)
		return 0;
	delete (PololuServoContinuous*)pointer;
	return 0;
}

/*
 * Class:     solarbeam_2_easydevices_jni_HitecServoMotor_Pololu
 * Method:    disableJni
 * Signature: (J)V
 */
JNIEXPORT void JNICALL Java_solarbeam_12_easydevices_jni_HitecServoMotor_00024Pololu_disableJni
  (JNIEnv *env, jobject obj, jlong pointer)
{
	((PololuServoContinuous*)pointer)->disable();
}

/*
 * Class:     solarbeam_2_easydevices_jni_HitecServoMotor_Pololu
 * Method:    setPositionJni
 * Signature: (JI)V
 */
JNIEXPORT void JNICALL Java_solarbeam_12_easydevices_jni_HitecServoMotor_00024Pololu_setPositionJni
  (JNIEnv *env, jobject obj, jlong pointer, jint microseconds)
{
	((PololuServoContinuous*)pointer)->setPosition((uint16_t)microseconds);
}

/*
 * Class:     solarbeam_2_easydevices_jni_HitecServoMotor_Pololu
 * Method:    getPositionJni
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_solarbeam_12_easydevices_jni_HitecServoMotor_00024Pololu_getPositionJni
  (JNIEnv *env, jobject obj, jlong pointer)
{
	return (jint)((PololuServoContinuous*)pointer)->getPosition();
}

/*
 * Class:     solarbeam_2_easydevices_jni_HitecServoMotor_Pololu
 * Method:    setIncrementJni
 * Signature: (JI)V
 */
JNIEXPORT void JNICALL Java_solarbeam_12_easydevices_jni_HitecServoMotor_00024Pololu_setIncrementJni
  (JNIEnv *env, jobject obj, jlong pointer, jint increment)
{
	((PololuServoContinuous*)pointer)->setIncrement((uint16_t)increment);
}

/*
 * Class:     solarbeam_2_easydevices_jni_HitecServoMotor_Pololu
 * Method:    stopJni
 * Signature: (J)V
 */
JNIEXPORT void JNICALL Java_solarbeam_12_easydevices_jni_HitecServoMotor_00024Pololu_stopJni
  (JNIEnv *env, jobject obj, jlong pointer)
{
	((PololuServoContinuous*)pointer)->stop();
}