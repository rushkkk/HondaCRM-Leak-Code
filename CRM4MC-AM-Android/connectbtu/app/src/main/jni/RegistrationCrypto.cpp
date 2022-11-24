#include <android/log.h>
#include <string.h>
#include "RegistrationCrypto.h"
#include "VehicleCryptoCommon.h"
#include "CommonCrypto.h"
#include "SecurityDataManager.h"

#define RSA_PRIVATE_KEY_BUF_SIZE 2048
#define ENCRYPTED_AES_SHAREDKEY_SIZE 256
#define REGISTRATION_AES_SHAREDKEY_SIZE 48
#define REGISTRATION_PINCODE_BUFSIZE 32
#define REGISTRATION_AUTHKEYDATA_SIZE 64
#define REGISTRATION_AUTHRESPDATA_SIZE 32

RegistrationCrypto::RegistrationCrypto(SecurityDataManager* secDataManager) :
mSecurityDataManager(secDataManager)
{
	if ((mRsaPrivateKey = (unsigned char *)malloc(RSA_PRIVATE_KEY_BUF_SIZE)) != NULL) {
		memset(mRsaPrivateKey, 0, RSA_PRIVATE_KEY_BUF_SIZE);
	}
	if ((mAesSharedKey = (unsigned char *)malloc(REGISTRATION_AES_SHAREDKEY_SIZE)) != NULL) {
		memset(mAesSharedKey, 0, REGISTRATION_AES_SHAREDKEY_SIZE);
	}
}

RegistrationCrypto::~RegistrationCrypto()
{
	if (mRsaPrivateKey != NULL) free(mRsaPrivateKey);
	if (mAesSharedKey != NULL) free(mAesSharedKey);
	mSecurityDataManager = NULL;
}

int RegistrationCrypto::createRsaPairKey(uchar* publicKey, int keyLength)
{
	int iRet = ERROR_NON;
	CommonCrypto*	crypto	= new CommonCrypto();
	DebugLogNativeD("@%s(%u) start", __func__, __LINE__);
	if (mRsaPrivateKey == NULL) {
		if ((mRsaPrivateKey = (unsigned char*)malloc(RSA_PRIVATE_KEY_BUF_SIZE)) == NULL) {
			iRet = ERROR_GET_DATA;
			DebugLogNativeE("@%s(%u) allocation error(mRsaPrivateKey)", __func__, __LINE__);
			goto finally;
		}
	}
	if ((iRet = crypto->createRsaPairKey(publicKey, keyLength, mRsaPrivateKey, RSA_PRIVATE_KEY_BUF_SIZE)) != ERROR_NON) {
		DebugLogNativeE("@%s(%u) createRsaPairKey:ret=%d", __func__, __LINE__, iRet);
		free(mRsaPrivateKey);
		mRsaPrivateKey = NULL;
		goto finally;
	}
	DebugLogNativeD("@%s(%u) end with success", __func__, __LINE__);
finally:
	delete crypto;
	return iRet;
}

