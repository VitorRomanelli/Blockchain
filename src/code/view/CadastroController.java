package code.view;

import code.Main;
import code.User;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class CadastroController {
	
	private Main main;
	
	private List <User> usuarios;
	
	@FXML
	private Stage cadStage;
	
	@FXML
	private AnchorPane erro;

	@FXML
	private AnchorPane sucesso;
	
	@FXML
	private TextField usuario;
	
	@FXML
	private TextField senha;
	
	@FXML
	private TextField senha2;
	
	public void setDados(Main main, List <User> usuarios) {
		this.main = main;
		this.usuarios = usuarios;
	}
	
	public void setStage(Stage cadStage) {
		this.cadStage = cadStage;
	}
	
	@FXML
	private void handleExit() {
		cadStage.close();
	}
	
	@FXML
	private void handleSair() {
		erro.setVisible(false);
		sucesso.setVisible(false);
	}
	
	@FXML
	private void handleCadastrar() {
		boolean b = true;
		
		for(User c : usuarios) {
			if(usuario.getText().equals(c.getUsuario())) {
				erro.setVisible(true);
				b = false;
			} 
		}
		
		if(b == true) {	
			if(senha.getText().equals(senha2.getText())) {
				User user = new User();
				user.setUsuario(usuario.getText());
				user.setSenha(senha.getText());
				main.addUsuario(user);
				sucesso.setVisible(true);
			} else {
				Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Senha inserida incorretamente");
                alert.setHeaderText("Por favor, corrija os campos inválidos");
                alert.setContentText("A senha inserida não bate com a senha de confirmação");
                alert.showAndWait();
			}
		}
	}
}
