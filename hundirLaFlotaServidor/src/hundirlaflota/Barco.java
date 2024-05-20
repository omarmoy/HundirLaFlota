package hundirlaflota;

public class Barco {
	protected int tamaño;
	protected boolean horizontal;
	protected int filaInicio;
	protected int columnaInicio;

	public Barco(int tamaño, boolean horizontal, int filaInicio, int columnaInicio) {
		this.tamaño = tamaño;
		this.horizontal = horizontal;
		this.filaInicio = filaInicio;
		this.columnaInicio = columnaInicio;
	}
	
	public Barco(int tamaño) {
		this.tamaño = tamaño;
	}
	
	
	
}
