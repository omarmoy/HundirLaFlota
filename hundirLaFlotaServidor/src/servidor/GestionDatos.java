package servidor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import hundirlaflota.Jugada;
import hundirlaflota.Jugador;
import hundirlaflota.Partida;
import hundirlaflota.Tablero;
import hundirlaflota.Tablero.EstadoCasilla;
// TODO: documentación. 
public class GestionDatos {

	private static GestionDatos instancia;
	private static final String nameDatabase = "HundirLaFlota";
	private static final String user = "root";
	private static final String pass = "MANAGER";
	private static final String url = "jdbc:mysql://localhost:3306/";

	private GestionDatos() {
	}

	public static GestionDatos getInstancia() {
		if (instancia == null) {
			instancia = new GestionDatos();
		}
		return instancia;
	}

	public synchronized ArrayList<Jugador> cargarJugadores() {
		ArrayList<Jugador> jugadores = new ArrayList<Jugador>();
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(url + nameDatabase, user, pass);
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT * FROM Jugadores;");
			String nombre;
			String contrasena;
			while (resultSet.next()) {
				nombre = resultSet.getString("nombre").trim();
				contrasena = resultSet.getString("contrasena").trim();
				jugadores.add(new Jugador(nombre, contrasena));
			}
			resultSet.close();
			statement.close();
			connection.close();
		} catch (Exception exception) {
			System.out.println(exception);
		}

