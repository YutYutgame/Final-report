package YutYutGame;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class YutBoardPanel extends JPanel {
    private final Map<Integer, BoardNode> nodes;
    private final Map<Integer, String> pieceTexts;
    private final Map<Integer, Color> pieceColors;

    public YutBoardPanel(Board nodeBoard) {
        setLayout(null);
        setPreferredSize(new Dimension(600, 600));
        setBackground(Color.WHITE);

        this.nodes = nodeBoard.getNodeMap();
        this.pieceTexts = new HashMap<>();
        this.pieceColors = new HashMap<>();

        // 초기화: 각 노드에 기본 텍스트/색상 설정
        for (Integer id : nodes.keySet()) {
            pieceTexts.put(id, "");
            pieceColors.put(id, Color.BLACK);
        }
    }

    public void updatePiecePosition(int id, Color color, String text) {
        if (nodes.containsKey(id)) {
            pieceTexts.put(id, text);
            pieceColors.put(id, color);
            repaint();
        }
    }

    public void clearPosition(int id) {
        if (nodes.containsKey(id)) {
            pieceTexts.put(id, "");
            pieceColors.put(id, Color.BLACK);
            repaint();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        for (Map.Entry<Integer, BoardNode> entry : nodes.entrySet()) {
            int id = entry.getKey();
            BoardNode node = entry.getValue();
            Point p = node.getPosition();
            int x = p.x;
            int y = p.y;

            g2.setColor(Color.LIGHT_GRAY);
            g2.fillOval(x, y, 40, 40);
            g2.setColor(Color.DARK_GRAY);
            g2.drawOval(x, y, 40, 40);
            
            g2.setColor(Color.BLUE);
            g2.setFont(new Font("맑은 고딕", Font.PLAIN, 10));
            String idText = String.valueOf(id);
            g2.drawString(idText, x + 15, y - 5);

            String text = pieceTexts.getOrDefault(id, "");
            if (!text.isEmpty()) {
                g2.setColor(pieceColors.getOrDefault(id, Color.BLACK));
                g2.setFont(new Font("맑은 고딕", Font.BOLD, 12));
                FontMetrics fm = g2.getFontMetrics();
                int textWidth = fm.stringWidth(text);
                int textX = x + (40 - textWidth) / 2;
                int textY = y + (40 + fm.getAscent()) / 2 - 4;
                g2.drawString(text, textX, textY);
            }
        }
    }
}
//바뀜