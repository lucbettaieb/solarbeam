/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class solarbeam_2_easydevices_jni_HitecServoMotor_Pololu */

#ifndef _Included_solarbeam_2_easydevices_jni_HitecServoMotor_Pololu
#define _Included_solarbeam_2_easydevices_jni_HitecServoMotor_Pololu
#ifdef __cplusplus
extern "C" {
#endif
#undef solarbeam_2_easydevices_jni_HitecServoMotor_Pololu_MAX_POSITION
#define solarbeam_2_easydevices_jni_HitecServoMotor_Pololu_MAX_POSITION 2000L
#undef solarbeam_2_easydevices_jni_HitecServoMotor_Pololu_MIN_POSITION
#define solarbeam_2_easydevices_jni_HitecServoMotor_Pololu_MIN_POSITION 1000L
/*
 * Class:     solarbeam_2_easydevices_jni_HitecServoMotor_Pololu
 * Method:    constructPololuServoJni
 * Signature: (JSI)J
 */
JNIEXPORT jlong JNICALL Java_solarbeam_12_easydevices_jni_HitecServoMotor_00024Pololu_constructPololuServoJni
  (JNIEnv *, jobject, jlong, jshort, jint);

/*
 * Class:     solarbeam_2_easydevices_jni_HitecServoMotor_Pololu
 * Method:    destructPololuServoJni
 * Signature: (J)J
 */
JNIEXPORT jlong JNICALL Java_solarbeam_12_easydevices_jni_HitecServoMotor_00024Pololu_destructPololuServoJni
  (JNIEnv *, jobject, jlong);

/*
 * Class:     solarbeam_2_easydevices_jni_HitecServoMotor_Pololu
 * Method:    disableJni
 * Signature: (J)V
 */
JNIEXPORT void JNICALL Java_solarbeam_12_easydevices_jni_HitecServoMotor_00024Pololu_disableJni
  (JNIEnv *, jobject, jlong);

/*
 * Class:     solarbeam_2_easydevices_jni_HitecServoMotor_Pololu
 * Method:    setPositionJni
 * Signature: (JI)V
 */
JNIEXPORT void JNICALL Java_solarbeam_12_easydevices_jni_HitecServoMotor_00024Pololu_setPositionJni
  (JNIEnv *, jobject, jlong, jint);

/*
 * Class:     solarbeam_2_easydevices_jni_HitecServoMotor_Pololu
 * Method:    getPositionJni
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_solarbeam_12_easydevices_jni_HitecServoMotor_00024Pololu_getPositionJni
  (JNIEnv *, jobject, jlong);

/*
 * Class:     solarbeam_2_easydevices_jni_HitecServoMotor_Pololu
 * Method:    setIncrementJni
 * Signature: (JI)V
 */
JNIEXPORT void JNICALL Java_solarbeam_12_easydevices_jni_HitecServoMotor_00024Pololu_setIncrementJni
  (JNIEnv *, jobject, jlong, jint);

/*
 * Class:     solarbeam_2_easydevices_jni_HitecServoMotor_Pololu
 * Method:    stopJni
 * Signature: (J)V
 */
JNIEXPORT void JNICALL Java_solarbeam_12_easydevices_jni_HitecServoMotor_00024Pololu_stopJni
  (JNIEnv *, jobject, jlong);

#ifdef __cplusplus
}
#endif
#endif
