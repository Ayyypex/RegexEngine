import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.Collections;

// compiled with: javac RegexEngine.java -d tests
//      run with: java RegexEngine (-v for verbose mode)

public class RegexEngine {
    public static void main(String[] args) 
        throws IOException 
    {
        // check if program is run in verbose mode
        boolean verbose  = false;
        if ( args.length == 1 && args[0].contains("-v") ) {
            verbose = true;
        }

        // create reader to read from System.in
        BufferedReader reader = new BufferedReader( new InputStreamReader(System.in) );

        // get regular expression to test, and check for illegal/invalid input
        String regex = reader.readLine();
        if ( !legalRegexCharacters(regex) || !legalBrackets(regex) || duplicateSymbols(regex) ) {
            System.out.println("Error: This input is invalid: " + regex);
            System.exit(1);
        }

        // add concatenation symbols, and convert expression to postfix notation
        regex = addConcatenations(regex);
        String postfix = toPostfix(regex);

        // generate NFA structure
        NFA finalNFA = generateNFA(postfix);

        // print table if verbose, then print ready
        if ( verbose ) {
            List<String> inputs = uniqueInput(regex);

            // get number of rows and cols, + 1 for table header and state column
            int rows = finalNFA.states.size() + 1;
            int cols = inputs.size() + 1;

            String[][] table = NFA.tableOf(finalNFA, inputs, rows, cols);

            // print output
            for ( int i=0; i < rows; i++ ) {
                String rowOutput = "";
                for ( int j=0; j < cols; j++ ) {
                    rowOutput += table[i][j];
                }
                System.out.println(rowOutput);
            }
            System.out.println("");
        }
        System.out.println("ready");
        
        // read each line until ctrl+c
        String completeInput = "";
        while ( true ) {
            if ( verbose ) {
                // print true or false depending on if the NFA accepts the input
                System.out.println( String.valueOf( simulateNFA(finalNFA, completeInput) ) );

                String line = reader.readLine();
                completeInput += line;

            } else {
                String line = reader.readLine();

                // print true or false depending on if the NFA accepts the input
                System.out.println( String.valueOf( simulateNFA(finalNFA, line) ) );
            }           
        }
    }

    // checks if character is valid
    static boolean legalChar(char ch) {
        if ( !Character.isLetterOrDigit(ch) && ch != ' ' ) {
            return false;
        }
        return true;
    }

    // checks if character is a valid symbol
    static boolean legalSymbol(char ch) {
        if ( ch == '|' || ch == '*' || ch == '+' || ch == '(' || ch == ')' ) {
            return true;
        }
        return false;
    }

    // checks if regex string contains any illegal characters
    static boolean legalRegexCharacters(String regex) {
        for ( int i=0; i < regex.length(); i++ ) {
            char ch = regex.charAt(i);
            if ( !legalChar(ch) && !legalSymbol(ch) ) {
                return false;
            }
        }
        return true;
    }

