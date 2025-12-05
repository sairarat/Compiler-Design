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
        JPanel main = new GridGradientPanel(new BorderLayout());
        main.setBorder(BorderFactory.createLineBorder(CustomColors.PURPLE, 4, true));

        // Clear Button
        clearButton = Button.createCuteButton("Clear");
        clearButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        clearButton.setVisible(false);

        String[] buttonTexts = { "Open File", "Lexical", "Syntax", "Semantics" };

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setOpaque(false);
        // Reduced side padding slightly
        leftPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 50, 30));

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

            // UPDATED: Reduced gap between buttons (was 14, now 6)
            leftPanel.add(Box.createVerticalStrut(i == 0 ? 15 : 6));
        }

        // === CLEAR BUTTON LOGIC ===
        clearButton.addActionListener(e -> {
            SourceCode.setCode("");
            Result.setResultText("Click 'Open File' or 'Lexical' to begin!");
            fileLoaded = false;
            for (JButton b : analysisButtons) b.setVisible(false);
            clearButton.setVisible(false);
            if (Result.isShowingSource()) Result.toggleSourceCode();
        });
        leftPanel.add(clearButton);
        leftPanel.add(Box.createVerticalStrut(6)); // Reduced gap

        // === TOGGLE BUTTON ===
        JButton toggleBtn = Button.createImageOnlyButton("Assets/Source Button.png");
        toggleBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        toggleBtn.setToolTipText("Toggle Source Code Result");
        toggleBtn.addActionListener(e -> Result.toggleSourceCode());

        leftPanel.add(toggleBtn);
        leftPanel.add(Box.createVerticalStrut(60));

        // Right side (Result Panel)
        JPanel resultPanel = Result.create();

        JPanel center = new JPanel(new BorderLayout());
        center.setOpaque(false);
        center.setBorder(BorderFactory.createEmptyBorder(20, 20, 40, 60));
        center.add(leftPanel, BorderLayout.WEST);
        center.add(resultPanel, BorderLayout.CENTER);

        // ... (Rest of logic remains the same)
        SourceCode.setRunAnalysisListener(e -> {
            String code = SourceCode.getText().trim();
            if (code.isEmpty()) {
                CustomDialog.showMessage(main, "Empty Code", "Please enter some code to analyze!", true);
                return;
            }
            fileLoaded = true;
            for (JButton b : analysisButtons) b.setVisible(true);
            clearButton.setVisible(true);
            LexicalAnalysis lexer = new LexicalAnalysis();
            lexer.analyze(code);
            Result.setResultText(lexer.output);
            if (Result.isShowingSource()) Result.toggleSourceCode();
            CustomDialog.showMessage(main, "Success!", "Code analyzed successfully!", false);
        });

        main.add(center, BorderLayout.CENTER);
        return main;
    }

    private static void handleButtonClick(String text, JPanel parent) {
        if ("Open File".equals(text)) {
            CustomFileChooser chooser = new CustomFileChooser(".");
            chooser.setFileFilter(new FileNameExtensionFilter("Java & Text Files", "java", "txt"));

            if (chooser.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION) {
                try {
                    File file = chooser.getSelectedFile();
                    String code = java.nio.file.Files.readString(file.toPath());
                    SourceCode.setCode(code);
                    fileLoaded = true;
                    LexicalAnalysis lexer = new LexicalAnalysis();
                    lexer.analyze(code);
                    Result.setResultText(lexer.output);
                    if (Result.isShowingSource()) Result.toggleSourceCode();

                    Timer timer = new Timer();
                    int[] step = { 0 };
                    timer.scheduleAtFixedRate(new TimerTask() {
                        @Override
                        public void run() {
                            SwingUtilities.invokeLater(() -> {
                                if (step[0] < 3) analysisButtons[step[0]].setVisible(true);
                                else if (step[0] == 3) clearButton.setVisible(true);
                                else timer.cancel();
                                step[0]++;
                            });
                        }
                    }, 0, 160);

                    CustomDialog.showMessage(parent, "Success!", "File loaded successfully!\n" + file.getName(), false);

                } catch (Exception ex) {
                    Result.setResultText("ERROR: " + ex.getMessage());
                    CustomDialog.showMessage(parent, "Error", "Failed to read file!\n" + ex.getMessage(), true);
                }
            }
        } else if ("Lexical".equals(text) && fileLoaded) {
            String code = SourceCode.getText().trim();
            LexicalAnalysis lexer = new LexicalAnalysis();
            lexer.analyze(code);
            Result.setResultText(lexer.output);
            if (Result.isShowingSource()) Result.toggleSourceCode();
        } else if ("Syntax".equals(text) && fileLoaded) {
            String code = SourceCode.getText().trim();
            LexicalAnalysis lexer = new LexicalAnalysis();
            lexer.analyze(code);
            if (lexer.isSuccess) {
                SyntaxAnalysis syntax = new SyntaxAnalysis(lexer.getLexemes());
                syntax.analyze();
                Result.setResultText(syntax.output);
            } else {
                Result.setResultText("Syntax Analysis Aborted.\nLexical Errors found:\n\n" + lexer.output);
                CustomDialog.showMessage(parent, "Lexical Error", "Fix lexical errors before checking syntax!", true);
            }
            if (Result.isShowingSource()) Result.toggleSourceCode();
        } else if ("Semantics".equals(text) && fileLoaded) {
            String code = SourceCode.getText().trim();
            LexicalAnalysis lexer = new LexicalAnalysis();
            lexer.analyze(code);
            if (lexer.isSuccess) {
                SemanticAnalysis semantic = new SemanticAnalysis(lexer.getLexemes());
                semantic.analyze();
                Result.setResultText(semantic.output);
            } else {
                Result.setResultText("Semantic Analysis Aborted.\nLexical Errors found:\n\n" + lexer.output);
                CustomDialog.showMessage(parent, "Lexical Error", "Fix lexical errors before checking semantics!", true);
            }
            if (Result.isShowingSource()) Result.toggleSourceCode();
        }
    }
}