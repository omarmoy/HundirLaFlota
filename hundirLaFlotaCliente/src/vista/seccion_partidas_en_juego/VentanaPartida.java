package vista.seccion_partidas_en_juego;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import juego.Partida;
import vista.VentanaPrincipal;
import vista.recursos.Boton;
import vista.recursos.EtiquetaNegrita;
import vista.recursos.PanelTablero;

public class VentanaPartida extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private VentanaPrincipal p;
	private Partida partida;
	private PanelPartidas padre;
	private JTextField filaEntrada;
	private JTextField columnaEntrada;

	public VentanaPartida(VentanaPrincipal p, Partida partida, PanelPartidas padre) {
		super("Partida " + partida.getId() + " contra " + partida.getRival());
		this.p = p;
		this.partida = partida;
		this.padre = padre;
		getContentPane().setLayout(new BorderLayout());
		this.setSize(600, 800);
		this.setVisible(true);
		mostrarContenido();
	}

	private void mostrarContenido() {
		// centro
		JPanel tableros = new JPanel(new BorderLayout());
		PanelTablero tableroRival = new PanelTablero(partida.getVistaRival(), this);
		PanelTablero tableroPropio = new PanelTablero(partida.getTablero(), false);
		tableros.add(tableroRival, BorderLayout.NORTH);
		tableros.add(tableroPropio, BorderLayout.SOUTH);

		getContentPane().add(tableros, BorderLayout.CENTER);

		// menú izquierda
		JPanel menu = new JPanel(new GridLayout(20, 1));
		EtiquetaNegrita turnoJuego = new EtiquetaNegrita("JUGAR");
		turnoJuego.setOpaque(true);
		turnoJuego.setHorizontalAlignment(SwingConstants.CENTER);
		if (partida.isTurnoJugador()) {
			turnoJuego.setBackground(Color.GREEN);
		} else {
			turnoJuego.setBackground(Color.RED);
		}
		JLabel fila = new JLabel("Fila:");
		JLabel columna = new JLabel("Columna:");
		this.filaEntrada = new JTextField();
		this.columnaEntrada = new JTextField();
		filaEntrada.setHorizontalAlignment(SwingConstants.CENTER);
		columnaEntrada.setHorizontalAlignment(SwingConstants.CENTER);
		filaEntrada.setEditable(false);
		columnaEntrada.setEditable(false);
		filaEntrada.setBorder(BorderFactory.createLineBorder(Color.WHITE));
		columnaEntrada.setBorder(BorderFactory.createLineBorder(Color.WHITE));
		filaEntrada.setFont(filaEntrada.getFont().deriveFont(Font.BOLD));
		columnaEntrada.setFont(filaEntrada.getFont().deriveFont(Font.BOLD));
		Boton disparar = new Boton("Disparar");
		JLabel espacio = new JLabel(" ");
		Boton renunciar = new Boton("Renunciar");

		menu.add(turnoJuego);
		menu.add(fila);
		menu.add(filaEntrada);
		menu.add(columna);
		menu.add(columnaEntrada);
		menu.add(disparar);
		menu.add(espacio);
		menu.add(renunciar);
		getContentPane().add(menu, BorderLayout.EAST);

		this.getRootPane().setDefaultButton(disparar);

		partida.setVista(this); // para poder repintar desde allí

		if (!partida.isTurnoJugador() || partida.isTerminada()) {
			filaEntrada.setEnabled(false);
			columnaEntrada.setEnabled(false);
			disparar.setEnabled(false);
		}

		if (partida.isTerminada()) {
			renunciar.setEnabled(false);
		}

		// accion botones
		renunciar.addActionListener(e -> {
			int eleccion = DialogoRenunciar.mostrar(this);
			if (eleccion == 0) {
				p.conexion.rendirse(partida);
				this.dispose();
				padre.repintar();
			}
		});

		disparar.addActionListener(e -> {

			if (disparoCorrecto(filaEntrada.getText(), columnaEntrada.getText())) {
				enviarDisparo(filaEntrada.getText(), columnaEntrada.getText());
			} else {
				JOptionPane.showMessageDialog(this,
						"Fila y columna han de ser una letra y un número entre A-J y 1-10 respectivamente", "ERROR",
						JOptionPane.ERROR_MESSAGE);
			}
			filaEntrada.setText("");
			columnaEntrada.setText("");
			this.padre.repintar();
		});
	}

	public void repintar() {
		this.getContentPane().removeAll();
		this.mostrarContenido();
		repaint();
		revalidate();
	}

	private boolean disparoCorrecto(String fila, String columna) {
		try {
			int columnaN = Integer.parseInt(columna);
			boolean correcto = (columnaN > 0 && columnaN < 11);
			switch (fila.toLowerCase()) {
			case "a":
			case "b":
			case "c":
			case "d":
			case "e":
			case "f":
			case "g":
			case "h":
			case "i":
			case "j":
				break;
			default:
				correcto = false;
				break;
			}
			return correcto;
		} catch (Exception e) {
			return false;
		}
	}

	private void enviarDisparo(String filaE, String columnaE) {
		int fila = codificarFila(filaE);
		int columna = Integer.parseInt(columnaE) - 1;
		String envio = "disparo\n";
		envio += partida.getId() + " ";
		envio += partida.getJugador() + " ";
		envio += (partida.getJugadas().size() + 1) + " "; // número de jugada
		envio += fila + " " + columna;
		p.conexion.enviar(envio);
		partida.setTurnoJugador(false);
	}

	private int codificarFila(String filaE) {
		int fila = -1;
		switch (filaE.toLowerCase()) {
		case "a":
			fila = 0;
			break;
		case "b":
			fila = 1;
			break;
		case "c":
			fila = 2;
			break;
		case "d":
			fila = 3;
			break;
		case "e":
			fila = 4;
			break;
		case "f":
			fila = 5;
			break;
		case "g":
			fila = 6;
			break;
		case "h":
			fila = 7;
			break;
		case "i":
			fila = 8;
			break;
		case "j":
			fila = 9;
			break;
		default:
			// nada
		}

		return fila;
	}

	public void finPartida(boolean victoria) {
		if (victoria) {
			String m = partida.numeroDeHundimientos(true)<5? " POR ABANDONO":"";
			JOptionPane.showMessageDialog(this, "VICTORIA"+m, "Fin Partida", JOptionPane.INFORMATION_MESSAGE);
		} else {
			JOptionPane.showMessageDialog(this, "DERROTA", "Fin Partida", JOptionPane.ERROR_MESSAGE);
		}
		repintar();
	}
	
	public void seleccinarDisparo (String fila, String columna) {
		this.filaEntrada.setText(fila);
		this.columnaEntrada.setText(columna);
	}
}
