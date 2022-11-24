#ifndef	VEHICLECRYPTOSERVICE_H
#define	VEHICLECRYPTOSERVICE_H

#include "VehicleCryptoCommon.h"

class	SecurityDataManager;
class	RegistrationCrypto;
class	AuthenticationCrypto;

/*
 * [PROTOCOL-CR-051] ユーザ名送出契機の変更　ユーザ名のみを設定できるIFを追加
 */

class  VehicleCryptoService
{
public:
	/**
	 * コンストラクタ.
	 * 変数宣言時に初期値を設定しているので初期化リストは不要かとも思うが、初期化しないとwarningが発生するため、その対策として実施する。
	 */
	VehicleCryptoService();

	/**
	 * デストラクタ
	 */
	~VehicleCryptoService();

	/**
	 * ユーザ名を取得する
	 * @param	userName	ユーザ名出力バッファ
	 * @param	nameLength	出力バッファサイズ
	 * @retval	ERROR_INSTANCE_NOT_FOUND	あるべきインスタンスが生成されていない
	 * @retval	ERROR_PARAM					パラメータエラー
	 * @retval	SecurityDataManager::getUserName()からの戻り値
	 */
	int getUserName(char* userName, int nameLength);

	/**
	 * ユーザ名とTIDを設定する
	 * @param	userName	設定するユーザ名
	 * @param	deviceId	設定する端末ID
	 * @retval	ERROR_INSTANCE_NOT_FOUND	あるべきインスタンスが生成されていない
	 * @retval	ERROR_PARAM					パラメータエラー
	 * @retval	SecurityDataManager::setUserName()からの戻り値
	 */
	int setUserInfo(const char* userName, const char* deviceId);

	/**
	 * ユーザ名を設定する
	 * @param	userName	設定するユーザ名
	 * @retval	ERROR_INSTANCE_NOT_FOUND	あるべきインスタンスが生成されていない
	 * @retval	ERROR_PARAM					パラメータエラー
	 * @retval	SecurityDataManager::setUserName()からの戻り値
	 */
	int setUserName(const char* userName);

	/**
	 * 車両の認証情報を保持しているか確認する.
	 * @param	vehicleName		確認する車両名称
	 * @retval	false	指定した車両の認証情報が無い。または、エラーが発生したため、確認できない。
	 * @retval	true	指定した車両の認証情報がある。
	 * @retval	SecurityDataManager::isExitVehicleInfo()からの戻り値
	 */
	bool isExistVehicleInfo(const char* vehicleName);

	/**
	 * RSA暗号用の公開鍵を生成する
	 * @param	key			RSA公開鍵出力バッファ
	 * @param	keyLength	RSA公開鍵出力バッファサイズ
	 * @retval	ERROR_INSTANCE_NOT_FOUND	あるべきインスタンスが生成されていない
	 * @retval	ERROR_PARAM					パラメータエラー
	 * @retval	RegistrationCrypto::createRsaPairKey()からの戻り値
	 */
	int generatePublicKey(uchar* key, int keyLength);

	/**
	 * AES暗号用の共通鍵を復号化により、取得・保持する
	 * @param	keydata			復号化対象データ（暗号化されたデータ）
	 * @param	keyDataLength	復号化対象データサイズ
	 * @retval	ERROR_INSTANCE_NOT_FOUND	あるべきインスタンスが生成されていない
	 * @retval	ERROR_PARAM					パラメータエラー
	 * @retval	RegistrationCrypto::extractAesSharedKey()からの戻り値
	 */
	int extractSharedKey(const uchar* keydata, int keyDataLength);

	/**
	 * PINコードを送信のため、暗号化する
	 * @param	input			暗号化対象データ（平文データ）32Byte
	 * @param	output			暗号結果出力バッファ
	 * @param	outputLength	暗号結果出力バッファサイズ
	 * @retval	ERROR_INSTANCE_NOT_FOUND	あるべきインスタンスが生成されていない
	 * @retval	ERROR_PARAM					パラメータエラー
	 * @retval	RegistrationCrypto::createPinCodeData()からの戻り値
	 */
	int getPinCodeSendData(const uchar* input, int inputLength, uchar* output, int outputLength);

	/**
	 * AES暗号を復号化し、認証鍵を取得・保持する
	 * @param	vehicleName		確認する車両名称
	 * @param	authKey			復号化対象データ（暗号化されたデータ）
	 * @param	authKeyLength	復号化対象データサイズ
	 * @param	response		端末IDの暗号結果出力バッファ
	 * @param	responseLength	端末IDの暗号結果出力バッファサイズ
	 * @retval	ERROR_INSTANCE_NOT_FOUND	あるべきインスタンスが生成されていない
	 * @retval	ERROR_PARAM					パラメータエラー
	 * @retval	RegistrationCrypto::createAuthKey()からの戻り値
	 */
	int createAuthKey(const uchar* vehicleName, const uchar* authKey, int authKeyLength, uchar* response, int responseLength);

