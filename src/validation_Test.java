import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;

// compile with: javac -cp .:junit-4.13.2.jar:hamcrest-core-1.3.jar *_Test.java
//     run with: java -cp .:junit-4.13.2.jar:hamcrest-core-1.3.jar org.junit.runner.JUnitCore validRegex_Test

public class validation_Test {

  @Test
  public void legalChar_Test() {
    Object[][] input =  {
      { 'a', true },
      { 'c', true },
      { 'z', true },
      { 'A', true },
      { 'C', true },
      { 'Z', true },
      { '0', true },
      { '9', true },
      { ' ', true },
      { '(', false },
      { ')', false },
      { '*', false },
      { '+', false },
      { '|', false },
      { '_', false },
      { '&', false },
    };

    for ( int i=0; i < input.length; i++ ) {
      assertEquals( input[i][1], RegexEngine.legalChar( String.valueOf(input[i][0]).charAt(0) ) );
    }
  }

  @Test
  public void legalSymbol_Test() {
    Object[][] input =  {
      { '(', true },
      { ')', true },
      { '*', true },
      { '+', true },
      { '|', true },
      { 'a', false },
      { 'c', false },
      { 'z', false },
      { 'A', false },
      { 'C', false },
      { 'Z', false },
      { '0', false },
      { '9', false },
      { ' ', false },
      { '_', false },
      { '&', false },
    };

    for ( int i=0; i < input.length; i++ ) {
      assertEquals( input[i][1], RegexEngine.legalSymbol( String.valueOf(input[i][0]).charAt(0) ) );
    }
  }

  @Test
  public void precedenceOf_Test() {
    Object[][] input =  {
      { '*', 2 },
      { '+', 2 },
      { '_', 1 },
      { '|', 0 },
      { 'a', 0 },
      { 'c', 0 },
      { 'z', 0 },
      { 'A', 0 },
      { 'C', 0 },
      { 'Z', 0 },
      { '0', 0 },
      { '9', 0 },
      { ' ', 0 },
    };

    for ( int i=0; i < input.length; i++ ) {
      assertEquals( input[i][1], RegexEngine.precedenceOf( String.valueOf(input[i][0]).charAt(0) ) );
    }
  }

  @Test
  public void legalRegexCharacters_Test() {
    Object[][] input = {
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
    };

    for ( int i=0; i < input.length; i++ ) {
      assertEquals( input[i][1], RegexEngine.legalRegexCharacters( String.valueOf(input[i][0]) ) );
    }
  }

  @Test
  public void legalBrackets_Test() {
    Object[][] input = {
      { "(a)", true },
      { "(a)c", true },
      { "(a*|b)+c", true },
      { "(a)(b)", true },
      { "(a|b)c", true },
      { "(a|b)c+(1f)", true },
      { "(a)(b)(c)(1)(2)(3)(d)(4)", true },
      { "(a", false },
      { "a)", false },
      { "((a))", false },
      { "(a b)(asda", false },
      { "(a b)asda)", false }
    };

    for ( int i=0; i < input.length; i++ ) {
      assertEquals( input[i][1], RegexEngine.legalBrackets( String.valueOf(input[i][0]) ) );
    }
  }

  @Test
  public void badSymbolUsage_Test() {
    Object[][] input = {
      { "(ab*)", false },
      { "(a|b)c", false },
      { "(a|b)c", false },
      { "(a|b+)*c", false },
      { "a|(bc)", false },
      { "a**", true },
      { "a*+", true },
      { "a++", true },
      { "+*", true },
      { "((a)b)", true },
      { "(a(b))", true },
      { "a|*", true },
      { "a|+", true },
      { "(a+b)*+", true }
    };

    for ( int i=0; i < input.length; i++ ) {
      assertEquals( input[i][1], RegexEngine.badSymbolUsage( String.valueOf(input[i][0]) ) );
    }
  }
}