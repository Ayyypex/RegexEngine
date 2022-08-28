import java.util.List;
import java.util.ArrayList;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import org.junit.Before;

/* */
public class simulateNFA_Test {
  @Before
  public void init() {
    RegexEngine.NFA.numberOfStates = 0;
  }
  
  @Test
  public void test1() {
    // create NFA to test
    String regex = "a";
    regex = RegexEngine.addConcatenations(regex);
    String postfix = RegexEngine.toPostfix(regex);
    RegexEngine.NFA nfa = RegexEngine.generateNFA(postfix);

    // input and expected output
    Object[][] input =  {
        { "a", true },
        { "b", false },
        { " ", false },
        { "a1", false },
        { "aa", false },
        { "a ", false },
        { " a", false },
        { "", false },
        { " a ", false }
    };

    // test each input against its expected output
    for ( int i=0; i < input.length; i++ ) {
        assertEquals( input[i][1], RegexEngine.simulateNFA( nfa, String.valueOf(input[i][0]) ) );
    }
  }

  @Test
  public void test2() {
    String regex = "abc";
    regex = RegexEngine.addConcatenations(regex);
    String postfix = RegexEngine.toPostfix(regex);
    RegexEngine.NFA nfa = RegexEngine.generateNFA(postfix);

    Object[][] input =  {
        { "abc", true },
        { "abb", false },
        { "ab ", false },
        { "acb", false },
        { "a1bc", false },
        { "", false },
        { "a b c", false }
    };

    for ( int i=0; i < input.length; i++ ) {
        assertEquals( input[i][1], RegexEngine.simulateNFA( nfa, String.valueOf(input[i][0]) ) );
    }
  }

  @Test
  public void test3() {
    String regex = "a1d2";
    regex = RegexEngine.addConcatenations(regex);
    String postfix = RegexEngine.toPostfix(regex);
    RegexEngine.NFA nfa = RegexEngine.generateNFA(postfix);

    Object[][] input =  {
        { "a1d2", true },
        { "ald2", false },
        { "a d2 ", false },
        { "2d1a", false },
        { "41d2", false },
        { "", false },
        { "a 1 d 2", false }
    };

    for ( int i=0; i < input.length; i++ ) {
        assertEquals( input[i][1], RegexEngine.simulateNFA( nfa, String.valueOf(input[i][0]) ) );
    }
  }

  @Test
  public void test4() {
    String regex = "a Z 5";
    regex = RegexEngine.addConcatenations(regex);
    String postfix = RegexEngine.toPostfix(regex);
    RegexEngine.NFA nfa = RegexEngine.generateNFA(postfix);

    Object[][] input =  {
        { "a Z 5", true },
        { "aZ5", false },
        { "a Z5 ", false },
        { "a Z 5 ", false },
        { "5az", false },
        { "", false },
        { "a Z  5", false }
    };

    for ( int i=0; i < input.length; i++ ) {
        assertEquals( input[i][1], RegexEngine.simulateNFA( nfa, String.valueOf(input[i][0]) ) );
    }
  }
  
  @Test
  public void test5() {
    String regex = "a*";
    regex = RegexEngine.addConcatenations(regex);
    String postfix = RegexEngine.toPostfix(regex);
    RegexEngine.NFA nfa = RegexEngine.generateNFA(postfix);

    Object[][] input =  {
        { "", true },
        { "a", true },
        { "aaa", true },
        { "aaaaaaaaaaaaaaaaa", true },
        { " aaa", false },
        { "aa1aa", false },
        { "aa aa", false },
        { "  ", false },
        { "a*", false }
    };

    for ( int i=0; i < input.length; i++ ) {
        assertEquals( input[i][1], RegexEngine.simulateNFA( nfa, String.valueOf(input[i][0]) ) );
    }
  }

  @Test
  public void test6() {
    String regex = "a1*";
    regex = RegexEngine.addConcatenations(regex);
    String postfix = RegexEngine.toPostfix(regex);
    RegexEngine.NFA nfa = RegexEngine.generateNFA(postfix);

    Object[][] input =  {
        { "a", true },
        { "a1", true },
        { "a11111111", true },
        { "", false },
        { "aa1", false },
        { "aa11aa", false },
        { " a11", false },
        { "a1*", false }
    };

    for ( int i=0; i < input.length; i++ ) {
        assertEquals( input[i][1], RegexEngine.simulateNFA( nfa, String.valueOf(input[i][0]) ) );
    }
  }

