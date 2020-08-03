package code.view;

import java.util.List;
import code.Main;
import code.User;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class loginController {
		
	private Main main;
	private Stage stage;
	private List <User> usuarios;
	
	@FXML
	private TextField usuario;
	
	@FXML
	private PasswordField senha;
	
	public void setStage(Stage primaryStage) {
		this.stage = primaryStage;
	}
	
	public void setMain(Main main) {
		this.main = main;
	}
	
	public void setUser(List <User> usuarios) {
		this.usuarios = usuarios;
	}
	
	@FXML
	private void handleEntrar() {
		boolean log = false;
		for(User c : usuarios) {
			if(usuario.getText().equals(c.getUsuario()) && senha.getText().equals(c.getSenha())) {
				stage.close();
				main.initBaseLayout();
				main.showHome();
				log = true;
			}
		}
		if(log == false) {
			Alert alert = new Alert(AlertType.ERROR);
	         alert.setTitle("Campos Inválidos");
	         alert.setHeaderText("Por favor, corrija os campos inválidos");
	         alert.setContentText("Login ou Senha inválidos");
	         alert.showAndWait();
		}
	}
	
	
	@FXML
	private void handleExit() {
		stage.close();
	}
}