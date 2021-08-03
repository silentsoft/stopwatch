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