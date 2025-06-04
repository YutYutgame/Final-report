package YutYutGame;

import javax.swing.*;
import java.awt.*;


class PieceIcon extends JPanel {
    private Color color;

    public PieceIcon(Color color) {
        this.color = color;
        setPreferredSize(new Dimension(24, 24));
        setOpaque(false);
    }
    
    public void setColor(Color color) {
    	this.color = color;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(color);
        g.fillOval(2, 2, getWidth() - 4, getHeight() - 4);
        g.setColor(Color.DARK_GRAY);
        g.drawOval(2, 2, getWidth() - 4, getHeight() - 4);
    }
}

