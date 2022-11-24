#include <jni.h>
#include <android/log.h>

#include <openssl/bn.h>
#include "aes.h"
#include "rsa.h"
#include "pem.h"
#include "engine.h"
#include "evp.h"
#include "CommonCrypto.h"

/*
 * [F14123-PR-D1-014] 【D1差替ST】(android)登録後の接続でアプリ強制終了する paddingが不正の場合は、復号結果を全てコピーする
 */

#define OPEN_SSL_RETURN_SUCCESS 1
#define OPEN_SSL_SET_KEY_SUCCESS 0
#define OPEN_SSL_CRYPTO_ERROR -1
#define RSA_KEY_LENGTH 2048
#define RSA_PUBLIC_KEY_BUF_SIZE 320

CommonCrypto::CommonCrypto()
{
	initParameter();
}

CommonCrypto::~CommonCrypto()
{
}

void CommonCrypto::setKey(uchar* Key)
{
	mKey = Key;
}
void CommonCrypto::setIv(uchar* iv, int ivLength)
{
	mIv = iv;
	mIvLength = ivLength;
}

void CommonCrypto::setBuf(uchar* inputBuf, int inputBufLength, uchar* outputBuf, int outputBufLength)
{
	mInputBuf = inputBuf;
	mInputBufLength = inputBufLength;
	mOutputBuf = outputBuf;
	mOutputBufLength = outputBufLength;
}

int CommonCrypto::decrypt(int transformation)
{
	if ((NULL == mInputBuf) || (NULL == mOutputBuf) || (mInputBufLength <= 0) || (mOutputBufLength <= 0)) {
		DebugLogNativeE("@%s(%u) parameter error", __func__, __LINE__);
		return ERROR_PARAM;
	}
	if (NULL == mKey) {
		DebugLogNativeE("@%s(%u) parameter error", __func__, __LINE__);
		return ERROR_PARAM;
	}
	switch (transformation) {
	case CIPHER_TRANSFORMATION_RSA2048_ECB_PKCS1PADDING:
		return decryptRSA();
	case CIPHER_TRANSFORMATION_AES_ECB_NOPAD:
		return cryptoAESECB(128, AES_DECRYPT);
	case CIPHER_TRANSFORMATION_AES256_ECB_NOPAD:
		return cryptoAESECB(256, AES_DECRYPT);
	case CIPHER_TRANSFORMATION_AES256_PKCS1PADDING:
		return cryptoAESCBC(256, AES_DECRYPT);
	default:
		break;
	}
	return ERROR_PARAM;
}

int CommonCrypto::encrypt(int transformation)
{
	if ((NULL == mInputBuf) || (NULL == mOutputBuf) || (mInputBufLength <= 0) || (mOutputBufLength <= 0)) {
		DebugLogNativeE("@%s(%u) parameter error", __func__, __LINE__);
		return ERROR_PARAM;
	}
	if (NULL == mKey) {
		DebugLogNativeE("@%s(%u) parameter error", __func__, __LINE__);
		return ERROR_PARAM;
	}
	switch (transformation) {
	case CIPHER_TRANSFORMATION_RSA2048_ECB_PKCS1PADDING:
		return ERROR_PARAM;
	case CIPHER_TRANSFORMATION_AES_ECB_NOPAD:
		return cryptoAESECB(128, AES_ENCRYPT);
	case CIPHER_TRANSFORMATION_AES256_ECB_NOPAD:
		return cryptoAESECB(256, AES_ENCRYPT);
	case CIPHER_TRANSFORMATION_AES256_PKCS1PADDING:
		return cryptoAESCBC(256, AES_ENCRYPT);
	default:
		break;
	}
	return ERROR_PARAM;
}

