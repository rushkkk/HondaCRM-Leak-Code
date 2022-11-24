#include "AuthenticationCrypto.h"

#include <android/log.h>
#include <jni.h>
#include <stdio.h>
#include <string.h>
#include "CommonCrypto.h"

#define MESSAGE_CRYPTOKEY_SIZE 32
#define MESSAGE_CRYPTOBLOCK_SIZE 16
#define RANDOM_SIZE 32

AuthenticationCrypto::AuthenticationCrypto(SecurityDataManager* secDataManager) :
mSecurityDataManager(secDataManager)
{
	if ((mMessageCryptoIv = (unsigned char*)malloc(MESSAGE_CRYPTOBLOCK_SIZE)) != NULL) {
		memset(mMessageCryptoIv, 0, MESSAGE_CRYPTOBLOCK_SIZE);
	}
	if ((mMessageCryptoKey = (unsigned char*)malloc(MESSAGE_CRYPTOKEY_SIZE)) != NULL) {
		memset(mMessageCryptoKey, 0, MESSAGE_CRYPTOKEY_SIZE);
	}
}

AuthenticationCrypto::~AuthenticationCrypto()
{
	if (mMessageCryptoIv != NULL) free(mMessageCryptoIv);
	if (mMessageCryptoKey != NULL) free(mMessageCryptoKey);
	mSecurityDataManager = NULL;
}

int AuthenticationCrypto::tryAuthentication(const uchar* vehicleName, const uchar* targetRandom, int randomLength, const uchar* vehicleAuthData, int vehicleAuthDataLength, uchar* responseAuthValue, int responseLength)
{
	int iRet = ERROR_NON;
	unsigned char* firstAuthKey = NULL;

	DebugLogNativeD("@%s(%u) start", __func__, __LINE__);
	if ((vehicleAuthDataLength < MESSAGE_CRYPTOKEY_SIZE) || (responseLength < MESSAGE_CRYPTOKEY_SIZE)
		|| (randomLength < MESSAGE_CRYPTOKEY_SIZE)) {
		iRet = ERROR_PARAM;
		DebugLogNativeE("@%s(%u) parameter error", __func__, __LINE__);
		DebugLogNativeE("vehicleAuthDataLength(%d)", vehicleAuthDataLength);
		DebugLogNativeE("esponseLength(%d)", responseLength);
		DebugLogNativeE("randomLength(%d)", randomLength);
		goto finally;
	}
	// 1. get CID KE KD from SecurityDataManager
	// 2. create key for 1stauthentication (only CID and KD)
	if ((firstAuthKey = createFirstAuthenticationKey(vehicleName)) == NULL) {
		iRet = ERROR_GET_DATA;
		DebugLogNativeE("@%s(%u) createFirstAuthenticationKey", __func__, __LINE__);
		goto finally;
	}

	// 3. encrypt random data with key created procedure 2
	// 4. memcmp vehicleAuthdata and result of procedure 3
	if ((iRet = doFirstAuthentication(targetRandom, randomLength, vehicleAuthData, vehicleAuthDataLength, firstAuthKey)) != ERROR_NON) {
		DebugLogNativeE("@%s(%u) doFirstAuthentication:ret=%d", __func__, __LINE__, iRet);
		goto finally;
	}

	// 5. get TID from SecurityDataManager
	// 6. create key for 2nd authentication (Both CID, KD, TID)
	if ((iRet = createMessageCryptoKey(firstAuthKey)) != ERROR_NON) {
		DebugLogNativeE("@%s(%u) createMessageCryptoKey:ret=%d", __func__, __LINE__, iRet);
		goto finally;
	}
	// 7. encrypt randam data with key created procedure 6
	// 8. set random value as IV
	if ((iRet = doSecondAuthentication(targetRandom, randomLength, responseAuthValue, responseLength)) != ERROR_NON) {
		DebugLogNativeE("@%s(%u) doSecondAuthentication:ret=%d", __func__, __LINE__, iRet);
		goto finally;
	}
	DebugLogNativeD("@%s(%u) end with success", __func__, __LINE__);
finally:
	if (firstAuthKey != NULL) free(firstAuthKey);
	return iRet;
}

