# Stopwatch

![Maven Central](https://img.shields.io/maven-central/v/org.silentsoft/stopwatch)
[![Build Status](https://travis-ci.com/silentsoft/stopwatch.svg?branch=main)](https://travis-ci.com/silentsoft/stopwatch)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=silentsoft_stopwatch&metric=alert_status)](https://sonarcloud.io/dashboard?id=silentsoft_stopwatch)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=silentsoft_stopwatch&metric=coverage)](https://sonarcloud.io/dashboard?id=silentsoft_stopwatch)
[![Hits](https://hits.sh/github.com/silentsoft/stopwatch.svg)](https://hits.sh)

`Stopwatch` for get rid of chore in your code.

## Usage
```java
Stopwatch stopwatch = new Stopwatch();

stopwatch.start("initialization");
// ...
stopwatch.stop();

stopwatch.start("processing");
// ...
stopwatch.stop();

stopwatch.start("rendering");
// ...
stopwatch.stop();

stopwatch.print();
```

## Result
```
|           name |     % |      ms |      s |
|----------------|-------|---------|--------|
| initialization | 59.5% | 1,234ms | 1.234s |
|     processing | 40.0% |   830ms | 0.830s |
|      rendering |  0.5% |    10ms | 0.010s |
|                |       |         |        |
|          total |  100% | 2,074ms | 2.074s |
```

## Maven Central
```xml
<dependency>
    <groupId>org.silentsoft</groupId>
    <artifactId>stopwatch</artifactId>
    <version>2.0.0</version>
</dependency>
```

## Contributing
Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

Please note we have a [CODE_OF_CONDUCT](https://github.com/silentsoft/stopwatch/blob/main/CODE_OF_CONDUCT.md), please follow it in all your interactions with the project.

## License
Please refer to [LICENSE](https://github.com/silentsoft/stopwatch/blob/main/LICENSE.txt).
