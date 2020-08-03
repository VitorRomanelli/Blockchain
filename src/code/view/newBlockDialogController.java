package code.view;

import java.text.SimpleDateFormat;
import java.util.Date;

import code.Block;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class newBlockDialogController {

	@FXML
	private TextField nomeField;

	@FXML
	private TextField remetField;

	@FXML
	private TextField destField;

	@FXML
	private TextArea conteudoArea;
	
	private Stage dialogStage;
	private Block bloco = new Block();
	private boolean salvarClicado = false;
	
	@FXML
	private void initialize() {
		
	}
	
	public void setBlock(Block bloco) {
		this.bloco = bloco;
	}
		
	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}
	
	@FXML
	private void handleExit() {
		dialogStage.close();
	}
	
	@FXML
	private void minimize() {
	    dialogStage.setIconified(true);
	}
	
	public boolean salvar() {
        return salvarClicado;
    }
	
	public String Convert(Date date) {
		SimpleDateFormat out = new SimpleDateFormat("HH:mm:ss dd-MM-yyyy");
		
		String result = out.format(date);
		return result;
	}
	
	@FXML
    private void handleSalvar() {

		if (isInputValid()) {
            bloco.setNome(nomeField.getText());
            bloco.setConteudo(conteudoArea.getText());
            bloco.setRemet(remetField.getText());
            bloco.setDest(destField.getText());
            bloco.setData(new java.util.Date());
            bloco.gerarHash();
            bloco.mineBlock(2);

            salvarClicado = true;
            dialogStage.close();
        }
    }
	
	@FXML
    private void handleCancelar() {
        dialogStage.close();
    }
	
	 private boolean isInputValid() {
	        String errorMessage = "";

	        if (nomeField.getText() == null || nomeField.getText().length() == 0) {
	            errorMessage += "Nome inválido!\n"; 
	        }
	        if (conteudoArea.getText() == null || conteudoArea.getText().length() == 0) {
	            errorMessage += "Conteúdo inválido!\n"; 
	        }
	        if (remetField.getText() == null || remetField.getText().length() == 0) {
	            errorMessage += "Remetente inválido!\n"; 
	        }
	        if (destField.getText() == null || destField.getText().length() == 0) {
	            errorMessage += "Destinatário inválido!\n"; 
	        }

	        
	        if (errorMessage.length() == 0) {
	            return true;
	        } else {
	            Alert alert = new Alert(AlertType.ERROR);
	                      alert.setTitle("Campos Inválidos");
	                      alert.setHeaderText("Por favor, corrija os campos inválidos");
	                      alert.setContentText(errorMessage);
	                      alert.showAndWait();
	                
	            return false;
	        }
	    }
}