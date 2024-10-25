let loginForm = document.querySelector(".myForm"); //Buscamos el elemento que tenga la clase CSS"myForm" y lo asignamos a la variable "loginForm"

loginForm.addEventListener("submit", (e) => {
  e.preventDefault();
  let email = document.getElementById("email");
  let password = document.getElementById("password");

  console.log("Email:", email.value);
  console.log("Password:", password.value);
  // process and send to API
});


