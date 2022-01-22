package org.silentsoft.stopwatch;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.atomic.AtomicBoolean;

public class StopwatchTest {

    @Test
    public void stopwatchTest() {
        Stopwatch stopwatch = new Stopwatch();
        stopwatch.start("test-1");
        {
            WatchItem watchItem = stopwatch.watchItems.peekFirst();

            Assertions.assertEquals(1, stopwatch.watchItems.size());
            Assertions.assertNotNull(watchItem);
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
            Assertions.assertNull(watchItem.getEndEpochMilli());
            Assertions.assertEquals("test-2", watchItem.getName());

            stopwatch.stop();

            Assertions.assertEquals(2, stopwatch.watchItems.size());
            Assertions.assertNotNull(watchItem.getEndEpochMilli());
        }
    }

    @Test
    public void runnableTest() {
        Stopwatch stopwatch = new Stopwatch();
        AtomicBoolean launchFlag = new AtomicBoolean(false);
        stopwatch.start("test", () -> {
            launchFlag.set(true);
            try {
                Thread.sleep(1);

                stopwatch.pause();
                Thread.sleep(1);
                stopwatch.resume();
            } catch (InterruptedException e) {

            }
        });
        Assertions.assertTrue(launchFlag.get());
        Assertions.assertEquals(1, stopwatch.watchItems.size());
        Assertions.assertNotNull(stopwatch.watchItems.get(0).getEndEpochMilli());
        Assertions.assertNull(stopwatch.watchItems.get(0).getPauseEpochMilli());
        Assertions.assertNull(stopwatch.watchItems.get(0).getResumeEpochMilli());
        Assertions.assertTrue(stopwatch.watchItems.get(0).getTotalPausedMilli() > 0);
        Assertions.assertTrue(stopwatch.getTotalElapsedSeconds() > 0);
        Assertions.assertTrue(stopwatch.getTotalElapsedMilliseconds() > 0);
    }

    @Test
    public void getTotalElapsedTest() {
        Stopwatch stopwatch = new Stopwatch();
        {
            stopwatch.start("test-1");
            /* stopwatch.stop(); */
        }
        {
            WatchItem watchItem = new WatchItem("test-2", 0, 1000);
            stopwatch.watchItems.add(watchItem);
        }
        {
            WatchItem watchItem = new WatchItem("test-3", 0, 234);
            stopwatch.watchItems.add(watchItem);
        }
        {
            WatchItem watchItem = new WatchItem("test-4", 0, 567);
            watchItem.setTotalPausedMilli(567);
            stopwatch.watchItems.add(watchItem);
        }
        {
            WatchItem watchItem = new WatchItem("test-5", 0, 890);
            watchItem.setTotalPausedMilli(890);
            stopwatch.watchItems.add(watchItem);
        }
        {
            stopwatch.start("test-6");
            /* stopwatch.stop(); */
        }

        Assertions.assertEquals(1234, stopwatch.getTotalElapsedMilliseconds());
        Assertions.assertEquals(1.234, stopwatch.getTotalElapsedSeconds());
    }

