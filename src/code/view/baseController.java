package code.view;

import java.io.File;

import code.Main;
import javafx.fxml.FXML;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class baseController {
	
	private Main main;
	private Stage stage;
	
	public void setStage(Stage primaryStage) {
		this.stage = primaryStage;
	}
	
	public void setMain(Main main) {
		this.main = main;
	}
	
	@FXML
	private void handleNew() throws Exception {
		main.getChain().remove(1, main.getChain().size());;
		main.setBlockFilePath(null);
	}
	
	@FXML
	private void handleOpen() {
		FileChooser fileChooser = new FileChooser();
		
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XML files(*.xml)", "*.xml");
		fileChooser.getExtensionFilters().add(extFilter);
		
		File file = fileChooser.showOpenDialog(main.getPrimaryStage());
		
		if(file != null) {
			main.loadBlockDataFromFile(file);
		}
	}
	
	@FXML
	private void handleSave() {
        File blockFile = main.getBlockFilePath();
        if (blockFile != null) {
            main.saveBlockDataToFile(blockFile);
        } else {
            handleSaveAs();
        }
    }
	
	@FXML
    private void handleSaveAs() {
        FileChooser fileChooser = new FileChooser();

        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml");
        fileChooser.getExtensionFilters().add(extFilter);

        File file = fileChooser.showSaveDialog(main.getPrimaryStage());

        if (file != null) {
            if (!file.getPath().endsWith(".xml")) {
                file = new File(file.getPath() + ".xml");
            }
            main.saveBlockDataToFile(file);
        }
    }
	
	@FXML
    private void handleAbout() throws ClassNotFoundException {
		main.showAbout();
	}
	
	@FXML
    private void handleCad() {
		main.showCadastro();
	}
	
	@FXML
	private void handleExit() {
		stage.close();
	}
	
	@FXML
	private void minimize() {
	    stage.setIconified(true);
	}
}
