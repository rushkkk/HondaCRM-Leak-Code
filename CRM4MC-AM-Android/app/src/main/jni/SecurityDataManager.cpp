#include <string>
#include <android/log.h>

#include "SecurityDataManager.h"
#include "SecurityFileManager.h"
#include "CommonCrypto.h"
#include "util.h"
/*
 * [PROTOCOL-CR-051] ユーザ名送出契機の変更　ユーザ名のみを設定できるIFを追加
 */

using namespace std;

const uchar SecurityDataManager::AES_FILE_CRYPTO_KEY[]	=
{
	0xA2, 0x02, 0xE6, 0x39, 0x13, 0xBD, 0xDA, 0x6A, 0xFF, 0x8B, 0x01, 0xEF, 0x0E, 0x27, 0x0E, 0x1D,
	0xFA, 0xCB, 0xCD, 0x67, 0x76, 0xD1, 0x57, 0x11, 0xB8, 0x98, 0x1F, 0x68, 0xFF, 0xF9, 0x28, 0x77
};
const uchar	SecurityDataManager::AES_FILE_CRYPTO_IV[]	=
{
	0xAF, 0x47, 0x8B, 0x8B, 0x4D, 0x65, 0xF3, 0xCA, 0xB9, 0x95, 0x29, 0xF6, 0x21, 0x01, 0x63, 0x6C
};

SecurityDataManager::SecurityDataManager() :
mSecurityFileManager(NULL)
{
	DebugLogNativeD("@%s(%u) : start", __func__, __LINE__);
	mSecurityFileManager		= new SecurityFileManager();
	DebugLogNativeD("@%s(%u) : end", __func__, __LINE__);
}

SecurityDataManager::~SecurityDataManager() {
	DebugLogNativeD("@%s(%u) : start", __func__, __LINE__);
	delete mSecurityFileManager;
	mSecurityFileManager	= NULL;
	DebugLogNativeD("@%s(%u) : end", __func__, __LINE__);
}

int SecurityDataManager::getUserName(char* name, int nameLength) {
	int	ret	= ERROR_NON;
	SecurityDataManagerInfo* info = NULL;
	DebugLogNativeD("@%s(%u) : start", __func__, __LINE__);

	if (NULL == mSecurityFileManager) {
		ret	= ERROR_INSTANCE_NOT_FOUND;
		DebugLogNativeE("@%s(%u) : return = 0x%x", __func__, __LINE__, ret);
		goto finally;
	}
	if ((name == NULL) || (nameLength < SECURITYDATA_USER_NAME_LENGTH)) {
		ret		= ERROR_PARAM;
		DebugLogNativeE("@%s(%u) : return = 0x%x", __func__, __LINE__, ret);
		goto finally;
	}
	memset(name, '\0', nameLength);

	if ((ret = getSavedSecureData(info, false)) != ERROR_NON) {
		DebugLogNativeE("@%s(%u) : return = 0x%x", __func__, __LINE__, ret);
		goto finally;
	}

	memcpy(name, info->user.name, SECURITYDATA_USER_NAME_LENGTH);

	DebugLogNativeD("@%s(%u) : end", __func__, __LINE__);

finally:
	delete info;
	return ret;
}

int SecurityDataManager::setUserInfo(const char* userName, const char* deviceId) {
	int	ret	= ERROR_NON;
	SecurityDataManagerInfo* info;
	info = new SecurityDataManagerInfo;
	DebugLogNativeD("@%s(%u) : start", __func__, __LINE__);
	if(NULL == mSecurityFileManager) {
		ret		= ERROR_INSTANCE_NOT_FOUND;
		DebugLogNativeE("@%s(%u) : return = 0x%x", __func__, __LINE__, ret);
		goto finally;
	}
	if (NULL == userName) {
		ret		= ERROR_PARAM;
		DebugLogNativeE("@%s(%u) : return = 0x%x", __func__, __LINE__, ret);
		goto finally;
	}
	if (NULL == deviceId) {
		ret		= ERROR_PARAM;
		DebugLogNativeE("@%s(%u) : return = 0x%x", __func__, __LINE__, ret);
		goto finally;
	}
	memset(info, 0, sizeof(SecurityDataManagerInfo));
	memcpy(info->user.name, userName, SECURITYDATA_USER_NAME_LENGTH);
	memcpy(info->user.tid, deviceId, SECURITYDATA_USER_TID_LENGTH);
	if ((ret = saveSecureData(info, false)) != ERROR_NON) {
		DebugLogNativeE("@%s(%u) : return = 0x%x", __func__, __LINE__, ret);
		goto finally;
	}
	DebugLogNativeD("@%s(%u) : end with success", __func__, __LINE__);

finally:
	if (info != NULL) delete info;
	return ret;
}

