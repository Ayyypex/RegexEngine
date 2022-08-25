import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import static org.junit.Assert.assertEquals;

// compile with: javac -cp .:junit-4.13.2.jar:hamcrest-core-1.3.jar *_Test.java
//     run with: java -cp .:junit-4.13.2.jar:hamcrest-core-1.3.jar org.junit.runner.JUnitCore toPostfix_Test

@RunWith(Parameterized.class)
public class toPostfix_Test {
  @Parameters
  public static Collection<Object[]> data() {
    return Arrays.asList(new Object[][] {
              { "a+_b", "a+b_" },
              { "b*_a_b", "b*a_b_" },
              { "a|b", "ab|" },
              { "(a|b)", "ab|" },
              { "a_b_(c|d)*_f", "ab_cd|*_f_" },
              
              // these inputs are the output from addConcatenations() tests
              { "a_b_c", "ab_c_" },  
              { "a_b_(c|d)*_f", "ab_cd|*_f_" }, 
              { "a+_b*_c+", "a+b*_c+_" },   
              { "a+_(b*_c|d_e)+_f+", "a+b*c_de_|+_f+_" },   
              { "a_(b|c_d|e*_f|g+_h|i)_(j_k)*", "abcd_|e*f_|g+h_|i|_jk_*_" },
              { "(a_ _b)_( *)", "a _b_ *_" }
    });
  }

  private String pInput;
  private String pExpected;

  public toPostfix_Test(String input, String expected) {
    this.pInput = input;
    this.pExpected = expected;
  }

  @Test
  public void test() {
    assertEquals(pExpected, RegexEngine.toPostfix(pInput) );
  }
}