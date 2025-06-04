package YutYutGame;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;


public abstract class Board {
    protected final Map<Integer, BoardNode> nodes = new HashMap<>();
    
    protected abstract void initNodes();
    protected abstract void initEdges();

    public abstract BoardNode getNode(int id);
    public abstract List<Integer> getNextNodes(int currentId, int prevId, boolean isCenter, int i);
    public abstract List<Integer> getPrevNodes(int nodeId);
    public abstract boolean shouldEscape(Piece piece, int currentNodeId, boolean hasMoved, int stepIndex);

    public Collection<BoardNode> getAllNodes() {
        return nodes.values();
    }

    public Map<Integer, BoardNode> getNodeMap() {
        return nodes;
    }
    
    public abstract boolean isForcedDiagonal(int id);
    public abstract Optional<Integer> getForcedNext(int id);
    
    public static List<Piece> getAllCarriedPieces(Piece piece) {
        List<Piece> all = new ArrayList<>();
        for (Piece p : piece.getCarriedPieces()) {
            all.add(p);
            all.addAll(getAllCarriedPieces(p));
        }
        return all;
    }
    
    public void renderBoard(YutBoardPanel panel) {
        for (BoardNode node : getAllNodes()) {
            List<Piece> pieces = node.getPieces();
            List<Piece> movedPieces = new ArrayList<>();

            for (Piece piece : pieces) {
                if (piece.hasMoved() && !piece.isFinished()) {
                    movedPieces.add(piece);
                }
            }

            if (!movedPieces.isEmpty()) {
                Piece topPiece = movedPieces.get(movedPieces.size() - 1);
                List<Integer> ids = new ArrayList<>();
                ids.add(topPiece.getId());
                for (Piece carried : topPiece.getCarriedPieces()) {
                    ids.add(carried.getId());
                }

                List<String> strIds = new ArrayList<>();
                for (int id : ids) {
                    strIds.add(String.valueOf(id));
                }

                String displayText = "말 " + String.join(",", strIds);
                panel.updatePiecePosition(node.getId(), topPiece.getColor(), displayText);
            } else {
                panel.clearPosition(node.getId());
            }
        }
    }
}

//바뀜