  @Test
  public void test7() {
    String regex = "a* *";
    regex = RegexEngine.addConcatenations(regex);
    String postfix = RegexEngine.toPostfix(regex);
    RegexEngine.NFA nfa = RegexEngine.generateNFA(postfix);

    Object[][] input =  {
        { "a ", true },
        { "aaaaa ", true },
        { "a        ", true },
        { "a", true },
        { " ", true },
        { "aaaaaaaaaa", true },
        { "          ", true },
        { "", true },
        { "aa   a", false },
        { " aa          ", false },
        { "aa aa", false },
        { "aa        a      ", false },
        { "a* *", false }
    };

    for ( int i=0; i < input.length; i++ ) {
        assertEquals( input[i][1], RegexEngine.simulateNFA( nfa, String.valueOf(input[i][0]) ) );
    }
  }
  
  @Test
  public void test8() {
    String regex = "a+";
    regex = RegexEngine.addConcatenations(regex);
    String postfix = RegexEngine.toPostfix(regex);
    RegexEngine.NFA nfa = RegexEngine.generateNFA(postfix);

    Object[][] input =  {
        { "a", true },
        { "aaa", true },
        { "aaaaaaaaaaaaaaaaa", true },
        { "", false },
        { " ", false },
        { "aa aa", false },
        { "aa1a", false },
        { "a+", false }
    };

    for ( int i=0; i < input.length; i++ ) {
        assertEquals( input[i][1], RegexEngine.simulateNFA( nfa, String.valueOf(input[i][0]) ) );
    }
  }

  @Test
  public void test9() {
    String regex = "a1+";
    regex = RegexEngine.addConcatenations(regex);
    String postfix = RegexEngine.toPostfix(regex);
    RegexEngine.NFA nfa = RegexEngine.generateNFA(postfix);

    Object[][] input =  {
        { "a1", true },
        { "a1111", true },
        { "aaaaaaaaaaaaaaaaa1", false },
        { "a", false },
        { "a ", false },
        { "aa 11", false },
        { "111a", false },
        { "", false },
        { "a1+", false }
    };

    for ( int i=0; i < input.length; i++ ) {
        assertEquals( input[i][1], RegexEngine.simulateNFA( nfa, String.valueOf(input[i][0]) ) );
    }
  }

  @Test
  public void test10() {
    String regex = "a+ +";
    regex = RegexEngine.addConcatenations(regex);
    String postfix = RegexEngine.toPostfix(regex);
    RegexEngine.NFA nfa = RegexEngine.generateNFA(postfix);

    Object[][] input =  {
        { "a ", true },
        { "aaaaa ", true },
        { "a        ", true },
        { "aaaa            ", true },
        { "a", false },
        { " ", false },
        { "", false },
        { "aa        a      ", false },
        { "a+ +", false }
    };

    for ( int i=0; i < input.length; i++ ) {
        assertEquals( input[i][1], RegexEngine.simulateNFA( nfa, String.valueOf(input[i][0]) ) );
    }
  }

  @Test
  public void test11() {
    String regex = "a|b";
    regex = RegexEngine.addConcatenations(regex);
    String postfix = RegexEngine.toPostfix(regex);
    RegexEngine.NFA nfa = RegexEngine.generateNFA(postfix);

    Object[][] input =  {
        { "a", true },
        { "b", true },
        { "ab", false },
        { "aa", false },
        { "bb", false },
        { " ", false },
        { "", false },
        { "a|b", false }
    };

    for ( int i=0; i < input.length; i++ ) {
        assertEquals( input[i][1], RegexEngine.simulateNFA( nfa, String.valueOf(input[i][0]) ) );
    }
  }

  @Test
  public void test12() {
    String regex = "a|b| |c";
    regex = RegexEngine.addConcatenations(regex);
    String postfix = RegexEngine.toPostfix(regex);
    RegexEngine.NFA nfa = RegexEngine.generateNFA(postfix);

    Object[][] input =  {
        { "a", true },
        { "b", true },
        { " ", true },
        { "c", true },
        { "ab c", false },
        { "", false },
        { "ab", false },
        { " c", false },
        { "aa", false },
        { "a|b| |c", false }
    };

    for ( int i=0; i < input.length; i++ ) {
        assertEquals( input[i][1], RegexEngine.simulateNFA( nfa, String.valueOf(input[i][0]) ) );
    }
  }

