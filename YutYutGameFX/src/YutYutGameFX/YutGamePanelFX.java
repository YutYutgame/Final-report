package YutYutGameFX;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;

import java.util.List;
import java.util.Optional;

public class YutGamePanelFX extends HBox {

    private final ListView<String> resultListView = new ListView<>();
    private final Label resultLabel = new Label("결과: ");
    private final Yut yut;

    private Runnable onThrowFinished;
    private final Rule yutRule;
    private Button useButton;

    public YutGamePanelFX(Rule yutRule, Yut yut) {
        this.yutRule = yutRule;
        this.yut = yut;

        this.setSpacing(20);
        this.setPadding(new Insets(10));
        this.getChildren().addAll(createYutPanel(), createResultPanel());
    }

    public void setOnThrowFinished(Runnable callback) {
        this.onThrowFinished = callback;
    }

    public void setUseButtonEnabled(boolean enabled) {
        if (useButton != null) {
            useButton.setDisable(!enabled);
        }
    }

    private void triggerCallbackIfSet() {
        if (onThrowFinished != null) {
            onThrowFinished.run();
        }
    }

    private VBox createYutPanel() {
        VBox yutPanel = new VBox(10);
        yutPanel.setPadding(new Insets(10));
        yutPanel.setStyle("-fx-border-color: black; -fx-border-width: 2; -fx-border-radius: 5;");
        yutPanel.setPrefWidth(200);

        Label title = new Label("윷 던지기");
        title.setFont(new Font("맑은 고딕", 20));

        resultLabel.setFont(new Font("맑은 고딕", 18));

        Button fixButton = new Button("결과 선택");
        Button randButton = new Button("무작위 던지기");

        fixButton.setOnAction(e -> {
            ChoiceDialog<String> dialog = new ChoiceDialog<>(yut.getOutcomes()[0], yut.getOutcomes());
            dialog.setTitle("윷 결과 선택");
            dialog.setHeaderText("결과를 선택하세요:");
            Optional<String> result = dialog.showAndWait();
            result.ifPresent(this::updateResult);
        });

        randButton.setOnAction(e -> {
            if (yutRule.getRemainingRolls() <= 0) {
                showAlert("남은 윷 던지기 기회가 없습니다.");
                return;
            }

            yutRule.useRollChance();

            int idx = yut.throwYutRand();
            String selected = yut.getOutcomes()[idx];
            updateResult(selected);

            if (idx == 4 || idx == 5) {
                showAlert(selected + "이 나와 한 번 더 던질 수 있습니다!");
                yutRule.addRollChance();
            }

            triggerCallbackIfSet();
        });

        yutPanel.getChildren().addAll(title, resultLabel, fixButton, randButton);
        return yutPanel;
    }

    private VBox createResultPanel() {
        VBox panel = new VBox(10);
        panel.setPadding(new Insets(10));
        panel.setStyle("-fx-border-color: black; -fx-border-width: 2; -fx-border-radius: 5;");
        panel.setPrefWidth(250);

        Label title = new Label("저장된 결과");
        title.setFont(new Font(16));

        useButton = new Button("선택한 결과 사용");
        useButton.setDisable(false);

        useButton.setOnAction(e -> {
            String selected = resultListView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                int idx = yut.getIdxOutcomes(selected);
                yutRule.setDistance(idx);
                resultListView.getItems().remove(selected);
                yut.useResult(idx);

                showAlert("사용한 결과: " + selected);

                if (yut.isAllUsed()) {
                    yutRule.markYutUsedUp();
                }

                useButton.setDisable(true);
                triggerCallbackIfSet();
            } else {
                showAlert("결과를 선택해주세요.");
            }
        });

        panel.getChildren().addAll(title, resultListView, useButton);
        return panel;
    }

    private void updateResult(String selected) {
        resultLabel.setText("결과: " + selected);
        resultListView.getItems().add(selected);
        yut.setSelectedResult(yut.getIdxOutcomes(selected));
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("안내");
        alert.setContentText(msg);
        alert.showAndWait();
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
