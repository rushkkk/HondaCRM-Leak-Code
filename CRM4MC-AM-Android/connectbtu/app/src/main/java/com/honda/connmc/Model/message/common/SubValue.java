package com.honda.connmc.Model.message.common;

import com.honda.connmc.Constants.message.ParamTag;

public class SubValue {
    public enum Type {
        RAW_VALUE, ON_OFF, KM_H, KM, ENGTEMP, OUTTEMP, TRICOM_RANGE, HOUR, MINUTE, DISPLAY_RANGE_UNIT, DISPLAY_TRIP,
        RES_REFTIME, OIL_LIFE, MAINT_INFO, MAINT_DATA, MAINT_UNIT, MAINT_RESULT, DISPLAY_CONDITION_MAINT, CAR_CONDITION_MAINT,
        EXCHANGE_DIST, TRICOM4_UNIT, METER_ECO_FUEL, METER_FUEL_REMAIN, VEH_DATA_TEMP, ASP, BRAKE_PRESSURE
    }

    private Type type;              //Type of value
    private String name;            //Display name.
    private String rawName;         //Raw name
    private String bitLayout;       //bit pos or bit range.
    private int rawValue;           //SubValue raw value.
    private String readableValue;   //ReaableValue

    public SubValue(Type type, String rawName, String name, String bitLayout) {
        this.type = type;
        this.rawName = rawName;
        this.name = name;
        this.bitLayout = bitLayout;
    }

    public SubValue() {
    }

    public String getRawName() {
        return rawName;
    }

    public String getName() {
        return name;
    }


    public String getBitLayout() {
        return bitLayout;
    }


    public String getReadableValue() {
        return readableValue;
    }

    public int getValue() {
        return rawValue;
    }

    public void setValue(int value) {
        this.rawValue = value;
        readableValue = setReadableValue(value);
    }

    private String setReadableValue(int value) {
        switch (type) {
            case RAW_VALUE:
                return String.valueOf(value);

            case ON_OFF:
                return getStrOnOff(value);

            case KM_H:
                return getStrKmH(value);

            case KM:
                return value + "km";

            case ENGTEMP:
                return getStrEngTemp(value);

            case OUTTEMP:
                return getStrOutTemp(value);

            case TRICOM_RANGE:
                return getStrTricomRange(value);

            case DISPLAY_RANGE_UNIT:
                return value == 0 ? "km" : "mile";

            case HOUR:
                return value + "hours";

            case MINUTE:
                return value + "minutes";

            case RES_REFTIME:
                return getStrResReftime(value);

            case DISPLAY_TRIP:
                return (float) value / 10 + "";

            case OIL_LIFE:
                return getStrOilLife(value);

            case MAINT_INFO:
                return getStrMaintInfo(value);

            case MAINT_DATA:
                return getStrMaintData(value);

            case MAINT_UNIT:
                return getStrMaintUnit(value);

            case MAINT_RESULT:
                return getStrMaintResult(value);

            case DISPLAY_CONDITION_MAINT:
                return getStrDisplayConditionMaint(value);

            case CAR_CONDITION_MAINT:
                return value == 0 ? "Stop" : "Running";

            case EXCHANGE_DIST:
                return getStrExchangeDist(value);

            case TRICOM4_UNIT:
                return getStrTricom4Unit(value);
            case METER_ECO_FUEL:
                return getStrMeterEcoFuel(value);

            case METER_FUEL_REMAIN:
                return getStrMeterFuelRemain(value);

            case VEH_DATA_TEMP:
                return value < ParamTag.CHECK_SUM_MESSAGE ? (value - 40) + "℃" : "Error";
            case ASP:
                return (float) value / 10 + "deg";

            case BRAKE_PRESSURE:
                return String.format("%02Xh", value);

            default:
                return "";
        }
    }


    private String getStrMeterFuelRemain(int value) {
        if (value == 0) {
            return "RES PATTERN";
        } else if (value <= 8) {
            return value + "(SEGMENT)";
        } else {
            return "Error";
        }
    }

    private String getStrMeterEcoFuel(int value) {
        if (value < 0x7D0) {
            return String.valueOf((float) value / 10);
        } else if (value < 0x7FF) {
            return "200.0 or more";
        } else {
            return "Error";
        }
    }

