package vn.co.honda.hondacrm.utils;

import java.util.regex.Pattern;

public class MobileUtils {

    public static boolean isValidMobile(String phone) {
        boolean check=false;
        if(Pattern.matches("^[0-9]*$", phone)) {
            if(phone.length() < 6 || phone.length() > 13) {
                check = false;
            } else {
                check = true;
            }
        } else {
            check=false;
        }
        return check;
    }
}
