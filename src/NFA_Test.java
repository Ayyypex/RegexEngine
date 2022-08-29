import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;

/** Tests methods from the NFA class */
public class NFA_Test {

  @Before
  public void init() {
    RegexEngine.NFA.numberOfStates = 0;
  }

  /**
   * Tests the creation of a new NFA by comparing the expected start, end, states, and transitions.
   */
  @Test
  public void newNFA_Test() {
    RegexEngine.NFA nfa = new RegexEngine.NFA('a');

    // States should be added in this order
    List<String> states = new ArrayList<String>();
    states.add("q0");
    states.add("q1");

    // Transitions should be added in this order
    List<RegexEngine.NFA.Transition> transitions = new ArrayList<RegexEngine.NFA.Transition>();
    transitions.add( new RegexEngine.NFA.Transition("q0", "a", "q1") );

    // check start and end states, all states, and number of transitions
    assertEquals( "q0", nfa.start );
    assertEquals( "q1", nfa.end );
    assertEquals( true, nfa.states.containsAll(states) );
    assertEquals( nfa.transitions.size(), transitions.size() );

    // check if each transition is equal
    if ( nfa.transitions.size() == transitions.size() ) {
        for ( int i=0; i < nfa.transitions.size(); i++ ) {
            assertEquals( nfa.transitions.get(i).transition, transitions.get(i).transition );
        }
    }
  }

  /**
   * Tests getNewState() by calling it a few times and checking it for an expected value
   */
  @Test
  public void getNewState_Test() {
    int expected = 3;
    for ( int i=0; i < expected; i++ ) {
        RegexEngine.NFA.getNewState();
    }
    assertEquals( expected, RegexEngine.NFA.numberOfStates );
  }

  /**
   * Applies the kleeneStar() operation to an NFA and compares the expected start, end, states, and transitions.
   * Uses a simple NFA to demonstrate its functionality.
   */
  @Test
  public void kleeneStar_Test() {
    RegexEngine.NFA nfa = new RegexEngine.NFA('b');
    RegexEngine.NFA nfaStar = RegexEngine.NFA.kleeneStar(nfa);

    List<String> states = new ArrayList<String>();
    states.add("q0");
    states.add("q1");
    states.add("q2");
    states.add("q3");

    List<RegexEngine.NFA.Transition> transitions = new ArrayList<RegexEngine.NFA.Transition>();
    transitions.add( new RegexEngine.NFA.Transition("q0", "b", "q1") );
    transitions.add( new RegexEngine.NFA.Transition("q2", "epsilon", "q0") );
    transitions.add( new RegexEngine.NFA.Transition("q1", "epsilon", "q2") );
    transitions.add( new RegexEngine.NFA.Transition("q2", "epsilon", "q3") );

    assertEquals( "q2", nfaStar.start );
    assertEquals( "q3", nfaStar.end );
    assertEquals( true, nfaStar.states.containsAll(states) );
    assertEquals( nfaStar.transitions.size(), transitions.size() );

    if ( nfaStar.transitions.size() == transitions.size() ) {
        for ( int i=0; i < nfaStar.transitions.size(); i++ ) {
            assertEquals( nfaStar.transitions.get(i).transition, transitions.get(i).transition );
        }
    }
  }

  /**
   * Applies the kleenePlus() operation to an NFA and compares the expected start, end, states, and transitions.
   * Uses a simple NFA to demonstrate its functionality.
   */
  @Test
  public void kleenePlus_Test() {
    RegexEngine.NFA nfa = new RegexEngine.NFA('c');
    RegexEngine.NFA nfaPlus = RegexEngine.NFA.kleenePlus(nfa);

    List<String> states = new ArrayList<String>();
    states.add("q0");
    states.add("q1");
    states.add("q2");

    List<RegexEngine.NFA.Transition> transitions = new ArrayList<RegexEngine.NFA.Transition>();
    transitions.add( new RegexEngine.NFA.Transition("q0", "c", "q1") );
    transitions.add( new RegexEngine.NFA.Transition("q2", "epsilon", "q0") );
    transitions.add( new RegexEngine.NFA.Transition("q1", "epsilon", "q2") );

    assertEquals( "q0", nfaPlus.start );
    assertEquals( "q2", nfaPlus.end );
    assertEquals( true, nfaPlus.states.containsAll(states) );
    assertEquals( nfaPlus.transitions.size(), transitions.size() );

    if ( nfaPlus.transitions.size() == transitions.size() ) {
        for ( int i=0; i < nfaPlus.transitions.size(); i++ ) {
            assertEquals( nfaPlus.transitions.get(i).transition, transitions.get(i).transition );
        }
    }
  }

