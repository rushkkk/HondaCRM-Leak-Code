#ifndef	SECURITYDATAMANAGER_H
#define	SECURITYDATAMANAGER_H

#include "VehicleCryptoCommon.h"
#include "SecurityInformation.h"

class	SecurityFileManager;
/*
 * [PROTOCOL-CR-051] ユーザ名送出契機の変更　ユーザ名のみを設定できるIFを追加
 */

class	SecurityDataManager
{
public:
	/**
	 * デフォルトコンストラクタ.
	 * SecurityFileManagerを生成し、mSecurityFileManagerに保存する。
	 * @see	mSecurityFileManager
	 */
	SecurityDataManager();

	/**
	 *  デストラクタ.
	 * 確保したメモリの解放を行う。
	 * @see	mSecurityFileManager
	 */
	~SecurityDataManager();

	/**
	 * ユーザ名取得.
	 * userInfo.secInfoを読み込み、復号化する。<br>
	 * 復号化したデータからユーザ名を取り出す。
	 * @param	name		ユーザ名出力バッファ
	 * @param	nameLength	出力バッファサイズ
	 * @retval	ERROR_NON					正常終了。但し、取得結果については出力バッファを確認すること。
	 * @retval	ERROR_INSTANCE_NOT_FOUND	あるべきインスタンスが生成されていない
	 * @retval	ERROR_PARAM					パラメータエラー
	 * @retval	SecurityFileManager::getUserInfo()からの戻り値
	 * @retval	CommonCrypto::encrypt()からの戻り値
	 */
	int getUserName(char* name, int nameLength);

	/**
	 * ユーザ名とTIDの設定.
	 * 引数からuserInfo.secInfoファイルデータを形成し、暗号化する。<br>
	 * 暗号化したデータをuserInfo.secInfoとして保存する。
	 * @param	userName	ユーザ名
	 * @param	deviceId	端末ID
	 * @retval	ERROR_INSTANCE_NOT_FOUND	あるべきインスタンスが生成されていない
	 * @retval	ERROR_PARAM					パラメータエラー
	 * @retval	CommonCrypto::decrypt()からの戻り値
	 * @retval	SecurityDataManager::setUserInfo()からの戻り値
	 */
	int setUserInfo(const char* userName, const char* deviceId);

	/**
	 * ユーザ名の設定.
	 * 引数からuserInfo.secInfoファイルデータを形成し、暗号化する。<br>
	 * 暗号化したデータをuserInfo.secInfoとして保存する。
	 * @param	userName	ユーザ名
	 * @retval	ERROR_INSTANCE_NOT_FOUND	あるべきインスタンスが生成されていない
	 * @retval	ERROR_PARAM					パラメータエラー
	 * @retval	CommonCrypto::decrypt()からの戻り値
	 * @retval	SecurityDataManager::setUserInfo()からの戻り値
	 */
	int setUserName(const char* userName);

	/**
	 * 端末ID取得.
	 * userInfo.secInfoを読み込み、復号化後、ユーザ名を取り出す。
	 * @param	deviceId		端末ID出力バッファ
	 * @param	deviceIdLength	出力バッファサイズ
	 * @retval	ERROR_NON					正常終了。但し、取得結果については出力バッファを確認すること。
	 * @retval	ERROR_INSTANCE_NOT_FOUND	あるべきインスタンスが生成されていない
	 * @retval	ERROR_PARAM					パラメータエラー
	 * @retval	SecurityFileManager::getUserInfo()からの戻り値
	 * @retval	CommonCrypto::encrypt()からの戻り値
	 * @see		mDeviceIdValueMaxLength
	 * @warning	出力バッファサイズは、mDeviceIdValueMaxLength+1を確保する事（1はNULLが入る）
	 */
	int getDeviceId(uchar* deviceId, int deviceIdLength);

