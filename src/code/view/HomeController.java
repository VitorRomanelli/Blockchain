package code.view;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import code.Main;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import code.Block;

public class HomeController {

	@FXML
	private TableView<Block> blockTable;

	@FXML
	private TableColumn<Block, String> nomeColumn;

	@FXML
	private TableColumn<Block, Number> numeroColumn;

	@FXML
	private Label nomeLabel;

	@FXML
	private Label numLabel;

	@FXML
	private Label nonceLabel;

	@FXML
	private Label dataLabel;

	@FXML
	private Label conteudoLabel;

	@FXML
	private Label prevHashLabel;

	@FXML
	private Label hashLabel;
	
	@FXML
	private Label validade;
	
	@FXML
	private Label remetLabel;
	
	@FXML
	private Label destLabel;
	
	@SuppressWarnings("unused")
	private Main main;
	
	@FXML
	public Block bloco;
	
	@FXML
	public String conteudo;
	
	public HomeController() {
		bloco = new Block();
	}
	
	@FXML
	private void initialize() {
		nomeColumn.setCellValueFactory(cellData -> cellData.getValue().nomeProperty());
		numeroColumn.setCellValueFactory(cellData -> cellData.getValue().numProperty());
		
		showBlockDetails(null);
		
		validade.setText("Corrente Válida");

		blockTable.getSelectionModel().selectedItemProperty().addListener(
	            (observable, oldValue, newValue) -> showBlockDetails(newValue));
	}
	
	public void setMain(Main main) {
		this.main = main;
	
		blockTable.setItems(main.getChain());
	}
	
	public String Convert(Date date) {
		SimpleDateFormat out = new SimpleDateFormat("HH:mm:ss dd-MM-yyyy");
		
		String result = out.format(date);
		return result;
	}
	
	private void showBlockDetails(Block bloco) {
		if(bloco != null) {
			this.bloco = bloco;
			nomeLabel.setText(bloco.getNome());
			nomeLabel.setTooltip(new Tooltip(nomeLabel.getText()));
			numLabel.setText(bloco.getNumero());
			numLabel.setTooltip(new Tooltip(numLabel.getText()));
			nonceLabel.setText(bloco.getNonce());
			nonceLabel.setTooltip(new Tooltip(nonceLabel.getText()));
			dataLabel.setText(Convert(bloco.getData()));
			dataLabel.setTooltip(new Tooltip(dataLabel.getText()));
			remetLabel.setText(bloco.getRemet());
			remetLabel.setTooltip(new Tooltip(remetLabel.getText()));
			destLabel.setText(bloco.getDest());
			destLabel.setTooltip(new Tooltip(destLabel.getText()));
			conteudoLabel.setText(bloco.getConteudo());
			conteudoLabel.setTooltip(new Tooltip(conteudoLabel.getText()));
			prevHashLabel.setText(bloco.getPrevhash());
			prevHashLabel.setTooltip(new Tooltip(prevHashLabel.getText()));
			hashLabel.setText(bloco.getHash());
			hashLabel.setTooltip(new Tooltip(hashLabel.getText()));
		} else {
			nomeLabel.setText("");
			numLabel.setText("");
			nonceLabel.setText("");
			dataLabel.setText("");
			remetLabel.setText("");
			destLabel.setText("");
			conteudoLabel.setText("");
			prevHashLabel.setText("");
			hashLabel.setText("");
		}
	}
	
	@FXML
	private void handleNovo() {
		Block tempBlock = new Block();
		boolean salvarClicado = main.showNewBlockDialog(tempBlock);
		boolean valido = main.validade();
		
		if(salvarClicado){
			main.getChain().add(tempBlock);

			if (valido) {
				validade.setText("Corrente Válida");
			}else {
				validade.setText("Corrente inválida");
			}
		}
	}
	
	public String getConteudo() {
		return conteudo;
	}
}