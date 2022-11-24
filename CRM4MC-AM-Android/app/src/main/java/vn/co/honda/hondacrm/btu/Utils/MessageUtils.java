package vn.co.honda.hondacrm.btu.Utils;


import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Map;

import vn.co.honda.hondacrm.btu.Bluetooth.BluetoothManager;
import vn.co.honda.hondacrm.btu.Constants.message.ParamTag;
import vn.co.honda.hondacrm.btu.Constants.message.ParamValue;
import vn.co.honda.hondacrm.btu.Model.message.common.VehicleMessage;
import vn.co.honda.hondacrm.btu.Model.message.common.VehicleParam;
import vn.co.honda.hondacrm.btu.NativeLib.NativeLibController;

public class MessageUtils {

    private static final int BYTE_PER_CRYPTOBLOCK = 16;

    /**
     * Decode data into VehicleMessage.
     *
     * @param data decrypted data.
     * @return VehicleMessage.
     */
    public static VehicleMessage decode(byte[] data) {
        LogUtils.startEndMethodLog(true);
        VehicleMessage msg = new VehicleMessage();

        ByteBuffer buffer;
        buffer = ByteBuffer.wrap(data);

        //Set MsgId = first 2 bytes of buffer.
        msg.setMsgId(buffer.getShort());
        LogUtils.d("MessageID:　" + msg.getMsgId());

        byte[] paramsArr;
        paramsArr = new byte[buffer.remaining()];
        buffer.get(paramsArr);
        buffer = ByteBuffer.wrap(paramsArr);

        VehicleParam param;

        while (buffer.remaining() > 0) {
            if (buffer.position() == buffer.array().length) {
                break;
            }
            try {
                short tag = buffer.getShort();
                String hex = String.format("0x%04x", tag);
                int tagInt = Integer.decode(hex);
                LogUtils.d("TAG16: " + hex + "value: " + tagInt);
                if (tag == ParamTag.END_OF_MESSAGE) {
                    LogUtils.d("Reach END_OF_MESSAGE tag.");
                    return msg;
                }
                // get data size
                int length = ParamTag.getLength(tagInt);
                if (length == 0) {
                    LogUtils.d("received TAG(" + tag + " is invalid");
                    return msg;
                }
                byte[] value = new byte[length];
                for (int valueNum = 0; valueNum < length; valueNum++) {
                    value[valueNum] = buffer.get();
                }
                LogUtils.d("Tag:length:value = " + tagInt + ":" + length + ":"
                        + value[0]);

                //Add param for VehicleMessage.
                param = new VehicleParam(tagInt, value);
                msg.addParams(param);
            } catch (BufferUnderflowException e) {
                break;
            }
        }

        LogUtils.startEndMethodLog(false);
        return msg;
    }

    /**
     * Encode VehicleMessage to byte [] data.
     *
     * @param msg VehicleMessage.
     * @return encoded byte [] data.
     */
    public static byte[] encode(VehicleMessage msg) {
        LogUtils.startEndMethodLog(true);

        int msgSize = msg.getMsgSize();
        ByteBuffer buffer = ByteBuffer.allocate(msgSize);

        // Add msg id.
        buffer.putShort(msg.getMsgId());
        LogUtils.d("MessageID:　" + msg.getMsgId());
        if (msg.getParams() != null) {
            for (VehicleParam param : msg.getParams()) {
                if (param == null) continue;
                LogUtils.d(String.format("tag16: 0x%04x", param.mTag));
                byte[] byteArray;
                ByteBuffer data = ByteBuffer.allocate(param.getSize());
                data.putShort((short) param.getTag());
                data.put(param.getValue());
                byteArray = data.array();
                // Add param.
                buffer.put(byteArray);
            }
        }

        // Add end of message.
        buffer.putShort((short) ParamTag.END_OF_MESSAGE);
        buffer.put((byte) ParamValue.END_OF_MESSAGE);

        byte[] encodedMsg = buffer.array();

        LogUtils.startEndMethodLog(false);
        return encodedMsg;
    }

    /**
     * Encrypt byte array data.
     *
     * @param data byte array.
     * @return encrypted data.
     */
    public static byte[] encrypt(byte[] data) {
        int blockNum = (data.length / BYTE_PER_CRYPTOBLOCK) + 1;
        ByteBuffer resultBuf = ByteBuffer.allocate(blockNum * BYTE_PER_CRYPTOBLOCK);
        byte[] encryptedData = resultBuf.array();
        NativeLibController.getInstance().encryptoSendData(data, encryptedData);
        return encryptedData;
    }