int RegistrationCrypto::extractAesSharedKey(const char* encryptSharedKey, int keyDataLength)
{
	int iRet = ERROR_NON;
	unsigned char* inbuf = NULL;
	unsigned char* outbuf = NULL;
	CommonCrypto* crypto = NULL;

	// input data check
	DebugLogNativeD("@%s(%u) start", __func__, __LINE__);
	if ((encryptSharedKey == NULL) || (keyDataLength < ENCRYPTED_AES_SHAREDKEY_SIZE)) {
		iRet = ERROR_PARAM;
		DebugLogNativeE("@%s(%u) parameter error(keyDataLength=%d)", __func__, __LINE__, keyDataLength);
		goto finally;
	}
	if (mRsaPrivateKey == NULL) {
		iRet = ERROR_PARAM;
		DebugLogNativeE("@%s(%u) mRsaPrivateKey == NULL", __func__, __LINE__);
		goto finally;
	}
	if ((inbuf = (unsigned char*)malloc(ENCRYPTED_AES_SHAREDKEY_SIZE)) == NULL) {
		iRet = ERROR_GET_DATA;
		DebugLogNativeE("@%s(%u) allocation error(inbuf)", __func__, __LINE__);
		goto finally;
	}
	memset(inbuf, 0, ENCRYPTED_AES_SHAREDKEY_SIZE);
	memcpy(inbuf, encryptSharedKey, ENCRYPTED_AES_SHAREDKEY_SIZE);

	if ((outbuf = (unsigned char*)malloc(ENCRYPTED_AES_SHAREDKEY_SIZE)) == NULL) {
		iRet = ERROR_GET_DATA;
		DebugLogNativeE("@%s(%u) allocation error(outbuf)", __func__, __LINE__);
		goto finally;
	}
	memset(outbuf, 0, ENCRYPTED_AES_SHAREDKEY_SIZE);

	crypto = new CommonCrypto();
	crypto->setKey(mRsaPrivateKey);
	crypto->setBuf(inbuf, keyDataLength, outbuf, ENCRYPTED_AES_SHAREDKEY_SIZE);
	if ((iRet = crypto->decrypt(CIPHER_TRANSFORMATION_RSA2048_ECB_PKCS1PADDING)) != ERROR_NON) {
		DebugLogNativeE("@%s(%u) decrypt:ret=%d", __func__, __LINE__, iRet);
		free(mRsaPrivateKey);
		free(mAesSharedKey);
		mRsaPrivateKey = NULL;
		mAesSharedKey = NULL;
		goto finally;
	}

	if (mAesSharedKey == NULL) {
		if ((mAesSharedKey = (unsigned char*)malloc(REGISTRATION_AES_SHAREDKEY_SIZE)) == NULL) {
			iRet = ERROR_GET_DATA;
			DebugLogNativeE("@%s(%u) allocation error(mAesSharedKey)", __func__, __LINE__);
			goto finally;
		}
	}
	memcpy(mAesSharedKey, outbuf, REGISTRATION_AES_SHAREDKEY_SIZE);
	DebugLogNativeD("@%s(%u) end with success", __func__, __LINE__);

finally:
	delete crypto;
	if (inbuf != NULL) free(inbuf);
	if (outbuf != NULL) free(outbuf);
	return iRet;
}

int RegistrationCrypto::createPinCodeData(const uchar* input, int inputLength, uchar* output, int outputLength)
{
	int iRet = ERROR_NON;
	unsigned char* inputbuf = NULL;
	CommonCrypto* crypto = NULL;

	DebugLogNativeD("@%s(%u) start", __func__, __LINE__);
	if ((input == NULL) || (inputLength < REGISTRATION_PINCODE_BUFSIZE)) {
		iRet = ERROR_PARAM;
		DebugLogNativeE("@%s(%u) parameter error", __func__, __LINE__);
		goto finally;
	}
	if (mAesSharedKey == NULL) {
		iRet = ERROR_GET_DATA;
		DebugLogNativeE("@%s(%u) mAesSharedKey == NULL", __func__, __LINE__);
		goto finally;
	}
	if ((inputbuf = (unsigned char*)malloc(REGISTRATION_PINCODE_BUFSIZE)) == NULL) {
		iRet = ERROR_GET_DATA;
		DebugLogNativeE("@%s(%u) allocation error(inputbuf)", __func__, __LINE__);
		goto finally;
	}
	memset(inputbuf, 0, REGISTRATION_PINCODE_BUFSIZE);
	memcpy(inputbuf, input, REGISTRATION_PINCODE_BUFSIZE);

	crypto= new CommonCrypto();
	crypto->setKey(mAesSharedKey);

	crypto->setBuf(inputbuf, REGISTRATION_PINCODE_BUFSIZE, output, outputLength);
	if ((iRet = crypto->encrypt(CIPHER_TRANSFORMATION_AES256_ECB_NOPAD)) != ERROR_NON) {
		DebugLogNativeE("@%s(%u) encrypt:ret=%d", __func__, __LINE__, iRet);
		goto finally;
	}

	DebugLogNativeD("@%s(%u) end with success", __func__, __LINE__);

finally:
	delete crypto;
	if (inputbuf != NULL) free(inputbuf);
	return iRet;
}

/*
 * 1. decrypt aithkey
 * 2. extract cid kd ke from result
 * 3. store cid kd ke vehicle name to SecurityDataManager
 * 4. get tid from SecurityDataManager
 * 5. create tid data buffer
 * 6. encrypt tid data buffer
 */