int CommonCrypto::createRsaPairKey(uchar* publicKey, int publicKeyLength, uchar* privateKey, int privateKeyLength)
{
	int bits = RSA_KEY_LENGTH;							// 鍵長：256bit(32byte)、512bit(64byte)、1024bit(128byte)、2048bit(256byte)
	unsigned long expornent = RSA_F4;		// 対数サイズ:RSA_3=3、RSA_F4=65537

	int iret = ERROR_NON;
	int l = 0;
	int modulus_len = 0;

	BIGNUM *bne = NULL;
	RSA *rsa_key = NULL;
	BIGNUM *mod = NULL;
	BIGNUM *moded = NULL;

	unsigned char *modbuf = NULL;
	unsigned char *exbuf = NULL;
	unsigned char *temp = NULL;

	BIO *bp_prikey_pem_mem = NULL;

	DebugLogNativeD("@%s(%u) start", __func__, __LINE__);
	if ((publicKey == NULL) || (publicKeyLength != RSA_PUBLIC_KEY_BUF_SIZE) ||
		(privateKey == NULL) || (privateKeyLength != RSA_KEY_LENGTH)) {
		iret = ERROR_PARAM;
		DebugLogNativeE("@%s(%u) parameter error", __func__, __LINE__);
		goto finally;
	}
	// キーペア生成準備
	if ((bne = BN_new()) == NULL) {
		iret = ERROR_CRYPTO;
		DebugLogNativeE("@%s(%u) BN_new() failed", __func__, __LINE__);
		goto finally;
	}
	if ((iret = BN_set_word(bne, expornent)) != OPEN_SSL_RETURN_SUCCESS)
	{
		iret = ERROR_CRYPTO;
		DebugLogNativeE("@%s(%u) BN_set_word:ret=%d", __func__, __LINE__, iret);
		goto finally;
	}
	if ((rsa_key = RSA_new()) == NULL) {
		iret = ERROR_CRYPTO;
		DebugLogNativeE("@%s(%u) RSA_new:failed", __func__, __LINE__);
		goto finally;
	}

	// キーペア生成
	if ((iret = RSA_generate_key_ex(rsa_key, bits, bne, NULL )) != OPEN_SSL_RETURN_SUCCESS)
	{
		iret = ERROR_CRYPTO;
		DebugLogNativeE("@%s(%u) RSA_generate_key_ex:ret=%d", __func__, __LINE__, iret);
		goto finally;
	}

	//　PUBLIC_KEY DATA 取り出し
	// modulus と publicExponentをバイト配列に変換
	mod = rsa_key->n;
	if ((modulus_len = BN_num_bytes(mod)) <= 0) {
		iret = ERROR_GET_DATA;
		DebugLogNativeE("@%s(%u) memory allocation error", __func__, __LINE__);
		goto finally;
	}
	if ((modbuf = (unsigned char *)malloc(modulus_len)) == NULL) {
		iret = ERROR_GET_DATA;
		DebugLogNativeE("@%s(%u) memory allocation error", __func__, __LINE__);
		goto finally;
	}
	memset(modbuf, 0, modulus_len);
	if ((iret =  BN_bn2bin(mod, modbuf)) < ERROR_NON) {
		DebugLogNativeE("@%s(%u) RSA_generate_key_ex:ret=%d", __func__, __LINE__, iret);
		goto finally;
	}
	moded = BN_bin2bn(modbuf, modulus_len, NULL);
	if (moded == NULL) {
		iret = ERROR_GET_DATA;
		DebugLogNativeE("@%s(%u) BN_bin2bn:ret=%d", __func__, __LINE__, iret);
		goto finally;
	}
	if ((iret = BN_cmp(mod, moded)) != ERROR_NON) {
		DebugLogNativeE("@%s(%u) BN_cmp:ret=%d", __func__, __LINE__, iret);
		goto finally;
	}

	memset(publicKey, 0x00, RSA_PUBLIC_KEY_BUF_SIZE); // とりあえずパディング埋める
	// [0]..[3] 指数値（SINT32）
	publicKey[0] = 0x00;
	publicKey[1] = 0x01;
	publicKey[2] = 0x00;
	publicKey[3] = 0x01;
	// [4]..[259] モジュロ数（符号無2048ビット）
	temp = &publicKey[4];
	::memcpy(temp, modbuf, modulus_len);

	// 秘密鍵をPEMでメモリ保存
	if ((bp_prikey_pem_mem = BIO_new(BIO_s_mem())) == NULL) {
		iret = ERROR_GET_DATA;
		DebugLogNativeE("@%s(%u) BIO_new:ret=%d", __func__, __LINE__, iret);
		goto finally;
	}
	if((iret = PEM_write_bio_RSAPrivateKey(bp_prikey_pem_mem, rsa_key, NULL, NULL, 0, NULL, NULL)) != OPEN_SSL_RETURN_SUCCESS){
		DebugLogNativeE("@%s(%u) PEM_write_bio_RSAPrivateKey:ret=%d", __func__, __LINE__, iret);
		goto finally;
	}
	memset(privateKey, 0, RSA_KEY_LENGTH);
	if ((iret = BIO_read(bp_prikey_pem_mem, privateKey, RSA_KEY_LENGTH)) < OPEN_SSL_RETURN_SUCCESS) {
		DebugLogNativeE("@%s(%u) BIO_read:ret=%d", __func__, __LINE__, iret);
		goto finally;
	}
	iret = ERROR_NON;

finally:
	BIO_free_all(bp_prikey_pem_mem);
	RSA_free(rsa_key);
	BN_free(bne);
	if (modbuf != NULL) free(modbuf);
	if (exbuf != NULL) free(exbuf);

	DebugLogNativeD("@%s(%u) end", __func__, __LINE__);
	return iret;
}

