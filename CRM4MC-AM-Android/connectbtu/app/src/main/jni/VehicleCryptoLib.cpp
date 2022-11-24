#include <jni.h>
#include <stdio.h>
#include <string.h>
#include <android/log.h>

#include "VehicleCryptoLib.h"
#include "VehicleCryptoCommon.h"
#include "VehicleCryptoService.h"

/*
 * [PROTOCOL-CR-051] ユーザ名送出契機の変更　ユーザ名のみを設定できるIFを追加
 */

static	VehicleCryptoService*	gVehicleCryptoService	= NULL;

JNIEXPORT void JNICALL Java_com_honda_connmc_NativeLib_NativeLibController_setUpNative(JNIEnv* env, jobject thiz)
{
	DebugLogNativeD("@%s(%u) start", __func__, __LINE__);
	if (gVehicleCryptoService == NULL) {
		gVehicleCryptoService	= new VehicleCryptoService();
	}
	DebugLogNativeD("@%s(%u) end", __func__, __LINE__);
}

JNIEXPORT void JNICALL Java_com_honda_connmc_NativeLib_NativeLibController_finalizeNative(JNIEnv* env, jobject thiz)
{
	DebugLogNativeD("@%s(%u) start", __func__, __LINE__);
	if(NULL == gVehicleCryptoService)
	{
		DebugLogNativeE("%s(%d) : return = 0x%x", __FILE__, __LINE__, ERROR_INSTANCE_NOT_FOUND);
		return;
	}
	delete gVehicleCryptoService;
	gVehicleCryptoService = NULL;
	DebugLogNativeD("@%s(%u) end", __func__, __LINE__);
}

JNIEXPORT jint JNICALL Java_com_honda_connmc_NativeLib_NativeLibController_getUserNameNative(JNIEnv* env, jobject thiz, jbyteArray outUserName)
{
	int ret = ERROR_NON;
	int outputLength = 0;
	jbyte* outResult = NULL;

	DebugLogNativeD("@%s(%u) start", __func__, __LINE__);
	if (outUserName == NULL) {
		DebugLogNativeE("%s(%d) : return = 0x%x", __func__, __LINE__, ERROR_PARAM);
		return ERROR_PARAM;
	}
	outputLength = env->GetArrayLength(outUserName);
	if (outputLength < SECURITYDATA_USER_NAME_LENGTH) {
		DebugLogNativeE("%s(%d) : return = 0x%x", __func__, __LINE__, ERROR_PARAM);
		return ERROR_PARAM;
	}

	jbyte resultData[outputLength];
	memset(resultData, 0, outputLength);

	ret = gVehicleCryptoService->getUserName((char*)resultData, outputLength);
	if (ret != ERROR_NON) {
		DebugLogNativeE("%s(%d) : return = 0x%x", __FILE__, __LINE__, ret);
		return ret;
	}
	outResult = env->GetByteArrayElements(outUserName, NULL);
	memcpy(outResult, resultData, outputLength);

	env->ReleaseByteArrayElements(outUserName, outResult, 0);
	DebugLogNativeD("@%s(%u) end", __func__, __LINE__);
	return ret;
}

