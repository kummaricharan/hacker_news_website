if ('webkitSpeechRecognition' in window) {
    const recognition = new webkitSpeechRecognition();

    const searchInput = document.getElementById('search-input');
    const voiceSearchIcon = document.getElementById('voice-search-icon');

    voiceSearchIcon.addEventListener('click', () => {
        // Start listening for speech
        recognition.start();

        // Change the icon color to green while listening
        voiceSearchIcon.classList.add('listening');
    });

    // Handle the speech recognition result
    recognition.onresult = (event) => {
        const result = event.results[0][0].transcript;
        searchInput.value = result;

        // Remove the green color when done listening
        voiceSearchIcon.classList.remove('listening');
    };

    // Handle errors
    recognition.onerror = (event) => {
        console.error('Speech recognition error:', event.error);

        // Remove the green color on error
        voiceSearchIcon.classList.remove('listening');
    };
} else {
    alert('Speech recognition is not supported in this browser. Please use a modern browser that supports the Web Speech API.');
}