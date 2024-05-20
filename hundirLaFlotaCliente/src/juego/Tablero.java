package juego;

import java.util.ArrayList;

public class Tablero {

	public enum EstadoCasilla {
		AGUA, BARCO, TOCADO, VACIO, HUNDIDO
	}

	private static final int TAMAÑO_TABLERO = 10;
	private EstadoCasilla[][] casillas = new EstadoCasilla[TAMAÑO_TABLERO][TAMAÑO_TABLERO];

	public Tablero() {
		for (int i = 0; i < TAMAÑO_TABLERO; i++) {
			for (int j = 0; j < TAMAÑO_TABLERO; j++) {
				this.casillas[i][j] = EstadoCasilla.VACIO;
			}
		}
	}

	public Tablero(String[] datos) {
		for (int i = 0; i < TAMAÑO_TABLERO; i++) {
			for (int j = 0; j < TAMAÑO_TABLERO; j++) {
				if (datos[i].charAt(j) == 'B')
					this.casillas[i][j] = EstadoCasilla.BARCO;
				else
					this.casillas[i][j] = EstadoCasilla.VACIO;
			}
		}
	}

	public void actualizarCasilla(int fila, int columna, EstadoCasilla estado) {
		this.casillas[fila][columna] = estado;
		comprobarBarcoHundido(fila, columna);
	}

	/**
	 * saca por pantalla el tablero
	 */
	public void mostrarPorConsola() {
		System.out.println("");
		// dos primeras líneas
		String tabla = "  ";
		for (int i = 0; i < TAMAÑO_TABLERO; i++) {
			tabla += i + " ";
		}
		tabla += "\n  ";

		for (int i = 0; i < TAMAÑO_TABLERO; i++) {
			tabla += "--";
		}
		tabla += "\n";

		// tabla:
		for (int i = 0; i < TAMAÑO_TABLERO; i++) {
			tabla += i + "|";
			for (int j = 0; j < TAMAÑO_TABLERO; j++) {
				tabla += casillaToChar(casillas[i][j]) + " ";
			}
			tabla += "|" + i + "\n";
		}

		// dos últimas líneas
		tabla += "  ";
		for (int i = 0; i < TAMAÑO_TABLERO; i++) {
			tabla += i + " ";
		}
		tabla += "\n  ";
		for (int i = 0; i < TAMAÑO_TABLERO; i++) {
			tabla += "--";
		}
		System.out.println(tabla);
		System.out.println("");

	}

	private char casillaToChar(EstadoCasilla estado) {
		char charCasilla = ' ';
		switch (estado) {
		case VACIO:
			charCasilla = '*';
			break;
		case BARCO:
			charCasilla = 'B';
			break;
		case AGUA:
			charCasilla = 'A';
			break;
		case TOCADO:
			charCasilla = 'T';
			break;
		case HUNDIDO:
			charCasilla = 'H';
			break;
		default:
			break;
		}

		return charCasilla;
	}

	public ArrayList<Character> listaCasillas() {
		ArrayList<Character> lista = new ArrayList<Character>();
		for (int i = 0; i < TAMAÑO_TABLERO; i++) {
			for (int j = 0; j < TAMAÑO_TABLERO; j++) {
				lista.add(casillaToChar(casillas[i][j]));
			}
		}
		return lista;
	}

	@Override
	public String toString() {
		String infoTablero = "";
		for (int i = 0; i < TAMAÑO_TABLERO; i++) {
			for (int j = 0; j < TAMAÑO_TABLERO; j++) {
				infoTablero += casillaToChar(casillas[i][j]);
			}
			infoTablero += ";";
		}
		return infoTablero;
	}

	public static int getTamañoTablero() {
		return TAMAÑO_TABLERO;
	}

	public EstadoCasilla[][] getCasillas() {
		return casillas;
	}

	private void comprobarBarcoHundido(int fila, int columna) {
		ArrayList<Coordenada> barco = new ArrayList<Coordenada>();
		if (casillas[fila][columna] == EstadoCasilla.HUNDIDO) {
			// buscar barco horizontal derecha:
			for (int j = columna; j < TAMAÑO_TABLERO; j++) {
				if (casillas[fila][j] == EstadoCasilla.TOCADO || casillas[fila][j] == EstadoCasilla.HUNDIDO) {
					barco.add(new Coordenada(fila, j));
				} else {
					break;
				}
			}
			// buscar barco horizontal izq:
			for (int j = columna; j >= 0; j--) {
				if (casillas[fila][j] == EstadoCasilla.TOCADO || casillas[fila][j] == EstadoCasilla.HUNDIDO) {
					barco.add(new Coordenada(fila, j));
				} else {
					break;
				}
			}
			// buscar barco vertical abajo:
			for (int i = fila; i < TAMAÑO_TABLERO; i++) {
				if (casillas[i][columna] == EstadoCasilla.TOCADO || casillas[i][columna] == EstadoCasilla.HUNDIDO) {
					barco.add(new Coordenada(i, columna));
				} else {
					break;
				}
			}
			// buscar barco vertical arriba:
			for (int i = fila; i >= 0; i--) {
				if (casillas[i][columna] == EstadoCasilla.TOCADO || casillas[i][columna] == EstadoCasilla.HUNDIDO) {
					barco.add(new Coordenada(i, columna));
				} else {
					break;
				}
			}
			// poner el resto del barco hundido
			for (Coordenada c : barco) {
				casillas[c.fila][c.columna] = EstadoCasilla.HUNDIDO;
			}

		}
	}

}
