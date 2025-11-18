
import java.util.ArrayList;
import java.util.List;

public class LexicalAnalysis {
	
    public String output;
    public boolean isSuccess;

    /**
     * This is the public method your UI will call.
     * It is now VOID (returns nothing).
     * Instead, it sets the 'output' and 'isSuccess' variables.
     */
    public void analyze(String sourceCode) {
        try {
            // 1. Run your splitLexemes method
            List<String> lexemes = splitLexemes(sourceCode.trim());

            // 2. Loop through and classify them
            StringBuilder resultString = new StringBuilder();
            for (String lex : lexemes) {
                resultString.append(classify(lex)).append(" ");
            }

            // 3. If everything worked, set the SUCCESS results
            this.output = "Lexical Analysis Phase Passed!" + resultString.toString().trim();
            this.isSuccess = true;

        } catch (Exception e) {
            // 4. If any method threw an error, set the FAILURE results
            this.output = "LEXICAL ERROR: " + e.getMessage();
            this.isSuccess = false;
        }
    }
		
	//split lexemes
    private  List<String> splitLexemes(String input) throws Exception {
        List<String> result = new ArrayList<>(); //for storing the final lexemes
        StringBuilder current = new StringBuilder(); //for storing the current lexemes
        boolean inDoubleQuotes = false; //for checking if we are in double quotes
        boolean inSingleQuotes = false;//for checking if we are in single quotes

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i); //for each loop to check each character of the input

            if (c == '"' && !inSingleQuotes) { // checks if its a " and if its not in single quotes
                inDoubleQuotes = !inDoubleQuotes; //inverts the variable to true to signal that we are in double quotes
                current.append(c); //append the current character including the "
            } else if (c == '\'' && !inDoubleQuotes) { // checks if its a ' and if its not in double quotes
                inSingleQuotes = !inSingleQuotes; // inverts teh variable to true to signal we are in single quotes
                current.append(c); // append the current character including the '
            } else if (inDoubleQuotes || inSingleQuotes) { // checks if we are in double quotes or in single 
                current.append(c); // append even the whitespaces
            } else if (c == ' ' || c == '\n' || c == '\t' || c == '=' || c == ';' || c == ',') { // checks if its a whitespace or = or ; 
                if (current.length() > 0) { // to skip the first whitespace if no chars yet
                    result.add(current.toString()); // to string the lexeme in stringbuilder then add it to result
                    current.setLength(0); // resets the string builder
                }
                if (c == '=' || c == ';' || c == ',') { // if = or ; or ,
                    result.add(Character.toString(c)); // make the char a string then add it to results
                }
            } else {
                current.append(c); // just appends the char
            }
        }

        if (current.length() > 0) { // checks if its empty. its for adding the current if it still has characters/ string mostly when it doesnt end with ;
            result.add(current.toString());// if not add string the current and add it to results
        }
        
     // --- THIS IS THE CRITICAL BUG FIX ---
        // Check for unterminated strings after the loop is done
        if (inDoubleQuotes) {
            throw new Exception("Unterminated double-quoted string.");
        }
        if (inSingleQuotes) {
            throw new Exception("Unterminated single-quoted string.");
        }
        // ------------------------------------

        return result; //return to the main method the results
    }

    // classify each lexeme
    private  String classify(String lexeme) {
        // for data types
        if (lexeme.equals("int") || lexeme.equals("double") || lexeme.equals("char") || lexeme.equals("String") ||
                lexeme.equals("boolean") || lexeme.equals("float") || lexeme.equals("long") || 
                lexeme.equals("short") || lexeme.equals("byte")) {
            return "<data_type>";
        }

        //for assignment operator
        if (lexeme.equals("=")) {
            return "<assignment_operator>";
        }

        //for delimiter
        if (lexeme.equals(";")) {
            return "<delimiter>";
        }
        if (lexeme.equals(",")) { 
            return "<delimiter>";
        }

        //for values like numbers, quoted strings, etc.
        if (isValue(lexeme)) {
            return "<value>";
        }

        // Otherwise, it's an identifier
        return "<identifier>";
    }

    // checks if its considered a VALUE
    private  boolean isValue(String lexeme) {
    	// Check for boolean literals
        if (lexeme.equals("true") || lexeme.equals("false")) {
            return true;
        }
        // 0–9 or decimal
    	if (lexeme.matches("\\d+(\\.\\d+)?[fFLl]?")) return true;
        // "double quoted" or 'single quoted'
        if ((lexeme.startsWith("\"") && lexeme.endsWith("\"")) ||
            (lexeme.startsWith("'") && lexeme.endsWith("'"))) {
            return true;
        }
        return false;
    }
}

