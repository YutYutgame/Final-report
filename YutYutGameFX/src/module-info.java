module YutYutGameFx {
	requires javafx.controls;
	requires java.desktop;
	
	opens YutYutGameFX to javafx.graphics, javafx.fxml;
}
