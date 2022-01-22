package org.silentsoft.stopwatch;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.LinkedList;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

/**
 * This class is used to measure the time of execution between the {@code start} and {@code stop} methods.
 * Also, it can be paused and resumed at any time so that exclusive time can be measured.</p>
 * The example below shows how to use this stopwatch to measure the time.
 * <pre>
 *   Stopwatch stopwatch = new Stopwatch();
 *
 *   stopwatch.start("initialization");
 *   // ...
 *   stopwatch.stop();
 *
 *   stopwatch.start("processing");
 *   // ...
 *   stopwatch.stop();
 *
 *   stopwatch.start("rendering");
 *   // ...
 *   stopwatch.stop();
 *
 *   stopwatch.print();
 * </pre>
 * and the output is as follows:
 * <pre>
 *   |           name |     % |      ms |      s |
 *   |----------------|-------|---------|--------|
 *   | initialization | 59.5% | 1,234ms | 1.234s |
 *   |     processing | 40.0% |   830ms | 0.830s |
 *   |      rendering |  0.5% |    10ms | 0.010s |
 *   |                |       |         |        |
 *   |          total |  100% | 2,074ms | 2.074s |
 * </pre>
 *
 * @see #start(String)
 * @see #start(String, Runnable)
 * @see #pause()
 * @see #pause(String)
 * @see #resume()
 * @see #resume(String)
 * @see #stop()
 * @see #stop(String)
 * @see #print()
 * @see #print(OutputStream) 
 */
public class Stopwatch {

    protected static final String NOT_AVAILABLE = "N/A";

    protected LinkedList<WatchItem> watchItems = new LinkedList<>();

    /**
     * Starts measuring time immediately with the given {@code name}.
     *
     * @param name the name of the task to start
     * @throws NullPointerException if the name is {@code null}
     * @see #start(String, Runnable)
     */
    public void start(String name) throws NullPointerException {
        add(new WatchItem(name));
    }

    /**
     * Starts measuring time immediately with the given {@code name} and {@code runnable}.</p>
     * Note that after execute the {@code runnable}, the task will be considered as finished.
     *
     * @param name the name of the task to start
     * @param runnable the runnable used to measure the elapsed time
     * @throws NullPointerException if the name is {@code null}
     * @see #start(String)
     */
    public void start(String name, Runnable runnable) throws NullPointerException {
        WatchItem watchItem = new WatchItem(name);
        add(watchItem);

        runnable.run();

        watchItem.stop();
    }

    /**
     * Adds the given {@code watchItem} to the inner list directly.
     *
     * @param watchItem the watch item containing elapsed time information
     * @throws NullPointerException if the name of the watch item is {@code null}
     */
    public void add(WatchItem watchItem) throws NullPointerException {
        if (watchItem.getName() == null) {
            throw new NullPointerException("Name cannot be null.");
        }

        synchronized (watchItems) {
            watchItems.add(watchItem);
        }
    }

    /**
     * Pauses the last started task.
     *
     * @see #pause(String)
     */
    public void pause() {
        pause(null);
    }

    /**
     * Pauses the last started task matching the given {@code name}. If the {@code name} is {@code null}, the last started task is paused.</p>
     * Note that this method does not pause all tasks started with the same name. It only pauses the last started task.
     *
     * @param name the name of the task to pause
     * @see #pause()
     */
    public void pause(String name) {
        synchronized (watchItems) {
            for (int i= watchItems.size()-1; i>=0; i--) {
                WatchItem watchItem = watchItems.get(i);
                if (name == null || watchItem.getName().equals(name)) {
                    if (watchItem.pause()) {
                        break;
                    }
                }
            }
        }
    }

    /**
     * Resumes the last paused task.
     *
     * @see #resume(String)
     */
    public void resume() {
        resume(null);
    }

    /**
     * Resumes the last paused task matching the given {@code name}. If the {@code name} is {@code null}, the last paused task is resumed.</p>
     * Note that this method does not resume all tasks paused with the same name. It only resumes the last paused task.
     *
     * @param name the name of the task to resume
     * @see #resume()
     */
    public void resume(String name) {
        synchronized (watchItems) {
            for (int i = watchItems.size() - 1; i >= 0; i--) {
                WatchItem watchItem = watchItems.get(i);
                if (name == null || watchItem.getName().equals(name)) {
                    if (watchItem.resume()) {
                        break;
                    }
                }
            }
        }
    }

    /**
     * Stops the last started task.
     *
     * @see #stop(String)
     */
    public void stop() {
        stop(null);
    }

