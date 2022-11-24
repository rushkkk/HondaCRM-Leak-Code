package com.honda.connmc.NativeLib;

import android.util.Log;

import com.honda.connmc.Utils.LogUtils;

public	class NativeLibController
		implements IF_Native_Lib_Methods {
	private static NativeLibController instance;
	private static final Object NATIVE_LOCK = new Object();
	private String mConnectedVehicleName;
	public static final int VEHICLE_CRYPTO_RESULT_SUCCESS = 0;
	public static final int VEHICLE_CRYPTO_RESULT_FAIL = -1;
	public static NativeLibController createInstance() {
		synchronized (NATIVE_LOCK) {
			if (instance != null) {
				instance.serviceFinalize();
				instance = null;
			}
			instance = new NativeLibController();
		}
		return instance;
	}
	
	public static NativeLibController getInstance() {
		return instance;
	}
	
	private NativeLibController() {
		setUpNative();
	}

	@Override
	public void serviceFinalize() {
		synchronized (NATIVE_LOCK) {
			finalizeNative();
		}
	}

	@Override
	public int getUserName(byte[] outUser) {;
		synchronized (NATIVE_LOCK) {
			return getUserNameNative(outUser);
		}
	}

	@Override
	public int setUserInfo(byte[] userName, byte[] deviceId) {
		synchronized (NATIVE_LOCK) {
			return setUserInfoNative(userName, deviceId);
		}
	}
	
	@Override
	public int setUserName(byte[] userName) {
		synchronized (NATIVE_LOCK) {
			return setUserNameNative(userName);
		}
	}

	@Override
	public boolean isExistVehicleInfo(String vehicleName) {
		if (vehicleName == null) {
			return false;
		}
		mConnectedVehicleName = vehicleName;
		synchronized (NATIVE_LOCK) {
			return isExistVehicleInfoNative(mConnectedVehicleName);
		}
	}

	@Override
	public int generatePublicKey(byte[] key) {
		synchronized (NATIVE_LOCK) {
			return generatePublicKeyNative(key);
		}
	}

	@Override
	public int extractSharedKey(byte[] keyData) {
		synchronized (NATIVE_LOCK) {
			return extractSharedKeyNative(keyData);
		}
	}

	@Override
	public int getPinCodeSendData(byte[] input, byte[] output) {
		synchronized (NATIVE_LOCK) {
			return getPinCodeSendDataNative(input, output);
		}
	}

	@Override
	public int createAuthKey(byte[] authKey, byte[] response) {
		synchronized (NATIVE_LOCK) {
			return createAuthKeyNative(mConnectedVehicleName, authKey, response);
		}
	}

	@Override
	public int deleteRegistrationKey() {
		synchronized (NATIVE_LOCK) {
			return deleteRegistrationKeyNative();
		}
	}

	@Override
	public int stopRegistrationCryption() {
		synchronized (NATIVE_LOCK) {
			return stopRegistrationCryptionNative();
		}
	}

	@Override
	public int deleteVehicleInfo(String vehicleName) {
		synchronized (NATIVE_LOCK) {
			return deleteVehicleInfoNative(vehicleName);
		}
	}

	@Override
	public int tryAuthentication(byte[] targetRandom, byte[] vehicleAuthValue, byte[] responseAuthVaue) {
		synchronized (NATIVE_LOCK) {
			return tryAuthenticationNative(mConnectedVehicleName, targetRandom, vehicleAuthValue, responseAuthVaue);
		}
	}

	@Override
	public int decryptoReceivedData(byte[] targetData, byte[] result) {
		synchronized (NATIVE_LOCK) {
			return decryptoReceivedDataNative(targetData, result);
		}
	}

	@Override
	public int encryptoSendData(byte[] targetData, byte[] result) {
		synchronized (NATIVE_LOCK) {
			return encryptoSendDataNative(targetData, result);
		}
	}

	@Override
	public int stopAuthenticationDataCryption() {
		synchronized (NATIVE_LOCK) {
			return stopAuthenticationDataCryptionNative();
		}
	}
	
	@Override
	protected void finalize() throws Throwable {
		synchronized (NATIVE_LOCK) {
			finalizeNative();
			LogUtils.d("finalize native");
		}
		super.finalize();
	}
	
	// native methods
	private native void setUpNative();
	private native void finalizeNative();
	private native int getUserNameNative(byte[] outUserName);
	private native int setUserInfoNative(byte[] userName, byte[] deviceId);
	private native int setUserNameNative(byte[] userName);
	private native boolean isExistVehicleInfoNative(String connectedDevice);
	private native int generatePublicKeyNative(byte[] key);
	private native int extractSharedKeyNative(byte[] key);
	private native int getPinCodeSendDataNative(byte[] input, byte[] output);
	private native int createAuthKeyNative(String vehicleName, byte[] authKey, byte[] response);
	private native int deleteRegistrationKeyNative();
	private native int stopRegistrationCryptionNative();
	private native int deleteVehicleInfoNative(String vehicleName);
	private native int tryAuthenticationNative(String vehicleName, byte[] targetRandom, byte[] vehicleAuthValue, byte[] responseAuthVaue);
	private native int decryptoReceivedDataNative(byte[] targetData, byte[] result);
	private native int encryptoSendDataNative(byte[] targetData, byte[] result);
	private native int stopAuthenticationDataCryptionNative();

	static {
		try {
			System.loadLibrary("VehicleCryptoLib");
		} catch (final UnsatisfiedLinkError e) {
			LogUtils.e("loadLibrary" + Log.getStackTraceString(e));
		}
	}
}
