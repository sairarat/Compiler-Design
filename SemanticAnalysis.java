import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SemanticAnalysis {

    private List<String> lexemes;
    private int currentTokenIndex;
    private String currentLexeme;

    // The Symbol Table: Stores Variable Name -> Data Type (e.g., "x" -> "int")
    private Map<String, String> symbolTable;

    public String output;
    public boolean isSuccess;

    // Constructor
    public SemanticAnalysis(List<String> lexemes) {
        this.lexemes = lexemes;
        this.currentTokenIndex = 0;
        this.currentLexeme = lexemes.isEmpty() ? "" : lexemes.get(0);
        this.symbolTable = new HashMap<>(); // Initialize the symbol table
    }

    // Main Analysis Method
    public void analyze() {
        try {
            // Clear previous run data if necessary
            symbolTable.clear();

            // Loop until all tokens are processed
            while (currentTokenIndex < lexemes.size()) {
                checkStatement();
            }

            this.isSuccess = true;
            this.output = "Semantic Analysis Passed! Type checking and scope analysis valid.";

        } catch (Exception e) {
            this.isSuccess = false;
            this.output = "SEMANTIC ERROR: " + e.getMessage();
        }
    }

    // Analyzes a single declaration: <type> <id> = <value> ;
    private void checkStatement() throws Exception {
        // 1. Get the Data Type
        String dataType = currentLexeme;
        consume(); // Move to Identifier

        // 2. Get the Identifier (Variable Name)
        String variableName = currentLexeme;

        // --- SEMANTIC CHECK 1: DUPLICATE DECLARATION ---
        if (symbolTable.containsKey(variableName)) {
            throw new Exception("Variable '" + variableName + "' is already declared.");
        }
        consume(); // Move to '='

        // 3. Skip the '=' (Syntax analyzer already ensured it's there)
        consume(); // Move to Value

        // 4. Get the Value
        String value = currentLexeme;

        // --- SEMANTIC CHECK 2: TYPE COMPATIBILITY ---
        if (!isTypeCompatible(dataType, value)) {
            throw new Exception("Type Mismatch: Cannot assign value '" + value + "' to variable of type '" + dataType + "'.");
        }

        // If checks pass, add to Symbol Table
        symbolTable.put(variableName, dataType);

        consume(); // Move to ';'
        consume(); // Move to next start of statement or EOF
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

    // The core logic for Type Checking
    private boolean isTypeCompatible(String dataType, String value) {
        switch (dataType) {
            case "int":
                // Regex: one or more digits, no decimal points
                return value.matches("-?\\d+");

            case "double":
                // Regex: digits, optional decimal part, optional 'f' or 'd' suffix
                return value.matches("-?\\d+(\\.\\d+)?[fFdD]?");

            case "boolean":
                return value.equals("true") || value.equals("false");

            case "char":
                // Must start and end with single quotes and have exactly 1 char inside
                return value.startsWith("'") && value.endsWith("'") && value.length() == 3;

            case "String":
                // Must start and end with double quotes
                return value.startsWith("\"") && value.endsWith("\"");

            default:
                return false;
        }
    }
}