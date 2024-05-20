package vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

import vista.recursos.Boton;
import vista.seccion_partida_nueva.PanelUsuariosConectados;
import vista.seccion_partidas_en_juego.PanelPartidas;
import vista.seccion_replay.PanelReplay;

public class MenuLateral extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	Boton partidasEJ, nuevaPartida, replay;
	VentanaPrincipal principal;

	public MenuLateral(VentanaPrincipal principal) {
		super();

//		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setLayout(new GridLayout(12, 1));

		this.partidasEJ = new Boton("En Juego");
		this.nuevaPartida = new Boton("Nueva Partida");
		this.replay = new Boton("Replay");
		this.principal = principal;

		escuchaBotones();
		add(partidasEJ);
		add(nuevaPartida);
		add(replay);

		setBackground(Color.WHITE);
		setBorder(new LineBorder(Color.LIGHT_GRAY, 3, false));
		setPreferredSize(new Dimension(150, getHeight()));
	}

	private void escuchaBotones() {

		partidasEJ.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				cambiarPanel(new PanelPartidas(principal));
			}
		});

		nuevaPartida.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				cambiarPanel(new PanelUsuariosConectados(principal));
			}
		});
		
		replay.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				cambiarPanel(new PanelReplay(principal));
			}
		});
	}

	private void cambiarPanel(JPanel nuevoPanel) {
		Component menuActual = principal.ventana.getContentPane().getComponent(2); // Índice 2 para CENTER
		Component pie = principal.ventana.getContentPane().getComponent(3);
		principal.ventana.getContentPane().remove(menuActual);
		principal.ventana.getContentPane().remove(pie);

		principal.ventana.getContentPane().add(nuevoPanel, BorderLayout.CENTER);
		JLabel sur = new JLabel("óscar_márquez_moya\u00AE");
		sur.setHorizontalAlignment(SwingConstants.CENTER);
		principal.ventana.getContentPane().add(sur, BorderLayout.SOUTH);
        

		principal.ventana.repaint();
		principal.ventana.revalidate();
	}
}
