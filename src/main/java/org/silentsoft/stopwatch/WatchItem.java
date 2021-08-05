package org.silentsoft.stopwatch;

public class WatchItem {

    private String name;

    private long startEpochMilli;

    private Long endEpochMilli;

    public WatchItem(String name) {
        this(name, System.currentTimeMillis());
    }

    public WatchItem(String name, long startEpochMilli) {
        this(name, startEpochMilli, null);
    }

    public WatchItem(String name, long startEpochMilli, long endEpochMilli) {
        this(name, startEpochMilli, Long.valueOf(endEpochMilli));
    }

    public WatchItem(String name, long startEpochMilli, Long endEpochMilli) {
        setName(name);
        setStartEpochMilli(startEpochMilli);
        setEndEpochMilli(endEpochMilli);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getStartEpochMilli() {
        return startEpochMilli;
    }

    public void setStartEpochMilli(long startEpochMilli) {
        this.startEpochMilli = startEpochMilli;
    }

    public Long getEndEpochMilli() {
        return endEpochMilli;
    }

    public void setEndEpochMilli(Long endEpochMilli) {
        this.endEpochMilli = endEpochMilli;
    }

}