  /**
   * Applies the concatenate() operation to two NFA and compares the expected start, end, states, and transitions.
   * Uses simple NFAs to demonstrate its functionality.
   */
  @Test
  public void concatenate_Test() {
    RegexEngine.NFA nfa1 = new RegexEngine.NFA('d');
    RegexEngine.NFA nfa2 = new RegexEngine.NFA('e');
    RegexEngine.NFA nfa3 = RegexEngine.NFA.concatenate(nfa1,nfa2);

    List<String> states = new ArrayList<String>();
    states.add("q0");
    states.add("q1");
    states.add("q2");
    states.add("q3");

    List<RegexEngine.NFA.Transition> transitions = new ArrayList<RegexEngine.NFA.Transition>();
    transitions.add( new RegexEngine.NFA.Transition("q0", "d", "q1") );
    transitions.add( new RegexEngine.NFA.Transition("q2", "e", "q3") );
    transitions.add( new RegexEngine.NFA.Transition("q1", "epsilon", "q2") );

    assertEquals( "q0", nfa3.start );
    assertEquals( "q3", nfa3.end );
    assertEquals( true, nfa3.states.containsAll(states) );
    assertEquals( nfa3.transitions.size(), transitions.size() );

    if ( nfa3.transitions.size() == transitions.size() ) {
        for ( int i=0; i < nfa3.transitions.size(); i++ ) {
            assertEquals( nfa3.transitions.get(i).transition, transitions.get(i).transition );
        }
    }
  }

  
  /**
   * Applies the alternate() operation to two NFA and compares the expected start, end, states, and transitions.
   * Uses simple NFAs to demonstrate its functionality.
   */
  @Test
  public void alternate_Test() {
    RegexEngine.NFA nfa1 = new RegexEngine.NFA('f');
    RegexEngine.NFA nfa2 = new RegexEngine.NFA('g');
    RegexEngine.NFA nfa3 = RegexEngine.NFA.alternate(nfa1,nfa2);

    List<String> states = new ArrayList<String>();
    states.add("q0");
    states.add("q1");
    states.add("q2");
    states.add("q3");
    states.add("q4");
    states.add("q5");

    List<RegexEngine.NFA.Transition> transitions = new ArrayList<RegexEngine.NFA.Transition>();
    transitions.add( new RegexEngine.NFA.Transition("q0", "f", "q1") );
    transitions.add( new RegexEngine.NFA.Transition("q2", "g", "q3") );
    transitions.add( new RegexEngine.NFA.Transition("q4", "epsilon", "q0") );
    transitions.add( new RegexEngine.NFA.Transition("q4", "epsilon", "q2") );
    transitions.add( new RegexEngine.NFA.Transition("q1", "epsilon", "q5") );
    transitions.add( new RegexEngine.NFA.Transition("q3", "epsilon", "q5") );

    assertEquals( "q4", nfa3.start );
    assertEquals( "q5", nfa3.end );
    assertEquals( true, nfa3.states.containsAll(states) );
    assertEquals( nfa3.transitions.size(), transitions.size() );

    if ( nfa3.transitions.size() == transitions.size() ) {
        for ( int i=0; i < nfa3.transitions.size(); i++ ) {
            assertEquals( nfa3.transitions.get(i).transition, transitions.get(i).transition );
        }
    }
  }

