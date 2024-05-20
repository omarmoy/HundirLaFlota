package hundirlaflota;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Tablero {

	public enum EstadoCasilla {
		AGUA, BARCO, TOCADO, VACIO, HUNDIDO
	}

	private static final int TAMAÑO_TABLERO = 10;
	private static final int[] TAMAÑO_BARCOS = { 5, 4, 3, 3, 2 };
	private Random rand = new Random();
	private EstadoCasilla[][] casillas = new EstadoCasilla[TAMAÑO_TABLERO][TAMAÑO_TABLERO];
	private ArrayList<Coordenada> coordenadas = new ArrayList<Coordenada>();

	public Tablero(Boolean generarBarcos) {
		if (generarBarcos)
			generarBarcosAleatorios();
		else
			tableroVacio();
	}

	private void generarBarcosAleatorios() {
		boolean barcosColocados = false;
		int contadorColocados = 0;
		while (!barcosColocados) {
			tableroVacio();
			Collections.shuffle(coordenadas);
			for (int tamaño : TAMAÑO_BARCOS) {
				for (Coordenada coordenada : coordenadas) {
					if (colocarBarco(new Barco(tamaño, rand.nextBoolean(), coordenada.fila, coordenada.columna))) {
						contadorColocados++;
						break;
					}
				}
				if (contadorColocados == TAMAÑO_BARCOS.length) {
					barcosColocados = true;
					break;
				}
			}
			contadorColocados = 0;
		}
	}

	private boolean colocarBarco(Barco barco) {
		if (barco == null)
			return false;

		if (sePuedeColocar(barco)) {
			if (barco.horizontal) {
				for (int j = barco.columnaInicio; j < (barco.columnaInicio + barco.tamaño); j++) {
					casillas[barco.filaInicio][j] = EstadoCasilla.BARCO;
				}
			} else {
				for (int i = barco.filaInicio; i < (barco.filaInicio + barco.tamaño); i++) {
					casillas[i][barco.columnaInicio] = EstadoCasilla.BARCO;
				}
			}
			return true;

		} else {
			return false;
		}
	}

	private boolean sePuedeColocar(Barco barco) {

		if (!estaEnTablero(barco.filaInicio, barco.columnaInicio)) {
			return false;
		}

		if (barco.horizontal) {
			if (barco.tamaño > (TAMAÑO_TABLERO - barco.columnaInicio)) {
				return false;
			}
			for (int i = barco.filaInicio - 1; i < barco.filaInicio + 2; i++) {
				for (int j = barco.columnaInicio - 1; j < barco.tamaño + barco.columnaInicio + 1; j++) {
					if (estaEnTablero(i, j) && casillas[i][j] == EstadoCasilla.BARCO) {
						return false;
					}
				}
			}

		} else {
			if (barco.tamaño > (TAMAÑO_TABLERO - barco.filaInicio)) {
				return false;
			}
			for (int i = barco.filaInicio - 1; i < barco.filaInicio + barco.tamaño + 1; i++) {
				for (int j = barco.columnaInicio - 1; j < barco.columnaInicio + 2; j++) {
					if (estaEnTablero(i, j) && casillas[i][j] == EstadoCasilla.BARCO) {
						return false;
					}
				}
			}
		}

		return true;

	}

	private boolean estaEnTablero(int i, int j) {
		return (i >= 0 && i < TAMAÑO_TABLERO) && (j >= 0 && j < TAMAÑO_TABLERO);
	}

	private void tableroVacio() {
		for (int i = 0; i < TAMAÑO_TABLERO; i++) {
			for (int j = 0; j < TAMAÑO_TABLERO; j++) {
				this.casillas[i][j] = EstadoCasilla.VACIO;
				this.coordenadas.add(new Coordenada(i, j));
			}
		}
	}

	public void colocarCasilla(int fila, int columna, EstadoCasilla estado) {
		this.casillas[fila][columna] = estado;
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
				tabla += casillaToString(casillas[i][j]) + " ";
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

	private String casillaToString(EstadoCasilla estado) {
		String stringCasilla = "";
		switch (estado) {
		case VACIO:
			stringCasilla = "*";
			break;
		case BARCO:
			stringCasilla = "B";
			break;
		case AGUA:
			stringCasilla = "A";
			break;
		case TOCADO:
			stringCasilla = "T";
			break;
		case HUNDIDO:
			stringCasilla = "H";
			break;
		default:
			break;
		}

		return stringCasilla;
	}

	// Método disparar
	public int disparar(int fila, int columna) {

		switch (casillas[fila][columna]) {
		case BARCO:
		case TOCADO:
		case HUNDIDO:
			casillas[fila][columna] = EstadoCasilla.TOCADO;
			break;
		default:
			casillas[fila][columna] = EstadoCasilla.AGUA;
			return 0;
		}
		if (comprobarBarcoHundido(fila, columna)) {
			casillas[fila][columna] = EstadoCasilla.HUNDIDO;
			return 2;
		} else {
			return 1;
		}
	}

	private boolean comprobarBarcoHundido(int fila, int columna) {
		ArrayList<Coordenada> hundidos = new ArrayList<Coordenada>();
		boolean arriba = true;
		boolean abajo = true;
		boolean derecha = true;
		boolean izquierda = true;
		// comprobación horizontal derecha:
		for (int j = columna; j < TAMAÑO_TABLERO; j++) {
			if (casillas[fila][j] == EstadoCasilla.BARCO) {
				derecha = false;
				break;
			} else if (casillas[fila][j] == EstadoCasilla.TOCADO) {
				hundidos.add(new Coordenada(fila, j));
			} else {
				break;
			}
		}
		// comprobación horizontal izq:
		for (int j = columna; j >= 0; j--) {
			if (casillas[fila][j] == EstadoCasilla.BARCO) {
				izquierda = false;
				break;
			} else if (casillas[fila][j] == EstadoCasilla.TOCADO) {
				hundidos.add(new Coordenada(fila, j));
			} else {
				break;
			}
		}
		// comprobación vertical abajo:
		for (int i = fila; i < TAMAÑO_TABLERO; i++) {
			if (casillas[i][columna] == EstadoCasilla.BARCO) {
				abajo = false;
				break;
			} else if (casillas[i][columna] == EstadoCasilla.TOCADO) {
				hundidos.add(new Coordenada(i, columna));
			} else {
				break;
			}
		}
		// comprobación vertical arriba:
		for (int i = fila; i >= 0; i--) {
			if (casillas[i][columna] == EstadoCasilla.BARCO) {
				arriba = false;
				break;
			} else if (casillas[i][columna] == EstadoCasilla.TOCADO) {
				hundidos.add(new Coordenada(i, columna));
			} else {
				break;
			}
		}
		
		return arriba && abajo && derecha && izquierda;
	}

	public boolean todosHundidos() {
		int aciertosParaTerminar = TAMAÑO_BARCOS.length;
		int aciertos = 0;
		for (int i = 0; i < TAMAÑO_TABLERO; i++) {
			for (int j = 0; j < TAMAÑO_TABLERO; j++) {
				if (casillas[i][j] == EstadoCasilla.HUNDIDO)
					aciertos++;
			}
		}
		return aciertos == aciertosParaTerminar;
	}

	public void cargarJugada(int fila, int columna, EstadoCasilla resultado) {
		casillas[fila][columna] = resultado;
	}

	@Override
	public String toString() {
		String infoTablero = "";
		for (int i = 0; i < TAMAÑO_TABLERO; i++) {
			for (int j = 0; j < TAMAÑO_TABLERO; j++) {
				infoTablero += casillaToString(casillas[i][j]);
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

	// getters

}