    /**
     * Stops the last started task matching the given {@code name}. If the {@code name} is {@code null}, the last started task is stopped.</p>
     * Note that this method does not stop all tasks started with the same name. It only stops the last started task.
     *
     * @param name the name of the task to stop
     * @see #stop()
     */
    public void stop(String name) {
        synchronized (watchItems) {
            for (int i= watchItems.size()-1; i>=0; i--) {
                WatchItem watchItem = watchItems.get(i);
                if (name == null || watchItem.getName().equals(name)) {
                    if (watchItem.stop()) {
                        break;
                    }
                }
            }
        }
    }

    /**
     * Returns the total elapsed time of all tasks in this stopwatch in milliseconds excluding any paused time.
     *
     * @return the total elapsed time in milliseconds
     * @see #getTotalElapsedSeconds()
     */
    public long getTotalElapsedMilliseconds() {
        Long totalElapsedMilliseconds;
        synchronized (watchItems) {
            totalElapsedMilliseconds = watchItems.stream().flatMapToLong(watchItem -> LongStream.of(watchItem.getElapsedMilli() != null ? watchItem.getElapsedMilli() : 0)).sum();
        }
        return totalElapsedMilliseconds;
    }

    /**
     * Returns the total elapsed time of all tasks in this stopwatch in seconds excluding any paused time.
     *
     * @return the total elapsed time in seconds
     * @see #getTotalElapsedMilliseconds()
     */
    public double getTotalElapsedSeconds() {
        return getTotalElapsedMilliseconds() / 1000.0;
    }

    /**
     * Prints the result of the stopwatch as a table to the default output stream.</p>
     * the following table format is used:
     * <pre>
     *   |           name |     % |      ms |      s |
     *   |----------------|-------|---------|--------|
     *   | initialization | 59.5% | 1,234ms | 1.234s |
     *   |     processing | 40.0% |   830ms | 0.830s |
     *   |      rendering |  0.5% |    10ms | 0.010s |
     *   |                |       |         |        |
     *   |          total |  100% | 2,074ms | 2.074s |
     * </pre>
     * Note that all tasks are cleared after printing.
     *
     * @see #print(OutputStream)
     * @see System#out
     */
    public void print() {
        print(System.out);
    }

