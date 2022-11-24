#include <errno.h>					// errno
#include <stdio.h>					// fopen
#include <unistd.h>					// SEEK_END
#include <sys/stat.h>				// chmod
#include <string.h>					// memcpy
#include <md5.h>					// MD5

#include "VehicleCryptoCommon.h"
#include "SecurityFileManager.h"
#include "Util.h"
#include "CommonCrypto.h"
// FPT 2019-01-21 : Need to change packet name for path
const char*	SecurityFileManager::file_name_userInfo_dat			= "/data/data/vn.co.honda.hondacrm/userInfo.dat";
const char*	SecurityFileManager::file_name_userInfo_secInfo		= "/data/data/vn.co.honda.hondacrm/userInfo.secInfo";
const char*	SecurityFileManager::file_name_vehicleInfo_dat		= "/data/data/vn.co.honda.hondacrm/vehicleInfo.dat";
const char*	SecurityFileManager::file_name_vehicleInfo_secInfo	= "/data/data/vn.co.honda.hondacrm/vehicleInfo.secInfo";

const uchar SecurityFileManager::AES_HASH_CRYPTO_KEY[] = {
	0x3D, 0x42, 0xC1, 0x30, 0x7A, 0xD8, 0xBB, 0x56, 0x96, 0x3D, 0x43, 0xE3, 0x80, 0x6B, 0x37, 0x8A
};

/*
 * [PROTOCOL-CR-051] ユーザ名送出契機の変更　ユーザ名のみを設定できるIFを追加
 */

SecurityFileManager::SecurityFileManager() :
mHashdataLength(16)
{
}

SecurityFileManager::~SecurityFileManager()
{
}

int SecurityFileManager::getUserInfo(uchar* inputBuf, int inputBufLength, unsigned int* exactLength)
{
	return file_read(FileKind_UserInfo, inputBuf, inputBufLength, exactLength);
}

int SecurityFileManager::setUserInfo(const uchar* outputBuf, int outputBufLength)
{
	return file_write(FileKind_UserInfo, outputBuf, outputBufLength);
}

int SecurityFileManager::getVehicleInfo(uchar* inputBuf, int inputBufLength, unsigned int* exactLength)
{
	return file_read(FileKind_VehicleInfo, inputBuf, inputBufLength, exactLength);
}

int SecurityFileManager::setVehicleInfo(const uchar* outputBuf, int outputBufLength)
{
	return file_write(FileKind_VehicleInfo, outputBuf, outputBufLength);
}

int SecurityFileManager::removeVehicleInfoFile()
{
	return file_remove(FileKind_VehicleInfo);
}
void SecurityFileManager::operator =(const SecurityFileManager& src)
{
}

SecurityFileManager::SecurityFileManager(const SecurityFileManager& src):
mHashdataLength(16)
{
}

