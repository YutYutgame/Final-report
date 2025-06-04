package YutYutGameFX;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class PieceSelectPanelFX extends VBox {
    public PieceSelectPanelFX(MainAppFX mainApp) {
        setSpacing(60);
        setPadding(new Insets(20));
        setAlignment(Pos.CENTER);

        Label label = new Label("말의 개수를 선택하세요");
        label.setFont(Font.font("맑은 고딕", 36));

        FlowPane buttonPane = new FlowPane();
        buttonPane.setHgap(40);
        buttonPane.setAlignment(Pos.CENTER);

        String[] options = {"2개", "3개", "4개", "5개"};

        for (String opt : options) {
            Button btn = new Button(opt);
            btn.setPrefSize(150, 60);
            btn.setFont(Font.font("맑은 고딕", 24));
            btn.setOnAction(e -> {
                MainAppFX.selectedPieceCount = Integer.parseInt(opt.replace("개", ""));
                mainApp.showPane(mainApp.getMapSelectPaneFX());
            });
            buttonPane.getChildren().add(btn);
        }

        getChildren().addAll(label, buttonPane);
    }
}
