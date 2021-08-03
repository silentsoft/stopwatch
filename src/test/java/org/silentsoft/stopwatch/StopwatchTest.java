package org.silentsoft.stopwatch;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;

public class StopwatchTest {

    @Test
    public void stopwatchTest() {
        Stopwatch stopwatch = new Stopwatch();
        stopwatch.start("test-1");
        {
            WatchItem watchItem = stopwatch.watchItems.peekFirst();

            Assertions.assertEquals(1, stopwatch.watchItems.size());
            Assertions.assertNotNull(watchItem);
            Assertions.assertNotNull(watchItem.getStartEpochMilli());
            Assertions.assertNull(watchItem.getEndEpochMilli());
            Assertions.assertEquals("test-1", watchItem.getName());

            stopwatch.stop("none");

            Assertions.assertEquals(1, stopwatch.watchItems.size());
            Assertions.assertNull(watchItem.getEndEpochMilli());

            stopwatch.stop("test-1");

            Assertions.assertEquals(1, stopwatch.watchItems.size());
            Assertions.assertNotNull(watchItem.getEndEpochMilli());
        }
        stopwatch.start("test-2");
        {
            WatchItem watchItem = stopwatch.watchItems.peekLast();

            Assertions.assertEquals(2, stopwatch.watchItems.size());
            Assertions.assertNotNull(watchItem);
            Assertions.assertNotNull(watchItem.getStartEpochMilli());
            Assertions.assertNull(watchItem.getEndEpochMilli());
            Assertions.assertEquals("test-2", watchItem.getName());

            stopwatch.stop();

            Assertions.assertEquals(2, stopwatch.watchItems.size());
            Assertions.assertNotNull(watchItem.getEndEpochMilli());
        }
    }

    @Test
    public void printTest() {
        Stopwatch stopwatch = new Stopwatch();
        {
            stopwatch.start("test-1");
            /* stopwatch.stop(); */
        }
        {
            WatchItem watchItem = new WatchItem("test-2", 0);
            watchItem.setEndEpochMilli(1L);
            stopwatch.watchItems.add(watchItem);
        }
        {
            WatchItem watchItem = new WatchItem("test-3", 0);
            watchItem.setEndEpochMilli(10L);
            stopwatch.watchItems.add(watchItem);
        }
        {
            WatchItem watchItem = new WatchItem("test-4", 0);
            watchItem.setEndEpochMilli(100L);
            stopwatch.watchItems.add(watchItem);
        }
        {
            WatchItem watchItem = new WatchItem("test-5", 0);
            watchItem.setEndEpochMilli(1000L);
            stopwatch.watchItems.add(watchItem);
        }
        {
            stopwatch.start("test-6");
            /* stopwatch.stop(); */
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        stopwatch.print(outputStream);

        StringBuilder builder = new StringBuilder();
        builder.append("|   name | elapsed(ms) | elapsed(s) |\n");
        builder.append("|--------|-------------|------------|\n");
        builder.append("| test-1 |         N/A |        N/A |\n");
        builder.append("| test-2 |         1ms |     0.001s |\n");
        builder.append("| test-3 |        10ms |     0.010s |\n");
        builder.append("| test-4 |       100ms |     0.100s |\n");
        builder.append("| test-5 |     1,000ms |     1.000s |\n");
        builder.append("| test-6 |         N/A |        N/A |\n");

        Assertions.assertEquals(builder.toString(), new String(outputStream.toByteArray()));
    }

    @Test
    public void exceptionTest() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            new Stopwatch().start(null);
        });
        Assertions.assertThrows(NullPointerException.class, () -> {
            new Stopwatch().print(null);
        });
        Assertions.assertDoesNotThrow(() -> {
            new Stopwatch().stop();
            new Stopwatch().stop(null);
            new Stopwatch().print();
        });
    }

}
