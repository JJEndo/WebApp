// Este código se ejecuta después de que el documento HTML haya sido completamente cargado
document.addEventListener('DOMContentLoaded', function() {
	// Obtener referencias al elemento de video y a los botones
    var video = document.getElementById('videoPlayer');
    var botonPlayPause = document.getElementById('botonPlayPause');
    var botonMute = document.getElementById('botonMute');

    // Función para reproducir o pausar el video
    function togglePlayPause() {
		console.log("togglePlayPause called");
		// Verificar si el video está en pausa o ha terminado
        if (video.paused || video.ended) {
            video.play(); // Reproducir el video
            botonPlayPause.textContent = 'Pause'; // Cambiar el texto del botón a 'Pause'
        } else {
            video.pause();  //Pausar el video
            botonPlayPause.textContent = 'Play'; // Cambiar el texto del botón a 'Play'
        }
    }

    // Función para silenciar o desmutear el video
    function toggleMute() {
		console.log("toggleMute called");
		// Verificar si el video está silenciado
        if (video.muted) {
            video.muted = false; // Desilenciar el video
            botonMute.textContent = 'Mute'; // Cambiar el texto del botón a 'Mute'
        } else {
            video.muted = true; // Silenciar el video
            botonMute.textContent = 'Unmute'; // Cambiar el texto del botón a 'Unmute'
        }
    }

     // Añadir listeners a los botones para llamar a las funciones apropiadas al hacer clic
    botonPlayPause.addEventListener('click', togglePlayPause);
    botonMute.addEventListener('click', toggleMute);
});
