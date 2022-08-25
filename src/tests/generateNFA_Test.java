import java.util.List;
import java.util.ArrayList;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import org.junit.runner.OrderWith;
import org.junit.runner.manipulation.Alphanumeric;
import org.junit.Before;


// compile with: javac -cp .:junit-4.13.2.jar:hamcrest-core-1.3.jar generateNFA_Test.java
//     run with: java -cp .:junit-4.13.2.jar:hamcrest-core-1.3.jar org.junit.runner.JUnitCore generateNFA_Test

@OrderWith(Alphanumeric.class)
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
    states.add("q0");
    states.add("q1");
    states.add("q2");
    states.add("q3");
    states.add("q4");
    states.add("q5");

    // Transitions should be added in this order
    List<RegexEngine.NFA.Transition> transitions = new ArrayList<RegexEngine.NFA.Transition>();
    transitions.add( new RegexEngine.NFA.Transition("q0", "a", "q1") );
    transitions.add( new RegexEngine.NFA.Transition("q2", "b", "q3") );
    transitions.add( new RegexEngine.NFA.Transition("q1", "eps", "q2") );
    transitions.add( new RegexEngine.NFA.Transition("q4", "c", "q5") );
    transitions.add( new RegexEngine.NFA.Transition("q3", "eps", "q4") );
    
    // 
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
  }
}