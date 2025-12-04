package Model;

import java.util.ArrayList;
import java.util.List;

public class LexicalAnalysis {

    public String output;
    public boolean isSuccess;
    private List<String> lexemes;

    public void analyze(String sourceCode) {
        try {
            this.lexemes = new ArrayList<>();
            this.lexemes = splitLexemes(sourceCode.trim());

            StringBuilder result = new StringBuilder();
            result.append("Lexical Analysis Phase Passed!\n");
            result.append("Lexemes found: ").append(lexemes.size()).append("\n\n");
            result.append("TOKEN LIST:\n");
            result.append("-".repeat(50)).append("\n");

            for (String lex : lexemes) {
                result.append(String.format("%-20s → %s\n", lex, classify(lex)));
            }

            this.output = result.toString();
            this.isSuccess = true;
        } catch (Exception e) {
            this.output = "LEXICAL ERROR: " + e.getMessage();
            this.isSuccess = false;
            this.lexemes = new ArrayList<>();
        }
    }

    public List<String> getLexemes() {
        return this.lexemes;
    }

    private List<String> splitLexemes(String input) throws Exception {
        List<String> result = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inDoubleQuotes = false;
        boolean inSingleQuotes = false;

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);

            if (c == '"' && !inSingleQuotes) {
                inDoubleQuotes = !inDoubleQuotes;
                current.append(c);
            } else if (c == '\'' && !inDoubleQuotes) {
                inSingleQuotes = !inSingleQuotes;
                current.append(c);
            } else if (inDoubleQuotes || inSingleQuotes) {
                current.append(c);
            } else if (Character.isWhitespace(c)) { // Handles spaces, tabs, and newlines correctly
                if (current.length() > 0) {
                    result.add(current.toString());
                    current.setLength(0);
                }
            } else if (c == '=' || c == ';' || c == ',') {
                if (current.length() > 0) {
                    result.add(current.toString());
                    current.setLength(0);
                }
                result.add(String.valueOf(c));
            } else {
                current.append(c);
            }
        }

        if (current.length() > 0) result.add(current.toString());
        if (inDoubleQuotes) throw new Exception("Unterminated double-quoted string");
        if (inSingleQuotes) throw new Exception("Unterminated single-quoted string");

        return result;
    }

    private String classify(String lexeme) {
        // Classification logic... (Can remain as you had it or see previous response)
        // For brevity, using your existing logic or the one I provided before works.
        if (lexeme.matches("int|double|char|String|boolean")) return "<data_type>";
        if (lexeme.equals("=")) return "<assignment_operator>";
        if (lexeme.equals(";")) return "<delimiter>";
        return "<identifier>";
    }
}