  @Test
  public void test13() {
    String regex = "a|b*";
    regex = RegexEngine.addConcatenations(regex);
    String postfix = RegexEngine.toPostfix(regex);
    RegexEngine.NFA nfa = RegexEngine.generateNFA(postfix);

    Object[][] input =  {
        { "a", true },
        { "b", true },
        { "", true },
        { "bbbbb", true },
        { "aaa", false },
        { "abbb", false },
        { "a|b*", false }
    };

    for ( int i=0; i < input.length; i++ ) {
        assertEquals( input[i][1], RegexEngine.simulateNFA( nfa, String.valueOf(input[i][0]) ) );
    }
  }

  @Test
  public void test14() {
    String regex = "a|b+";
    regex = RegexEngine.addConcatenations(regex);
    String postfix = RegexEngine.toPostfix(regex);
    RegexEngine.NFA nfa = RegexEngine.generateNFA(postfix);

    Object[][] input =  {
        { "a", true },
        { "b", true },
        { "bbbbb", true },
        { "", false },
        { "aaa", false },
        { "abbb", false },
        { "a|b+", false }
    };

    for ( int i=0; i < input.length; i++ ) {
        assertEquals( input[i][1], RegexEngine.simulateNFA( nfa, String.valueOf(input[i][0]) ) );
    }
  }

  @Test
  public void test15() {
    String regex = "(a)(b)";
    regex = RegexEngine.addConcatenations(regex);
    String postfix = RegexEngine.toPostfix(regex);
    RegexEngine.NFA nfa = RegexEngine.generateNFA(postfix);

    Object[][] input =  {
        { "ab", true },
        { "a", false },
        { "b", false },
        { "", false },
        { "aaa", false },
        { "aabb", false },
        { "(a)(b)", false }
    };

    for ( int i=0; i < input.length; i++ ) {
        assertEquals( input[i][1], RegexEngine.simulateNFA( nfa, String.valueOf(input[i][0]) ) );
    }
  }

  @Test
  public void test16() {
    String regex = "(abc)(d e)f(1)";
    regex = RegexEngine.addConcatenations(regex);
    String postfix = RegexEngine.toPostfix(regex);
    RegexEngine.NFA nfa = RegexEngine.generateNFA(postfix);

    Object[][] input =  {
        { "abcd ef1", true },
        { "abc", false },
        { "d e", false },
        { "f", false },
        { "1", false },
        { "", false },
        { "abcdef1", false },
        { "abcf1", false },
        { "(abc)(d e)f(1)", false }
    };

    for ( int i=0; i < input.length; i++ ) {
        assertEquals( input[i][1], RegexEngine.simulateNFA( nfa, String.valueOf(input[i][0]) ) );
    }
  }

  @Test
  public void test17() {
    String regex = "(ab)*";
    regex = RegexEngine.addConcatenations(regex);
    String postfix = RegexEngine.toPostfix(regex);
    RegexEngine.NFA nfa = RegexEngine.generateNFA(postfix);

    Object[][] input =  {
        { "", true },
        { "ab", true },
        { "abab", true },
        { "abababababab", true },
        { "abababababa", false },
        { "a", false },
        { "b", false },
        { "aabbb", false },
        { "abbbababbaaa", false },
        { "babababa", false },
        { " ", false },
        { "(ab)*", false }
    };

    for ( int i=0; i < input.length; i++ ) {
        assertEquals( input[i][1], RegexEngine.simulateNFA( nfa, String.valueOf(input[i][0]) ) );
    }
  }

  @Test
  public void test18() {
    String regex = "(ab)+";
    regex = RegexEngine.addConcatenations(regex);
    String postfix = RegexEngine.toPostfix(regex);
    RegexEngine.NFA nfa = RegexEngine.generateNFA(postfix);

    Object[][] input =  {
        { "ab", true },
        { "abab", true },
        { "abababababab", true },
        { "", false },
        { "abababababa", false },
        { "a", false },
        { "b", false },
        { "aabbb", false },
        { "abbbababbaaa", false },
        { "babababa", false },
        { " ", false },
        { "(ab)+", false }
    };

    for ( int i=0; i < input.length; i++ ) {
        assertEquals( input[i][1], RegexEngine.simulateNFA( nfa, String.valueOf(input[i][0]) ) );
    }
  }

