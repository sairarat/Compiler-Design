package View;

import View.Constants.CustomColors;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;

public class DialogUtils {

    // --- SOUND METHOD ---
    private static void playClickSound() {
        try {
            URL url = DialogUtils.class.getResource("/Assets/Click Sound.wav");
            if (url != null) {
                AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
                Clip clip = AudioSystem.getClip();
                clip.open(audioIn);
                clip.start();
            }
        } catch (Exception e) {
            // silent fail if sound missing
        }
    }

    public static JButton createDialogButton(String text) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int w = getWidth();
                int h = getHeight();
                int arc = h; // Full rounded corners (Pill shape)

                // 1. CLICK ANIMATION (Shrink Effect)
                double scale = getModel().isPressed() ? 0.95 : 1.0;

                int offsetX = (int) ((w - w * scale) / 2);
                int offsetY = (int) ((h - h * scale) / 2);

                g2.translate(offsetX, offsetY);
                g2.scale(scale, scale);

                // 2. BACKGROUND (Static Pink)
                g2.setColor(CustomColors.PINK);
                g2.fillRoundRect(0, 0, w, h, arc, arc);

                // 3. BORDER (Darker Pink)
                g2.setColor(CustomColors.PINK_DARK);
                g2.setStroke(new BasicStroke(3f));
                g2.drawRoundRect(1, 1, w - 3, h - 3, arc, arc);

                // 4. TEXT (White)
                g2.setColor(Color.WHITE);
                FontMetrics fm = g2.getFontMetrics();
                int x = (w - fm.stringWidth(getText())) / 2;
                int y = (h - fm.getHeight()) / 2 + fm.getAscent() - 1;

                g2.drawString(getText(), x, y);

                g2.dispose();
            }
        };

        // Button Settings
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setOpaque(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(110, 40));

        // ADDED: Sound Listener
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                playClickSound();
            }
        });

        return btn;
    }
}