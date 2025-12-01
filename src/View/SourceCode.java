package View;

import View.Constants.CustomColors;
import javax.swing.*;
import java.awt.*;

public class SourceCode {

    private static JTextArea textArea; // ← only new field

    public static JPanel create(String title) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createLineBorder(CustomColors.PURPLE, 4, true));

        // Header
        JLabel header = new JLabel(title, SwingConstants.CENTER);
        header.setFont(new Font("Segoe UI", Font.BOLD, 22));
        header.setForeground(Color.WHITE);
        header.setBackground(CustomColors.RESULT_HEADER_BG);
        header.setOpaque(true);
        header.setPreferredSize(new Dimension(0, 60));
        panel.add(header, BorderLayout.NORTH);

        // Text area
        textArea = new JTextArea(18, 32);
        textArea.setFont(new Font("Consolas", Font.PLAIN, 15));
        textArea.setBackground(new Color(250, 245, 255));
        textArea.setForeground(CustomColors.PURPLE);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setMargin(new Insets(12, 12, 12, 12));

        JScrollPane scroll = new JScrollPane(textArea);
        scroll.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        scroll.setPreferredSize(new Dimension(380, 360));

        panel.add(scroll, BorderLayout.CENTER);
        panel.setPreferredSize(new Dimension(400, 440));

        return panel;
    }

    public static void setCode(String code) {
        if (textArea != null) {
            textArea.setText(code);
            textArea.setCaretPosition(0);
        }
    }

    public static String getCode() {
        {
            return textArea != null ? textArea.getText() : "";
        }
    }
}