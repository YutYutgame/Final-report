package YutYutGameFX;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class PlayerSelectPanelFX extends VBox {
    public PlayerSelectPanelFX(MainAppFX mainApp) {
        setSpacing(60);
        setPadding(new Insets(20));
        setAlignment(Pos.CENTER);

        Label label = new Label("플레이어 수를 선택하세요");
        label.setFont(Font.font("맑은 고딕", 36));

        FlowPane buttonPane = new FlowPane();
        buttonPane.setHgap(40);
        buttonPane.setAlignment(Pos.CENTER);

        String[] options = {"2명", "3명", "4명"};

        for (String opt : options) {
            Button btn = new Button(opt);
            btn.setPrefSize(150, 60);
            btn.setFont(Font.font("맑은 고딕", 24));
            btn.setOnAction(e -> {
                MainAppFX.selectedPlayerCount = Integer.parseInt(opt.replace("명", ""));
                mainApp.showPane(mainApp.getPieceSelectPanelFX());
            });
            buttonPane.getChildren().add(btn);
        }

        getChildren().addAll(label, buttonPane);
    }
}
