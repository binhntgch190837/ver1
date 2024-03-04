// Get the modal element
var modal = document.getElementById("myModal");

// Display the modal when the page loads
modal.style.display = "block";

// Close the modal when the close button is clicked
var closeModal = document.getElementById("closeModal");
closeModal.onclick = function() {
    modal.style.display = "none";
}

// Close the modal if the user clicks outside of it
window.onclick = function(event) {
    if (event.target == modal) {
        modal.style.display = "none";
    }
}
