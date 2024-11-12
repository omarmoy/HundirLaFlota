package vista.seccion_partidas_en_juego;

import java.awt.Component;
import java.awt.Font;
import java.io.File;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.border.EmptyBorder;


public class FuenteListaNoConectados extends DefaultListCellRenderer {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        
		JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
		
		EmptyBorder borde = new EmptyBorder(5, 20, 0, 0);

        // Aplicar el borde al JLabel
        label.setBorder(borde);
        
        Font miFuente = null;
		try {
			miFuente = Font.createFont(Font.TRUETYPE_FONT, new File("fuentes/minecraft-regular.otf"));
			miFuente = miFuente.deriveFont(Font.PLAIN, 12);
		} catch (Exception e) {
			e.printStackTrace();
		}
		label.setFont(miFuente);
        

        return label;
    }
}

