/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class solarbeam_2_easydevices_jni_Gimbal */

#ifndef _Included_solarbeam_2_easydevices_jni_Gimbal
#define _Included_solarbeam_2_easydevices_jni_Gimbal
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     solarbeam_2_easydevices_jni_Gimbal
 * Method:    construct_gimbalPointer
 * Signature: (J)J
 */
JNIEXPORT jlong JNICALL Java_solarbeam_12_easydevices_jni_Gimbal_construct_1gimbalPointer
  (JNIEnv *, jobject, jlong);

/*
 * Class:     solarbeam_2_easydevices_jni_Gimbal
 * Method:    destroy_gimbalPointer
 * Signature: (J)J
 */
JNIEXPORT jlong JNICALL Java_solarbeam_12_easydevices_jni_Gimbal_destroy_1gimbalPointer
  (JNIEnv *, jobject, jlong);

/*
 * Class:     solarbeam_2_easydevices_jni_Gimbal
 * Method:    connect
 * Signature: (JI)Z
 */
JNIEXPORT jboolean JNICALL Java_solarbeam_12_easydevices_jni_Gimbal_connect
  (JNIEnv *, jobject, jlong, jint);

/*
 * Class:     solarbeam_2_easydevices_jni_Gimbal
 * Method:    disconnect
 * Signature: (J)Z
 */
JNIEXPORT jboolean JNICALL Java_solarbeam_12_easydevices_jni_Gimbal_disconnect
  (JNIEnv *, jobject, jlong);

/*
 * Class:     solarbeam_2_easydevices_jni_Gimbal
 * Method:    flush
 * Signature: (J)Z
 */
JNIEXPORT jboolean JNICALL Java_solarbeam_12_easydevices_jni_Gimbal_flush
  (JNIEnv *, jobject, jlong);

/*
 * Class:     solarbeam_2_easydevices_jni_Gimbal
 * Method:    setup
 * Signature: (JI)Z
 */
JNIEXPORT jboolean JNICALL Java_solarbeam_12_easydevices_jni_Gimbal_setup
  (JNIEnv *, jobject, jlong, jint);

/*
 * Class:     solarbeam_2_easydevices_jni_Gimbal
 * Method:    setDeviceNumber
 * Signature: (JI)V
 */
JNIEXPORT void JNICALL Java_solarbeam_12_easydevices_jni_Gimbal_setDeviceNumber
  (JNIEnv *, jobject, jlong, jint);

/*
 * Class:     solarbeam_2_easydevices_jni_Gimbal
 * Method:    getDeviceNumber
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_solarbeam_12_easydevices_jni_Gimbal_getDeviceNumber
  (JNIEnv *, jobject, jlong);

/*
 * Class:     solarbeam_2_easydevices_jni_Gimbal
 * Method:    setMotorPower
 * Signature: (JZ)V
 */
JNIEXPORT void JNICALL Java_solarbeam_12_easydevices_jni_Gimbal_setMotorPower
  (JNIEnv *, jobject, jlong, jboolean);

/*
 * Class:     solarbeam_2_easydevices_jni_Gimbal
 * Method:    setMotorSpeed
 * Signature: (JJJJI)V
 */
JNIEXPORT void JNICALL Java_solarbeam_12_easydevices_jni_Gimbal_setMotorSpeed
  (JNIEnv *, jobject, jlong, jlong, jlong, jlong, jint);

/*
 * Class:     solarbeam_2_easydevices_jni_Gimbal
 * Method:    setMotorSpeedAbort
 * Signature: (JI)V
 */
JNIEXPORT void JNICALL Java_solarbeam_12_easydevices_jni_Gimbal_setMotorSpeedAbort
  (JNIEnv *, jobject, jlong, jint);

/*
 * Class:     solarbeam_2_easydevices_jni_Gimbal
 * Method:    setMotorPosition
 * Signature: (JJJI)V
 */
JNIEXPORT void JNICALL Java_solarbeam_12_easydevices_jni_Gimbal_setMotorPosition
  (JNIEnv *, jobject, jlong, jlong, jlong, jint);

/*
 * Class:     solarbeam_2_easydevices_jni_Gimbal
 * Method:    setMode
 * Signature: (JI)V
 */
JNIEXPORT void JNICALL Java_solarbeam_12_easydevices_jni_Gimbal_setMode
  (JNIEnv *, jobject, jlong, jint);

/*
 * Class:     solarbeam_2_easydevices_jni_Gimbal
 * Method:    setBaudrate
 * Signature: (JI)V
 */
JNIEXPORT void JNICALL Java_solarbeam_12_easydevices_jni_Gimbal_setBaudrate
  (JNIEnv *, jobject, jlong, jint);

/*
 * Class:     solarbeam_2_easydevices_jni_Gimbal
 * Method:    calibrateMotors
 * Signature: (J)V
 */
JNIEXPORT void JNICALL Java_solarbeam_12_easydevices_jni_Gimbal_calibrateMotors
  (JNIEnv *, jobject, jlong);

/*
 * Class:     solarbeam_2_easydevices_jni_Gimbal
 * Method:    getMotorPosition
 * Signature: (JI)J
 */
JNIEXPORT jlong JNICALL Java_solarbeam_12_easydevices_jni_Gimbal_getMotorPosition
  (JNIEnv *, jobject, jlong, jint);

/*
 * Class:     solarbeam_2_easydevices_jni_Gimbal
 * Method:    getMotorSpeedHighspeed
 * Signature: (JI)J
 */
JNIEXPORT jlong JNICALL Java_solarbeam_12_easydevices_jni_Gimbal_getMotorSpeedHighspeed
  (JNIEnv *, jobject, jlong, jint);

#ifdef __cplusplus
}
#endif
#endif
