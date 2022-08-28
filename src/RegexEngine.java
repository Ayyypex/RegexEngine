import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.Collections;

/** Represents an engine that parses regular expressions and matches input against it  */
public class RegexEngine {
    /** Takes input from System.in to parse and evaluate a regex */
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
        if ( !checkRegex(regex) ) {
            System.out.println("Error: This input is invalid: " + regex);
            System.exit(1);
        }

        // add concatenation symbols, and convert expression to postfix notation
        regex = addConcatenations(regex);
        String postfix = toPostfix(regex);

        // generate NFA structure
        NFA finalNFA = generateNFA(postfix);

        // if we are in verbose mode, print the NFA's transition table
        if ( verbose ) {
            String[][] table = NFA.getTable(finalNFA);
            int rows = table.length;
            int cols = table[0].length;

            // print output row by row
            for ( int i=0; i < rows; i++ ) {
                String rowOutput = "";

                for ( int j=0; j < cols; j++ ) {
                    rowOutput += table[i][j];
                }

                System.out.println(rowOutput);
            }
            System.out.println("");
        }

        // print ready as per assignment spec
        System.out.println("ready");
        
        String completeInput = "";         // all of a user's input so far, used in verbose mode

        // reads each line until ctrl+c, printing whether the NFA is in an accepting state or not
        while ( true ) {
            if ( verbose ) {
                System.out.println( String.valueOf( simulateNFA(finalNFA, completeInput) ) );
                String line = reader.readLine();
                completeInput += line;

            } else {
                String line = reader.readLine();
                System.out.println( String.valueOf( simulateNFA(finalNFA, line) ) );
            }           
        }
    }

    /** 
     * Checks if character is alphanumeric or a space
     * @param  ch  character to check
     * @return     true or false depending on if character is legal 
     */
    static boolean legalChar(char ch) {
        if ( !Character.isLetterOrDigit(ch) && ch != ' ' ) {
            return false;
        }
        return true;
    }

    /** 
     * Checks if character is a legal symbol
     * @param  ch  character to check
     * @return     true or false depending on if input is legal symbol 
     */
    static boolean legalSymbol(char ch) {
        if ( ch == '|' || ch == '*' || ch == '+' || ch == '(' || ch == ')' ) {
            return true;
        }
        return false;
    }

    /** 
     * Checks if regex contains any illegal characters
     * @param  regex  expression to check
     * @return        true or false depending on if input is legal 
     */
    static boolean legalRegexCharacters(String regex) {
        for ( int i=0; i < regex.length(); i++ ) {
            char ch = regex.charAt(i);
            if ( !legalChar(ch) && !legalSymbol(ch) ) {
                return false;
            }
        }
        return true;
    }

    /** 
     * Checks if regex uses brackets correctly
     * @param  regex  expression to check
     * @return        true or false depending on if input is legal 
     */
    static boolean legalBrackets(String regex) {
        boolean openBracket = false;

        for ( int i=0; i < regex.length(); i++ ) {
            char ch = regex.charAt(i);

            // left bracket, now in an open bracket
            if ( !openBracket && ch == '(' ) {          
                openBracket = true;

            // right bracket without left one
            } else if ( !openBracket && ch == ')' ) {   
                return false;

            // nested bracket
            } else if ( openBracket && ch == '(' ) {      
                return false;

            // bracket closes
            } else if ( openBracket && ch == ')' ) {      
                openBracket = false;
            }
        }

        // if bracket is still open, then brackets are not being used correctly
        return !openBracket;
    }

    /** 
     * Checks if regex is using symbols correctly
     * @param  regex  expression to check
     * @return        true or false depending on if symbols are used correctly 
     */
    static boolean checkSymbolUsage(String regex) {
        for ( int i=0; i < regex.length()-1; i++ ) {
            char ch = regex.charAt(i);
            char next = regex.charAt(i+1);

            // invalid use of symbols detected
            if ( ( legalSymbol(ch) && ch == next ) 
                || ( ch == '|' && ( next == '*' || next == '+' ) ) 
                || ( ch == '*' && next == '+' ) 
                || ( ch == '+' && next == '*' ) )
            {
                return true;
            }
        }
        
        return false;
    }

    /** 
     * Checks regex for some invalid/erroneous input. Not an exhaustive check
     * @param  regex  expression to check
     * @return        true or false depending on if regex is valid 
     */
    static boolean checkRegex(String regex) {
        if ( !legalRegexCharacters(regex) || !legalBrackets(regex) || checkSymbolUsage(regex) || regex == "" ) {
            return false;
        }
        return true;
    }
    
    /** 
     * Adds concatenation symbols to a regex
     * @param  regex  expression to add concatenations to
     * @return        the regex epxression with added concatenations  
     */
    static String addConcatenations(String regex) {
        String output = "";

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

    /** 
     * Gets the precedence of a character 
     * @param  ch  expression to add concatenations to
     * @return     the integer value of the input's precedence 
     */
    static int getPrecedence(char ch) {
        if ( ch == '*' || ch == '+' ) {
            return 2;
        } else if ( ch  == '_' ) {
            return 1;
        }
        return 0;
    }

    /** 
     * Converts an infix regex to postfix notation
     * @param  regex  regex to convert
     * @return        regex in postfix notation  
     */
    static String toPostfix(String regex) {
        Stack<Character> st = new Stack<>();
        String postfix = "";

        for ( int i=0; i < regex.length(); i++ ) {
            char ch = regex.charAt(i);

            // append characters to postfix
            if ( legalChar(ch) ) {
                postfix += ch;
            
            // push ( onto stack
            } else if ( ch == '(' ) {       
                st.push(ch);

            // append operators within brackets
            } else if ( ch == ')' ) {
                while ( !st.empty() && st.peek() != '(' ) {
                    postfix += st.pop();
                }

                // pop ( off
                st.pop();

            // push operators: | + * _
            } else {
                // if top stack element precedence is higher than current element, append top
                while ( !st.empty() && st.peek() != '(' && getPrecedence(ch) <= getPrecedence(st.peek()) ) {
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

    /** 
     * Generates an NFA structure from a postfix regex
     * @param  postfix  postfix regex to generate NFA from
     * @return          NFA structure of regex
     */
    static NFA generateNFA(String postfix) {
        Stack<NFA> st = new Stack<>();

        for (int i=0; i < postfix.length(); i++ ) {
            char ch = postfix.charAt(i);

            // push characters onto stack
            if ( legalChar(ch) ) {
                st.push( new NFA(ch) );

            // perform kleeneStar op
            } else if ( ch == '*' ) {
                // error
                if ( st.empty() ) {
                    System.out.println("Error: Kleene Star when stack is empty");
                    System.exit(1);
                }

                st.push( NFA.kleeneStar( st.pop() ) );

            // perform kleenePlus op
            } else if ( ch == '+' ) {
                // error
                if ( st.empty() ) {
                    System.out.println("Error: Kleene Plus when stack is empty");
                    System.exit(1);
                }

                st.push( NFA.kleenePlus( st.pop() ) );

            // perform concatenation op
            } else if ( ch == '_' ) {
                // error
                if ( st.size() < 2 ) {
                    System.out.println("Error: Concatenation when there are less than two NFAs on stack");
                    System.exit(1);
                }

                NFA B = st.pop();
                NFA A = st.pop();
                st.push( NFA.concatenate(A,B) );

            // perform alternation op
            } else if ( ch == '|' ) {
                // error
                if ( st.size() < 2 ) {
                    System.out.println("Error: Alternation when there are less than two NFAs on stack");
                    System.exit(1);
                }

                NFA B = st.pop();
                NFA A = st.pop();
                st.push( NFA.alternate(A,B) );

            // Input is invalid in some way
            } else {
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

    /** 
     * Simulates the NFA's processing of input
     * @param  nfa    NFA structure to evaluate against
     * @param  input  string for the NFA to process
     * @return        true or false depending on whether the NFA accepts the input
     */
    static boolean simulateNFA( NFA nfa, String input ) {
        Set<String> currentStates = new HashSet<String>();
        Set<String> nextStates = new HashSet<String>();
        currentStates.add(nfa.start);
        
        for ( int i=0; i < input.length(); i++ ) {
            String ch = String.valueOf( input.charAt(i) );

            // compute epsilon closure
            currentStates = NFA.getEpsClosure(nfa, currentStates);

            // find and take all possible transitions 
            for ( int j=0; j < nfa.transitions.size(); j++ ) {
                NFA.Transition trans = nfa.transitions.get(j);

                // add resulting states to nextStates
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

        // compute epsilon closure one final time
        currentStates = NFA.getEpsClosure(nfa, currentStates);
        
        // return whether the NFA is in an accepting state
        return currentStates.contains(nfa.end);
    }

    /** Represents an epsilon NFA structure */
    static class NFA {
        /** NFA is represented as a start and end point, a set of states, and a set of transitions */
        public String start = "";
        public String end = "";
        public List<String> states = new ArrayList<String>();
        public List<Transition> transitions = new ArrayList<Transition>();

        /** Total number of states created for the NFA so far */
        static int numberOfStates = 0;
    
        /** 
         * Constructor creates the base single character NFA structure 
         * @param  ch  character input that will take NFA from initial to accepting state
         */
        public NFA(char ch) {
            String state = getNewState();
            String input = String.valueOf(ch);
            String result = getNewState();

            this.start = state;
            this.end = result;
            this.states.add(state);
            this.states.add(result);
            this.transitions.add( new Transition(state, input, result) );
        }

        /** Returns a new unused state name and increments the number of states */
        static String getNewState() {
            String state = "q" + String.valueOf(NFA.numberOfStates);
            NFA.numberOfStates++;
            return state;
        }

        /** 
         * Returns the set of states reachable from a state set by taking only epsilon transitions
         * @param  nfa     NFA structure to evaluate against
         * @param  states  set of states to evaluate
         * @return         set of all states reachable from states by epsilon transitions
         */
        static Set<String> getEpsClosure(NFA nfa, Set<String> states)  {
            Set<String> eClose = new HashSet<String>();
            eClose.addAll(states);

            // loop until all reachable states are found
            boolean newStateAdded = true;
            while ( newStateAdded ) { 
                newStateAdded = false;

                // check and take any valid epsilon transitions
                for ( int j=0; j < nfa.transitions.size(); j++ ) {
                    NFA.Transition trans = nfa.transitions.get(j);

                    // add if a new and reachable state is found 
                    if ( eClose.contains(trans.state) && trans.input.equals("epsilon") && !eClose.contains(trans.result) ) {
                        eClose.add(trans.result);
                        newStateAdded = true;
                    }
                }
            }

            return eClose;
        }
    
        /** Applies kleeneStar structure to an NFA */
        static NFA kleeneStar(NFA nfa) {
            NFA A = nfa;

            // add 2 new states to A
            String start = getNewState();
            String end = getNewState();
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
    
        /** Applies kleenePlus structure to an NFA */
        static NFA kleenePlus(NFA nfa) {
            NFA A = nfa;

            // add a new state to A
            String end = getNewState();
            A.states.add( end );

            // add epsilon transitions from new state to start of A and from end of A to new state
            A.transitions.add( new Transition(end, "epsilon", A.start) );
            A.transitions.add( new Transition(A.end, "epsilon", end) );

            // Change A's end to new state
            A.end = end;

            return A;
        }
    
        /** Applies concatenation structure to two NFAs */
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
    
        /** Applies alternation structure to two NFAs */
        static NFA alternate(NFA nfa1, NFA nfa2) {
            NFA A = nfa1;
            NFA B = nfa2;

            // add B's states and transitions to A
            A.states.addAll(B.states);
            A.transitions.addAll(B.transitions);

            // add 2 new states to A
            String start = getNewState();
            String end = getNewState();
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

        /** Returns the alphabet of an NFA */
        static List<String> getAlphabet(NFA nfa) {
            List<String> inputs = new ArrayList<String>();
            boolean epsilon = false;

            // check input of each transition
            for ( int i=0; i < nfa.transitions.size(); i++ ) {
                Transition trans = nfa.transitions.get(i);

                // don't add epsilon yet because we will sort later, and we want epsilon at front
                if ( trans.input == "epsilon" ) {
                    epsilon = true;

                // new input
                } else if ( !inputs.contains(trans.input) ) {
                    inputs.add(trans.input);
                }
            }

            // sort inputs
            Collections.sort(inputs);

            // add epsilon at start if nfa uses it
            if ( epsilon ) {
                inputs.add(0,"epsilon");
            }

            return inputs;
        }

        /** Returns transition table of an NFA */
        static String[][] getTable(NFA nfa) {
            List<String> inputs = getAlphabet(nfa);

            // get number of rows and cols, + 1 for table header and state column
            int rows = nfa.states.size() + 1;
            int cols = inputs.size() + 1;

            // create and initialize 2D array
            String[][] table = new String[rows][cols];
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
                // check for inital and accepting states
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
                
                // +1 to account for table header and state column
                int r = nfa.states.indexOf(trans.state) + 1;
                int c = inputs.indexOf(trans.input) + 1;

                table[r][c] += trans.result + ",";
            }

            // adjusts the width of each column
            for ( int i=0; i < cols; i++ ) {

                // find largest width in column
                int maxWidth = 1;
                for ( int j=0; j < rows; j++ ) {
                    if ( table[j][i].length() > maxWidth ) {
                        maxWidth = table[j][i].length();
                    }
                }

                // adjust width of column to the largest width
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

        /** Represents an NFA transition as a simple String triplet  */
        static class Transition {
            public String state = "";
            public String input = "";
            public String result = "";
            public String transition = "";      // used for a quicker way to check if transitions are equal 

            /** Constructor creates the triplet transition */            
            public Transition(String state, String input, String result) {
                this.state = state;
                this.input = input;
                this.result = result;
                this.transition = state + " " + input + " " + result;
            }
        }
    }
}

