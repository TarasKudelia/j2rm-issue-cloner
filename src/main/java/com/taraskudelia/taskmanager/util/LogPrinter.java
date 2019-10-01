package com.taraskudelia.taskmanager.util;

import org.slf4j.Logger;

public class LogPrinter {

    public static void exitWithError(Logger log, String errorMessage) {
        log.error(errorMessage);
        System.exit(1);
    }

}
