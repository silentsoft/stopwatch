package org.silentsoft.stopwatch;

import java.text.NumberFormat;

/**
 * This class is used to formatting percentage, milliseconds and seconds into human-readable format.</p>
 * <p>Examples:</p>
 * <ul>
 *   <li>{@code NumberFormatter.percentage(0)} returns {@code 0.0%}</li>
 *   <li>{@code NumberFormatter.percentage(100)} returns {@code 100%}</li>
 *   <li>{@code NumberFormatter.milliseconds(100)} returns {@code 100ms}</li>
 *   <li>{@code NumberFormatter.seconds(0)} returns {@code 0.000s}</li>
 *   <li>{@code NumberFormatter.seconds(0.83)} returns {@code 0.830s}</li>
 * </ul>
 * 
 * @see #percentage(double)
 * @see #milliseconds(double)
 * @see #seconds(double) 
 */
public class NumberFormatter {

    private NumberFormatter() { }

    private static NumberFormat percentageFormat;
    private static NumberFormat millisecondsFormat;
    private static NumberFormat secondsFormat;

    /**
     * Returns the given {@code number} as a human-readable string with a percentage sign.</p>
     * Note that the minimum fraction digits and maximum fraction digits are always set to {@code 1} except for {@code 100%}.
     * <p>Examples:</p>
     * <ul>
     *   <li>{@code NumberFormatter.percentage(0)} returns {@code 0.0%}</li>
     *   <li>{@code NumberFormatter.percentage(100)} returns {@code 100%}</li>
     * </ul>
     *
     * @param number the number to be formatted
     * @return a formatted string
     */
    public static String percentage(double number) {
        if (percentageFormat == null) {
            percentageFormat = NumberFormat.getInstance();
            percentageFormat.setMinimumFractionDigits(1);
            percentageFormat.setMaximumFractionDigits(1);
        }

        return (number == 100.0 ? "100" : percentageFormat.format(number)).concat("%");
    }

    /**
     * Returns the given {@code number} as a human-readable string with a {@code ms} suffix.</p>
     * <p>Examples:</p>
     * <ul>
     *   <li>{@code NumberFormatter.milliseconds(0)} returns {@code 0ms}</li>
     *   <li>{@code NumberFormatter.milliseconds(100)} returns {@code 100ms}</li>
     * </ul>
     *
     * @param number the number to be formatted
     * @return a formatted string
     */
    public static String milliseconds(double number) {
        if (millisecondsFormat == null) {
            millisecondsFormat = NumberFormat.getInstance();
        }

        return millisecondsFormat.format(number).concat("ms");
    }

    /**
     * Returns the given {@code number} as a human-readable string with a {@code s} suffix.</p>
     * Note that the minimum fraction digits and maximum fraction digits are always set to {@code 3}.
     * <p>Examples:</p>
     * <ul>
     *   <li>{@code NumberFormatter.seconds(0)} returns {@code 0.000s}</li>
     *   <li>{@code NumberFormatter.seconds(0.83)} returns {@code 0.830s}</li>
     * </ul>
     *
     * @param number the number to be formatted
     * @return a formatted string
     */
    public static String seconds(double number) {
        if (secondsFormat == null) {
            secondsFormat = NumberFormat.getInstance();
            secondsFormat.setMinimumFractionDigits(3);
            secondsFormat.setMaximumFractionDigits(3);
        }

        return secondsFormat.format(number).concat("s");
    }

}
