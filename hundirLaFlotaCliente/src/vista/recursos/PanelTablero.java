package vista.recursos;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Image;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import juego.Tablero;
import vista.seccion_partidas_en_juego.VentanaPartida;

public class PanelTablero extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ArrayList<Character> listaCasillas;


	/**
	 * Crea un PanelTablero iteractivo para realizar disparos.
	 * 
	 * @param tablero info actual del rival
	 * @param vp      VentanaPartida para repintar desde Interruptor
	 */
	public PanelTablero(Tablero tablero, VentanaPartida vp) {
		JPanel contenido = new JPanel(new BorderLayout());
		JPanel centro = new JPanel(new GridLayout(10, 10));
		centro.setBorder(BorderFactory.createLineBorder(Color.BLACK));

		int nChar = 65;
		Integer nColu = 1;
		int contador = 0;
		ButtonGroup grupo = new ButtonGroup();
		this.listaCasillas = tablero.listaCasillas();
		for (Character casilla : listaCasillas) {
			Interruptor interruptor;
			char letra = (char) nChar;
			switch (casilla) {
			case 'B':
				interruptor = new Interruptor("images/barco.png", String.valueOf(letra), nColu.toString(), vp, false);
				break;
			case 'A':
				interruptor = new Interruptor("images/agua.png", String.valueOf(letra), nColu.toString(), vp, false);
				break;
			case 'T':
				interruptor = new Interruptor("images/tocado.png", String.valueOf(letra), nColu.toString(), vp, false);
				break;
			case 'H':
				interruptor = new Interruptor("images/hundido.png", String.valueOf(letra), nColu.toString(), vp, false);
				break;
			default:
				interruptor = new Interruptor("images/oculto.png", String.valueOf(letra), nColu.toString(), vp, true);
				break;
			}
			grupo.add(interruptor);
			centro.add(interruptor);
			contador++;
			nColu++;
			if (contador % 10 == 0) {
				nColu = 1;
				nChar++;
			}
		}

		JPanel izq = new JPanel(new GridLayout(10, 1));
		nChar = 65;
		for (int i = 0; i < 10; i++) {
			char letra = (char) nChar;
			izq.add(new EtiquetaCoordenadas(String.valueOf(letra)));
			nChar++;
		}

		JPanel arriba = new JPanel(new GridLayout(1, 11));
		EtiquetaCoordenadas espacio = new EtiquetaCoordenadas(" ");
		arriba.add(espacio);
		for (int i = 1; i <= 10; i++) {
			arriba.add(new EtiquetaCoordenadas(String.valueOf(i)));
		}

		contenido.add(arriba, BorderLayout.NORTH);
		contenido.add(izq, BorderLayout.WEST);
		contenido.add(centro, BorderLayout.CENTER);
		add(contenido);
	}

	/**
	 * Crea un tablero con imágenes
	 * 
	 * @param tablero info del tablero
	 * @param rival booleano para diferenciar entre el tablero rival
	 * y el propio. 
	 */
	public PanelTablero(Tablero tablero, boolean rival) {
		JPanel contenido = new JPanel(new BorderLayout());
		JPanel centro = new JPanel(new GridLayout(10, 10));
		centro.setBorder(BorderFactory.createLineBorder(Color.BLACK));

		this.listaCasillas = tablero.listaCasillas();
		for (Character casilla : listaCasillas) {
			JLabel imagen;
			switch (casilla) {
			case 'B':
				imagen = generarCasilla("images/barco.png");
				break;
			case 'A':
				imagen = generarCasilla("images/agua.png");
				break;
			case 'T':
				imagen = generarCasilla("images/tocado.png");
				break;
			case 'H':
				imagen = generarCasilla("images/hundido.png");
				break;
			default:
				if (rival)
					imagen = generarCasilla("images/oculto.png");
				else
					imagen = generarCasilla("images/vacio.png");
				break;
			}
			centro.add(imagen);
		}

		if (rival) {
			JPanel izq = new JPanel(new GridLayout(10, 1));
			int nChar = 65;
			for (int i = 0; i < 10; i++) {
				char letra = (char) nChar;
				izq.add(new EtiquetaCoordenadas(String.valueOf(letra)));
				nChar++;
			}
			
			JPanel arriba = new JPanel(new GridLayout(1, 11));
			EtiquetaCoordenadas espacio = new EtiquetaCoordenadas(" ");
			arriba.add(espacio);
			for (int i = 1; i <= 10; i++) {
				arriba.add(new EtiquetaCoordenadas(String.valueOf(i)));
			}

			contenido.add(arriba, BorderLayout.NORTH);
			contenido.add(izq, BorderLayout.WEST);
		}else {
			centro.setBorder(new EmptyBorder(0, 15, 0, 1));
		}
		
		contenido.add(centro, BorderLayout.CENTER);
		add(contenido);
	}

	/**
	 * Reescala una imagen y devuelve un JLabel con dicha imagen
	 * 
	 * @param ruta de la imagen
	 * @return JLabel
	 */
	private JLabel generarCasilla(String ruta) {
		ImageIcon iconoOriginal = new ImageIcon(ruta);
		Image imagenOriginal = iconoOriginal.getImage();
		Image imagenEscalada = imagenOriginal.getScaledInstance(35, 35, Image.SCALE_AREA_AVERAGING); // 40
		return new JLabel(new ImageIcon(imagenEscalada));
	}
}
