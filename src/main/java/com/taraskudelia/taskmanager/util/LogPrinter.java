package com.taraskudelia.taskmanager.util;

import com.taraskudelia.taskmanager.TaskManagerApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogPrinter {

    private static final Logger LOGGER = LoggerFactory.getLogger(LogPrinter.class);

    private final static String USAGE = "java -jar " + TaskManagerApplication.class.getSimpleName() + ".java {issue_key}"
                                        + System.lineSeparator();

    public static void exitWithError(String errorMessage) {
        printError(errorMessage);
        LOGGER.info(USAGE);
        System.exit(1);
    }

    public static void printError(String errorMessage) {
        LOGGER.error(errorMessage);
    }

    public static void printInfo(String message) {
        LOGGER.info(message);
    }
}
