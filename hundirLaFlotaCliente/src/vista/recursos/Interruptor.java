package vista.recursos;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JToggleButton;

import vista.seccion_partidas_en_juego.VentanaPartida;

// TODO: BORRAR CLASE NO USADA
public class Interruptor extends JToggleButton {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public Interruptor(String ruta, String fila, String columna, VentanaPartida vp, boolean activo) {
		setPreferredSize(new Dimension(35, 35));
		ImageIcon iconoOriginal = new ImageIcon(ruta);
		Image imagenOriginal = iconoOriginal.getImage();
		Image imagenEscalada = imagenOriginal.getScaledInstance(35, 35, Image.SCALE_AREA_AVERAGING); // 40
		ImageIcon iconoEscalado = new ImageIcon(imagenEscalada);

		ImageIcon iconoOriginalS = new ImageIcon("images/seleccionado.png");
		Image imagenOrigilaS = iconoOriginalS.getImage();
		Image imagenEscaladaS = imagenOrigilaS.getScaledInstance(35, 35, Image.SCALE_AREA_AVERAGING);
		ImageIcon iconoEscaladoS = new ImageIcon(imagenEscaladaS);

		ImageIcon dianaOriginal = new ImageIcon("images/diana.png");
		Image dianaImagen = dianaOriginal.getImage();
		Image dianaEscalada = dianaImagen.getScaledInstance(35, 35, Image.SCALE_AREA_AVERAGING);
		ImageIcon dianaOE = new ImageIcon(dianaEscalada);

		setIcon(iconoEscalado);
		setEnabled(activo);
		setSelectedIcon(dianaOE);
		if (!activo)
			setDisabledIcon(iconoEscalado);

		// accion: pasar el raton por encima
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				setIcon(iconoEscaladoS);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				setIcon(iconoEscalado);
			}
		});

		addActionListener(e -> {			
			if (isSelected()) {
				vp.seleccinarDisparo(fila, columna);
			} else {
				vp.seleccinarDisparo("", ""); //TODO: nofunciona
			}
		});

	}
}

//if(!seleccionado) {
//seleccionado = true;
//vp.seleccinarDisparo(fila, columna);
//}else {
//seleccionado = false;
////setSelected(false);
//doClick();
//vp.seleccinarDisparo("", "");
//}