JNIEXPORT jint JNICALL Java_com_honda_connmc_NativeLib_NativeLibController_setUserInfoNative(JNIEnv* env, jobject thiz, jbyteArray userName, jbyteArray deviceUUID)
{
	int ret = ERROR_NON;
	int userNameLen = 0;
	int deviceUUIDLen = 0;
	jbyte* inUserName = NULL;
	jbyte* inDeviceUUID = NULL;

	DebugLogNativeD("@%s(%u) start", __func__, __LINE__);
	if ((userName == NULL) || (deviceUUID == NULL)) {
		DebugLogNativeE("%s(%d) : return = 0x%x", __func__, __LINE__, ret);
		return ERROR_PARAM;
	}
	userNameLen = env->GetArrayLength(userName);
	deviceUUIDLen = env->GetArrayLength(deviceUUID);
	if ((userNameLen <= 0) || (userNameLen > SECURITYDATA_USER_NAME_LENGTH) ||
		(deviceUUIDLen <= 0) || (deviceUUIDLen > SECURITYDATA_USER_TID_LENGTH)) {
		DebugLogNativeE("%s(%d) : return = 0x%x", __func__, __LINE__, ret);
		return ERROR_PARAM;
	}
	jbyte userNameData[SECURITYDATA_USER_NAME_LENGTH];
	memset(userNameData, 0, SECURITYDATA_USER_NAME_LENGTH);
	inUserName = env->GetByteArrayElements(userName, NULL);
	memcpy(userNameData, inUserName, userNameLen);

	jbyte deviceUUIDData[SECURITYDATA_USER_TID_LENGTH];
	memset(deviceUUIDData, 0, SECURITYDATA_USER_TID_LENGTH);
	inDeviceUUID = env->GetByteArrayElements(deviceUUID, NULL);
	memcpy(deviceUUIDData, inDeviceUUID, deviceUUIDLen);

	ret = gVehicleCryptoService->setUserInfo((const char*)userNameData, (const char*)deviceUUIDData);

	env->ReleaseByteArrayElements(userName, inUserName, 0);
	env->ReleaseByteArrayElements(deviceUUID, inDeviceUUID, 0);
	DebugLogNativeD("@%s(%u) end", __func__, __LINE__);
	return ret;
}

JNIEXPORT jint JNICALL Java_com_honda_connmc_NativeLib_NativeLibController_setUserNameNative(JNIEnv* env, jobject thiz, jbyteArray userName)
{
	int ret = ERROR_NON;
	int userNameLen = 0;
	jbyte* inUserName = NULL;

	DebugLogNativeD("@%s(%u) start", __func__, __LINE__);
	if (userName == NULL) {
		return ERROR_PARAM;
	}
	userNameLen = env->GetArrayLength(userName);
	if ((userNameLen <= 0) || (userNameLen > SECURITYDATA_USER_NAME_LENGTH)) {
		DebugLogNativeE("%s(%d) : return = 0x%x", __func__, __LINE__, ret);
		return ERROR_PARAM;
	}
	jbyte userNameData[SECURITYDATA_USER_NAME_LENGTH];
	memset(userNameData, 0, SECURITYDATA_USER_NAME_LENGTH);
	inUserName = env->GetByteArrayElements(userName, NULL);
	memcpy(userNameData, inUserName, userNameLen);

	ret = gVehicleCryptoService->setUserName((const char*)userNameData);
	env->ReleaseByteArrayElements(userName, inUserName, 0);
	DebugLogNativeD("@%s(%u) end", __func__, __LINE__);
	return ret;
}

JNIEXPORT jboolean JNICALL Java_com_honda_connmc_NativeLib_NativeLibController_isExistVehicleInfoNative(JNIEnv* env, jobject thiz, jstring vehicleName)
{
	bool isExist = false;
	const char* pDeviceName = NULL;
	int deviceNameLen = 0;

	DebugLogNativeD("@%s(%u) start", __func__, __LINE__);
	if (vehicleName == NULL) {
		DebugLogNativeE("%s(%d) : return = 0x%x", __func__, __LINE__, ERROR_PARAM);
		return false;
	}
	deviceNameLen = env->GetStringLength(vehicleName);
	if ((deviceNameLen <= 0) || (deviceNameLen > SECURITYDATA_VEHICLE_NAME_LENGTH_MAX)) {
		DebugLogNativeE("%s(%d) : return = 0x%x", __func__, __LINE__, ERROR_PARAM);
		return false;
	}
	pDeviceName = env->GetStringUTFChars(vehicleName, 0);

	char deviceNameData[SECURITYDATA_VEHICLE_NAME_LENGTH_MAX];
	memset(deviceNameData, 0, SECURITYDATA_VEHICLE_NAME_LENGTH_MAX);
	memcpy(deviceNameData, pDeviceName, deviceNameLen);

	isExist = gVehicleCryptoService->isExistVehicleInfo(deviceNameData);

	env->ReleaseStringUTFChars(vehicleName, pDeviceName);
	DebugLogNativeD("@%s(%u) end", __func__, __LINE__);
	return isExist;
}

