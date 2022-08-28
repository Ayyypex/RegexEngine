import java.util.List;
import java.util.ArrayList;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import org.junit.Before;


// compile with: javac -cp .:junit-4.13.2.jar:hamcrest-core-1.3.jar generateNFA_Test.java
//     run with: java -cp .:junit-4.13.2.jar:hamcrest-core-1.3.jar org.junit.runner.JUnitCore generateNFA_Test

public class generateNFA_Test {
 
  @Before
  public void init() {
    RegexEngine.NFA.numberOfStates = 0;
  }

  @Test
  public void test1() {
    // NFA to test
    RegexEngine.NFA abc = RegexEngine.generateNFA("ab_c_");

    // States should be added in this order
    List<String> states = new ArrayList<String>();
    states.add("q0");   // a
    states.add("q1");
    states.add("q2");   // b
    states.add("q3");
    states.add("q4");   // c
    states.add("q5");

    // Transitions should be added in this order
    List<RegexEngine.NFA.Transition> transitions = new ArrayList<RegexEngine.NFA.Transition>();
    transitions.add( new RegexEngine.NFA.Transition("q0", "a", "q1") );     // a
    transitions.add( new RegexEngine.NFA.Transition("q2", "b", "q3") );     // b
    transitions.add( new RegexEngine.NFA.Transition("q1", "epsilon", "q2") );   // a_b
    transitions.add( new RegexEngine.NFA.Transition("q4", "c", "q5") );     // c
    transitions.add( new RegexEngine.NFA.Transition("q3", "epsilon", "q4") );   // a_b_c
    
    // check start and end states, all states, and number of transitions
    assertEquals( "q0", abc.start );
    assertEquals( "q5", abc.end );
    assertEquals( true, abc.states.containsAll(states) );
    assertEquals( abc.transitions.size(), transitions.size() );

    // check if each transition is equal
    if ( abc.transitions.size() == transitions.size() ) {
        for ( int i=0; i < abc.transitions.size(); i++ ) {
            assertEquals( abc.transitions.get(i).transition, transitions.get(i).transition );
        }
    }
  }

  @Test
  public void test2() {
    RegexEngine.NFA abcdf = RegexEngine.generateNFA("ab_cd|*_f_");

    List<String> states = new ArrayList<String>();
    states.add("q0");   // a
    states.add("q1");
    states.add("q2");   // b
    states.add("q3");
    states.add("q4");   // c
    states.add("q5");
    states.add("q6");   // d
    states.add("q7");
    states.add("q8");   // |
    states.add("q9");
    states.add("q10");  // *
    states.add("q11");
    states.add("q12");  // f
    states.add("q13");

    List<RegexEngine.NFA.Transition> transitions = new ArrayList<RegexEngine.NFA.Transition>();
    transitions.add( new RegexEngine.NFA.Transition("q0", "a", "q1") );       // a
    transitions.add( new RegexEngine.NFA.Transition("q2", "b", "q3") );       // b
    transitions.add( new RegexEngine.NFA.Transition("q1", "epsilon", "q2") );     // a_b
    transitions.add( new RegexEngine.NFA.Transition("q4", "c", "q5") );       // c
    transitions.add( new RegexEngine.NFA.Transition("q6", "d", "q7") );       // d
    transitions.add( new RegexEngine.NFA.Transition("q8", "epsilon", "q4") );     // c|d
    transitions.add( new RegexEngine.NFA.Transition("q8", "epsilon", "q6") );
    transitions.add( new RegexEngine.NFA.Transition("q5", "epsilon", "q9") );
    transitions.add( new RegexEngine.NFA.Transition("q7", "epsilon", "q9") );
    transitions.add( new RegexEngine.NFA.Transition("q10", "epsilon", "q8") );    // (c|d)*
    transitions.add( new RegexEngine.NFA.Transition("q9", "epsilon", "q10") );
    transitions.add( new RegexEngine.NFA.Transition("q10", "epsilon", "q11") );
    transitions.add( new RegexEngine.NFA.Transition("q3", "epsilon", "q10") );    // a_b_(c|d)*
    transitions.add( new RegexEngine.NFA.Transition("q12", "f", "q13") );     // f
    transitions.add( new RegexEngine.NFA.Transition("q11", "epsilon", "q12") );   // a_b_(c|d)*_f

    assertEquals( "q0", abcdf.start );
    assertEquals( "q13", abcdf.end );
    assertEquals( true, abcdf.states.containsAll(states) );
    assertEquals( abcdf.transitions.size(), transitions.size() );

    if ( abcdf.transitions.size() == transitions.size() ) {
        for ( int i=0; i < abcdf.transitions.size(); i++ ) {
            assertEquals( abcdf.transitions.get(i).transition, transitions.get(i).transition );
        }
    }
  }