int RegistrationCrypto::createAuthKey(const uchar* vehicleName, const uchar* authKey, int authKeyLength, uchar* response, int responseLength)
{
	int iRet = ERROR_NON;
//	int i = 0;
	unsigned char* inputbuf = NULL;
	unsigned char* outputbuf = NULL;
	unsigned char* tidBuf = NULL;
	CommonCrypto* decryptor = NULL;
	CommonCrypto* encryptor = NULL;
	SecurityVehicleInfo info;
	int vehicleNameLength = 0;

	unsigned char* respInputbuf = NULL;

	DebugLogNativeD("@%s(%u) start", __func__, __LINE__);
	if ((authKey == NULL) || (authKeyLength < REGISTRATION_AUTHKEYDATA_SIZE) ||
		(response == NULL) ||(responseLength < REGISTRATION_AUTHRESPDATA_SIZE)) {
		iRet = ERROR_PARAM;
		DebugLogNativeE("@%s(%u) parameter error", __func__, __LINE__);
		DebugLogNativeE("authKeyLength(%d)", authKeyLength);
		DebugLogNativeE("esponseLength(%d)", responseLength);
		goto finally;
	}
	if (vehicleName == NULL) {
		iRet = ERROR_PARAM;
		DebugLogNativeE("@%s(%u) parameter error", __func__, __LINE__);
		goto finally;
	}
	vehicleNameLength = strlen((const char*)vehicleName);
	if ((vehicleNameLength <= 0) || (vehicleNameLength > SECURITYDATA_VEHICLE_NAME_LENGTH_MAX)) {
		iRet = ERROR_PARAM;
		DebugLogNativeE("@%s(%u) parameter error", __func__, __LINE__);
		goto finally;
	}

	if (mAesSharedKey == NULL) {
		iRet = ERROR_GET_DATA;
		DebugLogNativeE("@%s(%u) mAesSharedKey == NULL", __func__, __LINE__);
		goto finally;
	}

	if ((inputbuf = (unsigned char*)malloc(REGISTRATION_AUTHKEYDATA_SIZE)) == NULL) {
		iRet = ERROR_GET_DATA;
		DebugLogNativeE("@%s(%u) memory allocate error(inputbuf)", __func__, __LINE__);
		goto finally;
	}
	memset(inputbuf, 0, REGISTRATION_AUTHKEYDATA_SIZE);
	memcpy(inputbuf, authKey, REGISTRATION_AUTHKEYDATA_SIZE);

	if ((outputbuf = (unsigned char*)malloc(REGISTRATION_AUTHKEYDATA_SIZE)) == NULL) {
		iRet = ERROR_GET_DATA;
		DebugLogNativeE("@%s(%u) memory allocate error(outputbuf)", __func__, __LINE__);
		goto finally;
	}
	memset(outputbuf, 0, REGISTRATION_AUTHKEYDATA_SIZE);

	decryptor = new CommonCrypto();
	decryptor->setKey(mAesSharedKey);
	decryptor->setBuf(inputbuf, REGISTRATION_AUTHKEYDATA_SIZE, outputbuf, REGISTRATION_AUTHKEYDATA_SIZE);
	if ((iRet = decryptor->decrypt(CIPHER_TRANSFORMATION_AES256_ECB_NOPAD)) != ERROR_NON) {
		DebugLogNativeE("@%s(%u) decrypt:ret=%d", __func__, __LINE__, iRet);
		goto finally;
	}
	delete decryptor;
	decryptor = NULL;

	info = getConnectVehicleInfo(outputbuf, vehicleName, vehicleNameLength);
	if ((iRet = mSecurityDataManager->setConnectVehicleSecInfo(&info)) != ERROR_NON) {
		DebugLogNativeE("@%s(%u) setConnectVehicleSecInfo:ret=%d", __func__, __LINE__, iRet);
		goto finally;
	}

	// 4. get tid from SecurityDataManager
	if ((tidBuf = (unsigned char*)malloc(SECURITYDATA_USER_TID_LENGTH + 1)) == NULL) {
		iRet = ERROR_GET_DATA;
		DebugLogNativeE("@%s(%u) memory allocate error(tidBuf)", __func__, __LINE__);
		goto finally;
	}
	memset(tidBuf, 0, SECURITYDATA_USER_TID_LENGTH + 1);
	if ((iRet = mSecurityDataManager->getDeviceId(tidBuf, SECURITYDATA_USER_TID_LENGTH + 1)) != ERROR_NON) {
		DebugLogNativeE("@%s(%u) getDeviceId:ret=%d", __func__, __LINE__, iRet);
		goto finally;
	}
//	for (i = 0; i < SECURITYDATA_USER_TID_LENGTH; i++) {
//		DebugLogNativeD("tidBuf[%d]=0x%02x", i, tidBuf[i]);
//	}

	//5. create tid data buffer
	if ((respInputbuf = (unsigned char*)malloc(REGISTRATION_AUTHRESPDATA_SIZE)) == NULL) {
		iRet = ERROR_GET_DATA;
		DebugLogNativeE("@%s(%u) memory allocate error(respInputbuf)", __func__, __LINE__);
		goto finally;
	}
	memset(respInputbuf, 0, REGISTRATION_AUTHRESPDATA_SIZE);
	memcpy(&(respInputbuf[SECURITYDATA_VEHICLE_CID_LENGTH]), tidBuf, SECURITYDATA_USER_TID_LENGTH);

	// 6. request encrypt data
	encryptor = new CommonCrypto();
	encryptor->setKey(mAesSharedKey);
	encryptor->setBuf(respInputbuf, REGISTRATION_AUTHRESPDATA_SIZE, response, REGISTRATION_AUTHRESPDATA_SIZE);
	if ((iRet = encryptor->encrypt(CIPHER_TRANSFORMATION_AES256_ECB_NOPAD)) != ERROR_NON) {
		DebugLogNativeE("@%s(%u) encrypt:ret=%d", __func__, __LINE__, iRet);
		goto finally;
	}
	DebugLogNativeD("@%s(%u) end with success", __func__, __LINE__);

finally:
	delete decryptor;
	if (inputbuf != NULL) free(inputbuf);
	if (outputbuf != NULL) free(outputbuf);
	delete encryptor;
	if (respInputbuf != NULL) free(respInputbuf);
	if (tidBuf != NULL) free(tidBuf);
	return iRet;
}

