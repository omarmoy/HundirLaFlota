package hundirlaflota;

import java.util.ArrayList;

import hundirlaflota.Tablero.EstadoCasilla;

public class Partida {
	private Integer id;
	private boolean terminada;
	private String ganador;
	private String nombreJ1;
	private String nombreJ2;
	private Tablero tableroJ1;
	private Tablero tableroJ2;
	private ArrayList<Jugada> jugadas;
	private boolean turnoJ1;
	private boolean turnoJ2;

	// partida nueva
	public Partida(int id, String nombreJ1, String nombreJ2) {
		this.id = id;
		this.terminada = false;
		this.ganador = "en_juego";
		this.nombreJ1 = nombreJ1;
		this.nombreJ2 = nombreJ2;
		this.tableroJ1 = new Tablero(true);
		this.tableroJ2 = new Tablero(true);
		this.jugadas = new ArrayList<Jugada>();
		this.turnoJ1 = true;
		this.turnoJ2 = false;
	}

	// partida cargada
	public Partida(int id, boolean terminada, String ganador) {
		this.id = id;
		this.terminada = terminada;
		this.ganador = ganador;
		this.jugadas = new ArrayList<Jugada>();
	}

	public EstadoCasilla realizarJugada(String nombreJugador, int filaDisparo, int columnaDisparo) {
		EstadoCasilla resultado = EstadoCasilla.VACIO;
		if (nombreJ1.equals(nombreJugador) && turnoJ1) {
			if (tableroJ2.disparar(filaDisparo, columnaDisparo) == 1) {
				resultado = EstadoCasilla.TOCADO;
			} else if (tableroJ2.disparar(filaDisparo, columnaDisparo) == 2) {
				resultado = EstadoCasilla.HUNDIDO;
			} else {
				resultado = EstadoCasilla.AGUA;
			}
			if (tableroJ2.todosHundidos()) {
				this.ganador = nombreJugador;
				this.terminada = true;
			}
		} else if (nombreJ2.equals(nombreJugador) && turnoJ2) {
			if (tableroJ1.disparar(filaDisparo, columnaDisparo) == 1) {
				resultado = EstadoCasilla.TOCADO;//
			} else if (tableroJ1.disparar(filaDisparo, columnaDisparo) == 2) {
				resultado = EstadoCasilla.HUNDIDO;
			} else {
				resultado = EstadoCasilla.AGUA;
			}
			if (tableroJ1.todosHundidos()) {
				this.ganador = nombreJugador;
				this.terminada = true;
			}
		}
		return resultado;
	}

	public void escribirJugadasEnTableros() {
		for (Jugada j : jugadas) {
			cargarJugada(j);
		}
	}

	private void cargarJugada(Jugada j) {
		String nombreJugador = j.getJugador();
		if (nombreJ1.equals(nombreJugador)) {
			tableroJ2.cargarJugada(j.getFila(), j.getColumna(), j.getResultado());
		} else if (nombreJ2.equals(nombreJugador)) {
			tableroJ1.cargarJugada(j.getFila(), j.getColumna(), j.getResultado());
		}
	}

	public void rendirse(String nombreJugador) {
		if (nombreJugador.equals(nombreJ1)) {
			this.terminada = true;
			this.ganador = nombreJ2;
		}
		if (nombreJugador.equals(nombreJ2)) {
			this.terminada = true;
			this.ganador = nombreJ1;
		}
	}

	public void agregarJugada(Jugada jugada) {
		this.jugadas.add(jugada);
	}

	public String infoPartida(String nombreJugador) {

		// 0 1 2 3 4
		// id;terminada;ganador;rival;turno;
		// barcos;...;
		// jugadas;...;

		String infoPartida = id + ";" + terminada + ";" + ganador + ";";

		// tablero jugador
		if (nombreJugador.equals(this.nombreJ1)) {
			infoPartida += nombreJ2 + ";" + turnoJ1 + ";\n"; // oponente y turno
			infoPartida += tableroJ1.toString() + "\n"; // barcos propios
		}
		if (nombreJugador.equals(this.nombreJ2)) {
			infoPartida += nombreJ1 + ";" + turnoJ2 + ";\n";
			infoPartida += tableroJ2.toString() + "\n";
		}

		// jugadas:
		if (jugadas.size() == 0) {
			infoPartida += "sin_jugadas;";
		} else {
			for (Jugada j : jugadas) {
				infoPartida += j.toString();
			}
		}
		infoPartida += "\n";
		return infoPartida;
	}

	// Getters y setters
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

	public String getNombreJ1() {
		return nombreJ1;
	}

	public void setNombreJ1(String nombreJ1) {
		this.nombreJ1 = nombreJ1;
	}

	public String getNombreJ2() {
		return nombreJ2;
	}

	public void setNombreJ2(String nombreJ2) {
		this.nombreJ2 = nombreJ2;
	}

	public Tablero getTableroJ1() {
		return tableroJ1;
	}

	public void setTableroJ1(Tablero tableroJ1) {
		this.tableroJ1 = tableroJ1;
	}

	public Tablero getTableroJ2() {
		return tableroJ2;
	}

	public void setTableroJ2(Tablero tableroJ2) {
		this.tableroJ2 = tableroJ2;
	}

	public ArrayList<Jugada> getJugadas() {
		return jugadas;
	}

	public void setJugadas(ArrayList<Jugada> jugadas) {
		this.jugadas = jugadas;
	}

	public boolean isTurnoJ1() {
		return turnoJ1;
	}

	public void setTurnoJ1(boolean turnoJ1) {
		this.turnoJ1 = turnoJ1;
	}

	public boolean isTurnoJ2() {
		return turnoJ2;
	}

	public void setTurnoJ2(boolean turnoJ2) {
		this.turnoJ2 = turnoJ2;
	}

}