    /**
     * Decrypt byte array data.
     *
     * @param data encrypted data.
     * @return Decrypted data.
     */
    public static byte[] decrypt(byte[] data) {
        ByteBuffer rawData = ByteBuffer.allocate(data.length); // size is same because end message was fixed
        byte[] result = rawData.array();

        NativeLibController.getInstance().decryptoReceivedData(data, result);
        return result;
    }


    /**
     * Get value at position / range from bit array.
     *
     * @param byteArr  Byte array holds data of a message's value.
     * @param valueStr ParamValue.VehicleGwInfo/SmartphoneGwInfo. Ex: "0.7", "1.7-1.0"...
     * @return value at that position.
     */
    public static int getValueFromByteArr(byte[] byteArr, String valueStr) {
        boolean[] bitArr = byteArrToBitArr(byteArr);
        int outputValue = -1;
        if (valueStr.matches(FORMAT_BIT_POS_STR)) {
            outputValue = getValueAtPosition(bitArr, valueStr);
        } else if (valueStr.matches(FORMAT_BIT_RANGE_STR)) {
            outputValue = getValueAtRange(bitArr, valueStr);
        }
        return outputValue;
    }


    /**
     * Create byte array represent param value of Phone Gw info.
     *
     * @param paramSize Size of its param. Use ParamTag.getLength(Smartphone GW Info tags)
     * @param valueMap  a map of ParamValue.SmartphoneGwInfo and its value.
     * @return byte array hold data param value of Phone Gw info.
     */
    public static byte[] createPhoneGwMsgParam(int paramSize, Map<String, Integer> valueMap) {
        //Create bit array.
        boolean[] bitArr = new boolean[paramSize * BASE_UNIT8];
        if (valueMap == null) {
            return null;
        }
        for (Map.Entry<String, Integer> entry : valueMap.entrySet()) {
            //Add value to bit array.
            int value = entry.getValue();
            String key = entry.getKey();
            if (key.matches(FORMAT_BIT_POS_STR)) {
                bitArr = setValueAtPosition(bitArr, key, value);
            } else if (key.matches(FORMAT_BIT_RANGE_STR)) {
                bitArr = setValueIntoRange(bitArr, key, value);
            }
        }
        LogUtils.d("Bit arr after added values: " + StringUtils.bitArrToString(bitArr));
        //Convert to byte array.
        byte[] output = new byte[paramSize];
        for (int i = 0; i < paramSize; i++) {
            output[i] = (byte) bitArrToInt(bitArr, i * BASE_UNIT8, BASE_UNIT8);
        }
        LogUtils.d("Converted to byte array: " + Arrays.toString(output));
        return output;
    }


    /**
     * Get bit array from byte array
     *
     * @param byteArr array byte. Ex: {1, 2}
     * @return array bit. Ex: {0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0}
     */
    private static boolean[] byteArrToBitArr(byte[] byteArr) {
        if (byteArr == null) return null;
        int sizeArr = byteArr.length;
        //Init output bit arr.
        boolean[] bitArrs = new boolean[BASE_UNIT8 * sizeArr];
        for (int i = 0; i < byteArr.length; i++) {
            int valueAtPos = byteArr[i];
            boolean[] arrOfValue = intToBitArr(valueAtPos, BASE_UNIT8); //any byte fits in 8 bit.
            bitArrs = insertArrIntoArr(bitArrs, arrOfValue, i * BASE_UNIT8);
        }
        LogUtils.d("From byte array: " + Arrays.toString(byteArr));
        LogUtils.d("To bit array: " + StringUtils.bitArrToString(bitArrs));
        return bitArrs;
    }

    /**
     * Return value of bit at bitPosStr in bitArr.
     *
     * @param bitArr    Array of bits.
     * @param bitPosStr position of bit needs to get value. Ex: "0.5" => bit 5 of byte 0.
     * @return value at position. (1/0).
     */
    private static int getValueAtPosition(boolean[] bitArr, String bitPosStr) {
        int indexOfBit = bitIndex(bitPosStr);
        if (bitArr == null || bitArr.length <= indexOfBit) {
            LogUtils.e("Bit array is null or bit position is out of bound bit array.");
            return -1;
        }
        LogUtils.d("String \"" + bitPosStr + "\" is at index = " + indexOfBit + " has value = " + bitArr[indexOfBit]);
        return bitArr[indexOfBit] == true ? 1 : 0;
    }