  /**
   * Tests getAlphabet() by comparing the expected inputs to the ones received from calling the function
   * on NFAs of varying complexity. 
   */
  @Test
  public void getAlphabet_Test1() {
    // create NFA
    String regex = "a";
    regex = RegexEngine.addConcatenations(regex);
    String postfix = RegexEngine.toPostfix(regex);
    RegexEngine.NFA nfa = RegexEngine.generateNFA(postfix);

    // expected inputs in expected order
    List<String> expected = new ArrayList<String>();
    expected.add("a");

    List<String> result = RegexEngine.NFA.getAlphabet(nfa);

    // compare
    assertEquals( expected.size(), result.size() );
    assertTrue( expected.equals(result) );
  }

  @Test
  public void getAlphabet_Test2() {
    String regex = "a*";
    regex = RegexEngine.addConcatenations(regex);
    String postfix = RegexEngine.toPostfix(regex);
    RegexEngine.NFA nfa = RegexEngine.generateNFA(postfix);

    List<String> expected = new ArrayList<String>();
    expected.add("epsilon");
    expected.add("a");

    List<String> result = RegexEngine.NFA.getAlphabet(nfa);

    assertEquals( expected.size(), result.size() );
    assertTrue( expected.equals(result) );
  }

  @Test
  public void getAlphabet_Test3() {
    String regex = "a|b";
    regex = RegexEngine.addConcatenations(regex);
    String postfix = RegexEngine.toPostfix(regex);
    RegexEngine.NFA nfa = RegexEngine.generateNFA(postfix);

    List<String> expected = new ArrayList<String>();
    expected.add("epsilon");
    expected.add("a");
    expected.add("b");

    List<String> result = RegexEngine.NFA.getAlphabet(nfa);

    assertEquals( expected.size(), result.size() );
    assertTrue( expected.equals(result) );
  }

  @Test
  public void getAlphabet_Test4() {
    String regex = "(a|bc1)+( de2)";
    regex = RegexEngine.addConcatenations(regex);
    String postfix = RegexEngine.toPostfix(regex);
    RegexEngine.NFA nfa = RegexEngine.generateNFA(postfix);

    List<String> expected = new ArrayList<String>();
    expected.add("epsilon");
    expected.add(" ");
    expected.add("1");
    expected.add("2");
    expected.add("a");
    expected.add("b");
    expected.add("c");
    expected.add("d");
    expected.add("e");

    List<String> result = RegexEngine.NFA.getAlphabet(nfa);

    assertEquals( expected.size(), result.size() );
    assertTrue( expected.equals(result) );
  }

  @Test
  public void getAlphabet_Test5() {
    String regex = "a(b|cd|e*f|g+h|i)(jk)*";
    regex = RegexEngine.addConcatenations(regex);
    String postfix = RegexEngine.toPostfix(regex);
    RegexEngine.NFA nfa = RegexEngine.generateNFA(postfix);

    List<String> expected = new ArrayList<String>();
    expected.add("epsilon");
    expected.add("a");
    expected.add("b");
    expected.add("c");
    expected.add("d");
    expected.add("e");
    expected.add("f");
    expected.add("g");
    expected.add("h");
    expected.add("i");
    expected.add("j");
    expected.add("k");

    List<String> result = RegexEngine.NFA.getAlphabet(nfa);

    assertEquals( expected.size(), result.size() );
    assertTrue( expected.equals(result) );
  }

   /**
   * Tests getTable() by comparing the expected table to the one received from calling the function
   * on simple NFAs. Simple NFAs are used because the tables get quite large otherwise. All operators
   * are tested.
   */
  @Test
  public void getTable_Test1() {
    String regex = "a";
    regex = RegexEngine.addConcatenations(regex);
    String postfix = RegexEngine.toPostfix(regex);
    RegexEngine.NFA nfa = RegexEngine.generateNFA(postfix);

    // expected table
    String[][] expected = { { "    | ",  "a  | " }, 
                            { ">q0 | ",  "q1 | " }, 
                            { "*q1 | ",  "   | " }};
    
    // resulting table
    String[][] result = RegexEngine.NFA.getTable(nfa);

    assertTrue( Arrays.deepEquals(expected, result) );
  }

