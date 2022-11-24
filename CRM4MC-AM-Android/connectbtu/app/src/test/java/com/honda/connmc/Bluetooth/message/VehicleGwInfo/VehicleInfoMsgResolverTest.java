package com.honda.connmc.Bluetooth.message.VehicleGwInfo;

import android.util.SparseIntArray;
import com.honda.connmc.Constants.message.MsgId;
import com.honda.connmc.Constants.message.ParamTag;
import com.honda.connmc.Constants.message.ParamValue;
import com.honda.connmc.Model.BluetoothStatus;
import com.honda.connmc.Model.message.common.VehicleMessage;
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
import org.powermock.reflect.Whitebox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.*;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.whenNew;

@RunWith(PowerMockRunner.class)
@PrepareForTest({VehicleInfoMsgResolver.class, LogUtils.class, ParamTag.class, SparseIntArray.class})
@SuppressStaticInitializationFor("com.honda.connmc.Constants.message.ParamTag")
public class VehicleInfoMsgResolverTest {

    @Mock
    VehicleInfoMsgResolver mMockVehicleInfoMsgResolver;

    @Mock
    ArrayList<BluetoothStatus> mMockArrList;

    @Mock
    VehicleInfoMsgResolver.VehicleInfoListener mMockListener;

    Answer answer = invocation -> null;

    @Mock
    SparseIntArray mMockSparseIntArray;


    @Before
    public void prepareForTest() {
        mockStatic(LogUtils.class, ParamTag.class);
        try {
            Whitebox.setInternalState(mMockVehicleInfoMsgResolver, "mListener", mMockListener);
            PowerMockito.when(LogUtils.class, "d", anyString()).thenAnswer(answer);
            PowerMockito.when(LogUtils.class, "e", anyString()).thenAnswer(answer);
            whenNew(SparseIntArray.class).withNoArguments().thenReturn(mMockSparseIntArray);
            PowerMockito.when(mMockSparseIntArray, "put", anyInt(), anyInt()).thenAnswer(answer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Test method for resolveMsg.
     * Condition: VehicleMessage = GwVehicleInfoMsg.
     * Expected: Run test method success.
     */
    @Test
    public void resolveMsgTest() {
        try {
            PowerMockito.when(mMockVehicleInfoMsgResolver, "resolveMsg", anyObject()).thenCallRealMethod();

            //Prepare message.
            Map<String, Integer> valueMap = new HashMap<>();
            valueMap.put(ParamValue.VehicleGwInfo.FrameMicu.C_HAZARDSW, 1);
            valueMap.put(ParamValue.VehicleGwInfo.FrameMicu.C_IGKEYSW, 0);
            valueMap.put(ParamValue.VehicleGwInfo.FrameMicu.C_STOP, 1);
            byte[] paramData = MessageUtils.createPhoneGwMsgParam(5, valueMap);
            VehicleParam gwVehicleInfo = new VehicleParam((short) ParamTag.FRAME_MICU, paramData);
            VehicleMessage msg = new VehicleMessage(MsgId.GW_SMARTPHONE_INFO_INDICATION, gwVehicleInfo);

            //Call real method.
            mMockVehicleInfoMsgResolver.resolveMsg(msg);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test
    public void getDataFromParamTest() {
        //Prepare message.
        Map<String, Integer> valueMap = new HashMap<>();
        valueMap.put(ParamValue.VehicleGwInfo.FrameMicu.C_HAZARDSW, 1);
        valueMap.put(ParamValue.VehicleGwInfo.FrameMicu.C_IGKEYSW, 0);
        valueMap.put(ParamValue.VehicleGwInfo.FrameMicu.C_STOP, 1);
        byte[] paramData = MessageUtils.createPhoneGwMsgParam(5, valueMap);
        VehicleParam gwVehicleInfo = new VehicleParam((short) ParamTag.FRAME_MICU, paramData);

        try {
            PowerMockito.when(mMockVehicleInfoMsgResolver, "getDataFromParam", anyObject()).thenCallRealMethod();

            //Call real method.
            HashMap<String, BluetoothStatus> actual = Whitebox.invokeMethod(mMockVehicleInfoMsgResolver, "getDataFromParam", gwVehicleInfo);
            if (actual != null) {
                int size = actual.size();
                assertEquals(3, size);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
