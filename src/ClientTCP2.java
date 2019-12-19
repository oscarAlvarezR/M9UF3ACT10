import java.net.*;
import java.io.*;

public class ClientTCP2 implements Runnable {
	
	private static Socket client;
	
	public ClientTCP2(Socket client) {
		this.client = client;
	}
	
	public static void main (String[] args) throws IOException {
		
		// **********SEGONA PART**********
		String host = "localhost";
		int port = 60000;//Port remot
		
		client = new Socket(host, port);
		//FLUX DE SORTIDA AL SERVIDOR
		PrintWriter fsortida = new PrintWriter(client.getOutputStream(), true);
		
		//FLUX D'ENTRADA AL SERVIDOR
		BufferedReader fentrada = new BufferedReader(new InputStreamReader(client.getInputStream()));
		
		//FLUX PER A ENTRADA ESTĀNDARD
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		
		String name = "";
		System.out.println("Introdueix el nom:");
		// Lectura teclat
		name = in.readLine();
		
		
		while (name == null || name.equals("") ) {
			
			System.out.println("Error, per a poder enviar missatges introdueix el teu nom:");
			name = in.readLine();
		}
		
		Runnable run = new ClientTCP2(client);
		Thread entradaServer = new Thread(run);
		
		entradaServer.start();
		
		fsortida.println(name);
		
		String cadena, eco = "";
		System.out.println("Introdueix la cadena: ");
		//Lectura teclat
		cadena = in.readLine();
		
		while (cadena != null && !cadena.equals("")) {
			
			//Enviament cadena al servidor
			fsortida.println(cadena);
			//Rebuda cadena del servidor
			eco = fentrada.readLine();
			System.out.println("  =>ECO: "+eco);
			//Lectura del teclat
			cadena = in.readLine();	
		}
		fsortida.close();
		fentrada.close();
		System.out.println("Finalitzaciķ de l'enviament...");
		in.close();
		client.close();
	}
	
	@Override
	public void run() {
		
		try {
			
			BufferedReader fentrada = new BufferedReader(new InputStreamReader(client.getInputStream()));
			
			while (true) {
				
				System.out.println(fentrada.readLine());
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
}