  @Test
  public void test19() {
    String regex = "(a|b)| c";
    regex = RegexEngine.addConcatenations(regex);
    String postfix = RegexEngine.toPostfix(regex);
    RegexEngine.NFA nfa = RegexEngine.generateNFA(postfix);

    Object[][] input =  {
        { "a", true },
        { "b", true },
        { " c", true },
        { "", false },
        { "ab c", false },
        { "a c", false },
        { "b c", false },
        { " ", false },
        { "(a|b)| c", false }
    };

    for ( int i=0; i < input.length; i++ ) {
        assertEquals( input[i][1], RegexEngine.simulateNFA( nfa, String.valueOf(input[i][0]) ) );
    }
  }

  @Test
  public void test20() {
    String regex = "(ab)*(c )*";
    regex = RegexEngine.addConcatenations(regex);
    String postfix = RegexEngine.toPostfix(regex);
    RegexEngine.NFA nfa = RegexEngine.generateNFA(postfix);

    Object[][] input =  {
        { "", true },
        { "ab", true },
        { "c ", true },
        { "abc ", true },
        { "abababab", true },
        { "c c c c c c ", true },
        { "ababc c c ", true },
        { "abc", false },
        { "abc ab", false },
        { "ab c", false },
        { "bababa c c", false },
        { "(ab)*(c )*", false }
    };

    for ( int i=0; i < input.length; i++ ) {
        assertEquals( input[i][1], RegexEngine.simulateNFA( nfa, String.valueOf(input[i][0]) ) );
    }
  }

  @Test
  public void test21() {
    String regex = "(ab)+(c )+";
    regex = RegexEngine.addConcatenations(regex);
    String postfix = RegexEngine.toPostfix(regex);
    RegexEngine.NFA nfa = RegexEngine.generateNFA(postfix);

    Object[][] input =  {
        { "abc ", true },
        { "abababc ", true },
        { "abc c c c ", true },
        { "ababababc c c c c ", true },
        { "", false },
        { "ab", false },
        { "c ", false },
        { "ab c", false },
        { "bababa c c", false },
        { "(ab)+(c )+", false }
    };

    for ( int i=0; i < input.length; i++ ) {
        assertEquals( input[i][1], RegexEngine.simulateNFA( nfa, String.valueOf(input[i][0]) ) );
    }
  }

  @Test
  public void test22() {
    String regex = "(ab)*(c )+";
    regex = RegexEngine.addConcatenations(regex);
    String postfix = RegexEngine.toPostfix(regex);
    RegexEngine.NFA nfa = RegexEngine.generateNFA(postfix);

    Object[][] input =  {
        { "abc ", true },
        { "c ", true },
        { "abc c c c ", true },
        { "ababababc c c c c ", true },
        { "", false },
        { "ab", false },
        { "ab c", false },
        { "bababa c c", false },
        { "(ab)*(c )+", false }
    };

    for ( int i=0; i < input.length; i++ ) {
        assertEquals( input[i][1], RegexEngine.simulateNFA( nfa, String.valueOf(input[i][0]) ) );
    }
  }

  @Test
  public void test23() {
    String regex = "(ab)*|(c )+";
    regex = RegexEngine.addConcatenations(regex);
    String postfix = RegexEngine.toPostfix(regex);
    RegexEngine.NFA nfa = RegexEngine.generateNFA(postfix);

    Object[][] input =  {
        { "", true },
        { "ab", true },
        { "ababab", true },
        { "c ", true },
        { "c c c c ", true },
        { "abc ", false },
        { "abc c c ", false },
        { "ababababc c c ", false },
        { "(ab)*(c )+", false }
    };

    for ( int i=0; i < input.length; i++ ) {
        assertEquals( input[i][1], RegexEngine.simulateNFA( nfa, String.valueOf(input[i][0]) ) );
    }
  }
  
