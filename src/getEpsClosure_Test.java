import java.util.Set;
import java.util.HashSet;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;

/**
 * Tests getEpsClosure() by comparing the expected state set of the epsilon closure of the start of 
 * of NFAs generated from simple to more complex regular expressions.
 */
public class getEpsClosure_Test {
 
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

    // get epsilon closure of NFA's start state
    Set<String> eClose = new HashSet<String>();
    eClose.add(nfa.start);
    eClose = RegexEngine.NFA.getEpsClosure(nfa, eClose);

    // expected states from getEpsClosure
    Set<String> expected = new HashSet<String>();
    expected.add("q0");   // start has no eps transitions

    // check if size is equal and if expected has all the states in eClose
    assertEquals( expected.size(), eClose.size() );
    assertTrue( expected.containsAll(eClose) );
  }

  @Test
  public void test2() {
    String regex = "a*";
    String infix = RegexEngine.addConcatenations(regex);
    String postfix = RegexEngine.toPostfix(infix);
    RegexEngine.NFA nfa = RegexEngine.generateNFA(postfix);

    Set<String> eClose = new HashSet<String>();
    eClose.add(nfa.start);
    eClose = RegexEngine.NFA.getEpsClosure(nfa, eClose);

    Set<String> expected = new HashSet<String>();
    expected.add("q2");   // start of a*
    expected.add("q0");   // start of a
    expected.add("q3");   // end of a*

    assertEquals( expected.size(), eClose.size() );
    assertTrue( expected.containsAll(eClose) );
  }

  @Test
  public void test3() {
    String regex = "a+";
    String infix = RegexEngine.addConcatenations(regex);
    String postfix = RegexEngine.toPostfix(infix);
    RegexEngine.NFA nfa = RegexEngine.generateNFA(postfix);

    Set<String> eClose = new HashSet<String>();
    eClose.add(nfa.start);
    eClose = RegexEngine.NFA.getEpsClosure(nfa, eClose);

    Set<String> expected = new HashSet<String>();
    expected.add("q0");   // start has not eps transitions

    assertEquals( expected.size(), eClose.size() );
    assertTrue( expected.containsAll(eClose) );
  }

  @Test
  public void test4() {
    String regex = "a|b";
    String infix = RegexEngine.addConcatenations(regex);
    String postfix = RegexEngine.toPostfix(infix);
    RegexEngine.NFA nfa = RegexEngine.generateNFA(postfix);

    Set<String> eClose = new HashSet<String>();
    eClose.add(nfa.start);
    eClose = RegexEngine.NFA.getEpsClosure(nfa, eClose);

    Set<String> expected = new HashSet<String>();
    expected.add("q4");   // start of a|b
    expected.add("q0");   // start of a
    expected.add("q2");   // start of b

    assertEquals( expected.size(), eClose.size() );
    assertTrue( expected.containsAll(eClose) );
  }
  

  @Test
  public void test5() {
    String regex = "a|b|c|d";
    String infix = RegexEngine.addConcatenations(regex);
    String postfix = RegexEngine.toPostfix(infix);
    RegexEngine.NFA nfa = RegexEngine.generateNFA(postfix);

    Set<String> eClose = new HashSet<String>();
    eClose.add(nfa.start);
    eClose = RegexEngine.NFA.getEpsClosure(nfa, eClose);

    Set<String> expected = new HashSet<String>();
    expected.add("q12");  // start of a|b|c|d
    expected.add("q8");   // start of a|b|c
    expected.add("q10");  // start of d
    expected.add("q4");   // start of a|b
    expected.add("q6");   // start of c
    expected.add("q0");   // start of a
    expected.add("q2");   // start of b

    assertEquals( expected.size(), eClose.size() );
    assertTrue( expected.containsAll(eClose) );
  }

  @Test
  public void test6() {
    String regex = "(a|b)|(c|d)";
    String infix = RegexEngine.addConcatenations(regex);
    String postfix = RegexEngine.toPostfix(infix);
    RegexEngine.NFA nfa = RegexEngine.generateNFA(postfix);

    Set<String> eClose = new HashSet<String>();
    eClose.add(nfa.start);
    eClose = RegexEngine.NFA.getEpsClosure(nfa, eClose);

    Set<String> expected = new HashSet<String>();
    expected.add("q12");  // start of (a|b)|(c|d)
    expected.add("q4");   // start of a|b
    expected.add("q10");  // start of c|d
    expected.add("q0");   // start of a
    expected.add("q2");   // start of b
    expected.add("q6");   // start of c
    expected.add("q8");   // start of d

    assertEquals( expected.size(), eClose.size() );
    assertTrue( expected.containsAll(eClose) );
  }

  @Test
  public void test7() {
    String regex = "(a|b)|(c*)";
    String infix = RegexEngine.addConcatenations(regex);
    String postfix = RegexEngine.toPostfix(infix);
    RegexEngine.NFA nfa = RegexEngine.generateNFA(postfix);

    Set<String> eClose = new HashSet<String>();
    eClose.add(nfa.start);
    eClose = RegexEngine.NFA.getEpsClosure(nfa, eClose);

    Set<String> expected = new HashSet<String>();
    expected.add("q10");  // start of (a|b)|(c*)
    expected.add("q4");   // start of a|b
    expected.add("q8");   // start of c*
    expected.add("q0");   // start of a
    expected.add("q2");   // start of b
    expected.add("q6");   // start of c
    expected.add("q9");   // end of c*
    expected.add("q11");  // end of (a|b)|(c*)

    assertEquals( expected.size(), eClose.size() );
    assertTrue( expected.containsAll(eClose) );
  }

  @Test
  public void test8() {
    String regex = "(a|b)|(c+)";
    String infix = RegexEngine.addConcatenations(regex);
    String postfix = RegexEngine.toPostfix(infix);
    RegexEngine.NFA nfa = RegexEngine.generateNFA(postfix);

    Set<String> eClose = new HashSet<String>();
    eClose.add(nfa.start);
    eClose = RegexEngine.NFA.getEpsClosure(nfa, eClose);

    Set<String> expected = new HashSet<String>();
    expected.add("q9");   // start of (a|b)|(c+)
    expected.add("q4");   // start of a|b
    expected.add("q6");   // start of c+
    expected.add("q0");   // start of a
    expected.add("q2");   // start of b

    assertEquals( expected.size(), eClose.size() );
    assertTrue( expected.containsAll(eClose) );
  }
}