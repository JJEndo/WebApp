// Definición de la función getContextPath
// Esta función obtiene la ruta base de la aplicación web
function getContextPath() {
    // Obtiene la parte del pathname hasta el segundo "/"
    return window.location.pathname.substring(0, window.location.pathname.indexOf("/", 2));
}

// Evento que se ejecuta cuando toda la ventana se ha cargado completamente
window.onload = function() {
    console.log('Window loaded'); // Muestra un mensaje en la consola indicando que la ventana ha cargado
    llamada();  // Llama a la función 'llamada' para obtener datos
};

// Evento que se ejecuta cuando el DOM ha sido completamente cargado y parseado
document.addEventListener("DOMContentLoaded", function() {
    console.log('DOM fully loaded and parsed');  // Muestra un mensaje en la consola indicando que el DOM ha cargado completamente
    llamada(); // Llama a la función 'llamada' para obtener datos
});

// Función para hacer una petición al servidor y obtener datos de videos
function llamada() {
    console.log('Iniciando fetch para ListarVideos'); // Muestra un mensaje en la consola indicando el inicio de la petición
    fetch(getContextPath() + '/ListarVideos?op=1') // Hacemos una petición GET al servidor con el parámetro op=1
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
                console.error('Datos recibidos no son un array:', data); // Muestra un error si los datos no son un array
            }
        })
        .catch(error => console.error('Error fetching data:', error)); // Manejamos errores de la petición
}

// Función para borrar un video por su id
function borrar(id) {
    // Pregunta al usuario si está seguro de borrar
    if (confirm("¿Seguro que quieres borrar?")) {
        fetch(`/ListarVideos?id=${id}&op=3`)  // Hacemos una petición GET al servidor con el parámetro id y op=3
            .then(response => {
                console.log('Respuesta recibida', response);  // Muestra la respuesta recibida del servidor en la consola
                if (!response.ok) {
                    throw new Error(`Network response was not ok: ${response.status} ${response.statusText}`); // Muestra un error si la respuesta no es OK
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

// Función para redirigir a la página de edición con el id del video
function editar(id) {
    // Redirigir a subirVideo.html con los parámetros id y op
    window.location.href = `subirVideo.html?id=${id}&op=4`;
}

// Función para pintar una tabla con datos de videos
function pintarTabla(datos) {
    const listadoElement = document.getElementById("listado"); // Obtenemos el elemento con id "listado"
    if (!listadoElement) {
        console.error('Elemento con id "listado" no encontrado'); // Muestra un error si el elemento no existe
        return;
    }

    console.log('Pintando la tabla con datos', datos); // Muestra los datos que se van a pintar en la consola
    // Mapea los datos a filas HTML y los une en una cadena
    const filasHtml = datos.map(({ id, titulo, director, musica, sinopsis, foto }) => `
        <tr>
            <td>${id}</td>
            <td>${titulo}</td>
            <td>${director}</td>
            <td>${musica}</td>
            <td>${sinopsis}</td>
            <td>
                ${foto ? `<video width="320" height="240" controls>
                    <source src="${getContextPath()}/videosSubidos/${foto}" type="video/mp4">
                    Your browser does not support the video tag.
                </video>` : 'No hay ningún video subido.'}
            </td>
            <td>
                <button onclick="borrar(${id})">Borrar</button>
                <button onclick="editar(${id})">Editar</button>
            </td>
        </tr>
    `).join('');

    // Creamos el HTML de la tabla
    const tablaHtml = `<table class='listaTabla'><thead>
        <tr>
            <th>ID</th>
            <th>Título</th>
            <th>Director</th>
            <th>Música</th>
            <th>Sinopsis</th>
            <th>Video</th>
            <th>Acciones</th>
        </tr>
    </thead><tbody>${filasHtml}</tbody></table>`;
    listadoElement.innerHTML = tablaHtml; // Insertamos el HTML de la tabla en el elemento listado
}