  @Test
  public void test24() {
    String regex = "ab(c|d)*f";
    regex = RegexEngine.addConcatenations(regex);
    String postfix = RegexEngine.toPostfix(regex);
    RegexEngine.NFA nfa = RegexEngine.generateNFA(postfix);

    Object[][] input =  {
        { "abf", true },
        { "abcf", true },
        { "abdf", true },
        { "abcccccf", true },
        { "abddddf", true },
        { "abcdf", true },
        { "abccddf", true },
        { "abcddcdcdcdccddf", true },
        { "", false },
        { "ab f", false },
        { "abcdff", false },
        { "ababcdf", false },
        { "ab(c|d)*f", false }
    };

    for ( int i=0; i < input.length; i++ ) {
        assertEquals( input[i][1], RegexEngine.simulateNFA( nfa, String.valueOf(input[i][0]) ) );
    }
  }

  @Test
  public void test25() {
    String regex = "a+b*c+";
    regex = RegexEngine.addConcatenations(regex);
    String postfix = RegexEngine.toPostfix(regex);
    RegexEngine.NFA nfa = RegexEngine.generateNFA(postfix);

    Object[][] input =  {
        { "ac", true },
        { "abc", true },
        { "aac", true },
        { "acc", true },
        { "aaaccc", true },
        { "abbbc", true },
        { "aaabbbccc", true },
        { "", false },
        { "a", false },
        { "b", false },
        { "c", false },
        { "ab", false },
        { "bc", false },
        { "a+b*c+", false }
    };

    for ( int i=0; i < input.length; i++ ) {
        assertEquals( input[i][1], RegexEngine.simulateNFA( nfa, String.valueOf(input[i][0]) ) );
    }
  }

  @Test
  public void test26() {
    String regex = "a+(b*c|de)+f+";
    regex = RegexEngine.addConcatenations(regex);
    String postfix = RegexEngine.toPostfix(regex);
    RegexEngine.NFA nfa = RegexEngine.generateNFA(postfix);

    Object[][] input =  {
        { "acf", true },
        { "abcf", true },
        { "adef", true },
        { "aabbbcfff", true },
        { "aabbbcbcdedefff", true },
        { "aacccccf", true },
        { "aadededecbcfff", true },
        { "", false },
        { "adf", false },
        { "aef", false },
        { "acdcf", false },
        { "adedebf", false },
        { "aedf", false },
        { "a+(b*c|de)+f+", false }
    };

    for ( int i=0; i < input.length; i++ ) {
        assertEquals( input[i][1], RegexEngine.simulateNFA( nfa, String.valueOf(input[i][0]) ) );
    }
  }
  
  @Test
  public void test27() {
    String regex = "a(b|cd|e*f|g+h|i)(jk)*";
    regex = RegexEngine.addConcatenations(regex);
    String postfix = RegexEngine.toPostfix(regex);
    RegexEngine.NFA nfa = RegexEngine.generateNFA(postfix);

    Object[][] input =  {
        { "ab", true },
        { "acd", true },
        { "af", true },
        { "aef", true },
        { "aeeef", true },
        { "agh", true },
        { "agggh", true },
        { "ai", true },
        { "abjk", true },
        { "acdjkjkjk", true },
        { "aeeeeef", true },
        { "", false },
        { "ac", false },
        { "ag", false },
        { "ah", false },
        { "a", false },
        { "ajk", false },
        { "abcdefghijk", false },
        { "a(b|cd|e*f|g+h|i)(jk)*", false }
    };

    for ( int i=0; i < input.length; i++ ) {
        assertEquals( input[i][1], RegexEngine.simulateNFA( nfa, String.valueOf(input[i][0]) ) );
    }
  }

  @Test
  public void test28() {
    String regex = "(a b)( *)";
    regex = RegexEngine.addConcatenations(regex);
    String postfix = RegexEngine.toPostfix(regex);
    RegexEngine.NFA nfa = RegexEngine.generateNFA(postfix);

    Object[][] input =  {
        { "a b", true },
        { "a b ", true },
        { "a b     ", true },
        { "", false },
        { "ab ", false },
        { "ag", false },
        { "(a b)( *)", false }
    };

    for ( int i=0; i < input.length; i++ ) {
        assertEquals( input[i][1], RegexEngine.simulateNFA( nfa, String.valueOf(input[i][0]) ) );
    }
  }
}