//Selecciona el elemento del formulario que tiene la clase my-form.
let signupForm = document.querySelector(".my-form");
//Selecciona el elemento input para el nombre del usuario.
let nombre = document.getElementById("nombre");
//Selecciona el elemento input para el correo electrónico del usuario.
let email = document.getElementById("email");
// Selecciona el elemento input para la contraseña.
let password = document.getElementById("password");
//Selecciona el elemento input para confirmar la contraseña.

let confirmPassword = document.getElementById("confirm-password");

signupForm.addEventListener("submit", (e) => {
  //Evita que el formulario se envíe de la manera tradicional 
  e.preventDefault();
  //Imprime en la consola el valor del campo nombre.
  console.log("Nombre;", nombre.value);
  //Imprime en la consola el valor del campo email.
  console.log("Email:", email.value);
  // Imprime en la consola el valor del campo contraseña.
  console.log("Password:", password.value);
});

//Verifica si el valor en el campo de confirmación de contraseña es igual al valor en el campo de contraseña.
function onChange() {
	/*Si las contraseñas coinciden (confirmPassword.value === password.value), 
	 se llama a confirmPassword.setCustomValidity(""); para indicar que no hay errores de validación en ese campo.*/
  if (confirmPassword.value === password.value) {
    confirmPassword.setCustomValidity("");
  } else {
	  /**
	   * Si las contraseñas no coinciden, se muestra un mensaje de error de validación si el usuario intenta enviar el formulario.
	   */
    confirmPassword.setCustomValidity("Las contraseñas no coinciden");
  }
}

password.addEventListener("change", onChange);
confirmPassword.addEventListener("change", onChange);


