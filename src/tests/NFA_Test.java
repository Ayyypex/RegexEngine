import java.util.List;
import java.util.ArrayList;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import org.junit.runner.OrderWith;
import org.junit.runner.manipulation.Alphanumeric;


// compile with: javac -cp .:junit-4.13.2.jar:hamcrest-core-1.3.jar *_Test.java
//     run with: java -cp .:junit-4.13.2.jar:hamcrest-core-1.3.jar org.junit.runner.JUnitCore NFA_Test

@OrderWith(Alphanumeric.class)
public class NFA_Test {

  @Test
  public void test1_newNFA() {
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
  public void test2_newState() {
    int number = 3;
    for ( int i=0; i < number; i++ ) {
        RegexEngine.NFA.newState();
    }
    assertEquals( number+2, RegexEngine.NFA.numberOfStates );   // + 2 from previous test
  }

  @Test
  public void test3_kleeneStar() {
    RegexEngine.NFA b = new RegexEngine.NFA('b');
    RegexEngine.NFA bStar = RegexEngine.NFA.kleeneStar(b);

    List<String> states = new ArrayList<String>();
    states.add("q5");
    states.add("q6");
    states.add("q7");

    List<RegexEngine.NFA.Transition> transitions = new ArrayList<RegexEngine.NFA.Transition>();
    transitions.add( new RegexEngine.NFA.Transition("q5", "b", "q6") );
    transitions.add( new RegexEngine.NFA.Transition("q7", "eps", "q5") );
    transitions.add( new RegexEngine.NFA.Transition("q6", "eps", "q7") );

    assertEquals( "q7", bStar.start );
    assertEquals( "q7", bStar.end );
    assertEquals( true, bStar.states.containsAll(states) );
    assertEquals( bStar.transitions.size(), transitions.size() );

    // check if each transition is equal
    if ( bStar.transitions.size() == transitions.size() ) {
        for ( int i=0; i < bStar.transitions.size(); i++ ) {
            assertEquals( bStar.transitions.get(i).transition, transitions.get(i).transition );
        }
    }
  }

  @Test
  public void test4_kleenePlus() {
    RegexEngine.NFA c = new RegexEngine.NFA('c');
    RegexEngine.NFA cPlus = RegexEngine.NFA.kleeneStar(c);

    List<String> states = new ArrayList<String>();
    states.add("q8");
    states.add("q9");
    states.add("q10");

    List<RegexEngine.NFA.Transition> transitions = new ArrayList<RegexEngine.NFA.Transition>();
    transitions.add( new RegexEngine.NFA.Transition("q8", "c", "q9") );
    transitions.add( new RegexEngine.NFA.Transition("q10", "eps", "q8") );
    transitions.add( new RegexEngine.NFA.Transition("q9", "eps", "q10") );

    assertEquals( "q8", cPlus.start );
    assertEquals( "q10", cPlus.end );
    assertEquals( true, cPlus.states.containsAll(states) );
    assertEquals( cPlus.transitions.size(), transitions.size() );

    // check if each transition is equal
    if ( cPlus.transitions.size() == transitions.size() ) {
        for ( int i=0; i < cPlus.transitions.size(); i++ ) {
            assertEquals( cPlus.transitions.get(i).transition, transitions.get(i).transition );
        }
    }
  }

  @Test
  public void test5_concatenate() {
    RegexEngine.NFA d = new RegexEngine.NFA('d');
    RegexEngine.NFA e = new RegexEngine.NFA('e');
    RegexEngine.NFA de = RegexEngine.NFA.concatenate(d,e);

    List<String> states = new ArrayList<String>();
    states.add("q11");
    states.add("q12");
    states.add("q13");
    states.add("q14");

    List<RegexEngine.NFA.Transition> transitions = new ArrayList<RegexEngine.NFA.Transition>();
    transitions.add( new RegexEngine.NFA.Transition("q11", "d", "q12") );
    transitions.add( new RegexEngine.NFA.Transition("q13", "e", "q14") );
    transitions.add( new RegexEngine.NFA.Transition("q12", "eps", "q13") );

    assertEquals( "q11", de.start );
    assertEquals( "q14", de.end );
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
  public void test6_alternate() {
    RegexEngine.NFA f = new RegexEngine.NFA('f');
    RegexEngine.NFA g = new RegexEngine.NFA('g');
    RegexEngine.NFA fORg = RegexEngine.NFA.alternate(f,g);

    List<String> states = new ArrayList<String>();
    states.add("q15");
    states.add("q16");
    states.add("q17");
    states.add("q18");
    states.add("q19");
    states.add("q20");

    List<RegexEngine.NFA.Transition> transitions = new ArrayList<RegexEngine.NFA.Transition>();
    transitions.add( new RegexEngine.NFA.Transition("q15", "f", "q16") );
    transitions.add( new RegexEngine.NFA.Transition("q17", "g", "q18") );
    transitions.add( new RegexEngine.NFA.Transition("q19", "eps", "q15") );
    transitions.add( new RegexEngine.NFA.Transition("q19", "eps", "q17") );
    transitions.add( new RegexEngine.NFA.Transition("q16", "eps", "q20") );
    transitions.add( new RegexEngine.NFA.Transition("q18", "eps", "q20") );

    assertEquals( "q19", fORg.start );
    assertEquals( "q20", fORg.end );
    assertEquals( true, fORg.states.containsAll(states) );
    assertEquals( fORg.transitions.size(), transitions.size() );

    // check if each transition is equal
    if ( fORg.transitions.size() == transitions.size() ) {
        for ( int i=0; i < fORg.transitions.size(); i++ ) {
            assertEquals( fORg.transitions.get(i).transition, transitions.get(i).transition );
        }
    }
  }
}