JNIEXPORT jint JNICALL Java_com_honda_connmc_NativeLib_NativeLibController_generatePublicKeyNative(JNIEnv* env, jobject thiz, jbyteArray key)
{
	int ret = ERROR_GET_DATA;
	int keyLength = 0;
	jbyte* outKey = NULL;

	DebugLogNativeD("@%s(%u) start", __func__, __LINE__);
	if (key == NULL) {
		DebugLogNativeE("%s(%d) : return = 0x%x", __func__, __LINE__, ERROR_PARAM);
		return ERROR_PARAM;
	}
	keyLength = env->GetArrayLength(key);
	if (keyLength <= 0) {
		DebugLogNativeE("%s(%d) : return = 0x%x", __func__, __LINE__, ERROR_PARAM);
		return ERROR_PARAM;
	}
	jbyte keyData[keyLength];
	memset(keyData, 0, keyLength);

	ret = gVehicleCryptoService->generatePublicKey((uchar*)keyData, keyLength);
	if (ret != ERROR_NON)
	{
		DebugLogNativeE("%s(%d) : return = 0x%x", __FILE__, __LINE__, ret);
		goto finally;
	}

	outKey = env->GetByteArrayElements(key, NULL);
	memcpy(outKey, keyData, keyLength);

finally:
	if(outKey != NULL) env->ReleaseByteArrayElements(key, outKey, 0);
	DebugLogNativeD("@%s(%u) end", __func__, __LINE__);
	return ret;
}

JNIEXPORT jint JNICALL Java_com_honda_connmc_NativeLib_NativeLibController_extractSharedKeyNative(JNIEnv* env, jobject thiz, jbyteArray keydata)
{
	int ret = ERROR_GET_DATA;
	int keydataLen = 0;
	jbyte* inData = NULL;

	DebugLogNativeD("@%s(%u) start", __func__, __LINE__);
	if (keydata == NULL) {
		DebugLogNativeE("%s(%d) : return = 0x%x", __func__, __LINE__, ERROR_PARAM);
		return ERROR_PARAM;
	}
	keydataLen = env->GetArrayLength(keydata);
	if (keydataLen <= 0) {
		DebugLogNativeE("%s(%d) : return = 0x%x", __func__, __LINE__, ERROR_PARAM);
		return ERROR_PARAM;
	}
	jbyte keyData[keydataLen];
	memset(keyData, 0, keydataLen);
	inData = env->GetByteArrayElements(keydata, NULL);
	memcpy(keyData, inData, keydataLen);

	ret = gVehicleCryptoService->extractSharedKey((uchar*)keyData, keydataLen);
	if (ret != ERROR_NON)
	{
		DebugLogNativeE("%s(%d) : return = 0x%x", __FILE__, __LINE__, ret);
		goto finally;
	}

finally:
	if(inData != NULL) env->ReleaseByteArrayElements(keydata, inData, 0);
	DebugLogNativeD("@%s(%u) end", __func__, __LINE__);
	return ret;
}

