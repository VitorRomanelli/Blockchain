package code;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import code.network.Cliente;
import code.view.AboutController;
import code.view.CadastroController;
import code.view.HomeController;
import code.view.baseController;
import code.view.loginController;
import code.view.newBlockDialogController;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

public class Main extends Application{

	private Stage primaryStage;
	private BorderPane baseLayout;
	private Blockchain b = new Blockchain();
	private ObservableList<Block> chain = FXCollections.observableArrayList(b.getCorrente());
	private List<SerialBlock> serialChain;
	private List<User> usuarios;

	public static int prefix = 2;
		
	private double xOffset = 0;
	private double yOffset = 0;
	
	public Main(){
		usuarios = new ArrayList<User>();
		serialChain = new ArrayList<SerialBlock>();
		
		usuarios = lerArquivo();
		if(usuarios.size() == 0){
			User pessoa = new User();
			pessoa.setUsuario("admblock");
			pessoa.setSenha("4321");
			
			usuarios.add(pessoa);
			arquivar(usuarios);
		}
	}

	public ObservableList<Block> getChain(){
		return chain;
	}
	
	public void setChain(List<Block> chain){
		this.chain = (ObservableList<Block>) chain;
		validade();
	}
	
	public List<SerialBlock> getSerialChain(){
		return serialChain;
	}
	
	public void setSerialChain(List<SerialBlock> blocos){
		this.serialChain = blocos;
	}
	
	public void addUsuario(User user) {
		usuarios.add(user);
		arquivar(usuarios);
	}
	
	public void convertChain() {
		serialChain.clear();
		for(int i = 0; i < chain.size(); i++) {
			SerialBlock copiador = new SerialBlock(chain.get(i));
			serialChain.add(copiador);
		}
	}
	
	public void convertSerial() {
		chain.clear();
		for(int i = 0; i < serialChain.size(); i++) {
			Block copiador = new Block(serialChain.get(i));
			chain.add(copiador);
		}
	}
	
	public void start(Stage primaryStage) throws Exception {
		this.primaryStage = primaryStage;
		this.primaryStage.initStyle(StageStyle.TRANSPARENT);
		this.primaryStage.setTitle("Blockchain");
		this.primaryStage.getIcons().add(new Image("file:resources/images/blockchain.png"));
		
		initLogin();
		
	}
	
	public void initLogin() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(this.getClass().getResource("view/login.fxml"));
			AnchorPane page = (AnchorPane) loader.load();
			
			Scene cena = new Scene(page);
			primaryStage.setScene(cena);
			
			page.setOnMousePressed(new EventHandler<MouseEvent>() {
				public void handle(MouseEvent event) {
					xOffset = event.getSceneX();
					yOffset = event.getSceneY();
				}
			});
			
			page.setOnMouseDragged(new EventHandler<MouseEvent>() {
				public void handle(MouseEvent event) {
					primaryStage.setX(event.getScreenX() - xOffset);
					primaryStage.setY(event.getScreenY() - yOffset);
				}
			});
			
			loginController controller = loader.getController();
			controller.setStage(primaryStage);
			controller.setMain(this);
			controller.setUser(usuarios);
		