    private String getStrTricom4Unit(int value) {
        switch (value) {
            case 0:
                return "km/L";
            case 1:
                return "mpg(U.S. gallon)";
            case 2:
                return "mpg(imperial gallon)";
            case 3:
                return "l/100km";
            default:
                return "";
        }
    }

    private String getStrExchangeDist(int value) {
        if (value == 0) {
            return "";
        } else if (value <= 24) {
            return value + ",000";
        } else {
            return "Error";
        }
    }


    private String getStrDisplayConditionMaint(int value) {
        switch (value) {
            case 1:
                return "REMOVE KEY（MECHANICAL KEY）";
            case 2:
                return "REMOVE KEY（REMOTE KEY）";
            case 3:
                return "RETURN IGN TO LOCK POSITION (MECHANICAL KEY)";
            case 4:
                return "ACCESSORY POSITION (MECHANICAL KEY)";
            case 5:
                return "RETURN IGN TO LOCK POSITION (KEYLESS)";
            case 6:
                return "ACCESSORY POSITION (KEYLESS)";
            case 7:
                return "SHIFT TO P";
            case 8:
                return "VEHICLE OFF";
            case 9:
                return "ACCESSORY MODE";

            default:
                return "";
        }
    }

    private String getStrMaintResult(int value) {
        switch (value) {
            case 0:
                return "normally";
            case 1:
                return "Reset success";
            case 2:
                return "Reset failure";
            default:
                return "";
        }
    }

    private String getStrMaintUnit(int value) {
        if (value == 1) {
            return "km";
        } else if (value == 2) {
            return "mile";
        } else if (value == 3) {
            return "days";
        } else {
            return "";
        }
    }

    private String getStrMaintData(int value) {
        if (value <= 0x1ADAE) {
            return String.valueOf(value - 9999);
        } else if (value == 0x1ADAF || value == 0x1FFFE) {
            return "";
        } else {
            return "---";
        }
    }

    private String getStrMaintInfo(int value) {
        switch (value) {
            case 0:
                return "No maintenance function";
            case 1:
                return "D0/E0  Maintenance";
            case 2:
                return "A0/C0 Smart Maintenance";
            case 3:
                return "T0 Smart Maintenance";
            case 4:
                return " J0 Smart Maintenance";
            default:
                return "";
        }
    }

    private String getStrOilLife(int value) {
        if (value == 0) {
            return "- - -";
        } else if (value < 0x9) {
            int num = 100 - (value - 1) * 10;
            return num + "　「OIL LIFE％」";
        } else if (value == 0xA) {
            return "15　「SERVICE OIL LIFE％」";
        } else if (value == 0xB) {
            return "10　「SERVICE OIL LIFE％」";
        } else if (value == 0xC) {
            return "5　「SERVICE OIL LIFE％」";
        } else if (value == 0xD) {
            return "0";
        } else {
            return "";
        }
    }


    private String getStrTricomRange(int value) {
        if (value <= 0x7FD) {
            return (float) value / 10 + "";
        } else if (value == 0x7FE) {
            return "";
        } else {
            return "－－－－";
        }
    }


    private String getStrEngTemp(int value) {
        if (value < ParamTag.CHECK_SUM_MESSAGE) {
            return (value - 40) + "℃";
        } else {
            return "Error";
        }
    }

    private String getStrOutTemp(int value) {
        if (value == 0) {
            return "";
        } else if (value < 0xFE) {
            return (value - 41) + "℃";
        } else {
            return "Error";
        }
    }

    private String getStrKmH(int value) {
        if (value <= 0xF9) {
            return value + "km/h";
        } else if (value < 0xFF) {
            return "over 250km/h";
        } else {
            return "During communication failure";
        }
    }

    private String getStrOnOff(int value) {
        if (value == 0) {
            return "OFF";
        } else if (value == 1) {
            return "ON";
        } else {
            return "LONG PRESS";
        }
    }

    private String getStrResReftime(int value) {
        if (value == 0) {
            return "OFF";
        } else if (value == 1) {
            return "Finish";
        } else {
            return "Fail";
        }
    }
}
