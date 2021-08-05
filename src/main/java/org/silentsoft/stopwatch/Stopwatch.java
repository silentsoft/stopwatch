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

    public void stop() {
        stop(null);
    }

    public void stop(String name) {
        synchronized (watchItems) {
            for (int i= watchItems.size()-1; i>=0; i--) {
                WatchItem watchItem = watchItems.get(i);
                if (name == null || watchItem.getName().equals(name)) {
                    if (watchItem.getEndEpochMilli() == null) {
                        watchItem.setEndEpochMilli(System.currentTimeMillis());
                        break;
                    }
                }
            }
        }
    }

    public long getTotalElapsedMilliseconds() {
        synchronized (watchItems) {
            return watchItems.stream().flatMapToLong(watchItem -> {
                if (watchItem.getEndEpochMilli() == null) {
                    return LongStream.of(0);
                }

                return LongStream.of(watchItem.getEndEpochMilli() - watchItem.getStartEpochMilli());
            }).sum();
        }
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
                maxLengthOfName = Math.max(maxLengthOfName, "name".length());

                int maxLengthOfPercentage = "    %".length();

                long totalElapsedMilliseconds = getTotalElapsedMilliseconds();
                int maxLengthOfElapsedMilliseconds = watchItems.stream().flatMapToInt(watchItem -> IntStream.of(watchItem.getEndEpochMilli() == null ? 0 : NumberFormatter.milliseconds(watchItem.getEndEpochMilli() - watchItem.getStartEpochMilli()).length())).max().getAsInt();
                maxLengthOfElapsedMilliseconds = Math.max(maxLengthOfElapsedMilliseconds, NumberFormatter.milliseconds(totalElapsedMilliseconds).length());

                double totalElapsedSeconds = getTotalElapsedSeconds();
                int maxLengthOfElapsedSeconds = watchItems.stream().flatMapToInt(watchItem -> IntStream.of(watchItem.getEndEpochMilli() == null ? 0 : NumberFormatter.seconds((watchItem.getEndEpochMilli() - watchItem.getStartEpochMilli()) / 1000.0).length())).max().getAsInt();
                maxLengthOfElapsedSeconds = Math.max(maxLengthOfElapsedSeconds, NumberFormatter.seconds(totalElapsedSeconds).length());

                String title = String.format("| %s | %s | %s | %s |\n", fillWithWhitespace("name", maxLengthOfName), "    %", fillWithWhitespace("ms", maxLengthOfElapsedMilliseconds), fillWithWhitespace("s", maxLengthOfElapsedSeconds));
                writer.write(title);

                String separator = String.format("%s\n", createSeparator(maxLengthOfName, maxLengthOfPercentage, maxLengthOfElapsedMilliseconds, maxLengthOfElapsedSeconds));
                writer.write(separator);

                while (watchItems.isEmpty() == false) {
                    WatchItem watchItem = watchItems.poll();
                    String name = watchItem.getName();
                    String percentage;
                    String elapsedMilliseconds;
                    String elapsedSeconds;
                    if (watchItem.getEndEpochMilli() != null) {
                        long elapsedMilli = watchItem.getEndEpochMilli() - watchItem.getStartEpochMilli();
                        percentage = NumberFormatter.percentage((elapsedMilli / (totalElapsedMilliseconds * 1.0)) * 100);
                        elapsedMilliseconds = NumberFormatter.milliseconds(elapsedMilli);
                        elapsedSeconds = NumberFormatter.seconds(elapsedMilli / 1000.0);
                    } else {
                        percentage = "";
                        elapsedMilliseconds = NOT_AVAILABLE;
                        elapsedSeconds = NOT_AVAILABLE;
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
