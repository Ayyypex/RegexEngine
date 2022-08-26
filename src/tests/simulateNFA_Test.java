import java.util.List;
import java.util.ArrayList;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

// compile with: javac -cp .:junit-4.13.2.jar:hamcrest-core-1.3.jar simulateNFA_Test.java
//     run with: java -cp .:junit-4.13.2.jar:hamcrest-core-1.3.jar org.junit.runner.JUnitCore simulateNFA_Test

public class simulateNFA_Test {
 
  @Test
  public void test1() {
    // create NFA to test
    String regex = "a";
    String infix = RegexEngine.addConcatenations(regex);
    String postfix = RegexEngine.toPostfix(infix);
    RegexEngine.NFA nfa = RegexEngine.generateNFA(postfix);

    // input and expected output
    Object[][] input =  {
        { "a", true },
        { "b", false },
        { " ", false },
        { "ab", false },
        { "aa", false },
    };

    // test each input against its expected output
    for ( int i=0; i < input.length; i++ ) {
        assertEquals(input[i][1], RegexEngine.simulateNFA( nfa, String.valueOf(input[i][0]) ) );
    }


  }
}