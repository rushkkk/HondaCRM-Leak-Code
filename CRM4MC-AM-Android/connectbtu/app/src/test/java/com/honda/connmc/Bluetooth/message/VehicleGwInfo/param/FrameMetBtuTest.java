package com.honda.connmc.Bluetooth.message.VehicleGwInfo.param;

import com.honda.connmc.Model.message.common.VehicleParam;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertEquals;

@RunWith(PowerMockRunner.class)
@SuppressStaticInitializationFor("com.honda.connmc.Constants.message.ParamTag")
public class FrameMetBtuTest {

    @Mock
    VehicleParam mMockParam;

    @Test
    public void getReadableValueCase1() {
        String actual = setUp(0, 0);
        String expect = "OFF";
        assertEquals(expect, actual);
    }

    @Test
    public void getReadableValueCase2() {
        String actual = setUp(0, 1);
        String expect = "ON";
        assertEquals(expect, actual);
    }

    @Test
    public void getReadableValueCase3() {
        String actual = setUp(3, 0);
        String expect = "OFF";
        assertEquals(expect, actual);
    }

    @Test
    public void getReadableValueCase4() {
        String actual = setUp(3, 1);
        String expect = "Finish";
        assertEquals(expect, actual);
    }

    @Test
    public void getReadableValueCase5() {
        String actual = setUp(3, 2);
        String expect = "Fail";
        assertEquals(expect, actual);
    }


    /**
     * Setup mock.
     *
     * @param subValuePos position of subvalue in list subvalue.
     * @param value       value at position
     * @return actual getReadableValue.
     */
    private String setUp(int subValuePos, int value) {
        FrameMetBtu frame = new FrameMetBtu(mMockParam);
        frame.getListSubValue().get(subValuePos).setValue(value);
        return frame.getListSubValue().get(subValuePos).getReadableValue();
    }
}