int SecurityDataManager::setUserName(const char* userName) {
	int	ret	= ERROR_NON;
	SecurityDataManagerInfo* info;

	DebugLogNativeD("@%s(%u) : start", __func__, __LINE__);
	if(NULL == mSecurityFileManager) {
		ret		= ERROR_INSTANCE_NOT_FOUND;
		DebugLogNativeE("@%s(%u) : return = 0x%x", __func__, __LINE__, ret);
		goto finally;
	}
	if (NULL == userName) {
		ret		= ERROR_PARAM;
		DebugLogNativeE("@%s(%u) : return = 0x%x", __func__, __LINE__, ret);
		goto finally;
	}

	if ((ret = getSavedSecureData(info, false) != ERROR_NON)) {
		DebugLogNativeE("@%s(%u) : return = 0x%x", __func__, __LINE__, ret);
		goto finally;
	}

	memcpy(info->user.name, userName, SECURITYDATA_USER_NAME_LENGTH);
	if ((ret = saveSecureData(info, false)) != ERROR_NON) {
		DebugLogNativeE("@%s(%u) : return = 0x%x", __func__, __LINE__, ret);
		goto finally;
	}
	DebugLogNativeD("@%s(%u) : end with success", __func__, __LINE__);

finally:
	if (info != NULL) delete info;
	return ret;
}

int SecurityDataManager::getDeviceId(uchar* deviceId, int deviceIdLength) {
	int		ret	= ERROR_NON;
	SecurityDataManagerInfo* info = NULL;

	DebugLogNativeD("@%s(%u) : start", __func__, __LINE__);
	if (NULL == mSecurityFileManager) {
		ret		= ERROR_INSTANCE_NOT_FOUND;
		DebugLogNativeE("@%s(%u) : return = 0x%x", __func__, __LINE__, ret);
		return ret;
	}

	if ((NULL == deviceId) || (deviceIdLength < SECURITYDATA_USER_TID_LENGTH)) {
		ret		= ERROR_PARAM;
		DebugLogNativeE("@%s(%u) : return = 0x%x", __func__, __LINE__, ret);
		return ret;
	}
	memset(deviceId, '\0', deviceIdLength);

	if ((ret = getSavedSecureData(info, false) != ERROR_NON)) {
		DebugLogNativeE("@%s(%u) : return = 0x%x", __func__, __LINE__, ret);
		delete info;
		return ret;
	}

	memcpy(deviceId, info->user.tid, SECURITYDATA_USER_TID_LENGTH);

	DebugLogNativeD("@%s(%u) : end", __func__, __LINE__);
	delete info;

	return ERROR_NON;
}
/* FPT #176134 S */
int SecurityDataManager::deleteFile() {
	int ret = ERROR_NON;
	if ((ret = mSecurityFileManager->removeVehicleInfoFile()) != ERROR_NON) {
		DebugLogNativeE("@%s(%u)", __func__, __LINE__);
	}
	return ret;
}
/* FPT #176134 E */
int SecurityDataManager::deleteVehicleInfo(const char* vehicleName) {
	int ret = ERROR_NON;
	unsigned int registeredNum = 0;
	unsigned int storedInfoNum = 0;
	unsigned int i = 0;
	unsigned int copyNum = 0;
	bool retVal = false;
	SecurityDataManagerInfo* info = NULL;
	SecurityDataManagerInfo* newInfo = NULL;

	DebugLogNativeD("@%s(%u) : start", __func__, __LINE__);
	if ((ret = isExistVehicleInfo(vehicleName, &retVal, info, &registeredNum)) != ERROR_NON) {
		DebugLogNativeE("@%s(%u)", __func__, __LINE__);
		goto finally;
	}
	if 	((retVal == false) || (info == NULL)) {
		ret = ERROR_GET_DATA;
		DebugLogNativeE("@%s(%u)", __func__, __LINE__);
		goto finally;
	}
	for (storedInfoNum = 0; storedInfoNum < SECURITYDATA_VEHICLE_NUM_MAX; storedInfoNum++) {
		if (info->vehicle[storedInfoNum].nameSize == 0) {
			break;
		}
	}
	if (((registeredNum == 0) && (storedInfoNum == 1)) || ((registeredNum != 0) && (registeredNum > storedInfoNum))) {
		if ((ret = mSecurityFileManager->removeVehicleInfoFile()) != ERROR_NON) {
			DebugLogNativeE("@%s(%u)", __func__, __LINE__);
			goto finally;
		}
	} else {
		// create newInfo and save file.
		newInfo = new SecurityDataManagerInfo;
		memset(newInfo, 0, sizeof(SecurityDataManagerInfo));
		copyNum = 0;
		for (i = 0; i < storedInfoNum; i++) {
			if (i == registeredNum) {
				continue;
			}
			copyVehicleInfo(&newInfo->vehicle[copyNum], &info->vehicle[i]);
			copyNum++;
		}
		if ((ret = saveSecureData(newInfo, true)) != ERROR_NON) {
			DebugLogNativeE("@%s(%u)", __func__, __LINE__);
			goto finally;
		}
	}

	DebugLogNativeD("@%s(%u) : end", __func__, __LINE__);

finally:
	delete info;
	delete newInfo;
	return ret;
}

