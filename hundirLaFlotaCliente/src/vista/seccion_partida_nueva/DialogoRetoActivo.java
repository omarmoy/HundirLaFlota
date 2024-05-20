package vista.seccion_partida_nueva;

import java.awt.Component;

import javax.swing.JOptionPane;

public class DialogoRetoActivo extends JOptionPane {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static int mostrar(Component padre, String retador) {
		Object[] options = { "Aceptar", "Rechazar" };
		return showOptionDialog(padre, retador + " quiere jugar contigo", "Atención", JOptionPane.YES_NO_OPTION,
				JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
	}

}