    /**
     * Prints the result of the stopwatch as a table to the given output stream.</p>
     * the following table format is used:
     * <pre>
     *   |           name |     % |      ms |      s |
     *   |----------------|-------|---------|--------|
     *   | initialization | 59.5% | 1,234ms | 1.234s |
     *   |     processing | 40.0% |   830ms | 0.830s |
     *   |      rendering |  0.5% |    10ms | 0.010s |
     *   |                |       |         |        |
     *   |          total |  100% | 2,074ms | 2.074s |
     * </pre>
     * Note that all tasks are cleared after printing.
     *
     * @param outputStream the output stream to print
     * @throws NullPointerException if the output stream is null
     * @throws RuntimeException if the output stream cannot be written
     */
    public void print(OutputStream outputStream) {
        if (outputStream == null) {
            throw new NullPointerException("Output stream cannot be null.");
        }

        synchronized (watchItems) {
            if (watchItems.isEmpty()) {
                return;
            }

            try (OutputStreamWriter writer = new OutputStreamWriter(outputStream)) {
                int maxLengthOfName = watchItems.stream().flatMapToInt(watchItem -> IntStream.of(watchItem.getName().length())).max().getAsInt();
                maxLengthOfName = Math.max(maxLengthOfName, Math.max("name".length(), "total".length()));

                long totalElapsedMilliseconds = getTotalElapsedMilliseconds();

                int maxLengthOfPercentage = watchItems.stream().flatMapToInt(watchItem -> IntStream.of(watchItem.getElapsedMilli() == null ? 0 : NumberFormatter.percentage((watchItem.getElapsedMilli() / (totalElapsedMilliseconds * 1.0)) * 100).length())).max().getAsInt();
                maxLengthOfPercentage = Math.max(maxLengthOfPercentage, "100%".length());

                int maxLengthOfElapsedMilliseconds = watchItems.stream().flatMapToInt(watchItem -> IntStream.of(watchItem.getElapsedMilli() == null ? 0 : NumberFormatter.milliseconds(watchItem.getElapsedMilli()).length())).max().getAsInt();
                maxLengthOfElapsedMilliseconds = Math.max(maxLengthOfElapsedMilliseconds, Math.max(NOT_AVAILABLE.length(), NumberFormatter.milliseconds(totalElapsedMilliseconds).length()));

                double totalElapsedSeconds = getTotalElapsedSeconds();
                int maxLengthOfElapsedSeconds = watchItems.stream().flatMapToInt(watchItem -> IntStream.of(watchItem.getElapsedMilli() == null ? 0 : NumberFormatter.seconds(watchItem.getElapsedMilli() / 1000.0).length())).max().getAsInt();
                maxLengthOfElapsedSeconds = Math.max(maxLengthOfElapsedSeconds, Math.max(NOT_AVAILABLE.length(), NumberFormatter.seconds(totalElapsedSeconds).length()));

                String title = String.format("| %s | %s | %s | %s |\n", fillWithWhitespace("name", maxLengthOfName), fillWithWhitespace("%", maxLengthOfPercentage), fillWithWhitespace("ms", maxLengthOfElapsedMilliseconds), fillWithWhitespace("s", maxLengthOfElapsedSeconds));
                writer.write(title);

                String separator = String.format("%s\n", createSeparator(maxLengthOfName, maxLengthOfPercentage, maxLengthOfElapsedMilliseconds, maxLengthOfElapsedSeconds));
                writer.write(separator);

                while (watchItems.isEmpty() == false) {
                    WatchItem watchItem = watchItems.poll();
                    String name = watchItem.getName();

                    String percentage;
                    String elapsedMilliseconds;
                    String elapsedSeconds;

                    Long elapsedMilli = watchItem.getElapsedMilli();
                    if (elapsedMilli == null) {
                        percentage = "";
                        elapsedMilliseconds = NOT_AVAILABLE;
                        elapsedSeconds = NOT_AVAILABLE;
                    } else {
                        percentage = NumberFormatter.percentage((elapsedMilli / (totalElapsedMilliseconds * 1.0)) * 100);
                        elapsedMilliseconds = NumberFormatter.milliseconds(elapsedMilli);
                        elapsedSeconds = NumberFormatter.seconds(elapsedMilli / 1000.0);
                    }

                    writer.write(String.format("| %s | %s | %s | %s |\n", fillWithWhitespace(name, maxLengthOfName), fillWithWhitespace(percentage, maxLengthOfPercentage), fillWithWhitespace(elapsedMilliseconds, maxLengthOfElapsedMilliseconds), fillWithWhitespace(elapsedSeconds, maxLengthOfElapsedSeconds)));
                }

                String emptyRow = String.format("%s\n", createEmptyRow(maxLengthOfName, maxLengthOfPercentage, maxLengthOfElapsedMilliseconds, maxLengthOfElapsedSeconds));
                writer.write(emptyRow);

                String total = String.format("| %s | %s | %s | %s |\n", fillWithWhitespace("total", maxLengthOfName), fillWithWhitespace("100%", maxLengthOfPercentage), fillWithWhitespace(NumberFormatter.milliseconds(totalElapsedMilliseconds), maxLengthOfElapsedMilliseconds), fillWithWhitespace(NumberFormatter.seconds(totalElapsedSeconds), maxLengthOfElapsedSeconds));
                writer.write(total);

                writer.flush();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Returns the value with whitespace filled in to the left
     *
     * @param value the value to fill with whitespace
     * @param max the maximum length of the value
     * @return a string representation of the value with whitespace filled in to the left
     */
    protected String fillWithWhitespace(String value, int max) {
        int count = max - value.length();
        for (int i=0; i<count; i++) {
            value = " ".concat(value);
        }
        return value;
    }

    /**
     * Returns separator with the dashes in each column.
     *
     * @param lengthOfColumns the length of each column
     * @return a string which is used to separator for the table header
     */
    protected String createSeparator(int... lengthOfColumns) {
        String separator = "|-";
        for (int i=0, j=lengthOfColumns.length; i<j; i++) {
            int lengthOfColumn = lengthOfColumns[i];
            for (int k=0; k<lengthOfColumn; k++) {
                separator = separator.concat("-");
            }
            if (i < j-1) {
                separator = separator.concat("-|-");
            }
        }
        separator = separator.concat("-|");

        return separator;
    }

    /**
     * Returns empty row with the whitespaces in each column.
     *
     * @param lengthOfColumns the length of each column
     * @return a string which is used to create an empty row
     */
    protected String createEmptyRow(int... lengthOfColumns) {
        return createSeparator(lengthOfColumns).replaceAll("-", " ");
    }

}
