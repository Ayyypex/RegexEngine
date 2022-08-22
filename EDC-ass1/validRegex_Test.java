import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

// compile with: javac -cp .:junit-4.13.2.jar:hamcrest-core-1.3.jar *_Test.java
//     run with: java -cp .:junit-4.13.2.jar:hamcrest-core-1.3.jar org.junit.runner.JUnitCore validRegex_Test

@RunWith(Parameterized.class)
public class validRegex_Test {
  @Parameters
  public static Collection<Object[]> data() {
    return Arrays.asList(new Object[][] {
              { "abcde fghijklmnopqr stuvwxyz", true },
              { "ABCD EFGHI JKLMNOPQR STUVWXYZ", true },
              { "012 345 6789", true },
              { "( | * + )", true },
              { "Hello\nworld", false },
              { "(a3b)*|c+", true },
              { "(ab)*|c-", false },
              { "gray|grey", true },
              { "gr[ae]y", false },
              { "colou2?r", false },
              { "go+gle", true },
              { "z{3,6}", false },
              { "(a|b)(c|d)", true },
              { "(a|b|c)*     1+ | 9", true },
              { "Fi(ni+)*|te 5(t*a+t(e)) Au(t)(0|m|a|t*|(a+))", true }
    });
  }

  private String pInput;
  private boolean pExpected;

  public validRegex_Test(String input, boolean expected) {
    this.pInput = input;
    this.pExpected = expected;
  }

  @Test
  public void test() {
    assertEquals(pExpected, RegexEngine.validRegex(pInput) );
  }
}