JNIEXPORT jint JNICALL Java_com_honda_connmc_NativeLib_NativeLibController_getPinCodeSendDataNative(JNIEnv* env, jobject thiz, jbyteArray input, jbyteArray output)
{
	int ret = ERROR_GET_DATA;
	int inputLength = 0;
	int outputLength = 0;
	jbyte* in = NULL;
	jbyte* out = NULL;

	DebugLogNativeD("@%s(%u) start", __func__, __LINE__);
	if ((input == NULL) || (output == NULL)) {
		DebugLogNativeE("%s(%d) : return = 0x%x", __func__, __LINE__, ERROR_PARAM);
		return ERROR_PARAM;
	}
	inputLength = env->GetArrayLength(input);
	outputLength = env->GetArrayLength(output);
	if ((inputLength <= 0) || (outputLength <= 0)) {
		DebugLogNativeE("%s(%d) : return = 0x%x", __func__, __LINE__, ERROR_PARAM);
		return ERROR_PARAM;
	}
	jbyte inputData[inputLength];
	memset(inputData, 0, inputLength);
	in = env->GetByteArrayElements(input, NULL);
	memcpy(inputData, in, inputLength);

	jbyte outputData[outputLength];
	memset(outputData, 0, outputLength);

	ret = gVehicleCryptoService->getPinCodeSendData((const uchar*)inputData, inputLength, (uchar*)outputData, outputLength);
	if (ret != ERROR_NON)
	{
		DebugLogNativeE("%s(%d) : return = 0x%x", __FILE__, __LINE__, ret);
		goto finally;
	}
	out = env->GetByteArrayElements(output, NULL);
	memcpy(out, outputData, outputLength);


finally:
	if(out != NULL) env->ReleaseByteArrayElements(output, out, 0);
	if(in != NULL) env->ReleaseByteArrayElements(input, in, 0);
	DebugLogNativeD("@%s(%u) end", __func__, __LINE__);
	return ret;
}

JNIEXPORT jint JNICALL Java_com_honda_connmc_NativeLib_NativeLibController_createAuthKeyNative(JNIEnv* env, jobject thiz, jstring vehicleName, jbyteArray authKey, jbyteArray response)
{
	int ret = ERROR_GET_DATA;
	int authKeyLen = 0;
	int responseLen = 0;
	jbyte* inAuthKey = NULL;
	jbyte* outResponse = NULL;
	const char * pDeviceName = NULL;
	int deviceNameLen = 0;

	DebugLogNativeD("@%s(%u) start", __func__, __LINE__);
	if ((vehicleName == NULL) || (authKey == NULL) || (response == NULL)) {
		DebugLogNativeE("%s(%d) : return = 0x%x", __func__, __LINE__, ERROR_PARAM);
		return ERROR_PARAM;
	}

	deviceNameLen = env->GetStringLength(vehicleName);
	authKeyLen = env->GetArrayLength(authKey);
	responseLen = env->GetArrayLength(response);
	if ((deviceNameLen <= 0) || (deviceNameLen > SECURITYDATA_VEHICLE_NAME_LENGTH_MAX) ||
		(authKeyLen <= 0) || (responseLen <= 0)) {
		DebugLogNativeE("%s(%d) : return = 0x%x", __func__, __LINE__, ERROR_PARAM);
		return ERROR_PARAM;
	}

	pDeviceName = env->GetStringUTFChars(vehicleName, 0);
	char deviceNameData[SECURITYDATA_VEHICLE_NAME_LENGTH_MAX];
	memset(deviceNameData, 0, SECURITYDATA_VEHICLE_NAME_LENGTH_MAX);
	memcpy(deviceNameData, pDeviceName, deviceNameLen);

	jbyte authKeyData[authKeyLen];
	memset(authKeyData, 0, authKeyLen);
	inAuthKey = env->GetByteArrayElements(authKey, NULL);
	memcpy(authKeyData, inAuthKey, authKeyLen);

	jbyte responseData[responseLen];
	memset(responseData, 0, responseLen);

	ret = gVehicleCryptoService->createAuthKey((const uchar *)deviceNameData, (const uchar*)authKeyData, authKeyLen, (uchar*)responseData, responseLen);
	if (ret != ERROR_NON)
	{
		DebugLogNativeE("%s(%d) : return = 0x%x", __FILE__, __LINE__, ret);
		goto finally;
	}
	outResponse = env->GetByteArrayElements(response, NULL);
	memcpy(outResponse, responseData, responseLen);

finally:
	if(pDeviceName != NULL) env->ReleaseStringUTFChars(vehicleName, pDeviceName);
	if(inAuthKey != NULL) env->ReleaseByteArrayElements(authKey, inAuthKey, 0);
	if(outResponse != NULL) env->ReleaseByteArrayElements(response, outResponse, 0);
	DebugLogNativeD("@%s(%u) end", __func__, __LINE__);
	return ret;
}

