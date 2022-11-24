#ifndef	VEHICLECRYPTOCOMMOM_H
#define	VEHICLECRYPTOCOMMOM_H

#include <android/log.h>

/**
 * ***********************************************************************
 * TODO: セキュリティ上問題のあるログは、商用リリース前に全て整理すること
 * 		 ・コンパイルオプションによる出力制限
 * 		 ・明らかに不要なログの削除、必須ログについては出力文を難読化
 * ***********************************************************************
 */

typedef unsigned long				ulong;
typedef unsigned int				uint;
typedef unsigned short				ushort;
typedef unsigned char				uchar;

#define	EXTRA_SPACE					(8)				//!< バッファ確保時の余裕分

static const int	ERROR_NON					=	0x00000000;	//!< 正常終了
static const int	ERROR_PARAM					=	0xffffffff;	//!< パラメータエラー：引数が正しくない
static const int	ERROR_INSTANCE_NOT_FOUND	=	0xfffffffe;	//!< あるべきインスタンスが生成されていない
static const int	ERROR_FILE_ACCESS			=	0xfffffffd;	//!< ファイルアクセスエラー
static const int	ERROR_HASH					=	0xfffffffc;	//!< ハッシュ値不一致
static const int	ERROR_CRYPTO				=	0xfffffffb;	//!< 暗号化/復号化エラー
static const int	ERROR_GET_DATA				=	0xfffffffa;	//!< 取得したデータがエラー
static const int	ERROR_PROGRAM				=	0xfffffff9;	//!< プログラムのバグ
static const int	ERROR_HASH_PROG				=	0xfffffff8;	//!< MD5()からの戻り値がエラーだった
static const int	ERROR_VEHICLEDATA_FULL		=	0xfffffff7;	//!< 車両情報がフル
static const int	ERROR_DATA_NOT_FOUND		=	0xfffffff6;	//!< 対象データが見つからない

static const int SECURITYDATA_USER_NAME_LENGTH = 32; //!< ユーザ名バッファサイズ
static const int SECURITYDATA_USER_TID_LENGTH = 10; //!< TIDバッファサイズ

static const int SECURITYDATA_VEHICLE_NAME_LENGTH_MAX = 64; //!< 車両名称最大長
static const int SECURITYDATA_VEHICLE_CID_LENGTH = 6; //!< CIDバッファサイズ
static const int SECURITYDATA_VEHICLE_KD_LENGTH = 16; //!< KDバッファサイズ
static const int SECURITYDATA_VEHICLE_KE_LENGTH = 16; //!< KEバッファサイズ

static const int SECURITYDATA_VEHICLE_CID_RESERVED_LENGTH = 10; //!< CIDバッファサイズ

static const int SECURITYDATA_VEHICLE_NUM_MAX = 6; //!< 最大車両情報登録数

/**
 * vehicleInfo.secInfoファイル構造体.
 */
typedef struct {
	unsigned char name[SECURITYDATA_VEHICLE_NAME_LENGTH_MAX];
	unsigned int nameSize;
	unsigned char cid[SECURITYDATA_VEHICLE_CID_LENGTH];
	unsigned char cid_reserved[SECURITYDATA_VEHICLE_CID_RESERVED_LENGTH];
	unsigned char kd[SECURITYDATA_VEHICLE_KD_LENGTH];
	unsigned char ke[SECURITYDATA_VEHICLE_KE_LENGTH];
} SecurityVehicleInfo;


/*
 * 0：RSA2048/ECB/PKCS1Padding
1：AES/ECB/NOPAD
2：AES-256/ECB/NOPAD
3：AES/256/CBC/PKCS5Padding
 */
static const int 	CIPHER_TRANSFORMATION_RSA2048_ECB_PKCS1PADDING = 0;
static const int	CIPHER_TRANSFORMATION_AES_ECB_NOPAD = 1;
static const int	CIPHER_TRANSFORMATION_AES256_ECB_NOPAD = 2;
static const int	CIPHER_TRANSFORMATION_AES256_PKCS1PADDING = 3;

static const char*	LOG_TAG			= "VehicleCryptoLib";		//!< ログ出力用

#ifndef LOG_DEBUG
#define LOG_DEBUG
#endif

#ifndef LOG_ERROR
#define LOG_ERROR
#endif

#ifdef LOG_DEBUG
#define DebugLogNativeD(...) __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, __VA_ARGS__)
#else
#define DebugLogNativeD(...)
#endif
#ifdef LOG_ERROR
#define DebugLogNativeE(...) __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__)
#else
#define DebugLogNativeE(...)
#endif

#endif	//#ifndef	VEHICLECRYPTOCOMMOM_H
