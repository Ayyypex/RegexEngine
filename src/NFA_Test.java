import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;

/* */
public class NFA_Test {

  @Before
  public void init() {
    RegexEngine.NFA.numberOfStates = 0;
  }

  @Test
  public void newNFA_Test() {
    RegexEngine.NFA a = new RegexEngine.NFA('a');

    // States should be added in this order
    List<String> states = new ArrayList<String>();
    states.add("q0");
    states.add("q1");

    // Transitions should be added in this order
    List<RegexEngine.NFA.Transition> transitions = new ArrayList<RegexEngine.NFA.Transition>();
    transitions.add( new RegexEngine.NFA.Transition("q0", "a", "q1") );

    // check start and end states, all states, and number of transitions
    assertEquals( "q0", a.start );
    assertEquals( "q1", a.end );
    assertEquals( true, a.states.containsAll(states) );
    assertEquals( a.transitions.size(), transitions.size() );

    // check if each transition is equal
    if ( a.transitions.size() == transitions.size() ) {
        for ( int i=0; i < a.transitions.size(); i++ ) {
            assertEquals( a.transitions.get(i).transition, transitions.get(i).transition );
        }
    }
  }

  @Test
  public void getNewState_Test() {
    int number = 3;
    for ( int i=0; i < number; i++ ) {
        RegexEngine.NFA.getNewState();
    }
    assertEquals( number, RegexEngine.NFA.numberOfStates );
  }

  @Test
  public void kleeneStar_Test() {
    RegexEngine.NFA b = new RegexEngine.NFA('b');
    RegexEngine.NFA bStar = RegexEngine.NFA.kleeneStar(b);

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

    assertEquals( "q2", bStar.start );
    assertEquals( "q3", bStar.end );
    assertEquals( true, bStar.states.containsAll(states) );
    assertEquals( bStar.transitions.size(), transitions.size() );

    if ( bStar.transitions.size() == transitions.size() ) {
        for ( int i=0; i < bStar.transitions.size(); i++ ) {
            assertEquals( bStar.transitions.get(i).transition, transitions.get(i).transition );
        }
    }
  }

  @Test
  public void kleenePlus_Test() {
    RegexEngine.NFA c = new RegexEngine.NFA('c');
    RegexEngine.NFA cPlus = RegexEngine.NFA.kleenePlus(c);

    List<String> states = new ArrayList<String>();
    states.add("q0");
    states.add("q1");
    states.add("q2");

    List<RegexEngine.NFA.Transition> transitions = new ArrayList<RegexEngine.NFA.Transition>();
    transitions.add( new RegexEngine.NFA.Transition("q0", "c", "q1") );
    transitions.add( new RegexEngine.NFA.Transition("q2", "epsilon", "q0") );
    transitions.add( new RegexEngine.NFA.Transition("q1", "epsilon", "q2") );

    assertEquals( "q0", cPlus.start );
    assertEquals( "q2", cPlus.end );
    assertEquals( true, cPlus.states.containsAll(states) );
    assertEquals( cPlus.transitions.size(), transitions.size() );

    if ( cPlus.transitions.size() == transitions.size() ) {
        for ( int i=0; i < cPlus.transitions.size(); i++ ) {
            assertEquals( cPlus.transitions.get(i).transition, transitions.get(i).transition );
        }
    }
  }

  @Test
  public void concatenate_Test() {
    RegexEngine.NFA d = new RegexEngine.NFA('d');
    RegexEngine.NFA e = new RegexEngine.NFA('e');
    RegexEngine.NFA de = RegexEngine.NFA.concatenate(d,e);

    List<String> states = new ArrayList<String>();
    states.add("q0");
    states.add("q1");
    states.add("q2");
    states.add("q3");

    List<RegexEngine.NFA.Transition> transitions = new ArrayList<RegexEngine.NFA.Transition>();
    transitions.add( new RegexEngine.NFA.Transition("q0", "d", "q1") );
    transitions.add( new RegexEngine.NFA.Transition("q2", "e", "q3") );
    transitions.add( new RegexEngine.NFA.Transition("q1", "epsilon", "q2") );

    assertEquals( "q0", de.start );
    assertEquals( "q3", de.end );
    assertEquals( true, de.states.containsAll(states) );
    assertEquals( de.transitions.size(), transitions.size() );

    if ( de.transitions.size() == transitions.size() ) {
        for ( int i=0; i < de.transitions.size(); i++ ) {
            assertEquals( de.transitions.get(i).transition, transitions.get(i).transition );
        }
    }
  }

  @Test
  public void alternate_Test() {
    RegexEngine.NFA f = new RegexEngine.NFA('f');
    RegexEngine.NFA g = new RegexEngine.NFA('g');
    RegexEngine.NFA fORg = RegexEngine.NFA.alternate(f,g);

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

    assertEquals( "q4", fORg.start );
    assertEquals( "q5", fORg.end );
    assertEquals( true, fORg.states.containsAll(states) );
    assertEquals( fORg.transitions.size(), transitions.size() );

    if ( fORg.transitions.size() == transitions.size() ) {
        for ( int i=0; i < fORg.transitions.size(); i++ ) {
            assertEquals( fORg.transitions.get(i).transition, transitions.get(i).transition );
        }
    }
  }

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