    @Test
    public void printTest() {
        {
            Stopwatch stopwatch = new Stopwatch();
            stopwatch.start("test");

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            stopwatch.print(outputStream);

            StringBuilder builder = new StringBuilder();
            builder.append("|  name |    % |  ms |      s |\n");
            builder.append("|-------|------|-----|--------|\n");
            builder.append("|  test |      | N/A |    N/A |\n");
            builder.append("|       |      |     |        |\n");
            builder.append("| total | 100% | 0ms | 0.000s |\n");

            Assertions.assertEquals(builder.toString(), new String(outputStream.toByteArray()));
        }
        {
            Stopwatch stopwatch = new Stopwatch();
            stopwatch.add(new WatchItem("test", 0, 1000));

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            stopwatch.print(outputStream);

            StringBuilder builder = new StringBuilder();
            builder.append("|  name |    % |      ms |      s |\n");
            builder.append("|-------|------|---------|--------|\n");
            builder.append("|  test | 100% | 1,000ms | 1.000s |\n");
            builder.append("|       |      |         |        |\n");
            builder.append("| total | 100% | 1,000ms | 1.000s |\n");

            Assertions.assertEquals(builder.toString(), new String(outputStream.toByteArray()));
        }
        {
            Stopwatch stopwatch = new Stopwatch();
            {
                stopwatch.start("test-1");
                /* stopwatch.stop(); */
            }
            {
                WatchItem watchItem = new WatchItem("test-2", 0, 1);
                stopwatch.watchItems.add(watchItem);
            }
            {
                WatchItem watchItem = new WatchItem("test-3", 0, 10);
                stopwatch.watchItems.add(watchItem);
            }
            {
                WatchItem watchItem = new WatchItem("test-4", 0, 100);
                stopwatch.watchItems.add(watchItem);
            }
            {
                WatchItem watchItem = new WatchItem("test-5", 0, 1000);
                stopwatch.watchItems.add(watchItem);
            }
            {
                WatchItem watchItem = new WatchItem("test-6", 0, 1000);
                watchItem.setTotalPausedMilli(1000);
                stopwatch.watchItems.add(watchItem);
            }
            {
                stopwatch.start("test-7");
                /* stopwatch.stop(); */
            }

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            stopwatch.print(outputStream);

            StringBuilder builder = new StringBuilder();
            builder.append("|   name |     % |      ms |      s |\n");
            builder.append("|--------|-------|---------|--------|\n");
            builder.append("| test-1 |       |     N/A |    N/A |\n");
            builder.append("| test-2 |  0.1% |     1ms | 0.001s |\n");
            builder.append("| test-3 |  0.9% |    10ms | 0.010s |\n");
            builder.append("| test-4 |  9.0% |   100ms | 0.100s |\n");
            builder.append("| test-5 | 90.0% | 1,000ms | 1.000s |\n");
            builder.append("| test-6 |  0.0% |     0ms | 0.000s |\n");
            builder.append("| test-7 |       |     N/A |    N/A |\n");
            builder.append("|        |       |         |        |\n");
            builder.append("|  total |  100% | 1,111ms | 1.111s |\n");

            Assertions.assertEquals(builder.toString(), new String(outputStream.toByteArray()));
        }
    }

    @Test
    public void exceptionTest() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            new Stopwatch().start(null);
        });
        Assertions.assertThrows(NullPointerException.class, () -> {
            new Stopwatch().print(null);
        });
        Assertions.assertThrows(RuntimeException.class, () -> {
            Stopwatch stopwatch = new Stopwatch();
            stopwatch.start("");
            stopwatch.stop("");
            stopwatch.print(new OutputStream() {
                @Override
                public void write(int b) throws IOException {
                    throw new IOException();
                }
            });
        });
        Assertions.assertDoesNotThrow(() -> {
            new Stopwatch().stop();
            new Stopwatch().stop("");
            new Stopwatch().pause();
            new Stopwatch().pause("");
            new Stopwatch().resume();
            new Stopwatch().resume("");
            new Stopwatch().print();
            {
                Stopwatch stopwatch = new Stopwatch();
                stopwatch.start("test");
                stopwatch.start("test");
                stopwatch.stop();
                stopwatch.stop();
                stopwatch.stop();
            }
            {
                Stopwatch stopwatch = new Stopwatch();
                stopwatch.start("test");
                stopwatch.pause("test");
                stopwatch.pause("test");
            }
            {
                Stopwatch stopwatch = new Stopwatch();
                stopwatch.start("test");
                stopwatch.pause("test-1");
            }
            {
                Stopwatch stopwatch = new Stopwatch();
                stopwatch.start("test");
                stopwatch.pause("test");
                stopwatch.resume("test");
                stopwatch.resume("test");
            }
            {
                Stopwatch stopwatch = new Stopwatch();
                stopwatch.start("test");
                stopwatch.resume("test-1");
            }
        });
    }

}
