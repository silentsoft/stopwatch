package org.silentsoft.stopwatch;

import java.text.NumberFormat;

public class NumberFormatter {

    private static NumberFormat percentageFormat;
    private static NumberFormat millisecondsFormat;
    private static NumberFormat secondsFormat;

    public static String percentage(double number) {
        if (percentageFormat == null) {
            percentageFormat = NumberFormat.getInstance();
            percentageFormat.setMinimumFractionDigits(1);
            percentageFormat.setMaximumFractionDigits(1);
        }

        return (number == 100.0 ? "100" : percentageFormat.format(number)).concat("%");
    }

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
            secondsFormat.setMaximumFractionDigits(3);
        }

        return secondsFormat.format(number).concat("s");
    }

}
