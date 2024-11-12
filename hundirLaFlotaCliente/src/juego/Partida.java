package juego;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

import juego.Tablero.EstadoCasilla;
import vista.seccion_partidas_en_juego.VentanaPartida;

public class Partida {
	private Integer id;
	private boolean terminada;
	private String ganador;
	private String jugador;
	private String rival;
	private Tablero tablero;
	private Tablero vistaRival;
	private ArrayList<Jugada> jugadas = new ArrayList<Jugada>();
	private boolean turnoJugador;
	private boolean rivalConectado;
	private VentanaPartida vista;
	private int turnoReplay;
	private String[] barcos;

	public Partida(int id, boolean terminada, String ganador, String rival, boolean turnoJugador, String[] barcos,
			String[] jugadas, boolean rivalConectado, String jugador) {
		this.id = id;
		this.terminada = terminada;
		this.ganador = ganador;
		this.rival = rival;
		this.tablero = new Tablero(barcos);
		this.vistaRival = new Tablero();
		this.turnoJugador = turnoJugador;
		this.rivalConectado = rivalConectado;
		this.jugador = jugador;
		this.jugadas = contruirJugadas(jugadas);
		this.vista = null;
		this.turnoReplay = 0;
		this.barcos = barcos;
	}

	private ArrayList<Jugada> contruirJugadas(String[] jugadasString) {
		ArrayList<Jugada> jugadas = new ArrayList<Jugada>();
		if (!jugadasString[0].equals("sin_jugadas")) {
			for (String jugadaSt : jugadasString) {
				Scanner sc = new Scanner(jugadaSt);
				int id = sc.nextInt();
				String jugador = sc.next();
				int nJugada = sc.nextInt();
				int fila = sc.nextInt();
				int columna = sc.nextInt();
				EstadoCasilla resultado = EstadoCasilla.valueOf(sc.next());
				Jugada jugada = new Jugada(id, jugador, nJugada, fila, columna, resultado);
				jugadas.add(jugada);
				sc.close();
				if (!this.terminada) {
					cargarJugada(jugada);
				}
			}
			Collections.sort(jugadas);
		}
		return jugadas;
	}

	public void rendirse() {
		this.terminada = true;
		this.ganador = rival;
	}

	private void cargarJugada(Jugada j) {
		if (j.getJugador().equals(this.jugador)) {
			this.vistaRival.actualizarCasilla(j.getFila(), j.getColumna(), j.getResultado());
		} else {
			this.tablero.actualizarCasilla(j.getFila(), j.getColumna(), j.getResultado());
		}
		this.jugadas.add(j);
	}

	public void registrarJugada(Jugada j) {
		if (j.getJugador().equals(this.jugador)) {
			this.vistaRival.actualizarCasilla(j.getFila(), j.getColumna(), j.getResultado());
			this.turnoJugador = false;
		} else {
			this.tablero.actualizarCasilla(j.getFila(), j.getColumna(), j.getResultado());
			this.turnoJugador = true;
		}
		this.jugadas.add(j);
	}

	public void refrescarPantalla() {
		if (vista != null) {
			vista.repintar();
		}
	}

	public void notificarFin() {
		boolean victoria = jugador.equals(ganador);
		vista.finPartida(victoria);
//		refrescarPantalla();
	}

	@Override
	public String toString() {
		String resultado = " ";
		if (terminada) {
			if (ganador.equals(jugador)) {
				String motivo = numeroDeHundimientos(false) < 5 ? " por renuncia del rival" : "";
				resultado += "VICTORIA" + motivo;
			} else {
				String motivo = numeroDeHundimientos(true) < 5 ? " por renuncia" : "";
				resultado += "DERROTA" + motivo;
			}
		}

		return id.toString() + " partida contra " + rival + resultado;
	}

	public int numeroDeHundimientos(boolean jugador) {
		int n = 0;
		if (jugador) {
			for(Jugada j : jugadas) {
				if(j.getJugador().equals(rival))
					n++;
			}
		} else {
			for(Jugada j : jugadas) {
				if(!j.getJugador().equals(rival))
					n++;
			}
		}
		return n;
	}

	public void replaySiguienteJugada() {
		if (terminada && turnoReplay < jugadas.size()) {
			turnoReplay++;
			for (Jugada j : jugadas) {
				if (j.getnJugada() == turnoReplay) {
					replayJugada(j);
					break;
				}
			}
		}
	}

	private void replayJugada(Jugada j) {
		if (j.getJugador().equals(this.jugador)) {
			this.vistaRival.actualizarCasilla(j.getFila(), j.getColumna(), j.getResultado());
			this.turnoJugador = false;
		} else {
			this.tablero.actualizarCasilla(j.getFila(), j.getColumna(), j.getResultado());
			this.turnoJugador = true;
		}
	}

	public void reiniciarReplay() {
		this.tablero = new Tablero(barcos);
		this.vistaRival = new Tablero();
		this.turnoReplay = 0;
	}

	// Getters and Setters
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public boolean isTerminada() {
		return terminada;
	}

	public void setTerminada(boolean terminada) {
		this.terminada = terminada;
	}

	public String getGanador() {
		return ganador;
	}

	public void setGanador(String ganador) {
		this.ganador = ganador;
	}

	public String getJugador() {
		return jugador;
	}

	public void setJugador(String jugador) {
		this.jugador = jugador;
	}

	public String getRival() {
		return rival;
	}

	public void setRival(String rival) {
		this.rival = rival;
	}

	public Tablero getTablero() {
		return tablero;
	}

	public void setTablero(Tablero tablero) {
		this.tablero = tablero;
	}

	public ArrayList<Jugada> getJugadas() {
		return jugadas;
	}

	public void setJugadas(ArrayList<Jugada> jugadas) {
		this.jugadas = jugadas;
	}

	public boolean isTurnoJugador() {
		return turnoJugador;
	}

	public void setTurnoJugador(boolean turnoJugador) {
		this.turnoJugador = turnoJugador;
	}

	public boolean isRivalConectado() {
		return rivalConectado;
	}

	public void setRivalConectado(boolean rivalConectado) {
		this.rivalConectado = rivalConectado;
	}

	public Tablero getVistaRival() {
		return vistaRival;
	}

	public void setVistaRival(Tablero vistaRival) {
		this.vistaRival = vistaRival;
	}

	public VentanaPartida getVista() {
		return vista;
	}

	public void setVista(VentanaPartida vista) {
		this.vista = vista;
	}

	public int getTurnoReplay() {
		return turnoReplay;
	}

	public void setTurnoReplay(int turnoReplay) {
		this.turnoReplay = turnoReplay;
	}

}
