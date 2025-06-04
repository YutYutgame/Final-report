package YutYutGameFX;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.control.Alert.AlertType;

import java.util.ArrayList;
import java.util.List;

public class GamePanelFX extends BorderPane {

    private PieceIconFX selectedPieceIcon = null;
    private int selectedPiece = -1;
    private VBox pieceSelectPanel;
    private HBox playerPanel;

    private Rule yutRule;
    private Board board;
    private YutBoardPanelFX yutBoardPanel;
    private List<Player> players;
    private GameController controller;
    private YutGamePanelFX yutPanel;

    public GamePanelFX(Board board, YutBoardPanelFX yutBoardPanel, List<Player> players, Yut yut) {
        this.board = board;
        this.yutBoardPanel = yutBoardPanel;
        this.players = players;
        this.controller = new GameController(board);
        this.yutRule = new Rule(players.size(), yut);

        HBox topPanel = new HBox();
        topPanel.setSpacing(20);
        topPanel.setPadding(new Insets(10));

        VBox boardWrapper = new VBox();
        boardWrapper.setAlignment(Pos.CENTER);
        boardWrapper.setPadding(new Insets(10));
        boardWrapper.setStyle("-fx-border-color: black; -fx-border-width: 2;");
        boardWrapper.getChildren().add(yutBoardPanel);
        boardWrapper.setPrefSize(600, 600);
        topPanel.getChildren().add(boardWrapper);

        VBox yutPiecePanel = new VBox();
        yutPiecePanel.setSpacing(10);

        yutPanel = new YutGamePanelFX(yutRule, yut);
        yutPiecePanel.getChildren().add(yutPanel);

        pieceSelectPanel = new VBox();
        pieceSelectPanel.setSpacing(10);
        pieceSelectPanel.setPadding(new Insets(10));
        pieceSelectPanel.setStyle("-fx-border-color: black;");
        yutPiecePanel.getChildren().add(pieceSelectPanel);

        yutPanel.setOnThrowFinished(this::setupPieceSelectionPanel);
        topPanel.getChildren().add(yutPiecePanel);

        this.setCenter(topPanel);

        playerPanel = new HBox();
        playerPanel.setSpacing(10);
        playerPanel.setPadding(new Insets(10));
        playerPanel.setStyle("-fx-border-color: black;");
        this.setBottom(playerPanel);

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
    }

    private void setupPieceSelectionPanel() {
        pieceSelectPanel.getChildren().clear();

        Player currentPlayer = players.get(yutRule.getCurrentPlayerIndex());
        List<Piece> pieces = currentPlayer.getPieces();

        HBox row = new HBox(10);
        List<Button> pieceButtons = new ArrayList<>();

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

            Button pieceButton = new Button("말 " + String.join(",", idList));
            pieceButton.setPrefSize(100, 35);

            int index = i;
            pieceButton.setOnAction(ev -> {
                selectedPieceIcon = new PieceIconFX(currentPlayer.getColor());
                for (Button btn : pieceButtons) {
                    btn.setStyle("");
                }
                selectedPiece = index;
                pieceButton.setStyle("-fx-background-color: " + toHex(currentPlayer.getColor()));
            });

            pieceButtons.add(pieceButton);
            row.getChildren().add(pieceButton);
        }

        if (!canMoveExists && isBackDoOnly && yutRule.getRemainingRolls() == 0) {
            showAlert("적용할 수 있는 말이 없어 턴을 넘깁니다.");
            yutRule.forceNextTurn();
            yutPanel.setUseButtonEnabled(true);
            buildPlayerPanel();
            return;
        }

        pieceSelectPanel.getChildren().add(row);

        Button moveButton = new Button("말 이동");
        moveButton.setPrefSize(200, 40);

        moveButton.setOnAction(e -> {
            if (selectedPieceIcon == null) {
                showAlert("말을 먼저 선택하세요.");
                return;
            }

            if (yutRule.getDistance() == 0) {
                showAlert("윷 결과를 먼저 선택하세요.");
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

            buildPlayerPanel();

            moveButton.setDisable(true);
            selectedPiece = -1;
            selectedPieceIcon = null;

            yutRule.test();
        });

        pieceSelectPanel.getChildren().add(moveButton);
    }

    private void showAlert(String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void updateGameStatus(MoveResult result) {
        if (result.isBlocked()) {
            showAlert("해당 말은 다른 말에 업혀 있어 이동할 수 없습니다.");
            return;
        }

        if (result.hasCapturedOccurred()) {
            yutRule.addRollChance();
        }

        for (Piece captured : result.getCaptured()) {
            showAlert(String.format("🎯 Player %d의 말%d이 잡혔습니다!", captured.getOwner().getId(), captured.getId()));
        }

        for (Piece escaped : result.getEscaped()) {
            showAlert(String.format("🎯 Player %d의 말%d이 탈출했습니다!", escaped.getOwner().getId(), escaped.getId()));
        }

        Player currentPlayer = players.get(yutRule.getCurrentPlayerIndex());
        boolean allFinished = currentPlayer.getPieces().stream().allMatch(Piece::isFinished);

        if (allFinished) {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("게임 종료");
            alert.setHeaderText("🎉 Player " + currentPlayer.getId() + "이(가) 승리했습니다!\n게임을 다시 시작하시겠습니까?");
            alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
            alert.showAndWait().ifPresent(type -> {
                if (type == ButtonType.YES) {
                    MainAppFX.restartGame();
                } else {
                    System.exit(0);
                }
            });
        }
    }

    private void buildPlayerPanel() {
        playerPanel.getChildren().clear();

        for (int i = 0; i < players.size(); i++) {
            Player p = players.get(i);
            VBox pBox = new VBox(5);
            pBox.setAlignment(Pos.CENTER);
            pBox.setPadding(new Insets(5));
            pBox.setStyle("-fx-border-color: " + toHex(p.getColor()) + "; -fx-border-width: 2;");

            Label nameLabel = new Label((i == yutRule.getCurrentPlayerIndex() ? "→ " : "") + "Player " + p.getId());
            nameLabel.setFont(new Font("맑은 고딕", 16));

            HBox pieceIcons = new HBox(5);
            int remaining = (int) p.getPieces().stream().filter(piece -> !piece.isFinished()).count();
            for (Piece piece : p.getPieces()) {
                PieceIconFX icon = new PieceIconFX(Color.LIGHTGRAY);
                if (piece.isFinished()) {
                    icon.setColor(p.getColor());
                }
                pieceIcons.getChildren().add(icon);
            }

            Label countLabel = new Label("남은 말: " + remaining);
            countLabel.setFont(new Font("맑은 고딕", 12));

            pBox.getChildren().addAll(nameLabel, pieceIcons, countLabel);
            playerPanel.getChildren().add(pBox);
        }
    }

    private String toHex(javafx.scene.paint.Color fxColor) {
        return String.format("#%02x%02x%02x",
            (int)(fxColor.getRed() * 255),
            (int)(fxColor.getGreen() * 255),
            (int)(fxColor.getBlue() * 255));
    }

}
