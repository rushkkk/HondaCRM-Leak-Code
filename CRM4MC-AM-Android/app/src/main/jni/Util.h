#ifndef	ETC_H
#define	ETC_H

#include "aes.h"		// getCryptoBufferSize()用

/**
 * 何でもやりますクラス.
 * ある機能に特化した処理（処理の責務）を、呼び元に実装しないように、処理を公開する。<BR>
 * 当然、呼び元のクラスで実装しても良いが、複数クラスで実装が必要になった場合にめんどくさいので、このクラスで定義しておく。
 */
class	Util
{
public:
	/**
	 * 暗号化/復号化サブルーチン.
	 * 暗号化/復号化する際に必要なバッファサイズを取得する。<BR>
	 * 求めるバッファ終端は'\0'とすること。
	 * @param	inBuffer	暗号化/復号化データ
	 */
	static inline int getCryptoBufferSize(int dataLength)
	{
		return (AES_BLOCK_SIZE * ((dataLength + AES_BLOCK_SIZE) / AES_BLOCK_SIZE));
	}
};

#endif	//#ifndef	ETC_H