bool SecurityDataManager::isExistVehicleInfo(const char* vehicleName) {
	bool retVal = false;
	int ret = ERROR_NON;
	SecurityDataManagerInfo* info = NULL;

	DebugLogNativeD("@%s(%u) : start", __func__, __LINE__);

	if ((ret = isExistVehicleInfo(vehicleName, &retVal, info, NULL)) != ERROR_NON) {
		DebugLogNativeE("@%s(%u)", __func__, __LINE__);
		retVal = false;
		goto finally;
	}

	DebugLogNativeD("@%s(%u) : return = %d", __func__, __LINE__, retVal);

finally:
	delete info;
	return retVal;
}

int SecurityDataManager::setConnectVehicleSecInfo(SecurityVehicleInfo* target) {
	int ret = ERROR_NON;
	SecurityDataManagerInfo* storedInfo = NULL;
	SecurityDataManagerInfo* newInfo = NULL;

	bool retVal = false;
	unsigned int storedInfoNum = 0;
	unsigned int registeredNum = 0;
	unsigned int fifoCopyNum = 0;

	DebugLogNativeD("@%s(%u) : start", __func__, __LINE__);
	if ((target == NULL) || (target->nameSize == 0)){
		ret		= ERROR_PARAM;
		DebugLogNativeE("@%s(%u) : return = 0x%x", __func__, __LINE__, ret);
		goto finally;
	}
	newInfo = new SecurityDataManagerInfo;
	memset(newInfo, 0, sizeof(SecurityDataManagerInfo));
	if ((ret = isExistVehicleInfo((const char*)target->name, &retVal, storedInfo, &registeredNum)) != ERROR_NON) {
		// no vehicle info saved, so create new file and save only target
		DebugLogNativeD("@%s(%u) create vehicleInfo", __func__, __LINE__);
		copyVehicleInfo(&newInfo->vehicle[0], target);
	} else {
		for (storedInfoNum = 0; storedInfoNum < SECURITYDATA_VEHICLE_NUM_MAX; storedInfoNum++) {
			if (storedInfo->vehicle[storedInfoNum].nameSize == 0) {
				DebugLogNativeD("@%s(%u) storedInfoNum(%d)", __func__, __LINE__, storedInfoNum);
				break;
			}
		}
		if ((retVal == true) &&
			((((registeredNum == 0) && (storedInfoNum == 1)) ||
			 ((registeredNum != 0) && (registeredNum > storedInfoNum))))) {
			// already correspond vehicle info was saved, so replace it, save only one vehicle info, because other info was not valid
			DebugLogNativeD("@%s(%u) create vehicleInfo with broke file", __func__, __LINE__);
			copyVehicleInfo(&newInfo->vehicle[0], target);
		} else if (retVal == true) {
			// already correspond vehicle info was saved, so replace one, save it with other vehicle info
			DebugLogNativeD("@%s(%u) replace vehicleInfo[%d]", __func__, __LINE__, registeredNum);
			for (fifoCopyNum = 0; fifoCopyNum < storedInfoNum; fifoCopyNum++) {
				copyVehicleInfo(&newInfo->vehicle[fifoCopyNum], &storedInfo->vehicle[fifoCopyNum]);
			}
			copyVehicleInfo(&newInfo->vehicle[registeredNum], target);
		} else if ((retVal == false) && (storedInfoNum == SECURITYDATA_VEHICLE_NUM_MAX)) {
			// vehicle info file was full, delete the oldest info, add target to vehicle[5].
			DebugLogNativeD("@%s(%u) remove oldest", __func__, __LINE__);
			for (fifoCopyNum = 0; fifoCopyNum < SECURITYDATA_VEHICLE_NUM_MAX; fifoCopyNum++) {
				if (fifoCopyNum + 1 == SECURITYDATA_VEHICLE_NUM_MAX) {
					break;
				}
				copyVehicleInfo(&newInfo->vehicle[fifoCopyNum], &storedInfo->vehicle[fifoCopyNum + 1]);
			}
			copyVehicleInfo(&newInfo->vehicle[fifoCopyNum], target);
		} else {
			// some vehicle info was saved and not full yet, add target to last
			DebugLogNativeD("@%s(%u) add after vehicleInfo[%d]", __func__, __LINE__, storedInfoNum);
			for (fifoCopyNum = 0; fifoCopyNum < storedInfoNum; fifoCopyNum++) {
				copyVehicleInfo(&newInfo->vehicle[fifoCopyNum], &storedInfo->vehicle[fifoCopyNum]);
			}
			copyVehicleInfo(&newInfo->vehicle[storedInfoNum], target);
		}
	}
	if ((ret = saveSecureData(newInfo, true)) != ERROR_NON) {
		DebugLogNativeE("@%s(%u)", __func__, __LINE__);
		goto finally;
	}

	DebugLogNativeD("@%s(%u) : end", __func__, __LINE__);
finally:
	if (newInfo != NULL) delete newInfo;
	if (storedInfo != NULL) delete storedInfo;
	return ret;
}

