package juego;

public class Coordenada {	
	protected Integer fila;
	protected Integer columna;	
	public Coordenada(int fila, int columna) {
		this.fila = fila;
		this.columna = columna;
	}
	@Override
	public String toString() {
		return fila.toString()+columna.toString()+" ";
	}
	
	
}
