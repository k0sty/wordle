# Wordle::Web_Service

A sub-project which contains spring web for offering REST of WorldSolver.

(It's anticipated that wordle will be going through evolution of the algorithm and still need `main()` entries, this sub project a tactical way of getting REST going.)

## Symlinking

This web_service is a plan spring-web project, but uses symlinks to particular packages and resources in the base project (i.e. trie, utils, and word list files in `/resources`)

## SingletonSteward

This is a wrapping class to prevent multiple instances of Wordle functional classes are not created, and that resource files are read+parsed only once (on spring bootRun etc.).

## Commands

(all from this `/web_service` diretory)

`../gradlew bootRun`

Then access at [localhost:8080](localhost:8080)

```
# Response test
curl localhost:8080/responseTest

# renders JSON response
curl "localhost:8080/demoTrie?missingCharsCSV=a,r,s,m,o,v,t,l,h&charGuessesMapCSV=0n,2c&currentGuess=-i--e"
# with more responses
curl "localhost:8080/demoTrie?missingCharsCSV=a,r,s,m,o,v,t,l,h&charGuessesMapCSV=&currentGuess=-i--e"

# invalid request, curl printing Header+httpCode
curl -i "localhost:8080/demoTrie?missingCharsCSV=a,rt,s,m,o,v,t,l,h&charGuessesMapCSV=0n,2c&currentGuess=-i--e"
```

## Formal Service

```

../gradlew clean build
cat build/bootJarMainClassName  #get the fully qualified package name
cp -i build/libs/wordle_solver-0.0.1-SNAPSHOT.jar ~/wordle_running/

# sudo necessary under EC2 to run on port 80
cd ~/wordle_running/
sudo su
sudo SERVER_PORT=80 nohup /usr/bin/java -jar /home/ec2-user/wordle_running/wordle_solver-0.0.1-SNAPSHOT.jar web.wordle_solver.WordleSolverApplication > /home/ec2-user/wordle_running/nohup.out &
```

```
ps -aux | grep WordleSolverApplication
sudo kill -9 xxx
```

## Unit Tests

```
../gradlew clean test (-i)

# ...or speicific Class
../gradlew clean test --tests WordleSolverApplicationTests -i
```
