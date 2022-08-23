import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

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

        // NFA = createNFA(regex);
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
            if ( !Character.isAlphabetic(ch) && !Character.isDigit(ch) && !validSymbol(ch) ) {
                return false;
            }
        }
        return true;
    }

    // checks if character is acceptable
    static boolean validSymbol(char ch) {
        if ( ch == '|' || ch == '*' || ch == '+' || ch == '(' || ch == ')' || ch == ' ' ) {
            return true;
        }
        return false;
    }
}