  @Test
  public void test3() {
    RegexEngine.NFA abc = RegexEngine.generateNFA("a+b*_c+_");

    List<String> states = new ArrayList<String>();
    states.add("q0");   // a
    states.add("q1");
    states.add("q2");   // +
    states.add("q3");   // b
    states.add("q4");  
    states.add("q5");   // *
    states.add("q6");
    states.add("q7");   // c
    states.add("q8");    
    states.add("q9");   // +

    List<RegexEngine.NFA.Transition> transitions = new ArrayList<RegexEngine.NFA.Transition>();
    transitions.add( new RegexEngine.NFA.Transition("q0", "a", "q1") );      // a
    transitions.add( new RegexEngine.NFA.Transition("q2", "epsilon", "q0") );    // a+
    transitions.add( new RegexEngine.NFA.Transition("q1", "epsilon", "q2") );
    transitions.add( new RegexEngine.NFA.Transition("q3", "b", "q4") );      // b
    transitions.add( new RegexEngine.NFA.Transition("q5", "epsilon", "q3") );    // b*
    transitions.add( new RegexEngine.NFA.Transition("q4", "epsilon", "q5") );
    transitions.add( new RegexEngine.NFA.Transition("q5", "epsilon", "q6") );    
    transitions.add( new RegexEngine.NFA.Transition("q2", "epsilon", "q5") );    // a+_b*
    transitions.add( new RegexEngine.NFA.Transition("q7", "c", "q8") );      // c
    transitions.add( new RegexEngine.NFA.Transition("q9", "epsilon", "q7") );    // c+
    transitions.add( new RegexEngine.NFA.Transition("q8", "epsilon", "q9") );
    transitions.add( new RegexEngine.NFA.Transition("q6", "epsilon", "q7") );    // a+_b*_c+

    assertEquals( "q0", abc.start );
    assertEquals( "q9", abc.end );
    assertEquals( true, abc.states.containsAll(states) );
    assertEquals( abc.transitions.size(), transitions.size() );

    if ( abc.transitions.size() == transitions.size() ) {
        for ( int i=0; i < abc.transitions.size(); i++ ) {
            assertEquals( abc.transitions.get(i).transition, transitions.get(i).transition );
        }
    }
  }