int SecurityDataManager::getConnectVehicleSecInfo(SecurityVehicleInfo* out) {
	int ret = ERROR_NON;
	int i = 0;
	SecurityDataManagerInfo* info = NULL;
	unsigned int registeredNum = 0;

	DebugLogNativeD("@%s(%u) : start", __func__, __LINE__);
	if (((ret = getSavedSecureData(info, true)) != ERROR_NON) || (info == NULL)) {
		DebugLogNativeE("@%s(%u) : return = 0x%x", __func__, __LINE__, ret);
		goto finally;
	}
	if (info == NULL) {
		ret = ERROR_GET_DATA;
		DebugLogNativeE("@%s(%u)", __func__, __LINE__);
		goto finally;
	}
	for (i = 0; i < SECURITYDATA_VEHICLE_NUM_MAX; i++) {
		if (info->vehicle[i].nameSize == 0) {
			break;
		}
		copyVehicleInfo(&out[i], &info->vehicle[i]);
	}
	DebugLogNativeD("@%s(%u) : end", __func__, __LINE__);

finally:
	delete info;
	return ret;
}

void SecurityDataManager::operator =(const SecurityDataManager& src)
{
	mSecurityFileManager	= NULL;
}

SecurityDataManager::SecurityDataManager(const SecurityDataManager& src) :
mSecurityFileManager(NULL)
{
	mSecurityFileManager		= new SecurityFileManager();
}

int SecurityDataManager::isExistVehicleInfo(const char* vehicleName, bool* isExist, SecurityDataManagerInfo*& info, unsigned int* storedNum) {
	int ret = ERROR_NON;
	unsigned int vehicleNameLen = 0;
	int i = 0;

	DebugLogNativeD("@%s(%u) : start", __func__, __LINE__);

	if (vehicleName == NULL) {
		DebugLogNativeE("@%s(%u)", __func__, __LINE__);
		return ERROR_PARAM;
	}
	vehicleNameLen = strlen(vehicleName);
	if ((vehicleNameLen == 0) || (vehicleNameLen > SECURITYDATA_VEHICLE_NAME_LENGTH_MAX)) {
		DebugLogNativeE("@%s(%u)", __func__, __LINE__);
		return ERROR_PARAM;
	}
	if (((ret = getSavedSecureData(info, true)) != ERROR_NON) || (info == NULL)) {
		DebugLogNativeE("@%s(%u) : return = 0x%x", __func__, __LINE__, ret);
		return ret;
	}
	for (i = 0; i < SECURITYDATA_VEHICLE_NUM_MAX; i++) {
		if (info->vehicle[i].nameSize != vehicleNameLen) {
			continue;
		}
		if ((ret = memcmp(vehicleName, info->vehicle[i].name, vehicleNameLen)) == 0) {
			*isExist = true;
			DebugLogNativeD("@%s(%u) found at info->vehicle[%d]", __func__, __LINE__, i);
			if (storedNum != NULL) {
				*storedNum = i;
			}
			break;
		}
	}
	DebugLogNativeD("@%s(%u) : return = %d", __func__, __LINE__, ret);
	return ERROR_NON;
}

