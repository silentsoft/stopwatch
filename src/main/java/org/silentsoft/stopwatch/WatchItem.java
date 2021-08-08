package org.silentsoft.stopwatch;

public class WatchItem {

    private String name;

    private long startEpochMilli;

    private Long endEpochMilli;

    private Long pauseEpochMilli, resumeEpochMilli;

    private long totalPausedMilli;

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
        setPauseEpochMilli(null);
        setResumeEpochMilli(null);
        setTotalPausedMilli(0);
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

    public Long getPauseEpochMilli() {
        return pauseEpochMilli;
    }

    public void setPauseEpochMilli(Long pauseEpochMilli) {
        this.pauseEpochMilli = pauseEpochMilli;
    }

    public Long getResumeEpochMilli() {
        return resumeEpochMilli;
    }

    public void setResumeEpochMilli(Long resumeEpochMilli) {
        this.resumeEpochMilli = resumeEpochMilli;
    }

    public long getTotalPausedMilli() {
        return totalPausedMilli;
    }

    public void setTotalPausedMilli(long totalPausedMilli) {
        this.totalPausedMilli = totalPausedMilli;
    }

    public Long getElapsedMilli() {
        if (getEndEpochMilli() == null) {
            return null;
        }

        return getEndEpochMilli() - getStartEpochMilli() - getTotalPausedMilli();
    }

    public void pause() {
        setPauseEpochMilli(System.currentTimeMillis());
    }

    public void resume() {
        setResumeEpochMilli(System.currentTimeMillis());

        setTotalPausedMilli(getTotalPausedMilli() + (getResumeEpochMilli() - getPauseEpochMilli()));
        setPauseEpochMilli(null);
        setResumeEpochMilli(null);
    }

    public void stop() {
        if (getPauseEpochMilli() != null && getResumeEpochMilli() == null) {
            resume();
        }

        setEndEpochMilli(System.currentTimeMillis());
    }

}
