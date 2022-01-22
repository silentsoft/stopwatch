package org.silentsoft.stopwatch;

/**
 * This class is used to measure and store information about elapsed time.
 */
public class WatchItem {

    private String name;

    private long startEpochMilli;

    private Long endEpochMilli;

    private Long pauseEpochMilli, resumeEpochMilli;

    private long totalPausedMilli;

    /**
     * Creates a new {@link WatchItem} with the given {@code name}.</p>
     * Note that the start time of the task will be set to the current time.
     *
     * @param name the name of the task
     * @see #WatchItem(String, long)
     * @see #WatchItem(String, long, long)
     * @see #WatchItem(String, long, Long)
     */
    public WatchItem(String name) {
        this(name, System.currentTimeMillis());
    }

    /**
     * Creates a new {@link WatchItem} with the given {@code name} and {@code startEpochMilli}.
     *
     * @param name the name of the task
     * @param startEpochMilli a start time of the task in milliseconds
     * @see #WatchItem(String, long, long)
     * @see #WatchItem(String, long, Long)
     */
    public WatchItem(String name, long startEpochMilli) {
        this(name, startEpochMilli, null);
    }

    /**
     * Creates a new {@link WatchItem} with the given {@code name}, {@code startEpochMilli} and {@code endEpochMilli}.
     *
     * @param name the name of the task
     * @param startEpochMilli a start time of the task in milliseconds
     * @param endEpochMilli an end time of the task in milliseconds
     * @see #WatchItem(String, long, Long)
     */
    public WatchItem(String name, long startEpochMilli, long endEpochMilli) {
        this(name, startEpochMilli, Long.valueOf(endEpochMilli));
    }

    /**
     * Creates a new {@link WatchItem} with the given {@code name}, {@code startEpochMilli} and {@code endEpochMilli}.</p>
     * Note that if the {@code endEpochMilli} is null, the {@link WatchItem} will be considered as a started task. Otherwise, considered as a finished task.
     *
     * @param name the name of the task
     * @param startEpochMilli a start time of the task in milliseconds
     * @param endEpochMilli an end time of the task in milliseconds
     */
    public WatchItem(String name, long startEpochMilli, Long endEpochMilli) {
        setName(name);
        setStartEpochMilli(startEpochMilli);
        setEndEpochMilli(endEpochMilli);
        setPauseEpochMilli(null);
        setResumeEpochMilli(null);
        setTotalPausedMilli(0);
    }

    /**
     * Returns the name of this task.
     *
     * @return the task name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of this task.
     *
     * @param name the task name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the start time of this task in milliseconds.
     *
     * @return the epoch time in milliseconds
     */
    public long getStartEpochMilli() {
        return startEpochMilli;
    }

    /**
     * Sets the start time of this task in milliseconds.
     *
     * @param startEpochMilli the epoch time in milliseconds to set
     */
    public void setStartEpochMilli(long startEpochMilli) {
        this.startEpochMilli = startEpochMilli;
    }

    /**
     * Returns the end time of this task in milliseconds.
     *
     * @return the epoch time in milliseconds
     */
    public Long getEndEpochMilli() {
        return endEpochMilli;
    }

    /**
     * Sets the end time of this task in milliseconds.
     *
     * @param endEpochMilli the epoch time in milliseconds to set
     */
    public void setEndEpochMilli(Long endEpochMilli) {
        this.endEpochMilli = endEpochMilli;
    }

    /**
     * Returns the pause time of this task in milliseconds.
     *
     * @return the epoch time in milliseconds
     */
    public Long getPauseEpochMilli() {
        return pauseEpochMilli;
    }

    /**
     * Sets the pause time of this task in milliseconds.
     *
     * @param pauseEpochMilli the epoch time in milliseconds to set
     */
    public void setPauseEpochMilli(Long pauseEpochMilli) {
        this.pauseEpochMilli = pauseEpochMilli;
    }

    /**
     * Returns the resume time of this task in milliseconds.
     *
     * @return the epoch time in milliseconds
     */
    public Long getResumeEpochMilli() {
        return resumeEpochMilli;
    }

    /**
     * Sets the resume time of this task in milliseconds.
     *
     * @param resumeEpochMilli the epoch time in milliseconds to set
     */
    public void setResumeEpochMilli(Long resumeEpochMilli) {
        this.resumeEpochMilli = resumeEpochMilli;
    }

    /**
     * Returns the total paused time of this task in milliseconds.
     *
     * @return the total paused time in milliseconds
     */
    public long getTotalPausedMilli() {
        return totalPausedMilli;
    }

    /**
     * Sets the total paused time of this task in milliseconds.
     *
     * @param totalPausedMilli the total paused time in milliseconds to set
     */
    public void setTotalPausedMilli(long totalPausedMilli) {
        this.totalPausedMilli = totalPausedMilli;
    }

    /**
     * Returns the elapsed time of this task in milliseconds excluding any paused time.
     *
     * @return the elapsed time in milliseconds
     */
    public Long getElapsedMilli() {
        if (getEndEpochMilli() == null) {
            return null;
        }

        return getEndEpochMilli() - getStartEpochMilli() - getTotalPausedMilli();
    }

    /**
     * Pauses this task if it is not paused.
     *
     * @return {@code true} if this task is currently paused. Otherwise, returns {@code false}.
     */
    public boolean pause() {
        if (getPauseEpochMilli() == null) {
            setPauseEpochMilli(System.currentTimeMillis());

            return true;
        }

        return false;
    }

    /**
     * Resumes this task if it is paused.
     *
     * @return {@code true} if this task is currently resumed. Otherwise, returns {@code false}.
     */
    public boolean resume() {
        if (getPauseEpochMilli() != null) {
            setResumeEpochMilli(System.currentTimeMillis());

            setTotalPausedMilli(getTotalPausedMilli() + (getResumeEpochMilli() - getPauseEpochMilli()));
            setPauseEpochMilli(null);
            setResumeEpochMilli(null);

            return true;
        }

        return false;
    }

    /**
     * Stops this task if it is not stopped.</p>
     * Note that if this task is paused, it will be resumed before stopping to calculate the elapsed time.
     *
     * @return {@code true} if this task is currently stopped. Otherwise, returns {@code false}.
     */
    public boolean stop() {
        if (getEndEpochMilli() == null) {
            resume();

            setEndEpochMilli(System.currentTimeMillis());

            return true;
        }

        return false;
    }

}
