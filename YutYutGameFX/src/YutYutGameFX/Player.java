package YutYutGameFX;

import javafx.scene.paint.Color;
import java.util.ArrayList;
import java.util.List;

public class Player {
    private int id;
    private Color color;
    private List<Piece> pieces;

    public Player(int id, int pieceCount, Color color) {
        this.id = id;
        this.color = color;
        this.pieces = new ArrayList<>();

        // 각 말에 this (Player 객체)를 전달
        for (int i = 0; i < pieceCount; i++) {
            pieces.add(new Piece(color, i, this));
        }
    }

    public int getId() {
        return id;
    }

    public Color getColor() {
        return color;
    }

    public List<Piece> getPieces() {
        return pieces;
    }

    public void resetPiece(Piece piece) {
        piece.reset();
        piece.setCurrentNode(null);
    }
}
