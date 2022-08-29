import java.util.Arrays;
import java.util.Collection;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests legalChar(), legalSymbol, legalRegexCharacters(), legalBrackets(), checkSymbolUsage(),
 * and checkRegex().
 * 
 * These tests are grouped together because they all involve functions used in the validation 
 * validation of input.
 */
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
  public void checkSymbolUsage_Test() {
    Object[][] input = {
      { "(ab*)", true },
      { "(a|b)c", true },
      { "(a|b)c", true },
      { "(a|b+)*c", true },
      { "a|(bc)", true },
      { "a**", false },
      { "a*+", false },
      { "a++", false },
      { "+*", false },
      { "((a)b)", false },
      { "(a(b))", false },
      { "a|*", false },
      { "a|+", false },
      { "(a+b)*+", false }
    };

    for ( int i=0; i < input.length; i++ ) {
      assertEquals( input[i][1], RegexEngine.checkSymbolUsage( String.valueOf(input[i][0]) ) );
    }
  }

  @Test
  public void checkRegex_Test() {
    Object[][] input = {
      { "", false },
      { "a-b", false },
      { "a,b", false },
      { "(ab", false },
      { "ab)", false },
      { "a?ab", false },
      { "a||b", false },
      { "a?ab", false },
      { "a?ab", false },
      { "a?ab", false },
      { "a+b", true },
      { "a*b|c", true },
      { "(a+b)*|(cd)+", true },
      { "abc", true },
      { "ab(c|d)*f*", true },
      { "a+b*c", true },
      { "a+(b*c|de)+f+", true },
      { "a(b|cd|e*f|g+h|i)(jk)*", true },
      { "(a b)( *)", true },
    };

    for ( int i=0; i < input.length; i++ ) {
      assertEquals( input[i][1], RegexEngine.checkRegex( String.valueOf(input[i][0]) ) );
    }
  }
}