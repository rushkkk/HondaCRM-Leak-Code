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
public class FrameMaintanceTest {

    @Mock
    VehicleParam mMockParam;

    @Test
    public void getReadableValueCase1() {
        String actual = setUp(0, 0);
        String expect = "- - -";
        assertEquals(expect, actual);
    }

    @Test
    public void getReadableValueCase2() {
        String actual = setUp(0, 1);
        String expect = "100　「OIL LIFE％」";
        assertEquals(expect, actual);
    }
    @Test
    public void getReadableValueCase3() {
        String actual = setUp(0, 5);
        String expect = "60　「OIL LIFE％」";
        assertEquals(expect, actual);
    }

    @Test
    public void getReadableValueCase4() {
        String actual = setUp(0, 0xA);
        String expect = "15　「SERVICE OIL LIFE％」";
        assertEquals(expect, actual);
    }

    @Test
    public void getReadableValueCase5() {
        String actual = setUp(0, 0xD);
        String expect = "0";
        assertEquals(expect, actual);
    }

    @Test
    public void getReadableValueCase6() {
        String actual = setUp(0, 0xE);
        String expect = "";
        assertEquals(expect, actual);
    }

    @Test
    public void getReadableValueCase7() {
        String actual = setUp(1, 0);
        String expect = "No maintenance function";
        assertEquals(expect, actual);
    }

    @Test
    public void getReadableValueCase8() {
        String actual = setUp(1, 4);
        String expect = " J0 Smart Maintenance";
        assertEquals(expect, actual);
    }

    @Test
    public void getReadableValueCase9() {
        String actual = setUp(2, 0);
        String expect = "-9999";
        assertEquals(expect, actual);
    }

    @Test
    public void getReadableValueCase10() {
        String actual = setUp(2, 0x1ADAE);
        String expect = "99999";
        assertEquals(expect, actual);
    }

    @Test
    public void getReadableValueCase11() {
        String actual = setUp(2, 0x1FFFF);
        String expect = "---";
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
        FrameMaintance frame = new FrameMaintance(mMockParam);
        frame.getListSubValue().get(subValuePos).setValue(value);
        return frame.getListSubValue().get(subValuePos).getReadableValue();
    }
}
