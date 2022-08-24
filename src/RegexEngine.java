import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Stack;

// compiled with: javac RegexEngine.java
//      run with: java RegexEngine (-v for verbose mode)

public class RegexEngine {
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

        // add concatenation symbols and convert expression to postfix notation
        String infix = addConcatenations(regex);
        String postfix = toPostfix(infix);

        // generate NFA structure
        NFA finalNFA = generateNFA(postfix);

        // if(verbose) printTable(NFA) ; 
        System.out.println("ready");
        
        // read each line until ctrl+c
        while (true) {
            String line = reader.readLine();

            // if ( simulateNFA(NFA, line) ) System.out.println("true");
            // else System.out.println("false");
        }
    }

    // checks if regex string contains any illegal character
    static boolean validRegex(String regex) {
        for ( int i=0; i < regex.length(); i++ ) {
            char ch = regex.charAt(i);
            if ( !validChar(ch) && !validSymbol(ch) ) {
                return false;
            }
        }
        return true;
    }

    // checks if character is valid
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

    //
    static NFA generateNFA(String postfix) {
        Stack<NFA> st = new Stack<>();

        for (int i=0; i < postfix.length(); i++ ) {
            char ch = postfix.charAt(i);

            if ( validChar(ch) ) {
                st.push( new NFA(ch) );
            }

            else if ( ch == '*' ) {
                // error
                if ( st.empty() ) {
                    System.out.println("Error: Kleene Star when stack is empty");
                    System.exit(1);
                }

                st.push( NFA.kleeneStar( st.pop() ) );
            }

            else if ( ch == '+' ) {
                // error
                if ( st.empty() ) {
                    System.out.println("Error: Kleene Plus when stack is empty");
                    System.exit(1);
                }

                st.push( NFA.kleenePlus( st.pop() ) );
            }

            else if ( ch == '_' ) {
                // error
                if ( st.size() < 2 ) {
                    System.out.println("Error: Concatenation when there are less than two NFAs on stack");
                    System.exit(1);
                }

                st.push( NFA.concatenate(st.pop(), st.pop()) );
            }

            else if ( ch == '|' ) {
                // error
                if ( st.size() < 2 ) {
                    System.out.println("Error: Alternation when there are less than two NFAs on stack");
                    System.exit(1);
                }

                st.push( NFA.alternate(st.pop(), st.pop()) );
            }

            else {
                System.out.println("Error: How'd we get here?");
                System.exit(1);
            } 
        }

        // there should only be 1 NFA on the stack
        if ( st.size() != 1 ) {
            System.out.println("Error: There is not just 1 NFA left");
            System.exit(1);
        }

        return st.pop();
    }

    // NFA Class
    static class NFA {
        // instance variables
        String start = "";
        String end = "";
        String states = "";
        String transitions = "";
    
        // Constructor
        public NFA(char ch) {
            this.start = "q0";
            this.end = "q1";
            this.states = "q0 q1";
            this.transitions = "q0,ch,q1";
        }
    
        // 
        static NFA kleeneStar(NFA nfa) {
            return nfa;
        }
    
        // 
        static NFA kleenePlus(NFA nfa) {
            return nfa;
        }
    
        // 
        static NFA concatenate(NFA nfa1, NFA nfa2) {
            return nfa1;
        }
    
        // 
        static NFA alternate(NFA nfa1, NFA nfa2) {
            return nfa1;
        }
    }
}

