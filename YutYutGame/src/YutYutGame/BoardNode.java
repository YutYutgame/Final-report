package YutYutGame;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class BoardNode {
    private final int id;                // 노드 ID (0~28)
    private final Point position;        // 보드에서의 실제 좌표 (픽셀 단위)
    private final boolean isCenter;      // 중앙 노드 여부
    private final List<Piece> pieces;    // 이 노드 위에 있는 말들

    public BoardNode(int id, Point position, boolean isCenter) {
        this.id = id;
        this.position = position;
        this.isCenter = isCenter;
        this.pieces = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public Point getPosition() {
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
// 여기 바뀜