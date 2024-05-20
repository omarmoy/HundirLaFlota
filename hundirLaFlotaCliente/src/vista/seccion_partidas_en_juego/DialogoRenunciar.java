package vista.seccion_partidas_en_juego;

import java.awt.Component;

import javax.swing.JOptionPane;

public class DialogoRenunciar extends JOptionPane {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static int mostrar(Component padre) {
		Object[] options = { "Rendirse", "Cancelar" };
		return showOptionDialog(padre, "La partida se dará por perdida", "Atención", JOptionPane.YES_NO_OPTION,
				JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
	}

}
