package YutYutGame;

import javax.swing.*;
import java.awt.*;

public class PieceSelectPanel extends JPanel {
    public PieceSelectPanel(JPanel mainPanel, CardLayout cardLayout) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        add(Box.createVerticalGlue());

        JLabel label = new JLabel("말의 개수를 선택하세요", SwingConstants.CENTER);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        label.setFont(new Font("맑은 고딕", Font.BOLD, 36));
        add(label);

        add(Box.createRigidArea(new Dimension(0, 60)));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 0));
        String[] options = {"2개", "3개", "4개", "5개"};

        for (String opt : options) {
            JButton btn = new JButton(opt);
            btn.setPreferredSize(new Dimension(150, 60));
            btn.setFont(new Font("맑은 고딕", Font.BOLD, 24));
            btn.addActionListener(e -> {
                MainApp.selectedPieceCount = Integer.parseInt(opt.replace("개", ""));
                cardLayout.show(mainPanel, "mapSelect");
            });
            buttonPanel.add(btn);
        }

        add(buttonPanel);
        add(Box.createVerticalGlue());
    }
}