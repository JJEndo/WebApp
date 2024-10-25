// Cuando la ventana se carga completamente, se llama a la función 'llamada'
window.onload = function() {
    console.log('Window loaded'); // Muestra un mensaje en la consola indicando que la ventana ha cargado
    llamada();  // Llama a la función 'llamada' para obtener datos
};

// Cuando el DOM se ha cargado y parseado, se llama a la función 'llamada'
document.addEventListener("DOMContentLoaded", function() {
    console.log('DOM fully loaded and parsed');  // Muestra un mensaje en la consola indicando que el DOM ha cargado completamente
    llamada(); // Llama a la función 'llamada' para obtener datos
});

// Función para hacer una petición al servidor y obtener datos de usuarios
function llamada() {
    console.log('Iniciando fetch para FormUsuarios'); // Muestra un mensaje en la consola indicando el inicio de la petición
    fetch('/web_eq_proyecto1/FormUsuarios?op=1') // Hacemos una petición GET al servidor con el parámetro op=1
        .then(response => {
            console.log('Respuesta recibida', response); // Muestra la respuesta recibida del servidor en la consola
            if (!response.ok) {
                throw new Error(`Network response was not ok: ${response.status} ${response.statusText}`); // Muestra un error si la respuesta no es OK
            }
            return response.text();  // Cambiar a .text() temporalmente para ver el contenido de la respuesta
        })
        .then(text => {
            try {
                const data = JSON.parse(text);  // Intentar convertir el texto a JSON
                console.log('Datos recibidos', data); // Muestra los datos recibidos en la consola
                if (Array.isArray(data)) {
                    pintarTabla(data); // Llama a la función 'pintarTabla' si los datos son un array
                } else {               
                    console.error('Datos recibidos no son un array:', data);
                }
            } catch (e) {
                console.error('Error parsing JSON:', e, text);  // Mostrar el texto que causó el error
            }
        })
        .catch(error => console.error('Error fetching data:', error)); // Muestra un error si los datos no son un array
}

// Función para borrar un usuario por su id
function borrar(id) {
    if (confirm("¿Seguro que quieres borrar?")) { // Pregunta al usuario si está seguro de borrar
        fetch(`/web_eq_proyecto1/FormUsuarios?id=${id}&op=3`)  // Hacemos una petición GET al servidor con el parámetro id y op=3
            .then(response => {
                console.log('Respuesta recibida', response);  // Muestra la respuesta recibida del servidor en la consola
                if (!response.ok) {
                    throw new Error(`Network response was not ok: ${response.status} ${response.statusText}`);// Muestra un error si la respuesta no es OK
                }
                return response.json(); // Parseamos la respuesta JSON
            })
            .then(data => {
                console.log('Datos después de borrar:', data);  // Muestra los datos después de borrar en la consola
                if (data.success) {
                    llamada(); // Vuelve a llamar para obtener la lista actualizada
                } else {
                    console.error('Error en la respuesta de borrado:', data); // Muestra un error si hay un problema en la respuesta de borrado
                }
            })
            .catch(error => console.error('Error:', error)); // Manejamos errores de la petición
    }
}

// Función para pintar una tabla con datos de usuarios
function pintarTabla(datos) {
    const listadoElement = document.getElementById("listado"); // Obtenemos el elemento con id "listado"
    if (!listadoElement) {
        console.error('Elemento con id "listado" no encontrado'); // Muestra un error si el elemento no existe
        return;
    }

    console.log('Pintando la tabla con datos', datos); // Muestra los datos que se van a pintar en la consola
    const filasHtml = datos.map(({ id, nombre, email, permiso }) => `
        <tr>
            <td>${id}</td>
            <td>${nombre}</td>
            <td>${email}</td>
            <td>${permiso}</td>
            <td>
                <a href="altaUsuario.html?id=${id}&op=2" class="btn-editar">Editar</a>
                <a href="javascript:borrar(${id})" class="btn-borrar">Borrar</a>
            </td>
        </tr>
    `).join(''); // Mapea los datos a filas HTML y los une en una cadena

    const tablaHtml = `<table class='listaTabla'><thead>
        <tr>
            <th>ID</th>
            <th>Nombre</th>
            <th>Email</th>
            <th>Permiso</th>
            <th>Acciones</th>
        </tr>
    </thead><tbody>${filasHtml}</tbody></table>`; // Creamos el HTML de la tabla
    listadoElement.innerHTML = tablaHtml; // Insertamos el HTML de la tabla en el elemento listado
}

// Función para buscar usuarios por tipo
function busquedaPorTipo(tipo) {
    tipo = Number(tipo); // Convertimos el tipo a número

    if (isNaN(tipo)) {
        console.error('El tipo debe ser un número válido'); // Muestra un error si el tipo no es un número válido
        return;
    }

    console.log('Iniciando fetch para FormUsuarios'); // Muestra un mensaje en la consola indicando el inicio de la petición
    fetch(`/web_eq_proyecto1/FormUsuarios?op=4&tipoUsuario=${encodeURIComponent(tipo)}`) // Hacemos una petición GET al servidor con el parámetro tipoUsuario
        .then(response => {
            console.log('Respuesta recibida', response); // Muestra la respuesta recibida del servidor en la consola
            if (!response.ok) {
                throw new Error(`Network response was not ok: ${response.status} ${response.statusText}`); // Muestra un error si la respuesta no es OK
            }
            return response.json(); // Parseamos la respuesta JSON
        })
        .then(data => {
            console.log('Datos recibidos', data); // Muestra los datos recibidos en la consola
            if (Array.isArray(data)) {
                pintarTabla(data); // Llama a la función 'pintarTabla' si los datos son un array
            } else {
                console.error('Datos recibidos no son un array:', data);// Muestra un error si los datos no son un array
            }
        })
        .catch(error => console.error('Error fetching data:', error)); // Manejamos errores de la petición
}
