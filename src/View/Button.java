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

public class Button {

    private static final Font BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 20);

    private static final ImageIcon BTN_NORMAL  = loadImage("Assets/Pink Button.png");
    private static final ImageIcon BTN_HOVER   = loadImage("Assets/Pink Button Hover.png");
    private static final ImageIcon BTN_PRESSED = loadImage("Assets/Pink Button Pressed.png");

    private static ImageIcon loadImage(String path) {
        URL url = Button.class.getResource("/" + path);
        return (url != null) ? new ImageIcon(url) : null;
    }

    private static void playClickSound() {
        try {
            URL url = Button.class.getResource("/Assets/Click Sound.wav");
            if (url != null) {
                AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
                Clip clip = AudioSystem.getClip();
                clip.open(audioIn);
                clip.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static JButton createCuteButton(String text) {
        return new JButton(text) {
            {
                setFont(BUTTON_FONT);
                setForeground(CustomColors.PURPLE);
                setContentAreaFilled(false);
                setBorderPainted(false);
                setFocusPainted(false);
                setOpaque(false);
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

                setPreferredSize(new Dimension(250, 95));
                setMinimumSize(new Dimension(250, 95));
                setMaximumSize(new Dimension(250, 95));

                addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        playClickSound();
                    }
                });
            }

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int w = getWidth();
                int h = getHeight();

                double scale = getModel().isPressed() ? 0.94 : 1.0;
                int offsetX = (int) ((w - w * scale) / 2);
                int offsetY = (int) ((h - h * scale) / 2) + (getModel().isPressed() ? 4 : 0);

                g2.translate(offsetX, offsetY);
                g2.scale(scale, scale);

                ImageIcon current = BTN_NORMAL;
                if (getModel().isPressed() && BTN_PRESSED != null) current = BTN_PRESSED;
                else if (getModel().isRollover() && BTN_HOVER != null) current = BTN_HOVER;

                if (current != null) {
                    g2.drawImage(current.getImage(), 0, 0, w, h, this);
                } else {
                    g2.setColor(CustomColors.PINK);
                    g2.fillRoundRect(0, 0, w, h, 60, 60);
                }

                FontMetrics fm = g2.getFontMetrics();
                int x = (w - fm.stringWidth(getText())) / 2;
                // Vertical centering fix for the new height
                int y = (h - fm.getHeight()) / 2 + fm.getAscent() - 2;
                g2.setColor(getForeground());
                g2.drawString(getText(), x, y);

                g2.dispose();
            }
        };
    }

    public static JButton createImageOnlyButton(String imagePath) {
        return new JButton() {
            private final ImageIcon normal  = loadImage(imagePath);
            private final ImageIcon hover   = loadImage(imagePath.replace(".png", " Hover.png"));
            private final ImageIcon pressed = loadImage(imagePath.replace(".png", " Pressed.png"));

            {
                setContentAreaFilled(false);
                setBorderPainted(false);
                setFocusPainted(false);
                setOpaque(false);
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                setPreferredSize(new Dimension(96, 96));
                setMinimumSize(new Dimension(96, 96));
                setMaximumSize(new Dimension(96, 96));

                addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        playClickSound();
                    }
                });
            }

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                double scale = getModel().isPressed() ? 0.92 : 1.0;
                int offsetX = (int) ((getWidth()  - getWidth()  * scale) / 2);
                int offsetY = (int) ((getHeight() - getHeight() * scale) / 2) + (getModel().isPressed() ? 5 : 0);

                g2.translate(offsetX, offsetY);
                g2.scale(scale, scale);

                ImageIcon current = normal;
                if (getModel().isPressed() && pressed != null) current = pressed;
                else if (getModel().isRollover() && hover != null) current = hover;

                if (current != null) {
                    g2.drawImage(current.getImage(), 0, 0, getWidth(), getHeight(), this);
                }

                g2.dispose();
            }
        };
    }

    public static JLabel createTitle(String text) {
        JLabel label = new JLabel(" " + text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 32));
        label.setForeground(Color.WHITE);
        return label;
    }
}