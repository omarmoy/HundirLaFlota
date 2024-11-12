package juego;

import juego.Tablero.EstadoCasilla;

public class Jugada implements Comparable<Jugada>{

	private int idPartida;
	private String jugador; 
	private int nJugada;
	private int fila;
	private int columna;
	private EstadoCasilla resultado;

	public Jugada(int idPartida, String jugador, int nJugada, int fila, int columna, EstadoCasilla resultado) {
		this.idPartida = idPartida;
		this.jugador = jugador;
		this.nJugada = nJugada;
		this.fila = fila;
		this.columna = columna;
		this.resultado = resultado;
	}

	@Override
	public String toString() {
		String string = idPartida + " ";
		string += jugador + " ";
		string += nJugada + " ";
		string += fila + " ";
		string += columna + " ";
		string += resultado + ";";
		return string;
	}

	public int getIdPartida() {
		return idPartida;
	}

	public String getJugador() {
		return jugador;
	}

	public int getnJugada() {
		return nJugada;
	}

	public int getFila() {
		return fila;
	}

	public int getColumna() {
		return columna;
	}

	public EstadoCasilla getResultado() {
		return resultado;
	}

	@Override
	public int compareTo(Jugada j) {
		return this.nJugada-j.getnJugada();
	}

	
}
