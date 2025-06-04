package YutYutGame;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GamePanel extends JPanel {

    private PieceIcon selectedPieceIcon = null;
    private int selectedPiece = -1;
    private JPanel pieceSelectPanel;
    private JPanel playerPanel;

    private Rule yutRule;
    private Board board;
    private YutBoardPanel yutBoardPanel;
    private List<Player> players;
    private GameController controller;
    private YutGamePanel yutPanel;

    public GamePanel(Board board, YutBoardPanel yutBoardPanel, List<Player> players, Yut yut) {
        setLayout(new BorderLayout());
        this.board = board;
        this.yutBoardPanel = yutBoardPanel;
        this.players = players;
        this.controller = new GameController(board);
        this.yutRule = new Rule(players.size(), yut);

        JPanel topPanel = new JPanel(new GridLayout(1, 2));

        JPanel boardWrapper = new JPanel(new BorderLayout());
        boardWrapper.setBorder(BorderFactory.createTitledBorder("윷놀이판"));
        boardWrapper.add(yutBoardPanel, BorderLayout.CENTER);
        boardWrapper.setPreferredSize(new Dimension(500, 500));
        topPanel.add(boardWrapper, BorderLayout.CENTER);

        JPanel yutPiecePanel = new JPanel(new GridLayout(2, 1));
        yutPanel = new YutGamePanel(yutRule, yut);
        yutPiecePanel.add(yutPanel);

        pieceSelectPanel = new JPanel();
        pieceSelectPanel.setLayout(new BoxLayout(pieceSelectPanel, BoxLayout.Y_AXIS));
        pieceSelectPanel.setBorder(BorderFactory.createTitledBorder("말 선택"));
        yutPiecePanel.add(pieceSelectPanel);

        yutPanel.setOnThrowFinished(this::setupPieceSelectionPanel);
        topPanel.add(yutPiecePanel, BorderLayout.EAST);
        add(topPanel, BorderLayout.CENTER);

        playerPanel = new JPanel(new GridLayout(1, players.size(), 10, 10));
        playerPanel.setPreferredSize(new Dimension(0, 130));
        playerPanel.setBorder(BorderFactory.createTitledBorder("플레이어"));
        add(playerPanel, BorderLayout.SOUTH);

        initializeGame();
    }

    private void initializeGame() {
        buildPlayerPanel();
        BoardNode startNode = board.getNode(0);
        for (Player player : players) {
            for (Piece piece : player.getPieces()) {
                piece.reset();
                piece.setCurrentNode(startNode);
                startNode.addPiece(piece);
            }
        }
        board.renderBoard(yutBoardPanel);
        yutBoardPanel.revalidate();
        yutBoardPanel.repaint();
    }

    private void setupPieceSelectionPanel() {
        pieceSelectPanel.removeAll();
        pieceSelectPanel.setLayout(new BoxLayout(pieceSelectPanel, BoxLayout.Y_AXIS));

        Player currentPlayer = players.get(yutRule.getCurrentPlayerIndex());
        List<Piece> pieces = currentPlayer.getPieces();

        JPanel rowPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        List<JButton> pieceButtons = new ArrayList<>();

        boolean isBackDoOnly = (yutRule.getDistance() == -1);
        boolean canMoveExists = false;

        for (int i = 0; i < pieces.size(); i++) {
            Piece piece = pieces.get(i);
            if (piece.isFinished() || piece.getCurrentNode() == null) continue;
            if (isBackDoOnly && !piece.hasMoved()) continue;

            canMoveExists = true;

            List<String> idList = new ArrayList<>();
            idList.add(String.valueOf(piece.getId()));
            for (Piece carried : piece.getCarriedPieces()) {
                idList.add(String.valueOf(carried.getId()));
            }

            JButton pieceButton = new JButton("말 " + String.join(",", idList));
            pieceButton.setPreferredSize(new Dimension(100, 35));

            int index = i;
            pieceButton.addActionListener(ev -> {
                selectedPieceIcon = new PieceIcon(currentPlayer.getColor());
                for (JButton btn : pieceButtons) {
                    btn.setBackground(null);
                }
                selectedPiece = index;
                pieceButton.setBackground(currentPlayer.getColor());
            });

            pieceButtons.add(pieceButton);
            rowPanel.add(pieceButton);
        }

        if (!canMoveExists && isBackDoOnly && yutRule.getRemainingRolls() == 0) {
            JOptionPane.showMessageDialog(this, "적용할 수 있는 말이 없어 턴을 넘깁니다.");
            yutRule.forceNextTurn();
            yutPanel.setUseButtonEnabled(true);
            buildPlayerPanel();
            return;
        }
        	

        pieceSelectPanel.add(rowPanel);

        JButton moveButton = new JButton("말 이동");
        moveButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        moveButton.setPreferredSize(new Dimension(200, 40));
        moveButton.setMaximumSize(new Dimension(200, 40));

        moveButton.addActionListener(e -> {
            if (selectedPieceIcon == null) {
                JOptionPane.showMessageDialog(this, "말을 먼저 선택하세요.");
                return;
            }

            if (yutRule.getDistance() == 0) {
                JOptionPane.showMessageDialog(this, "윷 결과를 먼저 선택하세요.");
                return;
            }

            Player activePlayer = players.get(yutRule.getCurrentPlayerIndex());
            Piece movingPiece = activePlayer.getPieces().get(selectedPiece);

            MoveResult result = controller.movePiece(movingPiece, yutRule.getDistance());
            updateGameStatus(result);            
            yutPanel.setUseButtonEnabled(true);

            board.renderBoard(yutBoardPanel);

            yutRule.markPieceMoved(true);
            yutRule.changePlayerIfTurnDone();

            yutBoardPanel.revalidate();
            yutBoardPanel.repaint();

            Container boardWrapper = yutBoardPanel.getParent();
            if (boardWrapper != null) {
                boardWrapper.revalidate();
                boardWrapper.repaint();
            }

            buildPlayerPanel();

            moveButton.setEnabled(false);
            selectedPiece = -1;
            selectedPieceIcon = null;
            
        });

        pieceSelectPanel.add(Box.createVerticalStrut(10));
        pieceSelectPanel.add(moveButton);

        pieceSelectPanel.revalidate();
        pieceSelectPanel.repaint();
    }

    public void updateGameStatus(MoveResult result) {
        if (result.isBlocked()) {
            JOptionPane.showMessageDialog(this, "해당 말은 다른 말에 업혀 있어 이동할 수 없습니다.");
            return;
        }

        if (result.hasCapturedOccurred()) {
            yutRule.addRollChance();
        }

        for (Piece captured : result.getCaptured()) {
            JOptionPane.showMessageDialog(this,
                    String.format("🎯 Player %d의 말%d이 잡혔습니다!",
                            captured.getOwner().getId(), captured.getId()));
        }

        for (Piece escaped : result.getEscaped()) {
            JOptionPane.showMessageDialog(this,
                    String.format("🎯 Player %d의 말%d이 탈출했습니다!",
                            escaped.getOwner().getId(), escaped.getId()));
        }

        Player currentPlayer = players.get(yutRule.getCurrentPlayerIndex());
        boolean allFinished = currentPlayer.getPieces().stream()
                .allMatch(Piece::isFinished);

        if (allFinished) {
            int option = JOptionPane.showOptionDialog(this,
                    "🎉 Player " + currentPlayer.getId() + "이(가) 승리했습니다!\n게임을 다시 시작하시겠습니까?",
                    "게임 종료",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    new Object[]{"재시작", "종료"},
                    "재시작");

            if (option == JOptionPane.YES_OPTION) {
                SwingUtilities.getWindowAncestor(this).dispose();
                SwingUtilities.invokeLater(() -> MainApp.main(new String[]{}));
            } else {
                System.exit(0);
            }
        }
    }

    private void buildPlayerPanel() {
        playerPanel.removeAll();

        for (int i = 0; i < players.size(); i++) {
            Player p = players.get(i);
            JPanel pPanel = new JPanel(new BorderLayout());
            pPanel.setBorder(BorderFactory.createLineBorder(p.getColor(), 2));

            JLabel playerLabel = new JLabel(
                    (i == yutRule.getCurrentPlayerIndex() ? "→ " : "") + "Player " + p.getId(),
                    SwingConstants.CENTER
            );
            playerLabel.setFont(new Font("맑은 고딕", Font.BOLD, 16));

            JPanel piecesPanel = new JPanel();
            int remaining = (int) p.getPieces().stream().filter(piece -> !piece.isFinished()).count();
            for (Piece piece : p.getPieces()) {
                PieceIcon icon = new PieceIcon(Color.LIGHT_GRAY);
                if (piece.isFinished()) {
                    icon.setColor(p.getColor()); // 나간 말은 회색
                    icon.setOpaque(true);
                }
                piecesPanel.add(icon);
            }

            JLabel countLabel = new JLabel("남은 말: " + remaining, SwingConstants.CENTER);
            countLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 12));

            pPanel.add(playerLabel, BorderLayout.NORTH);
            pPanel.add(piecesPanel, BorderLayout.CENTER);
            pPanel.add(countLabel, BorderLayout.SOUTH);

            playerPanel.add(pPanel);
        }

        playerPanel.revalidate();
        playerPanel.repaint();
    }
}