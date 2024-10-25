package controlador;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import modelo.DaoVideos;
import modelo.Video;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;

/**
 * Servlet implementation class GestionVideos
 */
@MultipartConfig
public class GestionVideos extends HttpServlet {
	private static final long serialVersionUID = 1L;
	/**
	 * Este atributo de la clase es para indicar la ruta dónde están ubicados los
	 * archivos
	 */
	private String pathFiles = "C:\\Users\\jjend\\eclipse-workspace\\web_eq_proyecto1\\src\\main\\webapp\\videosSubidos";
	/**
	 * Este objeto es para que Java entienda que es una ruta que vamos a trabajar
	 * con archivos
	 */
	private File uploads = new File(pathFiles);

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public GestionVideos() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub

		// Intentar obtener la lista de videos en formato JSON
		try {
			String respuestaJSON = DaoVideos.getInstance().listarJson(); // Obtener la lista de videos en formato JSON
			response.setContentType("application/json"); // Establecer el tipo de contenido de la respuesta como JSON
			response.setCharacterEncoding("UTF-8"); // Establecer la codificación de caracteres de la respuesta
			
			// Obtener el escritor para enviar la respuesta
			PrintWriter out = response.getWriter();
			out.print(respuestaJSON); // Enviar la respuesta JSON al cliente
			out.flush(); // Asegurar que todos los datos se envíen
		} catch (SQLException e) {
			// Manejar excepciones SQL
			e.printStackTrace(); // Imprimir la traza de la excepción
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error al obtener los videos."); // Enviar un error 500 al cliente
		}

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub

		
		//Recuperaamos los parámetros de la solicitud HTTP que ha sido enviada por elcliente al servidor.
		 
		String titulo = request.getParameter("titulo");
		String director = request.getParameter("director");
		String musica = request.getParameter("musica");
		String sinopsis = request.getParameter("sinopsis");

		// Recuperar y leer los datos binarios (el archivo de video) desde el cliente
		Part part = request.getPart("video");
		if (part == null) {
			// Indicamos que algo está mal en la solicitud del cliente y que debe ser corregido.
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return; // Terminar la ejecución del método
		}

		// Gestionar la ruta y obtener el nombre del archivo original
		Path path = Paths.get(part.getSubmittedFileName());
		String fileName = path.getFileName().toString(); // Guardar el nombre del archivo en modo texto

		// Definir el directorio donde se subirán los videos
		String uploads = "/web_eq_proyecto1/src/webapp/videosSubidos";
		File file = new File(uploads, fileName); // Crear un nuevo archivo en el directorio especificado

		// Intentar copiar el archivo al servidor
		try (InputStream input = part.getInputStream()) {
			// Asegurar que el directorio exista
			File uploadDir = new File(uploads);
			if (!uploadDir.exists()) {
				uploadDir.mkdirs(); // Crear el directorio si no existe
			}

			 // Copiar el archivo a la ubicación de destino
			Files.copy(input, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			
			// Manejar excepciones de E/S
			e.printStackTrace();  // Imprimir la traza de la excepción
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // Enviar un error 500 al cliente
			PrintWriter error = response.getWriter();
			error.print("Se ha producido un error al subir el archivo. Contacte con el administrador."); // Mensaje de error para el usuario
			error.println("Detalles del error: " + e.getMessage());// Detalles del error
			return;// Terminar la ejecución del método
		}

		// Crear un nuevo objeto Video con los datos obtenidos
		Video a = new Video(titulo, director, musica, sinopsis, fileName);

		 // Intentar insertar los detalles del video en la base de datos
		try {
			a.insertar(); // Insertar el video en la base de datos
		} catch (SQLException e) {
			// Manejar excepciones SQL
			e.printStackTrace(); // Imprimir la traza de la excepción
			System.out.println("Error de conexión"); // Mensaje en la consola para depuración
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);  // Enviar un error 500 al cliente
			PrintWriter error = response.getWriter();
			error.print("Se ha producido un error en la base de datos. Contacte con el administrador."); // Mensaje de error para el usuario
			error.println("Detalles del error: " + e.getMessage()); // Detalles del error
			return;  // Terminar la ejecución del método
		}

		// Imprimir los detalles del video en la consola 
		System.out.println(a.toString());
	}

}
