function showFriendCardsOnScroll(entries, observer) {
    entries.forEach((entry) => {
        if (entry.isIntersecting) {
            entry.target.style.transform = "translateY(0)"; // Move the card back to its original position
            observer.unobserve(entry.target);
        }
    });
}

const cardObserver = new IntersectionObserver(showFriendCardsOnScroll, {
    root: null,
    threshold: 0.2, // Adjust the threshold as needed
});

document.querySelectorAll(".card-container").forEach((cardContainer) => {
    cardObserver.observe(cardContainer);
});
