# wordle
![Build](https://github.com/k0sty/wordle/actions/workflows/gradle.yml/badge.svg)

Live deployment at [solvewordle.net](http://solvewordle.net/).

Read the [blog post](https://matthewkindzerske.com/2022/03/30/side-project-wordle-solver/).

## Requirements

- Java (OpenJDK ok)
- Gradle

## Usage

`./gradlew build`

Command allows for `System.in` interaction with `WordlePlayer`:

`./gradlew -q --console plain run`

This command is not useful, but syntax handy for future gradle work:

`./gradlew --stacktrace clean build run`

## Unit Tests

`./gradlew clean test (-i)`

# Sub Projects

## Lambda_Function

The directory `lambda_function` contains an AWS `RequestStreamHandler` which will expose functionality from the overall `wordle` repo.  There are also supporting bash scripts for deployment via AWS CLI.  See [lambda_function/README.md](web_service/README.md) for more detail.

## Web_Service

The directory `web_service` contains a spring-web + gradle project which will expose functionality from the overall `wordle` repo.  See [web_service/README.md](web_service/README.md) for more detail.
