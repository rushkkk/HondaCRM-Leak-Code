package com.honda.connmc.Bluetooth.message.VehicleGwInfo.param;

import android.util.SparseIntArray;
import com.honda.connmc.Constants.message.ParamTag;
import com.honda.connmc.Constants.message.ParamValue;
import com.honda.connmc.Model.BluetoothStatus;
import com.honda.connmc.Model.message.common.VehicleParam;
import com.honda.connmc.Utils.LogUtils;
import com.honda.connmc.Utils.MessageUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.whenNew;

@RunWith(PowerMockRunner.class)
@PrepareForTest({FrameMicu.class, LogUtils.class, ParamTag.class})
@SuppressStaticInitializationFor("com.honda.connmc.Constants.message.ParamTag")
public class FrameMicuTest {
    Answer answer = invocation -> null;

    @Mock
    SparseIntArray mMockSparseIntArray;

    @Mock
    VehicleParam mMockParam;

    @Before
    public void prepareForTest() {
        mockStatic(LogUtils.class, ParamTag.class);
        try {
            PowerMockito.when(LogUtils.class, "d", anyString()).thenAnswer(answer);
            PowerMockito.when(LogUtils.class, "e", anyString()).thenAnswer(answer);
            whenNew(SparseIntArray.class).withNoArguments().thenReturn(mMockSparseIntArray);
            PowerMockito.when(mMockSparseIntArray, "put", anyInt(), anyInt()).thenAnswer(answer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


/*    @Test
    public void getAllStatusTest() {
        VehicleParam param = prepareVehicleParam();
        ArrayList<BluetoothStatus> list = new FrameMicu(param).getAllStatus();
        assertEquals(3, list.size());
    }*/


    @Test
    public void getListSubValueTest() {
        VehicleParam param = prepareVehicleParam();
        FrameMicu micu = new FrameMicu(param);
        int statusSize = micu.getAllStatus().size();
        assertEquals(4, statusSize);
    }


    private VehicleParam prepareVehicleParam() {
        Map<String, Integer> valueMap = new HashMap<>();
        valueMap.put(ParamValue.VehicleGwInfo.FrameMicu.C_HAZARDSW, 1);
        valueMap.put(ParamValue.VehicleGwInfo.FrameMicu.C_IGKEYSW, 0);
        valueMap.put(ParamValue.VehicleGwInfo.FrameMicu.C_STOP, 1);
        byte[] paramData = MessageUtils.createPhoneGwMsgParam(5, valueMap);
        return new VehicleParam((short) ParamTag.FRAME_MICU, paramData);
    }

    @Test
    public void getReadableValueCase1(){
        FrameMicu micu = new FrameMicu(mMockParam);
        micu.getListSubValue().get(0).setValue(1);
        String actual = micu.getListSubValue().get(0).getReadableValue();
        String expect = "ON";
        assertEquals(expect,actual);
    }

    @Test
    public void getReadableValueCase2(){
        FrameMicu micu = new FrameMicu(mMockParam);
        micu.getListSubValue().get(0).setValue(0);
        String actual = micu.getListSubValue().get(0).getReadableValue();
        String expect = "OFF";
        assertEquals(expect,actual);
    }

}