JNIEXPORT jint JNICALL Java_com_honda_connmc_NativeLib_NativeLibController_deleteRegistrationKeyNative(JNIEnv* env, jobject thiz)
{
	int ret = ERROR_NON;
	DebugLogNativeD("@%s(%u) start", __func__, __LINE__);
	ret = gVehicleCryptoService->deleteRegistrationKey();
	DebugLogNativeD("@%s(%u) end", __func__, __LINE__);
	return ret;
}

JNIEXPORT jint JNICALL Java_com_honda_connmc_NativeLib_NativeLibController_stopRegistrationCryptionNative(JNIEnv* env, jobject thiz)
{
	int ret = ERROR_NON;
	DebugLogNativeD("@%s(%u) start", __func__, __LINE__);
	gVehicleCryptoService->stopRegistrationCryption();
	DebugLogNativeD("@%s(%u) end", __func__, __LINE__);
	return ret;
}

JNIEXPORT jint JNICALL Java_com_honda_connmc_NativeLib_NativeLibController_deleteVehicleInfoNative(JNIEnv* env, jobject thiz, jstring vehicleName)
{
	int ret = ERROR_NON;
	int vehicleNameLen = 0;
	const char* pDeviceName = NULL;

	DebugLogNativeD("@%s(%u) start", __func__, __LINE__);
	if (vehicleName == NULL) {
		DebugLogNativeE("%s(%d) : return = 0x%x", __func__, __LINE__, ERROR_PARAM);
		return ERROR_PARAM;
	}
	vehicleNameLen = env->GetStringLength(vehicleName);
	if ((vehicleNameLen <= 0) || (vehicleNameLen > SECURITYDATA_VEHICLE_NAME_LENGTH_MAX)) {
		DebugLogNativeE("%s(%d) : return = 0x%x", __func__, __LINE__, ERROR_PARAM);
		return ERROR_PARAM;
	}
	pDeviceName = env->GetStringUTFChars(vehicleName, 0);

	char deviceNameData[SECURITYDATA_VEHICLE_NAME_LENGTH_MAX];
	memset(deviceNameData, 0, SECURITYDATA_VEHICLE_NAME_LENGTH_MAX);
	memcpy(deviceNameData, pDeviceName, vehicleNameLen);

	ret = gVehicleCryptoService->deleteVehicleInfo(deviceNameData);
	env->ReleaseStringUTFChars(vehicleName, pDeviceName);
	DebugLogNativeD("@%s(%u) end", __func__, __LINE__);
	return ret;
}

