package View;

import View.Constants.CustomColors;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.net.URL;

public class TitleBar extends JPanel {

    private final JFrame frame;
    private final Point clickPoint = new Point();

    private static final ImageIcon ICON_CLOSE       = loadIcon("Assets/Close Button.png");
    private static final ImageIcon ICON_CLOSE_HOVER = loadIcon("Assets/Close Button Hover.png");
    private static final ImageIcon ICON_MINIMIZE    = loadIcon("Assets/Minimize Button.png");
    private static final ImageIcon ICON_RESIZE      = loadIcon("Assets/Resize Button.png");

    public TitleBar(JFrame frame) {
        this.frame = frame;
        setLayout(new BorderLayout());
        setBackground(CustomColors.PINK);
        setPreferredSize(new Dimension(0, 70));

        // adds the bottom purple border
        setBorder(BorderFactory.createMatteBorder(0, 0, 5, 0, CustomColors.PINK_DARK));

        add(createLeftPanel(), BorderLayout.WEST);
        add(createWindowControls(), BorderLayout.EAST);

        enableWindowDrag();
    }

    // --- SOUND METHOD ---
    private void playClickSound() {
        try {
            URL url = TitleBar.class.getResource("/Assets/Click Sound.wav");
            if (url != null) {
                AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
                Clip clip = AudioSystem.getClip();
                clip.open(audioIn);
                clip.start();
            }
        } catch (Exception e) {
            // silent fail
        }
    }

    private JPanel createLeftPanel() {
        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 25, 0));
        left.setOpaque(false);

        JLabel title = new JLabel("Pawmpiler");
        title.setFont(new Font("Segoe UI", Font.BOLD, 32));
        title.setForeground(Color.WHITE);
        left.add(title);

        return left;
    }

    private JPanel createWindowControls() {
        JPanel controls = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        controls.setOpaque(false);
        controls.setPreferredSize(new Dimension(180, 70));

        // --- MINIMIZE BUTTON ---
        JLabel minBtn = createScaledImageButton(ICON_MINIMIZE);
        minBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                playClickSound(); // Play sound on press
            }
            @Override
            public void mouseClicked(MouseEvent e) {
                frame.setState(JFrame.ICONIFIED);
            }
        });

        // --- RESIZE BUTTON ---
        JLabel resizeBtn = createScaledImageButton(ICON_RESIZE);
        resizeBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                playClickSound(); // Play sound on press
            }
            @Override
            public void mouseClicked(MouseEvent e) {
                if (frame.getExtendedState() == JFrame.MAXIMIZED_BOTH) {
                    frame.setExtendedState(JFrame.NORMAL);
                } else {
                    frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
                }
            }
        });

        // --- CLOSE BUTTON ---
        JLabel closeBtn = createScaledImageButton(ICON_CLOSE);
        closeBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                playClickSound(); // Play sound on press
            }
            @Override
            public void mouseClicked(MouseEvent e) {
                // Short delay to ensure sound starts before JVM kills the app
                try { Thread.sleep(100); } catch (InterruptedException ignored) {}
                System.exit(0);
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                closeBtn.setIcon(ICON_CLOSE_HOVER != null ? scaleIcon(ICON_CLOSE_HOVER) : scaleIcon(ICON_CLOSE));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                closeBtn.setIcon(scaleIcon(ICON_CLOSE));
            }
        });

        controls.add(minBtn);
        controls.add(resizeBtn);
        controls.add(closeBtn);
        return controls;
    }

    private JLabel createScaledImageButton(ImageIcon original) {
        JLabel btn = new JLabel();
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(46, 46));
        if (original != null) {
            btn.setIcon(scaleIcon(original));
        }
        return btn;
    }

    private ImageIcon scaleIcon(ImageIcon original) {
        if (original == null) return null;
        Image scaled = original.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
        return new ImageIcon(scaled);
    }

    private static ImageIcon loadIcon(String path) {
        java.net.URL url = TitleBar.class.getResource("/" + path);
        if (url == null) {
            System.err.println("Warning: Could not find icon: " + path);
            return null;
        }
        return new ImageIcon(url);
    }

    private void enableWindowDrag() {
        addMouseListener(new MouseAdapter() {
            @Override public void mousePressed(MouseEvent e) {
                clickPoint.setLocation(e.getPoint());
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override public void mouseDragged(MouseEvent e) {
                if (frame.getExtendedState() != JFrame.MAXIMIZED_BOTH) {
                    Point loc = frame.getLocation();
                    frame.setLocation(loc.x + e.getX() - clickPoint.x,
                            loc.y + e.getY() - clickPoint.y);
                }
            }
        });
    }
}