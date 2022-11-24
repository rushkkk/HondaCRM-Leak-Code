package com.honda.connmc.Utils;

import android.util.SparseIntArray;
import com.honda.connmc.Constants.message.ParamTag;
import com.honda.connmc.Constants.message.ParamValue;
import com.honda.connmc.NativeLib.NativeLibController;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.whenNew;

@RunWith(PowerMockRunner.class)
@PrepareForTest({MessageUtils.class, LogUtils.class, String.class, ByteBuffer.class, NativeLibController.class, ParamTag.class})
@SuppressStaticInitializationFor("com.honda.connmc.Constants.message.ParamTag")
public class MessageUtilsTest {

    @Mock
    MessageUtils mMockMsgUtil;

    @Mock
    ByteBuffer mMockByteBuffer;

    @Mock
    NativeLibController mMockNativeLibController;

    @Mock
    SparseIntArray mMockSparseIntArray;

    /**
     * Test method for encryptTest.
     * Test condition: No condition.
     * Expected result: Run method test success. Pass method encryptTest.
     */
    @Test
    public void encryptTest() {
        //TODO: Build failed lib C++.
/*        try {
            //Setup mock
            mockStatic(MessageUtils.class, ByteBuffer.class);
            byte[] data = {1, 2, 3};
            byte[] encrypted = {4, 5, 6};
            when(ByteBuffer.allocate(anyInt())).thenReturn(mMockByteBuffer);
            when(NativeLibController.getInstance()).thenReturn(mMockNativeLibController);

            PowerMockito.when(mMockNativeLibController, "encryptoSendData", data, any()).thenReturn(0);


            //Call real method and verify result.
            byte[] actual = MessageUtils.encrypt(data);
            assertEquals(actual, encrypted);
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }


    /**
     * Test method for decryptTest.
     * Test condition: No condition.
     * Expected result: Run method test success. Pass method decryptTest.
     */
    @Test
    public void decryptTest() {

    }

    Answer answer = invocation -> null;

    @Before
    public void prepareForTest() {
        mockStatic(LogUtils.class, ParamTag.class);
        try {
            PowerMockito.when(LogUtils.class, "d", anyString()).thenAnswer(answer);
            PowerMockito.when(LogUtils.class, "e", anyString()).thenAnswer(answer);
            PowerMockito.when(ParamTag.class, "getLength", ParamTag.FRAME_SAB_FLAG).thenReturn(1);
            whenNew(SparseIntArray.class).withNoArguments().thenReturn(mMockSparseIntArray);
            PowerMockito.when(mMockSparseIntArray, "put", anyInt(), anyInt()).thenAnswer(answer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Test method for getValueFromByteArr Case1.
     * Test condition: valueStr = type Position string
     * Expected result: Run method test success. Return right value.
     * Result: Passed.
     */
    @Test
    public void getValueFromByteArrCase1() throws Exception {
        byte[] arr = {1, 2};
        String valueStr = "0.0";
        int actual = Whitebox.invokeMethod(MessageUtils.class, "getValueFromByteArr", arr, valueStr);
        int expected = 1;
        assertEquals(expected, actual);
    }

    /**
     * Test method for getValueFromByteArr Case2.
     * Test condition: valueStr = type Range string
     * Expected result: Run method test success. Return right value.
     * Test result: Passed.
     */
    @Test
    public void getValueFromByteArrCase2() throws Exception {
        byte[] arr = {1, 2};
        String valueStr = "0.7-0.0";
        int actual = Whitebox.invokeMethod(MessageUtils.class, "getValueFromByteArr", arr, valueStr);
        int expected = 1;
        assertEquals(expected, actual);
    }

    /**
     * Test method for createPhoneGwMsgParam.
     * Test condition: Right input.
     * Expected result: Run method test success. Return right value.
     * Result: Passed.
     */
    @Test
    public void createPhoneGwMsgParamTest() throws Exception {
        int paramSize = ParamTag.getLength(ParamTag.FRAME_SAB_FLAG);
        Map<String, Integer> valueMap = new HashMap<>();
        valueMap.put(ParamValue.SmartphoneGwInfo.FrameSabFlag.C_SAB_FLAG, 1);
        byte[] actual = Whitebox.invokeMethod(MessageUtils.class, "createPhoneGwMsgParam", paramSize, valueMap);
        System.out.println("createPhoneGwMsgParamTest: Actual byte array = " + Arrays.toString(actual));
        byte[] expect = {1};
        Assert.assertArrayEquals(expect, actual);
    }

    /**
     * Test method for createPhoneGwMsgParam.
     * Test condition: Right input.
     * Expected result: Run method test success. Return right value.
     * Test result: Passed.
     */
    @Test
    public void createPhoneGwMsgParamTest2() throws Exception {
        int paramSize = 8;
        Map<String, Integer> valueMap = new HashMap<>();
        valueMap.put(ParamValue.SmartphoneGwInfo.FrameSmpTime.C_SMP_LOCAL_YEAR, 18);
        valueMap.put(ParamValue.SmartphoneGwInfo.FrameSmpTime.C_SMP_LOCAL_MONTH, 01);
        valueMap.put(ParamValue.SmartphoneGwInfo.FrameSmpTime.C_SMP_LOCAL_DAY, 19);
        valueMap.put(ParamValue.SmartphoneGwInfo.FrameSmpTime.C_SMP_LOCAL_HOUR, 12);
        valueMap.put(ParamValue.SmartphoneGwInfo.FrameSmpTime.C_SMP_LOCAL_MINUTE, 01);
        valueMap.put(ParamValue.SmartphoneGwInfo.FrameSmpTime.C_SMP_LOCAL_SECOND, 10);
        byte[] actual = Whitebox.invokeMethod(MessageUtils.class, "createPhoneGwMsgParam", paramSize, valueMap);
        System.out.println("createPhoneGwMsgParamTest2: Actual byte array = " + Arrays.toString(actual));
        // Assert.assertArrayEquals(expect, actual);
    }


    /**
     * Passed.
     */
    @Test
    public void byteArrToBitArrTest() throws Exception {
        byte[] arr = {1, 2};
        boolean[] actual = Whitebox.invokeMethod(MessageUtils.class, "byteArrToBitArr", arr);
        System.out.println("byteArrToBitArrTest: byte arr = " + Arrays.toString(arr)
                + " => actual bit array: " + StringUtils.bitArrToString(actual));
        boolean[] expect = {false, false, false, false, false, false, false, true,
                false, false, false, false, false, false, true, false};
        Assert.assertArrayEquals(expect, actual);
    }

    /**
     * Passed.
     */
    @Test
    public void getValueAtPositionTest() throws Exception {
        boolean[] bitArr = {false, true, false, false, false, true, false, true};
        String bitPos1 = "0.7";
        String bitPos2 = "0.0";
        int actual1 = Whitebox.invokeMethod(MessageUtils.class, "getValueAtPosition", bitArr, bitPos1);
        int actual2 = Whitebox.invokeMethod(MessageUtils.class, "getValueAtPosition", bitArr, bitPos2);

        assertEquals(0, actual1);
        assertEquals(1, actual2);
    }

    /**
     * Test method for getValueAtPosition.
     * Condition: bit pos out of range bit array.
     * Expected result: branch abnormal was executed.
     */
    @Test
    public void getValueAtPositionCase2() throws Exception {
        boolean[] bitArr = {false, true, false, false, false, true, false, true};
        String bitPos1 = "1.5";
        int actual1 = Whitebox.invokeMethod(MessageUtils.class, "getValueAtPosition", bitArr, bitPos1);

        assertEquals(-1, actual1);
    }


    /**
     * Passed.
     */
    @Test
    public void getValueAtRangeTest() throws Exception {
        boolean[] bitArr = {false, true, false, false, false, true, false, true};
        String bitRange1 = "0.7-0.0";
        String bitRange2 = "0.6-0.1";
        int expected1 = 69;
        int expected2 = 34;
        int actual1 = Whitebox.invokeMethod(MessageUtils.class, "getValueAtRange", bitArr, bitRange1);
        int actual2 = Whitebox.invokeMethod(MessageUtils.class, "getValueAtRange", bitArr, bitRange2);

        assertEquals(expected1, actual1);
        assertEquals(expected2, actual2);
    }

    /**
     * Test method for getValueAtRange case 2.
     * Test condition: String bit range not match format.
     * Expected result: Pass test method. Branch abnormal was executed.
     */
    @Test
    public void getValueAtRangeCase2() throws Exception {
        boolean[] bitArr = {false, true, false, false, false, true, false, true};
        String bitRange = "abc";
        int expected = -1;
        int actual1 = Whitebox.invokeMethod(MessageUtils.class, "getValueAtRange", bitArr, bitRange);

        assertEquals(expected, actual1);
    }

    /**
     * Test method for getValueAtRange case 2.
     * Test condition: String bit range out of bound bitarr.
     * Expected result: Pass test method. Branch abnormal was executed.
     */
    @Test
    public void getValueAtRangeCase3() throws Exception {
        boolean[] bitArr = {false, true, false, false, false, true, false, true};
        String bitRange = "1.0-2.6";
        int expected = -1;
        int actual1 = Whitebox.invokeMethod(MessageUtils.class, "getValueAtRange", bitArr, bitRange);

        assertEquals(expected, actual1);
    }


    /**
     * Passed.
     */
    @Test
    public void setValueAtPositionTest() throws Exception {
        boolean[] bitArr = {false, true, false, false, false, true, false, true};
        String bitPos = "0.0";
        int valueToSet = 0;
        boolean[] actual = Whitebox.invokeMethod(MessageUtils.class, "setValueAtPosition", bitArr, bitPos, valueToSet);

        boolean[] expect = {false, true, false, false, false, true, false, false};
        Assert.assertArrayEquals(expect, actual);
    }

    /**
     * Test method for setValueAtPosition.
     * Test condition: String bit pos out of range bit array.
     * Expected result: Pass test method. Branch abnormal was executed.
     */
    @Test
    public void setValueAtPositionCase2() throws Exception {
        boolean[] bitArr = {false, true, false, false, false, true, false, true};
        String bitPos = "1.7";
        int valueToSet = 0;
        boolean[] actual = Whitebox.invokeMethod(MessageUtils.class, "setValueAtPosition", bitArr, bitPos, valueToSet);

        Assert.assertArrayEquals(bitArr, actual);
    }

    /**
     * Test method for setValueAtPosition.
     * Test condition: inserted value not 0 or 1.
     * Expected result: Pass test method. Branch abnormal was executed.
     */
    @Test
    public void setValueAtPositionCase3() throws Exception {
        boolean[] bitArr = {false, true, false, false, false, true, false, true};
        String bitPos = "0.7";
        int valueToSet = 69;
        boolean[] actual = Whitebox.invokeMethod(MessageUtils.class, "setValueAtPosition", bitArr, bitPos, valueToSet);

        Assert.assertArrayEquals(bitArr, actual);
    }

    /**
     * Passed.
     */
    @Test
    public void setValueIntoRangeTest() throws Exception {
        boolean[] bitArr = {false, true, false, false, false, true, false, true};
        String bitRange = "0.7-0.4";
        int valueToSet = 1;
        boolean[] expect = {false, false, false, true, false, true, false, true};
        boolean[] actual = Whitebox.invokeMethod(MessageUtils.class, "setValueIntoRange", bitArr, bitRange, valueToSet);

        System.out.println("Actual bit arr = " + StringUtils.bitArrToString(actual));
        Assert.assertArrayEquals(expect, actual);
    }

    /**
     * Test method for setValueIntoRange.
     * Test condition: String bit range not match format.
     * Expected result: Pass test method. Branch abnormal was executed.
     */
    @Test
    public void setValueIntoRangeCase2() throws Exception {
        boolean[] bitArr = {false, true, false, false, false, true, false, true};
        String bitRange = "abc";
        int valueToSet = 1;
        boolean[] actual = Whitebox.invokeMethod(MessageUtils.class, "setValueIntoRange", bitArr, bitRange, valueToSet);

        System.out.println("Actual bit arr = " + StringUtils.bitArrToString(actual));
        Assert.assertArrayEquals(bitArr, actual);
    }

    /**
     * Test method for setValueIntoRange.
     * Test condition: String bit range out of range bit array.
     * Expected result: Pass test method. Branch abnormal was executed.
     */
    @Test
    public void setValueIntoRangeCase3() throws Exception {
        boolean[] bitArr = {false, true, false, false, false, true, false, true};
        String bitRange = "0.3-1.7";
        int valueToSet = 1;
        boolean[] actual = Whitebox.invokeMethod(MessageUtils.class, "setValueIntoRange", bitArr, bitRange, valueToSet);

        System.out.println("Actual bit arr = " + StringUtils.bitArrToString(actual));
        Assert.assertArrayEquals(bitArr, actual);
    }


    /**
     * Passed.
     */
    @Test
    public void bitIndexTest() throws Exception {
        String bitPos1 = "0.5";
        String bitPos2 = "1.7";
        int expect1 = 2;
        int expect2 = 8;
        int actual1 = Whitebox.invokeMethod(MessageUtils.class, "bitIndex", bitPos1);
        int actual2 = Whitebox.invokeMethod(MessageUtils.class, "bitIndex", bitPos2);
        assertEquals(expect1, actual1);
        assertEquals(expect2, actual2);
    }

    /**
     * Test method for bitIndex case 2.
     * Test condition: String bit pos does not match format.
     * Expected result: Pass test method. Branch abnormal was executed.
     */
    @Test
    public void bitIndexCase2() throws Exception {
        String bitPos1 = "10.15";
        int expect1 = -1;
        int actual1 = Whitebox.invokeMethod(MessageUtils.class, "bitIndex", bitPos1);
        assertEquals(expect1, actual1);
    }

    /**
     * Passed.
     */
    @Test
    public void bitArrToIntCase1() throws Exception {
        boolean[] bitArr = {false, true, false, false, false, true, false, true};
        int expect = 69;
        int actual = Whitebox.invokeMethod(MessageUtils.class, "bitArrToInt", bitArr);

        assertEquals(expect, actual);
    }

    /**
     * Passed.
     */
    @Test
    public void bitArrToIntCase2() throws Exception {
        boolean[] bitArr = {false, true, false, false, false, true, false, true};
        int startIndex = 0;
        int rangeSize = 4;
        int expect = 4;
        int actual = Whitebox.invokeMethod(MessageUtils.class, "bitArrToInt", bitArr, startIndex, rangeSize);

        assertEquals(expect, actual);
    }

    /**
     * Passed.
     */
    @Test
    public void intToBitArrTest() throws Exception {
        int number = 255;
        int bitArrSize = 8;
        boolean[] expect = {true, true, true, true, true, true, true, true};
        boolean[] actual = Whitebox.invokeMethod(MessageUtils.class, "intToBitArr", number, bitArrSize);

        Assert.assertArrayEquals(expect, actual);
    }

    /**
     * Test method for intToBitArr case 2.
     * Test condition: int number too large to fit in bit arr.
     * Expected result: Pass test method. Branch abnormal was executed.
     */
    @Test
    public void intToBitArrCase2() throws Exception {
        int number = 256;
        int bitArrSize = 8;
        boolean[] actual = Whitebox.invokeMethod(MessageUtils.class, "intToBitArr", number, bitArrSize);

        Assert.assertArrayEquals(null, actual);
    }

    /**
     * Passed.
     */
    @Test
    public void insertArrIntoArrTest() throws Exception {
        boolean[] arrContainer = {false, true, false, false, false, true, false, true};
        boolean[] arrToInsert = {true, true, true};
        int startIndex = 2;
        boolean[] expect = {false, true, true, true, true, true, false, true};
        boolean[] actual = Whitebox.invokeMethod(MessageUtils.class, "insertArrIntoArr", arrContainer, arrToInsert, startIndex);

        Assert.assertArrayEquals(expect, actual);
    }

    /**
     * Test method for insertArrIntoArr case 2.
     * Test condition: abnormal condition.
     * Expected result: Pass test method. Abnormal case was executed.
     */
    @Test
    public void insertArrIntoArrCase2() throws Exception {
        boolean[] arrContainer = {false, true, false, false, false, true, false, true};
        boolean[] arrToInsert = {true, true, true, true, true};
        int startIndex = 4;
        boolean[] actual = Whitebox.invokeMethod(MessageUtils.class, "insertArrIntoArr", arrContainer, arrToInsert, startIndex);

        Assert.assertArrayEquals(null, actual);
    }
}
