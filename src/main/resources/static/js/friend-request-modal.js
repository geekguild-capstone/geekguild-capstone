// modal.js
document.addEventListener("DOMContentLoaded", function() {
    const modalTriggerButton = document.getElementById("modalTriggerButton");
    const modalContainer = document.getElementById("myModalContainer");

    // Function to fetch and display the modal content
    function openModal() {
        fetch("/static/modals/friend-request-modal.html")
            .then(response => response.text())
            .then(content => {
                modalContainer.innerHTML = content;
            });
    }

    // Event listener for the trigger button
    modalTriggerButton.addEventListener("click", openModal);

    // Event listener for closing the modal
    // (Note: This assumes the close button inside the modal has an ID "closeModalButton")
    // modalContainer.addEventListener("click", function(event) {
    //     if (event.target.id === "closeModalButton") {
    //         modalContainer.innerHTML = "";
    //     }
    // });

    // close the modal by clicking outside the modal content
    window.addEventListener("click", function(event) {
        if (event.target === modalContainer) {
            modalContainer.innerHTML = "";
        }
    });
});
