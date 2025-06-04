package YutYutGame;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.awt.Color;
import java.util.Arrays;


public class YutYutGameJunitTest {
    private GameController controller;
    private Player player1, player2;
    private Piece piece1, piece2;
    private Board board;

    @BeforeEach
    void setup() {
    	player1 = new Player(0, 4, Color.RED);
    	player2 = new Player(1, 4, Color.BLUE);

        board = new SquareBoard(); 
        controller = new GameController(Arrays.asList(player1, player2), board);


        piece1 = player1.getPieces().get(0);
        piece2 = player2.getPieces().get(0);
        board.getStartNode().addPiece(piece1);
        board.getStartNode().addPiece(piece2);
    }

    @Test
    void testThrowYutResultValid() {
        Yut yut = new Yut(); 
        for (int i = 0; i < 100; i++) {
            int result = yut.throwYutRand(); 
            assertTrue(result >= 0 && result <= 5, "윷 결과가 유효한 범위를 벗어났습니다: " + result);
        }
    }


    @Test
    void testPieceMove() {
        BoardNode start = board.getStartNode();
        start.addPiece(piece1);
        piece1.setCurrentNode(start);

        controller.movePiece(piece1, 3);

        assertEquals(3, piece1.getDistanceMoved(), "말이 3칸 이동하지 않았습니다");
    }


    @Test
    void testCatchPiece() {
        BoardNode start = board.getStartNode();
        start.addPiece(piece1);
        start.addPiece(piece2);
        piece1.setCurrentNode(start);
        piece2.setCurrentNode(start);


        controller.movePiece(piece2, 2); // piece2 먼저 이동
        controller.movePiece(piece1, 2); // 같은 위치로 piece1 이동 → piece2 잡힘

        assertEquals(0, piece2.getCurrentNode().getId(), "잡힌 말이 시작점(ID 0)으로 돌아가지 않았습니다");
    }

    @Test
    void testGroupPieces() {
        Piece secondPiece = player1.getPieces().get(1);

        BoardNode start = board.getStartNode();
        start.addPiece(piece1);
        start.addPiece(secondPiece);

        piece1.setCurrentNode(start);
        secondPiece.setCurrentNode(start);

        // 먼저 piece1 이동 (업히는 대상)
        controller.movePiece(piece1, 1);

        // 그 다음 secondPiece가 같은 칸으로 이동해서 업기 시도
        controller.movePiece(secondPiece, 1);

        assertTrue(secondPiece.getCarriedPieces().contains(piece1), "말 업기가 되지 않았습니다");
    }



    @Test
    void testNextTurnChangesPlayer() {
        int initialIndex = controller.getCurrentPlayerIndex();
        controller.forceNextTurn();
        int newIndex = controller.getCurrentPlayerIndex();
        assertNotEquals(initialIndex, newIndex, "턴이 변경되지 않았습니다");
    }

    @Test
    void testWinCondition() {
        for (Piece p : player1.getPieces()) {
        	p.setFinished(true);// 모든 말 완주 처리
        }
        assertTrue(player1.allPiecesFinished(), "모든 말이 완주했는데 승리로 처리되지 않았습니다");
    }
}
