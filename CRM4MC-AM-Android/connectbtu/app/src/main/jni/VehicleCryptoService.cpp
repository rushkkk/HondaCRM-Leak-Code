#include <fcntl.h>
#include <android/log.h>

#include "VehicleCryptoCommon.h"
#include "SecurityDataManager.h"
#include "RegistrationCrypto.h"
#include "AuthenticationCrypto.h"
#include "VehicleCryptoService.h"

/*
 * [PROTOCOL-CR-051] ユーザ名送出契機の変更　ユーザ名のみを設定できるIFを追加
 */

VehicleCryptoService::VehicleCryptoService() :
mSecurityDataManager(NULL),
mRegistrationCrypto(NULL),
mAuthenticationCrypto(NULL)
{
	mSecurityDataManager	= new SecurityDataManager();
}

VehicleCryptoService::~VehicleCryptoService()
{
	if(NULL != mSecurityDataManager)
	{
		delete mSecurityDataManager;
		mSecurityDataManager	= NULL;
	}
	if(NULL != mRegistrationCrypto)
	{
		delete mRegistrationCrypto;
		mRegistrationCrypto	= NULL;
	}
	if(NULL != mAuthenticationCrypto)
	{
		delete mAuthenticationCrypto;
		mAuthenticationCrypto	= NULL;
	}
}

int VehicleCryptoService::getUserName(char* userName, int nameLength)
{
	if(NULL == mSecurityDataManager)
	{
		DebugLogNativeE("%s(%d) : return = 0x%x", __FILE__, __LINE__, ERROR_INSTANCE_NOT_FOUND);
		return ERROR_INSTANCE_NOT_FOUND;
	}
	if(NULL == userName)
	{
		DebugLogNativeE("%s(%d) : return = 0x%x", __FILE__, __LINE__, ERROR_PARAM);
		return ERROR_PARAM;
	}
	if(0 == nameLength)
	{
		DebugLogNativeE("%s(%d) : return = 0x%x", __FILE__, __LINE__, ERROR_PARAM);
		return ERROR_PARAM;
	}

	return mSecurityDataManager->getUserName(userName, nameLength);
}

int VehicleCryptoService::setUserInfo(const char* userName, const char* deviceId)
{
	if(NULL == mSecurityDataManager)
	{
		DebugLogNativeE("%s(%d) : return = 0x%x", __FILE__, __LINE__, ERROR_INSTANCE_NOT_FOUND);
		return ERROR_INSTANCE_NOT_FOUND;
	}

	if(NULL == userName)
	{
		DebugLogNativeE("%s(%d) : return = 0x%x", __FILE__, __LINE__, ERROR_PARAM);
		return ERROR_PARAM;
	}
	if(NULL == deviceId)
	{
		DebugLogNativeE("%s(%d) : return = 0x%x", __FILE__, __LINE__, ERROR_PARAM);
		return ERROR_PARAM;
	}

	return mSecurityDataManager->setUserInfo(userName, deviceId);
}

int VehicleCryptoService::setUserName(const char* userName)
{
	if(NULL == mSecurityDataManager)
	{
		DebugLogNativeE("%s(%d) : return = 0x%x", __FILE__, __LINE__, ERROR_INSTANCE_NOT_FOUND);
		return ERROR_INSTANCE_NOT_FOUND;
	}

	if(NULL == userName)
	{
		DebugLogNativeE("%s(%d) : return = 0x%x", __FILE__, __LINE__, ERROR_PARAM);
		return ERROR_PARAM;
	}

	return mSecurityDataManager->setUserName(userName);
}

bool VehicleCryptoService::isExistVehicleInfo(const char* vehicleName)
{
	if(NULL == mSecurityDataManager)
	{
		DebugLogNativeE("%s(%d) : return = 0x%x", __FILE__, __LINE__, ERROR_INSTANCE_NOT_FOUND);
		return false;
	}

	if(NULL == vehicleName)
	{
		return false;
	}
	if(0 == strlen(vehicleName))
	{
		return false;
	}

	return mSecurityDataManager->isExistVehicleInfo(vehicleName);
}

