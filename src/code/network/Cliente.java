package code.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import code.Block;
import code.Blockchain;
import code.Main;
import code.SerialBlock;

public class Cliente {
	
	Main main = new Main();
	Socket echoSocket;
	static List<SerialBlock> blocos;
	
	public Cliente() throws UnknownHostException, IOException {
		blocos = new ArrayList<SerialBlock>();	
		System.out.println("[cliente] - Conectando ao servidor localhost na porta 2806...");
		echoSocket = new Socket("localhost", 2806);
		
		System.out.println("[cliente] - Conectado");
		
	}
	
	public void setBlocos(List<SerialBlock> bloc) {
		blocos = bloc;
	}
	
	public void atualizaLista() throws UnknownHostException, IOException, ClassNotFoundException {
		
		PrintWriter out = new PrintWriter(echoSocket.getOutputStream(), true);	    
		
		BufferedReader in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
		
	    in.readLine();
	    out.println("atualize");
	    
	    String resposta = in.readLine();
	    System.out.println("[cliente] - resposta do servidor \"" + resposta + "\"");
	    
	    InputStream input = echoSocket.getInputStream();
	    ObjectInputStream inputObject = new ObjectInputStream(input);
	    
	    blocos = (List<SerialBlock>) inputObject.readObject();
	    
	    main.setSerialChain(blocos);
	    main.convertSerial();
	    
	    System.out.println("[cliente] - Saindo...");
	    out.println("tchau");	    
		
	}
	
	public void configurarRede() throws UnknownHostException, IOException, ClassNotFoundException {
		
		PrintWriter out = new PrintWriter(echoSocket.getOutputStream(), true);	    
		ObjectOutputStream objectOut = new ObjectOutputStream(echoSocket.getOutputStream());
		
		BufferedReader in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
		
	    System.out.println("Tamanho da Lista: " + blocos.size());
		
		in.readLine();
	    out.println("receba");
	    objectOut.writeObject(blocos);
	    
	    String resposta = in.readLine();
	    System.out.println("[cliente] - resposta do servidor \"" + resposta + "\"");
	    String resposta2 = in.readLine();
	    
	    if(resposta2.equals("atualizando")) {
	    	InputStream serverInputStream = echoSocket.getInputStream();
	    	ObjectInputStream serverObjectInputStream = new ObjectInputStream(serverInputStream);
		    
	    	blocos = (List<SerialBlock>) serverObjectInputStream.readObject();
    		System.out.println("[cliente] - Lista atualizada");
		    main.setSerialChain(blocos);
		    main.convertSerial();
	    }
	    
	    System.out.println("[cliente] - Saindo...");
	    out.println("tchau");	    	
	}
	
	public static void main(String[] args) throws UnknownHostException, IOException, ClassNotFoundException {
		Cliente cliente = new Cliente();
		cliente.configurarRede();
	}
}