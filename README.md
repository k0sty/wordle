# wordle

## Reqs

- Java (openJDK ok)
- Gradle

## invoke

`./gradlew build`

Command allows for `System.in` interaction with `WordlePlayer`:

`./gradlew -q --console plain run`

This command not useful, but syntax handy for future gradle work

`./gradlew --stacktrace clean build run`

## unit test

`./gradlew clean test (-i)`

