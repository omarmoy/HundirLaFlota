package vista.seccion_replay;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import juego.Partida;
import vista.VentanaPrincipal;
import vista.recursos.Boton;
import vista.recursos.PanelTablero;

public class DialogoPartidaReplay extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Partida partida;

	public DialogoPartidaReplay(VentanaPrincipal p, Partida partida) {
		super(p.ventana, "Partida " + partida.getId() + " vs. " + partida.getRival(), true);
		this.partida = partida;
		getContentPane().setLayout(new BorderLayout());
		this.setSize(600, 800);
		setLocationRelativeTo(p.ventana);

		mostrarContenido();		
	}

	private void mostrarContenido() {
		//izquierda
		JPanel izquierda = new JPanel(new GridLayout(20,1));
		JLabel total = new JLabel("Total jugadas: "+partida.getJugadas().size());
		JLabel actual = new JLabel("Actual: "+partida.getTurnoReplay());
		izquierda.add(total);
		izquierda.add(actual);
		getContentPane().add(izquierda, BorderLayout.WEST);
		
		// centro
		JPanel tableros = new JPanel(new BorderLayout());
		PanelTablero rival = new PanelTablero(partida.getVistaRival(), true);
		PanelTablero propio = new PanelTablero(partida.getTablero(), false);
		tableros.add(rival, BorderLayout.NORTH);
		tableros.add(propio, BorderLayout.SOUTH);
		getContentPane().add(tableros, BorderLayout.CENTER);

		// menú abajo
		JPanel menu = new JPanel(new GridLayout(1, 2));
		Boton siguiente = new Boton("Siguiente");
		Boton inicio = new Boton("Inicio");
		menu.add(inicio);
		menu.add(siguiente);
		getContentPane().add(menu, BorderLayout.SOUTH);
		getRootPane().setDefaultButton(siguiente); // preselecciona botón
		inicio.addActionListener(e -> {
			partida.reiniciarReplay();
			repintar();
		});
		siguiente.addActionListener(e -> {
			partida.replaySiguienteJugada();
			repintar();
		});


		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				partida.reiniciarReplay();
			}
		});
	}

	public void repintar() {
		this.getContentPane().removeAll();
		this.mostrarContenido();
		repaint();
		revalidate();
	}
}
