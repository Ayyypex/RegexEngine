import java.util.List;
import java.util.ArrayList;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import org.junit.runner.OrderWith;
import org.junit.Before;


// compile with: javac -cp .:junit-4.13.2.jar:hamcrest-core-1.3.jar NFA_Test.java
//     run with: java -cp .:junit-4.13.2.jar:hamcrest-core-1.3.jar org.junit.runner.JUnitCore NFA_Test

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
  public void newState_Test() {
    int number = 3;
    for ( int i=0; i < number; i++ ) {
        RegexEngine.NFA.newState();
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

    List<RegexEngine.NFA.Transition> transitions = new ArrayList<RegexEngine.NFA.Transition>();
    transitions.add( new RegexEngine.NFA.Transition("q0", "b", "q1") );
    transitions.add( new RegexEngine.NFA.Transition("q2", "eps", "q0") );
    transitions.add( new RegexEngine.NFA.Transition("q1", "eps", "q2") );

    assertEquals( "q2", bStar.start );
    assertEquals( "q2", bStar.end );
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
    transitions.add( new RegexEngine.NFA.Transition("q2", "eps", "q0") );
    transitions.add( new RegexEngine.NFA.Transition("q1", "eps", "q2") );

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
    transitions.add( new RegexEngine.NFA.Transition("q1", "eps", "q2") );

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
    transitions.add( new RegexEngine.NFA.Transition("q4", "eps", "q0") );
    transitions.add( new RegexEngine.NFA.Transition("q4", "eps", "q2") );
    transitions.add( new RegexEngine.NFA.Transition("q1", "eps", "q5") );
    transitions.add( new RegexEngine.NFA.Transition("q3", "eps", "q5") );

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
}