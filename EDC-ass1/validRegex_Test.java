import static org.junit.Assert.assertEquals;
import org.junit.Test;

// compile with: javac -cp .:junit-4.13.2.jar:hamcrest-core-1.3.jar *_Test.java
//     run with: java -cp .:junit-4.13.2.jar:hamcrest-core-1.3.jar org.junit.runner.JUnitCore validRegex_Test

public class validRegex_Test {
  @Test
  public void test1() {
    assertEquals( true, RegexEngine.validRegex("abcde fghijklmnopqr stuvwxyz") );
  }

  @Test
  public void test2() {
    assertEquals( true, RegexEngine.validRegex("ABCD EFGHI JKLMNOPQR STUVWXYZ") );
  }

  @Test
  public void test3() {
    assertEquals( true, RegexEngine.validRegex("012345 6789") );
  }

  @Test
  public void test4() {
    assertEquals( true, RegexEngine.validRegex("( | * + )") );
  }

  @Test
  public void test5() {
    assertEquals( false, RegexEngine.validRegex("Hello\nworld") );
  }

  @Test
  public void test6() {
    assertEquals( true, RegexEngine.validRegex("(ab)*|c+") );
  }

  @Test
  public void test7() {
    assertEquals( false, RegexEngine.validRegex("(ab)*|c-") );
  }

  @Test
  public void test8() {
    assertEquals( true, RegexEngine.validRegex("gray|grey") );
  }

  @Test
  public void test9() {
    assertEquals( false, RegexEngine.validRegex("gr[ae]y") );
  }

  @Test
  public void test10() {
    assertEquals( false, RegexEngine.validRegex("colou?r") );
  }

  @Test
  public void test11() {
    assertEquals( true, RegexEngine.validRegex("go+gle") );
  }

  @Test
  public void test12() {
    assertEquals( false, RegexEngine.validRegex("colou?r") );
  }

  @Test
  public void test13() {
    assertEquals( false, RegexEngine.validRegex("z{3,6}") );
  }

  @Test
  public void test14() {
    assertEquals( true, RegexEngine.validRegex("(a|b)(c|d") );
  }

  @Test
  public void test15() {
    assertEquals( true, RegexEngine.validRegex("colou?r") );
  }
}