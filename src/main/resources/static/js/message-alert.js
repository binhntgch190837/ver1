setTimeout(function() {
  var flashMessage = document.getElementById('flash-message');
  if (flashMessage.textContent !== '') {
    flashMessage.classList.add('slide-out');
    setTimeout(function() {
      flashMessage.remove();
    }, 500); // Wait for 500ms for the animation to complete
  }
}, 5000);