int SecurityFileManager::file_read(eFileName mode, const uchar* inputBuf, int inputBufLength, unsigned int* exactLength)
{
	DebugLogNativeD("@%s(%u) : start", __func__, __LINE__);

	int		ret		= ERROR_NON;
	if(NULL == inputBuf)
	{
		ret		= ERROR_PARAM;
		DebugLogNativeE("@%s(%u) : return = 0x%x", __func__, __LINE__, ret);
		return ret;
	}
	if(0 >= inputBufLength)
	{
		ret		= ERROR_PARAM;
		DebugLogNativeE("@%s(%u) : return = 0x%x", __func__, __LINE__, ret);
		return ret;
	}

	const	char*	hash_file_name;
	const	char*	file_name;
	if(FileKind_UserInfo == mode)
	{
		hash_file_name	=	file_name_userInfo_dat;
		file_name		= 	file_name_userInfo_secInfo;
	}
	else
	{
		hash_file_name	=	file_name_vehicleInfo_dat;
		file_name		= 	file_name_vehicleInfo_secInfo;
	}

	FILE*	fp	= fopen(hash_file_name, "r");
	if(NULL == fp)
	{
		DebugLogNativeE("@%s(%u) : %s fopen errno = 0x%x", __func__, __LINE__, hash_file_name, errno);
		return errno;
	}

	if(fseek(fp, 0L, SEEK_END) != 0 ) {
		return errno;
	}
	long	hash_length = ftell(fp);
	if(fseek(fp, 0L, SEEK_SET) != 0){
		return errno;
	}

	uchar	hash_encrypt_data[ hash_length + EXTRA_SPACE ];
	size_t	nmemb	= 1;
	size_t	frd		= fread((void*)hash_encrypt_data, hash_length, nmemb, fp);
	fclose(fp);
	if(nmemb > frd)
	{
		DebugLogNativeE("@%s(%u) : %s fread() error", __func__, __LINE__, hash_file_name);
		return ERROR_FILE_ACCESS;
	}

	CommonCrypto*	crypto	= new CommonCrypto();
	crypto->setKey((uchar*)AES_HASH_CRYPTO_KEY);
	uchar	hash_decrypt_data[ hash_length ];
	crypto->setBuf((uchar*)hash_encrypt_data, hash_length, (uchar*)hash_decrypt_data, hash_length);
	int		transformation	= CIPHER_TRANSFORMATION_AES_ECB_NOPAD;
	ret	= crypto->decrypt(transformation);
	delete crypto;

	if(ERROR_NON != ret)
	{
		DebugLogNativeE("@%s(%u) : decrypt error = 0x%x", __func__, __LINE__, ret);
		return ret;
	}

	fp	= fopen(file_name, "r");
	if(NULL == fp)
	{
		DebugLogNativeE("@%s(%u) : %s fopen errno = 0x%x", __func__, __LINE__, file_name, errno);
		return errno;
	}
	if(fseek(fp, 0L, SEEK_END) != 0 ) {
		return errno;
	}
	long	datalen = ftell(fp);
	if(fseek(fp, 0L, SEEK_SET) != 0){
		return errno;
	}
	uchar	read_buffer[ datalen ];
	frd	= fread((void*)read_buffer, datalen, nmemb, fp);
	fclose(fp);
	if(nmemb > frd)
	{
		DebugLogNativeE("@%s(%u) : %s fread error = 0x%x", __func__, __LINE__, file_name, errno);
		return ERROR_FILE_ACCESS;
	}
	*exactLength =  (unsigned int)datalen;

	uchar	hash_value[ mHashdataLength + EXTRA_SPACE ];
	getHashValue((const char*)read_buffer, datalen, hash_value);

	if(0 != memcmp(hash_value, hash_decrypt_data, mHashdataLength))
	{
		DebugLogNativeE("%s(%d) : return = 0x%x", __func__, __LINE__, ERROR_HASH);
		return ERROR_HASH;
	}

	memcpy((void*)inputBuf, read_buffer, inputBufLength);

	DebugLogNativeD("@%s(%u) : end", __func__, __LINE__);

	return ERROR_NON;
}

