package logger;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Custom logger
 */
public class InLogger {
    public static final boolean ACTIVE = true;
    private static boolean showInConsole = true;
    private static final String DEFAULT_LOG_NAME = "general.log";

    /**
     * Writes string in file end
     *
     * @param msg     message
     * @param logFile logfile
     */
    private static void writeToFile(String msg, String logFile) {
        if (ACTIVE == false) {
            return;
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(logFile, true))) {
            if (showInConsole) System.out.println(msg);
            writer.write(msg + "\n");
            writer.flush();
        } catch (IOException ignore) {
            //NOP
        }
    }

    /**
     * Console output enable/disable
     *
     * @param set
     */
    public static void setShowConsole(boolean set) {
        showInConsole = set;
    }

    /**
     * Formatted time
     *
     * @return String
     */
    private static String getTime() {
        return new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(Calendar.getInstance().getTime());
    }

    /**
     * Messege constructor
     *
     * @param level   Message level Exception, Info ecc.
     * @param msg     message body
     * @param logFile log file where write to
     */
    private static void messageConstruct(String level, String msg, String logFile) {
        msg = "[" + getTime() + "]" + level + " " + msg;
        writeToFile(msg, logFile);
    }

    /**
     * INFO
     *
     * @param msg     text
     * @param logFile logfiles
     */
    public static void info(String msg, String logFile) {
        messageConstruct("INFO", msg, logFile);
    }

    /**
     * INFO
     *
     * @param msg text
     */
    public static void info(String msg) {
        info(Thread.currentThread().getStackTrace()[2].getClassName() + " => " + msg, DEFAULT_LOG_NAME);
    }

    /**
     * EXCEPTION
     *
     * @param msg     text
     * @param logFile logfiles
     */
    public static void exception(String msg, String logFile) {
        messageConstruct("EXCEPTION", msg, logFile);
    }

    /**
     * EXCEPTION
     *
     * @param msg text
     */
    public static void exception(String msg) {
        exception(Thread.currentThread().getStackTrace()[2].getClassName() + " => " + msg, DEFAULT_LOG_NAME);
    }

    /**
     * WARNING
     *
     * @param msg     text
     * @param logFile logfiles
     */
    public static void warning(String msg, String logFile) {
        messageConstruct("WARNING", msg, logFile);
    }

    /**
     * WARNING
     *
     * @param msg text
     */
    public static void warning(String msg) {
        warning(Thread.currentThread().getStackTrace()[2].getClassName() + " => " + msg, DEFAULT_LOG_NAME);
    }
}
