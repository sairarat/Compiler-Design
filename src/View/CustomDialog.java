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
        dialog.setUndecorated(true);
        dialog.setLayout(new BorderLayout());
        ((JPanel)dialog.getContentPane()).setBorder(BorderFactory.createLineBorder(CustomColors.PINK, 1));

        // 1. HEADER
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(CustomColors.PINK);
        headerPanel.setPreferredSize(new Dimension(100, 35));
        headerPanel.setBorder(new EmptyBorder(0, 10, 0, 10));
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.WEST);
        JLabel closeBtn = new JLabel("X");
        closeBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        closeBtn.setForeground(Color.WHITE);
        closeBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        closeBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) { dialog.dispose(); }
            @Override
            public void mouseEntered(MouseEvent e) { closeBtn.setForeground(CustomColors.PINK_DARK); }
            @Override
            public void mouseExited(MouseEvent e) { closeBtn.setForeground(Color.WHITE); }
        });
        headerPanel.add(closeBtn, BorderLayout.EAST);
        dialog.add(headerPanel, BorderLayout.NORTH);

        // 2. BODY
        JPanel bodyPanel = new JPanel(new BorderLayout(15, 0));
        bodyPanel.setBackground(CustomColors.MAIN_BG);
        bodyPanel.setBorder(new EmptyBorder(20, 20, 15, 20));
        JLabel iconLabel = new JLabel(isError ? "!" : "i", SwingConstants.CENTER) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
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
        JTextArea messageArea = new JTextArea(message);
        messageArea.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        messageArea.setForeground(Color.BLACK);
        messageArea.setBackground(CustomColors.MAIN_BG);
        messageArea.setLineWrap(true);
        messageArea.setWrapStyleWord(true);
        messageArea.setEditable(false);
        messageArea.setFocusable(false);
        messageArea.setSize(new Dimension(280, 10));
        messageArea.setPreferredSize(new Dimension(280, messageArea.getPreferredSize().height));
        bodyPanel.add(messageArea, BorderLayout.CENTER);
        dialog.add(bodyPanel, BorderLayout.CENTER);

        // 3. BUTTON PANEL (UPDATED)
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(CustomColors.MAIN_BG);
        buttonPanel.setBorder(new EmptyBorder(0, 0, 15, 0));

        // Use the new helper to create the "OK" button
        JButton okButton = DialogUtils.createDialogButton("OK");
        okButton.addActionListener(e -> dialog.dispose());

        buttonPanel.add(okButton);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        dialog.pack();
        dialog.setLocationRelativeTo(parent);
        dialog.setVisible(true);
    }
}