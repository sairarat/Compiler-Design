package View;

import javax.swing.*;
import java.awt.*;

public class GridGradientPanel extends JPanel {

    // Colors extracted from your reference image
    private static final Color TOP_COLOR    = new Color(130, 225, 255); // Cyan/Baby Blue
    private static final Color BOTTOM_COLOR = new Color(255, 180, 210); // Pastel Pink
    private static final Color GRID_COLOR   = new Color(255, 255, 255, 80); // Translucent White

    private static final int GRID_SIZE = 25; // Size of the grid squares

    public GridGradientPanel(LayoutManager layout) {
        super(layout);
        setOpaque(false); // Optimization
    }

    public GridGradientPanel() {
        super();
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Enable anti-aliasing for smoother lines
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();

        // 1. DRAW GRADIENT BACKGROUND
        GradientPaint gp = new GradientPaint(0, 0, TOP_COLOR, 0, h, BOTTOM_COLOR);
        g2d.setPaint(gp);
        g2d.fillRect(0, 0, w, h);

        // 2. DRAW GRID PATTERN
        g2d.setColor(GRID_COLOR);
        g2d.setStroke(new BasicStroke(1)); // Thin lines

        // Vertical Lines
        for (int x = 0; x < w; x += GRID_SIZE) {
            g2d.drawLine(x, 0, x, h);
        }

        // Horizontal Lines
        for (int y = 0; y < h; y += GRID_SIZE) {
            g2d.drawLine(0, y, w, y);
        }
    }
}