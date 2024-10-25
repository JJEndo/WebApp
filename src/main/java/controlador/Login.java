package controlador;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import modelo.Persona;

import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

/**
 * Servlet implementation class Login
 */
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
	HttpSession sesion;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Login() {
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

		// Obtener los parámetros "email" y "pass" de la solicitud
		String mail = request.getParameter("email");
		String pass = getMD5(request.getParameter("pass")); // Encriptar la contraseña usando el método getMD5

		// Verificar si el email o la contraseña son nulos o están vacíos
		if (mail == null || pass == null || mail.isEmpty() || pass.isEmpty()) {
			response.sendRedirect("error.html"); // Redirige a una página de error si los parámetros son inválidos
			return; // Terminar la ejecución del método
		}

		// Crear un nuevo objeto Persona y establecer su email
		Persona p = new Persona();
		p.setEmail(mail);

		// Intentar realizar el inicio de sesión
		try {
			if (p.logueo(pass)) { // Verificar si el inicio de sesión es exitoso
				// Si el inicio de sesión es exitoso, obtener la sesión actual del usuario
				HttpSession sesion = request.getSession();
				sesion.setAttribute("id", p.getId()); // Establecer el atributo "id" en la sesión
				sesion.setAttribute("permiso", p.getPermiso()); // Establecer el atributo "permiso" en la sesión
				response.sendRedirect("admin.html"); // Redirigir a la página de administración
			} else {
				// Si el inicio de sesión falla, redirigir a la página de creación de cuent
				response.sendRedirect("crearCuenta.html");
			}
		} catch (SQLException | IOException e) {
			// Manejar excepciones SQL e IO
			e.printStackTrace();
			response.sendRedirect("error.html"); // Redirige a una página de error en caso de excepción
		}
	}

	//Algoritmo MD5 para calcular el hash de la contraseña
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
