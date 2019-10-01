package com.taraskudelia.taskmanager.util;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Taras Kudelia
 * @since 20.09.19
 */
@Slf4j
public class DateUtil {

    private final static String[] MONTH_NAMES = {
            "January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"
    };

    public static String getMonthName(int monthNumber) {
        if (monthNumber < 0 || monthNumber > 11) {
            log.error("Incorrect month number " + monthNumber);
            return null;
        }
        return MONTH_NAMES[monthNumber];
    }

}
