function openConfirmationModal(username, userId, userImage) {
    const image = document.querySelector('.profile-picture-request')
    const modal = document.getElementById("confirmationModal");
    const friendUsername = document.getElementById("friendUsername");
    // const friendId = document.getElementById("friendId");
    const receiverIdInput = document.getElementById("receiverIdInput");
    receiverIdInput.value = userId;
    friendUsername.textContent = username;
    image.setAttribute("src", userImage)
    // friendId.textContent = userId;
    modal.style.display = "flex";

    // Store the user ID in a hidden input field inside the modal
    const hiddenUserIdInput = document.createElement("input");
    hiddenUserIdInput.type = "hidden";
    hiddenUserIdInput.name = "receiverId";
    hiddenUserIdInput.value = userId;
    modal.appendChild(hiddenUserIdInput);
}

function closeModal() {
    const modal = document.getElementById("confirmationModal");
    modal.style.display = "none";
}

window.addEventListener('click', function (event) {
    const modal = document.getElementById('confirmationModal');
    if (event.target === modal) {
        closeModal();
    }
});