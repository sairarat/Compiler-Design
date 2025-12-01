package View;

import Model.LexicalAnalysis;
import View.Constants.CustomColors;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;

public class OpenFile {

    private static boolean fileLoaded = false;
    private static final JButton[] analysisButtons = new JButton[3]; // Lexical, Syntax, Semantics

    public static JPanel createUI() {
        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(CustomColors.MAIN_BG);
        main.setBorder(BorderFactory.createLineBorder(CustomColors.PURPLE, 4, true));

        String[] buttonTexts = { "Open File", "Lexical", "Syntax", "Semantics" };

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBackground(CustomColors.MAIN_BG);
        leftPanel.setBorder(BorderFactory.createEmptyBorder(40, 60, 40, 60));

        for (int i = 0; i < buttonTexts.length; i++) {
            String text = buttonTexts[i];
            JButton btn = Button.createCuteButton(text);
            btn.setAlignmentX(Component.CENTER_ALIGNMENT);

            // Hide the three analysis buttons at startup, only open file button is present
            if (i > 0) {
                btn.setVisible(false);
                analysisButtons[i - 1] = btn; // 0=Lexical, 1=Syntax, 2=Semantics
            }

            btn.addActionListener(e -> {
                if ("Open File".equals(text)) {
                    JFileChooser chooser = new JFileChooser(".");
                    chooser.setFileFilter(new FileNameExtensionFilter("Java & Text Files", "java", "txt"));

                    if (chooser.showOpenDialog(main) == JFileChooser.APPROVE_OPTION) {
                        try {
                            File file = chooser.getSelectedFile();
                            String code = java.nio.file.Files.readString(file.toPath());

                            // Show source code
                            SourceCode.setCode(code);
                            fileLoaded = true;

                            // Auto-run Lexical Analysis
                            LexicalAnalysis lexer = new LexicalAnalysis();
                            lexer.analyze(code);
                            Result.setResultText(lexer.output);
                            if (Result.isShowingSource()) Result.toggleSourceCode();

                            Timer revealTimer = new Timer(200, null);
                            final int[] step = {0};

                            revealTimer.addActionListener(evt -> {
                                if (step[0] < 3) {
                                    analysisButtons[step[0]].setVisible(true);
                                    analysisButtons[step[0]].revalidate();
                                    analysisButtons[step[0]].repaint();
                                    step[0]++;
                                } else {
                                    ((Timer) evt.getSource()).stop();
                                }
                            });

                            revealTimer.start();

                            JOptionPane.showMessageDialog(main,
                                    "File loaded successfully!\n" + file.getName() + "\n\nNow you can run analysis ♡",
                                    "Pawmpiler", JOptionPane.INFORMATION_MESSAGE);

                        } catch (Exception ex) {
                            Result.setResultText("ERROR loading file:\n" + ex.getMessage());
                            JOptionPane.showMessageDialog(main, "Failed to read file!", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }

                } else if ("Lexical".equals(text) && fileLoaded) {
                    String code = SourceCode.getCode().trim();
                    if (code.isEmpty()) return;

                    LexicalAnalysis lexer = new LexicalAnalysis();
                    lexer.analyze(code);
                    Result.setResultText(lexer.output);
                    if (Result.isShowingSource()) Result.toggleSourceCode();

                } else if (fileLoaded) {
                    // Syntax & Semantics placeholders
                    JOptionPane.showMessageDialog(main,
                            text + " Analysis\nComing soon — stay pawsitive ♡",
                            "Work in Progress", JOptionPane.INFORMATION_MESSAGE);
                }
            });

            leftPanel.add(btn);
            leftPanel.add(Box.createVerticalStrut(20));
        }

        // Toggle Source/Result button: always visible
        JButton toggleBtn = Button.createImageOnlyButton("Assets/Source Button.png");
        toggleBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        toggleBtn.addActionListener(e -> Result.toggleSourceCode());
        leftPanel.add(Box.createVerticalStrut(55));
        leftPanel.add(toggleBtn);

        JPanel resultPanel = Result.create();

        JPanel center = new JPanel(new BorderLayout());
        center.setOpaque(false);
        center.setBorder(BorderFactory.createEmptyBorder(20, 20, 40, 60));
        center.add(leftPanel, BorderLayout.WEST);
        center.add(resultPanel, BorderLayout.CENTER);

        main.add(center, BorderLayout.CENTER);
        return main;
    }
}