int VehicleCryptoService::generatePublicKey(uchar* key, int keyLength)
{
	if(NULL == mSecurityDataManager)
	{
		DebugLogNativeE("%s(%d) : return = 0x%x", __FILE__, __LINE__, ERROR_INSTANCE_NOT_FOUND);
		return ERROR_INSTANCE_NOT_FOUND;
	}
	if(NULL == key)
	{
		DebugLogNativeE("%s(%d) : return = 0x%x", __FILE__, __LINE__, ERROR_PARAM);
		return ERROR_PARAM;
	}
	if(0 == keyLength)
	{
		DebugLogNativeE("%s(%d) : return = 0x%x", __FILE__, __LINE__, ERROR_PARAM);
		return ERROR_PARAM;
	}

	mRegistrationCrypto		= new RegistrationCrypto(mSecurityDataManager);
	return mRegistrationCrypto->createRsaPairKey(key, keyLength);
}

int VehicleCryptoService::extractSharedKey(const uchar* keydata, int keyDataLength)
{
	if(NULL == mRegistrationCrypto)
	{
		DebugLogNativeE("%s(%d) : return = 0x%x", __FILE__, __LINE__, ERROR_INSTANCE_NOT_FOUND);
		return ERROR_INSTANCE_NOT_FOUND;
	}
	if(NULL == keydata)
	{
		DebugLogNativeE("%s(%d) : return = 0x%x", __FILE__, __LINE__, ERROR_PARAM);
		return ERROR_PARAM;
	}
	if(0 == keyDataLength)
	{
		DebugLogNativeE("%s(%d) : return = 0x%x", __FILE__, __LINE__, ERROR_PARAM);
		return ERROR_PARAM;
	}

	return mRegistrationCrypto->extractAesSharedKey((const char*)keydata, keyDataLength);
}

int VehicleCryptoService::getPinCodeSendData(const uchar* input, int inputLength, uchar* output, int outputLength)
{
	if(NULL == mRegistrationCrypto)
	{
		DebugLogNativeE("%s(%d) : return = 0x%x", __FILE__, __LINE__, ERROR_INSTANCE_NOT_FOUND);
		return ERROR_INSTANCE_NOT_FOUND;
	}
	if(NULL == input)
	{
		DebugLogNativeE("%s(%d) : return = 0x%x", __FILE__, __LINE__, ERROR_PARAM);
		return ERROR_PARAM;
	}
	if(0 >= inputLength)
	{
		DebugLogNativeE("%s(%d) : return = 0x%x", __FILE__, __LINE__, ERROR_PARAM);
		return ERROR_PARAM;
	}
	if(NULL == output)
	{
		DebugLogNativeE("%s(%d) : return = 0x%x", __FILE__, __LINE__, ERROR_PARAM);
		return ERROR_PARAM;
	}
	if(0 >= outputLength)
	{
		DebugLogNativeE("%s(%d) : return = 0x%x", __FILE__, __LINE__, ERROR_PARAM);
		return ERROR_PARAM;
	}

	return mRegistrationCrypto->createPinCodeData(input, inputLength, output, outputLength);
}

int VehicleCryptoService::createAuthKey(const uchar* vehicleName, const uchar* authKey, int authKeyLength, uchar* response, int responseLength)
{
	if(NULL == mRegistrationCrypto)
	{
		DebugLogNativeE("%s(%d) : return = 0x%x", __FILE__, __LINE__, ERROR_INSTANCE_NOT_FOUND);
		return ERROR_INSTANCE_NOT_FOUND;
	}
	if(NULL == vehicleName)
	{
		DebugLogNativeE("%s(%d) : return = 0x%x", __FILE__, __LINE__, ERROR_PARAM);
		return ERROR_PARAM;
	}
	if(NULL == authKey)
	{
		DebugLogNativeE("%s(%d) : return = 0x%x", __FILE__, __LINE__, ERROR_PARAM);
		return ERROR_PARAM;
	}
	if(0 >= authKeyLength)
	{
		DebugLogNativeE("%s(%d) : return = 0x%x", __FILE__, __LINE__, ERROR_PARAM);
		return ERROR_PARAM;
	}
	if(NULL == response)
	{
		DebugLogNativeE("%s(%d) : return = 0x%x", __FILE__, __LINE__, ERROR_PARAM);
		return ERROR_PARAM;
	}
	if(0 >= responseLength)
	{
		DebugLogNativeE("%s(%d) : return = 0x%x", __FILE__, __LINE__, ERROR_PARAM);
		return ERROR_PARAM;
	}

	return mRegistrationCrypto->createAuthKey(vehicleName, authKey, authKeyLength, response, responseLength);
}

