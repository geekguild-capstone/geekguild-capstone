const bannerButton = document.querySelector('.upload-group-banner');
const banner = document.querySelector('.group-banner');
const bannerURLInput = document.querySelector('#fileURLInputGroupBanner');

bannerButton.addEventListener('click', () => {
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
            const bannerURL = uploadResponse.filesUploaded[0].url;
            banner.setAttribute('value', bannerURL);
            bannerURLInput.value = bannerURL; // Store the postImageURL in the hidden input field.
            // enableSubmitButton();
            console.log(bannerURLInput.value)

        }
    };
    client.picker(options).open();
});
