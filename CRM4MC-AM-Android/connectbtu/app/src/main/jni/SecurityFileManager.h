#ifndef	SECURITYFILEMANAGER_H
#define	SECURITYFILEMANAGER_H

#include "VehicleCryptoCommon.h"

class	SecurityFileManager
{
public:
	/** コンストラクタ */
	SecurityFileManager();

	/** デストラクタ */
	~SecurityFileManager();

	/**
	 * userInfo.datから暗号化されたハッシュ値を読み込み、復号化する。<br>
	 * userInfo.secInfoを読み込み、ハッシュ値を求める。<br>
	 * 求めた２つのハッシュ値を比較し、一致する場合は有効データとして、バッファへ設定する。
	 * @param	inputBuf		出力バッファ
	 * @param	inputBufLength	出力バッファサイズ
	 * @return	file_readd()からの戻り値
	 * @see		eFileName
	 * @see		file_read()
	 */
	int getUserInfo(uchar* inputBuf, int inputBufLength, unsigned int* exactLength);

	/**
	 * 指定データのハッシュ値を求め、暗号化し、userInfo.datへ保存する。<br>
	 * 指定データをuserInfo.secInfoへ保存する。
	 * @param	outputBuf		ファイルへ出力するデータ
	 * @param	outputBufLength	データ長
	 * @return	file_write()からの戻り値
	 * @see		eFileName
	 */
	int setUserInfo(const uchar* outputBuf, int outputBufLength);

	/**
	 * vehicleInfo.datから暗号化されたハッシュ値を読み込み、復号化する。<br>
	 * vehicleInfo.secInfoを読み込み、ハッシュ値を求める。<br>
	 * 求めた２つのハッシュ値を比較し、一致する場合は有効データとして、バッファへ設定する。
	 * @param	inputBuf		出力バッファ
	 * @param	inputBufLength	出力バッファサイズ
	 * @return	file_readd()からの戻り値
	 * @see		eFileName
	 * @see		file_read()
	 */
	int getVehicleInfo(uchar* inputBuf, int inputBufLength, unsigned int* exactLength);

	/**
	 * 指定データのハッシュ値を求め、暗号化し、vehicleInfo.datへ保存する。<br>
	 * 指定データをvehicleInfo.secInfoへ保存する。
	 * @param	outputBuf		ファイルへ出力するデータ
	 * @param	outputBufLength	データ長
	 * @return	file_write()からの戻り値
	 * @see		eFileName
	 */
	int setVehicleInfo(const uchar* outputBuf, int outputBufLength);

	int removeVehicleInfoFile();

private:
	/**
	 * コピーオペランド
	 */
	void operator =(const SecurityFileManager& src);

	/**
	 * コピーコンストラクタ
	 */
	SecurityFileManager(const SecurityFileManager& src);

	/** 種別種別 */
	enum	eFileName
	{
		FileKind_UserInfo	= 0			//!< userInfo処理
		, FileKind_VehicleInfo			//!< VehicleInfo処理
	};
	/**
	 * ハッシュファイルから暗号化されたハッシュ値を読み込み、復号化する。<br>
	 * ファイルを読み込み、ハッシュ値を求める。<br>
	 * 求めた２つのハッシュ値を比較し、一致する場合は有効データとして、バッファへ設定する。
	 * @param	mode			読み込むファイル種別
	 * @param	inputBuf		出力バッファ
	 * @param	inputBufLength	出力バッファサイズ
	 * @retval	ERROR_NON			正常終了
	 * @retval	ERROR_PARAM			パラメータエラー
	 * @retval	ERROR_HASH			ハッシュ値不一致
	 * @retval	ERROR_FILE_ACCESS	ファイルアクセスエラー
	 * @retval	CommonCrypto::decrypt()からの戻り値
	 * @retval	上記以外				errno
	 * @see		eFileName
	 */
	int file_read(eFileName mode, const uchar* inputBuf, int inputBufLength, unsigned int* exactLength);

	/**
	 * 指定データのハッシュ値を求め、暗号化し、ハッシュファイルへ保存する。<br>
	 * 指定データをファイルへ保存する。
	 * ファイルが既に存在する場合には長さゼロに切り詰め、ファイルが無い場合は新規作成とする。<br>
	 * ストリームはファイルの先頭に位置される。<br>
	 * ファイルには以下のパーミッションを設定する。
	 * - 所有者による読み取り
	 * - 所有者による書き込み
	 * @param	mode			作成するファイル種別
	 * @param	outputBuf		ファイルへ出力するデータ
	 * @param	outputBufLength	データ長
	 * @retval	ERROR_NON			正常終了
	 * @retval	ERROR_PARAM			パラメータエラー
	 * @retval	ERROR_FILE_ACCESS	ファイルアクセスエラー
	 * @retval	CommonCrypto::decrypt()からの戻り値
	 * @retval	上記以外				errno
	 * @see		eFileName
	 */
	int file_write(eFileName mode, const uchar* outputBuf, int outputBufLength);

	int file_remove(eFileName mode);

	/**
	 * MD5を使用してハッシュ値を求める。<br>
	 * MD5は指定データに対し、128Bit(16Byte）のハッシュ値（バイナリー）を出力する。
	 * @param	data		ハッシュ値を求める基データ
	 * @param	dataLength	基データ長
	 * @param	outbuffer	ハッシュ値出力バッファ（16Byte確保すること）
	 * @see		mHashdLength
	 */
	void getHashValue(const char* data, int dataLength, uchar* outbuffer);

	int					mHashdataLength;					//!< MD5で出力されるハッシュ値のバイト長

	static const char*	file_name_userInfo_dat;				//!< userInfo.datのファイルパス
	static const char*	file_name_userInfo_secInfo;			//!< userInfo.secInfoのファイルパス
	static const char*	file_name_vehicleInfo_dat;			//!< vehicleInfo.datのファイルパス
	static const char*	file_name_vehicleInfo_secInfo;		//!< vehicleInfo.secInfoのファイルパス

	static const uchar	AES_HASH_CRYPTO_KEY[];				//!< ハッシュデータ暗号用AES_HASH_SHARED_KEY
};

#endif	//#ifndef	SECURITYFILEMANAGER_H
