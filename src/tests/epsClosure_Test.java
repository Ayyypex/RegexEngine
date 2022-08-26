import java.util.Set;
import java.util.HashSet;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;


// compile with: javac -cp .:junit-4.13.2.jar:hamcrest-core-1.3.jar epsClosure_Test.java
//     run with: java -cp .:junit-4.13.2.jar:hamcrest-core-1.3.jar org.junit.runner.JUnitCore epsClosure_Test

public class epsClosure_Test {
 
  @Before
  public void init() {
    RegexEngine.NFA.numberOfStates = 0;
  }

  @Test
  public void test1() {
    // NFA to test
    String regex = "a";
    String infix = RegexEngine.addConcatenations(regex);
    String postfix = RegexEngine.toPostfix(infix);
    RegexEngine.NFA nfa = RegexEngine.generateNFA(postfix);

    // get epsilong closure of NFA's start state
    Set<String> eClose = new HashSet<String>();
    eClose.add(nfa.start);
    eClose = RegexEngine.NFA.epsClosure(nfa, eClose);

    // expected states from epsClosure
    Set<String> expected = new HashSet<String>();
    expected.add("q0");   

    // check if size is equal and if expected has all the states in eClose
    assertEquals( expected.size(), eClose.size() );
    assertTrue( expected.containsAll(eClose) );
  }
}