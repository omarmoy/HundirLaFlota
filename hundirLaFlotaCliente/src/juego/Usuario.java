package juego;

public class Usuario {
	private String nombre;
	private boolean retado;
	private boolean retador;

	public Usuario(String nombre) {
		this.nombre = nombre;
		this.retado = false;
		this.retador = false;
	}

	public String getNombre() {
		return nombre;
	}	

	public boolean isRetado() {
		return retado;
	}

	public void setRetado(boolean retado) {
		this.retado = retado;
	}

	public boolean isRetador() {
		return retador;
	}

	public void setRetador(boolean retador) {
		this.retador = retador;
	}

	@Override
	public boolean equals(Object obj) {
		
		if (obj == null || obj.getClass() != this.getClass())
			return false;
		
		return nombre.equals(((Usuario) obj).getNombre());
	}

	@Override
	public String toString() {
		if (retado) {
			return nombre + ": reto enviado, esperando confirmación";
		} else if (retador) {
			return nombre + ": te ha retado";
		}else {
			return nombre;
		}
	}

}