int SecurityDataManager::getSavedSecureData(SecurityDataManagerInfo*& info, bool isVehicle) {
	int	ret	= ERROR_NON;
	int bufferSize = 0;
	unsigned int exactSize = 0;
	unsigned char* fileBuffer = NULL;
	unsigned char* rawDataBuffer = NULL;
	CommonCrypto* decryptor = NULL;

	DebugLogNativeD("@%s(%u) : start", __func__, __LINE__);

	bufferSize = Util::getCryptoBufferSize(SECURITYDATA_MAX_BUF_SIZE);
	fileBuffer = new unsigned char[bufferSize];
	memset(fileBuffer, 0, bufferSize);
	if (isVehicle == true) {
		if ((ret = mSecurityFileManager->getVehicleInfo(fileBuffer, bufferSize, &exactSize)) != ERROR_NON) {
			DebugLogNativeE("@%s(%u) : SecurityFileManager::getUserInfo error = 0x%x", __func__, __LINE__, ret);
			goto finally;
		}
	} else {
		if ((ret = mSecurityFileManager->getUserInfo(fileBuffer, bufferSize, &exactSize)) != ERROR_NON) {
			DebugLogNativeE("@%s(%u) : SecurityFileManager::getUserInfo error = 0x%x", __func__, __LINE__, ret);
			goto finally;
		}
	}

	if (exactSize == 0) {
		ret	= ERROR_PARAM;
		DebugLogNativeE("@%s(%u) : return = 0x%x", __func__, __LINE__, ret);
		goto finally;
	}
	rawDataBuffer = new unsigned char [exactSize];
//	DebugLogNativeD("@%s(%u) : exactSize = %d", __func__, __LINE__, exactSize);
	memset(rawDataBuffer, 0, exactSize);

	decryptor = new CommonCrypto();
	decryptor->setKey((uchar*)AES_FILE_CRYPTO_KEY);
	decryptor->setIv((uchar*)AES_FILE_CRYPTO_IV, sizeof(AES_FILE_CRYPTO_IV));
	decryptor->setBuf(fileBuffer, exactSize, rawDataBuffer, exactSize);
	if ((ret = decryptor->decrypt(CIPHER_TRANSFORMATION_AES256_PKCS1PADDING)) != ERROR_NON) {
		DebugLogNativeE("@%s(%u) : decrypt error = 0x%x", __func__, __LINE__, ret);
		goto finally;
	}
	info = new SecurityDataManagerInfo;
	memset(info, 0, sizeof(SecurityDataManagerInfo));

	if ((ret = parseSecurityBuffer(rawDataBuffer, exactSize, isVehicle, info)) != ERROR_NON) {
		DebugLogNativeE("@%s(%u) : parseSecurityBuffer error = 0x%x", __func__, __LINE__, ret);
		goto finally;
	}

	DebugLogNativeD("@%s(%u) : end", __func__, __LINE__);

finally:
	delete decryptor;
	if (fileBuffer != NULL) delete[] fileBuffer;
	if (rawDataBuffer != NULL) delete[] rawDataBuffer;
	return ret;
}

