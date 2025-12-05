package View;

import View.Constants.CustomColors;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

public class CustomFileChooser extends JFileChooser {

    private JDialog dialog;
    private int returnValue = JFileChooser.ERROR_OPTION;

    public CustomFileChooser(String currentDirectoryPath) {
        super(currentDirectoryPath);
        setControlButtonsAreShown(false);
    }

    @Override
    public int showOpenDialog(Component parent) {
        dialog = new JDialog(SwingUtilities.getWindowAncestor(parent), "Open File", Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setUndecorated(true);
        dialog.setLayout(new BorderLayout());
        ((JPanel) dialog.getContentPane()).setBorder(BorderFactory.createLineBorder(CustomColors.PINK, 1));

        // 1. HEADER
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(CustomColors.PINK);
        headerPanel.setPreferredSize(new Dimension(100, 35));
        headerPanel.setBorder(new EmptyBorder(0, 10, 0, 10));
        JLabel titleLabel = new JLabel("Select Source Code");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.WEST);
        JLabel closeBtn = new JLabel("X");
        closeBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        closeBtn.setForeground(Color.WHITE);
        closeBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        closeBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                returnValue = JFileChooser.CANCEL_OPTION;
                dialog.dispose();
            }
            @Override
            public void mouseEntered(MouseEvent e) { closeBtn.setForeground(CustomColors.PINK_DARK); }
            @Override
            public void mouseExited(MouseEvent e) { closeBtn.setForeground(Color.WHITE); }
        });
        headerPanel.add(closeBtn, BorderLayout.EAST);
        dialog.add(headerPanel, BorderLayout.NORTH);

        // 2. FILE CHOOSER
        this.setBackground(CustomColors.MAIN_BG);
        styleComponent(this);
        dialog.add(this, BorderLayout.CENTER);

        // 3. CUSTOM BUTTONS (UPDATED)
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        buttonPanel.setBackground(CustomColors.MAIN_BG);

        // Use the new helper for both buttons
        JButton btnCancel = DialogUtils.createDialogButton("Cancel");
        btnCancel.addActionListener(e -> {
            returnValue = JFileChooser.CANCEL_OPTION;
            dialog.dispose();
        });

        JButton btnOpen = DialogUtils.createDialogButton("Open File");
        btnOpen.addActionListener(e -> {
            if (getSelectedFile() != null) {
                returnValue = JFileChooser.APPROVE_OPTION;
                dialog.dispose();
            }
        });

        buttonPanel.add(btnCancel);
        buttonPanel.add(btnOpen);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        dialog.pack();
        dialog.setSize(650, 450);
        dialog.setLocationRelativeTo(parent);
        dialog.setVisible(true);

        return returnValue;
    }

    private void styleComponent(Component c) {
        if (c instanceof JPanel) c.setBackground(CustomColors.MAIN_BG);
        if (c instanceof JViewport) c.setBackground(CustomColors.MAIN_BG);
        if (c instanceof JScrollPane) ((JScrollPane) c).setBorder(BorderFactory.createLineBorder(CustomColors.LIGHT_PURPLE));
        if (c instanceof Container) {
            for (Component child : ((Container) c).getComponents()) {
                styleComponent(child);
            }
        }
    }
}