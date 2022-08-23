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
              { "abc", "a_b_c" },
              { "ab(c|d)*f", "a_b_(c|d)*_f" }, 
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