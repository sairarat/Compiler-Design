package View;

import View.Constants.CustomColors;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CustomDialog {

    public static void showMessage(Component parent, String title, String message, boolean isError) {
        JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(parent), title, Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setUndecorated(true); // Remove default OS title bar
        dialog.setLayout(new BorderLayout());

        // Optional: Add a thin border to frame the white dialog against white backgrounds
        ((JPanel)dialog.getContentPane()).setBorder(BorderFactory.createLineBorder(CustomColors.PINK, 1));

        // 1. CUSTOM HEADER (PINK)
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(CustomColors.PINK);
        headerPanel.setPreferredSize(new Dimension(100, 35));
        headerPanel.setBorder(new EmptyBorder(0, 10, 0, 10));

        // Title Text
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.WEST);

        // Close 'X' Button
        JLabel closeBtn = new JLabel("X");
        closeBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        closeBtn.setForeground(Color.WHITE);
        closeBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        closeBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                dialog.dispose();
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                closeBtn.setForeground(CustomColors.PINK_DARK); //
            }
            @Override
            public void mouseExited(MouseEvent e) {
                closeBtn.setForeground(Color.WHITE);
            }
        });
        headerPanel.add(closeBtn, BorderLayout.EAST);

        dialog.add(headerPanel, BorderLayout.NORTH);

        // 2. MAIN BODY (WHITE)
        JPanel bodyPanel = new JPanel(new BorderLayout(15, 0));
        bodyPanel.setBackground(CustomColors.MAIN_BG);
        bodyPanel.setBorder(new EmptyBorder(20, 20, 15, 20));

        // -- Icon (Left) --
        // Uses PURPLE for Info and PINK_DARK for Error
        JLabel iconLabel = new JLabel(isError ? "!" : "i", SwingConstants.CENTER) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // Draw circle background
                g2.setColor(isError ? CustomColors.PINK_DARK : CustomColors.PURPLE);
                g2.fillOval(0, 0, getWidth(), getHeight());
                super.paintComponent(g);
            }
        };
        iconLabel.setPreferredSize(new Dimension(40, 40));
        iconLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        iconLabel.setForeground(Color.WHITE);

        JPanel iconContainer = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        iconContainer.setBackground(CustomColors.MAIN_BG);
        iconContainer.add(iconLabel);

        bodyPanel.add(iconContainer, BorderLayout.WEST);

        // -- Message Text (Center) --
        JTextArea messageArea = new JTextArea(message);
        messageArea.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        messageArea.setForeground(Color.BLACK);
        messageArea.setBackground(CustomColors.MAIN_BG); // Matches background
        messageArea.setLineWrap(true);
        messageArea.setWrapStyleWord(true);
        messageArea.setEditable(false);
        messageArea.setFocusable(false);

        // Size constraints
        messageArea.setSize(new Dimension(280, 10));
        messageArea.setPreferredSize(new Dimension(280, messageArea.getPreferredSize().height));

        bodyPanel.add(messageArea, BorderLayout.CENTER);
        dialog.add(bodyPanel, BorderLayout.CENTER);

        // 3. BUTTON PANEL (WHITE)
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(CustomColors.MAIN_BG);
        buttonPanel.setBorder(new EmptyBorder(0, 0, 15, 0));

        // The "OK" Button - Uses PINK and PINK_DARK for hover
        JButton okButton = new JButton("OK");
        okButton.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        okButton.setBackground(CustomColors.PINK);
        okButton.setForeground(Color.WHITE);
        okButton.setFocusPainted(false);
        okButton.setBorderPainted(false);
        okButton.setPreferredSize(new Dimension(80, 28));
        okButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        okButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { okButton.setBackground(CustomColors.PINK_DARK); }
            public void mouseExited(MouseEvent e) { okButton.setBackground(CustomColors.PINK); }
        });

        okButton.addActionListener(e -> dialog.dispose());

        buttonPanel.add(okButton);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        // Final Setup
        dialog.pack();
        dialog.setLocationRelativeTo(parent);
        dialog.setVisible(true);
    }
}