    // checks if regex string uses brackets correctly
    static boolean legalBrackets(String regex) {
        boolean openBracket = false;

        for ( int i=0; i < regex.length(); i++ ) {
            char ch = regex.charAt(i);

            // right bracket without left one
            if ( !openBracket && ch == ')' ) {
                return false;
            }

            // nested bracket
            if ( openBracket && ch == '(' ) {
                return false;
            }

            // bracket closes
            if ( openBracket && ch == ')' ) {
                openBracket = false;
            }
        }

        // if bracket is still open, then brackets are not being used correctly
        return !openBracket;
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

    // checks whether there are duplicate symbols chained together
    static boolean duplicateSymbols(String regex) {
        for ( int i=0; i < regex.length()-1; i++ ) {
            char ch = regex.charAt(i);
            char next = regex.charAt(i+1);

            // duplicate symbol detected
            if ( legalSymbol(ch) && ch == next )  {
                return true;
            }
        }
        
        return false;
    }

    // adds concatenation symbols to an infix string
    static String addConcatenations(String regex) {
        String output = "";

        // iterate over every character
        for ( int i=0; i < regex.length(); i++ ) {
            char ch = regex.charAt(i);
            output += ch;

            // if character is not ( or |, then investigate the next
            if ( ch != '(' && ch != '|' && i < regex.length()-1 ) {
                char next = regex.charAt(i+1);
                // if next character is not a symbol (except left bracket) then add concatenation symbol
                if ( legalChar(next) ||  next == '(' ) {       
                    output += '_';
                }
            }
        }

        return output;
    }

    // converts infix expression to postfix
    static String toPostfix(String infix) {
        Stack<Character> st = new Stack<>();
        String postfix = "";

        // iterate over every character
        for ( int i=0; i < infix.length(); i++ ) {
            char ch = infix.charAt(i);

            // append 
            if ( legalChar(ch) ) {
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

    // generates an NFA from a postfix regex expression
    static NFA generateNFA(String postfix) {
        Stack<NFA> st = new Stack<>();

        for (int i=0; i < postfix.length(); i++ ) {
            char ch = postfix.charAt(i);

            if ( legalChar(ch) ) {
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

                NFA B = st.pop();
                NFA A = st.pop();
                st.push( NFA.concatenate(A,B) );
                A = null;
                B = null;
            }

            else if ( ch == '|' ) {
                // error
                if ( st.size() < 2 ) {
                    System.out.println("Error: Alternation when there are less than two NFAs on stack");
                    System.exit(1);
                }

                NFA B = st.pop();
                NFA A = st.pop();
                st.push( NFA.alternate(A,B) );
                A = null;
                B = null;
            }

            else {
                System.out.println("Error: How'd we get here?");
                System.exit(1);
            } 
        }

        // there should only be 1 NFA on the stack
        if ( st.size() != 1 ) {
            System.out.println("Error: There is a non-1 number of NFAs left on the stack");
            System.exit(1);
        }

        return st.pop();
    }

    // simulates the NFA's processing of the input, returns true if NFA accepts input
    static boolean simulateNFA( NFA nfa, String input ) {
        // set up state sets
        Set<String> currentStates = new HashSet<String>();
        Set<String> nextStates = new HashSet<String>();
        currentStates.add(nfa.start);
        
        // iterate over each character
        for ( int i=0; i < input.length(); i++ ) {
            String ch = String.valueOf( input.charAt(i) );

            // compute epsilon closure
            currentStates = NFA.epsClosure(nfa, currentStates);

            // find and take all possible transitions, add resulting states to nextStates
            for ( int j=0; j < nfa.transitions.size(); j++ ) {
                NFA.Transition trans = nfa.transitions.get(j);
                if ( currentStates.contains(trans.state) && trans.input.equals(ch) ) {
                    nextStates.add(trans.result);
                }
            }

            // if nextStates is empty, then NFA has 'died'
            if ( nextStates.isEmpty() ) {
                return false;
            }

            // set currentStates to nextStates and clear out nextStates
            currentStates.clear();
            currentStates.addAll(nextStates);
            nextStates.clear();
        }

        // compute epsilon closure
        currentStates = NFA.epsClosure(nfa, currentStates);
        
        // check if NFA is in an accepting state
        if ( currentStates.contains(nfa.end) ) {
            return true;
        } else {
            return false;
        }
    }

    // count number of unique inputs that will result from the regex
    static List<String> uniqueInput(String regex) {
        List<String> inputs = new ArrayList<String>();

        // if it has a length of 1, then there will be no epsilon transitions
        if ( regex.length() == 1 && legalChar(regex.charAt(0)) ) {
            inputs.add(regex);
            return inputs;
        }
        
        // iterate over each character
        for (int i=0; i < regex.length(); i++ ) {
            char ch = regex.charAt(i);

            // if character is valid and not in set, add it to set
            if ( legalChar(ch) && !inputs.contains( String.valueOf(ch) ) ) {
                inputs.add( String.valueOf(ch) );
            }
        }

        // sort inputs and add epsilon to the start
        Collections.sort(inputs);
        inputs.add(0,"epsilon");

        return inputs;
    }

    // NFA class to represent FSA
    static class NFA {
        // instance variables
        public String start = "";
        public String end = "";
        public List<String> states = new ArrayList<String>();
        public List<Transition> transitions = new ArrayList<Transition>();

        // static variables
        static int numberOfStates = 0;
    
        // Constructor - creates base NFA for single character
        public NFA(char ch) {
            String state = newState();
            String input = String.valueOf(ch);
            String result = newState();

            this.start = state;
            this.end = result;
            this.states.add(state);
            this.states.add(result);
            this.transitions.add( new Transition(state, input, result) );
        }

        // returns a new unused state name and increments number of states
        static String newState() {
            String state = "q" + String.valueOf(NFA.numberOfStates);
            NFA.numberOfStates++;
            return state;
        }

        // returns set of states reachable from current state set by taking only epsilon transitions
        static Set<String> epsClosure(NFA nfa, Set<String> states)  {
            // 
            Set<String> eClose = new HashSet<String>();
            eClose.addAll(states);

            // loop until 
            boolean newStateAdded = true;
            while ( newStateAdded ) { 
                newStateAdded = false;

                for ( int j=0; j < nfa.transitions.size(); j++ ) {
                    NFA.Transition trans = nfa.transitions.get(j);

                    // if new state is 
                    if ( eClose.contains(trans.state) && trans.input.equals("epsilon") && !eClose.contains(trans.result) ) {
                        eClose.add(trans.result);
                        newStateAdded = true;
                    }
                }
            }

            return eClose;
        }
    
        // perform kleeneStar operation
        static NFA kleeneStar(NFA nfa) {
            NFA A = nfa;

            // add 2 new states to A
            String start = newState();
            String end = newState();
            A.states.add( start );
            A.states.add( end );

            // add epsilon transitions
            A.transitions.add( new Transition(start, "epsilon", A.start) );
            A.transitions.add( new Transition(A.end, "epsilon", start) );
            A.transitions.add( new Transition(start, "epsilon", end) );

            // Change A's start and end to new states
            A.start = start;
            A.end = end;

            return A;
        }
    
        // perform kleenePlus operation
        static NFA kleenePlus(NFA nfa) {
            NFA A = nfa;

            // add a new state to A
            String end = newState();
            A.states.add( end );

            // add epsilon transitions from new state to start of A and from end of A to new state
            A.transitions.add( new Transition(end, "epsilon", A.start) );
            A.transitions.add( new Transition(A.end, "epsilon", end) );

            // Change A's end to new state
            A.end = end;

            return A;
        }
    
        // perform concatenate operation
        static NFA concatenate(NFA nfa1, NFA nfa2) {
            NFA A = nfa1;
            NFA B = nfa2;

            // add B's states and transitions to A
            A.states.addAll(B.states);
            A.transitions.addAll(B.transitions);

            // add epsilon transition from end of A to start of B
            A.transitions.add( new Transition(A.end, "epsilon", B.start) );

            // Change A's end to B's end
            A.end = B.end;

            return A;
        }
    
        // perform alternate operation
        static NFA alternate(NFA nfa1, NFA nfa2) {
            NFA A = nfa1;
            NFA B = nfa2;

            // add B's states and transitions to A
            A.states.addAll(B.states);
            A.transitions.addAll(B.transitions);

            // add 2 new states to A
            String start = newState();
            String end = newState();
            A.states.add( start );
            A.states.add( end );

            // add epsilon transitions
            A.transitions.add( new Transition(start, "epsilon", A.start) );
            A.transitions.add( new Transition(start, "epsilon", B.start) );
            A.transitions.add( new Transition(A.end, "epsilon", end) );
            A.transitions.add( new Transition(B.end, "epsilon", end) );

            // Change A's start and end to the new states respectively
            A.start = start;
            A.end = end;

            return A;
        }

        // prints transition table of an NFA
        static String[][] tableOf(NFA nfa, List<String> inputs, int rows, int cols) {
            String[][] table = new String[rows][cols];

            // initialize 2D array
            for ( int i=0; i < rows; i++ ) {
                for ( int j=0; j < cols; j++ ) {
                    table[i][j] = "";
                }
            }

            // set table header
            for ( int i=0; i < inputs.size(); i++ ) {
                table[0][i+1] = inputs.get(i) + " ";
            }

            // set table state column
            for ( int i=0; i < nfa.states.size(); i++ ) {
                if ( nfa.states.get(i) == nfa.start ) {
                    table[i+1][0] += ">";
                }
                if ( nfa.states.get(i) == nfa.end ) {
                    table[i+1][0] += "*";
                }
                table[i+1][0] += nfa.states.get(i) + " ";
            }

            // add transitions to table
            for ( int i=0; i < nfa.transitions.size(); i++ ) {
                Transition trans = nfa.transitions.get(i);
                
                int r = nfa.states.indexOf(trans.state) + 1;
                int c = inputs.indexOf(trans.input) + 1;

                table[r][c] += trans.result + ",";
            }

            // adjust the width of each column
            for ( int i=0; i < cols; i++ ) {

                // find largest width in column
                int maxWidth = 1;
                for ( int j=0; j < rows; j++ ) {
                    if ( table[j][i].length() > maxWidth ) {
                        maxWidth = table[j][i].length();
                    }
                }

                // adjust width of column
                for ( int j=0; j < rows; j++ ) {
                    int len = table[j][i].length();

                    // remove any trailing commas
                    if ( len > 1 && table[j][i].charAt(len-1) == ',' ) {
                        table[j][i] = table[j][i].substring(0,len-1);
                        len--;
                    }
 
                    // add spaces to pad string
                    int diff = maxWidth - len;
                    for ( int k=0; k < diff; k++ ) {
                        table[j][i] += " ";
                    }

                    // add column divider
                    table[j][i] += "| ";
                }
            }

            return table;
        }

        // Transition class to represent a simple String triplet
        static class Transition {
            public String state = "";
            public String input = "";
            public String result = "";
            public String transition = "";      // used for a quicker way to check if transitions are equal 

            // Constructor
            public Transition(String state, String input, String result) {
                this.state = state;
                this.input = input;
                this.result = result;
                this.transition = state + " " + input + " " + result;
            }
        }
    }
}

