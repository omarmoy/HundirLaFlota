package vista.recursos;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class Boton extends JButton {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public Boton(String texto) {
		setText(texto);
//		setPreferredSize(new Dimension(100, 100));  
		diseñoPredeterminado();

	}
	
	public Boton(ImageIcon imagen) {
		setPreferredSize(new Dimension(50, 50));  
		Image imagenEscalada = imagen.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
		ImageIcon icono = new ImageIcon(imagenEscalada);
		setIcon(icono);
		diseñoPredeterminado();

	}
	
	private void diseñoPredeterminado() {
		setForeground(Color.BLACK);
		setBackground(Color.WHITE);
		Border line = new LineBorder(Color.BLACK);
		Border margin = new EmptyBorder(0, 15, 5, 15);
		Border compound = new CompoundBorder(line, margin);
		setBorder(compound);
		
		setVerticalTextPosition(SwingConstants.BOTTOM);
		
		// accion: pasar el raton por encima
		addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setBackground(Color.LIGHT_GRAY);
            }

            @Override
            public void mouseExited(MouseEvent e) {
//                setBackground(UIManager.getColor("Button.background"));
        		setBackground(Color.WHITE);
            }
        });

	}
}

//button.setForeground(Color.BLACK);
//button.setBackground(Color.WHITE);
//Border line = new LineBorder(Color.BLACK);
//Border margin = new EmptyBorder(5, 15, 5, 15);
//Border compound = new CompoundBorder(line, margin);
//button.setBorder(compound);