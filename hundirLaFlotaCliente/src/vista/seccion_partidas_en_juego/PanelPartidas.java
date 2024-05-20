package vista.seccion_partidas_en_juego;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import juego.Partida;
import vista.VentanaPrincipal;
import vista.recursos.EtiquetaNegrita;

import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;

import java.util.ArrayList;

public class PanelPartidas extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private ArrayList<Partida> partidas;
	private JList<Partida> listaConectados;
	private DefaultListModel<Partida> modeloListaConectados;
	private JList<Partida> listaNoConectadosTurno;
	private DefaultListModel<Partida> modeloListaNoConectadosTurno;
	private JList<Partida> listaNoConectados;
	private DefaultListModel<Partida> modeloListaNoConectados;

	private VentanaPrincipal p;

	public PanelPartidas(VentanaPrincipal p) {
		super();
		this.partidas = p.conexion.getGJ().getPartidas();
		this.p = p;

		// panel principal
		setLayout(new BorderLayout());

		// cabecera
		JPanel cabecera = new JPanel(new BorderLayout());
		EtiquetaNegrita titulo = new EtiquetaNegrita("Partidas en Juego", 25);
		titulo.setHorizontalAlignment(SwingConstants.CENTER);
		EmptyBorder bordeT = new EmptyBorder(10, 0, 10, 0);
		titulo.setBorder(bordeT);
		cabecera.add(titulo, BorderLayout.CENTER);

		JPanel subtitulos = new JPanel(new GridLayout(1, 3));
		JLabel conectados = new JLabel("Rivales Conectados");
		JLabel noConectadosTurno = new JLabel("No conectados en turno");
		JLabel noConectados = new JLabel("No conectados sin turno");
		subtitulos.add(conectados);
		subtitulos.add(noConectadosTurno);
		subtitulos.add(noConectados);
		cabecera.add(subtitulos, BorderLayout.SOUTH);

		add(cabecera, BorderLayout.NORTH);

		// cuerpo
		JPanel cuerpo = new JPanel(new GridLayout(1, 3));

		modeloListaConectados = new DefaultListModel<>();
		listaConectados = new JList<>(modeloListaConectados);
		listaConectados.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listaConectados.setCellRenderer(new FuenteListaPartidas());
		JScrollPane scrollPane = new JScrollPane(listaConectados);
		cuerpo.add(scrollPane);

		modeloListaNoConectadosTurno = new DefaultListModel<>();
		listaNoConectadosTurno = new JList<>(modeloListaNoConectadosTurno);
		listaNoConectadosTurno.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listaNoConectadosTurno.setCellRenderer(new FuenteListaNoConectados());
		JScrollPane scrollPane2 = new JScrollPane(listaNoConectadosTurno);
		cuerpo.add(scrollPane2);

		modeloListaNoConectados = new DefaultListModel<>();
		listaNoConectados = new JList<>(modeloListaNoConectados);
		listaNoConectados.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listaNoConectados.setCellRenderer(new FuenteListaNoConectados());
		JScrollPane scrollPane3 = new JScrollPane(listaNoConectados);
		cuerpo.add(scrollPane3);

		add(cuerpo, BorderLayout.CENTER);

		// rellenar listas
		for (Partida partida : partidas) {
			if (partida.isRivalConectado() && !partida.isTerminada())
				modeloListaConectados.addElement(partida);
		}
		for (Partida partida : partidas) {

			if (!partida.isRivalConectado() && partida.isTurnoJugador() && !partida.isTerminada()) {
				modeloListaNoConectadosTurno.addElement(partida);
			}
		}
		for (Partida partida : partidas) {
			if (!partida.isRivalConectado() && !partida.isTurnoJugador()&& !partida.isTerminada()) {
				modeloListaNoConectados.addElement(partida);			
			}
		}
		// acciones
		listaNoConectadosTurno.addListSelectionListener(e -> partidaNoConectadosTurno());
		listaNoConectados.addListSelectionListener(e -> partidaNoConectados());
		listaConectados.addListSelectionListener(e -> {
			if (!e.getValueIsAdjusting()) { // Evitar procesar eventos intermedios
				int indiceSeleccionado = listaConectados.getSelectedIndex();
				// Verificar que hay una selección válida
				if (indiceSeleccionado != -1) {
					VentanaPartida vp = new VentanaPartida(p, listaConectados.getSelectedValue(), this);
					vp.setVisible(true);
					listaConectados.clearSelection();
				}
			}
		});
		
		p.conexion.getGJ().setVistaPartidas(this);
	}

	private void partidaNoConectadosTurno() {
		if (listaNoConectadosTurno.getSelectedValue() != null) {
			VentanaPartida vp = new VentanaPartida(p, listaNoConectadosTurno.getSelectedValue(), this);
			vp.setVisible(true);
			listaNoConectadosTurno.clearSelection();
		}
	}

	private void partidaNoConectados() {
		if (listaNoConectados.getSelectedValue() != null) {
			VentanaPartida vp = new VentanaPartida(p, listaNoConectados.getSelectedValue(), this);
			vp.setVisible(true);
			listaNoConectados.clearSelection();
		}
	}

	public void repintar() {
		listaConectados.clearSelection();
		listaNoConectadosTurno.clearSelection();
		listaNoConectados.clearSelection();
		modeloListaConectados.clear();
		modeloListaNoConectadosTurno.clear();
		modeloListaNoConectados.clear();
		partidas = p.conexion.getGJ().getPartidas();

		for (Partida partida : partidas) {
			if (partida.isRivalConectado() && !partida.isTerminada())
				modeloListaConectados.addElement(partida);
		}
		for (Partida partida : partidas) {

			if (!partida.isRivalConectado() && partida.isTurnoJugador() && !partida.isTerminada()) {
				modeloListaNoConectadosTurno.addElement(partida);
			}
		}
		for (Partida partida : partidas) {
			if (!partida.isRivalConectado() && !partida.isTurnoJugador()&& !partida.isTerminada()) {
				modeloListaNoConectados.addElement(partida);			
			}
		}
		revalidate();
		repaint();
	}

}