void CommonCrypto::operator =(const CommonCrypto& src)
{
}

CommonCrypto::CommonCrypto(const CommonCrypto& src)
{
}

void CommonCrypto::initParameter()
{
	mKey = NULL;
	mIv = NULL;
	mInputBuf = NULL;
	mOutputBuf = NULL;
	mInputBufLength = 0;
	mOutputBufLength = 0;
	mIvLength = 0;
}

int CommonCrypto::decryptRSA() {
	int iret = ERROR_NON;
	int padding = RSA_PKCS1_PADDING;
	int rsa_len = 0;
	int outlen = 0;

	unsigned char *inbuff = NULL;
	unsigned char *outbuff = NULL;
	void *decrypted_data = NULL;

	RSA *rsa_key = NULL;

	BIO *bp_prikey_pem_mem = NULL;		// メモリ保存(PEM)用

	DebugLogNativeD("@%s(%u) start", __func__, __LINE__);
	// 秘密鍵(PEM)をメモリからロード
	if ((bp_prikey_pem_mem = BIO_new_mem_buf((void*)mKey, -1)) == NULL) { // -1: NULL終端文字列として読み込むという指定になる
		DebugLogNativeE("@%s(%u) BIO_new_mem_buf:failed", __func__, __LINE__);
		iret = ERROR_CRYPTO;
		goto finally;
	}
	if ((rsa_key = PEM_read_bio_RSAPrivateKey(bp_prikey_pem_mem, &rsa_key, NULL, NULL)) == NULL)
	{
		DebugLogNativeE("@%s(%u) PEM_read_bio_RSAPrivateKey:failed", __func__, __LINE__);
		iret = ERROR_CRYPTO;
		goto finally;
	}

	rsa_len = RSA_size(rsa_key);
	if (mInputBufLength > rsa_len)
	{
		DebugLogNativeE("@%s(%u)input parameter error", __func__, __LINE__);
		iret = ERROR_PARAM;
		goto finally;
	}
	if (mOutputBufLength < rsa_len)
	{
		DebugLogNativeE("@%s(%u)output parameter error", __func__, __LINE__);
		iret = ERROR_PARAM;
		goto finally;
	}

	// 入出力データバッファ作成
	inbuff = (unsigned char*)malloc(rsa_len);
	if (inbuff == NULL) {
		goto finally;
	}
	memset(inbuff, 0, rsa_len);
	memcpy(inbuff, mInputBuf, mInputBufLength);
	outbuff = (unsigned char*)malloc(rsa_len);
	if (outbuff == NULL) {
		goto finally;
	}
	memset(outbuff, 0, rsa_len);

	// 秘密鍵で復号化(RSA_PKCS1_PADDING)
	if((outlen = RSA_private_decrypt(rsa_len, inbuff, outbuff, rsa_key, padding)) < OPEN_SSL_RETURN_SUCCESS)
	{
		DebugLogNativeE("@%s(%u)RSA_private_decrypt:ret=%d", __func__, __LINE__, outlen);
		iret = ERROR_CRYPTO;
		goto finally;
	}

	// 暗号／複合化データを呼出元の領域にコピー
	memcpy(mOutputBuf, outbuff, rsa_len);

finally:
	BIO_free_all(bp_prikey_pem_mem);
	if (inbuff != NULL) free(inbuff);
	if (outbuff != NULL) free(outbuff);

	DebugLogNativeD("@%s(%u) end", __func__, __LINE__);
	return iret;
}

