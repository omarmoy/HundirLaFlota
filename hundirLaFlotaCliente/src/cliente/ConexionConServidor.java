package cliente;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

import juego.Partida;

/**
 * 
 */
public class ConexionConServidor extends Thread {

	private static final String HOST = "localhost";
	private static final int Puerto = 2000;
	private String verificada;
	private GestionJuego gj;
	private DataOutputStream envio;
	private DataInputStream lectura;

	public ConexionConServidor(String nombre, String password) {
		if (!usuarioSinConexion(nombre, password).equals("ok")) {
			try {
				// escritura
				Socket socket = new Socket(HOST, Puerto);
				OutputStream salida = socket.getOutputStream();
				envio = new DataOutputStream(salida);
				envio.writeUTF(nombre);
				envio.writeUTF(password);

				// lectura
				InputStream entrada = socket.getInputStream();
				lectura = new DataInputStream(entrada);

				String datosRecibidos = lectura.readUTF();
				Scanner sc = new Scanner(datosRecibidos);				
				this.verificada = sc.nextLine().trim();

				if (verificada.equals("ok")) {
					gj = new GestionJuego(nombre);
					if (sc.hasNextLine()) {
						gj.cargarPartidas(datosRecibidos); 
					}
					String conectados = lectura.readUTF();
					Scanner sc2 = new Scanner(conectados);
					sc2.nextLine();					
					gj.actualizarJugadores(sc2.nextLine());
					sc2.close();
				} else {
					socket.close();
				}
												
				sc.close();
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else {
			gj = new GestionJuego("usuario sin conexion");
		}
	}

	@Override
	public void run() {
		try {
			while (true) {
				// La primera linea de lo que se reciba ha detener la acción a realizar, menos en la lista de conectados.
				String escucha = lectura.readUTF();
				Scanner sc = new Scanner(escucha);
				String recibido = sc.nextLine();
				switch (recibido) {
				case "jugada":
					String jugada = sc.nextLine();
					gj.registrarJugada(jugada);
					break;
				case "teReta":
					String retador = sc.nextLine();
					gj.comunicarReto(retador);
					break;
				case "nueva": 
					String lineaPartida = sc.nextLine();
					String[] partida = lineaPartida.split(";");					
					gj.nuevaPartida(escucha, partida[3]); // le manda rival
					break;
				case "ganador":
					String victoria = sc.nextLine();
					gj.registrarVictoria(victoria);					
					break;	
				case "renuncia":
					int idPartida = sc.nextInt();
					gj.registrarVictoriaPorRenuncia(idPartida);
					break;
				case "conectados":
					String conectados = sc.nextLine();
					gj.actualizarJugadores(conectados);
					break;
				default:
					break;
				}
				sc.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}	
	

	public synchronized void enviar(String dato) {
		try {
			envio.writeUTF(dato);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("ERROR envio: "+ dato);
		}
	}
	
	public synchronized void rendirse(Partida p) {
		String rendicion = "rendicion\n";
		rendicion+=p.getId()+" ";
		rendicion+=p.getRival();
		
		try {
			envio.writeUTF(rendicion);
			p.setTerminada(true);
			p.setGanador(p.getRival());
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("ERROR envio: "+ rendicion);
		}
	}
	
	public void enviarReto(String jugadorRetado) {
		String reto = "retar\n"+ jugadorRetado;
		enviar(reto);
	}
	
	public void aceptarReto(String retador) {
		String aceptado = "retoAceptado\n"+retador;
		enviar(aceptado);
		gj.actualizarRetosPdt();
	}

	// getters
	public String getVerificada() {
		return this.verificada;
	}

	public GestionJuego getGJ() {
		return gj;
	}

	/**
	 * Permine logearse si conexión, para pruebas de Interfaz.
	 * 
	 * @param nombre
	 * @param password
	 * @return
	 */
	private String usuarioSinConexion(String nombre, String password) {
		boolean n = nombre.equalsIgnoreCase("u");
		boolean p = password.equalsIgnoreCase("u");
		if (n && p) {
			this.verificada = "ok";
			return this.verificada;
		}else {
			return "";
		}

	}

}
