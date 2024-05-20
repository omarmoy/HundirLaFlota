package servidor;

import java.net.ServerSocket;
import java.net.Socket;

public class MainServer {

	static final int PUERTO = 2000;
	private ServerSocket skServidor;

	public MainServer() {
		try {
			skServidor = new ServerSocket(PUERTO);
//			System.out.println("SERVIDOR INICIA"); // consola
			while (true) {
				Socket skCliente = skServidor.accept();
				new ConexionConCliente(skCliente).start();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		new MainServer();
	}

}
