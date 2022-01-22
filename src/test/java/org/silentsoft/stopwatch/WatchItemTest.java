package org.silentsoft.stopwatch;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

public class WatchItemTest {

    @Test
    public void constructorTest() {
        WatchItem watchItem = new WatchItem("test", 1000, 3000);
        Assertions.assertEquals("test", watchItem.getName());
        Assertions.assertEquals(1000, watchItem.getStartEpochMilli());
        Assertions.assertEquals(3000, watchItem.getEndEpochMilli());
        Assertions.assertNull(watchItem.getPauseEpochMilli());
        Assertions.assertNull(watchItem.getResumeEpochMilli());
        Assertions.assertEquals(0, watchItem.getTotalPausedMilli());
    }

    @Test
    public void getElapsedMilliTest() {
        WatchItem watchItem = new WatchItem("test", 1000, 3000);
        Assertions.assertEquals(2000, watchItem.getElapsedMilli());
    }

    @Test
    public void stopTest() throws Exception {
        {
            WatchItem watchItem = new WatchItem("test");
            pause(watchItem);
            watchItem.resume();
            watchItem.stop();
            Assertions.assertTrue(watchItem.getTotalPausedMilli() > 0);
            Assertions.assertTrue(watchItem.getEndEpochMilli() > 0);
        }
        {
            WatchItem watchItem = new WatchItem("test");
            pause(watchItem);
            watchItem.stop();
            Assertions.assertTrue(watchItem.getTotalPausedMilli() > 0);
            Assertions.assertTrue(watchItem.getEndEpochMilli() > 0);
        }
    }

    private void pause(WatchItem watchItem) throws Exception {
        Field pauseEpochMilliField = watchItem.getClass().getDeclaredField("pauseEpochMilli");
        pauseEpochMilliField.setAccessible(true);
        pauseEpochMilliField.set(watchItem, 1000L);
    }

}
