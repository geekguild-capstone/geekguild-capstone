// Function to show/hide the code snippet when the "Show Code" button is clicked
function toggleCodeSnippet(button) {
    const codeContainer = button.parentNode.querySelector('pre');
    codeContainer.style.display = codeContainer.style.display === 'none' ? 'block' : 'none';
    Prism.highlightElement(codeContainer.querySelector('code'));
}


// Add event listeners to all "Show Code" buttons
const showCodeButtons = document.querySelectorAll('.show-code-btn');
showCodeButtons.forEach(button => {
    button.addEventListener('click', () => toggleCodeSnippet(button));
});