JNIEXPORT jint JNICALL Java_com_honda_connmc_NativeLib_NativeLibController_tryAuthenticationNative(JNIEnv* env, jobject thiz, jstring vehicleName, jbyteArray targetRandom, jbyteArray vehicleAuthValue, jbyteArray responseAuthValue)
{
	int ret = ERROR_GET_DATA;
	int targetRandomLen = 0;
	int vehicleAuthValueLen = 0;
	int responseAuthValueLen = 0;
	int vehicleNameLen = 0;
	const char* pDevName = NULL;
	jbyte* inTargetRndom = NULL;
	jbyte* inVehicleAuthValue = NULL;
	jbyte* outResponseAuthValue = NULL;

	DebugLogNativeD("@%s(%u) start", __func__, __LINE__);

	if ((vehicleName == NULL) || (targetRandom == NULL) || (vehicleAuthValue == NULL) || (responseAuthValue == NULL)) {
		DebugLogNativeE("%s(%d) : return = 0x%x", __func__, __LINE__, ERROR_PARAM);
		return ERROR_PARAM;
	}

	vehicleNameLen = env->GetStringLength(vehicleName);
	targetRandomLen = env->GetArrayLength(targetRandom);
	vehicleAuthValueLen = env->GetArrayLength(vehicleAuthValue);
	responseAuthValueLen = env->GetArrayLength(responseAuthValue);
	if ((vehicleNameLen <= 0) || (vehicleNameLen > SECURITYDATA_VEHICLE_NAME_LENGTH_MAX) ||
		(targetRandomLen <= 0) || (vehicleAuthValueLen <= 0) || (responseAuthValueLen <= 0)) {
		DebugLogNativeE("%s(%d) : return = 0x%x", __func__, __LINE__, ERROR_PARAM);
		return ERROR_PARAM;
	}

	pDevName = env->GetStringUTFChars(vehicleName, 0);
	char deviceNameData[SECURITYDATA_VEHICLE_NAME_LENGTH_MAX];
	memset(deviceNameData, 0, SECURITYDATA_VEHICLE_NAME_LENGTH_MAX);
	memcpy(deviceNameData, pDevName, vehicleNameLen);

	jbyte targetRandomData[targetRandomLen];
	memset(targetRandomData, 0, targetRandomLen);
	inTargetRndom = env->GetByteArrayElements(targetRandom, NULL);
	memcpy(targetRandomData, inTargetRndom, targetRandomLen);

	jbyte vehicleAuthValueData[vehicleAuthValueLen];
	memset(vehicleAuthValueData, 0, vehicleAuthValueLen);
	inVehicleAuthValue = env->GetByteArrayElements(vehicleAuthValue, NULL);
	memcpy(vehicleAuthValueData, inVehicleAuthValue, vehicleAuthValueLen);

	jbyte responseAuthValueData[responseAuthValueLen];
	memset(responseAuthValueData, 0, responseAuthValueLen);

	ret = gVehicleCryptoService->tryAuthentication((const uchar*)deviceNameData,
												   (const uchar*)targetRandomData,
												   targetRandomLen,
												   (const uchar*)vehicleAuthValueData,
												   vehicleAuthValueLen,
												   (uchar*)responseAuthValueData,
												   responseAuthValueLen);
	if (ret != ERROR_NON)
	{
		DebugLogNativeE("%s(%d) : return = 0x%x", __FILE__, __LINE__, ret);
		goto finally;
	}
	outResponseAuthValue = env->GetByteArrayElements(responseAuthValue, NULL);
	memcpy(outResponseAuthValue, responseAuthValueData, responseAuthValueLen);


finally:
	if(pDevName != NULL) env->ReleaseStringUTFChars(vehicleName, pDevName);
	if(outResponseAuthValue != NULL) env->ReleaseByteArrayElements(responseAuthValue, outResponseAuthValue, 0);
	if(inVehicleAuthValue != NULL) env->ReleaseByteArrayElements(vehicleAuthValue, inVehicleAuthValue, 0);
	if(inTargetRndom != NULL) env->ReleaseByteArrayElements(targetRandom, inTargetRndom, 0);
	DebugLogNativeD("@%s(%u) end", __func__, __LINE__);
	return ret;
}

JNIEXPORT jint JNICALL Java_com_honda_connmc_NativeLib_NativeLibController_decryptoReceivedDataNative(JNIEnv* env, jobject thiz, jbyteArray targetData, jbyteArray result)
{
	int ret = ERROR_GET_DATA;
	int targetDataLen = 0;
	int resultLength = 0;
	jbyte* inTargetData = NULL;
	jbyte* outResult = NULL;

	DebugLogNativeD("@%s(%u) start", __func__, __LINE__);
	if ((targetData == NULL) || (result == NULL)) {
		DebugLogNativeE("%s(%d) : return = 0x%x", __func__, __LINE__, ERROR_PARAM);
		return ERROR_PARAM;
	}

	targetDataLen = env->GetArrayLength(targetData);
	resultLength = env->GetArrayLength(result);
	if ((targetDataLen <= 0) || (resultLength <= 0)) {
		DebugLogNativeE("%s(%d) : return = 0x%x", __func__, __LINE__, ERROR_PARAM);
		return ERROR_PARAM;
	}

	jbyte targetRandomData[targetDataLen];
	memset(targetRandomData, 0, targetDataLen);
	inTargetData = env->GetByteArrayElements(targetData, NULL);
	memcpy(targetRandomData, inTargetData, targetDataLen);

	jbyte resultData[resultLength];
	memset(resultData, 0, resultLength);

	ret = gVehicleCryptoService->decryptReceivedData((const uchar*)targetRandomData,
													 targetDataLen,
													 (uchar*)resultData,
													 resultLength);
	if (ret != ERROR_NON)
	{
		DebugLogNativeE("%s(%d) : return = 0x%x", __FILE__, __LINE__, ret);
		goto finally;
	}
	outResult = env->GetByteArrayElements(result, NULL);
	memcpy(outResult, resultData, resultLength);

finally:
	if(inTargetData != NULL) env->ReleaseByteArrayElements(targetData, inTargetData, 0);
	if(outResult != NULL) env->ReleaseByteArrayElements(result, outResult, 0);
	DebugLogNativeD("@%s(%u) end", __func__, __LINE__);
	return 0;
}