int VehicleCryptoService::deleteRegistrationKey()
{
	if(NULL == mRegistrationCrypto)
	{
		DebugLogNativeE("%s(%d) : return = 0x%x", __FILE__, __LINE__, ERROR_INSTANCE_NOT_FOUND);
		return ERROR_INSTANCE_NOT_FOUND;
	}
	delete mRegistrationCrypto;
	mRegistrationCrypto = NULL;
	return ERROR_NON;
}

int VehicleCryptoService::stopRegistrationCryption()
{
	if(NULL == mRegistrationCrypto)
	{
		return ERROR_NON;
	}
	delete mRegistrationCrypto;
	mRegistrationCrypto = NULL;
	return ERROR_NON;
}

int VehicleCryptoService::deleteVehicleInfo(const char* vehicleName)
{
	if(NULL == mSecurityDataManager)
	{
		DebugLogNativeE("%s(%d) : return = 0x%x", __FILE__, __LINE__, ERROR_INSTANCE_NOT_FOUND);
		return ERROR_INSTANCE_NOT_FOUND;
	}
	if(NULL == vehicleName)
	{
		DebugLogNativeE("%s(%d) : return = 0x%x", __FILE__, __LINE__, ERROR_PARAM);
		return ERROR_PARAM;
	}
	if(strlen(vehicleName) == 0)
	{
		DebugLogNativeE("%s(%d) : return = 0x%x", __FILE__, __LINE__, ERROR_PARAM);
		return ERROR_PARAM;
	}

	return mSecurityDataManager->deleteVehicleInfo(vehicleName);
}
/* FPT #176134 S */
int VehicleCryptoService::deleteFile()
{
	if(NULL == mSecurityDataManager)
	{
		DebugLogNativeE("%s(%d) : return = 0x%x", __FILE__, __LINE__, ERROR_INSTANCE_NOT_FOUND);
		return ERROR_INSTANCE_NOT_FOUND;
	}
	return mSecurityDataManager->deleteFile();
}
/* FPT #176134 E */

int VehicleCryptoService::tryAuthentication(const uchar* vehicleName, const uchar* targetRandom, int randomLength, const uchar* vehicleAuthData, int vehicleAuthDataLength, uchar* responseAuthValue, int responseLength)
{
	if(NULL == mSecurityDataManager)
	{
		DebugLogNativeE("%s(%d) : return = 0x%x", __FILE__, __LINE__, ERROR_INSTANCE_NOT_FOUND);
		return ERROR_INSTANCE_NOT_FOUND;
	}
	if(NULL == vehicleName)
	{
		DebugLogNativeE("%s(%d) : return = 0x%x", __FILE__, __LINE__, ERROR_PARAM);
		return ERROR_PARAM;
	}
	if(strlen((const char*)vehicleName) == 0)
	{
		DebugLogNativeE("%s(%d) : return = 0x%x", __FILE__, __LINE__, ERROR_PARAM);
		return ERROR_PARAM;
	}
	if(NULL == targetRandom)
	{
		DebugLogNativeE("%s(%d) : return = 0x%x", __FILE__, __LINE__, ERROR_PARAM);
		return ERROR_PARAM;
	}
	if(0 >= randomLength)
	{
		DebugLogNativeE("%s(%d) : return = 0x%x", __FILE__, __LINE__, ERROR_PARAM);
		return ERROR_PARAM;
	}
	if(NULL == vehicleAuthData)
	{
		DebugLogNativeE("%s(%d) : return = 0x%x", __FILE__, __LINE__, ERROR_PARAM);
		return ERROR_PARAM;
	}
	if(0 >= vehicleAuthDataLength)
	{
		DebugLogNativeE("%s(%d) : return = 0x%x", __FILE__, __LINE__, ERROR_PARAM);
		return ERROR_PARAM;
	}
	if(NULL == responseAuthValue)
	{
		DebugLogNativeE("%s(%d) : return = 0x%x", __FILE__, __LINE__, ERROR_PARAM);
		return ERROR_PARAM;
	}
	if(0 >= responseLength)
	{
		DebugLogNativeE("%s(%d) : return = 0x%x", __FILE__, __LINE__, ERROR_PARAM);
		return ERROR_PARAM;
	}

	mAuthenticationCrypto	= new AuthenticationCrypto(mSecurityDataManager);
	return mAuthenticationCrypto->tryAuthentication(vehicleName, targetRandom, randomLength, vehicleAuthData, vehicleAuthDataLength, responseAuthValue, responseLength);
}

