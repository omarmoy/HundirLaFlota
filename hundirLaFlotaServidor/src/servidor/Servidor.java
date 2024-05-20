package servidor;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import hundirlaflota.*;
import hundirlaflota.Tablero.EstadoCasilla;

public class Servidor {

	static final int PUERTO = 2000;
	private BlockingQueue<ConexionConCliente> clientesConectados = new ArrayBlockingQueue<>(10);
	private GestionDatos bbdd = GestionDatos.getInstancia();
	private ArrayList<Jugador> jugadores = bbdd.cargarJugadores();

	private static Servidor instancia;

	private Servidor() {
	}

	public static Servidor getInstancia() {
		if (instancia == null) {
			instancia = new Servidor();
		}
		return instancia;
	}

	public synchronized boolean verificarLogin(ConexionConCliente conexion) {
		String respuesta = "";
		boolean jugadorExiste = jugadores.contains(conexion.getJugador());
		boolean jugadorYaConectado = clientesConectados.contains(conexion);
		boolean servidorCompleto = false;
		if (jugadorExiste && !jugadorYaConectado) {
			servidorCompleto = !clientesConectados.offer(conexion);
		}

		if (!jugadorExiste) {
			respuesta = "jugadorNoExiste";
		} else if (jugadorYaConectado) {
			respuesta = "jugadorYaConectado";
		} else if (servidorCompleto) {
			respuesta = "servidorCompleto";
		} else {
			respuesta = cargarPartidas(conexion.getJugador().getNombre());
		}
		conexion.enviar(respuesta);

		return jugadorExiste && !jugadorYaConectado && !servidorCompleto;

	}

	public String cargarPartidas(String nombreJugador) {
		String respuesta = "ok";
		ArrayList<Partida> partidas = bbdd.cargarPartidas(nombreJugador);
		if (partidas.size() > 0) {
			respuesta += "\n";
			for (Partida p : partidas) {
				respuesta += p.infoPartida(nombreJugador);
			}
		}
		return respuesta;
	}

	public synchronized void desconectarCliente(ConexionConCliente cliente) {
		clientesConectados.remove(cliente);
		notificarConexion();
	}

	public synchronized void notificarConexion() {
		String listaConectados = clientesConectados();
		for (ConexionConCliente c : clientesConectados) {
			c.enviar(listaConectados);
		}
	}

	private String clientesConectados() {
		String listaConectados = "conectados\n";
		for (ConexionConCliente c : clientesConectados) {
			listaConectados += c.getJugador().getNombre() + ";";
		}
		return listaConectados;
	}

	public void notificarReto(String retador, String retado) {
		String reto = "teReta\n" + retador;
		ConexionConCliente cc = buscarCliente(retado);
		if (cc != null)
			cc.enviar(reto);
	}

	public void notificarRendicion(int partida, String ganador) {
		String renuncia = "renuncia\n" + partida;
		ConexionConCliente cc = buscarCliente(ganador);
		if (cc != null)
			cc.enviar(renuncia);
		bbdd.finPartida(partida, ganador);
	}

	public void nuevaPartida(String retador, String retado) {
		String nueva = "nueva\n";
		ConexionConCliente ccRetador = buscarCliente(retador);
		ConexionConCliente ccRetado = buscarCliente(retado);
		if (ccRetado != null && ccRetador != null) {
			Partida partida = bbdd.nuevaPartida(retador, retado);
			String infoRetador = partida.infoPartida(retador);
			String infoRetado = partida.infoPartida(retado);
			ccRetador.enviar(nueva + infoRetador);
			ccRetado.enviar(nueva + infoRetado);
		}

	}

	private synchronized ConexionConCliente buscarCliente(String nombreJugador) {
		for (ConexionConCliente cc : clientesConectados) {
			if (nombreJugador.equals(cc.getJugador().getNombre())) {
				return cc;
			}
		}
		return null;
	}

	public void gestionarJugada(String datosJugada) {
		// registrar jugada
		Scanner sc = new Scanner(datosJugada);
		int idPartida = sc.nextInt();
		String jugador = sc.next();
		int nJugada = sc.nextInt();
		int fila = sc.nextInt();
		int columna = sc.nextInt();
		sc.close();
		Partida partida = bbdd.cargarPartida(idPartida);

		EstadoCasilla resultado = partida.realizarJugada(jugador, fila, columna);
		Jugada jugada = new Jugada(idPartida, jugador, nJugada, fila, columna, resultado);
		bbdd.registrarJugada(jugada);

		// enviar resultado jugada
		ConexionConCliente ccJ1 = buscarCliente(partida.getNombreJ1());
		ConexionConCliente ccJ2 = buscarCliente(partida.getNombreJ2());
		String envio = "jugada\n" + jugada.toString();
		if (ccJ1 != null) {
			ccJ1.enviar(envio);
		}
		if (ccJ2 != null) {
			ccJ2.enviar(envio);
		}

		// enviar resultado partida
		if (partida.isTerminada()) {
			bbdd.finPartida(idPartida, partida.getGanador());
			String terminada = "ganador\n" + partida.getId() + " " + partida.getGanador();
			if (ccJ1 != null) {
				ccJ1.enviar(terminada);
			}
			if (ccJ2 != null) {
				ccJ2.enviar(terminada);
			}
		}
	}

}
