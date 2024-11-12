package hundirlaflota;

public class Jugador {
	private String nombre;
	private String contra;
	
	public Jugador(String nombre, String contra) {
		this.nombre = nombre;
		this.contra = contra;
	}	

	public String getNombre() {
		return nombre;
	}

	public String getContra() {
		return contra;
	}

	@Override
	public boolean equals(Object obj) {
		
		if (obj == null || obj.getClass() != this.getClass())
			return false;
		
		boolean nombre = this.nombre.equals(((Jugador) obj).getNombre());
		boolean contra = this.contra.equals(((Jugador) obj).getContra());
		return nombre && contra;
	}

	
	
	
	
	
	
	
}
