package YutYutGame;

import java.util.*;

import static YutYutGame.Board.getAllCarriedPieces;

public class GameController {
    private final Board board;

    public GameController(Board board) {
        this.board = board;
    }

    public MoveResult movePiece(Piece piece, int steps) {
        MoveResult result = new MoveResult();
        BoardNode current = piece.getCurrentNode();

        if (current == null) {
            System.out.println("🚫 이동 실패: currentNode가 null입니다.");
            result.setBlocked(true);
            return result;
        }

        BoardNode next = current;
        int prevId = -1;
        int count = Math.abs(steps);
        int direction = (steps > 0) ? 1 : -1;

        System.out.printf("▶ 말%d 이동 시작: 현재 위치 %d → 목표 거리 %d칸, 방향: %s\n",
                piece.getId(), current.getId(), steps, direction > 0 ? "전진" : "후진");

        for (int i = 0; i < count; i++) {
            int id = next.getId();

            if (direction == 1 && board.shouldEscape(piece, id, piece.hasMoved(), count)) {
                System.out.printf("🏁 말%d 탈출 조건 충족: 위치 %d → 탈출 처리됨\n", piece.getId(), id);
                result.addEscaped(piece);
                for (Piece carried : getAllCarriedPieces(piece)) {
                    result.addEscaped(carried);
                    carried.setFinished(true);
                    carried.setCurrentNode(null);
                    System.out.printf("🧳 업혀있던 말%d도 함께 탈출 처리됨\n", carried.getId());
                }
                piece.setFinished(true);
                piece.setCurrentNode(null);
                return result;
            }

            if (direction == 1) {
                if (i == 0 && board.isForcedDiagonal(id)) {
                    Optional<Integer> forced = board.getForcedNext(id);
                    if (forced.isPresent()) {
                        int forcedId = forced.get();
                        System.out.printf("🔀 강제 지름길 진입: %d → %d\n", id, forcedId);
                        next = board.getNode(forcedId);
                        prevId = id;
                        continue;
                    }
                }

                List<Integer> nextList = board.getNextNodes(id, prevId, board.getNode(id).isCenter(), i);

                if (nextList.isEmpty()) {
                    System.out.println("🚫 다음 노드 없음: 이동 중단");
                    return result;
                }

                next = board.getNode(nextList.get(0));
                prevId = id;
            } else {
                List<Integer> prevList = board.getPrevNodes(id);
                if (prevList.isEmpty()) return result;
                next = board.getNode(prevList.get(0));
            }
        }

        System.out.printf("✅ 말%d 최종 이동 완료: %d → %d\n", piece.getId(), current.getId(), next.getId());

        for (Piece other : new ArrayList<>(next.getPieces())) {
            if (!other.getOwner().equals(piece.getOwner()) && other.hasMoved() && !other.isFinished()) {
                System.out.printf("🎯 말%d이 적 말%d을 잡았습니다.\n", piece.getId(), other.getId());
                result.addCaptured(other);
                result.addAllCaptured(getAllCarriedPieces(other));
            }
        }

        for (Piece captured : result.getCaptured()) {
            BoardNode node = captured.getCurrentNode();
            if (node != null) node.removePiece(captured);
            captured.reset();
            BoardNode startNode = board.getNode(0);
            startNode.addPiece(captured);
            captured.setCurrentNode(startNode);
        }

        if (!result.getCaptured().isEmpty()) {
            result.setCapturedOccurred(true);
        }

        current.removePiece(piece);

        for (Piece other : new ArrayList<>(next.getPieces())) {
            if (other.getOwner().equals(piece.getOwner()) && other.hasMoved() && !other.isFinished()) {
                piece.addCarriedPiece(other);
                piece.getCarriedPieces().addAll(getAllCarriedPieces(other));
                next.removePiece(other);
                other.setCurrentNode(null);
                other.getCarriedPieces().clear();
                System.out.printf("📦 말%d이 말%d을 업었습니다.\n", piece.getId(), other.getId());
            }
        }

        next.addPiece(piece);
        piece.setCurrentNode(next);
        piece.setHasMoved(true);

        return result;
    }
}