package View;

import Model.LexicalAnalysis;
import Model.SemanticAnalysis;
import Model.SyntaxAnalysis;
import View.Constants.CustomColors;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

public class OpenFile {

    private static boolean fileLoaded = false;
    private static final JButton[] analysisButtons = new JButton[3];
    private static JButton clearButton;

    public static JPanel createUI() {
        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(CustomColors.MAIN_BG);
        main.setBorder(BorderFactory.createLineBorder(CustomColors.PURPLE, 4, true));

        // Clear Button (hidden until file loaded)
        clearButton = Button.createCuteButton("Clear");
        clearButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        clearButton.setVisible(false);

        String[] buttonTexts = { "Open File", "Lexical", "Syntax", "Semantics" };

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBackground(CustomColors.MAIN_BG);
        // Reduced top padding → starts higher up!
        leftPanel.setBorder(BorderFactory.createEmptyBorder(30, 60, 50, 60));

        for (int i = 0; i < buttonTexts.length; i++) {
            String text = buttonTexts[i];
            JButton btn = Button.createCuteButton(text);
            btn.setAlignmentX(Component.CENTER_ALIGNMENT);

            if (i > 0) {
                btn.setVisible(false);
                analysisButtons[i - 1] = btn;
            }

            btn.addActionListener(e -> handleButtonClick(text, main));

            leftPanel.add(btn);
            // Spacing between buttons
            leftPanel.add(Box.createVerticalStrut(i == 0 ? 20 : 14));
        }

        // === CLEAR BUTTON LOGIC ===
        clearButton.addActionListener(e -> {
            SourceCode.setCode("");
            Result.setResultText("Click 'Open File' or 'Lexical' to begin!");
            fileLoaded = false;
            for (JButton b : analysisButtons)
                b.setVisible(false);
            clearButton.setVisible(false);
            if (Result.isShowingSource())
                Result.toggleSourceCode();
        });
        leftPanel.add(clearButton);
        leftPanel.add(Box.createVerticalStrut(14));

        // === TOGGLE BUTTON (Eye Icon) ===
        JButton toggleBtn = Button.createImageOnlyButton("Assets/Source Button.png");
        toggleBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        toggleBtn.setToolTipText("Toggle Source Code Result");
        toggleBtn.addActionListener(e -> Result.toggleSourceCode());

        leftPanel.add(toggleBtn);
        leftPanel.add(Box.createVerticalStrut(60)); // Bottom padding

        // Right side (Result Panel)
        JPanel resultPanel = Result.create();

        JPanel center = new JPanel(new BorderLayout());
        center.setOpaque(false);
        center.setBorder(BorderFactory.createEmptyBorder(20, 20, 40, 60));
        center.add(leftPanel, BorderLayout.WEST);
        center.add(resultPanel, BorderLayout.CENTER);

        // === RUN ANALYSIS BUTTON LISTENER (for Source Code panel) ===
        SourceCode.setRunAnalysisListener(e -> {
            String code = SourceCode.getText().trim();

            if (code.isEmpty()) {
                CustomDialog.showMessage(main, "Empty Code", "Please enter some code to analyze!", true);
                return;
            }

            // Enable all analysis buttons
            fileLoaded = true;
            for (JButton b : analysisButtons) {
                b.setVisible(true);
            }
            clearButton.setVisible(true);

            // Run Lexical Analysis
            LexicalAnalysis lexer = new LexicalAnalysis();
            lexer.analyze(code);
            Result.setResultText(lexer.output);

            // Switch back to Result view to show analysis
            if (Result.isShowingSource()) {
                Result.toggleSourceCode();
            }

            // Show success message
            CustomDialog.showMessage(main, "Success!", "Code analyzed successfully!", false);
        });

        main.add(center, BorderLayout.CENTER);
        return main;
    }

