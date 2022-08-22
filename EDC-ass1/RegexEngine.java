import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

// compiled with: javac RegexEngine.java
//      run with: java RegexEngine (-v for verbose mode)

public class RegexEngine {
    public static void main(String[] args) {
        // take in regex expression
        BufferedReader reader = new BufferedReader( new InputStreamReader(System.in) );
        String regex = "";
        try {
            regex = reader.readLine();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
        System.out.println(regex);

        // NFA = createNFA(regex);
        // if(verbose) System.out.println( getTable(NFA) ); 
        System.out.println("ready");
        
        // read each line until ctrl+c
        while (true) {
            String line = "";
            try {
                line = reader.readLine();
            }
            catch(IOException e) {
                e.printStackTrace();
            }
            System.out.println(line);

            // if( simulateNFA(NFA, line) ) System.out.println("true");
            // else System.out.println("false");
        }
    }
}