import java.util.List;
import java.util.ArrayList;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import org.junit.Before;


// compile with: javac -cp .:junit-4.13.2.jar:hamcrest-core-1.3.jar simulateNFA_Test.java
//     run with: java -cp .:junit-4.13.2.jar:hamcrest-core-1.3.jar org.junit.runner.JUnitCore simulateNFA_Test

public class simulateNFA_Test {
  @Before
  public void init() {
    RegexEngine.NFA.numberOfStates = 0;
  }
 
  /* 
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
        { "a1", false },
        { "aa", false },
        { "a ", false },
        { " a", false },
        { " a ", false }
    };

    // test each input against its expected output
    for ( int i=0; i < input.length; i++ ) {
        assertEquals( input[i][1], RegexEngine.simulateNFA( nfa, String.valueOf(input[i][0]) ) );
    }
  }

  @Test
  public void test2() {
    String regex = "abc";
    String infix = RegexEngine.addConcatenations(regex);
    String postfix = RegexEngine.toPostfix(infix);
    RegexEngine.NFA nfa = RegexEngine.generateNFA(postfix);

    Object[][] input =  {
        { "abc", true },
        { "abb", false },
        { "ab ", false },
        { "acb", false },
        { "a1bc", false },
        { "a b c", false }
    };

    for ( int i=0; i < input.length; i++ ) {
        assertEquals( input[i][1], RegexEngine.simulateNFA( nfa, String.valueOf(input[i][0]) ) );
    }
  }

  @Test
  public void test3() {
    String regex = "a1d2";
    String infix = RegexEngine.addConcatenations(regex);
    String postfix = RegexEngine.toPostfix(infix);
    RegexEngine.NFA nfa = RegexEngine.generateNFA(postfix);

    Object[][] input =  {
        { "a1d2", true },
        { "ald2", false },
        { "a d2 ", false },
        { "2d1a", false },
        { "41d2", false },
        { "a 1 d 2", false }
    };

    for ( int i=0; i < input.length; i++ ) {
        assertEquals( input[i][1], RegexEngine.simulateNFA( nfa, String.valueOf(input[i][0]) ) );
    }
  }

  @Test
  public void test4() {
    String regex = "a Z 5";
    String infix = RegexEngine.addConcatenations(regex);
    String postfix = RegexEngine.toPostfix(infix);
    RegexEngine.NFA nfa = RegexEngine.generateNFA(postfix);

    Object[][] input =  {
        { "a Z 5", true },
        { "aZ5", false },
        { "a Z5 ", false },
        { "a Z 5 ", false },
        { "5az", false },
        { "a Z  5", false }
    };

    for ( int i=0; i < input.length; i++ ) {
        assertEquals( input[i][1], RegexEngine.simulateNFA( nfa, String.valueOf(input[i][0]) ) );
    }
  }
  */
  @Test
  public void test5() {
    String regex = "a*";
    String infix = RegexEngine.addConcatenations(regex);
    String postfix = RegexEngine.toPostfix(infix);
    RegexEngine.NFA nfa = RegexEngine.generateNFA(postfix);

    System.out.println(infix);
    System.out.println(postfix);

    Object[][] input =  {
        { "", true },
        { "a", true },
        { "aaa", true },
        { "aaaaaaaaaaaaaaaaa", true },
        { " aaa", false },
        { "aa1aa", false },
        { "aa aa", false },
        { "  ", false },
    };

    for ( int i=0; i < input.length; i++ ) {
        assertEquals( input[i][1], RegexEngine.simulateNFA( nfa, String.valueOf(input[i][0]) ) );
    }
  }
}