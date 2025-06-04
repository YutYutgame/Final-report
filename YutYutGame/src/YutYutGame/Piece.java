package YutYutGame;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Piece {
    private final Color color;
    private final int id;
    private final Player owner;

    private boolean isFinished = false;
    private boolean hasMoved = false;

    private BoardNode currentNode;

    private final List<Piece> carriedPieces = new ArrayList<>();

    public Piece(Color color, int id, Player owner) {
        this.color = color;
        this.id = id + 1;
        this.owner = owner;
    }

    public Color getColor() {
        return color;
    }

    public int getId() {
        return id;
    }

    public Player getOwner() {
        return owner;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public void setFinished(boolean finished) {
        this.isFinished = finished;
    }

    public boolean hasMoved() {
        return hasMoved;
    }

    public void setHasMoved(boolean moved) {
        this.hasMoved = moved;
    }

    public BoardNode getCurrentNode() {
        return currentNode;
    }

    public void setCurrentNode(BoardNode currentNode) {
        this.currentNode = currentNode;
    }

    public List<Piece> getCarriedPieces() {
        return carriedPieces;
    }

    public void addCarriedPiece(Piece piece) {
        carriedPieces.add(piece);
    }

    public void clearCarriedPieces() {
        carriedPieces.clear();
    }

    public void reset() {
        this.isFinished = false;
        this.hasMoved = false;
        this.currentNode = null;
        this.carriedPieces.clear();
    }
}
//바뀜