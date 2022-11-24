#ifndef	AUTHENTICATIONCRYPTO_H
#define	AUTHENTICATIONCRYPTO_H

#include "VehicleCryptoCommon.h"
#include "SecurityDataManager.h"
#include <fcntl.h>

class	SecurityDataManager;

class	AuthenticationCrypto
{
public:
	/**
	 * コンストラクタ
	 * @param	secDataManager	SecrityDataManagerインスタンス
	 */
	AuthenticationCrypto(SecurityDataManager* secDataManager);

	/**
	 * デストラクタ.
	 */
	~AuthenticationCrypto();

	int tryAuthentication(const uchar* vehicleName, const uchar* targetRandom, int randomLength, const uchar* vehicleAuthData, int vehicleAuthDataLength, uchar* responseAuthValue, int responseLength);

	int decryptReceivedData(const uchar* targetData, int targetDataLength, uchar* result, int resultLength);

	int encryptSendData(const uchar* targetData, int targetDataLength, uchar* result, int resultLength);

private:
	/**
	 * デフォルトコンストラクタ
	 */
	AuthenticationCrypto();

	/**
	 * コピーオペランド
	 */
	void operator =(const AuthenticationCrypto& src);

	/**
	 * コピーコンストラクタ
	 */
	AuthenticationCrypto(const AuthenticationCrypto& src);

	SecurityDataManager*	mSecurityDataManager;

	unsigned char* mMessageCryptoIv = NULL;
	unsigned char* mMessageCryptoKey = NULL;

	unsigned char * createFirstAuthenticationKey(const uchar* vehicleName);
	int createMessageCryptoKey(unsigned char* firstKey);
	int doFirstAuthentication(const uchar* targetRandom, int randomLength, const uchar* vehicleAuthData, int vehicleAuthDataLength, unsigned char* cryptoKey);
	int doSecondAuthentication(const uchar* targetRandom, int randomLencth, uchar* resultData, int resultDataLength);
	int getCorrespondVehicleSecInfo(const uchar* vehicleName, SecurityVehicleInfo* out);
};

#endif	//#ifndef	AUTHENTICATIONCRYPTO_H
