import java.util.List;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

// compile with: javac -cp .:junit-4.13.2.jar:hamcrest-core-1.3.jar *_Test.java
//     run with: java -cp .:junit-4.13.2.jar:hamcrest-core-1.3.jar org.junit.runner.JUnitCore NFA_Test

public class NFA_Test {

  @Test
  public void newNFA_test() {
    RegexEngine.NFA aTest = new RegexEngine.NFA('a');

    List<String> statesTest = new ArrayList<String>();
    statesTest.add("q0");
    statesTest.add("q1");

    List<RegexEngine.NFA.Transition> transitionsTest = new ArrayList<RegexEngine.NFA.Transition>();
    transitionsTest.add( new RegexEngine.NFA.Transition("q0", "a", "q1") );

    assertEquals( "q0", aTest.start );
    assertEquals( "q1", aTest.end );
    assertEquals( true, aTest.states.containsAll(statesTest) );
    assertEquals( aTest.transitions.size(), transitionsTest.size() );

    // check if each transition is equal
    if ( aTest.transitions.size() == transitionsTest.size() ) {
        for ( int i=0; i<aTest.transitions.size(); i++ ) {
            assertEquals( aTest.transitions.get(i).all, transitionsTest.get(i).all );
        }
    }
  }

  public void newState_test() {
    assertEquals( true, RegexEngine.validRegex("abcde fghijklmnopqr stuvwxyz") );
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
    assertEquals( true, RegexEngine.validRegex("012345 6789") );
  }

  @Test
  public void alternate_test() {
    assertEquals( true, RegexEngine.validRegex("012345 6789") );
  }
}