	/**
	 * vehicleInfo.secInfoを読み込み、復号化し、登録先車両名を検索する。<br>
	 * 該当する情報（各キーの値）を削除し、暗号化後、ファイルを再作成する。
	 * @param	vehicleName		削除する登録先車両名
	 * @retval	ERROR_INSTANCE_NOT_FOUND	あるべきインスタンスが生成されていない
	 * @retval	ERROR_PARAM					パラメータエラー
	 * @retval	CommonCrypto::decrypt()からの戻り値
	 * @retval	SecurityFileManager::setVehicleInfo()からの戻り値
	 * @warning	vehicleInfo.secInfo内に同一登録先車両名が存在する場合は、ファイル先頭寄りのものを削除する。
	 */
	int deleteVehicleInfo(const char* vehicleName);
	/* FPT #176134 S */
	int deleteFile();
	/* FPT #176134 E */
	/**
	 * vehicleInfo.secInfoを読み込み、復号化し、登録先車両名を検索する。<br>
	 * 該当する情報（各キーの値）の有無を返す。
	 * @param	vehicleName		検索する登録先車両名
	 * @retval	true	指定された登録先車両名が存在する
	 * @retval	false	指定された登録先車両名が存在しない、または、エラーが発生した
	 */
	bool isExistVehicleInfo(const char* vehicleName);

	/**
	 * 車両情報設定.
	 * 登録されている車両情報から、登録する情報が存在するかをチェックする。<br>
	 * 既に登録されている車両名であれば、引数の情報に置き換えでvehicleInfo.secInfoファイルデータを形成し、暗号化する。<br>
	 * 登録されていない車両名であれば、引数の情報を追加でvehicleInfo.secInfoファイルデータを形成し、暗号化する。<br>
	 * 但し、有効最大数の車両が登録されている場合には追加できない。<br>
	 * 暗号化したデータをvehicleInfo.secInfoとして保存する。
	 * @param	info		設定する車両情報<br>gVehicleMax分のバッファを用意すること。
	 * @retval	ERROR_INSTANCE_NOT_FOUND	あるべきインスタンスが生成されていない
	 * @retval	ERROR_PARAM					パラメータエラー
	 * @retval	CommonCrypto::decrypt()からの戻り値
	 * @retval	SecurityDataManager::setUserInfo()からの戻り値
	 * @see		gVehicleMax
	 * @warning	infoには各value変数を設定すること。他の固定値は問わない。
	 */
	int setConnectVehicleSecInfo(SecurityVehicleInfo* info);

	/**
	 * 車両情報取得.
	 * vehicleInfo.secInfoを読み込み、情報を取り出す。
	 * @param	out			出力バッファ
	 * @retval	ERROR_NON					正常終了。但し、取得結果については出力バッファを確認すること。
	 * @retval	ERROR_INSTANCE_NOT_FOUND	あるべきインスタンスが生成されていない
	 * @retval	ERROR_PARAM					パラメータエラー
	 * @retval	SecurityFileManager::getUserInfo()からの戻り値
	 * @retval	CommonCrypto::encrypt()からの戻り値
	 * @see		gVehicleMax
	 * @warning	outはgVehicleMax分のサイズを確保すること
	 */
	int getConnectVehicleSecInfo(SecurityVehicleInfo* out);

private:
	/**
	 * コピーオペランド
	 */
	void operator =(const SecurityDataManager& src);

	/**
	 * コピーコンストラクタ
	 */
	SecurityDataManager(const SecurityDataManager& src);

	int isExistVehicleInfo(const char* vehicleName, bool* isExist, SecurityDataManagerInfo*& info, unsigned int* storedNum);

	int getSavedSecureData(SecurityDataManagerInfo*& info, bool isVehicle);

	int saveSecureData(const SecurityDataManagerInfo* info, bool isVehicle);

	int parseSecurityBuffer(const unsigned char* dataBuffer, const int bufLength, const bool isVehicleFile, SecurityDataManagerInfo* info);

	int createSecurityBuffer(const bool isVehicleFile, unsigned char* bufferPtr, unsigned int* bufLength, const SecurityDataManagerInfo* info);

	int parseSecurityTag(const unsigned char secTag, bool* isVehicle, unsigned char* vehicleNum, unsigned char* type);

	bool isValidSecurityBuffer(const unsigned int* copiedTagSum, const unsigned int copiedVehicleNum, const bool isVehicle);

	int makeSecurityTagBlock(unsigned char* bufPtr, const unsigned int offset, const unsigned char tag, const unsigned char length, const unsigned char* data);

	void copyVehicleInfo(SecurityVehicleInfo* dst, SecurityVehicleInfo* src);

	SecurityFileManager*	mSecurityFileManager;				//!< SecurityFileManagerクラスのインスタンス

	static const uchar		AES_FILE_CRYPTO_KEY[];				//!< 不揮発データ暗号用AES_SHARED_KEY
	static const uchar		AES_FILE_CRYPTO_IV[];				//!< 不揮発データ暗号用初期ベクトル

};

#endif	//#ifndef	SECURITYDATAMANAGER_H
