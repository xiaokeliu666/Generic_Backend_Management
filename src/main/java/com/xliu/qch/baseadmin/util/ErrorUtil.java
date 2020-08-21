package com.xliu.qch.baseadmin.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 *
 * Catch error log
 */
public class ErrorUtil {

    /**
     * Print stack error
     */
    public static String errorInfoToString(Throwable e) {
        StringWriter sw = null;
        PrintWriter pw = null;
        try {
            sw = new StringWriter();
            pw = new PrintWriter(sw);
            // output stack error into printWriter
            e.printStackTrace(pw);
            pw.flush();
            sw.flush();
        } finally {
            if (sw != null) {
                try {
                    sw.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (pw != null) {
                pw.close();
            }
        }
        return sw.toString();
    }
}