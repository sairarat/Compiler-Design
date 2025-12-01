package View;

import View.Constants.CustomColors;
import javax.swing.*;
import java.awt.*;

public class Result {

    private static final CardLayout cardLayout = new CardLayout();
    private static final JPanel cardPanel = new JPanel(cardLayout);

    private static final JPanel defaultResultPanel;
    private static final JPanel sourceCodePanel;

    private static boolean showingSource = false;

    private static JTextArea resultTextArea; // ← only new field

    static {
        // === DEFAULT RESULT PANEL ===
        defaultResultPanel = new JPanel(new BorderLayout());
        defaultResultPanel.setOpaque(false);
        defaultResultPanel.setBorder(BorderFactory.createLineBorder(CustomColors.PURPLE, 4, true));

        JLabel header = new JLabel("Result", SwingConstants.CENTER);
        header.setFont(new Font("Segoe UI", Font.BOLD, 22));
        header.setForeground(Color.WHITE);
        header.setBackground(CustomColors.RESULT_HEADER_BG);
        header.setOpaque(true);
        header.setPreferredSize(new Dimension(0, 60));
        defaultResultPanel.add(header, BorderLayout.NORTH);

        resultTextArea = new JTextArea();
        resultTextArea.setFont(new Font("Consolas", Font.PLAIN, 16));
        resultTextArea.setBackground(new Color(250, 245, 255));
        resultTextArea.setForeground(CustomColors.PURPLE);
        resultTextArea.setEditable(false);
        resultTextArea.setMargin(new Insets(20, 20, 20, 20));
        resultTextArea.setText("Click 'Open File' or 'Lexical' to begin!");

        JScrollPane scroll = new JScrollPane(resultTextArea);
        scroll.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        defaultResultPanel.add(scroll, BorderLayout.CENTER);

        // === SOURCE CODE PANEL ===
        sourceCodePanel = SourceCode.create("Source Code");

        // === ADD TO CARD LAYOUT ===
        cardPanel.add(defaultResultPanel, "RESULT");
        cardPanel.add(sourceCodePanel, "SOURCE");

        cardLayout.show(cardPanel, "RESULT");
    }

    public static JPanel create() {
        return cardPanel;
    }

    public static void toggleSourceCode() {
        showingSource = !showingSource;
        cardLayout.show(cardPanel, showingSource ? "SOURCE" : "RESULT");
    }

    public static boolean isShowingSource() {
        return showingSource;
    }

    // ← Only this method added
    public static void setResultText(String text) {
        if (resultTextArea != null) {
            resultTextArea.setText(text);
            resultTextArea.setCaretPosition(0);
        }
    }
}