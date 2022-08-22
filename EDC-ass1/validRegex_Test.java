import static org.junit.Assert.assertEquals;
import org.junit.Test;

// compile with: javac -cp .:junit-4.13.2.jar:hamcrest-core-1.3.jar *_Test.java
//     run with: java -cp .:junit-4.13.2.jar:hamcrest-core-1.3.jar org.junit.runner.JUnitCore validRegex_Test

public class validRegex_Test {
  @Test
  public void test1() {
    boolean valid = RegexEngine.validRegex("1+2+3");
    assertEquals(true, valid);
  }
}