int AuthenticationCrypto::decryptReceivedData(const uchar* targetData, int targetDataLength, uchar* result, int resultLength)
{
	int iRet = ERROR_NON;
	unsigned char* inbuf = NULL;
	CommonCrypto* crypto = NULL;

	DebugLogNativeD("@%s(%u) start", __func__, __LINE__);
	if ((targetData == NULL) || (targetDataLength <= 0)) {
		iRet = ERROR_PARAM;
		DebugLogNativeE("@%s(%u) memory allocate error", __func__, __LINE__);
		goto finally;
	}
	if ((mMessageCryptoKey == NULL) || (mMessageCryptoIv == NULL)) {
		iRet = ERROR_PROGRAM;
		DebugLogNativeE("@%s(%u) memory allocate error", __func__, __LINE__);
		goto finally;
	}
	if ((inbuf = (unsigned char*)malloc(targetDataLength)) == NULL) {
		iRet = ERROR_GET_DATA;
		DebugLogNativeE("@%s(%u) memory allocate error", __func__, __LINE__);
		goto finally;
	}
	memset(inbuf, 0, targetDataLength);
	memcpy(inbuf, targetData, targetDataLength);

	crypto = new CommonCrypto();
	crypto->setIv(mMessageCryptoIv, MESSAGE_CRYPTOBLOCK_SIZE);
	crypto->setKey(mMessageCryptoKey);
	crypto->setBuf(inbuf, targetDataLength, result, resultLength);
	if ((iRet = crypto->decrypt(CIPHER_TRANSFORMATION_AES256_PKCS1PADDING)) != ERROR_NON) {
		DebugLogNativeE("@%s(%u) decrypt:ret=%d", __func__, __LINE__, iRet);
		goto finally;
	}

	DebugLogNativeD("@%s(%u) end with success", __func__, __LINE__);
finally:
	delete crypto;
	if (inbuf != NULL) free(inbuf);
	return iRet;
}

int AuthenticationCrypto::encryptSendData(const uchar* targetData, int targetDataLength, uchar* result, int resultLength)
{
	int iRet = ERROR_NON;
	unsigned char* inbuf = NULL;
	CommonCrypto* crypto = NULL;

	DebugLogNativeD("@%s(%u) start", __func__, __LINE__);
	if ((targetData == NULL) || (targetDataLength <= 0)) {
		iRet = ERROR_PARAM;
		DebugLogNativeE("@%s(%u) memory allocate error", __func__, __LINE__);
		goto finally;
	}
	if ((mMessageCryptoKey == NULL) || (mMessageCryptoIv == NULL)) {
		iRet = ERROR_PROGRAM;
		DebugLogNativeE("@%s(%u) memory allocate error", __func__, __LINE__);
		goto finally;
	}
	if ((inbuf = (unsigned char*)malloc(targetDataLength)) == NULL) {
		iRet = ERROR_GET_DATA;
		DebugLogNativeE("@%s(%u) memory allocate error", __func__, __LINE__);
		goto finally;
	}
	memset(inbuf, 0, targetDataLength);
	memcpy(inbuf, targetData, targetDataLength);

	crypto = new CommonCrypto();
	crypto->setIv(mMessageCryptoIv, MESSAGE_CRYPTOBLOCK_SIZE);
	crypto->setKey(mMessageCryptoKey);
	crypto->setBuf(inbuf, targetDataLength, result, resultLength);
	if ((iRet = crypto->encrypt(CIPHER_TRANSFORMATION_AES256_PKCS1PADDING)) != ERROR_NON) {
		DebugLogNativeE("@%s(%u) encrypt:ret=%d", __func__, __LINE__, iRet);
		goto finally;
	}

	DebugLogNativeD("@%s(%u) end with success", __func__, __LINE__);

finally:
	delete crypto;
	if (inbuf != NULL) free(inbuf);
	return iRet;
}

AuthenticationCrypto::AuthenticationCrypto()  :
mSecurityDataManager(NULL)
{
}

void AuthenticationCrypto::operator =(const AuthenticationCrypto& src)
{
}

AuthenticationCrypto::AuthenticationCrypto(const AuthenticationCrypto& src) :
mSecurityDataManager(NULL)
{
}

