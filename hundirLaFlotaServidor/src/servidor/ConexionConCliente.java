package servidor;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

import hundirlaflota.*;

public class ConexionConCliente extends Thread {
	private Servidor servidor;
	private Socket skCliente;
	private InputStream entrada;
	private DataInputStream datoEntrada;
	private OutputStream salida;
	private DataOutputStream datoSalida;
	private Jugador jugador;

	public ConexionConCliente(Socket skCliente) {
		this.servidor = Servidor.getInstancia();
		this.skCliente = skCliente;
	}

	@Override
	public void run() {
		try {
			entrada = skCliente.getInputStream();
			datoEntrada = new DataInputStream(entrada);
			salida = skCliente.getOutputStream();
			datoSalida = new DataOutputStream(salida);

			String nombre = datoEntrada.readUTF().trim();
			String pass = datoEntrada.readUTF().trim();
//			System.out.println("Solicitud: " + nombre + " " + pass);  // consola
			this.jugador = new Jugador(nombre, pass);

			if (servidor.verificarLogin(this)) {
//				System.out.println("Cliente conectado: " + nombre); // consola				
				servidor.notificarConexion();
				servir();

			} else {
//				System.out.println("Solicitud rechazada: " + nombre); // consola
			}
			
			skCliente.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void servir() {
		try {
			while (true) {
				// La primera linea de lo que se reciba contiene la acción a realizar.
				String lectura = datoEntrada.readUTF();
				
				Scanner sc = new Scanner(lectura);
				String accion = sc.nextLine();
				switch (accion) {
				case "disparo":
					String jugada = sc.nextLine();					
					servidor.gestionarJugada(jugada);
					break;
				case "retar":
					String jugadorRetado = sc.nextLine();
					servidor.notificarReto(jugador.getNombre(), jugadorRetado);
					break;
				case "retoAceptado":
					String retador = sc.nextLine();
					servidor.nuevaPartida(retador, this.jugador.getNombre());
					break;
				case "rendicion":
					int partida = sc.nextInt();
					String ganador = sc.next();
					servidor.notificarRendicion(partida, ganador);
					break;					
				default:
					break;
				}

				sc.close();
			}
		} catch (Exception e) {
//			System.out.println("Cliente desconecato: " + jugador.getNombre()); // consola
		} finally {
			servidor.desconectarCliente(this);
		}
	}

	public synchronized void enviar(String envio)  {
		try {
			datoSalida.writeUTF(envio);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Error envio: "+envio);
		}
	}

	// getters
	public Jugador getJugador() {
		return jugador;
	}


	@Override
	public boolean equals(Object obj) {
		return jugador.equals(((ConexionConCliente) obj).getJugador());
	}

}
