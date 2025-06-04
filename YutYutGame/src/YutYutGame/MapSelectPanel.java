package YutYutGame;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class MapSelectPanel extends JPanel {

    public MapSelectPanel(JPanel mainPanel, CardLayout cardLayout) {
        setLayout(new BorderLayout());

        // 상단 안내 문구
        JLabel guideLabel = new JLabel("윷판을 선택하세요", SwingConstants.CENTER);
        guideLabel.setFont(new Font("맑은 고딕", Font.BOLD, 36));
        guideLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(guideLabel, BorderLayout.NORTH);

        // 중앙 이미지 선택 영역
        JPanel imagePanel = new JPanel(new GridLayout(1, 3, 40, 0));
        imagePanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        String[] mapNames = {"사각형", "오각형", "육각형"};

        for (String name : mapNames) {
            JPanel singleMap = new JPanel(new BorderLayout(10, 10));
            singleMap.setBorder(BorderFactory.createTitledBorder(new LineBorder(Color.BLACK), name));

            // 이미지 파일명 매핑
            String fileName;
            switch (name) {
                case "사각형":
                    fileName = "square.png";
                    break;
                case "오각형":
                    fileName = "pentagon.png";
                    break;
                case "육각형":
                    fileName = "hexagon.png";
                    break;
                default:
                    fileName = null;
            }

            JLabel imageLabel;
            if (fileName != null) {
                try {
                    ImageIcon icon = new ImageIcon(getClass().getResource("/images/" + fileName));
                    Image img = icon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
                    imageLabel = new JLabel(new ImageIcon(img));
                } catch (Exception e) {
                    imageLabel = new JLabel("이미지 오류", SwingConstants.CENTER);
                }
            } else {
                imageLabel = new JLabel("이미지 없음", SwingConstants.CENTER);
            }

            imageLabel.setPreferredSize(new Dimension(200, 200));
            singleMap.add(imageLabel, BorderLayout.CENTER);

            JButton selectBtn = new JButton(name);
            selectBtn.setPreferredSize(new Dimension(120, 50));
            selectBtn.setFont(new Font("맑은 고딕", Font.BOLD, 20));
            selectBtn.addActionListener(e -> {
                MainApp.selectedMap = name;
                onMapSelected(name);
            });
            singleMap.add(selectBtn, BorderLayout.SOUTH);

            imagePanel.add(singleMap);
        }

        add(imagePanel, BorderLayout.CENTER);
    }

    /**
     * 맵이 선택되었을 때 호출되는 콜백 메서드.
     */
    protected void onMapSelected(String mapName) {
        // override용 메서드
    }
}