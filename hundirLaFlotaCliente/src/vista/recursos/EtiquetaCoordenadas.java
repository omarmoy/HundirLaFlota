package vista.recursos;

import java.awt.Font;
import java.io.File;

import javax.swing.JLabel;

public class EtiquetaCoordenadas extends JLabel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public EtiquetaCoordenadas(String texto) {
		super(texto);
		Font miFuente=null;
		try {
			miFuente = Font.createFont(Font.TRUETYPE_FONT, new File("fuentes/minecraft_font.ttf"));
			miFuente = miFuente.deriveFont(Font.PLAIN, 20);
		} catch (Exception e) {
			e.printStackTrace();
		}
		setFont(miFuente);
	}
	
	public EtiquetaCoordenadas(String texto, int tam) {
		super(texto);
		
		Font miFuente=null;
		try {
			miFuente = Font.createFont(Font.TRUETYPE_FONT, new File("fuentes/minecraft_font.ttf"));
			miFuente = miFuente.deriveFont(Font.PLAIN, tam);
		} catch (Exception e) {
			e.printStackTrace();
		}
		setFont(miFuente);
	}
}