    private static void handleButtonClick(String text, JPanel parent) {

        // --- 1. OPEN FILE ---
        if ("Open File".equals(text)) {
            JFileChooser chooser = new JFileChooser(".");
            chooser.setFileFilter(new FileNameExtensionFilter("Java & Text Files", "java", "txt"));

            if (chooser.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION) {
                try {
                    File file = chooser.getSelectedFile();
                    String code = java.nio.file.Files.readString(file.toPath());

                    SourceCode.setCode(code);
                    fileLoaded = true;

                    // Run Lexical Analysis immediately on load to verify basic structure
                    LexicalAnalysis lexer = new LexicalAnalysis();
                    lexer.analyze(code);
                    Result.setResultText(lexer.output);

                    if (Result.isShowingSource())
                        Result.toggleSourceCode();

                    // Reveal buttons smoothly with animation
                    Timer timer = new Timer();
                    int[] step = { 0 };
                    timer.scheduleAtFixedRate(new TimerTask() {
                        @Override
                        public void run() {
                            SwingUtilities.invokeLater(() -> {
                                if (step[0] < 3) {
                                    analysisButtons[step[0]].setVisible(true);
                                } else if (step[0] == 3) {
                                    clearButton.setVisible(true);
                                } else {
                                    timer.cancel();
                                }
                                step[0]++;
                            });
                        }
                    }, 0, 160);

                    // Show Custom Success Dialog
                    CustomDialog.showMessage(parent, "Success!", "File loaded successfully!\n" + file.getName(), false);

                } catch (Exception ex) {
                    Result.setResultText("ERROR: " + ex.getMessage());
                    CustomDialog.showMessage(parent, "Error", "Failed to read file!\n" + ex.getMessage(), true);
                }
            }

            // --- 2. LEXICAL ANALYSIS BUTTON ---
        } else if ("Lexical".equals(text) && fileLoaded) {
            String code = SourceCode.getText().trim();
            if (code.isEmpty()) {
                CustomDialog.showMessage(parent, "Empty Code", "No code to analyze!", true);
                return;
            }
            LexicalAnalysis lexer = new LexicalAnalysis();
            lexer.analyze(code);
            Result.setResultText(lexer.output);
            if (Result.isShowingSource())
                Result.toggleSourceCode();

            // --- 3. SYNTAX ANALYSIS BUTTON ---
        } else if ("Syntax".equals(text) && fileLoaded) {
            String code = SourceCode.getText().trim();

            // Step A: Run Lexer first (Need tokens)
            LexicalAnalysis lexer = new LexicalAnalysis();
            lexer.analyze(code);

            if (lexer.isSuccess) {
                // Step B: Pass tokens to Syntax Analyzer
                SyntaxAnalysis syntax = new SyntaxAnalysis(lexer.getLexemes());
                syntax.analyze();

                // Step C: Display Syntax Result
                Result.setResultText(syntax.output);
            } else {
                Result.setResultText("Syntax Analysis Aborted.\nLexical Errors found:\n\n" + lexer.output);
                CustomDialog.showMessage(parent, "Lexical Error", "Fix lexical errors before checking syntax!", true);
            }

            if (Result.isShowingSource())
                Result.toggleSourceCode();

            // --- 4. SEMANTIC ANALYSIS BUTTON ---
        } else if ("Semantics".equals(text) && fileLoaded) {
            String code = SourceCode.getText().trim();

            // Step A: Run Lexer first (Need tokens)
            LexicalAnalysis lexer = new LexicalAnalysis();
            lexer.analyze(code);

            if (lexer.isSuccess) {
                // Step B: Pass tokens to Semantic Analyzer
                SemanticAnalysis semantic = new SemanticAnalysis(lexer.getLexemes());
                semantic.analyze();

                // Step C: Display Semantic Result
                Result.setResultText(semantic.output);
            } else {
                Result.setResultText("Semantic Analysis Aborted.\nLexical Errors found:\n\n" + lexer.output);
                CustomDialog.showMessage(parent, "Lexical Error", "Fix lexical errors before checking semantics!",
                        true);
            }

            if (Result.isShowingSource())
                Result.toggleSourceCode();
        }
    }
}