int SecurityDataManager::saveSecureData(const SecurityDataManagerInfo* info, bool isVehicle) {
	int		ret	= ERROR_NON;
	unsigned char* infoBuffer = NULL;
	unsigned int dataLength = 0;
	unsigned char* encryptedBuffer = NULL;
	unsigned int encryptedDataLength = 0;
	CommonCrypto* encryptor = NULL;

	DebugLogNativeD("@%s(%u) : start", __func__, __LINE__);
	infoBuffer = new unsigned char[SECURITYDATA_MAX_BUF_SIZE];
	memset(infoBuffer, 0, SECURITYDATA_MAX_BUF_SIZE);
	if ((ret = createSecurityBuffer(isVehicle, infoBuffer, &dataLength, info)) != ERROR_NON) {
		DebugLogNativeE("@%s(%u) : return = 0x%x", __func__, __LINE__, ret);
		goto finally;
	}
	if (dataLength == 0) {
		ret = ERROR_PROGRAM;
		DebugLogNativeE("@%s(%u) : return = 0x%x", __func__, __LINE__, ret);
		goto finally;
	}
//	for (int i = 0; i < dataLength; i++) {
//		DebugLogNativeD("bufPtr[%d]=0x%02x", i, bufPtr[i]);
//	}

	encryptedDataLength = Util::getCryptoBufferSize(dataLength);
	encryptedBuffer = new unsigned char[encryptedDataLength];
	memset(encryptedBuffer, 0, sizeof(encryptedDataLength));

	encryptor = new CommonCrypto();
	encryptor->setIv((uchar*)AES_FILE_CRYPTO_IV, sizeof(AES_FILE_CRYPTO_IV));
	encryptor->setKey((uchar*)AES_FILE_CRYPTO_KEY);
	encryptor->setBuf(infoBuffer, dataLength, encryptedBuffer, encryptedDataLength);
//	DebugLogNativeD("@%s(%u) : encryptedDataLength = %d", __func__, __LINE__, encryptedDataLength);
	if ((ret = encryptor->encrypt(CIPHER_TRANSFORMATION_AES256_PKCS1PADDING)) != ERROR_NON) {
		DebugLogNativeE("@%s(%u) : return = 0x%x", __func__, __LINE__, ret);
		goto finally;
	}
	if (isVehicle == true) {
		if ((ret = mSecurityFileManager->setVehicleInfo((const uchar*)encryptedBuffer, encryptedDataLength)) != ERROR_NON) {
			DebugLogNativeE("@%s(%u) : return = 0x%x", __func__, __LINE__, ret);
			goto finally;
		}
	} else {
		if ((ret = mSecurityFileManager->setUserInfo((const uchar*)encryptedBuffer, encryptedDataLength)) != ERROR_NON) {
			DebugLogNativeE("@%s(%u) : return = 0x%x", __func__, __LINE__, ret);
			goto finally;
		}
	}
	DebugLogNativeD("@%s(%u) : end with success", __func__, __LINE__);

finally:
	delete encryptor;
	if (infoBuffer != NULL) delete[] infoBuffer;
	if (encryptedBuffer != NULL) delete[] encryptedBuffer;
	return ret;
}



int SecurityDataManager::parseSecurityBuffer(const unsigned char* dataBuffer, const int bufLength, const bool isVehicleFile, SecurityDataManagerInfo* info) {
	int readByte = 0;
	int copyByte = 0;
	unsigned int copiedTagSum[SECURITYDATA_VEHICLE_NUM_MAX] = {0};
	unsigned int lastCopiedVehicleNum = 0;
	while (readByte < bufLength) {
		unsigned char tag = dataBuffer[readByte];
		if (tag == SECURITYTAG_EOF) {
			break;
		}
		readByte++;
		if (readByte > bufLength) {
			return ERROR_PROGRAM;
		}
		unsigned char length = dataBuffer[readByte];
		if ((readByte + length) > bufLength) {
			return ERROR_PROGRAM;
		}
		readByte++;

		bool isVehicle = false;
		unsigned char vehicleNum = 0;
		unsigned char type = 0;
		if (parseSecurityTag(tag, &isVehicle, &vehicleNum, &type) != 0) {
			DebugLogNativeE("@%s(%u) : error = 0x%x", __func__, __LINE__, ERROR_PROGRAM);
			return ERROR_PROGRAM;
		}
		if (isVehicleFile != isVehicle) {
			DebugLogNativeE("@%s(%u) : error = 0x%x", __func__, __LINE__, ERROR_PROGRAM);
			return ERROR_PROGRAM;
		}
		// setLength and ptr
		unsigned char * targetPtr = NULL;
		int targetLength = 0;
		if (isVehicle == true) {
			switch(type) {
			case SECURITYTAG_DATATYPE_VALUE_NAME:
				if (length > SECURITYDATA_VEHICLE_NAME_LENGTH_MAX) {
					return ERROR_PROGRAM;
				}
				targetLength = length;
				targetPtr = info->vehicle[vehicleNum].name;
//				DebugLogNativeD("@%s(%u) name[%d]", __func__, __LINE__, vehicleNum);
				info->vehicle[vehicleNum].nameSize = targetLength;
				break;
			case SECURITYTAG_DATATYPE_VALUE_ID:
				targetLength = SECURITYDATA_VEHICLE_CID_LENGTH;
//				DebugLogNativeD("@%s(%u) cid[%d]", __func__, __LINE__, vehicleNum);
				targetPtr = info->vehicle[vehicleNum].cid;
				break;
			case SECURITYTAG_DATATYPE_VALUE_KD:
				targetLength = SECURITYDATA_VEHICLE_KD_LENGTH;
				targetPtr = info->vehicle[vehicleNum].kd;
//				DebugLogNativeD("@%s(%u) kd[%d]", __func__, __LINE__, vehicleNum);
				break;
			case SECURITYTAG_DATATYPE_VALUE_KE:
				targetLength = SECURITYDATA_VEHICLE_KE_LENGTH;
//				DebugLogNativeD("@%s(%u) ke[%d]", __func__, __LINE__, vehicleNum);
				targetPtr = info->vehicle[vehicleNum].ke;
				break;
			default:
				DebugLogNativeE("@%s(%u) : error = 0x%x", __func__, __LINE__, ERROR_PROGRAM);
				return ERROR_PROGRAM;
			}
		} else {
			switch (type) {
			case SECURITYTAG_DATATYPE_VALUE_NAME:
				targetLength = SECURITYDATA_USER_NAME_LENGTH;
				targetPtr = info->user.name;
//				DebugLogNativeD("@%s(%u) user", __func__, __LINE__);
				break;
			case SECURITYTAG_DATATYPE_VALUE_ID:
				targetLength = SECURITYDATA_USER_TID_LENGTH;
				targetPtr = info->user.tid;
//				DebugLogNativeD("@%s(%u) tid", __func__, __LINE__);
				break;
			default:
				DebugLogNativeE("@%s(%u) : error = 0x%x", __func__, __LINE__, ERROR_PROGRAM);
				return ERROR_PROGRAM;
			}
		}
		if ((targetPtr == NULL) || (length != targetLength)) {
			DebugLogNativeE("@%s(%u) : error = 0x%x", __func__, __LINE__, ERROR_PROGRAM);
			return ERROR_PROGRAM;
		}
		memcpy(targetPtr, &dataBuffer[readByte], length);
		readByte += length;
		copyByte += length;
		copiedTagSum[vehicleNum] += tag;
		lastCopiedVehicleNum = vehicleNum;
	}
	if (copyByte == 0) {
		DebugLogNativeE("@%s(%u) : error = 0x%x", __func__, __LINE__, ERROR_PROGRAM);
		return ERROR_PROGRAM;
	}
	if (isValidSecurityBuffer(copiedTagSum, lastCopiedVehicleNum, isVehicleFile) != true) {
		DebugLogNativeE("@%s(%u) : error = 0x%x", __func__, __LINE__, ERROR_PROGRAM);
		return ERROR_PROGRAM;
	}
	return ERROR_NON;
}

