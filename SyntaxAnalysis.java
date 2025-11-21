
import java.util.List;

public class SyntaxAnalysis {

    private List<String> lexemes;
    private int currentTokenIndex;
    private String currentLexeme;
    public String output;
    public boolean isSuccess;

    // Constructor to accept the tokens from the Lexer
    public SyntaxAnalysis(List<String> lexemes) {
        this.lexemes = lexemes;
        this.currentTokenIndex = 0;
        this.currentLexeme = lexemes.isEmpty() ? "" : lexemes.get(0);
    }

    // The main entry point for analysis
    public void analyze() {
        try {
            // Loop until we process all tokens
            while (currentTokenIndex < lexemes.size()) {
                parseStatement();
            }

            this.isSuccess = true;
            this.output = "Syntax Analysis Passed! The code follows the grammar rules.";

        } catch (Exception e) {
            this.isSuccess = false;
            this.output = "SYNTAX ERROR: " + e.getMessage();
        }
    }

    // Logic to parse a single statement (Declaration)
    private void parseStatement() throws Exception {
        // Rule: <data_type> <identifier> = <value> ;

        // 1. Expect a Data Type
        if (!isDataType(currentLexeme)) {
            throw new Exception("Expected a Data Type at the start of the statement but found '" + currentLexeme + "'");
        }
        consume(); // Move to next token

        // 2. Expect an Identifier
        if (isKeyword(currentLexeme) || isValue(currentLexeme) || isSymbol(currentLexeme)) {
            throw new Exception("Expected an Identifier (Variable Name) but found '" + currentLexeme + "'");
        }
        consume();

        // 3. Expect Assignment Operator (=) OR Delimiter (;)
        if (currentLexeme.equals("=")) {
            consume(); // Eat the '='

            // 4. If we had '=', we Expect a Value
            if (!isValue(currentLexeme)) {
                throw new Exception("Expected a Value after assignment but found '" + currentLexeme + "'");
            }
            consume();
        }

        // 5. Expect a Delimiter (;)
        if (!currentLexeme.equals(";")) {
            throw new Exception("Missing semicolon ';'. Found '" + currentLexeme + "' instead.");
        }
        consume(); // Finished this statement
    }

    // --- Helper Methods ---

    // Moves to the next token in the list
    private void consume() {
        currentTokenIndex++;
        if (currentTokenIndex < lexemes.size()) {
            currentLexeme = lexemes.get(currentTokenIndex);
        } else {
            currentLexeme = "EOF"; // End of File
        }
    }

    // Reusing logic to identify Data Types
    private boolean isDataType(String lexeme) {
        return lexeme.equals("int") || lexeme.equals("double") || lexeme.equals("char") ||
                lexeme.equals("String") || lexeme.equals("boolean");
    }

    // Reusing logic to identify Values
    private boolean isValue(String lexeme) {
        if (lexeme.equals("true") || lexeme.equals("false")) return true;
        if (lexeme.matches("\\d+(\\.\\d+)?[fFLl]?")) return true; // Numbers
        if ((lexeme.startsWith("\"") && lexeme.endsWith("\"")) ||
                (lexeme.startsWith("'") && lexeme.endsWith("'"))) return true; // Strings/Chars
        return false;
    }

    // Helper to prevent using keywords as variable names
    private boolean isKeyword(String lexeme) {
        return isDataType(lexeme);
    }

    private boolean isSymbol(String lexeme) {
        return lexeme.equals("=") || lexeme.equals(";") || lexeme.equals(",");
    }


}