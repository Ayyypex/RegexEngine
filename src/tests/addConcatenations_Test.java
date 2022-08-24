import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

// compile with: javac -cp .:junit-4.13.2.jar:hamcrest-core-1.3.jar *_Test.java
//     run with: java -cp .:junit-4.13.2.jar:hamcrest-core-1.3.jar org.junit.runner.JUnitCore addConcatenations_Test

@RunWith(Parameterized.class)
public class addConcatenations_Test {
  @Parameters
  public static Collection<Object[]> data() {
    return Arrays.asList(new Object[][] {
              { "abc", "a_b_c" },                                             // basic sequence test
              { "ab(c|d)*f", "a_b_(c|d)*_f" },                                // tests how it handles the other symbols
              { "a+b*c+", "a+_b*_c+" },                                       // tests sequence of alternating char and op 
              { "a+(b*c|de)+f+", "a+_(b*_c|d_e)+_f+" },                       // tests expression within brackets
              { "a(b|cd|e*f|g+h|i)(jk)*", "a_(b|c_d|e*_f|g+_h|i)_(j_k)*" }    // tests sequence of brackets (x)(y) 
    });
  }

  private String pInput;
  private String pExpected;

  public addConcatenations_Test(String input, String expected) {
    this.pInput = input;
    this.pExpected = expected;
  }

  @Test
  public void test() {
    assertEquals(pExpected, RegexEngine.addConcatenations(pInput) );
  }
}