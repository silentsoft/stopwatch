package org.silentsoft.stopwatch;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class NumberFormatterTest {

    @Test
    public void percentageTest() {
        Assertions.assertEquals("0.0%", NumberFormatter.percentage(0));
        Assertions.assertEquals("49.1%", NumberFormatter.percentage(49.1));
        Assertions.assertEquals("50.0%", NumberFormatter.percentage(50));
        Assertions.assertEquals("51.9%", NumberFormatter.percentage(51.9));
        Assertions.assertEquals("100%", NumberFormatter.percentage(100));
    }

    @Test
    public void millisecondsTest() {
        Assertions.assertEquals("0ms", NumberFormatter.milliseconds(0));
        Assertions.assertEquals("100ms", NumberFormatter.milliseconds(100));
    }

    @Test
    public void secondsTest() {
        Assertions.assertEquals("0.000s", NumberFormatter.seconds(0));
        Assertions.assertEquals("0.830s", NumberFormatter.seconds(0.83));
        Assertions.assertEquals("1.000s", NumberFormatter.seconds(1));
    }

}
