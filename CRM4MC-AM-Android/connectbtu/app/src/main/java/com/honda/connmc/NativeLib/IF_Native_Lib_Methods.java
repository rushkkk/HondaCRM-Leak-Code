package com.honda.connmc.NativeLib;
/*
 * [PROTOCOL-CR-051] ユーザ名送出契機の変更　ユーザ名のみを設定できるIFを追加
 */

public interface IF_Native_Lib_Methods {
	public void serviceFinalize();
	public int getUserName(byte[] outUser);
	public int setUserInfo(byte[] userName, byte[] deviceId);
	public int setUserName(byte[] userName);
	public boolean isExistVehicleInfo(String vehicleName);
	public int generatePublicKey(byte[] key);
	public int extractSharedKey(byte[] keyData);
	public int getPinCodeSendData(byte[] input, byte[] output);
	public int createAuthKey(byte[] authKey, byte[] response);
	public int deleteRegistrationKey();
	public int stopRegistrationCryption();

	public int deleteVehicleInfo(String vehicleName);
	public int tryAuthentication(byte[] targetRandom, byte[] vehicleAuthValue, byte[] responseAuthVaue);
	public int decryptoReceivedData(byte[] targetData, byte[] result);
	public int encryptoSendData(byte[] targetData, byte[] result);
	public int stopAuthenticationDataCryption();
}
