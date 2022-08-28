import static org.junit.Assert.assertEquals;
import java.util.Arrays;
import java.util.Collection;
import org.junit.Test;

/* */
public class expression_Test {
  @Test
  public void addConcatenations_Test() {
    // input and expected output
    Object[][] input =  {
      { "abc", "a_b_c" },                                             // basic sequence test
      { "ab(c|d)*f", "a_b_(c|d)*_f" },                                // tests how it handles the other symbols
      { "a+b*c+", "a+_b*_c+" },                                       // tests sequence of alternating char and op 
      { "a+(b*c|de)+f+", "a+_(b*_c|d_e)+_f+" },                       // tests expression within brackets
      { "a(b|cd|e*f|g+h|i)(jk)*", "a_(b|c_d|e*_f|g+_h|i)_(j_k)*" },   // tests sequence of brackets (x)(y) 
      { "(a b)( *)", "(a_ _b)_( *)" }                                 // tests space as a character
    };
    
    // test each input against its expected output
    for ( int i=0; i < input.length; i++ ) {
      assertEquals( input[i][1], RegexEngine.addConcatenations( String.valueOf(input[i][0]) ) );
    }
  }

  @Test
  public void getPrecedence_Test() {
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
      assertEquals( input[i][1], RegexEngine.getPrecedence( String.valueOf(input[i][0]).charAt(0) ) );
    }
  }

  @Test
  public void toPostfix_Test() {
    Object[][] input =  {
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
    };
    
    for ( int i=0; i < input.length; i++ ) {
      assertEquals( input[i][1], RegexEngine.toPostfix( String.valueOf(input[i][0]) ) );
    }
  }
}