		return jugadores;
	}


	// carga una partida concreta
	public synchronized Partida cargarPartida(int idPartida) {
		Partida p = new Partida(idPartida, false, "en_juego");
		cargarJugadores(p);
		cargarBarcos(p);
		cargarJugadas(p);
		p.escribirJugadasEnTableros();
		return p;
	}
	
	// carga las partidas de un Jugador
	public synchronized ArrayList<Partida> cargarPartidas(String jugador) {
		ArrayList<Partida> partidas = new ArrayList<Partida>();
		String sql = "SELECT * FROM Partidas P JOIN JugadoresPartida JP ON P.ID_Partida=JP.partida WHERE JP.jugador=?";

		try {
			Connection connection = DriverManager.getConnection(url + nameDatabase, user, pass);
			PreparedStatement pstmt = connection.prepareStatement(sql);
			pstmt.setString(1, jugador);

			ResultSet resultSet = pstmt.executeQuery();
			while (resultSet.next()) {
				int idPartida = resultSet.getInt("ID_Partida");
				String ganador = resultSet.getString("ganador").trim();
				boolean terminada = resultSet.getBoolean("terminada");
				partidas.add(new Partida(idPartida, terminada, ganador));
			}

			resultSet.close();
			pstmt.close();
			connection.close();

		} catch (Exception exception) {
			System.out.println(exception);
		}
		cargarDatosPartidas(partidas);

		return partidas;
	}

	private void cargarDatosPartidas(ArrayList<Partida> partidas) {
		for (Partida partida : partidas) {
			cargarJugadores(partida);
			cargarBarcos(partida);
			cargarJugadas(partida);
		}
	}

	private synchronized void cargarJugadores(Partida partida) {
		String sql = "SELECT * FROM JugadoresPartida WHERE partida = ?";
		Connection conexion = null;
		try {
			conexion = DriverManager.getConnection(url + nameDatabase, user, pass);
			PreparedStatement consulta = conexion.prepareStatement(sql);
			consulta.setInt(1, partida.getId());
			ResultSet resultado = consulta.executeQuery();
			while (resultado.next()) {
				String jugador = resultado.getString("jugador");
				boolean turno = resultado.getBoolean("turno");
				if (resultado.isFirst()) {
					partida.setNombreJ1(jugador);
					partida.setTurnoJ1(turno);
				} else {
					partida.setNombreJ2(jugador);
					partida.setTurnoJ2(turno);
				}
			}
			resultado.close();
			consulta.close();
			conexion.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private synchronized void cargarBarcos(Partida partida) {

		Tablero tableroJ1 = new Tablero(false);
		Tablero tableroJ2 = new Tablero(false);

		try {
			String sqlBarcos = "SELECT * FROM Posicion_Barcos WHERE partida=?";
			Connection connection = DriverManager.getConnection(url + nameDatabase, user, pass);
			PreparedStatement psBarcos = connection.prepareStatement(sqlBarcos);
			psBarcos.setInt(1, partida.getId());
			ResultSet resultSetBarcos = psBarcos.executeQuery();
			while (resultSetBarcos.next()) {
				String jugador = resultSetBarcos.getString("jugador");
				int fila = resultSetBarcos.getInt("fila");
				int columna = resultSetBarcos.getInt("columna");
				if (partida.getNombreJ1().equals(jugador))
					tableroJ1.colocarCasilla(fila, columna, EstadoCasilla.BARCO);
				else
					tableroJ2.colocarCasilla(fila, columna, EstadoCasilla.BARCO);
			}
			resultSetBarcos.close();
			psBarcos.close();
			connection.close();
		} catch (Exception exception) {
			System.out.println(exception);
		}

		partida.setTableroJ1(tableroJ1);
		partida.setTableroJ2(tableroJ2);
	}

	private synchronized void cargarJugadas(Partida partida) {

		try {
			Connection connection = DriverManager.getConnection(url + nameDatabase, user, pass);
			String sqlJugadas = "SELECT * FROM Jugadas WHERE partida=?";
			PreparedStatement psJugadas = connection.prepareStatement(sqlJugadas);
			psJugadas.setInt(1, partida.getId());
			ResultSet resultSetJugadas = psJugadas.executeQuery();

			while (resultSetJugadas.next()) {
				String jugador = resultSetJugadas.getString("jugador");
				int nJugada = resultSetJugadas.getInt("n_jugada");
				int fila = resultSetJugadas.getInt("fila");
				int columna = resultSetJugadas.getInt("columna");
				EstadoCasilla resultado = EstadoCasilla.valueOf(resultSetJugadas.getString("resultado"));
				partida.agregarJugada(new Jugada(partida.getId(), jugador, nJugada, fila, columna, resultado));
			}			
			
			resultSetJugadas.close();
			psJugadas.close();
			connection.close();

		} catch (Exception exception) {
			System.out.println(exception);
		}
	}

	public synchronized Partida nuevaPartida(String jugador1, String jugador2) {
		Connection connection = null;
		String insertNuevaPartida = "INSERT INTO Partidas (ganador, terminada) VALUES (?, ?);";
		String selecNuevaPartida = "SELECT MAX(ID_Partida) AS max_id_partida FROM Partidas;";
		int idPartida = 0;
		try {
			// Conexion y sentencia SQL
			connection = DriverManager.getConnection(url + nameDatabase, user, pass);
			PreparedStatement pstmt = connection.prepareStatement(insertNuevaPartida);

			// Establecer los parámetros y ejecutar la inserción
			pstmt.setString(1, "en_juego");
			pstmt.setBoolean(2, false);
			pstmt.executeUpdate();
			pstmt.close();

			// Obtener id de la nueva partida
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(selecNuevaPartida);
			if (resultSet.next()) {
				idPartida = resultSet.getInt("max_id_partida");
			}
			resultSet.close();
			statement.close();

			connection.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("ERROR AL CREAR PARTIDA EN BBDD");
		}

		Partida nuevaPartida = new Partida(idPartida, jugador1, jugador2);
		registrarJugadoresPartida(nuevaPartida);
		registrarBarcos(nuevaPartida);
		return nuevaPartida;

	}
	
	private void registrarJugadoresPartida(Partida p) {
		int id = p.getId();
		String jugador1 = p.getNombreJ1();
		boolean turnoJ1 = p.isTurnoJ1();
		String jugador2 = p.getNombreJ2();
		boolean turnoJ2 = p.isTurnoJ2();
		String sql = "INSERT INTO JugadoresPartida (partida, jugador, turno) VALUES (?, ?, ?);";
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(url + nameDatabase, user, pass);
			
			PreparedStatement insert1 = connection.prepareStatement(sql);
			insert1.setInt(1, id);
			insert1.setString(2, jugador1);
			insert1.setBoolean(3, turnoJ1);
			insert1.executeUpdate();
			insert1.close();
			
			PreparedStatement insert2 = connection.prepareStatement(sql);
			insert2.setInt(1, id);
			insert2.setString(2, jugador2);
			insert2.setBoolean(3, turnoJ2);
			insert2.executeUpdate();
			insert2.close();
			
			connection.close();
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}

	private void registrarBarcos(Partida partida) {
		int idPartida = partida.getId();
		String jugador1 = partida.getNombreJ1();
		String jugador2 = partida.getNombreJ2();
		EstadoCasilla[][] tableroJ1 = partida.getTableroJ1().getCasillas();
		EstadoCasilla[][] tableroJ2 = partida.getTableroJ2().getCasillas();
		int tamTablero = Tablero.getTamañoTablero();

		for (int i = 0; i < tamTablero; i++) {
			for (int j = 0; j < tamTablero; j++) {
				if (tableroJ1[i][j] == EstadoCasilla.BARCO) {
					registrarCasillaBarco(idPartida, jugador1, i, j);
				}
				if (tableroJ2[i][j] == EstadoCasilla.BARCO) {
					registrarCasillaBarco(idPartida, jugador2, i, j);
				}
			}
		}
	}

	private void registrarCasillaBarco(int partida, String jugador, int fila, int columna) {
		String sql = "INSERT INTO Posicion_Barcos (partida, jugador, fila, columna) VALUES (?, ?, ?, ?);";
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(url + nameDatabase, user, pass);
			PreparedStatement pstmt = connection.prepareStatement(sql);
			pstmt.setInt(1, partida);
			pstmt.setString(2, jugador);
			pstmt.setInt(3, fila);
			pstmt.setInt(4, columna);
			pstmt.executeUpdate();
			pstmt.close();
			connection.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public synchronized void registrarJugada(Jugada j) {
		String sql = "INSERT INTO Jugadas (partida, jugador, n_jugada, fila, columna, resultado) VALUES (?, ?, ?, ?, ?, ?);";
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(url + nameDatabase, user, pass);
			PreparedStatement pstmt = connection.prepareStatement(sql);
			pstmt.setInt(1, j.getIdPartida());
			pstmt.setString(2, j.getJugador());
			pstmt.setInt(3, j.getnJugada());
			pstmt.setInt(4, j.getFila());
			pstmt.setInt(5, j.getColumna());
			pstmt.setString(6, j.getResultado().toString());
			pstmt.executeUpdate();
			pstmt.close();
			connection.close();
			cambioTurno(j.getIdPartida(), j.getJugador());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public synchronized void cambioTurno(int idPartida, String ultimoJugador) {
		String sql1 = "UPDATE JugadoresPartida SET turno = false WHERE partida = ? AND jugador = ?;";
		String sql2 = "UPDATE JugadoresPartida SET turno = true WHERE partida = ? AND jugador != ?;";
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(url + nameDatabase, user, pass);
			
			PreparedStatement udate1 = connection.prepareStatement(sql1);
			udate1.setInt(1, idPartida);
			udate1.setString(2, ultimoJugador);
			udate1.executeUpdate();
			udate1.close();
			
			PreparedStatement update2 = connection.prepareStatement(sql2);
			update2.setInt(1, idPartida);
			update2.setString(2, ultimoJugador);
			update2.executeUpdate();
			update2.close();
			
			connection.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("cascó cambio de turno");
		}		
	}

	public synchronized void finPartida(int partida, String ganador) {
		String sql = "UPDATE Partidas SET ganador = ?, terminada = TRUE WHERE ID_Partida = ?;";
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(url + nameDatabase, user, pass);
			PreparedStatement pstmt = connection.prepareStatement(sql);
			
			pstmt.setString(1, ganador);
			pstmt.setInt(2, partida);
			pstmt.executeUpdate();
			
			pstmt.close();
			connection.close();
			finTurno(partida, ganador);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error al modificar Partida "+partida);
		}
	}
	
	public synchronized void finTurno(int idPartida, String ultimoJugador) {
		String sql1 = "UPDATE JugadoresPartida SET turno = false WHERE partida = ? AND jugador = ?;";
		String sql2 = "UPDATE JugadoresPartida SET turno = false WHERE partida = ? AND jugador != ?;";
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(url + nameDatabase, user, pass);
			
			PreparedStatement udate1 = connection.prepareStatement(sql1);
			udate1.setInt(1, idPartida);
			udate1.setString(2, ultimoJugador);
			udate1.executeUpdate();
			udate1.close();
			
			PreparedStatement update2 = connection.prepareStatement(sql2);
			update2.setInt(1, idPartida);
			update2.setString(2, ultimoJugador);
			update2.executeUpdate();
			update2.close();
			
			connection.close();
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
}