/* FPT #176134 S */
JNIEXPORT jint JNICALL Java_com_honda_connmc_NativeLib_NativeLibController_deleteFileNative(JNIEnv* env, jobject thiz) {
	int ret = ERROR_GET_DATA;
	ret = gVehicleCryptoService->deleteFile();
	if (ret != ERROR_NON) {
		DebugLogNativeE("%s(%d) : return = 0x%x", __FILE__, __LINE__, ret);
	}
	return ret;
}
/* FPT #176134 E */

JNIEXPORT jint JNICALL Java_com_honda_connmc_NativeLib_NativeLibController_encryptoSendDataNative(JNIEnv* env, jobject thiz, jbyteArray targetData, jbyteArray result)
{
	int ret = ERROR_GET_DATA;
	int targetDataLen = 0;
	int resultLength = 0;
	jbyte* inTargetData= NULL;
	jbyte* outResult = NULL;

	DebugLogNativeD("@%s(%u) start", __func__, __LINE__);
	if ((targetData == NULL) || (result == NULL)) {
		DebugLogNativeE("%s(%d) : return = 0x%x", __func__, __LINE__, ERROR_PARAM);
		return ERROR_PARAM;
	}
	targetDataLen = env->GetArrayLength(targetData);
	resultLength = env->GetArrayLength(result);
	if ((targetDataLen <= 0) || (resultLength <= 0)) {
		DebugLogNativeE("%s(%d) : return = 0x%x", __func__, __LINE__, ERROR_PARAM);
		return ERROR_PARAM;
	}
	inTargetData = env->GetByteArrayElements(targetData, NULL);
	jbyte targetRandomData[targetDataLen];
	memset(targetRandomData, 0, targetDataLen);
	memcpy(targetRandomData, inTargetData, targetDataLen);

	jbyte resultData[resultLength];
	memset(resultData, 0, resultLength);

	ret = gVehicleCryptoService->encryptSendData((const uchar*)targetRandomData, targetDataLen, (uchar*)resultData, resultLength);
	if (ret != ERROR_NON) {
		DebugLogNativeE("%s(%d) : return = 0x%x", __FILE__, __LINE__, ret);
		goto finally;
	}
	outResult = env->GetByteArrayElements(result, NULL);
	memcpy(outResult, resultData, resultLength);

finally:
	if(inTargetData != NULL) env->ReleaseByteArrayElements(targetData, inTargetData, 0);
	if(outResult != NULL) env->ReleaseByteArrayElements(result, outResult, 0);
	DebugLogNativeD("@%s(%u) end", __func__, __LINE__);
	return ret;
}

JNIEXPORT jint JNICALL Java_com_honda_connmc_NativeLib_NativeLibController_stopAuthenticationDataCryptionNative(JNIEnv* env, jobject thiz)
{
	int ret = ERROR_NON;
	DebugLogNativeD("@%s(%u) start", __func__, __LINE__);
	gVehicleCryptoService->stopAuthenticationDataCryption();
	DebugLogNativeD("@%s(%u) end", __func__, __LINE__);
	return ret;
}
