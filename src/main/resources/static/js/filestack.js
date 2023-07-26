const postImageButton = document.querySelector('.upload-post-image');
const postImage = document.querySelector('.post-image');
const postImageURLInput = document.querySelector('#postImageURLInput');
const postBodyTextarea = document.querySelector('#postBodyTextarea');
const submitButton = document.querySelector('.submit-post');

postImageButton.addEventListener('click', () => {
    const apiKey = 'APaRx6LcnR9W7kXg7lQw2z';
    const client = filestack.init(apiKey);
    const options = {
        onFileUploadStarted: () => {
            // This callback is triggered when the user selects a file and clicks the "Upload" button.
            showUploadedImage();
        },
        onUploadDone: (uploadResponse) => {
            console.log('onUploadDone', uploadResponse);
            const postImageURL = uploadResponse.filesUploaded[0].url;
            postImage.setAttribute('src', postImageURL);
            postImageURLInput.value = postImageURL; // Store the postImageURL in the hidden input field.
            enableSubmitButton();
        }
    };
    client.picker(options).open();
});

function showUploadedImage() {
    // Show the container by removing the "display: none" CSS property.
    const uploadPostImgContainer = document.querySelector('.upload-post-img');
    uploadPostImgContainer.style.display = 'block';
}


function enableSubmitButton() {
    if (postBodyTextarea.value.trim() !== '') {
        submitButton.disabled = false;
    }
}

postBodyTextarea.addEventListener('input', () => {
    enableSubmitButton();
});