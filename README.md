# Stopwatch

`Stopwatch` for get rid of chore in your code.

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

## Contributing
Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

Please note we have a [CODE_OF_CONDUCT](https://github.com/silentsoft/stopwatch/blob/main/CODE_OF_CONDUCT.md), please follow it in all your interactions with the project.

## License
Please refer to [LICENSE](https://github.com/silentsoft/stopwatch/blob/main/LICENSE.txt).
