package Model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SemanticAnalysis {

    private List<String> lexemes;
    private int currentTokenIndex;
    private String currentLexeme;

    // The Symbol Table: Stores Variable Name -> Data Type
    private Map<String, String> symbolTable;

    public String output;
    public boolean isSuccess;

    private StringBuilder log;

    // Constructor
    public SemanticAnalysis(List<String> lexemes) {
        this.lexemes = lexemes;
        this.currentTokenIndex = 0;
        this.currentLexeme = (lexemes != null && !lexemes.isEmpty()) ? lexemes.get(0) : "EOF";
        this.symbolTable = new HashMap<>();
        this.log = new StringBuilder();
    }

    // Main Analysis Method
    public void analyze() {
        try {
            if (lexemes == null || lexemes.isEmpty()) {
                throw new Exception("No tokens found. Did Lexical Analysis fail?");
            }

            symbolTable.clear();
            log.append("=========================================\n");
            log.append("       SEMANTIC ANALYSIS REPORT\n");
            log.append("=========================================\n\n");

            int statementCount = 1;

            while (currentTokenIndex < lexemes.size()) {
                if (currentLexeme.equals("EOF")) break;

                log.append("Statement #").append(statementCount).append("\n");
                log.append("-----------------------------------------\n");
                checkStatement();
                log.append(">> STATUS: Valid\n\n");
                statementCount++;
            }

            this.isSuccess = true;
            log.append("=========================================\n");
            log.append("               FINAL RESULT              \n");
            log.append("=========================================\n");
            log.append("Analysis:  SUCCESS\n");
            log.append("Message:   All statements are semantically valid.\n\n");

            // Pretty Print Symbol Table
            log.append("--- Symbol Table (Declared Variables) ---\n");
            if (symbolTable.isEmpty()) {
                log.append("(No variables declared)\n");
            } else {
                log.append(String.format("%-15s | %-15s\n", "Variable", "Type"));
                log.append("--------------------------------\n");
                for (Map.Entry<String, String> entry : symbolTable.entrySet()) {
                    log.append(String.format("%-15s | %-15s\n", entry.getKey(), entry.getValue()));
                }
            }

            this.output = log.toString();

        } catch (Exception e) {
            this.isSuccess = false;
            // Format the error nicely
            StringBuilder errorLog = new StringBuilder();
            errorLog.append(log.toString()); // Keep previous successful logs
            errorLog.append("\n>> STATUS: FAILED\n");
            errorLog.append("-----------------------------------------\n");
            errorLog.append("!!! SEMANTIC ERROR !!!\n");
            errorLog.append("Reason: ").append(e.getMessage()).append("\n");
            errorLog.append("-----------------------------------------\n");
            this.output = errorLog.toString();
        }
    }

    private void checkStatement() throws Exception {
        // 1. Get Data Type
        String dataType = currentLexeme;
        consume();
        if (currentLexeme.equals("EOF")) return;

        // 2. Get Identifier
        String variableName = currentLexeme;

        // Log what we are processing
        log.append(String.format("Processing:   %s %s = ... ;\n", dataType, variableName));

        // --- CHECK 1: SCOPE ---
        // We log "Checking..." and then "OK" only if it passes. If it fails, the catch block handles it.
        if (symbolTable.containsKey(variableName)) {
            throw new Exception("Duplicate Declaration. Variable '" + variableName + "' already exists.");
        }
        log.append("Scope Check:  Passed (New Variable)\n");

        consume(); // Move to '='
        consume(); // Move to Value

        // 4. Get Value
        String value = currentLexeme;

        // --- CHECK 2: TYPE COMPATIBILITY ---
        if (!isTypeCompatible(dataType, value)) {
            throw new Exception("Type Mismatch. Cannot assign '" + value + "' to " + dataType + ".");
        }
        log.append(String.format("Type Check:   Passed ('%s' fits %s)\n", value, dataType));

        // Add to Symbol Table
        symbolTable.put(variableName, dataType);

        consume(); // Move to ';'
        consume(); // Move to next
    }

    private void consume() {
        currentTokenIndex++;
        if (currentTokenIndex < lexemes.size()) {
            currentLexeme = lexemes.get(currentTokenIndex);
        } else {
            currentLexeme = "EOF";
        }
    }

    private boolean isTypeCompatible(String dataType, String value) {
        switch (dataType) {
            case "int": return value.matches("-?\\d+");
            case "double": return value.matches("-?\\d+(\\.\\d+)?[fFdD]?");
            case "boolean": return value.equals("true") || value.equals("false");
            case "char": return value.startsWith("'") && value.endsWith("'") && value.length() == 3;
            case "String": return value.startsWith("\"") && value.endsWith("\"");
            default: return false;
        }
    }
}