import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Stack;

// compiled with: javac RegexEngine.java
//      run with: java RegexEngine (-v for verbose mode)

public class RegexEngine {
    // main function
    public static void main(String[] args) 
        throws IOException 
    {
        BufferedReader reader = new BufferedReader( new InputStreamReader(System.in) );

        // get regular expression to test
        String regex = reader.readLine();
        if ( !validRegex(regex) ) {
            System.out.println("Error: This input is invalid: " + regex);
            System.exit(1);
        }

        // convert expression to postfix notation
        String infix = addConcatenations(regex);
        String postfix = toPostfix(infix);

        // NFA = createNFA(postfix);
        // if(verbose) System.out.println( getTable(NFA) ); 
        System.out.println("ready");
        
        // read each line until ctrl+c
        while (true) {
            String line = reader.readLine();

            // if ( simulateNFA(NFA, line) ) System.out.println("true");
            // else System.out.println("false");
        }
    }

    // checks if regex string is acceptable
    static boolean validRegex(String regex) {
        for ( int i=0; i < regex.length(); i++ ) {
            char ch = regex.charAt(i);
            if ( !validChar(ch) && !validSymbol(ch) ) {
                return false;
            }
        }
        return true;
    }

    // checks if character is a valid char
    static boolean validChar(char ch) {
        if ( !Character.isLetterOrDigit(ch) && ch != ' ' ) {
            return false;
        }
        return true;
    }

    // checks if character is a valid symbol
    static boolean validSymbol(char ch) {
        if ( ch == '|' || ch == '*' || ch == '+' || ch == '(' || ch == ')' ) {
            return true;
        }
        return false;
    }

    // converts infix expression to postfix
    static String toPostfix(String infix) {
        Stack<Character> st = new Stack<>();
        String postfix = "";

        // iterate over every character
        for ( int i=0; i < infix.length(); i++ ) {
            char ch = infix.charAt(i);

            // append 
            if ( validChar(ch) ) {
                postfix += ch;
            }

            // push onto stack
            else if ( ch == '(' ) {
                st.push(ch);
            }

            // append operators within brackets
            else if ( ch == ')' ) {
                while ( !st.empty() && st.peek() != '(' ) {
                    postfix += st.pop();
                }
                // pop ( off
                st.pop();
            }

            // push operators: | + * _
            else {
                // if top stack element precedence is higher than current element, append top
                while ( !st.empty() && st.peek() != '(' && precedenceOf(ch) <= precedenceOf(st.peek()) ) {
                    postfix += st.pop();
                }
                st.push(ch);
            }
        }

        // append any remaining operators on stack
        while ( !st.empty() ) {
            postfix += st.pop();
        }

        return postfix;
    }

    // returns the precedence of a character
    static int precedenceOf(char ch) {
        if ( ch == '*' || ch == '+' ) {
            return 2;
        }
        else if ( ch  == '_' ) {
            return 1;
        }
        return 0;
    }

    // adds concatenation symbols to an infix string
    static String addConcatenations(String infix) {
        String output = "";

        // iterate over every character
        for ( int i=0; i < infix.length(); i++ ) {
            char ch = infix.charAt(i);
            output += ch;

            // if character is not ( or |, then investigate the next
            if ( ch != '(' && ch != '|' && i < infix.length()-1 ) {
                char next = infix.charAt(i+1);
                // if next character is not a symbol (except left bracket '(' ) then add concatenation symbol
                if ( validChar(next) ||  next == '(' ) {       
                    output += '_';
                }
            }
        }

        return output;
    }
}