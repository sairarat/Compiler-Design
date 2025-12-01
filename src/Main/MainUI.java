package Main;

import Model.LexicalAnalysis;
import View.OpenFile;
import View.TitleBar;

import javax.swing.*;
import java.awt.*;

public class MainUI {
    private static final LexicalAnalysis lexer = new LexicalAnalysis();

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainUI::createAndShowGUI);
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Pawmpiler");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setUndecorated(true);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setMinimumSize(new Dimension(950, 600));

        // Title Bar
        frame.add(new TitleBar(frame), BorderLayout.NORTH);

        // Main Content
        JPanel content = OpenFile.createUI();
        content.setBorder(null);
        frame.add(content, BorderLayout.CENTER);

        frame.setVisible(true);

        // Test in console
        testLexicalAnalysis();
    }

    private static void testLexicalAnalysis() {
        String sampleCode = """
            int age = 21;
            String name = "Pawmpiler";
            double height = 5.5;
            boolean isCute = true;
            char grade = 'A';
            """;

        System.out.println("=== PAWMPILER LEXICAL ANALYSIS ===\n");
        System.out.println("Input Code:\n" + sampleCode + "\n");

        lexer.analyze(sampleCode);

        if (lexer.isSuccess) {
            System.out.println("SUCCESS: Lexical Analysis Passed!\n");
        } else {
            System.out.println("FAILED: Lexical Error Found!\n");
        }

        System.out.println(lexer.output);
        System.out.println("\n" + "=".repeat(70) + "\n");
    }
}