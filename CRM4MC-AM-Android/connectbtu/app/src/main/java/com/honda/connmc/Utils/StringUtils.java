package com.honda.connmc.Utils;

import android.support.annotation.Nullable;
import android.widget.EditText;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class StringUtils {

    private static final String CHARSET_NAME = "UTF-8";

    public static byte[] bytesFromString(String string) {
        byte[] stringBytes = new byte[0];
        try {
            stringBytes = string.getBytes(CHARSET_NAME);
        } catch (UnsupportedEncodingException e) {
            LogUtils.e("Failed to convert message string to byte array");
        }

        return stringBytes;
    }

    @Nullable
    public static String stringFromBytes(byte[] bytes) {
        String byteString = null;
        try {
            byteString = new String(bytes, CHARSET_NAME);
        } catch (UnsupportedEncodingException e) {
            LogUtils.e("Unable to convert message bytes to string");
        }
        return byteString;
    }

    /**
     * Print bit array to string, replace all "true" "false" value into 1/0 for readability.
     * @param bitArr bit array.
     * @return bit array toString.
     */
    public static String bitArrToString(boolean[] bitArr) {
        return Arrays.toString(bitArr).replaceAll("true", "1").replaceAll("false", "0");
    }

    /**
     * Return byte array to string, data format to hex.
     * @param data byte arr.
     * @return byte arr to string hex.
     */
    public static String byteArrToStringHex(byte[] data) {
        String result = null;
        if (data == null) {
            result = "(null)";
        } else {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < data.length; i++) {
                String hex = String.format(" %02X", data[i]);
                sb.append(hex);
            }
            result = sb.toString();
        }
        return result;
    }

    public static String convertBytesToString(byte[] target, String defaultValue) {
        ByteBuffer targetBuf = ByteBuffer.wrap(target);
        int padSize;
        for(padSize = 0; padSize < target.length; padSize++) {
            if (target[padSize] == '\0') {
                break;
            }
        }
        if (padSize == 0) {
            return defaultValue;
        }
        ByteBuffer data = ByteBuffer.allocate(padSize);
        targetBuf.get(data.array(), 0, padSize);
        try {
            return new String(data.array(), "Shift_JIS");
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    /**
     * Check if edittext is empty.
     * @param edt EditText
     * @return true isEmpty.
     */
    public static boolean isEmpty(EditText edt){
        if (edt.getText().toString().trim().length() > 0)
            return false;

        return true;
    }
}