unsigned char* AuthenticationCrypto::createFirstAuthenticationKey(const uchar* vehicleName)
{
	int iRet = ERROR_NON;
	SecurityVehicleInfo vehicleInfo;
	unsigned char* authKey = NULL;
	unsigned char* kd = NULL;
	unsigned char* cid = NULL;
//	int i = 0;

//	DebugLogNativeD("@%s(%u) start", __func__, __LINE__);
	if ((iRet = getCorrespondVehicleSecInfo(vehicleName, &vehicleInfo)) != ERROR_NON) {
		DebugLogNativeE("@%s(%u) getCorrespondVehicleSecInfo:ret=%d", __func__, __LINE__, iRet);
		goto finally;
	}
	if ((cid = (unsigned char*)malloc(SECURITYDATA_VEHICLE_CID_LENGTH + 1)) == NULL) {
		DebugLogNativeE("@%s(%u) allocation error(cid)", __func__, __LINE__);
		goto finally;
	}
	memset(cid, 0, SECURITYDATA_VEHICLE_CID_LENGTH +1);
	memcpy(cid, vehicleInfo.cid, SECURITYDATA_VEHICLE_CID_LENGTH);

	if ((kd = (unsigned char*)malloc(SECURITYDATA_VEHICLE_KD_LENGTH +1)) == NULL) {
		DebugLogNativeE("@%s(%u) allocation error(kd)", __func__, __LINE__);
		goto finally;
	}
	memset(kd, 0, SECURITYDATA_VEHICLE_KD_LENGTH);
	memcpy(kd, vehicleInfo.kd, SECURITYDATA_VEHICLE_KD_LENGTH);

//	for (i = 0; i < SECURITYDATA_VEHICLE_CID_LENGTH; i++) {
//		DebugLogNativeD("cid[%d]=0x%02x", i, cid[i]);
//	}
//	for (i = 0; i < SECURITYDATA_VEHICLE_KD_LENGTH; i++) {
//		DebugLogNativeD("kd[%d]=0x%02x", i, kd[i]);
//	}

	if ((authKey = (unsigned char*)malloc(MESSAGE_CRYPTOKEY_SIZE)) == NULL) {
		iRet = ERROR_GET_DATA;
		DebugLogNativeE("@%s(%u) allocation error(authKey)", __func__, __LINE__);
		goto finally;
	}
	memset(authKey, 0, MESSAGE_CRYPTOKEY_SIZE);
	memcpy(authKey, kd, SECURITYDATA_VEHICLE_KD_LENGTH);

	memcpy(&authKey[SECURITYDATA_VEHICLE_KD_LENGTH], cid, SECURITYDATA_VEHICLE_CID_LENGTH);
//	DebugLogNativeD("@%s(%u) end with success", __func__, __LINE__);

finally:
	if (cid != NULL) free(cid);
	if (kd != NULL) free(kd);
	return authKey;
}

int AuthenticationCrypto::createMessageCryptoKey(unsigned char* firstKey)
{
	int iRet = ERROR_NON;
//	int i = 0;
	unsigned char* tid = NULL;

//	DebugLogNativeD("@%s(%u) start", __func__, __LINE__);
	if ((tid = (unsigned char*)malloc(SECURITYDATA_USER_TID_LENGTH + 1)) == NULL) {
		iRet = ERROR_GET_DATA;
		DebugLogNativeE("@%s(%u) allocation error(tid)", __func__, __LINE__);
		goto finally;
	}
	memset(tid, 0, SECURITYDATA_USER_TID_LENGTH);

	if ((iRet = mSecurityDataManager->getDeviceId(tid, SECURITYDATA_USER_TID_LENGTH + 1)) != ERROR_NON) {
		DebugLogNativeE("@%s(%u) getDeviceId:ret=%d", __func__, __LINE__, iRet);
		goto finally;
	}

//	for (i = 0; i < SECURITYDATA_USER_TID_LENGTH; i++) {
//		tid[i] ^= 0x00;
//	}
	if (mMessageCryptoKey == NULL) {
		if ((mMessageCryptoKey = (unsigned char*)malloc(MESSAGE_CRYPTOKEY_SIZE)) == NULL){
			iRet = ERROR_GET_DATA;
			DebugLogNativeE("@%s(%u) allocation error(mMessageCryptoKey)", __func__, __LINE__);
			goto finally;
		}
	}
	memcpy(mMessageCryptoKey, firstKey, MESSAGE_CRYPTOKEY_SIZE);
	memcpy(&mMessageCryptoKey[SECURITYDATA_VEHICLE_KD_LENGTH + SECURITYDATA_VEHICLE_CID_LENGTH], tid, SECURITYDATA_USER_TID_LENGTH);

//	DebugLogNativeD("@%s(%u) end with success", __func__, __LINE__);

finally:
	if (tid != NULL) free(tid);
	return iRet;
}

