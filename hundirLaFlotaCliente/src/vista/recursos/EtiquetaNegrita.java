package vista.recursos;

import java.awt.Font;
import java.io.File;

import javax.swing.JLabel;

public class EtiquetaNegrita extends JLabel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public EtiquetaNegrita(String texto) {
		super(texto);
		
		Font miFuente=null;
		try {
			miFuente = Font.createFont(Font.TRUETYPE_FONT, new File("fuentes/minecraft_font.ttf"));
			miFuente = miFuente.deriveFont(Font.PLAIN, 12);
		} catch (Exception e) {
			e.printStackTrace();
		}
		setFont(miFuente);
	}
	
	public EtiquetaNegrita(String texto, int tam) {
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
