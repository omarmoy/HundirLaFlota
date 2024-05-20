package vista.seccion_partida_nueva;

import java.awt.Component;

import javax.swing.JOptionPane;

public class DialogoRetar extends JOptionPane{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static int mostrar(Component padre, String mensaje) {
		Object[] options = { "Retar", "Cancelar" };
		return showOptionDialog(padre, mensaje, "Atención!", JOptionPane.YES_NO_OPTION,
				JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
	}
	
}
