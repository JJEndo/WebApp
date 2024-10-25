// Obtenemos referencias a los elementos del formulario de subida de video
const uploadForm = document.getElementById('upload-form'); // Formulario
const videoInput = document.getElementById('video-input'); // Input para seleccionar el video
const uploadBtn = document.getElementById('upload-btn');  // Botón para subir el video
const progressBarFill = document.getElementById('progress-bar-fill');  // Barra de progreso de la subida
const uploadStatus = document.getElementById('upload-status'); // Elemento para mostrar el estado de la subida

// Añadimos un evento click al botón de subir video
uploadBtn.addEventListener('click', () => {
    const file = videoInput.files[0]; // Obtenemos el primer archivo seleccionado
    if (file) {
        const formData = new FormData(uploadForm); // Creamos un FormData con los datos del formulario
        formData.append('video', file); // Añadimos el archivo de video a FormData

        const xhr = new XMLHttpRequest(); // Creamos una nueva solicitud XMLHttpRequest
        xhr.open('POST', 'C:\Users\jjend\git\repository\web_eq_proyecto1\src\main\webapp\videosSubidos', true); // Configuramos la solicitud para enviar los datos al servidor

		// Añadimos un evento para actualizar la barra de progreso durante la subida
        xhr.upload.addEventListener('progress', (event) => { 
            const percent = (event.loaded / event.total) * 100; // Calculamos el porcentaje de subida
            progressBarFill.style.width = percent + '%'; // Actualizamos la anchura de la barra de progreso
        });

		 // Añadimos un evento para manejar la respuesta del servidor al finalizar la subida
        xhr.onload = () => {
            if (xhr.status === 200) {
                uploadStatus.textContent = '¡El video se ha subido correctamente!';
            } else {
                uploadStatus.textContent = 'Error al subir el video. Por favor, inténtalo de nuevo.';
            }
        };
		// Añadimos un evento para manejar errores en la subida
        xhr.onerror = () => {
            uploadStatus.textContent = 'Error al subir el video. Por favor, inténtalo de nuevo.';
        };

        xhr.send(formData); // Enviamos los datos al servidor
    } else {
        uploadStatus.textContent = 'Por favor, selecciona un video antes de subirlo.';
    }
});
	// Cuando la ventana se carga, obtenemos parámetros de la URL y llamamos a una función para procesarlos
       window.onload = function() {
            const id = getParameterByName("id"); // Obtenemos el parámetro "id" de la URL
            const op = getParameterByName("op"); // Obtenemos el parámetro "op" de la URL
            console.log(`Parámetros obtenidos de la URL: id=${id}, op=${op}`);
            if (id && op) {
                llamada(id, op); // Llamamos a la función "llamada" si existen ambos parámetros
            } else {
                console.log('ID y/o OP no encontrados en la URL');
            }
        };

		// Función para hacer una petición al servidor y obtener datos de video
        function llamada(id, op) {
            console.log(`Llamada a fetch con id=${id} y op=${op}`);
            fetch(`/ListarVideos?id=${id}&op=2`)  // Hacemos una petición GET al servidor con los parámetros id y op
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
            const regex = new RegExp("[?&]" + name + "(=([^&#]*)|&|#|$)"),
                  results = regex.exec(location.search);
            return results ? decodeURIComponent(results[2].replace(/\+/g, " ")) : null;
        }
		
		// Función para llenar un formulario con datos
        function pintarFormulario(datos) {
            if (datos) {
                document.getElementById("id").value = datos.id || '';// Llenamos el campo "id" con los datos recibidos
                document.getElementById("titulo").value = datos.titulo || '';
                document.getElementById("director").value = datos.director || '';
                document.getElementById("musica").value = datos.musica || '';
                document.getElementById("sinopsis").value = datos.sinopsis || '';
                document.getElementById("foto").value = datos.foto || '';
            } else {
                console.error('Datos no encontrados o inválidos');  // Error si no hay datos
            }
        }