int SecurityDataManager::parseSecurityTag(const unsigned char secTag, bool* isVehicle, unsigned char* vehicleNum, unsigned char* type) {
	if ((secTag & SECURITYTAG_INFOTYPE_MASK) == SECURITYTAG_INFOTYPE_MASK) {
		*isVehicle = true;
	} else {
		*isVehicle = false;
	}
	*vehicleNum = (secTag & SECURITYTAG_VEHICLEINFO_MASK) >> SECURITYTAG_VEHICLEINFO_SHIFT;
	*type = (secTag & SECURITYTAG_DATATYPE_MASK);
	if (*isVehicle == false) {
		if ((*vehicleNum != SECURITYTAG_VEHICLEINFO_MIN) || (*type > SECURITYTAG_DATATYPE_VALUE_ID)) {
			return ERROR_PROGRAM;
		}
	} else {
		if ((*vehicleNum > SECURITYTAG_VEHICLEINFO_MAX) || (*type > SECURITYTAG_DATATYPE_VALUE_KD)) {
			return ERROR_PROGRAM;
		}
	}
	return ERROR_NON;
}

bool SecurityDataManager::isValidSecurityBuffer(const unsigned int* copiedTagSum, const unsigned int copiedVehicleNum, const bool isVehicle) {
	unsigned int i = 0;
	unsigned int target = 0;
	unsigned int expect = 0;
	for(i = 0; i < SECURITYDATA_VEHICLE_NUM_MAX; i++) {
		target = copiedTagSum[i];
		expect = 0;
		if ((i <= copiedVehicleNum) && (isVehicle == true)) {
			expect += SECURITYTAG_INFOTYPE_MASK * SECURITYDATA_VEHICLEINFO_SETTAGNUM;
			expect += (i << SECURITYTAG_VEHICLEINFO_SHIFT) * SECURITYDATA_VEHICLEINFO_SETTAGNUM;
			expect += SECURITYTAG_DATATYPE_VALUE_NAME + SECURITYTAG_DATATYPE_VALUE_ID + SECURITYTAG_DATATYPE_VALUE_KE + SECURITYTAG_DATATYPE_VALUE_KD;
		} else if (i <= copiedVehicleNum) {
			expect = SECURITYTAG_DATATYPE_VALUE_NAME + SECURITYTAG_DATATYPE_VALUE_ID;
		}
//		DebugLogNativeD("@%s(%u) target[%d]=%d, expect=%d", __func__, __LINE__, i, target, expect);
		if (target != expect){
			return false;
		}
	}
	return true;
}

