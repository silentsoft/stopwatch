package org.silentsoft.stopwatch;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.LinkedList;
import java.util.stream.IntStream;

public class Stopwatch {

    protected static final String NOT_AVAILABLE = "N/A";

    protected LinkedList<WatchItem> watchItems = new LinkedList<>();

    public void start(String name) throws NullPointerException {
        if (name == null) {
            throw new NullPointerException("Name cannot be null.");
        }

        synchronized (watchItems) {
            watchItems.add(new WatchItem(name));
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

                int maxLengthOfElapsedMilliseconds = watchItems.stream().flatMapToInt(watchItem -> IntStream.of(watchItem.getEndEpochMilli() == null ? 0 : NumberFormatter.milliseconds(watchItem.getEndEpochMilli() - watchItem.getStartEpochMilli()).length())).max().getAsInt();
                maxLengthOfElapsedMilliseconds = Math.max(maxLengthOfElapsedMilliseconds, "elapsed(ms)".length());

                int maxLengthOfElapsedSeconds = watchItems.stream().flatMapToInt(watchItem -> IntStream.of(watchItem.getEndEpochMilli() == null ? 0 : NumberFormatter.seconds((watchItem.getEndEpochMilli() - watchItem.getStartEpochMilli()) / 1000.0).length())).max().getAsInt();
                maxLengthOfElapsedSeconds = Math.max(maxLengthOfElapsedSeconds, "elapsed(s)".length());

                String title = String.format("| %s | %s | %s |\n", fillWithWhitespace("name", maxLengthOfName), fillWithWhitespace("elapsed(ms)", maxLengthOfElapsedMilliseconds), fillWithWhitespace("elapsed(s)", maxLengthOfElapsedSeconds));
                writer.write(title);

                String separator = String.format("%s\n", createSeparator(maxLengthOfName, maxLengthOfElapsedMilliseconds, maxLengthOfElapsedSeconds));
                writer.write(separator);

                while (watchItems.isEmpty() == false) {
                    WatchItem watchItem = watchItems.poll();
                    String name = watchItem.getName();
                    String elapsedMilli;
                    String elapsedSecond;
                    if (watchItem.getEndEpochMilli() != null) {
                        long elapsed = watchItem.getEndEpochMilli() - watchItem.getStartEpochMilli();
                        elapsedMilli = NumberFormatter.milliseconds(elapsed);
                        elapsedSecond = NumberFormatter.seconds(elapsed / 1000.0);
                    } else {
                        elapsedMilli = NOT_AVAILABLE;
                        elapsedSecond = NOT_AVAILABLE;
                    }
                    writer.write(String.format("| %s | %s | %s |\n", fillWithWhitespace(name, maxLengthOfName), fillWithWhitespace(elapsedMilli, maxLengthOfElapsedMilliseconds), fillWithWhitespace(elapsedSecond, maxLengthOfElapsedSeconds)));
                }
                writer.flush();
            } catch (Exception e) {
                e.printStackTrace();
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

}
