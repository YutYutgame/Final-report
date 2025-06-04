package YutYutGame;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MainApp {
    public static int selectedPlayerCount = 0;
    public static int selectedPieceCount = 0;
    public static String selectedMap = "";

    public static void main(String[] args) {
        JFrame frame = new JFrame("윷놀이");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1280, 720);

        CardLayout cardLayout = new CardLayout();
        JPanel mainPanel = new JPanel(cardLayout);

        // === start screen 구성 ===
        JPanel startScreen = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("윷놀이", SwingConstants.CENTER);
        titleLabel.setFont(new Font("맑은 고딕", Font.BOLD, 80));
        startScreen.add(titleLabel, BorderLayout.CENTER);

        JButton startButton = new JButton("시작");
        startButton.setPreferredSize(new Dimension(150, 50));
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(startButton);
        startScreen.add(buttonPanel, BorderLayout.SOUTH);

        // === 중간 화면 ===
        PlayerSelectPanel playerSelectPanel = new PlayerSelectPanel(mainPanel, cardLayout);
        PieceSelectPanel pieceSelectPanel = new PieceSelectPanel(mainPanel, cardLayout);

        // === GamePanel 감싸는 Wrapper ===
        JPanel gameWrapperPanel = new JPanel(new BorderLayout());
        mainPanel.add(gameWrapperPanel, "game");

        // === 맵 선택 화면 ===
        MapSelectPanel mapSelectPanel = new MapSelectPanel(mainPanel, cardLayout) {
            @Override
            protected void onMapSelected(String mapName) {
                selectedMap = mapName;

                Board board = createBoard(mapName);             
                YutBoardPanel yutBoardPanel = new YutBoardPanel(board);   // 2. 주입해서 전달
                
                // 2. 플레이어 생성
                List<Color> colors = new ArrayList<>();
                colors.add(Color.RED);
                colors.add(Color.BLUE);
                colors.add(Color.GREEN);
                colors.add(Color.ORANGE);
                List<Player> players = new ArrayList<>();
                for (int i = 0; i < selectedPlayerCount; i++) {
                    players.add(new Player(i + 1, selectedPieceCount, colors.get(i)));
                }

                // 3. 윷 객체 생성
                Yut yut = new Yut();

                // 4. GamePanel 생성 및 삽입
                GamePanel gamePanel = new GamePanel(board, yutBoardPanel, players, yut);
                gameWrapperPanel.removeAll();
                gameWrapperPanel.add(gamePanel, BorderLayout.CENTER);
                gameWrapperPanel.revalidate();
                gameWrapperPanel.repaint();

                // 5. 화면 전환
                cardLayout.show(mainPanel, "game");
            }
        };

        // === 화면 등록 ===
        mainPanel.add(startScreen, "start");
        mainPanel.add(playerSelectPanel, "playerSelect");
        mainPanel.add(pieceSelectPanel, "pieceSelect");
        mainPanel.add(mapSelectPanel, "mapSelect");

        // 시작 버튼 액션
        startButton.addActionListener(e -> cardLayout.show(mainPanel, "playerSelect"));

        frame.getContentPane().add(mainPanel);
        cardLayout.show(mainPanel, "start");
        frame.setVisible(true);
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
} //바뀜