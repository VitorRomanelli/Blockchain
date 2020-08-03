package code;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Blockchain implements Serializable{

	private static final long serialVersionUID = 1L;

	private static List<Block> corrente;
	
	public static int prefix = 2;
	
	public List<Block> getCorrente() {
		return corrente;
	}
	
	public Blockchain() {
		
		corrente = new ArrayList<Block>();
		//corrente = lerArquivo();
		if(corrente.size() == 0){
			corrente.add(gerarGenesis());
		}
		
	}
	
	public Block gerarGenesis() {
		
		Block genesis = new Block("Genesis", "<Bloco Genese>");
		genesis.setPrevhash("NULL");
		genesis.setData(new java.util.Date());
		genesis.gerarHash();
		genesis.mineBlock(prefix);
		genesis.setRemet("<Programador>");
		genesis.setDest("<Usúarios>");
		return genesis;
		
	}
	
	public void addBlock(Block bloco) {
		
		Block newBlock = bloco;
		
		newBlock.setPrevhash(corrente.get(corrente.size()-1).getHash());
		newBlock.gerarHash();
		//newBlock.mineBlock(prefix);
		
		if(Blockchain.validade() == true) {
			Blockchain.corrente.add(newBlock);
			//Blockchain.arquivar(corrente);
		}else {
			System.out.println("O bloco não foi inserido");
		}
		
	}
	
	public void imprimeBloco() {

		for(int i = 0; i < corrente.size(); i++) {
			
			System.out.println("Bloco: " + i);
			System.out.println("Numero do Bloco: " + corrente.get(i).getNumero());
			System.out.println("Nome do Bloco: " + corrente.get(i).getNome());
			System.out.println("Data: " + corrente.get(i).getData());
			System.out.println("Conteudo do Bloco: " + corrente.get(i).getConteudo());
			System.out.println("Hash anterior: " + corrente.get(i).getPrevhash());
			System.out.println("Hash: " + corrente.get(i).getHash());
			System.out.println();
			
		}
	}
	
	public static boolean validade() {
	
		String pegarHash = new String(new char[prefix]).replace('\0', '0');
		
		for(int i = corrente.size()-1; i > 0; i--) {
			
			if( !(corrente.get(i).getHash().equals(corrente.get(i).gerarHash())) ) {
				System.out.println("Corrente Invalida.");
				return false;
			}
			
			if( !(corrente.get(i).getPrevhash().equals(corrente.get(i-1).gerarHash()))) {
				System.out.println("Corrente Invalida.");
				return false;
			}
			
			if( !(corrente.get(i).getHash().substring(0, prefix).equals(pegarHash)) ){
				System.out.println("Bloco nao minerado.");
				return false;
			}
		}
		
		return true;
		
	}
	
	public static void arquivar(List<Block> blocos) {
		
		File arq = new File("ListaBlocos");
		
		try {
			
			arq.delete();
			arq.createNewFile();
			
			ObjectOutputStream objOutput = new ObjectOutputStream(new FileOutputStream(arq));
			objOutput.writeObject(blocos);
			objOutput.close();
			
			System.out.println("Arquivo Salvo");
			
		} catch (IOException erro) {
			System.out.println("Erro: " + erro.getMessage());
		}
	}
	
	@SuppressWarnings("unchecked")
	public static ArrayList<Block> lerArquivo() {
		
		ArrayList <Block> blocos = new ArrayList<Block>();
		
		try {
			
			File arq = new File("ListaBlocos");
			
			if(arq.exists()) {
				
				ObjectInputStream objInput = new ObjectInputStream(new FileInputStream(arq));
				blocos = (ArrayList<Block>)objInput.readObject();
				objInput.close();
				return blocos;
			}
			
		} catch(IOException erro1) {
			System.out.println("Erro: " + erro1.getMessage());
		} catch(ClassNotFoundException erro2) {
			System.out.println("Erro: " + erro2.getMessage());
		}
		return blocos;
	}
}
