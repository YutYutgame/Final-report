package YutYutGameFX;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;

public class MapSelectPanelFX extends BorderPane {

    public MapSelectPanelFX(MainAppFX mainApp) {
        // 상단 안내 문구
        Label guideLabel = new Label("윷판을 선택하세요");
        guideLabel.setFont(Font.font("맑은 고딕", 36));
        guideLabel.setPadding(new Insets(20));
        setTop(guideLabel);
        BorderPane.setAlignment(guideLabel, Pos.CENTER);

        // 중앙 이미지 선택 영역
        HBox imageBox = new HBox(40);
        imageBox.setPadding(new Insets(20, 40, 20, 40));
        imageBox.setAlignment(Pos.CENTER);

        String[] mapNames = {"사각형", "오각형", "육각형"};

        for (String name : mapNames) {
            VBox singleMap = new VBox(10);
            singleMap.setAlignment(Pos.CENTER);
            singleMap.setStyle("-fx-border-color: black; -fx-border-width: 1px; -fx-padding: 10;");

            String fileName;
            switch (name) {
                case "사각형": fileName = "square.png"; break;
                case "오각형": fileName = "pentagon.png"; break;
                case "육각형": fileName = "hexagon.png"; break;
                default: fileName = null;
            }

            ImageView imageView = new ImageView();
            if (fileName != null) {
                try {
                    Image img = new Image(getClass().getResourceAsStream("/images/" + fileName), 200, 200, true, true);
                    imageView.setImage(img);
                } catch (Exception e) {
                    // 이미지 로드 실패 시 텍스트 표시
                    Label errorLabel = new Label("이미지 오류");
                    singleMap.getChildren().add(errorLabel);
                }
            } else {
                Label noImageLabel = new Label("이미지 없음");
                singleMap.getChildren().add(noImageLabel);
            }

            // 이미지 뷰는 이미지가 없더라도 추가해서 레이아웃 유지
            singleMap.getChildren().add(imageView);

            Button selectBtn = new Button(name);
            selectBtn.setPrefSize(120, 50);
            selectBtn.setFont(Font.font("맑은 고딕", 20));
            selectBtn.setOnAction(e -> {
                MainAppFX.selectedMap = name;
                mainApp.onMapSelected(name);
            });
            singleMap.getChildren().add(selectBtn);

            imageBox.getChildren().add(singleMap);
        }

        setCenter(imageBox);
    }
}
