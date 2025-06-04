package YutYutGameFX;

import javafx.geometry.Point2D;
import java.util.*;

public class HexagonBoard extends Board {

    private final Map<Integer, List<Integer>> adjacency = new HashMap<>();

    private int base_x = 370, base_y = 450, p_idx = 0;
    private List<Point2D> hexagonPoints = new ArrayList<>(49);
    private Piece[] piecesOnBoard = new Piece[49];

    public HexagonBoard() {
        initNodes();
        initEdges();
    }

    @Override
    protected void initNodes() {
        nodes.put(0, new BoardNode(0, new Point2D(485, 430), false));

        for (int i = 0; i < 49; i++) {
            piecesOnBoard[i] = null;
        }

        for (; p_idx < 5; p_idx++) {
            nodes.put(p_idx, new BoardNode(p_idx, new Point2D(base_x, base_y), false));
            base_x += 30;
            base_y -= 40;
        }

        for (; p_idx < 10; p_idx++) {
            nodes.put(p_idx, new BoardNode(p_idx, new Point2D(base_x, base_y), false));
            base_x -= 30;
            base_y -= 40;
        }

        for (; p_idx < 15; p_idx++) {
            nodes.put(p_idx, new BoardNode(p_idx, new Point2D(base_x, base_y), false));
            base_x -= 40;
        }

        for (; p_idx < 20; p_idx++) {
            nodes.put(p_idx, new BoardNode(p_idx, new Point2D(base_x, base_y), false));
            base_x -= 30;
            base_y += 40;
        }

        for (; p_idx < 25; p_idx++) {
            nodes.put(p_idx, new BoardNode(p_idx, new Point2D(base_x, base_y), false));
            base_x += 30;
            base_y += 40;
        }

        for (; p_idx < 30; p_idx++) {
            nodes.put(p_idx, new BoardNode(p_idx, new Point2D(base_x, base_y), false));
            base_x += 40;
        }

        base_x = 260;
        base_y = 250;
        nodes.put(p_idx, new BoardNode(p_idx, new Point2D(base_x, base_y), true));
        p_idx++;

        setShortCut(30, -50);
        setShortCut(60, 0);
        setShortCut(30, 50);
        setShortCut(-20, 50);
        setShortCut(-60, 0);
        setShortCut(-20, -50);
    }

    @Override
    protected void initEdges() {
        link(5, 36); link(36, 35); link(35, 34); link(34, 30);
        link(10, 33); link(33, 32); link(32, 31); link(31, 30);
        link(15, 48); link(48, 47); link(47, 46); link(46, 30);
        link(20, 45); link(45, 44); link(44, 43); link(43, 30);

        link(30, 40); link(40, 41); link(41, 42); link(42, 25);
        link(30, 37); link(37, 38); link(38, 39); link(39, 0);
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
            return Collections.singletonList(37);
        } else {
            if (isForcedDiagonal(currentId) && i == 0) {
                System.out.println("▶ currentId = " + currentId + ", isForcedDiagonal = " + isForcedDiagonal(currentId));
                return getForcedNext(currentId).map(Collections::singletonList).orElse(Collections.emptyList());
            } else if (isOuterNode(currentId)) {
                return Collections.singletonList((currentId + 1) % 30);
            } else {
                return adjacency.getOrDefault(currentId, Collections.emptyList());
            }
        }
    }

    @Override
    public List<Integer> getPrevNodes(int nodeId) {
        if (isOuterNode(nodeId)) {
            int prevId = (nodeId - 1 + 30) % 30;
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
        return id >= 0 && id <= 29;
    }

    public boolean isForcedDiagonal(int id) {
        return id == 5 || id == 10 || id == 15 || id == 20;
    }

    public Optional<Integer> getForcedNext(int id) {
        if (id == 5) return Optional.of(36);
        else if (id == 10) return Optional.of(33);
        else if (id == 15) return Optional.of(48);
        else if (id == 20) return Optional.of(45);
        return Optional.empty();
    }

    public boolean shouldEscape(Piece piece, int currentNodeId, boolean hasMoved, int stepIndex) {
        if (!hasMoved) return false;

        switch (currentNodeId) {
            case 0: return stepIndex >= 1;
            default: return false;
        }
    }

    private void setShortCut(int xx, int yy) {
        int short_idx = p_idx + 3;
        base_x = 260;
        base_y = 250;
        for (; p_idx < short_idx; p_idx++) {
            base_x += xx;
            base_y += yy;
            nodes.put(p_idx, new BoardNode(p_idx, new Point2D(base_x, base_y), false));
        }
    }
}