package code.view;

import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class AboutController {

	private Stage stage;
	
	@FXML
	private ImageView btnBlock;
	
	@FXML
	private ImageView btnTeam;
	
	@FXML
	private AnchorPane aboutBlock;
	
	@FXML
	private AnchorPane aboutTeam;
	
	public void setStage(Stage stage) {
		this.stage = stage;
	}
	
	@FXML
	public void handleButtonAction(MouseEvent event) {
		if(event.getTarget() == btnBlock) {
			aboutBlock.setVisible(true);
			aboutTeam.setVisible(false);
		} else if(event.getTarget() == btnTeam) {
			aboutTeam.setVisible(true);
			aboutBlock.setVisible(false);
		}
	}
	
	@FXML
	private void handleExit() {
		stage.close();
	}
}
