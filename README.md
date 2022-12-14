# RegexEngine
The RegexEngine parses user input to create an Epsilon-NFA structure which is used to match against following input strings. 

The initial input is in the form of a basic regular expression that may consist of the following symbols: ( ) + * |

The program will print true or false for each of the following input strings depending on whether they match the regex. In verbose mode, the program will print the current state of acceptance as the user adds input. In this mode, each line of input is concatenated to a string which the engine will evaluate.

### Compiling and running the program
```shell
cd src
javac RegexEngine.java 
java RegexEngine 

# OR run in verbose mode
java RegexEngine -v
```

### Compiling and running the tests
```shell
# compile all tests 
cd src
javac -cp .:junit-4.13.2.jar:hamcrest-core-1.3.jar *_Test.java

# run specific test
java -cp .:junit-4.13.2.jar:hamcrest-core-1.3.jar org.junit.runner.JUnitCore "TestName"
```
