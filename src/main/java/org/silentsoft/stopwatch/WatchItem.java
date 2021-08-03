package org.silentsoft.stopwatch;

public class WatchItem {

    private String name;

    private Long startEpochMilli, endEpochMilli;

    private WatchItem() {

    }

    public WatchItem(String name) {
        this(name, System.currentTimeMillis());
    }

    public WatchItem(String name, long startEpochMilli) {
        this.name = name;
        this.startEpochMilli = startEpochMilli;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getStartEpochMilli() {
        return startEpochMilli;
    }

    public void setStartEpochMilli(Long startEpochMilli) {
        this.startEpochMilli = startEpochMilli;
    }

    public Long getEndEpochMilli() {
        return endEpochMilli;
    }

    public void setEndEpochMilli(Long endEpochMilli) {
        this.endEpochMilli = endEpochMilli;
    }

}
