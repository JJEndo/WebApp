package modelo;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.google.gson.Gson;

/**
 * Clase DaoPersona.
 * 
 * Esta clase implementa el patrón Singleton para gestionar el acceso a la base de datos
 * y proporciona métodos para realizar operaciones CRUD en la tabla `persona`.
 */
public class DaoPersona {

	/**
	 * Conexión a la base de datos.
	 * 
	 * Esta es una conexión compartida utilizada por todos los métodos de la clase para
	 * interactuar con la base de datos.
	 * 
	 */
	public static Connection con = null;
	
	/**
	 * Instancia única de DaoPersona.
	 * 
	 * Esta es la instancia única que se utiliza para implementar el patrón Singleton.
	 */
	private static DaoPersona instance = null;

	/**
	 * Constructor de DaoPersona.
	 * 
	 * Este constructor inicializa la conexión a la base de datos utilizando la clase DBConnection.
	 * 
	 * @throws SQLException Si ocurre un error al obtener la conexión a la base de datos.
	 */
	public DaoPersona() throws SQLException {

		this.con = DBConection.getConnection();

	}
	
	/**
	 *  Obtiene la instancia única de DaoPersona.
	 *  
	 *  Este método sigue el patrón Singleton para garantizar que solo exista una instancia de DaoPersona.
	 *  Está sincronizado para ser seguro en un entorno multi-hilo.
	 *  
	 * @return La instancia única de DaoPersona.
	 * @throws SQLException Si ocurre un error al acceder a la base de datos durante la creación de la instancia.
	 */
	public static synchronized DaoPersona getInstance() throws SQLException {

		if (instance == null) {
			instance = new DaoPersona();
		}

		return instance;
	}

	/**
	 * Método de inserción en la base de datos del objeto Persona.
	 * 
	 * Este método inserta una nueva fila en la tabla `persona` con los valores del objeto `Persona` proporcionado.
	 * 
	 * @param p El objeto Persona que contiene los datos a insertar en la base de datos.
	 * @throws SQLException Si ocurre un error al acceder a la base de datos.
	 */
	public void insertar(Persona p) throws SQLException {

		PreparedStatement ps = null;  // Declaro el PreparedStatement
		try {
			// Preparo la consulta SQL para insertar un nuevo registro en la tabla 'persona'
			ps = con.prepareStatement("INSERT INTO persona (nombre,email,permiso) VALUES (?,?,?)");
			// Establece los parámetros de la consulta SQL con los valores del objeto Persona
			ps.setString(1, p.getNombre()); // Establece los valores
			ps.setString(2, p.getEmail());
			ps.setInt(3, p.getPermiso());

			int filas = ps.executeUpdate(); // Ejecuta la consulta de inserción
		} finally {
			if (ps != null) // Si el PreparedStatement no es nulo
				ps.close(); // Cierra el PreparedStatement para liberar recursos
		}
	}


