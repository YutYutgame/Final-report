package YutYutGameFX;

import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

public class PieceIconFX extends Region {
    private final Canvas canvas;
    private Color color;

    public PieceIconFX(Color color) {
        this.color = color;
        this.canvas = new Canvas(24, 24);
        getChildren().add(canvas);
        draw();
    }

    public void setColor(Color color) {
        this.color = color;
        draw();
    }

    private void draw() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.setFill(color);
        gc.fillOval(2, 2, canvas.getWidth() - 4, canvas.getHeight() - 4);
        gc.setStroke(Color.DARKGRAY);
        gc.strokeOval(2, 2, canvas.getWidth() - 4, canvas.getHeight() - 4);
    }
}
