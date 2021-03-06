# 2.2.1 (22 Jan 2022)

## Enhancements
- Add JavaDoc comments
- Minor code cleanup

# 2.2.0 (7 Sep 2021)

## New Features
- Add `start(String, Runnable)` feature to measure `Runnable` code snippets on the fly.

# 2.1.0 (8 Aug 2021)

## New Features
- Add `pause()` and `resume()` features in `Stopwatch`.

## Bug Fixes
- Fix incorrect calculation of fraction digits in `NumberFormatter`.

# 2.0.0 (5 Aug 2021)

## Enhancements
- Add percentage information to the output.
- Add `NumberFormatter.percentage()` method to format the given value.
- Add more constructors to `WatchItem`.
- Change some column names in the output to be shorter.
- Support `getTotalElapsedMilliseconds()` and `getTotalElapsedSeconds()` methods in `Stopwatch`.
- Support `add()` method in `Stopwatch` to customize the output.

# 1.0.0 (3 Aug 2021)

## Usage
```java
Stopwatch stopwatch = new Stopwatch();

stopwatch.start("first phase");
// ...
stopwatch.stop();

stopwatch.start("second phase");
// ...
stopwatch.stop();

stopwatch.print();
```

## Result
```
|         name | elapsed(ms) | elapsed(s) |
|--------------|-------------|------------|
|  first phase |     1,234ms |     1.234s |
| second phase |       830ms |     0.830s |
```