RegistrationCrypto::RegistrationCrypto() :
mSecurityDataManager(NULL)
{
}

void RegistrationCrypto::operator =(const RegistrationCrypto& src)
{
}

RegistrationCrypto::RegistrationCrypto(const RegistrationCrypto& src):
mSecurityDataManager(NULL)
{
}

SecurityVehicleInfo RegistrationCrypto::getConnectVehicleInfo(unsigned char* inputBuf, const unsigned char* vehicleName, const unsigned int nameLength)
{
	SecurityVehicleInfo info;
//	int i = 0;

	memset(&info, 0, sizeof(SecurityVehicleInfo));

	memcpy(info.cid, inputBuf, SECURITYDATA_VEHICLE_CID_LENGTH);
	memcpy(info.kd, &inputBuf[SECURITYDATA_VEHICLE_CID_LENGTH + SECURITYDATA_USER_TID_LENGTH], SECURITYDATA_VEHICLE_KD_LENGTH);
	memcpy(info.ke, &inputBuf[SECURITYDATA_VEHICLE_CID_LENGTH + SECURITYDATA_USER_TID_LENGTH + SECURITYDATA_VEHICLE_KD_LENGTH], SECURITYDATA_VEHICLE_KE_LENGTH);
	info.nameSize = nameLength;
	memcpy(info.name, vehicleName, nameLength);

//	for (i = 0; i < SECURITYDATA_VEHICLE_CID_LENGTH; i++) {
//		DebugLogNativeD("cid[%d]=0x%02x", i,info.cid_value[i]);
//	}
//	for (i = 0; i < SECURITYDATA_VEHICLE_KD_LENGTH; i++) {
//		DebugLogNativeD("kd[%d]=0x%02x", i,info.kd_value[i]);
//	}
//	for (i = 0; i < SECURITYDATA_VEHICLE_KE_LENGTH; i++) {
//		DebugLogNativeD("ke[%d]=0x%02x", i,info.ke_value[i]);
//	}

	return info;
}
