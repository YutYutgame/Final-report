package YutYutGameFX;

import javafx.geometry.Point2D;

import java.util.*;
import java.util.Optional;

public class SquareBoard extends Board {

    private final Map<Integer, List<Integer>> adjacency = new HashMap<>();

    public SquareBoard() {
        initNodes();
        initEdges();
    }

    @Override
    protected void initNodes() {
        nodes.put(0, new BoardNode(0, new Point2D(485, 430), false));
        nodes.put(1, new BoardNode(1, new Point2D(485, 347), false));
        nodes.put(2, new BoardNode(2, new Point2D(485, 264), false));
        nodes.put(3, new BoardNode(3, new Point2D(485, 181), false));
        nodes.put(4, new BoardNode(4, new Point2D(485, 98), false));
        nodes.put(5, new BoardNode(5, new Point2D(485, 15), false));
        nodes.put(6, new BoardNode(6, new Point2D(402, 15), false));
        nodes.put(7, new BoardNode(7, new Point2D(319, 15), false));
        nodes.put(8, new BoardNode(8, new Point2D(236, 15), false));
        nodes.put(9, new BoardNode(9, new Point2D(153, 15), false));
        nodes.put(10, new BoardNode(10, new Point2D(70, 15), false));
        nodes.put(11, new BoardNode(11, new Point2D(70, 98), false));
        nodes.put(12, new BoardNode(12, new Point2D(70, 181), false));
        nodes.put(13, new BoardNode(13, new Point2D(70, 264), false));
        nodes.put(14, new BoardNode(14, new Point2D(70, 347), false));
        nodes.put(15, new BoardNode(15, new Point2D(70, 430), false));
        nodes.put(16, new BoardNode(16, new Point2D(153, 430), false));
        nodes.put(17, new BoardNode(17, new Point2D(236, 430), false));
        nodes.put(18, new BoardNode(18, new Point2D(319, 430), false));
        nodes.put(19, new BoardNode(19, new Point2D(402, 430), false));
        nodes.put(20, new BoardNode(20, new Point2D(270, 215), true));
        nodes.put(21, new BoardNode(21, new Point2D(210, 155), false));
        nodes.put(22, new BoardNode(22, new Point2D(150, 95), false));
        nodes.put(23, new BoardNode(23, new Point2D(330, 265), false));
        nodes.put(24, new BoardNode(24, new Point2D(390, 325), false));
        nodes.put(25, new BoardNode(25, new Point2D(210, 265), false));
        nodes.put(26, new BoardNode(26, new Point2D(150, 335), false));
        nodes.put(27, new BoardNode(27, new Point2D(380, 95), false));
        nodes.put(28, new BoardNode(28, new Point2D(330, 155), false));
    }

    @Override
    protected void initEdges() {
        link(5, 27); link(27, 28); link(28, 20); link(20, 25); link(25, 26); link(26, 15);
        link(10, 22); link(22, 21); link(21, 20); link(20, 23); link(23, 24); link(24, 0);
    }

    private void link(int from, int to) {
        adjacency.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
    }

    @Override
    public BoardNode getNode(int id) {
        return nodes.get(id);
    }

    @Override
    public List<Integer> getNextNodes(int currentId, int prevId, boolean isCenter, int i) {
        if (isCenter && i == 0) {
            return Collections.singletonList(23);
        } else {
            if (isForcedDiagonal(currentId) && i == 0) {
                return getForcedNext(currentId).map(Collections::singletonList).orElse(Collections.emptyList());
            } else if (isOuterNode(currentId)) {
                return Collections.singletonList((currentId + 1) % 20);
            } else {
                return adjacency.getOrDefault(currentId, Collections.emptyList());
            }
        }
    }

    @Override
    public List<Integer> getPrevNodes(int nodeId) {
        if (isOuterNode(nodeId)) {
            int prevId = (nodeId - 1 + 20) % 20;
            return Collections.singletonList(prevId);
        }
        List<Integer> result = new ArrayList<>();
        for (Map.Entry<Integer, List<Integer>> entry : adjacency.entrySet()) {
            if (entry.getValue().contains(nodeId)) {
                result.add(entry.getKey());
            }
        }
        return result;
    }

    private boolean isOuterNode(int id) {
        return id >= 0 && id <= 19;
    }

    public boolean isForcedDiagonal(int id) {
        return id == 5 || id == 10;
    }

    public Optional<Integer> getForcedNext(int id) {
        if (id == 5) return Optional.of(27);
        if (id == 10) return Optional.of(22);
        return Optional.empty();
    }

    public boolean shouldEscape(Piece piece, int currentNodeId, boolean hasMoved, int stepIndex) {
        if (!hasMoved) return false;

        switch (currentNodeId) {
            case 0: return stepIndex >= 1;
            default: return false;
        }
    }
}