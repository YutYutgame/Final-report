package YutYutGameFX;

import javafx.geometry.Point2D;//바뀜 point -> Point2D로 바꿈
import javafx.scene.paint.Color; //바뀜
import java.util.ArrayList;
import java.util.List;

public class BoardNode {
    private final int id;                // 노드 ID (0~28)
    private final Point2D position;        // 보드에서의 실제 좌표 (픽셀 단위)
    private final boolean isCenter;      // 중앙 노드 여부
    private final List<Piece> pieces;    // 이 노드 위에 있는 말들

    public BoardNode(int id, Point2D position, boolean isCenter) {
        this.id = id;
        this.position = position;
        this.isCenter = isCenter;
        this.pieces = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public Point2D getPosition() { //바뀜
        return position;
    }

    public boolean isCenter() {
        return isCenter;
    }

    public List<Piece> getPieces() {
        return pieces;
    }

    public void addPiece(Piece piece) {
        pieces.add(piece);
    }

    public void removePiece(Piece piece) {
        pieces.remove(piece);
    }

    public boolean hasEnemyPiece(Piece piece) {
        return pieces.stream().anyMatch(p -> !p.getOwner().equals(piece.getOwner()));
    }

    public boolean isOccupied() {
        return !pieces.isEmpty();
    }

    public Color getPieceColor() {
        if (!pieces.isEmpty()) {
            return pieces.get(pieces.size() - 1).getColor();
        }
        return null;
    }
}
