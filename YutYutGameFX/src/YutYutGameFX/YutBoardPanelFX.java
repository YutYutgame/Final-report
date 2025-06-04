package YutYutGameFX;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.geometry.Point2D;
import java.util.HashMap;
import java.util.Map;

public class YutBoardPanelFX extends Region {
    private final Map<Integer, BoardNode> nodes;
    private final Map<Integer, String> pieceTexts;
    private final Map<Integer, Color> pieceColors;
    private final Canvas canvas;

    public YutBoardPanelFX(Board nodeBoard) {
        this.nodes = nodeBoard.getNodeMap();
        this.pieceTexts = new HashMap<>();
        this.pieceColors = new HashMap<>();

        this.canvas = new Canvas(600, 600);
        getChildren().add(canvas);

        for (Integer id : nodes.keySet()) {
            pieceTexts.put(id, "");
            pieceColors.put(id, Color.BLACK);
        }

        draw();
    }

    public void updatePiecePosition(int id, Color color, String text) {
        if (nodes.containsKey(id)) {
            pieceTexts.put(id, text);
            pieceColors.put(id, color);
            draw();
        }
    }

    public void clearPosition(int id) {
        if (nodes.containsKey(id)) {
            pieceTexts.put(id, "");
            pieceColors.put(id, Color.BLACK);
            draw();
        }
    }

    private void draw() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        for (Map.Entry<Integer, BoardNode> entry : nodes.entrySet()) {
            int id = entry.getKey();
            BoardNode node = entry.getValue();
            Point2D p = node.getPosition();
            int x = (int) p.getX(); //바뀜
            int y = (int) p.getY(); //바뀜

            gc.setFill(Color.LIGHTGRAY);
            gc.fillOval(x, y, 40, 40);
            gc.setStroke(Color.DARKGRAY);
            gc.strokeOval(x, y, 40, 40);

            gc.setFill(Color.BLUE);
            gc.setFont(Font.font("맑은 고딕", FontWeight.NORMAL, 10));
            gc.fillText(String.valueOf(id), x + 15, y - 5);

            String text = pieceTexts.getOrDefault(id, "");
            if (!text.isEmpty()) {
                gc.setFill(pieceColors.getOrDefault(id, Color.BLACK));
                gc.setFont(Font.font("맑은 고딕", FontWeight.BOLD, 12));
                gc.fillText(text, x + 5, y + 25);
            }
        }
    }
}
