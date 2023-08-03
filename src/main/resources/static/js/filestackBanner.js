const bannerImageButton = document.querySelector('.upload-banner-icon');
const bannerImage = document.querySelector('.banner-logo');
const bannerImageURLInput = document.querySelector('#fileURLInputBanner');
// const postBodyTextarea = document.querySelector('#postBodyTextarea');
const submitButton = document.querySelector('#save-profile-btn');
const form = document.querySelector(".inline-form")

bannerImageButton.addEventListener('click', () => {
    const apiKey = 'AkB0xPD0qS7OWG1qTtSu5z';
    // const apiKey = 'APaRx6LcnR9W7kXg7lQw2z';

    const client = filestack.init(apiKey);
    const options = {
        onFileUploadStarted: () => {
            // This callback is triggered when the user selects a file and clicks the "Upload" button.
            showUploadedImage();
        },
        onUploadDone: (uploadResponse) => {
            console.log('onUploadDone', uploadResponse);
            const bannerImageURL = uploadResponse.filesUploaded[0].url;
            bannerImage.setAttribute('src', bannerImageURL);
            bannerImageURLInput.value = bannerImageURL; // Store the postImageURL in the hidden input field.
            // enableSubmitButton();
        }
    };
    client.picker(options).open();
});

function showUploadedImage() {
    // Show the container by removing the "display: none" CSS property.
    const uploadPostImgContainer = document.querySelector('.banner-logo');
    uploadPostImgContainer.style.display = 'block';
}

submitButton.addEventListener('click', () => {
    console.log("clicked")
    form.submit()
})


// function enableSubmitButton() {
//     if (postBodyTextarea.value.trim() !== '') {
//         submitButton.disabled = false;
//     }
// }
//
// postBodyTextarea.addEventListener('input', () => {
//     enableSubmitButton();
// });