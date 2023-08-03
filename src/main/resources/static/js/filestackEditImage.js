const imageButton = document.querySelector('.upload-icon');
const image = document.querySelector('.logo');
const imageURLInput = document.querySelector('#fileURLInputImage');

imageButton.addEventListener('click', () => {
    const apiKey = 'AkB0xPD0qS7OWG1qTtSu5z';
    // const apiKey = 'APaRx6LcnR9W7kXg7lQw2z';

    const client = filestack.init(apiKey);
    const options = {
        // onFileUploadStarted: () => {
        //     // This callback is triggered when the user selects a file and clicks the "Upload" button.
        //     showUploadedImage();
        // },
        onUploadDone: (uploadResponse) => {
            console.log('onUploadDone', uploadResponse);
            const imageURL = uploadResponse.filesUploaded[0].url;
            image.setAttribute('src', imageURL);
            imageURLInput.value = imageURL; // Store the postImageURL in the hidden input field.
            // enableSubmitButton();
        }
    };
    client.picker(options).open();
});

// function showUploadedImage() {
//     // Show the container by removing the "display: none" CSS property.
//     const uploadPostImgContainer = document.querySelector('.banner-logo');
//     uploadPostImgContainer.style.display = 'block';
// }




// function enableSubmitButton() {
//     if (postBodyTextarea.value.trim() !== '') {
//         submitButton.disabled = false;
//     }
// }
//
// postBodyTextarea.addEventListener('input', () => {
//     enableSubmitButton();
// });