int VehicleCryptoService::decryptReceivedData(const uchar* targetData, int targetDataLength, uchar* result, int resultLength)
{
	if(NULL == mAuthenticationCrypto)
	{
		DebugLogNativeE("%s(%d) : return = 0x%x", __FILE__, __LINE__, ERROR_INSTANCE_NOT_FOUND);
		return ERROR_INSTANCE_NOT_FOUND;
	}
	if(NULL == targetData)
	{
		DebugLogNativeE("%s(%d) : return = 0x%x", __FILE__, __LINE__, ERROR_PARAM);
		return ERROR_PARAM;
	}
	if(0 >= targetDataLength)
	{
		DebugLogNativeE("%s(%d) : return = 0x%x", __FILE__, __LINE__, ERROR_PARAM);
		return ERROR_PARAM;
	}
	if(NULL == result)
	{
		DebugLogNativeE("%s(%d) : return = 0x%x", __FILE__, __LINE__, ERROR_PARAM);
		return ERROR_PARAM;
	}
	if(0 >= resultLength)
	{
		DebugLogNativeE("%s(%d) : return = 0x%x", __FILE__, __LINE__, ERROR_PARAM);
		return ERROR_PARAM;
	}

	return mAuthenticationCrypto->decryptReceivedData(targetData, targetDataLength, result, resultLength);
}

int VehicleCryptoService::encryptSendData(const uchar* targetData, int targetDataLength, uchar* result, int resultLength)
{
	if(NULL == mAuthenticationCrypto)
	{
		DebugLogNativeE("%s(%d) : return = 0x%x", __FILE__, __LINE__, ERROR_INSTANCE_NOT_FOUND);
		return ERROR_INSTANCE_NOT_FOUND;
	}
	if(NULL == targetData)
	{
		DebugLogNativeE("%s(%d) : return = 0x%x", __FILE__, __LINE__, ERROR_PARAM);
		return ERROR_PARAM;
	}
	if(0 >= targetDataLength)
	{
		DebugLogNativeE("%s(%d) : return = 0x%x", __FILE__, __LINE__, ERROR_PARAM);
		return ERROR_PARAM;
	}
	if(NULL == result)
	{
		DebugLogNativeE("%s(%d) : return = 0x%x", __FILE__, __LINE__, ERROR_PARAM);
		return ERROR_PARAM;
	}
	if(0 >= resultLength)
	{
		DebugLogNativeE("%s(%d) : return = 0x%x", __FILE__, __LINE__, ERROR_PARAM);
		return ERROR_PARAM;
	}

	return mAuthenticationCrypto->encryptSendData(targetData, targetDataLength, result, resultLength);
}

int VehicleCryptoService::stopAuthenticationDataCryption()
{
	if(NULL == mAuthenticationCrypto)
	{
		return ERROR_NON;
	}
	delete mAuthenticationCrypto;
	mAuthenticationCrypto	= NULL;

	return ERROR_NON;
}

void VehicleCryptoService::operator =(const VehicleCryptoService& src)
{
	mSecurityDataManager	= NULL;
}

VehicleCryptoService::VehicleCryptoService(const VehicleCryptoService& src) :
mSecurityDataManager(NULL),
mRegistrationCrypto(NULL),
mAuthenticationCrypto(NULL)
{
}