    /**
     * Set int value (0/1) at bitPosStr in bitArr.
     *
     * @param bitArr    Array of bits.
     * @param bitPosStr position of bit needs to set value. Ex: "0.5" => bit 5 of byte 0.
     * @param value     value to insert to bit. (0/1).
     * @return bit array has set value at position.
     */
    private static boolean[] setValueAtPosition(boolean[] bitArr, String bitPosStr, int value) {
        //Get index from string.
        int indexOfBit = bitIndex(bitPosStr);
        //Validate.
        if (bitArr == null || bitArr.length <= indexOfBit) {
            LogUtils.e("Bit array is null or bit position is out of bound bit array.");
            return bitArr;
        }
        if (value < 0 || value > 1) {
            LogUtils.e("Value to be inserted too large to insert into a bit.");
            return bitArr;
        }
        //Set value to bit array.
        if (value == 0) {
            bitArr[indexOfBit] = false;
        } else {
            bitArr[indexOfBit] = true;
        }

        return bitArr;
    }


    /**
     * Get value in range of bit in bitArr.
     *
     * @param bitArr      Array of bits.
     * @param bitRangeStr Range of bit needs to get value. Ex: "1.7-2.0".
     * @return converted decimal value in that range of bit.
     */
    private static int getValueAtRange(boolean[] bitArr, String bitRangeStr) {
        if (bitArr == null || bitArr.length == 0) {
            LogUtils.e("Bit array is null or bit position is out of bound bit array.");
            return -1;
        }
        if (!bitRangeStr.matches(FORMAT_BIT_RANGE_STR)) {
            LogUtils.e("Input bit range string does not match format.");
            return -1; // Check format of string.
        }
        int indexOfSeperator = bitRangeStr.indexOf("-");
        String strStartPos = bitRangeStr.substring(0, indexOfSeperator);
        String strEndPos = bitRangeStr.substring(indexOfSeperator + 1);
        int indexOfStartPos = bitIndex(strStartPos);
        int indexOfEndPos = bitIndex(strEndPos);
        if (bitArr.length <= indexOfEndPos) {
            indexOfEndPos = bitArr.length - 1;
        }
        int rangeSize = indexOfEndPos - indexOfStartPos + 1;

        //Extract bit range from source bit array.
        boolean[] bitRange = new boolean[rangeSize];
        int copyIndex = indexOfStartPos;
        for (int i = 0; i < rangeSize; i++) {
            bitRange[i] = bitArr[copyIndex];
            copyIndex++;
        }
        return bitArrToInt(bitRange);
    }

    /**
     * Set value into range of bit in bitArr.
     *
     * @param bitArr      Array of bits.
     * @param bitRangeStr Range of bit needs to get value. Ex: "1.7-2.0".
     * @param value       int value to set into range of bits.
     * @return array of bits which has value inserted into that range of bits.
     */
    private static boolean[] setValueIntoRange(boolean[] bitArr, String bitRangeStr, int value) {
        if (!bitRangeStr.matches(FORMAT_BIT_RANGE_STR)) {
            LogUtils.e("Input bit range string does not match format.");
            return bitArr; // Check format of string.
        }
        int indexOfSeperator = bitRangeStr.indexOf("-");
        String strStartPos = bitRangeStr.substring(0, indexOfSeperator);
        String strEndPos = bitRangeStr.substring(indexOfSeperator + 1);
        int indexOfStartPos = bitIndex(strStartPos);
        int indexOfEndPos = bitIndex(strEndPos);
        int sizeOfRange = indexOfEndPos - indexOfStartPos + 1;

        if (bitArr == null || bitArr.length <= indexOfEndPos) {
            LogUtils.e("Bit array is null or bit position is out of bound bit array.");
            return bitArr;
        }
        boolean[] valueToArrBit = intToBitArr(value, sizeOfRange);
        LogUtils.d("Bit arr before insert: " + StringUtils.bitArrToString(bitArr));
        bitArr = insertArrIntoArr(bitArr, valueToArrBit, indexOfStartPos);
        LogUtils.d("Bit arr before insert value: " + StringUtils.bitArrToString(bitArr));
        return bitArr;
    }


    /**
     * Get index of bit based on String.
     *
     * @param bitPosStr String format: "byte.pos" Ex: "1.5" - position of bit 5 of
     *                  byte 1.
     * @return position in array. Ex: "1.5" => 10.
     * [0.7,0.6,0.5,0.4,0.3,0.2,0.1,0.0][1.7,1.6,(1.5),1.4,1.3,1.2,1.1,1.0]
     */
    private static int bitIndex(String bitPosStr) {
        if (!bitPosStr.matches(FORMAT_BIT_POS_STR)) {
            LogUtils.e("Input bit range string does not match format.");
            return -1; // Check format of string.
        }
        int indexOfPoint = bitPosStr.indexOf(".");
        int byteNum = Integer.parseInt(bitPosStr.substring(0, indexOfPoint));
        int posNum = Integer.parseInt(bitPosStr.substring(indexOfPoint + 1));

        int index = -1;

        for (int i = 0; i <= byteNum; i++) {
            for (int j = 7; j >= 0; j--) {
                index++;
                if ((i == byteNum) && j == posNum) {
                    LogUtils.d("String = " + bitPosStr + " index of bit = " + index);
                    return index;
                }
            }
        }
        LogUtils.d("String = " + bitPosStr + " index of bit = " + index);
        return index;
    }

