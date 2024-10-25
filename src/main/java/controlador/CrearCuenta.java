package controlador;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import modelo.DaoPersona;
import modelo.Persona;

import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

/**
 * Servlet implementation class CrearCuenta
 */
public class CrearCuenta extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public CrearCuenta() {
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
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub

		// Obtener parámetros de la solicitud
		String email = request.getParameter("email");
		String password = getMD5(request.getParameter("password"));
		String nombre = request.getParameter("nombre");

		 // Validar los parámetros
		if (email == null || password == null || nombre == null || email.isEmpty() || password.isEmpty()
				|| nombre.isEmpty()) {
			response.sendRedirect("error.html"); // Redirige a una página de error si los parámetros son inválidos
			return; // Termina la ejecución del método
		}

		try {
			// Intentar registrar al usuario
			if (DaoPersona.getInstance().registrarUsuario(email, password, nombre)) {
				response.sendRedirect("playlist.html"); // Redirige a la página de playlist si el registro es exitoso
			} else {
				response.sendRedirect("error.html"); // Redirige a una página de error si el registro falla
			}
		} catch (SQLException e) {
			e.printStackTrace(); // Imprime la traza de la excepción
			response.sendRedirect("error.html"); // Redirige a una página de error en caso de excepción
		}
	}

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
