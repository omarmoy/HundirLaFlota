package vista.seccion_partida_nueva;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import juego.Usuario;
import vista.VentanaPrincipal;
import vista.recursos.EtiquetaNegrita;

import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;

import java.util.ArrayList;

public class PanelUsuariosConectados extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private ArrayList<Usuario> usuarios;
	private JList<Usuario> lista;
	private VentanaPrincipal p;
	private DefaultListModel<Usuario> modeloLista;

	public PanelUsuariosConectados(VentanaPrincipal p) {
		super();
		this.usuarios = p.conexion.getGJ().getJugadoresConectados();
		this.p = p;
		// panel principal
		setBackground(Color.ORANGE);
		setLayout(new BorderLayout());

		// cabecera
		JPanel cabecera = new JPanel(new BorderLayout());
		EtiquetaNegrita titulo = new EtiquetaNegrita("Usuarios conectados", 25);
		titulo.setHorizontalAlignment(SwingConstants.CENTER);
		EmptyBorder bordeT = new EmptyBorder(10, 0, 10, 0);
		titulo.setBorder(bordeT);
		cabecera.add(titulo, BorderLayout.CENTER);
		add(cabecera, BorderLayout.NORTH);

		// cuerpo
		modeloLista = new DefaultListModel<>();
		lista = new JList<>(modeloLista);
		lista.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		lista.setCellRenderer(new FuenteListaUsuarios());
		JScrollPane scrollPane = new JScrollPane(lista);
		add(scrollPane, BorderLayout.CENTER);

		// rellenar lista
		for (Usuario usuario : usuarios) {
			modeloLista.addElement(usuario);
		}

		// acciones
		lista.addListSelectionListener(e -> accionUsuario());

		p.conexion.getGJ().setVistaUsuarios(this);
	}

	private void accionUsuario() {
		Usuario usuario = lista.getSelectedValue();
		if (usuario != null) {
			if (usuario.isRetador()) {
				int eleccion = DialogoRetoActivo.mostrar(this, usuario.getNombre());
				if (eleccion == JOptionPane.YES_OPTION) {
					usuario.setRetador(false);
					p.conexion.aceptarReto(usuario.getNombre());
					repintar();
				} else if (eleccion == JOptionPane.NO_OPTION) {
					usuario.setRetador(false);
					p.conexion.getGJ().actualizarRetosPdt();
					repintar();
				}

			} else {
				int eleccion = DialogoRetar.mostrar(this,
						"¿Quieres retar a una partida a " + usuario.getNombre() + "?");
				if (eleccion == 0) {
					usuario.setRetado(true);
					p.conexion.enviarReto(usuario.getNombre());
					repintar();
				}
			}
		}
	}

	public void repintar() {
		lista.clearSelection();
		modeloLista.clear();
		for (Usuario usuario : usuarios) {
			modeloLista.addElement(usuario);
		}
		revalidate();
		repaint();
	}

}
