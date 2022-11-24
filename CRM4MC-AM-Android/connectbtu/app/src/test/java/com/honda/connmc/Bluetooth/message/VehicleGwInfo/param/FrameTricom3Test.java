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
public class FrameTricom3Test {

    @Mock
    VehicleParam mMockParam;

    @Test
    public void getReadableValueCase1() {
        String actual = setUp(2, 0);
        String expect = "";
        assertEquals(expect, actual);
    }

    @Test
    public void getReadableValueCase2() {
        String actual = setUp(2, 1);
        String expect = "-40℃";
        assertEquals(expect, actual);
    }

    @Test
    public void getReadableValueCase3() {
        String actual = setUp(2, 2);
        String expect = "-39℃";
        assertEquals(expect, actual);
    }

    @Test
    public void getReadableValueCase4() {
        String actual = setUp(2, 0xFE);
        String expect = "Error";
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
        FrameTricom3 frame = new FrameTricom3(mMockParam);
        frame.getListSubValue().get(subValuePos).setValue(value);
        return frame.getListSubValue().get(subValuePos).getReadableValue();
    }
}
