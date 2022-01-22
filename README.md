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

# Web_Service (sub project)

The directory `web_service` contains a spring-web + gradle project which will expose functionality from the overall `wordle` repo.  See [web_service/README.md](web_service/README.md) for more detail.