	/**
	 * 保持中のRSA暗号鍵、AES暗号鍵を削除する
	 * @retval	ERROR_INSTANCE_NOT_FOUND	あるべきインスタンスが生成されていない
	 * @return	ERROR_NON					正常終了
	 */
	int deleteRegistrationKey();

	/**
	 * 車両登録処理を停止・終了する
	 * @retval	ERROR_INSTANCE_NOT_FOUND	あるべきインスタンスが生成されていない
	 * @return	ERROR_NON					正常終了
	 */
	int stopRegistrationCryption();

	/**
	 * 車両認証情報を削除する
	 * @param	vehicleName	削除するデータの車両名称
	 * @retval	ERROR_INSTANCE_NOT_FOUND	あるべきインスタンスが生成されていない
	 * @retval	ERROR_PARAM					パラメータエラー
	 * @retval	SecurityDataManager::deleteVehicleInfo()からの戻り値
	 */
	int deleteVehicleInfo(const char* vehicleName);
	
	/* FPT #176134 S */
	int deleteFile();
	/* FPT #176134 E */
	
	/**
	 * 車両認証を実施する
	 * @param	vehicleName				認証ターゲット名称
	 * @param	targetRandom			認証ターゲット乱数値
	 * @param	randomLength			認証ターゲット乱数値データ長
	 * @param	vehicleAuthData			車両認証値
	 * @param	vehicleAuthDataLength	車両認証値データ長
	 * @param	responseAuthValue		応答認証値出力バッファ
	 * @param	responseLength			応答認証値出力バッファサイズ
	 * @retval	ERROR_NON					正常終了
	 * @retval	ERROR_PARAM					パラメータエラー
	 * @retval	ERROR_INSTANCE_NOT_FOUND	あるべきインスタンスが生成されていない
	 * @retval	AuthenticationCrypto->tryAuthentication()からの戻り値
	 */
	int tryAuthentication(const uchar* vehicleName, const uchar* targetRandom, int randomLength, const uchar* vehicleAuthData, int vehicleAuthDataLength, uchar* responseAuthValue, int responseLength);

	/**
	 * 受信データを復号化する
	 * @param	targetData			復号化対象データ（暗号化されたデータ）
	 * @param	targetDataLength	復号化対象データ長
	 * @param	result				復号化データ出力バッファ
	 * @param	resultLength		復号化データ出力バッファサイズ
	 * @retval	ERROR_INSTANCE_NOT_FOUND	あるべきインスタンスが生成されていない
	 * @retval	ERROR_PARAM					パラメータエラー
	 * @retval	AuthenticationCrypto::decryptReceivedData()からの戻り値
	 */
	int decryptReceivedData(const uchar* targetData, int targetDataLength, uchar* result, int resultLength);

	/**
	 * 受信データを暗号化する
	 * @param	targetData			暗号化対象データ（平文データ）
	 * @param	targetDataLength	暗号化対象データ長
	 * @param	result				暗号化データ出力バッファ
	 * @param	resultLength		暗号化データ出力バッファサイズ
	 * @retval	ERROR_INSTANCE_NOT_FOUND	あるべきインスタンスが生成されていない
	 * @retval	ERROR_PARAM					パラメータエラー
	 * @retval	AuthenticationCrypto::encryptSendData()からの戻り値
	 */
	int encryptSendData(const uchar* targetData, int targetDataLength, uchar* result, int resultLength);

	/**
	 * 車両認証・データ暗号・復号処理を停止・終了する
	 * @retval	ERROR_INSTANCE_NOT_FOUND	あるべきインスタンスが生成されていない
	 * @return	ERROR_NON					正常終了
	 */
	int stopAuthenticationDataCryption();

private:
	/**
	 * コピーオペランド
	 * @param	src		コピーする値
	 */
	void operator =(const VehicleCryptoService& src);

	/**
	 * コピーコンストラクタ.
	 * 変数宣言時に初期値を設定しているので初期化リストは不要かとも思うが、初期化しないとwarningが発生するため、その対策として実施する。
	 * @param	src		設定する値
	 */
	VehicleCryptoService(const VehicleCryptoService& src);

	SecurityDataManager*	mSecurityDataManager;		//!< SecurityDataManagerインスタンス
	RegistrationCrypto*		mRegistrationCrypto;		//!< RegistrationCryptoインスタンス
	AuthenticationCrypto*	mAuthenticationCrypto;		//!< AuthenticationCryptoインスタンス
};

#endif	//#ifndef	VEHICLECRYPTOSERVICE_H
