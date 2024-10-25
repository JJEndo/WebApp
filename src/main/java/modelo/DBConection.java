package modelo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Clase DBConnection.
 * 
 * Esta clase proporciona una conexión a la base de datos utilizando el patrón Singleton.
 * Se asegura de que solo haya una instancia de la conexión a la base de datos a lo largo de la aplicación.
 * 
 */
public class DBConection {
	/**
	 * La URL de conexión JDBC
	 * 
	 * Esta constante define la URL de conexión para la base de datos MySQL.
	 */
	public static final String JDBC_URL = "jdbc:mysql://localhost:3306/eq";
	
	/**
	 * Instancia única de la conexión a la base de datos.
	 * 
	 * Esta variable mantiene la única instancia de la conexión a la base de datos.
	 */
	public static Connection instance = null;
	
	/**
	 * Constructor privado de DBConnection.
	 * 
	 */
	private DBConection() {
		
	}
	
	/**
	 * Obtiene la instancia de la conexión a la base de datos.
	 * 
	 * Este método proporciona acceso a la conexión única a la base de datos. Si la conexión no existe,
	 * la crea utilizando los parámetros de conexión especificados.
	 * 
	 * @return La instancia de la conexión a la base de datos.
	 * @throws SQLException Si ocurre un error al acceder a la base de datos.
	 */
	public static Connection getConnection() throws SQLException {
		
		// Verificar si la instancia de la conexión es nula
		if(instance == null) {
			// Crear un objeto Properties para almacenar las propiedades de la conexión
			Properties props = new Properties();
			// Establecer el nombre de usuario para la conexión
			props.put("user", "root");
			// Establecer la contraseña para la conexión
			props.put("password", "");
			// Establecer el conjunto de caracteres para la conexión
			props.put("charset", "UTF-8");

			// Crear la conexión a la base de datos utilizando DriverManager y las propiedades definidas
			instance = DriverManager.getConnection(JDBC_URL, props);	
		}
		// Devolver la instancia de la conexión
		return instance;
		
	}
		

}
