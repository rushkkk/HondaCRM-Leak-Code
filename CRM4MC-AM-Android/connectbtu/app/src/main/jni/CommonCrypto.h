#ifndef	COMMONCRYPTO_H
#define	COMMONCRYPTO_H

#include "VehicleCryptoCommon.h"

class	CommonCrypto
{
public:
	CommonCrypto();

	~CommonCrypto();

	void setKey(uchar* Key);

	void setIv(uchar* iv, int ivLength);

	void setBuf(uchar* inputBuf, int inputBufLength, uchar* outputBuf, int outputBufLength);

	/**
	 * @param	transformation	暗号化/複合化種別
	 * @retval	0	正常終了
	 * @retval	-1	暗号化/複合化エラー
	 * @retval	-2	ファイルアクセスエラー
	 */
	int decrypt(int transformation);

	/**
	 * @param	transformation	暗号化/複合化種別
	 * @retval	0	正常終了
	 * @retval	-1	暗号化/複合化エラー
	 * @retval	-2	ファイルアクセスエラー
	 */
	int encrypt(int transformation);

	// ・RSA暗号では、原理的に鍵長以上のデータを暗号化できない。
	// ・通常、11byteをPaddingに使うため、指定したbit長から88bit(11byte)引いたものが平文の最大長となる。
	// ・RSA_NO_PADDINGの場合はPaddingしないため、鍵長がそのまま平文の最大長となる。
	int createRsaPairKey(uchar* publicKey, int publicKeyLength, uchar* privateKey, int privateKeyLength);

private:
	/**
	 * コピーオペランド
	 */
	void operator =(const CommonCrypto& src);

	/**
	 * コピーコンストラクタ
	 */
	CommonCrypto(const CommonCrypto& src);

	int decryptRSA();
	int cryptoAESECB(int keylength, int enc);
	int cryptoAESCBC(int keylength, int enc);
	int encryptAES128NoPad();

	void initParameter();

	unsigned char* mKey = NULL;
	unsigned char* mInputBuf = NULL;
	unsigned char* mOutputBuf = NULL;
	unsigned char* mIv = NULL;
	int mInputBufLength;
	int mOutputBufLength;
	int mIvLength;
};

#endif	//#ifndef	COMMONCRYPTO_H