  @Test
  public void test4() {
    RegexEngine.NFA abcdef = RegexEngine.generateNFA("a+b*c_de_|+_f+_");

    List<String> states = new ArrayList<String>();
    states.add("q0");   // a
    states.add("q1");
    states.add("q2");   // +
    states.add("q3");   // b
    states.add("q4");  
    states.add("q5");   // *
    states.add("q6");   
    states.add("q7");   // c
    states.add("q8");    
    states.add("q9");   // d
    states.add("q10");  
    states.add("q11");  // e
    states.add("q12");  
    states.add("q13");  // |
    states.add("q14"); 
    states.add("q15");  // +
    states.add("q16");  // f
    states.add("q17");  
    states.add("q18");  // +

    List<RegexEngine.NFA.Transition> transitions = new ArrayList<RegexEngine.NFA.Transition>();
    transitions.add( new RegexEngine.NFA.Transition("q0", "a", "q1") );      // a
    transitions.add( new RegexEngine.NFA.Transition("q2", "epsilon", "q0") );    // a+
    transitions.add( new RegexEngine.NFA.Transition("q1", "epsilon", "q2") );
    transitions.add( new RegexEngine.NFA.Transition("q3", "b", "q4") );      // b
    transitions.add( new RegexEngine.NFA.Transition("q5", "epsilon", "q3") );    // b*
    transitions.add( new RegexEngine.NFA.Transition("q4", "epsilon", "q5") );
    transitions.add( new RegexEngine.NFA.Transition("q5", "epsilon", "q6") );
    transitions.add( new RegexEngine.NFA.Transition("q7", "c", "q8") );      // c
    transitions.add( new RegexEngine.NFA.Transition("q6", "epsilon", "q7") );    // b*_c
    transitions.add( new RegexEngine.NFA.Transition("q9", "d", "q10") );      // d
    transitions.add( new RegexEngine.NFA.Transition("q11", "e", "q12") );    // e
    transitions.add( new RegexEngine.NFA.Transition("q10", "epsilon", "q11") );   // d_e
    transitions.add( new RegexEngine.NFA.Transition("q13", "epsilon", "q5") );   // b*_c|d_e
    transitions.add( new RegexEngine.NFA.Transition("q13", "epsilon", "q9") );
    transitions.add( new RegexEngine.NFA.Transition("q8", "epsilon", "q14") );
    transitions.add( new RegexEngine.NFA.Transition("q12", "epsilon", "q14") );
    transitions.add( new RegexEngine.NFA.Transition("q15", "epsilon", "q13") );  // (b*_c|d_e)+
    transitions.add( new RegexEngine.NFA.Transition("q14", "epsilon", "q15") );
    transitions.add( new RegexEngine.NFA.Transition("q2", "epsilon", "q13") );   // a+_(b*_c|d_e)+
    transitions.add( new RegexEngine.NFA.Transition("q16", "f", "q17") );    // f
    transitions.add( new RegexEngine.NFA.Transition("q18", "epsilon", "q16") );  // f+
    transitions.add( new RegexEngine.NFA.Transition("q17", "epsilon", "q18") );
    transitions.add( new RegexEngine.NFA.Transition("q15", "epsilon", "q16") );  // a+_(b*_c|d_e)+_f+

    assertEquals( "q0", abcdef.start );
    assertEquals( "q18", abcdef.end );
    assertEquals( true, abcdef.states.containsAll(states) );
    assertEquals( abcdef.transitions.size(), transitions.size() );

    if ( abcdef.transitions.size() == transitions.size() ) {
        for ( int i=0; i < abcdef.transitions.size(); i++ ) {
            assertEquals( abcdef.transitions.get(i).transition, transitions.get(i).transition );
        }
    }
  }
  
  @Test
  public void test5() {
    RegexEngine.NFA ab = RegexEngine.generateNFA("a _b_ *_");

    List<String> states = new ArrayList<String>();
    states.add("q0");   // a
    states.add("q1");
    states.add("q2");   // ' ' space character
    states.add("q3");   
    states.add("q4");   // b
    states.add("q5");   
    states.add("q6");   // ' ' space character
    states.add("q7");    
    states.add("q8");   // *
    states.add("q9");

    List<RegexEngine.NFA.Transition> transitions = new ArrayList<RegexEngine.NFA.Transition>();
    transitions.add( new RegexEngine.NFA.Transition("q0", "a", "q1") );      // a
    transitions.add( new RegexEngine.NFA.Transition("q2", " ", "q3") );      // ' ' space character
    transitions.add( new RegexEngine.NFA.Transition("q1", "epsilon", "q2") );    // a_
    transitions.add( new RegexEngine.NFA.Transition("q4", "b", "q5") );      // b
    transitions.add( new RegexEngine.NFA.Transition("q3", "epsilon", "q4") );    // a_ _b
    transitions.add( new RegexEngine.NFA.Transition("q6", " ", "q7") );      // ' ' space character
    transitions.add( new RegexEngine.NFA.Transition("q8", "epsilon", "q6") );    //  *
    transitions.add( new RegexEngine.NFA.Transition("q7", "epsilon", "q8") );     
    transitions.add( new RegexEngine.NFA.Transition("q8", "epsilon", "q9") );     
    transitions.add( new RegexEngine.NFA.Transition("q5", "epsilon", "q8") );    // (a_ _b)_( *)


    assertEquals( "q0", ab.start );
    assertEquals( "q9", ab.end );
    assertEquals( true, ab.states.containsAll(states) );
    assertEquals( ab.transitions.size(), transitions.size() );

    if ( ab.transitions.size() == transitions.size() ) {
        for ( int i=0; i < ab.transitions.size(); i++ ) {
            assertEquals( ab.transitions.get(i).transition, transitions.get(i).transition );
        }
    }
  }
}