package YutYutGameFX;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class MainAppFX extends Application {
    public static int selectedPlayerCount = 0;
    public static int selectedPieceCount = 0;
    public static String selectedMap = "";

    private static MainAppFX instance;
    private static Stage mainStage;

    private StackPane mainPane;
    private BorderPane startScreen;
    private PlayerSelectPanelFX playerSelectPanelFX;
    private static PieceSelectPanelFX pieceSelectPanelFX;
    private MapSelectPanelFX mapSelectPanelFX;
    private BorderPane gameWrapperPane;

    @Override
    public void start(Stage primaryStage) {
        instance = this;
        mainStage = primaryStage;

        primaryStage.setTitle("윷놀이");

        mainPane = new StackPane();

        // === start screen ===
        startScreen = new BorderPane();
        Label titleLabel = new Label("윷놀이");
        titleLabel.setFont(Font.font("맑은 고딕", 80));
        startScreen.setCenter(titleLabel);

        Button startButton = new Button("시작");
        startButton.setPrefSize(150, 50);
        HBox buttonBox = new HBox(startButton);
        buttonBox.setStyle("-fx-alignment: center; -fx-padding: 20;");
        startScreen.setBottom(buttonBox);

        // === 중간 화면 ===
        playerSelectPanelFX = new PlayerSelectPanelFX(this);
        pieceSelectPanelFX = new PieceSelectPanelFX(this);

        // === 게임 화면 Wrapper ===
        gameWrapperPane = new BorderPane();

        // === 맵 선택 화면 ===
        mapSelectPanelFX = new MapSelectPanelFX(this);

        // 화면 모두 mainPane에 추가하고, 화면 전환은 setVisible로 처리
        mainPane.getChildren().addAll(startScreen, playerSelectPanelFX, pieceSelectPanelFX, mapSelectPanelFX, gameWrapperPane);

        showPane(startScreen);

        // 시작 버튼 이벤트
        startButton.setOnAction(e -> showPane(playerSelectPanelFX));

        Scene scene = new Scene(mainPane, 1280, 720);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // 화면 전환 메서드
    public void showPane(Region paneToShow) {
        for (var child : mainPane.getChildren()) {
            child.setVisible(false);
        }
        paneToShow.setVisible(true);
    }

    // 맵 선택 후 호출될 메서드
    public void onMapSelected(String mapName) {
        selectedMap = mapName;

        Board board = createBoard(mapName);

        YutBoardPanelFX yutBoardPane = new YutBoardPanelFX(board);

        List<Color> colors = List.of(Color.RED, Color.BLUE, Color.GREEN, Color.ORANGE);
        List<Player> players = new ArrayList<>();
        for (int i = 0; i < selectedPlayerCount; i++) {
            players.add(new Player(i + 1, selectedPieceCount, colors.get(i)));
        }

        Yut yut = new Yut();

        GamePanelFX gamePane = new GamePanelFX(board, yutBoardPane, players, yut);

        gameWrapperPane.setCenter(gamePane);

        showPane(gameWrapperPane);
    }


    public static Board createBoard(String mapName) {
        switch (mapName) {
            case "사각형":
                return new SquareBoard();
            case "오각형":
                return new PentagonBoard();
            case "육각형":
                return new HexagonBoard();
            default:
                throw new IllegalArgumentException("지원하지 않는 맵입니다: " + mapName);
        }
    }

    // 게임 재시작 메서드
    public static void restartGame() {
        if (instance != null && mainStage != null) {
            // 선택 초기화
            selectedPlayerCount = 0;
            selectedPieceCount = 0;
            selectedMap = "";

            // 다시 시작 화면 보여주기
            instance.mainPane.getChildren().forEach(node -> node.setVisible(false));
            instance.showPane(instance.startScreen);
        }
    }

    public static PieceSelectPanelFX getPieceSelectPanelFX() {
        return pieceSelectPanelFX;
    }

    public MapSelectPanelFX getMapSelectPaneFX() {
        return mapSelectPanelFX;
    }
    
    public static void main(String[] args) {
        launch();
    }
}
