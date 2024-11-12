package cliente;

import java.util.ArrayList;
import java.util.Scanner;

import juego.Jugada;
import juego.Partida;
import juego.Usuario;
import vista.Cabecera;
import vista.VentanaPrincipal;
import vista.seccion_partida_nueva.PanelUsuariosConectados;
import vista.seccion_partidas_en_juego.PanelPartidas;
import vista.seccion_partidas_en_juego.VentanaPartida;
import vista.seccion_replay.PanelReplay;
import juego.Tablero.EstadoCasilla;

public class GestionJuego {

	private String jugador;
	private ArrayList<Partida> partidas;
	private ArrayList<Usuario> jugadoresConectados;
	private PanelPartidas vistaPartidas;
	private PanelUsuariosConectados vistaUsuarios;
	private Cabecera vistaCabecera;
	private PanelReplay vistaReplay;
	private VentanaPrincipal vistaPrincipal;

	public GestionJuego(String jugador) {
		this.jugador = jugador;
		this.partidas = new ArrayList<Partida>();
		this.jugadoresConectados = new ArrayList<Usuario>();
		this.vistaPartidas = null;
		this.vistaUsuarios = null;
		this.vistaCabecera = null;
		this.vistaReplay = null;
		this.vistaPrincipal = null;
	}

	public void actualizarJugadores(String datos) {
		if (!datos.equals("")) {

			String[] listaActualizada = datos.split(";");
			ArrayList<Usuario> jugadoresActualizado = new ArrayList<Usuario>();
			for (String jugador : listaActualizada) {
				if (!jugador.equals(this.jugador))
					jugadoresActualizado.add(new Usuario(jugador));
			}
			// eliminar desconectados
			jugadoresConectados.retainAll(jugadoresActualizado);

			// agregar nuevos
			jugadoresActualizado.removeAll(jugadoresConectados);
			jugadoresConectados.addAll(jugadoresActualizado);

			// actualizar partidas
			for (Partida p : partidas) {
				if (jugadoresConectados.contains(new Usuario(p.getRival())))
					p.setRivalConectado(true);
				else
					p.setRivalConectado(false);
			}

			// actualizar vista
			if (vistaPartidas != null)
				vistaPartidas.repintar();
			if (vistaUsuarios != null)
				vistaUsuarios.repintar();
		}
	}
	
	public void cargarJugadoresConectados(String datos) {
		if (!datos.equals("")) {

			String[] listaActualizada = datos.split(";");
			ArrayList<Usuario> jugadoresActualizado = new ArrayList<Usuario>();
			for (String jugador : listaActualizada) {
				if (!jugador.equals(this.jugador))
					jugadoresActualizado.add(new Usuario(jugador));
			}
			// eliminar desconectados
			jugadoresConectados.retainAll(jugadoresActualizado);

			// agregar nuevos
			jugadoresActualizado.removeAll(jugadoresConectados);
			jugadoresConectados.addAll(jugadoresActualizado);

			// actualizar partidas
			for (Partida p : partidas) {
				if (jugadoresConectados.contains(new Usuario(p.getRival())))
					p.setRivalConectado(true);
				else
					p.setRivalConectado(false);
			}

			// actualizar vista
			if (vistaPartidas != null)
				vistaPartidas.repintar();
			if (vistaUsuarios != null)
				vistaUsuarios.repintar();
		}
	}

	public void cargarPartidas(String datos) {
		Scanner sc = new Scanner(datos);
		sc.nextLine();
		while (sc.hasNextLine()) {
			String[] infoPartida = sc.nextLine().split(";");
			String[] barcos = sc.nextLine().split(";");
			String[] jugadas = sc.nextLine().split(";");

			int id = Integer.parseInt(infoPartida[0]);
			boolean terminada = Boolean.parseBoolean(infoPartida[1]);
			String ganador = infoPartida[2];
			String rival = infoPartida[3];
			boolean enTurno = Boolean.parseBoolean(infoPartida[4].trim());
			boolean rivalConectado = jugadoresConectados.contains(new Usuario(rival));

			Partida p = new Partida(id, terminada, ganador, rival, enTurno, barcos, jugadas, rivalConectado, jugador);
			partidas.add(p);
		}
		sc.close();
	}

	public void comunicarReto(String retador) {
		int indiceRetador = jugadoresConectados.indexOf(new Usuario(retador));
		jugadoresConectados.get(indiceRetador).setRetador(true);
		if (vistaUsuarios != null)
			vistaUsuarios.repintar();
		if (vistaCabecera != null)
			vistaCabecera.mostrarRetos(true);
	}

