#include "solarbeam_2_easydevices_jni_HitecServoMotor.h"
#include "HS785HB.h"


/*
 * Class:     solarbeam_2_easydevices_jni_HitecServoMotor
 * Method:    constructHS785HBJni
 * Signature: (JII)J
 */
JNIEXPORT jlong JNICALL Java_solarbeam_12_easydevices_jni_HitecServoMotor_constructHS785HBJni
  (JNIEnv *env, jobject obj, jlong pointer, jint address, jint netDutyMicroseconds)
{
	if (pointer != NULL)
		return pointer;
	cerr << "address: " << (uint16_t)address << "\tnetDutyMicroseconds: " << netDutyMicroseconds << endl;
	return (jlong)(new HS785HB((uint8_t)address, (uint16_t)netDutyMicroseconds));
}

/*
 * Class:     solarbeam_2_easydevices_jni_HitecServoMotor
 * Method:    destroyHS785HBJni
 * Signature: (J)J
 */
JNIEXPORT jlong JNICALL Java_solarbeam_12_easydevices_jni_HitecServoMotor_destroyHS785HBJni
  (JNIEnv *env, jobject obj, jlong pointer)
{
	if (pointer == NULL)
		return 0;
	delete (HS785HB*)pointer;
}

/*
 * Class:     solarbeam_2_easydevices_jni_HitecServoMotor
 * Method:    enableJni
 * Signature: (JZ)V
 */
JNIEXPORT void JNICALL Java_solarbeam_12_easydevices_jni_HitecServoMotor_enableJni
  (JNIEnv *env, jobject obj, jlong pointer, jboolean turnOn)
{
	((HS785HB*)pointer)->enable((bool)turnOn);
}

/*
 * Class:     solarbeam_2_easydevices_jni_HitecServoMotor
 * Method:    setPositionDegreesJni
 * Signature: (JI)V
 */
JNIEXPORT void JNICALL Java_solarbeam_12_easydevices_jni_HitecServoMotor_setPositionDegreesJni
  (JNIEnv *env, jobject obj, jlong pointer, jint degrees)
{
	cerr << "Pointer: " << pointer << endl;
	cerr << "degrees: " << degrees << endl;
	((HS785HB*)pointer)->setPositionDegrees((uint16_t)degrees);
}

/*
 * Class:     solarbeam_2_easydevices_jni_HitecServoMotor
 * Method:    getPositionDegreesJni
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_solarbeam_12_easydevices_jni_HitecServoMotor_getPositionDegreesJni
  (JNIEnv *env, jobject obj, jlong pointer)
{
	return (jint)((HS785HB*)pointer)->getPositionDegrees();
}