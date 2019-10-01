package com.taraskudelia.taskmanager.util;

/**
 * @author Taras Kudelia
 * @since 20.09.19
 */
public class DateUtil {

    private final static String[] MONTH_NAMES = {
            "January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"
    };

    public static String getMonthName(int monthNumber) {
        if (monthNumber < 0 || monthNumber > 11) {
            LogPrinter.printError("Incorrect month number " + monthNumber + ".");
            return null;
        }
        return MONTH_NAMES[monthNumber];
    }

}
