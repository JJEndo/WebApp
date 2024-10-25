package modelo;

import java.sql.SQLException;

import com.google.gson.Gson;

/**
 * Clase Persona.
 * 
 * Representa a una persona con un identificador, nombre, correo electrónico y
 * nivel de permiso.
 * 
 */
public class Persona {

	/**
	 * El identificador único de la persona.
	 */
	private int id;

	/**
	 * El nombre de la persona.
	 */
	private String nombre;

	/**
	 * El correo electrónico de la persona.
	 */
	private String email;

	/**
	 * El nivel de permiso de la persona.
	 */
	private int permiso;

	/**
	 * Constructor para generar un objeto vacio de tipo persona
	 */
	public Persona() {

	}

	/**
	 * Constructor para la creación del objeto desde el formulario.
	 * 
	 * @param nombre  El nombre de la persona.
	 * @param email   El correo electrónico de la persona.
	 * @param permiso El nivel de permiso de la persona.
	 */
	public Persona(String nombre, String email, int permiso) {
		super();
		this.nombre = nombre;
		this.email = email;
		this.permiso = permiso;
	}

	/**
	 * Constructor completo para la creación del objeto con todos los atributos.
	 * 
	 * @param id      El identificador único de la persona.
	 * @param nombre  El nombre de la persona.
	 * @param email   El correo electrónico de la persona.
	 * @param permiso El nivel de permiso de la persona.
	 */
	public Persona(int id, String nombre, String email, int permiso) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.email = email;
		this.permiso = permiso;
	}

	/**
	 * Método de inclusión del id en el objeto.
	 * 
	 * @return El id de la persona en tipo entero.
	 */
	public int getId() {
		return id;
	}

	/**
	 * Método para establecer el ID de la persona.
	 * 
	 * @param id El ID de la persona.
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Método para obtener el nombre de la persona.
	 * 
	 * @return El nombre de la persona.
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * Método para establecer el nombre de la persona.
	 * 
	 * @param nombre El nombre de la persona.
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	/**
	 * Método para obtener el correo electrónico de la persona.
	 * 
	 * @return El correo electrónico de la persona.
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Método para establecer el correo electrónico de la persona.
	 * 
	 * @param email El correo electrónico de la persona.
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * Método para obtener el nivel de permiso de la persona.
	 * 
	 * @return El nivel de permiso de la persona.
	 */
	public int getPermiso() {
		return permiso;
	}

	/**
	 * Método para establecer el nivel de permiso de la persona.
	 * 
	 * @param permiso El nivel de permiso de la persona.
	 */
	public void setPermiso(int permiso) {
		this.permiso = permiso;
	}

	/**
	 * Inserta la persona en la base de datos.
	 * 
	 * Este método utiliza la instancia del DAO para insertar a la persona actual en la base de datos.
	 * 
	 * @throws SQLException Si ocurre un error al acceder a la base de datos.
	 */
	public void insertar() throws SQLException {

		DaoPersona.getInstance().insertar(this);
	}

	/**
	 * Obtiene una persona de la base de datos por su ID y actualiza los datos del
	 * objeto actual.
	 * 
	 * @param id El identificador de la persona a obtener.
	 * @throws SQLException Si ocurre un error al acceder a la base de datos.
	 */
	public void obtenerPorID(int id) throws SQLException {

		Persona persona = DaoPersona.getInstance().obtenerPorID(id);

		if (persona != null) {
			this.setId(persona.getId());
			this.setNombre(persona.getNombre());
			this.setEmail(persona.getEmail());
			this.setPermiso(persona.getPermiso());
		}
	}

	/**
	 * Autentica a una persona utilizando la contraseña proporcionada.
	 * 
	 * Este método verifica si la contraseña proporcionada coincide con la
	 * registrada en la base de datos. Si la autenticación es correcta, los datos de
	 * la persona se actualizan con la información obtenida de la base de datos.
	 * 
	 * @param pass La contraseña de la persona.
	 * @return true si la autenticación es exitosa, false en caso contrario.
	 * @throws SQLException Si ocurre un error al acceder a la base de datos.
	 */
	public boolean logueo(String pass) throws SQLException {
		boolean ok = false;
		Persona persona = DaoPersona.getInstance().logueando(this, pass);

		if (persona != null) {
			ok = true;
			this.setId(persona.getId());
			this.setNombre(persona.getNombre());
			this.setEmail(persona.getEmail());
			this.setPermiso(persona.getPermiso());
		}

		return ok;
	}

	/**
	 * Convierte la información de la persona a formato JSON.
	 * 
	 * @return Una cadena JSON que representa la persona actual.
	 */
	public String dameJson() {
		Gson gson = new Gson();
		return gson.toJson(this);
	}

	/**
	 * Actualiza los datos de la persona en la base de datos.
	 * 
	 * @throws SQLException Si ocurre un error al acceder a la base de datos.
	 */
	public void actualizar() throws SQLException {

		DaoPersona.getInstance().actualizar(this);

	}

	/**
	 * Borra la persona de la base de datos por su ID.
	 * 
	 * @param id El identificador de la persona a borrar.
	 * @throws SQLException Si ocurre un error al acceder a la base de datos.
	 */
	public void borrar(int id) throws SQLException {

		DaoPersona.getInstance().borrar(id);

	}


	/**
	 * Retorna una representación en forma de cadena de la persona.
	 */
	@Override
	public String toString() {
		return "Persona [id=" + id + ", nombre=" + nombre + ", email=" + email + ", permiso=" + permiso + "]";
	}

}