			primaryStage.setScene(cena);
			primaryStage.show();
			
		} catch (IOException erro) {
			erro.printStackTrace();
		}
	}
		
	public void initBaseLayout() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(this.getClass().getResource("view/Base.fxml"));
			baseLayout = (BorderPane) loader.load();
			
			Scene cena = new Scene(baseLayout);
			primaryStage.setScene(cena);
			
			baseLayout.setOnMousePressed(new EventHandler<MouseEvent>() {
				public void handle(MouseEvent event) {
					xOffset = event.getSceneX();
					yOffset = event.getSceneY();
				}
			});
			
			baseLayout.setOnMouseDragged(new EventHandler<MouseEvent>() {
				public void handle(MouseEvent event) {
					primaryStage.setX(event.getScreenX() - xOffset);
					primaryStage.setY(event.getScreenY() - yOffset);
				}
			});
			
			baseController controller = loader.getController();
			controller.setStage(primaryStage);
			controller.setMain(this);

			primaryStage.setScene(cena);
			primaryStage.show();
				
		} catch (IOException erro) {
			erro.printStackTrace();
		}
		
		File file = getBlockFilePath();
		if(file != null) {
			loadBlockDataFromFile(file);
		}
	}
	
	public void showHome() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(this.getClass().getResource("view/Home.fxml"));
			AnchorPane home = (AnchorPane) loader.load();
			
			baseLayout.setCenter(home);
			
			HomeController controller = loader.getController();
			controller.setMain(this);
			
		} catch(IOException erro) {
			erro.printStackTrace();
		}
	}
	
	public boolean showNewBlockDialog(Block bloco) {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/newBlockDialog.fxml"));
			AnchorPane page = (AnchorPane) loader.load();
			
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Novo Bloco");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);
			dialogStage.initStyle(StageStyle.TRANSPARENT);
			
			page.setOnMousePressed(new EventHandler<MouseEvent>() {
				public void handle(MouseEvent event) {
					xOffset = event.getSceneX();
					yOffset = event.getSceneY();
				}
			});
			
			page.setOnMouseDragged(new EventHandler<MouseEvent>() {
				public void handle(MouseEvent event) {
					dialogStage.setX(event.getScreenX() - xOffset);
					dialogStage.setY(event.getScreenY() - yOffset);
				}
			});
			
			newBlockDialogController controller = loader.getController();
			controller.setDialogStage(dialogStage);
			
			bloco.setPrevhash(chain.get(chain.size()-1).getHash());
			bloco.setNumero(chain.size());
			
			controller.setBlock(bloco);
			
			dialogStage.setScene(scene);
			dialogStage.showAndWait();
			
			return controller.salvar();
			
		} catch (IOException erro) {
			erro.printStackTrace();
			return false;
		}
	}
	
	public void showAbout() throws ClassNotFoundException {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/About.fxml"));
			AnchorPane about = (AnchorPane) loader.load();
			
			Stage aboutStage = new Stage();
			aboutStage.setTitle("Novo Bloco");
			aboutStage.initModality(Modality.WINDOW_MODAL);
			aboutStage.initOwner(primaryStage);
			Scene scene = new Scene(about);
			aboutStage.setScene(scene);
			aboutStage.initStyle(StageStyle.TRANSPARENT);
			
			about.setOnMousePressed(new EventHandler<MouseEvent>() {
				public void handle(MouseEvent event) {
					xOffset = event.getSceneX();
					yOffset = event.getSceneY();
				}
			});
			
			about.setOnMouseDragged(new EventHandler<MouseEvent>() {
				public void handle(MouseEvent event) {
					aboutStage.setX(event.getScreenX() - xOffset);
					aboutStage.setY(event.getScreenY() - yOffset);
				}
			});
			
			
			AboutController controller = loader.getController();
			controller.setStage(aboutStage);
			
			/*Cliente cliente = new Cliente();
			System.out.print("Tamanho do chain: " + chain.size());
			convertChain();
			cliente.setBlocos(serialChain);
			System.out.println("Tamanho do serial: " + serialChain.size());			
			cliente.configurarRede();
			*/
			
			aboutStage.setScene(scene);
			aboutStage.showAndWait();
				
		} catch(IOException erro) {
			erro.printStackTrace();
		}
	}	
	
	public void showCadastro() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/Cadastro.fxml"));
			AnchorPane cad = (AnchorPane) loader.load();

			Stage cadStage = new Stage();
			cadStage.setTitle("Novo Bloco");
			cadStage.initModality(Modality.WINDOW_MODAL);
			cadStage.initOwner(primaryStage);
			Scene scene = new Scene(cad);
			cadStage.setScene(scene);
			cadStage.initStyle(StageStyle.TRANSPARENT);
			
			cad.setOnMousePressed(new EventHandler<MouseEvent>() {
				public void handle(MouseEvent event) {
					xOffset = event.getSceneX();
					yOffset = event.getSceneY();
				}
			});
			
			cad.setOnMouseDragged(new EventHandler<MouseEvent>() {
				public void handle(MouseEvent event) {
					cadStage.setX(event.getScreenX() - xOffset);
					cadStage.setY(event.getScreenY() - yOffset);
				}
			});
			
			CadastroController controller = loader.getController();
			controller.setStage(cadStage);
			controller.setDados(this, usuarios);
	
			cadStage.setScene(scene);
			cadStage.showAndWait();
			
			
		} catch(IOException erro) {
			erro.printStackTrace();
		}
	}
	
	public boolean validade() {
		
		String pegarHash = new String(new char[prefix]).replace('\0', '0');
		
		for(int i = chain.size()-1; i > 0; i--) {
			
			if( !(chain.get(i).getHash().equals(chain.get(i).gerarHash())) ) {
				System.out.println("Corrente Inva.");
				return false;
			}
			
			if( !(chain.get(i).getPrevhash().equals(chain.get(i-1).gerarHash()))) {
				System.out.println("Corrente Invalida.");
				return false;
			}
			
			if( !(chain.get(i).getHash().substring(0, prefix).equals(pegarHash)) ){
				System.out.println("Bloco nao minerado.");
				return false;
			}
		}
		
		return true;
		
	}
	
	public File getBlockFilePath() {
		Preferences prefs = Preferences.userNodeForPackage(Main.class);
		String filePath = prefs.get("filePath", null);
		
		if(filePath != null) {
			return new File(filePath);
		} else {
			return null;
		}
	}
	
	public void setBlockFilePath(File file) {
		Preferences prefs = Preferences.userNodeForPackage(Main.class);
		if (file != null) {
			prefs.put("filePath", file.getPath());
		
		} else {
			prefs.remove("filePath");
			
			primaryStage.setTitle("Blockchain");
		}
	}
	
	public void loadBlockDataFromFile(File file) {
		try {
			JAXBContext context = JAXBContext.newInstance(BlockListWrapper.class);
			Unmarshaller um = context.createUnmarshaller();
			
			BlockListWrapper wrapper = (BlockListWrapper) um.unmarshal(file);
			
			chain.clear();
			chain.addAll(wrapper.getBlocks());
			serialChain.clear();
			serialChain.addAll(wrapper.getSerialBlocks());
			
			setBlockFilePath(file);
		}  catch (Exception erro) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Erro");
			alert.setHeaderText("Não foi possivel carregar dados do arquivo");
			alert.setContentText(file.getPath());

			alert.showAndWait();
		}
	}
	
	public void saveBlockDataToFile(File file) {
	    try {
	        JAXBContext context = JAXBContext
	                .newInstance(BlockListWrapper.class);
	        Marshaller m = context.createMarshaller();
	        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

	        BlockListWrapper wrapper = new BlockListWrapper();
	        wrapper.setBlocks(chain);
	        wrapper.setSerialBlocks(serialChain);

	        m.marshal(wrapper, file);

	        setBlockFilePath(file);
	    } catch (Exception e) { 
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Erro");
			alert.setHeaderText("Não foi possivel carregar dados do arquivo");
			alert.setContentText(file.getPath());

			alert.showAndWait();
	    }
	}
	
	public static void arquivar(List<User> usuarios) {
		
		File arq = new File("ListaUsuarios");
		
		try {
			
			arq.delete();
			arq.createNewFile();
			
			ObjectOutputStream objOutput = new ObjectOutputStream(new FileOutputStream(arq));
			objOutput.writeObject(usuarios);
			objOutput.close();
			
			System.out.println("Arquivo Salvo");
			
		} catch (IOException erro) {
			System.out.println("Erro: " + erro.getMessage());
		}
	}
	
	@SuppressWarnings("unchecked")
	public static ArrayList<User> lerArquivo() {
		
		ArrayList <User> usuarios = new ArrayList<User>();
		
		try {
			
			File arq = new File("ListaUsuarios");
			
			if(arq.exists()) {
				
				ObjectInputStream objInput = new ObjectInputStream(new FileInputStream(arq));
				usuarios = (ArrayList<User>)objInput.readObject();
				objInput.close();
				return usuarios;
			}
			
		} catch(IOException erro1) {
			System.out.println("Erro: " + erro1.getMessage());
		} catch(ClassNotFoundException erro2) {
			System.out.println("Erro: " + erro2.getMessage());
		}
		return usuarios;
	}
	
	public Stage getPrimaryStage() {
		return primaryStage;
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}