package code.network;

import code.SerialBlock;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
	
	static List<SerialBlock> blocos;
	static List<SerialBlock> fixBlocos;
	public static int prefix = 2;
	
	public static boolean validade() {
		
		String pegarHash = new String(new char[prefix]).replace('\0', '0');
		
		for(int i = blocos.size()-1; i > 0; i--) {
			
			if( !(blocos.get(i).getHash().equals(blocos.get(i).gerarHash())) ) {
				System.out.println("Corrente Inva.");
				return false;
			}
			
			if( !(blocos.get(i).getPrevHash().equals(blocos.get(i-1).gerarHash()))) {
				System.out.println("Corrente Invalida.");
				return false;
			}
			
			if( !(blocos.get(i).getHash().substring(0, prefix).equals(pegarHash)) ){
				System.out.println("Bloco nao minerado.");
				return false;
			}
		}
		
		return true;
		
	}
	
	public static void main(String[] args) throws IOException, ClassNotFoundException {
		blocos  = new ArrayList<SerialBlock>();
		fixBlocos = new ArrayList<SerialBlock>();
		ServerSocket serverSocket = new ServerSocket(2806);
	    
		System.out.println("[servidor] - Escutando clientes na porta 2806...");
		
	    while(true) {
	    	
	    	System.out.println("[servidor] - Aguardando conexao...");
	    	Socket clientSocket = serverSocket.accept();

	    	System.out.println("[servidor] - Cliente conectado...");
	    	
	    	OutputStream clientOutputStream = clientSocket.getOutputStream();
	    	ObjectOutputStream objectOut = new ObjectOutputStream(clientSocket.getOutputStream());
	    	PrintWriter out = new PrintWriter(clientOutputStream, true);
	    	
	    	InputStream clienteInputStream = clientSocket.getInputStream();
	    	Reader clientReader = new InputStreamReader(clienteInputStream);
	    	BufferedReader in = new BufferedReader(clientReader);
	    	ObjectInputStream clienteObjectInputStream = new ObjectInputStream(clienteInputStream);
		    
	    	out.println("Ola, bem vindo");
		    out.flush();
		    
		    String linha;
		    
		    do {
		    	linha = in.readLine();
		    	System.out.println("[servidor] - Cliente enviou \"" + linha + "\"");
		    	if(linha.equals("atualize")) {
		    		objectOut.writeObject(blocos);
		    		System.out.println("[servidor] - writeObject() enviou um objeto do tipo " + blocos.getClass().getName());
		    	}		    	
		    	if (linha.equals("receba")) {
		    		blocos = (List<SerialBlock>) clienteObjectInputStream.readObject();
		    		System.out.println("[servidor] - readObject() leu um objeto do tipo " + blocos.getClass().getName());
		    		System.out.println("[servidor] - readObject() Tamanho da lista: " + blocos.size());
		    		
		    		if(validade() == true) {
		    			out.println("Recebido, o objeto e valido!");		    		
			    			if(fixBlocos.size() < blocos.size()) {
			    				fixBlocos = blocos;
			    				blocos.clear();
			    				System.out.println("[servidor] - Lista adicionada a rede");
			    				out.println("Sua lista foi adicionada");
			    			}else {
			    				System.out.println("[servidor] - Sua lista esta desatualizada");
			    				System.out.println("[servidor] - Atualizando sua lista");
			    				out.println("atualizando");
			    				objectOut.writeObject(fixBlocos);
			    			}
		    		}		    		
		    	} else {
		    		out.println("comando \"" + linha + "\"desconhecido");
		    	}
		    } while (!linha.equals("tchau"));
		    
		    clientSocket.close();
	    }
	}
}
