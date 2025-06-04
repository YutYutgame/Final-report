package YutYutGame;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class YutGamePanel extends JPanel {

    private final DefaultListModel<String> resultModel = new DefaultListModel<>();
    private final JList<String> resultList = new JList<>(resultModel);
    private final JLabel resultLabel = new JLabel("결과: ");
    private final Yut yut ;

    private Runnable onThrowFinished;
    private final Rule yutRule;
    private JButton useButton;

    public YutGamePanel(Rule yutRule, Yut yut) {
        this.yutRule = yutRule;
        this.yut = yut;
        setLayout(new GridLayout(1, 2));
        add(createYutPanel());
        add(createResultPanel());
    }

    public void setOnThrowFinished(Runnable callback) {
        this.onThrowFinished = callback;
    }

    public void setUseButtonEnabled(boolean enabled) {
        if (useButton != null) {
            useButton.setEnabled(enabled);
        }
    }

    private void triggerCallbackIfSet() {
        if (onThrowFinished != null) {
            onThrowFinished.run();
        }
    }

    private JPanel createYutPanel() {
        JPanel yutPanel = new JPanel();
        yutPanel.setLayout(new BoxLayout(yutPanel, BoxLayout.Y_AXIS));
        yutPanel.setBorder(BorderFactory.createTitledBorder("윷 던지기"));

        resultLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        resultLabel.setFont(new Font("맑은 고딕", Font.BOLD, 18));

        JButton fixButton = new JButton("결과 선택");
        JButton randButton = new JButton("무작위 던지기");

        fixButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        randButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        fixButton.addActionListener(e -> {
            String selected = (String) JOptionPane.showInputDialog(
                    this, "결과를 선택하세요:", "윷 결과 선택",
                    JOptionPane.PLAIN_MESSAGE, null,
                    yut.getOutcomes(), yut.getOutcomes()[0]
            );
            if (selected != null) {
                updateResult(selected);
            }
        });

        randButton.addActionListener(e -> {
            if (yutRule.getRemainingRolls() <= 0) {
                JOptionPane.showMessageDialog(this, "남은 윷 던지기 기회가 없습니다.");
                return;
            }

            yutRule.useRollChance();

            int idx = yut.throwYutRand();
            String selected = yut.getOutcomes()[idx];
            updateResult(selected);

            if (idx == 4 || idx == 5) {
                JOptionPane.showMessageDialog(this, selected + "이 나와 한 번 더 던질 수 있습니다!");
                yutRule.addRollChance();
            }

            triggerCallbackIfSet();
        });

        yutPanel.add(Box.createVerticalStrut(30));
        yutPanel.add(resultLabel);
        yutPanel.add(Box.createVerticalStrut(20));
        yutPanel.add(fixButton);
        yutPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        yutPanel.add(randButton);

        return yutPanel;
    }

    private JPanel createResultPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("저장된 결과"));

        resultList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(resultList);
        panel.add(scrollPane, BorderLayout.CENTER);

        useButton = new JButton("선택한 결과 사용");

        useButton.addActionListener(e -> {
            String selected = resultList.getSelectedValue();
            if (selected != null) {
                int idx = yut.getIdxOutcomes(selected);
                yutRule.setDistance(idx);
                resultModel.removeElement(selected);
                yut.useResult(idx);

                JOptionPane.showMessageDialog(this, "사용한 결과: " + selected);

                if (yut.isAllUsed()) {
                    yutRule.markYutUsedUp();
                }

                useButton.setEnabled(false);

                triggerCallbackIfSet();
            } else {
                JOptionPane.showMessageDialog(this, "결과를 선택해주세요.");
            }
        });

        panel.add(useButton, BorderLayout.SOUTH);
        return panel;
    }

    private void updateResult(String selected) {
        resultLabel.setText("결과: " + selected);
        resultModel.addElement(selected);
        yut.setSelectedResult(yut.getIdxOutcomes(selected));
    }

    public void incrementRerollCount() {
        yutRule.addRollChance();
    }

    public void enableUseButtonAfterMove() {
        setUseButtonEnabled(true);
    }

    public List<Integer> getResultList() {
        return yut.getResultList();
    }
}