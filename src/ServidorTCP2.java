import java.net.*;
import java.util.Scanner;
import java.io.*;

public class ServidorTCP2 implements Runnable {

	Socket client;
	ServerSocket server;
	Socket[] sortidaClients;
	static int numClient;
	String cadena = "";

	public ServidorTCP2(Socket clientConnectat, ServerSocket server, Socket[] sortidaClients) {
		this.client = clientConnectat;
		this.server = server;
		this.numClient ++;
		this.sortidaClients = sortidaClients;
	}

	public static void main (String[] args) throws IOException {

		// **********SEGONA PART**********
		Scanner teclado = new Scanner(System.in);


		int numPort = 60000;
		ServerSocket servidor = new ServerSocket(numPort);



		// Demanem el numClients per a indicar 
		// el max de clients que rebra el servidor
		System.out.println("Introdueix el num de clients que podra rebre el servidor: ");
		int numClients = teclado.nextInt();

		Socket[] sortidaClients = new Socket[numClients];

		ServidorTCP2[] arrayRunnable = new ServidorTCP2[numClients];
		Thread[] arrayThread = new Thread[numClients];
		
		// Determinem les vegades que es conectaran els clients
		for (int i = 0; i < arrayRunnable.length; i++) {

			Socket clientConnectat = servidor.accept();
			boolean stop = false;
			
			for (int j = 0; j < sortidaClients.length; j++) {
				
				if (sortidaClients[i] != null && stop == false) {
					
					sortidaClients[i] = clientConnectat;
					stop = true;
				}
			}

			// Runnable
			arrayRunnable[i] = new ServidorTCP2(clientConnectat, servidor, sortidaClients);

			// Thread
			arrayThread[i] = new Thread(arrayRunnable[i]);
			arrayThread[i].start();

		}


	}

	@Override
	public void run() {

		try {
			PrintWriter fsortida = null;
			BufferedReader fentrada = null;

			System.out.println("Client " + this.numClient + " connectat... ");

			//FLUX DE SORTIDA AL CLIENT
			fsortida = new PrintWriter(this.client.getOutputStream(), true);


			//FLUX D'ENTRADA DEL CLIENT
			fentrada = new BufferedReader(new InputStreamReader(this.client.getInputStream()));

			if ((cadena = fentrada.readLine()) != null) {
				
				System.out.println("Nom Client " + this.numClient + ": " + cadena);
			}
			
			
			while ((cadena = fentrada.readLine()) != null) {

				fsortida.println(cadena);
				
				for (int i = 0; i < sortidaClients.length; i++) {
					
					if (sortidaClients[i] != null) {
						fsortida = new PrintWriter(this.sortidaClients[i].getOutputStream(), true);
						fsortida.println("Client " + this.numClient + ": " + cadena);
					}
				}
				
				System.out.println("Rebent: "+cadena);
				if (cadena.equals("*")) break;

			}
			fentrada.close();
			fsortida.close();
			this.client.close();
			this.server.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}