int AuthenticationCrypto::doFirstAuthentication(const uchar* targetRandom, int randomLength, const uchar* vehicleAuthData, int vehicleAuthDataLength, unsigned char* cryptoKey)
{
	int iRet = ERROR_NON;
	unsigned char* tryData = NULL;
	unsigned char* result = NULL;
	CommonCrypto* cryptor = NULL;
//	int i = 0;

//	DebugLogNativeD("@%s(%u) start", __func__, __LINE__);
	if ((targetRandom == NULL) || (randomLength != RANDOM_SIZE)) {
		iRet = ERROR_PARAM;
		DebugLogNativeE("@%s(%u) allocation error(tryData)", __func__, __LINE__);
		goto finally;
	}
	if ((vehicleAuthData == NULL) || (vehicleAuthDataLength != RANDOM_SIZE)) {
		iRet = ERROR_PARAM;
		DebugLogNativeE("@%s(%u) allocation error(tryData)", __func__, __LINE__);
		goto finally;
	}
	if ((tryData = (unsigned char*)malloc(RANDOM_SIZE)) == NULL) {
		iRet = ERROR_GET_DATA;
		DebugLogNativeE("@%s(%u) allocation error(tryData)", __func__, __LINE__);
		goto finally;
	}
	memset(tryData, 0, RANDOM_SIZE);
	memcpy(tryData, targetRandom, RANDOM_SIZE);

	if ((result = (unsigned char*)malloc(RANDOM_SIZE)) == NULL) {
		iRet = ERROR_GET_DATA;
		DebugLogNativeE("@%s(%u) allocation error(result)", __func__, __LINE__);
		goto finally;
	}
	memset(result, 0, RANDOM_SIZE);
//	for(i = 0; i < MESSAGE_CRYPTOKEY_SIZE; i++) {
//		DebugLogNativeD("CommonCrypto[%d]=0x%02x", i, cryptoKey[i]);
//	}

	cryptor = new CommonCrypto();
	cryptor->setKey(cryptoKey);
	cryptor->setBuf(tryData, RANDOM_SIZE, result, RANDOM_SIZE);
	if ((iRet = cryptor->encrypt(CIPHER_TRANSFORMATION_AES256_ECB_NOPAD)) != 0) {
		DebugLogNativeE("@%s(%u) encrypt:ret=%d", __func__, __LINE__, iRet);
		goto finally;
	}

	// 4. memcmp vehicleAuthdata and result of procedure 3
	if ((iRet = memcmp(result, vehicleAuthData, RANDOM_SIZE)) != 0) {
		DebugLogNativeE("@%s(%u) memcmp:ret=%d", __func__, __LINE__, iRet);
		goto finally;
	}
//	DebugLogNativeD("@%s(%u) end with success", __func__, __LINE__);

finally:
	if (tryData != NULL) free(tryData);
	if (result != NULL) free(result);
	delete cryptor;
	return iRet;
}

int AuthenticationCrypto::doSecondAuthentication(const uchar* targetRandom, int randomLength, uchar* resultData, int resultDataLength) {
	int iRet = ERROR_NON;
//	int i = 0;
	unsigned char* inbuf = NULL;
	CommonCrypto* encryptor = NULL;

//	DebugLogNativeD("@%s(%u) start", __func__, __LINE__);
	if ((targetRandom == NULL) || (randomLength != RANDOM_SIZE)) {
		iRet = ERROR_PARAM;
		DebugLogNativeE("@%s(%u) allocation error(tryData)", __func__, __LINE__);
		goto finally;
	}
	if ((inbuf = (unsigned char*)malloc(RANDOM_SIZE)) == NULL) {
		iRet = ERROR_GET_DATA;
		DebugLogNativeE("@%s(%u) allocation error(inbuf)", __func__, __LINE__);
		goto finally;
	}
	memset(inbuf, 0, RANDOM_SIZE);
	memcpy(inbuf, targetRandom, RANDOM_SIZE);

	encryptor = new CommonCrypto();
	encryptor->setKey(mMessageCryptoKey);
//	for(i = 0; i < MESSAGE_CRYPTOKEY_SIZE; i++) {
//		DebugLogNativeD("CommonCrypto[%d]=0x%02x", i, mMessageCryptoKey[i]);
//	}
	encryptor->setBuf(inbuf, RANDOM_SIZE, resultData, resultDataLength);
	if ((iRet = encryptor->encrypt(CIPHER_TRANSFORMATION_AES256_ECB_NOPAD)) != ERROR_NON) {
		DebugLogNativeE("@%s(%u) encrypt:ret=%d", __func__, __LINE__, iRet);
		memset(mMessageCryptoKey, 0, MESSAGE_CRYPTOKEY_SIZE);
		free(mMessageCryptoKey);
		mMessageCryptoKey = NULL;
		goto finally;
	}

	if (mMessageCryptoIv == NULL) {
		if ((mMessageCryptoIv = (unsigned char*)malloc(MESSAGE_CRYPTOBLOCK_SIZE)) == NULL) {
			iRet = ERROR_GET_DATA;
			DebugLogNativeE("@%s(%u) allocation error(mMessageCryptoIv)", __func__, __LINE__);
			goto finally;
		}
	}
	memcpy(mMessageCryptoIv, inbuf, MESSAGE_CRYPTOBLOCK_SIZE);
//	DebugLogNativeD("@%s(%u) end with success", __func__, __LINE__);

finally:
	if (inbuf != NULL) free(inbuf);
	delete encryptor;
	return iRet;
}

