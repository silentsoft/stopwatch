package org.silentsoft.stopwatch;

import java.text.NumberFormat;

public final class NumberFormatter {

    private static NumberFormat millisecondsFormat;
    private static NumberFormat secondsFormat;

    public static String milliseconds(double number) {
        if (millisecondsFormat == null) {
            millisecondsFormat = NumberFormat.getInstance();
        }

        return millisecondsFormat.format(number).concat("ms");
    }

    public static String seconds(double number) {
        if (secondsFormat == null) {
            secondsFormat = NumberFormat.getInstance();
            secondsFormat.setMinimumFractionDigits(3);
        }

        return secondsFormat.format(number).concat("s");
    }

}