int SecurityFileManager::file_write(eFileName mode, const uchar* outputBuf, int outputBufLength)
{
	DebugLogNativeD("@%s(%u) : start", __func__, __LINE__);

	int		ret		= ERROR_NON;
	if(NULL == outputBuf)
	{
		ret		= ERROR_PARAM;
		DebugLogNativeE("@%s(%u) : return = 0x%x", __func__, __LINE__, ret);
		return ret;
	}
	if(0 >= outputBufLength)
	{
		ret		= ERROR_PARAM;
		DebugLogNativeE("@%s(%u) : return = 0x%x", __func__, __LINE__, ret);
		return ret;
	}

	const	char*	hash_file_name;
	const	char*	file_name;
	if(FileKind_UserInfo == mode)
	{
		hash_file_name	=	file_name_userInfo_dat;
		file_name		=	file_name_userInfo_secInfo;
	}
	else
	{
		hash_file_name	=	file_name_vehicleInfo_dat;
		file_name		=	file_name_vehicleInfo_secInfo;
	}

	FILE*	fp = fopen(hash_file_name, "w");
	if(NULL == fp)
	{
		DebugLogNativeE("@%s(%u) : %s fopen errno = 0x%x", __func__, __LINE__, hash_file_name, errno);
		return errno;
	}

	ret = chmod(hash_file_name, S_IRGRP | S_IWGRP | S_IRUSR | S_IWUSR);
	if(0 != ret)
	{
		DebugLogNativeE("%s(%d) : %s chmod errno = 0x%x", __func__, __LINE__, hash_file_name, errno);
		fclose(fp);
		return errno;
	}

	uchar	hash_value[ mHashdataLength + EXTRA_SPACE ];
	getHashValue((const char*)outputBuf, outputBufLength, hash_value);
	int		buffer_size		= mHashdataLength;//Util::getCryptoBufferSize(mHashdataLength);
	uchar	encrypt[ buffer_size ];
	CommonCrypto*	crypto	= new CommonCrypto();
	crypto->setKey((uchar*)AES_HASH_CRYPTO_KEY);
	crypto->setBuf(hash_value, mHashdataLength, encrypt, buffer_size);
	int		transformation	= CIPHER_TRANSFORMATION_AES_ECB_NOPAD;
	ret	= crypto->encrypt(transformation);
	delete crypto;
	if(ERROR_NON != ret)
	{
		fclose(fp);
		DebugLogNativeE("@%s(%u) : encrypt error=%d", __func__, __LINE__, ret);
		return ret;
	}

	size_t	nmemb	= 1;
	size_t	fwb		= fwrite(encrypt, buffer_size, nmemb, fp);
	fclose(fp);
	if(nmemb > fwb)
	{
		DebugLogNativeE("%s(%d) : %s fwrite errno = 0x%x", __func__, __LINE__, hash_file_name, errno);
		return ERROR_FILE_ACCESS;
	}

	fp	= fopen(file_name, "w");
	if(NULL == fp)
	{
		DebugLogNativeE("@%s(%u) : %s fopen errno = 0x%x", __func__, __LINE__, file_name, errno);
		return errno;
	}

	ret = chmod(file_name, S_IRGRP | S_IWGRP | S_IRUSR | S_IWUSR);
	if(0 != ret)
	{
		DebugLogNativeE("%s(%d) : %s chmod errno = 0x%x", __func__, __LINE__, file_name, errno);
		fclose(fp);
		return errno;
	}

	fwb	= fwrite(outputBuf, outputBufLength, nmemb, fp);
	fclose(fp);
	if(nmemb > fwb)
	{
		DebugLogNativeE("%s(%d) : %s fwrite errno = 0x%x", __func__, __LINE__, file_name, errno);
		return ERROR_FILE_ACCESS;
	}

	DebugLogNativeD("@%s(%u) : end", __func__, __LINE__);

	return ERROR_NON;
}

int SecurityFileManager::file_remove(eFileName mode)
{
	DebugLogNativeD("@%s(%u) : start", __func__, __LINE__);

	const	char*	hash_file_name;
	const	char*	file_name;

	DebugLogNativeD("@%s(%u) : end", __func__, __LINE__);
	if(FileKind_UserInfo == mode)
	{
		hash_file_name	=	file_name_userInfo_dat;
		file_name		= 	file_name_userInfo_secInfo;
	}
	else
	{
		hash_file_name	=	file_name_vehicleInfo_dat;
		file_name		= 	file_name_vehicleInfo_secInfo;
	}

	int ret;

	ret	= remove(hash_file_name);
	if(ret != 0)
	{
		DebugLogNativeE("@%s(%u) : %s remove errno = 0x%x", __func__, __LINE__, hash_file_name, errno);
		return errno;
	}

	ret	= remove(file_name);
	if(ret != 0)
	{
		DebugLogNativeE("@%s(%u) : %s remove errno = 0x%x", __func__, __LINE__, hash_file_name, errno);
		return errno;
	}

	DebugLogNativeD("@%s(%u) : end", __func__, __LINE__);

	return ERROR_NON;
}

void SecurityFileManager::getHashValue(const char* data, int dataLength, uchar* outbuffer)
{
	DebugLogNativeD("@%s(%u) : start", __func__, __LINE__);

	memset(outbuffer, '\0', mHashdataLength);
	if (dataLength <= 0)
	{
		DebugLogNativeE("%s(%d) :dataLength = 0x%x", __func__, __LINE__, dataLength);
	}
	/****************************************
	 * hash値処理の正常終了時は、戻り値にoutbufferの先頭アドレスが設定される
	 */
	uchar*	hash	= MD5((const uchar*)data, (unsigned int)dataLength, outbuffer);
	if(NULL == hash)
	{
		DebugLogNativeE("%s(%d) : return = 0x%x", __func__, __LINE__, ERROR_HASH_PROG);
	}

	DebugLogNativeD("@%s(%u) : end", __func__, __LINE__);
}
