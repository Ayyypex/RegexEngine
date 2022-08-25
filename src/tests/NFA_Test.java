import java.util.List;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

// compile with: javac -cp .:junit-4.13.2.jar:hamcrest-core-1.3.jar *_Test.java
//     run with: java -cp .:junit-4.13.2.jar:hamcrest-core-1.3.jar org.junit.runner.JUnitCore NFA_Test

public class NFA_Test {

  @Test
  public void newNFA_test() {
    RegexEngine.NFA a = new RegexEngine.NFA('a');

    List<String> states = new ArrayList<String>();
    states.add("q0");
    states.add("q1");

    List<RegexEngine.NFA.Transition> transitions = new ArrayList<RegexEngine.NFA.Transition>();
    transitions.add( new RegexEngine.NFA.Transition("q0", "a", "q1") );

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
  public void newState_test() {
    int number = 3;
    for ( int i=0; i < number; i++ ) {
        RegexEngine.NFA.newState();
    }
    assertEquals( number+2, RegexEngine.NFA.numberOfStates );   // + 2 from previous test
  }

  @Test
  public void kleeneStar_test() {
    assertEquals( true, RegexEngine.validRegex("abcde fghijklmnopqr stuvwxyz") );
  }

  @Test
  public void kleenePlus_test() {
    assertEquals( true, RegexEngine.validRegex("ABCD EFGHI JKLMNOPQR STUVWXYZ") );
  }

  @Test
  public void concatenate_test() {
    RegexEngine.NFA d = new RegexEngine.NFA('d');
    RegexEngine.NFA e = new RegexEngine.NFA('e');
    RegexEngine.NFA de = RegexEngine.NFA.concatenate(d,e);

    List<String> states = new ArrayList<String>();
    states.add("q5");
    states.add("q6");
    states.add("q7");
    states.add("q8");

    List<RegexEngine.NFA.Transition> transitions = new ArrayList<RegexEngine.NFA.Transition>();
    transitions.add( new RegexEngine.NFA.Transition("q5", "d", "q6") );
    transitions.add( new RegexEngine.NFA.Transition("q7", "e", "q8") );
    transitions.add( new RegexEngine.NFA.Transition("q6", "eps", "q7") );

    assertEquals( "q5", de.start );
    assertEquals( "q8", de.end );
    assertEquals( true, de.states.containsAll(states) );
    assertEquals( de.transitions.size(), transitions.size() );

    // check if each transition is equal
    if ( de.transitions.size() == transitions.size() ) {
        for ( int i=0; i < de.transitions.size(); i++ ) {
            assertEquals( de.transitions.get(i).transition, transitions.get(i).transition );
        }
    }
  }

  @Test
  public void alternate_test() {
    assertEquals( true, RegexEngine.validRegex("012345 6789") );
  }
}