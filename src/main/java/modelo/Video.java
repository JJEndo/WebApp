package modelo;

import java.sql.SQLException;

import com.google.gson.Gson;

/**
 * Clase Video.
 * 
 * Representa un video con un identificador, título, director, música, sinopsis y video.
 */
public class Video {
	
	/**
	 * El identificador único del video.
	 */
	private int id;
	
	/**
	 * El título del video.
	 */
	private String titulo;
	
	/**
	 * El director del video.
	 */
	private String director;
	
	/**
	 * La música del video.
	 */
	private String musica;
	
	/**
	 * La sinopsis del video.
	 */
	private String sinopsis;
	
	/**
	 * El nombre del video
	 */
	private String foto;

	/**
	 * Constructor para generar un objeto vacío de tipo Video.
	 */
	public Video() {

	}

	/**
	 * Constructor para la creación de un objeto Video con todos los atributos.
	 * 
	 * @param id El identificador único del video.
	 * @param titulo El título del video.
	 * @param director El director del video.
	 * @param musica La música del video.
	 * @param sinopsis La sinopsis del video.
	 * @param foto La foto asociada al video.
	 */
	public Video(int id, String titulo, String director, String musica, String sinopsis, String foto) {

		this.id = id;
		this.titulo = titulo;
		this.director = director;
		this.musica = musica;
		this.sinopsis = sinopsis;
		this.foto = foto;
	}

	/**
	 * Constructor para la creación de un objeto Video sin el atributo ID
	 * 
	 * @param titulo El título del video.
	 * @param director El director del video.
	 * @param musica La música del video.
	 * @param sinopsis La sinopsis del video.
	 * @param foto La foto asociada al video.
	 */
	public Video(String titulo, String director, String musica, String sinopsis, String foto) {
		super();
		this.titulo = titulo;
		this.director = director;
		this.musica = musica;
		this.sinopsis = sinopsis;
		this.foto = foto;
	}

	/**
	 * Constructor para la creación de un objeto Video sin los atributos ID y foto.
	 * 
	 * @param titulo El título del video.
	 * @param director El director del video.
	 * @param musica La música del video.
	 * @param sinopsis La sinopsis del video.
	 */
	public Video(String titulo, String director, String musica, String sinopsis) {
		super();
		this.titulo = titulo;
		this.director = director;
		this.musica = musica;
		this.sinopsis = sinopsis;
	}

	/**
	 * Constructor para la creación de un objeto Video con todos los atributos menos la foto.
	 * 
	 * @param id El identificador único del video.
	 * @param titulo El título del video.
	 * @param director El director del video.
	 * @param musica La música del video.
	 * @param sinopsis La sinopsis del video.
	 */
	public Video(int id, String titulo, String director, String musica, String sinopsis) {
		super();
		this.id = id;
		this.titulo = titulo;
		this.director = director;
		this.musica = musica;
		this.sinopsis = sinopsis;
	}

	/**
	 * Método para obtener el ID del video.
	 * 
	 * @return El ID del video.
	 */
	public int getId() {
		return id;
	}

	/**
	 * Establece el ID del video.
	 * 
	 * @param id El identificador único del video.
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Obtiene el título del video.
	 * 
	 * @return El título del video.
	 */
	public String getTitulo() {
		return titulo;
	}

	/**
	 * Establece el título del video.
	 * 
	 * @param titulo El título del video.
	 */
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	/**
	 * Obtiene el director del video.
	 * 
	 * @return El director del video.
	 */
	public String getDirector() {
		return director;
	}

	/**
	 * Establece el director del video.
	 * 
	 * @param director El director del video.
	 */
	public void setDirector(String director) {
		this.director = director;
	}

	/**
	 * Obtiene la música del video.
	 * 
	 * @return La música del video.
	 */
	public String getMusica() {
		return musica;
	}

	/**
	 * Establece la música del video.
	 * 
	 * @param musica La música del video.
	 */
	public void setMusica(String musica) {
		this.musica = musica;
	}

	/**
	 * Obtiene la sinopsis del video.
	 * 
	 * @return La sinopsis del video.
	 */
	public String getSinopsis() {
		return sinopsis;
	}

	/**
	 * Establece la sinopsis del video.
	 * 
	 * @param sinopsis La sinopsis del video.
	 */
	public void setSinopsis(String sinopsis) {
		this.sinopsis = sinopsis;
	}

	/**
	 * Obtiene el video.
	 * 
	 * @return El video.
	 */
	public String getFoto() {
		return foto;
	}

	/**
	 * Establece el video.
	 * 
	 * @param foto El video
	 */
	public void setFoto(String foto) {
		this.foto = foto;
	}

	/**
	 * Inserta el video en la base de datos.
	 * 
	 * Este método utiliza la instancia del DAO para insertar el video actual en la base de datos.
	 * 
	 * @throws SQLException Si ocurre un error al acceder a la base de datos.
	 */
	public void insertar() throws SQLException {
		/*
		 * DaoVideos dao = new DaoVideos(); dao.insertar(this);
		 */
		DaoVideos.getInstance().insertar(this);
	}

	/**
	 * Obtiene un video de la base de datos por su ID y actualiza los datos del objeto actual.
	 * 
	 * @param id El identificador del video a obtener.
	 * @throws SQLException Si ocurre un error al acceder a la base de datos.
	 */
	public void obtenerPorID(int id) throws SQLException {

		Video video = DaoVideos.getInstance().obtenerPorId(id);

		if (video != null) {
			this.setId(video.getId());
			this.setTitulo(video.getTitulo());
			this.setDirector(video.getDirector());
			this.setMusica(video.getMusica());
			this.setSinopsis(video.getSinopsis());
			this.setFoto(video.getFoto());
		}
	}

	/**
	 * Actualiza los datos del video en la base de datos.
	 * 
	 * @throws SQLException Si ocurre un error al acceder a la base de datos.
	 */
	public void update() throws SQLException {
		DaoVideos.getInstance().update(this);
	}

	/**
	 * Borra el video de la base de datos por su ID.
	 * 
	 * @param id El identificador del video a borrar.
	 * @throws SQLException Si ocurre un error al acceder a la base de datos.
	 */
	public void borrar(int id) throws SQLException {

		DaoVideos.getInstance().borrar(id);
	}

	/**
	 * Convierte la información del video a formato JSON.
	 * 
	 * @return Una cadena JSON que representa el video actual.
	 */
	public String dameJson() {
		Gson gson = new Gson();
		return gson.toJson(this);
	}

	/**
	 * Retorna una representación en forma de cadena del video.
	 */
	@Override
	public String toString() {
		return "Video [id=" + id + ", titulo=" + titulo + ", director=" + director + ", musica=" + musica
				+ ", sinopsis=" + sinopsis + ", foto=" + foto + "]";
	}

}
