const imageButton = document.querySelector('.upload-group-icon');
const input = document.querySelector('.group-logo');
const submitButton = document.querySelector(".create-group-button")
const imageURLInput = document.querySelector('#fileURLInputGroupImage');
const form = document.querySelector(".group-form")

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
            input.setAttribute('value', imageURL);
            imageURLInput.value = imageURL; // Store the postImageURL in the hidden input field.
            // enableSubmitButton();
        }
    };
    client.picker(options).open();
});

submitButton.addEventListener('click', () => {
    console.log("clicked")
    form.submit()
})
