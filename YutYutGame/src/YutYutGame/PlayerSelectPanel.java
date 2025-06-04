package YutYutGame;

import javax.swing.*;
import java.awt.*;

public class PlayerSelectPanel extends JPanel {
    public PlayerSelectPanel(JPanel mainPanel, CardLayout cardLayout) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        add(Box.createVerticalGlue()); // 위 여백

        JLabel label = new JLabel("플레이어 수를 선택하세요", SwingConstants.CENTER);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        label.setFont(new Font("맑은 고딕", Font.BOLD, 36));
        add(label);

        add(Box.createRigidArea(new Dimension(0, 60))); // 간격

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 40, 0));
        String[] options = {"2명", "3명", "4명"};

        for (String opt : options) {
            JButton btn = new JButton(opt);
            btn.setPreferredSize(new Dimension(150, 60));
            btn.setFont(new Font("맑은 고딕", Font.BOLD, 24));
            btn.addActionListener(e -> {
                MainApp.selectedPlayerCount = Integer.parseInt(opt.replace("명", ""));
                cardLayout.show(mainPanel, "pieceSelect");
            });
            buttonPanel.add(btn);
        }

        add(buttonPanel);
        add(Box.createVerticalGlue()); // 아래 여백
    }
}