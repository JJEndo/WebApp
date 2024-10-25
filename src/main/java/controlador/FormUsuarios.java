package controlador;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import modelo.DaoPersona;
import modelo.Persona;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

/**
 * Servlet implementation class FormUsuarios
 */
public class FormUsuarios extends HttpServlet {
	private static final long serialVersionUID = 1L;
	HttpSession sesion;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public FormUsuarios() {
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

		if (idSesionObj != null) {

			int idSesion = (int) idSesionObj;// Convertir el atributo "id" a un entero

			// Si el "id" de la sesión no es 0, lo que indica que el usuario está autenticado
			if (idSesion != 0) {
				// Configurar la respuesta para que sea de tipo JSON y con codificación UTF-8
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");

				// Obtener el escritor para enviar la respuesta
				PrintWriter out = response.getWriter();
				// Obtener el parámetro "op" de la solicitud, que indica la operación a realizar
				int opcion = Integer.parseInt(request.getParameter("op"));

				try {
					// Manejar las diferentes operaciones basadas en el valor del parámetro "op"
					switch (opcion) {
					case 1:
						// Opción 1: Listar todos los usuarios en formato JSON
						String respuestaJSON1 = DaoPersona.getInstance().listarJson();
						out.print(respuestaJSON1);// Enviar la respuesta JSON al cliente
						break;
					case 2:
						// Opción 2: Obtener un usuario por ID y devolverlo en formato JSON
						int id2 = Integer.parseInt(request.getParameter("id")); // Obtener el ID del usuario
						Persona p2 = DaoPersona.getInstance().obtenerPorID(id2); // Obtener el usuario por ID
						out.print(p2.dameJson()); // Enviar la respuesta JSON del usuario
						break;
					case 3:
						// Opción 3: Borrar un usuario por ID
						int id3 = Integer.parseInt(request.getParameter("id"));// Obtener el ID del usuario
						DaoPersona.getInstance().borrar(id3); // Borrar el usuario por ID
						out.print("{\"success\": true}"); // Enviar una respuesta de éxito en formato JSON
						break;
					case 4:
						// Opción 4: Listar usuarios por tipo en formato JSON
						int tipo = Integer.parseInt(request.getParameter("tipoUsuario")); // Obtener el tipo de usuario
						String respuestaJSON4 = DaoPersona.getInstance().listarJson(tipo); // Obtener la lista de
																							// usuarios por tipo en JSON
						out.print(respuestaJSON4); // Enviar la respuesta JSON al cliente
						break;
					default:
						// Manejar opciones inválidas
						response.setStatus(HttpServletResponse.SC_BAD_REQUEST); // Establecer el estado de respuesta
																				// como "Bad Request"
						out.print("{\"error\": \"Opción inválida\"}"); // Enviar un mensaje de error en formato JSON
						break;
					}
				} catch (SQLException e) {
					// Manejar excepciones SQL
					e.printStackTrace(); // Imprimir la traza de la excepción
					response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // Establecer el estado de
																						// respuesta como "Internal
																						// Server Error"
					out.print("{\"error\": \"Error interno del servidor\"}"); // Enviar un mensaje de error en formato
																				// JSON
				} finally {
					// Asegurar que el PrintWriter se cierre
					out.close();
				}
			} else {
				// Si el "id" de la sesión es 0, redirigir al usuario a la página de cuenta
				response.sendRedirect("cuenta.html");
			}
		} else {
			// Si el atributo "id" no existe en la sesión, redirigir al usuario a la página
			// de para que se cree una cuenta
			response.sendRedirect("crearCuenta.html");
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub

		// Obtener los parámetros de la solicitud
		String nombre = request.getParameter("nombre");
		String email = request.getParameter("email");
		int permiso = 0;
		int id = 0;

		// Intentar convertir el parámetro "permiso" a entero
		try {
			permiso = Integer.parseInt(request.getParameter("permiso"));
		} catch (NumberFormatException e) {
			// Manejo de error en caso de que el permiso no sea un entero válido
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Permiso inválido");
			return; // Terminar la ejecución del método si el permiso no es válido
		}

		// Intentar convertir el parámetro "id" a entero
		try {
			id = Integer.parseInt(request.getParameter("id"));
		} catch (NumberFormatException e) {
			// Manejo de error en caso de que el id no sea un entero válido
			id = 0; // Asignar 0 si el id no es válido para insertar como nuevo registro
		}

		// Crear un nuevo objeto Persona con los parámetros obtenidos
		Persona p = new Persona(nombre, email, permiso);

		// Intentar insertar o actualizar el usuario en la base de datos
		try {
			if (id == 0) {
				// Si el ID es 0, insertar un nuevo usuario
				p.insertar();
			} else {
				// Si el ID es diferente de 0, actualizar el usuario existente
				p.setId(id);
				p.actualizar();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace(); // Manejar excepciones SQL
			System.out.println("Error de conexión");
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error de conexión a la base de datos");
			return; // Terminar la ejecución del método en caso de excepción
		}

		System.out.println(p.toString());

		// Redirigir a la página "listarUsuarios.html" después de la operación
		response.sendRedirect("listarUsuarios.html");
	}
}
