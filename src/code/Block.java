package code;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Block implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private final IntegerProperty numero;
	private final IntegerProperty nonce;
	private final StringProperty nome;
	private final StringProperty remet;
	private final StringProperty dest;
	private final StringProperty conteudo;
	private final ObjectProperty<Date> data;
	private String prevHash;
	private StringProperty hash;
	
	public Block() {
		this(null, null);
	}
	
	public Block(SerialBlock bloco) {

		this.nome = new SimpleStringProperty(bloco.getNome());
		this.numero = new SimpleIntegerProperty(bloco.getNumero());
		this.nonce = new SimpleIntegerProperty(bloco.getNonce());
		this.remet = new SimpleStringProperty(bloco.getRemet());
		this.dest = new SimpleStringProperty(bloco.getDest());
		this.data = new SimpleObjectProperty<Date>(bloco.getData());
		this.conteudo = new SimpleStringProperty(bloco.getConteudo());
		this.prevHash = new String(bloco.getPrevHash());
		this.hash = new SimpleStringProperty(bloco.getHash());

	}
	
	public Block(String nome,  String conteudo) {
		
		this.nome = new SimpleStringProperty(nome);
		this.numero = new SimpleIntegerProperty(0);
		this.nonce = new SimpleIntegerProperty(0);
		this.remet = new SimpleStringProperty("");
		this.dest = new SimpleStringProperty("");
		this.data = new SimpleObjectProperty<Date>();
		this.conteudo = new SimpleStringProperty(conteudo);
		this.prevHash = new String("");
		this.hash = new SimpleStringProperty(gerarHash());
		
	}
	
	public String gerarHash() {
		
		String palavraHash = "" + this.nonce.getValue() + this.numero.getValue() + this.nome.getValue() + this.conteudo.getValue() + this.data.getValue();
		
		MessageDigest dig;
		String codificado = null;
		
		try {
			dig = MessageDigest.getInstance("SHA-256");
			byte[] hash = dig.digest(palavraHash.getBytes(StandardCharsets.UTF_8));
			codificado = Base64.getEncoder().encodeToString(hash);
			
		}catch(NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		
		this.hash = new SimpleStringProperty(codificado);
		return codificado;
		
	}
	
	public StringProperty mineBlock(int prefix) {
	    String prefixString = new String(new char[prefix]).replace('\0', '0');
	    int num = 0;
	    String h;
	    
	    h = this.hash.getValue();
    	
	    while (!h.substring(0, prefix).equals(prefixString)) {
	    	h = this.hash.getValue();
	    	num++;
	    	this.nonce.set(num);
	    	h = gerarHash();
	    }
	    
	    hash = new SimpleStringProperty(h);
	    return hash;
	}
	
	public String getNome() {
		return nome.getValue();
	}
	
	public void setNome(String nome) {
		this.nome.set(nome);
	}
	
	public StringProperty nomeProperty() {
		return nome;
	}
	
	public String getNumero() {
		int num = numero.getValue();
		return Integer.toString(num);
	}
	
	public int getNumeroInt() {
		int num = numero.getValue();
		return num;
	}

	public void setNumero(int numero) {
		this.numero.set(numero);;
	}
	
	public IntegerProperty numProperty() {
		return numero;
	}
	
	public String getRemet() {
		return remet.getValue();
	}
	
	public void setRemet(String remet) {
		this.remet.set(remet);
	}
	
	public String getDest() {
		return dest.getValue();
	}
	
	public void setDest(String dest) {
		this.dest.set(dest);
	}
	
	public String getNonce() {
		int n = nonce.getValue();
		return Integer.toString(n);
	}
	
	public int getNonceInt() {
		int n = nonce.getValue();
		return n;
	}

	public void setNonce(int nonce) {
		this.nonce.set(nonce);
	}
	
	public IntegerProperty nonceProperty() {
		return nonce;
	}
	
	public Date getData() {
		return data.getValue();
	}
	
	public void setData(Date data) {
		this.data.set(data);
	}
	
	public ObjectProperty<Date> dataProperty(){
		return data;
	}

	public String getConteudo() {
		return conteudo.getValue();
	}

	public void setConteudo(String conteudo) {
		this.conteudo.set(conteudo);
	}

	public String getPrevhash() {
		return prevHash;
	}

	public void setPrevhash(String prevhash) {
		this.prevHash = prevhash;
	}
	
	public String getHash() {
		return hash.getValue();
	}

	public void setHash(String hash) {
		this.hash.set(hash);
	}

	public StringProperty Hash() {
		return hash;
	}
}