	/**
	 * Obtiene una instancia de Persona desde la base de datos usando su identificador único.
	 * 
	 * @param id El identificador único de la persona.
	 * @return Un objeto Persona si se encuentra una fila con el ID proporcionado, o null si no se encuentra.
	 * @throws SQLException Si ocurre un error al acceder a la base de datos.
	 */
	public Persona obtenerPorID(int id) throws SQLException {
		// Consulta SQL para seleccionar una persona por su ID
		String sql = "SELECT * FROM persona WHERE id=?";
		// Prepara el PreparedStatement y asegura su cierre automático
		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setInt(1, id); // Establece el valor del ID en la consulta SQL
			// Ejecuta la consulta y asegura el cierre automático del ResultSet
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {  // Verifica si hay un resultado
					// Crea y devuelve un nuevo objeto Persona con los datos del ResultSet
					return new Persona(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getInt(4));
				} else {
					return null; // Devuelve null si no se encontró ninguna persona con el ID proporcionado
				}
			}
		}
	}

	/**
	 * Autentica a un usuario con el correo electrónico y la contraseña proporcionados.
	 * 
	 * Este método verifica si existe un usuario en la base de datos con el correo electrónico y la contraseña proporcionados.
	 * Si se encuentra un usuario, retorna un objeto `Persona` con la información del usuario. De lo contrario, retorna null.
	 * 
	 * @param p El objeto Persona que contiene el correo electrónico a verificar.
	 * @param pass La contraseña del usuario a verificar.
	 * @return Un objeto `Persona` si se encuentra una coincidencia en la base de datos, o null si no se encuentra ninguna coincidencia.
	 * @throws SQLException Si ocurre un error al acceder a la base de datos.
	 */
	public Persona logueando(Persona p, String pass) throws SQLException {
		
		// Consulta SQL para autenticar al usuario por su email y contraseña
		String sql = "SELECT * FROM persona WHERE email=? AND pass=?";
		PreparedStatement ps = null; // Declara el PreparedStatement
		ResultSet rs = null; // Declara el ResultSet
		Persona aux = null; // Declara la variable auxiliar para el objeto Persona
		try {
			ps = con.prepareStatement(sql); // Prepara la consulta SQL
			ps.setString(1, p.getEmail()); // Establece el valor del email en la consulta SQL
			ps.setString(2, pass); // Establece el valor de la contraseña en la consulta SQL

			System.out.println("Executing query: " + ps.toString()); // Imprime la consulta SQL para depuración

			rs = ps.executeQuery(); // Ejecuta la consulta y obtiene el ResultSet

			if (rs.next()) { // Verifica si hay un resultado
				aux = new Persona(); // Crea un nuevo objeto Persona
				aux.setId(rs.getInt("id")); // Establece el ID de la persona
				aux.setNombre(rs.getString("nombre")); // Establece el nombre de la persona
				aux.setEmail(rs.getString("email")); // Establece el email de la persona
				aux.setPermiso(rs.getInt("permiso")); // Establece el permiso de la persona
				System.out.println("User found: " + aux.getEmail()); // Imprime que se encontró un usuario
			} else {
				System.out.println("No user found with email: " + p.getEmail()); // Imprime que no se encontró un usuario
			}
		} catch (SQLException e) {
			e.printStackTrace(); // Imprime la traza de la excepción
			throw e; // Re-lanzamos la excepción para pdoer visualizarla.
		} finally {
			if (rs != null) { // Si el ResultSet no es nulo
				try {
					rs.close(); // Cierra el ResultSet para liberar recursos
				} catch (SQLException e) {
					e.printStackTrace(); // Imprime la traza de la excepción si ocurre al cerrar
				}
			}
			if (ps != null) { // Si el PreparedStatement no es nulo
				try {
					ps.close(); // Cierra el PreparedStatement para liberar recursos
				} catch (SQLException e) {
					e.printStackTrace(); // Imprime la traza de la excepción si ocurre al cerrar
				}
			}
		}

		return aux;  // Retorna el objeto Persona encontrado o null si no se encontró ninguna coincidencia
	}

	/**
	 * Registra un nuevo usuario en la base de datos.
	 * 
	 * Este método inserta una nueva fila en la tabla `persona` con el email, la contraseña y el nombre proporcionados.
	 * 
	 * @param email El correo electrónico del usuario que se va a registrar.
	 * @param password La contraseña del usuario que se va a registrar.
	 * @param nombre El nombre del usuario que se va a registrar.
	 * @return boolean Verdadero si el registro fue exitoso, falso en caso contrario
	 * @throws SQLException Si ocurre un error al acceder a la base de datos.
	 */
	public boolean registrarUsuario(String email, String password, String nombre) throws SQLException {
		PreparedStatement ps = null;
		boolean registrado = false;

		try {
			//// Consulta SQL para insertar un nuevo usuario en la tabla 'persona'
			String sql = "INSERT INTO persona (email, pass, nombre) VALUES (?, ?, ?)";
			ps = con.prepareStatement(sql); // Prepara la consulta SQL
			ps.setString(1, email); // Establece los parametros
			ps.setString(2, password); 
			ps.setString(3, nombre); 

			int rows = ps.executeUpdate(); // Ejecuta la consulta de inserción
			if (rows > 0) { // Verifica si la inserción fue exitosa
				registrado = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();  // Imprime la traza de la excepción
			throw e;
		} finally {
			if (ps != null) {
				try {
					ps.close(); // Cierra el PreparedStatement para liberar recursos
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

		return registrado; // Devuelve el resultado del registro
	}

	/**
	 * Actualiza los datos de una persona existente en la base de datos.
	 * 
	 * 
	 * Este método toma un objeto `Persona` que contiene los nuevos datos de una persona
	 * y actualiza la fila correspondiente en la base de datos utilizando el ID de la persona.
	 * 
	 * @param p El objeto Persona que contiene los datos actualizados.
	 * @return El objeto Persona con los datos actualizados.
	 * @throws SQLException Si ocurre un error al acceder a la base de datos.
	 */
	public Persona actualizar(Persona p) throws SQLException {

		PreparedStatement ps = null; // Declara el PreparedStatement
		try {
			// Prepara la consulta SQL para actualizar un registro en la tabla 'persona'
			ps = con.prepareStatement("UPDATE persona SET nombre=?,email=?,permiso=? WHERE id=?");
			
			// Establece los parámetros de la consulta SQL con los valores del objeto Persona
			ps.setString(1, p.getNombre());
			ps.setString(2, p.getEmail());
			ps.setInt(3, p.getPermiso());
			ps.setInt(4, p.getId());

			int filas = ps.executeUpdate(); // Ejecuta la consulta de actualización
		} finally {
			if (ps != null) // Si el PreparedStatement no es nulo
				ps.close();  // Cierra el PreparedStatement para liberar recursos
		}
		return p; // Retorna el objeto Persona actualizado

	}

	/**
	 * Borra una persona de la base de datos por su ID.
	 * 
	 * Este método elimina la fila de la base de datos que corresponde al ID proporcionado.
	 * 
	 * @param id El identificador de la persona a borrar.
	 * @throws SQLException Si ocurre un error al acceder a la base de datos.
	 */
	public void borrar(int id) throws SQLException {

		// Consulta SQL para eliminar un registro de la tabla 'persona' por su ID
		String sql = "DELETE FROM persona WHERE id=?";
		 // Prepara el PreparedStatement para ejecutar la consulta SQL
		PreparedStatement ps = con.prepareStatement(sql);
		// Establece el valor del ID en la consulta SQL
		ps.setInt(1, id);
		// Ejecuta la consulta de eliminación
		int filas = ps.executeUpdate();
		 // Cierra el PreparedStatement para liberar recursos
		ps.close();

	}

	/**
	 * Lista todas las personas de la base de datos.
	 * 
	 * Este método recupera todas las filas de la tabla `persona` de la base de datos y
	 * las convierte en una lista de objetos `Persona`.
	 * 
	 * @return Una lista de objetos Persona que contiene todas las filas de la tabla `persona`.
	 * @throws SQLException Si ocurre un error al acceder a la base de datos.
	 */
	public ArrayList<Persona> listar() throws SQLException {
		
		 // Consulta SQL para seleccionar todos los registros de la tabla 'persona'
		String sql = "SELECT * FROM persona";
	    // Inicializa una lista vacía para almacenar los resultados
		ArrayList<Persona> result = new ArrayList<>();
		// Prepara el PreparedStatement y ejecuta la consulta SQL, asegurando el cierre automático de recursos
		try (PreparedStatement ps = con.prepareStatement(sql); 
				ResultSet rs = ps.executeQuery()) {
			// Itera sobre cada fila del ResultSet
			while (rs.next()) {
				// Crea un nuevo objeto Persona con los datos de la fila actual y lo añade a la lista de resultados
				result.add(new Persona(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getInt(4)));
			}
		}
		// Devuelve la lista de objetos Persona
		return result;
	}
	

	/**
	 * Metodo listar que retorna los usuarios con el filtrado de tipo.
	 * 
	 * Este método obtiene una lista de objetos `Persona` desde la base de datos cuyo campo `permiso`
	 * coincide con el tipo proporcionado.
	 * 
	 * @param tipo El tipo de permiso que se usará para filtrar los registros
	 * @return ArrayList Persona Una lista de objetos Persona que cumplen con el filtro.
	 * @throws SQLException Si ocurre un error al acceder a la base de datos.
	 */
	public ArrayList<Persona> listar(int tipo) throws SQLException {
		
		// Consulta SQL para seleccionar registros de la tabla 'persona' con un permiso específico
		String sql = "SELECT * FROM persona WHERE permiso = ?";
		// Inicializa una lista vacía para almacenar los resultados
		ArrayList<Persona> result = new ArrayList<>();
		
		// Prepara el PreparedStatement y asegura su cierre automático
		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setInt(1, tipo);  // Establece el valor del permiso en la consulta SQL
			
			// Ejecuta la consulta y asegura el cierre automático del ResultSet
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) { // Itera sobre cada fila del ResultSet
					// Crea un nuevo objeto Persona con los datos de la fila actual y lo añade a la lista de resultados
					result.add(new Persona(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getInt(4)));
				}
			}
		}
	    // Devuelve la lista de objetos Persona
		return result;
	}


	/**
	 * Retorna una lista de usuarios en formato JSON.
	 * 
	 * Este método obtiene una lista de todos los usuarios en la base de datos y la convierte en una cadena JSON.
	 * 
	 * @return Una cadena JSON que representa la lista de todos los usuarios.
	 * @throws SQLException Si ocurre un error al acceder a la base de datos.
	 */
	public String listarJson() throws SQLException {
		Gson gson = new Gson(); // Crea una instancia de Gson
		return gson.toJson(this.listar()); // Convierte la lista de personas a JSON y la devuelve
	}

	/**
	 * Retorna una lista de usuarios filtrados por tipo en formato JSON.
	 * 
	 * Este método obtiene una lista de usuarios de la base de datos cuyo campo `permiso` coincide con el tipo proporcionado
	 * y la convierte en una cadena JSON.
	 * 
	 * @param tipo El tipo de permiso para filtrar los usuarios
	 * @return Una cadena JSON que representa la lista de usuarios filtrados por tipo.
	 * @throws SQLException Si ocurre un error al acceder a la base de datos.
	 */
	public String listarJson(int tipo) throws SQLException {

		Gson gson = new Gson(); // Crea una instancia de Gson
		ArrayList<Persona> personas = this.listar(tipo); // Obtiene la lista de personas con el permiso específico
		return gson.toJson(personas); // Convierte la lista de personas a JSON y la devuelve
	}

	/**
	 * Calcula el hash MD5 de una cadena de texto.
	 * 
	 * Este método toma una cadena de texto como entrada y devuelve su hash MD5 correspondiente.
	 * 
	 * @param input La cadena de texto a la que se le calculará el hash MD5.
	 * @return Una cadena que representa el hash MD5 de la entrada.
	 */
	public static String getMD5(String input) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] messageDigest = md.digest(input.getBytes());
			BigInteger number = new BigInteger(1, messageDigest);
			String hashtext = number.toString(16);

			while (hashtext.length() < 32) {
				hashtext = "0" + hashtext;
			}
			return hashtext;
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}

}
