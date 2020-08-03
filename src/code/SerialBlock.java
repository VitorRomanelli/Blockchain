package code;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;

public class SerialBlock implements Serializable {

	private static final long serialVersionUID = 1L;
	private int numero;
	private int nonce;
	private String nome;
	private String remet;
	private String dest;
	private String conteudo;
	private Date data;
	private String prevHash;
	private String hash;
	
	public SerialBlock(Block bloco) {
		numero = bloco.getNumeroInt();
		nonce = bloco.getNonceInt();
		nome = bloco.getNome();
		remet = bloco.getRemet();
		dest = bloco.getDest();
		conteudo = bloco.getConteudo();
		data = bloco.getData();
		prevHash = bloco.getPrevhash();
		hash = bloco.getHash();
	}
	
	public String gerarHash() {
		
		String palavraHash = "" + this.nonce + this.numero + this.nome + this.conteudo + this.data;
		
		MessageDigest dig;
		String codificado = null;
		
		try {
			dig = MessageDigest.getInstance("SHA-256");
			byte[] hash = dig.digest(palavraHash.getBytes(StandardCharsets.UTF_8));
			codificado = Base64.getEncoder().encodeToString(hash);
			
		}catch(NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		
		this.hash = codificado;
		return codificado;
		
	}
	
	public String mineBlock(int prefix) {
	    String prefixString = new String(new char[prefix]).replace('\0', '0');
	    int num = 0;
	    String h;
	    
	    h = this.hash;
    	
	    while (!h.substring(0, prefix).equals(prefixString)) {
	    	h = this.hash;
	    	num++;
	    	this.nonce = num;
	    	h = gerarHash();
	    }
	    
	    hash = h;
	    return hash;
	}

	public int getNumero() {
		return numero;
	}

	public void setNumero(int numero) {
		this.numero = numero;
	}

	public int getNonce() {
		return nonce;
	}

	public void setNonce(int nonce) {
		this.nonce = nonce;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getRemet() {
		return remet;
	}

	public void setRemet(String remet) {
		this.remet = remet;
	}

	public String getDest() {
		return dest;
	}

	public void setDest(String dest) {
		this.dest = dest;
	}

	public String getConteudo() {
		return conteudo;
	}

	public void setConteudo(String conteudo) {
		this.conteudo = conteudo;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public String getPrevHash() {
		return prevHash;
	}

	public void setPrevHash(String prevHash) {
		this.prevHash = prevHash;
	}

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}	
}