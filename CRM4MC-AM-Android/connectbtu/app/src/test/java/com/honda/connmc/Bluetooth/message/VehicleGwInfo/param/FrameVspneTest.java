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
public class FrameVspneTest {

    @Mock
    VehicleParam mMockParam;

    @Test
    public void getReadableValueCase1() {
        String actual = setUp(0, 0);
        String expect = "0km/h";
        assertEquals(expect, actual);
    }

    @Test
    public void getReadableValueCase2() {
        String actual = setUp(0, 0xF9);
        String expect = "249km/h";
        assertEquals(expect, actual);
    }

    @Test
    public void getReadableValueCase3() {
        String actual = setUp(0, 0xFA);
        String expect = "over 250km/h";
        assertEquals(expect, actual);
    }

    @Test
    public void getReadableValueCase4() {
        String actual = setUp(0, 0xFF);
        String expect = "During communication failure";
        assertEquals(expect, actual);
    }

    @Test
    public void getReadableValueCase5() {
        String actual = setUp(1, 0xFFFE);
        String expect = "65534";
        assertEquals(expect, actual);
    }

    @Test
    public void getReadableValueCase6() {
        String actual = setUp(1, 0xFFFF);
        String expect = "65535";
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
        FrameVspne frame = new FrameVspne(mMockParam);
        frame.getListSubValue().get(subValuePos).setValue(value);
        return frame.getListSubValue().get(subValuePos).getReadableValue();
    }
}