	public void nuevaPartida(String nuevaPartida, String rival) {
		int indiceRival = jugadoresConectados.indexOf(new Usuario(rival));
		jugadoresConectados.get(indiceRival).setRetador(false);
		jugadoresConectados.get(indiceRival).setRetado(false);
		construirPartida(nuevaPartida);
		actualizarRetosPdt();
		if (vistaUsuarios != null)
			vistaUsuarios.repintar();
	}

	private void construirPartida(String datos) {
		Scanner sc = new Scanner(datos);
		sc.nextLine();
		String[] infoPartida = sc.nextLine().split(";");
		String[] barcos = sc.nextLine().split(";");
		String[] jugadas = sc.nextLine().split(";");

		int id = Integer.parseInt(infoPartida[0]);
		boolean terminada = Boolean.parseBoolean(infoPartida[1]);
		String ganador = infoPartida[2];
		String rival = infoPartida[3];
		boolean enTurno = Boolean.parseBoolean(infoPartida[4].trim());
		boolean rivalConectado = jugadoresConectados.contains(new Usuario(rival));

		Partida p = new Partida(id, terminada, ganador, rival, enTurno, barcos, jugadas, rivalConectado, jugador);
		partidas.add(p);
		sc.close();

		// Abrir ventana de la nueva partida
		VentanaPartida vp = new VentanaPartida(vistaPrincipal, p, vistaPartidas); 
		vp.setVisible(true);
	}

	public void actualizarRetosPdt() {
		boolean rPendientes = false;
		for (Usuario j : jugadoresConectados) {
			if (j.isRetador()) {
				rPendientes = true;
			}
		}
		vistaCabecera.mostrarRetos(rPendientes);
	}

	public void registrarVictoria(String datosVictoria) {
		Scanner sc = new Scanner(datosVictoria);
		int idPartida = sc.nextInt();
		String ganador = sc.next();
		sc.close();
		for (Partida p : partidas) {
			if (p.getId() == idPartida) {
				p.setGanador(ganador);
				p.setTerminada(true);
				p.notificarFin();
			}
		}
		if (vistaPartidas != null)
			vistaPartidas.repintar();
		if (vistaReplay != null)
			vistaReplay.repintar();
	}

	public void registrarVictoriaPorRenuncia(int idPartida) {		
		for (Partida p : partidas) {
			if (p.getId() == idPartida) {
				p.setGanador(jugador);
				p.setTerminada(true);
				p.notificarFin();
			}
		}
		if (vistaPartidas != null)
			vistaPartidas.repintar();
		if (vistaReplay != null)
			vistaReplay.repintar();
	}
	
	public void registrarJugada(String jugadaRecibida) {
		String[] jugadaSinPuntoyComa = jugadaRecibida.split(";");
		Scanner sc = new Scanner(jugadaSinPuntoyComa[0]);
		int idPartida = sc.nextInt();
		String jugador = sc.next();
		int nJugada = sc.nextInt();
		int fila = sc.nextInt();
		int columna = sc.nextInt();
		EstadoCasilla resultado = EstadoCasilla.valueOf(sc.next());
		sc.close();
		Jugada jugada = new Jugada(idPartida, jugador, nJugada, fila, columna, resultado);
		for (Partida p : partidas) {
			if (p.getId() == idPartida) {
				p.registrarJugada(jugada);
				p.refrescarPantalla();
			}
		}
		if (vistaPartidas != null)
			vistaPartidas.repintar();
	}

	// getters
	public String getJugador() {
		return jugador;
	}

	public ArrayList<Partida> getPartidas() {
		return partidas;
	}

	public ArrayList<Usuario> getJugadoresConectados() {
		return jugadoresConectados;
	}

	// setters
	public void setVistaPartidas(PanelPartidas vistaPartidas) {
		this.vistaPartidas = vistaPartidas;
	}

	public void setVistaUsuarios(PanelUsuariosConectados vistaUsuarios) {
		this.vistaUsuarios = vistaUsuarios;
	}

	public void setVistaCabecera(Cabecera vistaCabecera) {
		this.vistaCabecera = vistaCabecera;
	}

	public void setVistaReplay(PanelReplay vistaReplay) {
		this.vistaReplay = vistaReplay;
	}

	public void setVistaPrincipal(VentanaPrincipal vistaPrincipal) {
		this.vistaPrincipal = vistaPrincipal;
	}

}
