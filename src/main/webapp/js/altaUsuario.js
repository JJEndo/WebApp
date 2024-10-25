// Cuando la ventana se carga, obtenemos parámetros de la URL y llamamos a una función para procesarlos
window.onload = function() {
window.onload = function() {
    const id = getParameterByName("id"); // Obtenemos el parámetro "id" de la URL
    const op = getParameterByName("op"); // Obtenemos el parámetro "op" de la URL
    console.log(`Parámetros obtenidos de la URL: id=${id}, op=${op}`);
    if (id && op) {
        llamada(id, op);
    } else {
        console.log('ID y/o OP no encontrados en la URL'); // Llamamos a la función "llamada" si existen ambos parámetros
    }
};

// Función para hacer una petición al servidor y obtener datos del usuario
function llamada(id, op) {
    console.log(`Llamada a fetch con id=${id} y op=${op}`);
    fetch(`/FormUsuarios?id=${id}&op=${op}`) // Hacemos una petición GET al servidor con los parámetros id y op
        .then(response => {
            console.log('Respuesta recibida', response);
            if (!response.ok) {
                throw new Error(`Network response was not ok: ${response.status} ${response.statusText}`);
            }
            return response.json(); // Parseamos la respuesta JSON
        })
        .then(data => {
            console.log('Datos recibidos del servidor', data);
            pintarFormulario(data); // Llamamos a la función "pintarFormulario" con los datos recibidos
        })
        .catch(error => console.error('Error fetching data', error)); // Manejamos errores de la petición
}

// Función para obtener parámetros de la URL por nombre
function getParameterByName(name) {
    name = name.replace(/[\[\]]/g, "\\$&");
    const regex = new RegExp("[?&]" + name + "(=([^&#]*)|&|#|$)"), // Creamos una expresión regular para buscar el parámetro
          results = regex.exec(location.search); // Ejecutamos la expresión regular en la URL
    return results ? decodeURIComponent(results[2].replace(/\+/g, " ")) : null; // Devolvemos el valor del parámetro o null
}


// Función para llenar un formulario con datos del usuario
function pintarFormulario(datos) {
    if (datos) {
        document.getElementById("id").value = datos.id || ''; // Llenamos el campo "id" con los datos recibidos
        document.getElementById("nombre").value = datos.nombre || ''; // Llenamos el campo "nombre"
        document.getElementById("email").value = datos.email || ''; // Llenamos el campo "email"
        document.getElementById("permiso").value = datos.permiso || ''; // Llenamos el campo "permiso"
    } else {
        console.error('Datos no encontrados o inválidos'); // Error si no hay datos
    }
    }
}
