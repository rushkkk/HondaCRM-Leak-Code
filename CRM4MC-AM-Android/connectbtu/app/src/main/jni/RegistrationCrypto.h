#ifndef	REGISTRATIONCRYPT_H
#define	REGISTRATIONCRYPT_H

#include <fcntl.h>
#include "VehicleCryptoCommon.h"

class	SecurityDataManager;

class	RegistrationCrypto
{
public:
	/**
	 * コンストラクタ
	 * @param	secDataManager	SecrityDataManagerインスタンス
	 */
	RegistrationCrypto(SecurityDataManager* secDataManager);

	/**
	 * デストラクタ.
	 */
	~RegistrationCrypto();

	int createRsaPairKey(uchar* publicKey, int keyLength);

	int extractAesSharedKey(const char* encryptSharedKey, int keyDataLength);

	int createPinCodeData(const uchar* input, int inputLength, uchar* output, int outputLength);

	int createAuthKey(const uchar* vehicleName, const uchar* authKey, int authKeyLength, uchar* response, int responseLength);

private:
	/**
	 * デフォルトコンストラクタ
	 */
	RegistrationCrypto();

	/**
	 * コピーオペランド
	 */
	void operator =(const RegistrationCrypto& src);

	/**
	 * コピーコンストラクタ
	 */
	RegistrationCrypto(const RegistrationCrypto& src);

	SecurityVehicleInfo getConnectVehicleInfo(unsigned char* inputBuf, const unsigned char* vehicleName, const unsigned int nameLength);


	// define値を定義するまで適当なサイズで確保。
//	uchar		mRsaPublicKey[ 100 ];			//!< 車両登録時使用RSA_PUBLIC_KEY
//	uchar		mRsaPrivateKey[ 100 ];			//!< 車両登録時使用RSA_PRIVATE_KEY
//	uchar		mAesSharedKey[ 100 ];			//!< 車両登録時使用AES_SHARED_KEY
	unsigned char* mRsaPrivateKey = NULL;
	unsigned char* mAesSharedKey = NULL;

	SecurityDataManager*	mSecurityDataManager	= NULL;
};

#endif	//#ifndef	REGISTRATIONCRYPT_H