int AuthenticationCrypto::getCorrespondVehicleSecInfo(const uchar* vehicleName, SecurityVehicleInfo* out) {
	int iRet = ERROR_NON;
	int i = 0;
	int vehicleNameLen = 0;
	SecurityVehicleInfo info[SECURITYDATA_VEHICLE_NUM_MAX];

	DebugLogNativeD("@%s(%u) start", __func__, __LINE__);
	if (vehicleName == NULL) {
		DebugLogNativeE("@%s(%u) parameter error", __func__, __LINE__);
		return ERROR_PARAM;
	}
	vehicleNameLen = strlen((const char*)vehicleName);
	if ((vehicleNameLen <= 0) || (vehicleNameLen > SECURITYDATA_VEHICLE_NAME_LENGTH_MAX)) {
		DebugLogNativeE("@%s(%u) parameter error", __func__, __LINE__);
		return ERROR_PARAM;
	}
	memset(info, 0, (sizeof(SecurityVehicleInfo) * SECURITYDATA_VEHICLE_NUM_MAX));
	if (((iRet = mSecurityDataManager->getConnectVehicleSecInfo(info)) != ERROR_NON) || (NULL == info)){
		DebugLogNativeE("@%s(%u) getConnectVehicleSecInfo:ret=%d", __func__, __LINE__, iRet);
		return iRet;
	}

	for(i = 0; i < SECURITYDATA_VEHICLE_NUM_MAX; i++)	{
		if((info[i].nameSize == vehicleNameLen) && ((iRet = memcmp(vehicleName, info[i].name, vehicleNameLen)) == 0)) {
			memcpy(out->cid, info[i].cid, SECURITYDATA_VEHICLE_CID_LENGTH);
			memcpy(out->kd, info[i].kd, SECURITYDATA_VEHICLE_KD_LENGTH);
			out->nameSize = info[i].nameSize;
//			DebugLogNativeD("correspond name[%d]=%s", i, info[i].name);
//			int l = 0;
//			for (l = 0; l < SECURITYDATA_VEHICLE_CID_LENGTH; l++) {
//				DebugLogNativeD("correspond cid_value[%d]=0x%02x", l, info[i].cid[l]);
//			}
//			for (l = 0; l < SECURITYDATA_VEHICLE_KD_LENGTH; l++) {
//				DebugLogNativeD("correspond kd_value[%d]=0x%02x", l, info[i].kd[l]);
//			}
			DebugLogNativeD("@%s(%u) end with found", __func__, __LINE__);
			return ERROR_NON;
		} else if (info[i].nameSize != 0) {
//			int l = 0;
//			DebugLogNativeD("name[%d]=%s", i, info[i].name);
//			for (l = 0; l < SECURITYDATA_VEHICLE_CID_LENGTH; l++) {
//				DebugLogNativeD("cid_value[%d]=0x%02x", l, info[i].cid[l]);
//			}
//			for (l = 0; l < SECURITYDATA_VEHICLE_KD_LENGTH; l++) {
//				DebugLogNativeD("kd_value[%d]=0x%02x", l, info[i].kd[l]);
//			}
		}
	}
	iRet = ERROR_DATA_NOT_FOUND;
	DebugLogNativeE("@%s(%u) end not found", __func__, __LINE__);
	return iRet;
}

