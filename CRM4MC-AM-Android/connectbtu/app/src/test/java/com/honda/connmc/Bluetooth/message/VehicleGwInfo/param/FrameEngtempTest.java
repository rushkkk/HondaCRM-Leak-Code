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
public class FrameEngtempTest {

    @Mock
    VehicleParam mMockParam;

    @Test
    public void getReadableValueCase1() {
        String actual = setUp(0, 0);
        String expect = "-40℃";
        assertEquals(expect, actual);
    }

    @Test
    public void getReadableValueCase2() {
        String actual = setUp(0, 40);
        String expect = "0℃";
        assertEquals(expect, actual);
    }

    @Test
    public void getReadableValueCase3() {
        String actual = setUp(0, 0xBE);
        String expect = "150℃";
        assertEquals(expect, actual);
    }

    @Test
    public void getReadableValueCase4() {
        String actual = setUp(0, 0xFF);
        String expect = "Error";
        assertEquals(expect, actual);
    }

    @Test
    public void getReadableValueCase5() {
        String actual = setUp(1, 0);
        String expect = "0km";
        assertEquals(expect, actual);
    }

    @Test
    public void getReadableValueCase6() {
        String actual = setUp(1, 0x188D27);
        String expect = "1608999km";
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
        FrameEngtemp frame = new FrameEngtemp(mMockParam);
        frame.getListSubValue().get(subValuePos).setValue(value);
        return frame.getListSubValue().get(subValuePos).getReadableValue();
    }
}