int SecurityDataManager::createSecurityBuffer(const bool isVehicleFile, unsigned char* bufferPtr, unsigned int* bufLength, const SecurityDataManagerInfo* info) {
	int iRet = ERROR_NON;
	unsigned int writeBytes = 0;
	unsigned int tagBlockBytes = 0;
	unsigned char commonTag = 0;
	int i = 0;

	if (isVehicleFile == true) {
		for (i = 0; i < SECURITYDATA_VEHICLE_NUM_MAX; i++) {
			if ((info->vehicle[i].nameSize <= 0) || (info->vehicle[i].nameSize > SECURITYDATA_VEHICLE_NAME_LENGTH_MAX)) {
				if (i == 0) {
					iRet = ERROR_PROGRAM;
					goto finally;
				}
				break;
			}
			commonTag = SECURITYTAG_INFOTYPE_MASK | (i << SECURITYTAG_VEHICLEINFO_SHIFT);
			// 1.vehicle name
			tagBlockBytes = makeSecurityTagBlock(bufferPtr, writeBytes, (commonTag | SECURITYTAG_DATATYPE_VALUE_NAME), info->vehicle[i].nameSize, info->vehicle[i].name);
			writeBytes += tagBlockBytes;

			// 2.cid
			tagBlockBytes = makeSecurityTagBlock(bufferPtr, writeBytes, (commonTag | SECURITYTAG_DATATYPE_VALUE_ID), SECURITYDATA_VEHICLE_CID_LENGTH, info->vehicle[i].cid);
			writeBytes += tagBlockBytes;

			// 3.kd
			tagBlockBytes = makeSecurityTagBlock(bufferPtr, writeBytes, (commonTag | SECURITYTAG_DATATYPE_VALUE_KD), SECURITYDATA_VEHICLE_KD_LENGTH, info->vehicle[i].kd);
			writeBytes += tagBlockBytes;

			// 4.ke
			tagBlockBytes = makeSecurityTagBlock(bufferPtr, writeBytes, (commonTag | SECURITYTAG_DATATYPE_VALUE_KE), SECURITYDATA_VEHICLE_KE_LENGTH, info->vehicle[i].ke);
			writeBytes += tagBlockBytes;
		}
	} else {
		// 1.user name
		if (info->user.name[0] == 0) {
			iRet = ERROR_PROGRAM;
			goto finally;
		}
		tagBlockBytes = makeSecurityTagBlock(bufferPtr, writeBytes, SECURITYTAG_DATATYPE_VALUE_NAME, SECURITYDATA_USER_NAME_LENGTH, info->user.name);
		writeBytes += tagBlockBytes;

		// 2.tid
		tagBlockBytes = makeSecurityTagBlock(bufferPtr, writeBytes, SECURITYTAG_DATATYPE_VALUE_ID,SECURITYDATA_USER_TID_LENGTH, info->user.tid);
		writeBytes += tagBlockBytes;
	}

	bufferPtr[writeBytes] = SECURITYTAG_EOF;
	writeBytes++;
	bufferPtr[writeBytes] = SECURITYDATA_EOF_LENGTH;
	writeBytes++;

	*bufLength = writeBytes;
finally:
	return iRet;
}

int SecurityDataManager::makeSecurityTagBlock(unsigned char* bufPtr, const unsigned int offset, const unsigned char tag, const unsigned char length, const unsigned char* data) {
	int copyBytes = 0;

	bufPtr[offset] = tag;
	copyBytes++;
	bufPtr[offset + copyBytes] = length;
	copyBytes++;
	memcpy(&bufPtr[offset + copyBytes], data, length);
	copyBytes += length;

	return copyBytes;
}

void SecurityDataManager::copyVehicleInfo(SecurityVehicleInfo* dst, SecurityVehicleInfo* src) {
	if (src->nameSize == 0) return;
	dst->nameSize = src->nameSize;
	memcpy(dst->name, src->name, src->nameSize);
	memcpy(dst->cid, src->cid, SECURITYDATA_VEHICLE_CID_LENGTH);
	memcpy(dst->kd, src->kd, SECURITYDATA_VEHICLE_KD_LENGTH);
	memcpy(dst->ke, src->ke, SECURITYDATA_VEHICLE_KE_LENGTH);
}