int CommonCrypto::cryptoAESECB(int keylength, int enc) {
	int iret = ERROR_NON;

	int keybuflen = 0;
	unsigned char *inbuff = NULL;
	unsigned char *outbuff = NULL;
	unsigned char *keybuff = NULL;
	int cryptByte = 0;
	AES_KEY  aesKey;

	DebugLogNativeD("@%s(%u) start", __func__, __LINE__);

	keybuflen = keylength / 8;
	if ((keybuff = (unsigned char*)malloc(keybuflen)) == NULL) {
		DebugLogNativeE("@%s(%u)allocate error(keybuff)", __func__, __LINE__);
		iret = ERROR_GET_DATA;
		goto finally;
	}
	memset(keybuff, 0, keybuflen);
	memcpy(keybuff, mKey, keybuflen);

	memset(&aesKey, 0, sizeof(aesKey));

	if (mInputBufLength % AES_BLOCK_SIZE != 0) {
		DebugLogNativeE("@%s(%u)input parameter error", __func__, __LINE__);
		iret = ERROR_PARAM;
		goto finally;
	}

	if (mOutputBufLength != mInputBufLength) {
		DebugLogNativeE("@%s(%u)output parameter error", __func__, __LINE__);
		iret = ERROR_PARAM;
		goto finally;
	}

	// 入力データバッファ作成
	if ((inbuff = (unsigned char*)malloc(mInputBufLength)) == NULL)
	{
		DebugLogNativeE("@%s(%u)allocate error(inbuff)", __func__, __LINE__);
		iret = ERROR_GET_DATA;
		goto finally;
	}
	memset(inbuff, 0, mInputBufLength);
	memcpy(inbuff, mInputBuf, mInputBufLength);

	// 出力データバッファ作成
	if ((outbuff = (unsigned char*)malloc(mInputBufLength)) == NULL)
	{
		DebugLogNativeE("@%s(%u)allocate error(outbuff)", __func__, __LINE__);
		iret = ERROR_GET_DATA;
		goto finally;
	}
	memset(outbuff, 0, mInputBufLength);

	// set key
	if (enc == AES_ENCRYPT) {
		iret = AES_set_encrypt_key(keybuff, keylength, &aesKey);
	} else if (enc == AES_DECRYPT) {
		iret = AES_set_decrypt_key(keybuff, keylength, &aesKey);
	}
	if (iret != OPEN_SSL_SET_KEY_SUCCESS) {
		DebugLogNativeE("@%s(%u)AES_set_encrypt_key failed (%d)", __func__, __LINE__, iret);
		goto finally;
	}
	// prepare output buffer
	memset(mOutputBuf, 0, mOutputBufLength);

	while(cryptByte < mInputBufLength)
	{
	    AES_ecb_encrypt(inbuff+cryptByte, outbuff+cryptByte, &aesKey, enc);
	    cryptByte += AES_BLOCK_SIZE;
	    DebugLogNativeD("cryptByte=%d", cryptByte);
	}

	// 暗号／複合化データを呼出元の領域にコピー
	memcpy(mOutputBuf, outbuff, mOutputBufLength);

finally:
	if (inbuff != NULL) free(inbuff);
	if (outbuff != NULL) free(outbuff);
	if (keybuff != NULL) free(keybuff);

	DebugLogNativeD("@%s(%u) end", __func__, __LINE__);
	return iret;
}