  @Test
  public void getTable_Test2() {
    String regex = "ab";
    regex = RegexEngine.addConcatenations(regex);
    String postfix = RegexEngine.toPostfix(regex);
    RegexEngine.NFA nfa = RegexEngine.generateNFA(postfix);

    String[][] expected = { { "    | ",  "epsilon | ",  "a  | ",  "b  | " }, 
                            { ">q0 | ",  "        | ",  "q1 | ",  "   | " }, 
                            { "q1  | ",  "q2      | ",  "   | ",  "   | " }, 
                            { "q2  | ",  "        | ",  "   | ",  "q3 | " }, 
                            { "*q3 | ",  "        | ",  "   | ",  "   | " }};
    
    String[][] result = RegexEngine.NFA.getTable(nfa);

    assertTrue( Arrays.deepEquals(expected, result) );
  }

  @Test
  public void getTable_Test3() {
    String regex = "abc*";
    regex = RegexEngine.addConcatenations(regex);
    String postfix = RegexEngine.toPostfix(regex);
    RegexEngine.NFA nfa = RegexEngine.generateNFA(postfix);

    String[][] expected = { { "    | ",  "epsilon | ",  "a  | ",  "b  | ",  "c  | " }, 
                            { ">q0 | ",  "        | ",  "q1 | ",  "   | ",  "   | " }, 
                            { "q1  | ",  "q2      | ",  "   | ",  "   | ",  "   | " }, 
                            { "q2  | ",  "        | ",  "   | ",  "q3 | ",  "   | " }, 
                            { "q3  | ",  "q6      | ",  "   | ",  "   | ",  "   | " },
                            { "q4  | ",  "        | ",  "   | ",  "   | ",  "q5 | " }, 
                            { "q5  | ",  "q6      | ",  "   | ",  "   | ",  "   | " }, 
                            { "q6  | ",  "q4,q7   | ",  "   | ",  "   | ",  "   | " }, 
                            { "*q7 | ",  "        | ",  "   | ",  "   | ",  "   | " }};
    
    String[][] result = RegexEngine.NFA.getTable(nfa);

    assertTrue( Arrays.deepEquals(expected, result) );
  }

  @Test
  public void getTable_Test4() {
    String regex = "ab +";
    regex = RegexEngine.addConcatenations(regex);
    String postfix = RegexEngine.toPostfix(regex);
    RegexEngine.NFA nfa = RegexEngine.generateNFA(postfix);

    String[][] expected = { { "    | ",  "epsilon | ",  "   | ",  "a  | ",  "b  | " }, 
                            { ">q0 | ",  "        | ",  "   | ",  "q1 | ",  "   | " }, 
                            { "q1  | ",  "q2      | ",  "   | ",  "   | ",  "   | " }, 
                            { "q2  | ",  "        | ",  "   | ",  "   | ",  "q3 | " }, 
                            { "q3  | ",  "q4      | ",  "   | ",  "   | ",  "   | " },
                            { "q4  | ",  "        | ",  "q5 | ",  "   | ",  "   | " }, 
                            { "q5  | ",  "q6      | ",  "   | ",  "   | ",  "   | " }, 
                            { "*q6 | ",  "q4      | ",  "   | ",  "   | ",  "   | " }};
    
    String[][] result = RegexEngine.NFA.getTable(nfa);

    assertTrue( Arrays.deepEquals(expected, result) );
  }

  @Test
  public void getTable_Test5() {
    String regex = "a|b";
    regex = RegexEngine.addConcatenations(regex);
    String postfix = RegexEngine.toPostfix(regex);
    RegexEngine.NFA nfa = RegexEngine.generateNFA(postfix);

    String[][] expected = { { "    | ",  "epsilon | ",  "a  | ",  "b  | " }, 
                            { "q0  | ",  "        | ",  "q1 | ",  "   | " }, 
                            { "q1  | ",  "q5      | ",  "   | ",  "   | " }, 
                            { "q2  | ",  "        | ",  "   | ",  "q3 | " }, 
                            { "q3  | ",  "q5      | ",  "   | ",  "   | " },
                            { ">q4 | ",  "q0,q2   | ",  "   | ",  "   | " }, 
                            { "*q5 | ",  "        | ",  "   | ",  "   | " }};
    
    String[][] result = RegexEngine.NFA.getTable(nfa);

    assertTrue( Arrays.deepEquals(expected, result) );
  }
}