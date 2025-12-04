package Model;

import java.util.List;

public class SyntaxAnalysis {

    private List<String> lexemes;
    private int currentTokenIndex;
    private String currentLexeme;
    public String output;
    public boolean isSuccess;

    // We use this to build the detailed step-by-step log
    private StringBuilder log;

    public SyntaxAnalysis(List<String> lexemes) {
        this.lexemes = lexemes;
        this.currentTokenIndex = 0;
        this.currentLexeme = (lexemes != null && !lexemes.isEmpty()) ? lexemes.get(0) : "EOF";
        this.log = new StringBuilder();
    }

    public void analyze() {
        try {
            // Check for empty input
            if (lexemes == null || lexemes.isEmpty()) {
                throw new Exception("No tokens found. Did Lexical Analysis fail?");
            }

            log.append("--- STARTING SYNTAX ANALYSIS ---\n\n");

            int statementCount = 1;

            // Loop until we process all tokens
            while (currentTokenIndex < lexemes.size()) {
                if (currentLexeme.equals("EOF")) break;

                log.append("Parsing Statement #").append(statementCount).append(":\n");
                parseStatement();
                log.append("✓ Statement #").append(statementCount).append(" is Valid.\n\n");

                statementCount++;
            }

            this.isSuccess = true;
            log.append("----------------------------------\n");
            log.append("RESULT: Syntax Analysis Passed!\n");
            log.append("The code follows the grammar rules.");

            // Assign the full log to the output variable so the GUI displays it
            this.output = log.toString();

        } catch (Exception e) {
            this.isSuccess = false;
            log.append("\n❌ SYNTAX ERROR:\n").append(e.getMessage());
            this.output = log.toString();
        }
    }

    // Logic to parse a single statement (Declaration)
    private void parseStatement() throws Exception {
        // Rule: <data_type> <identifier> = <value> ;

        // 1. Expect a Data Type
        if (!isDataType(currentLexeme)) {
            throw new Exception("Expected a Data Type at start but found '" + currentLexeme + "'");
        }
        log.append("   [Step 1] Found Data Type:      ").append(currentLexeme).append("\n");
        consume();

        // 2. Expect an Identifier
        if (isKeyword(currentLexeme) || isValue(currentLexeme) || isSymbol(currentLexeme)) {
            throw new Exception("Expected an Identifier (Variable Name) but found '" + currentLexeme + "'");
        }
        log.append("   [Step 2] Found Identifier:     ").append(currentLexeme).append("\n");
        consume();

        // 3. Expect Assignment Operator (=) OR Delimiter (;)
        if (currentLexeme.equals("=")) {
            log.append("   [Step 3] Found Assignment:     =\n");
            consume(); // Eat the '='

            // 4. If we had '=', we Expect a Value
            if (!isValue(currentLexeme)) {
                throw new Exception("Expected a Value after assignment but found '" + currentLexeme + "'");
            }
            log.append("   [Step 4] Found Value:          ").append(currentLexeme).append("\n");
            consume();
        } else {
            log.append("   [Step 3] No assignment detected (Declaration only).\n");
        }

        // 5. Expect a Delimiter (;)
        if (!currentLexeme.equals(";")) {
            throw new Exception("Missing semicolon ';'. Found '" + currentLexeme + "' instead.");
        }
        log.append("   [Step 5] Found Semicolon:      ;\n");
        consume();
    }

    // --- Helper Methods ---

    private void consume() {
        currentTokenIndex++;
        if (currentTokenIndex < lexemes.size()) {
            currentLexeme = lexemes.get(currentTokenIndex);
        } else {
            currentLexeme = "EOF";
        }
    }

    private boolean isDataType(String lexeme) {
        return lexeme.equals("int") || lexeme.equals("double") || lexeme.equals("char") ||
                lexeme.equals("String") || lexeme.equals("boolean");
    }

    private boolean isValue(String lexeme) {
        if (lexeme.equals("true") || lexeme.equals("false")) return true;
        if (lexeme.matches("-?\\d+(\\.\\d+)?[fFLl]?")) return true;
        if ((lexeme.startsWith("\"") && lexeme.endsWith("\"")) ||
                (lexeme.startsWith("'") && lexeme.endsWith("'"))) return true;
        return false;
    }

    private boolean isKeyword(String lexeme) {
        return isDataType(lexeme);
    }

    private boolean isSymbol(String lexeme) {
        return lexeme.equals("=") || lexeme.equals(";") || lexeme.equals(",");
    }
}