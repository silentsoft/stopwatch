package org.silentsoft.stopwatch;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.LinkedList;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

public class Stopwatch {

    protected static final String NOT_AVAILABLE = "N/A";

    protected LinkedList<WatchItem> watchItems = new LinkedList<>();

    public void start(String name) throws NullPointerException {
        add(new WatchItem(name));
    }

    public void add(WatchItem watchItem) throws NullPointerException {
        if (watchItem.getName() == null) {
            throw new NullPointerException("Name cannot be null.");
        }

        synchronized (watchItems) {
            watchItems.add(watchItem);
        }
    }

    public void pause() {
        pause(null);
    }

    public void pause(String name) {
        synchronized (watchItems) {
            for (int i= watchItems.size()-1; i>=0; i--) {
                WatchItem watchItem = watchItems.get(i);
                if (name == null || watchItem.getName().equals(name)) {
                    if (watchItem.getPauseEpochMilli() == null) {
                        watchItem.pause();

                        break;
                    }
                }
            }
        }
    }

    public void resume() {
        resume(null);
    }

    public void resume(String name) {
        synchronized (watchItems) {
            for (int i = watchItems.size() - 1; i >= 0; i--) {
                WatchItem watchItem = watchItems.get(i);
                if (name == null || watchItem.getName().equals(name)) {
                    if (watchItem.getPauseEpochMilli() != null && watchItem.getResumeEpochMilli() == null) {
                        watchItem.resume();

                        break;
                    }
                }
            }
        }
    }

    public void stop() {
        stop(null);
    }

    public void stop(String name) {
        synchronized (watchItems) {
            for (int i= watchItems.size()-1; i>=0; i--) {
                WatchItem watchItem = watchItems.get(i);
                if (name == null || watchItem.getName().equals(name)) {
                    if (watchItem.getEndEpochMilli() == null) {
                        watchItem.stop();

                        break;
                    }
                }
            }
        }
    }

    public long getTotalElapsedMilliseconds() {
        Long totalElapsedMilliseconds;
        synchronized (watchItems) {
            totalElapsedMilliseconds = watchItems.stream().flatMapToLong(watchItem -> LongStream.of(watchItem.getElapsedMilli() != null ? watchItem.getElapsedMilli() : 0)).sum();
        }
        return totalElapsedMilliseconds;
    }

    public double getTotalElapsedSeconds() {
        return getTotalElapsedMilliseconds() / 1000.0;
    }

    public void print() {
        print(System.out);
    }

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

    protected String fillWithWhitespace(String value, int max) {
        int count = max - value.length();
        for (int i=0; i<count; i++) {
            value = " ".concat(value);
        }
        return value;
    }

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

    protected String createEmptyRow(int... lengthOfColumns) {
        return createSeparator(lengthOfColumns).replaceAll("-", " ");
    }

}
