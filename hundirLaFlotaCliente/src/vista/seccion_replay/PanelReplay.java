package vista.seccion_replay;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.DefaultListModel;
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

public class PanelReplay extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private ArrayList<Partida> partidas;
	private JList<Partida> lista;
	private VentanaPrincipal principal;
	private DefaultListModel<Partida> modeloLista;

	public PanelReplay(VentanaPrincipal p) {
		super();
		this.partidas = p.conexion.getGJ().getPartidas();
		this.principal = p;

		// panel principal
		setBackground(Color.ORANGE);
		setLayout(new BorderLayout());

		// cabecera
		JPanel cabecera = new JPanel(new BorderLayout());
		EtiquetaNegrita titulo = new EtiquetaNegrita("Partidas Replay", 25);
		titulo.setHorizontalAlignment(SwingConstants.CENTER);
		EmptyBorder bordeT = new EmptyBorder(10, 0, 10, 0);
		titulo.setBorder(bordeT);
		cabecera.add(titulo, BorderLayout.CENTER);
		add(cabecera, BorderLayout.NORTH);

		// cuerpo
		modeloLista = new DefaultListModel<>();
		lista = new JList<>(modeloLista);
		lista.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		lista.setCellRenderer(new FuenteListaReplay());
		JScrollPane scrollPane = new JScrollPane(lista);
		add(scrollPane, BorderLayout.CENTER);

		// rellenar lista
		for (Partida partida : partidas) {
			if (partida.isTerminada())
				modeloLista.addElement(partida);
		}

		// acciones
		lista.addListSelectionListener(e -> verReplay());

		p.conexion.getGJ().setVistaReplay(this);
	}

	private void verReplay() {
		if (lista.getSelectedValue() != null) {
			DialogoPartidaReplay r = new DialogoPartidaReplay(principal, lista.getSelectedValue());
			r.setVisible(true);
			lista.clearSelection();
		}
	}

	public void repintar() {
		lista.clearSelection();
		modeloLista.clear();
		for (Partida partida : partidas) {
			if (partida.isTerminada())
				modeloLista.addElement(partida);
		}
		revalidate();
		repaint();
	}

}
