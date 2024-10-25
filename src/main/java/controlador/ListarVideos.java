package controlador;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import modelo.DaoVideos;
import modelo.Video;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Servlet implementation class ListarVideos
 */
public class ListarVideos extends HttpServlet {
	private static final long serialVersionUID = 1L;
	HttpSession sesion;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ListarVideos() {
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
		// Obtener la sesión
		HttpSession sesion = request.getSession();

		// Verificar si el atributo "id" existe en la sesión
		Object idSesionObj = sesion.getAttribute("id");
		
		// Si el atributo "id" existe en la sesión
		if (idSesionObj != null) {
			int idSesion = (int) idSesionObj; // Convertir el atributo "id" a un entero
			
			// Si el "id" de la sesión no es 0, lo que indica que el usuario está autenticado
			if (idSesion != 0) {
				// Configurar la respuesta para que sea de tipo JSON y con codificación UTF-8
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");

				PrintWriter out = response.getWriter();
				
				// Inicializar la variable opcion y obtener el parámetro "op" de la solicitud
				int opcion;
				try {
					opcion = Integer.parseInt(request.getParameter("op")); // Convertir el parámetro "op" a entero
				} catch (NumberFormatException e) {
					// Si el parámetro "op" no es un número válido, enviar un error 400 y un mensaje de error en JSON
					response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
					out.print("{\"error\": \"Opción inválida\"}");
					out.close();
					return; // Terminar la ejecución del método
				}

				// Intentar realizar la operación basada en el valor del parámetro "op"
				try {
					switch (opcion) {
					case 1: // Listar videos
						String respuestaJSON1 = DaoVideos.getInstance().listarJson(); // Obtener la lista de videos en formato JSON
						out.print(respuestaJSON1); // Enviar la respuesta JSON al cliente
						break;

					case 2: // Obtener video por ID
						int id2 = Integer.parseInt(request.getParameter("id")); // Obtener el ID del video de la solicitud
						Video video = new Video();
						video.obtenerPorID(id2); // Obtener el video por ID
						out.print(video.dameJson()); // Enviar la respuesta JSON del video
						break;

					case 3: // Borrar video por ID
						int id3 = Integer.parseInt(request.getParameter("id")); // Obtener el ID del video de la solicitud
						DaoVideos.getInstance().borrar(id3); // Borrar el video por ID
						out.print("{\"success\": \"Video eliminado\"}"); // Enviar una respuesta de éxito en formato JSON
						break;

					case 4: // Editar video
						//Obtiene los parametros del video de la solicitud
						int id4 = Integer.parseInt(request.getParameter("id")); 
						String titulo = request.getParameter("titulo");
						String director = request.getParameter("director");
						String musica = request.getParameter("musica");
						String sinopsis = request.getParameter("sinopsis");
						String foto = request.getParameter("foto");

						// Crear un nuevo objeto Video con los datos obtenidos
						Video videoEditado = new Video(id4, titulo, director, musica, sinopsis, foto);
						DaoVideos.getInstance().update(videoEditado); // Actualizar el video en la base de datos
						out.print("{\"success\": \"Video actualizado\"}"); // Enviar una respuesta de éxito en formato JSON
						break;

					default: // Manejar opciones inválidas
						response.setStatus(HttpServletResponse.SC_BAD_REQUEST); // Establecer el estado de respuesta como "Bad Request"
						out.print("{\"error\": \"Opción inválida\"}"); // Enviar un mensaje de error en formato JSON
						break;
					}
				} catch (SQLException e) {
					// Manejar excepciones SQL
					e.printStackTrace();  // Imprimir la traza de la excepción
					response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // Establecer el estado de respuesta como "Internal Server Error"
					out.print("{\"error\": \"Error interno del servidor\"}"); // Enviar un mensaje de error en formato JSON
				} finally {
					 // Asegurar que el PrintWriter se cierre
					out.close();
				}
			} else {
				// Si el "id" de la sesión es 0, redirigir al usuario a la página de cuenta
				response.sendRedirect("cuenta.html");
			}
		} else {
			// Si el atributo "id" no existe en la sesión, redirigir al usuario a la página de cuenta
			response.sendRedirect("cuenta.html");
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