// OpenSSL default padding is PKCS5padding
int CommonCrypto::cryptoAESCBC(int keylength, int enc) {
	int result = ERROR_NON;

	int datablknum = 0;			// データブロック数
	int outBuffLen = 0;		// データバッファサイズ
	int inBuffLen = 0;
	int keybuflen = 0;
	unsigned char pkcs5pad = 0;
//	int i = 0;

	unsigned char *ivec = NULL;
	unsigned char *inbuff = NULL;
	unsigned char *outbuff = NULL;
	unsigned char * keybuff = NULL;

	DebugLogNativeD("@%s(%u) start", __func__, __LINE__);

	keybuflen = keylength / 8;
	if ((keybuff = (unsigned char*)malloc(keybuflen)) == NULL) {
		DebugLogNativeE("@%s(%u)allocate error(keybuff)", __func__, __LINE__);
		result = ERROR_GET_DATA;
		goto finally;
	}
	memset(keybuff, 0, keybuflen);
	memcpy(keybuff, mKey, keybuflen);

	AES_KEY dec_key;
	memset(&dec_key, 0, sizeof(AES_KEY));

	// ブロック数、データサイズ算出
	datablknum = (mInputBufLength + AES_BLOCK_SIZE) / AES_BLOCK_SIZE;
	outBuffLen = AES_BLOCK_SIZE * datablknum;
	if (enc == AES_DECRYPT) {
		outBuffLen = mInputBufLength;
	}

	if (mOutputBufLength < outBuffLen) {
		DebugLogNativeE("@%s(%u)output parameter error", __func__, __LINE__);
		result = ERROR_PARAM;
		goto finally;
	}

	// IVバッファ作成
	if ((NULL == mIv) || (mIvLength != AES_BLOCK_SIZE)) {
		DebugLogNativeE("@%s(%u)iv parameter error)", __func__, __LINE__);
		result = ERROR_PARAM;
		goto finally;
	}
	if ((ivec = (unsigned char*)malloc(AES_BLOCK_SIZE)) == NULL)
	{
		DebugLogNativeE("@%s(%u)allocate error(ivec)", __func__, __LINE__);
		result = ERROR_GET_DATA;
		goto finally;
	}
	memset(ivec, 0, AES_BLOCK_SIZE);
	memcpy(ivec, mIv, AES_BLOCK_SIZE);

	inBuffLen = (enc == AES_ENCRYPT) ? outBuffLen : mInputBufLength;

	// 入力データバッファ作成
	if ((inbuff = (unsigned char*)malloc(inBuffLen)) == NULL)
	{
		DebugLogNativeE("@%s(%u)allocate error(inbuff)", __func__, __LINE__);
		result = ERROR_GET_DATA;
		goto finally;
	}
	memset(inbuff, 0, inBuffLen);
	if (enc == AES_ENCRYPT) {
		pkcs5pad = AES_BLOCK_SIZE - (mInputBufLength % AES_BLOCK_SIZE);
		memset(inbuff, pkcs5pad, inBuffLen);
	}
	memcpy(inbuff, mInputBuf, mInputBufLength);
//	for (i = 0; i < inBuffLen; i++) {
//		DebugLogNativeD("crypto inbuf[%d]= 0x%02x", i, inbuff[i]);
//	}

	// 出力データバッファ作成
	if ((outbuff = (unsigned char*)malloc(outBuffLen)) == NULL)
	{
		DebugLogNativeE("@%s(%u)allocate error(outbuff)", __func__, __LINE__);
		result = ERROR_GET_DATA;
		goto finally;
	}
	memset(outbuff, 0, outBuffLen);

	// 暗号／複合化
	switch (enc) {
	case AES_DECRYPT:
		// AESキー作成
		result = AES_set_decrypt_key(keybuff, keylength, &dec_key);
		break;
	case AES_ENCRYPT:
		// AESキー作成
		result = AES_set_encrypt_key(keybuff, keylength, &dec_key);
		break;
	default:
		DebugLogNativeE("@%s(%u)parameter error(enc=%d)", __func__, __LINE__, enc);
		result = ERROR_PARAM;
		goto finally;
	}
	if (result != OPEN_SSL_SET_KEY_SUCCESS) {
		DebugLogNativeE("@%s(%u)AES_set_decrypt_key error(%d)", __func__, __LINE__, result);
	}
	AES_cbc_encrypt(inbuff, outbuff, outBuffLen, &dec_key, ivec, enc);
	if (enc == AES_DECRYPT) {
		pkcs5pad = outbuff[outBuffLen - 1];
		if (pkcs5pad <= AES_BLOCK_SIZE) {
			outBuffLen = outBuffLen - pkcs5pad;
		}
	}

	// 暗号／複合化データを呼出元の領域にコピー
	memset(mOutputBuf, 0, mOutputBufLength);
	memcpy(mOutputBuf, outbuff, outBuffLen);
//	for (i = 0; i < mOutputBufLength; i++) {
//		DebugLogNativeD(rypto mOutputBuf[%d]= 0x%02x", i, mOutputBuf[i]);
//	}

finally:
	if (ivec != NULL) free(ivec);
	if (keybuff != NULL) free(keybuff);
	if (inbuff != NULL) free(inbuff);
	if (outbuff != NULL) free(outbuff);

	DebugLogNativeD("@%s(%u) end", __func__, __LINE__);
	return result;
}