    /**
     * Convert an bit array into int value
     *
     * @param bitArr array of bits.
     * @return int value converted from bit arr.
     */
    private static int bitArrToInt(boolean[] bitArr) {
        int output = 0;
        int arrLength = bitArr.length;
        for (int i = 0; i < arrLength; i++) {
            output = (output << 1) + (bitArr[i] ? 1 : 0);
        }
        LogUtils.d("From bit arr " + StringUtils.bitArrToString(bitArr) + "to integer = " + output);
        return output;
    }

    /**
     * Convert an bit array into int value
     *
     * @param bitArr     array of bits.
     * @param startIndex start index. (count from 0).
     * @param rangeSize  range size to get value from start index. (count from 1).
     * @return int value converted from range startIndex to endIndex in bitArr.
     */
    private static int bitArrToInt(boolean[] bitArr, int startIndex, int rangeSize) {
        int output = 0;
        int endIndex = startIndex + rangeSize;
        for (int index = startIndex; index < endIndex; index++) {
            output = (output << 1) + (bitArr[index] ? 1 : 0);
        }
        LogUtils.d("Bit arr " + StringUtils.bitArrToString(bitArr)
                + " start index = " + startIndex
                + "range size= " + rangeSize
                + "to integer = " + output);
        return output;
    }

    /**
     * Convert an Integer into bit array (Format Unit8).
     *
     * @param number     int number.
     * @param bitArrSize size of bit array (Count from 1).
     * @return bit array from int.
     */
    private static boolean[] intToBitArr(int number, int bitArrSize) {
        if (number < 0) number &= ParamTag.CHECK_SUM_MESSAGE; //Convert to unsigned number.
        String binary = Integer.toBinaryString(number);
        if (bitArrSize < binary.length()) {
            LogUtils.e("Int number: " + number + " does not fit in bit array size = " + bitArrSize);
            return null;
        }
        // Create bit array.
        boolean[] bitArr = new boolean[bitArrSize];
        int indexArr = bitArrSize - 1;
        for (int i = (binary.length() - 1); i >= 0; i--) {
            String bitValue = String.valueOf(binary.charAt(i));
            if (bitValue.equals("0")) {
                bitArr[indexArr] = false;
            } else {
                bitArr[indexArr] = true;
            }
            indexArr--;
        }
        LogUtils.d("From integer: " + number + " to bit arr: " + StringUtils.bitArrToString(bitArr));
        return bitArr;
    }

    /**
     * Insert value from an array into an array.
     *
     * @param arrContainer Container array.
     * @param arrToInsert  array needs to insert to container array.
     * @param startIndex   start index to insert.
     * @return arrayContainer which has inserted sub-array.
     */
    private static boolean[] insertArrIntoArr(boolean[] arrContainer, boolean[] arrToInsert, int startIndex) {
        if (arrContainer == null || arrContainer.length == 0
                || arrToInsert == null || arrToInsert.length == 0
                || startIndex < 0
                || startIndex > (arrContainer.length - arrToInsert.length)
                || arrToInsert.length > arrContainer.length) return null;

        int i = 0;
        while (i < arrToInsert.length) {
            arrContainer[startIndex] = arrToInsert[i];
            startIndex++;
            i++;
        }
        return arrContainer;
    }

    /**
     * Get Transaction identity of VEHICLE_TRANSACTION_REQUEST message;
     *
     * @return int transaction identity.
     */
    public static int getNewTransactionIdentity() {
        int transactionIdentity = BluetoothManager.getInstance().getTransactionIdentity();
        transactionIdentity++;
        transactionIdentity %= 256;    //256: Transaction identity range.
        BluetoothManager.getInstance().setTransactionIdentity(transactionIdentity);
        return transactionIdentity;
    }


    public static final int BASE_UNIT8 = 8;
    public static final String FORMAT_BIT_POS_STR = "\\d+\\.[0-7]"; // format of string bit position. Ex: "a.b".
    public static final String FORMAT_BIT_RANGE_STR = "\\d+\\.[0-7]-\\d+\\.[0-7]"; // Format of string bit range. Ex: "a.b-c.d".
}
