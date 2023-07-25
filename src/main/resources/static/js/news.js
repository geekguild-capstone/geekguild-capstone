// const fetchData = (q, from, sortBy, key) => {
//
//     const url = `https://newsapi.org/v2/everything?q=${q}&from=${from}&sortBy=${sortBy}&apiKey=${key}`;
//
//     const req = new Request(url);
//
//     fetch(req)
//         .then(function (response) {
//             return response.json(); // Parse the JSON data and return the resolved value as another Promise
//         })
//         .then(function (data) {
//             console.log(data); // Access the resolved JSON data here and log it to the console
//             renderCards(data);
//         })
//         .catch(function (error) {
//             console.error('Error fetching data:', error);
//         });
//
// }
// "from" date format 2023-07-24
// fetchData('microsoft', "2023-07-24", "relevance", NEWS_API)
const searchInput = document.getElementById('searchInput');
const apiKey = NEWS_API; // Replace with your News API key
let url = `https://newsapi.org/v2/everything?q=technology&from=2023-07-24&sortBy=popularity&language=en&apiKey=${apiKey}`;

// Function to fetch news articles based on the search input value
function fetchNews(event) {
    event.preventDefault();
    console.log("...fetching news")
    url = `https://newsapi.org/v2/everything?q=${searchInput.value}&from=2023-07-24&sortBy=popularity&language=en&apiKey=${apiKey}`;

    fetch(url)
        .then(function (response) {
            return response.json();
        })
        .then(function (data) {
            console.log(data)
            renderNewsCards(data.articles);
        })
        .catch(function (error) {
            console.error('Error fetching data:', error);
        });
}

// Function to render news cards in the container
function renderNewsCards(articles) {
    const newsCardsContainer = document.querySelector('.news-cards-container');
    newsCardsContainer.innerHTML = ''; // Clear previous content
    let html = ''

    articles.forEach(function (article, i) {
        if (i < 10) {
            html += `
        <div class="article-card">
            <h3 class="article-title">
            <a class="article-url" href="${article.url}" target="_blank">${article.title}</a>
            </h3>
            <img class="article-image" src="${article.urlToImage}" alt="Article Image">
            </a>
            <p>
            ${article.description}
            </p>
<!--            <a class="article-url" href="https://example.com/article-url" target="_blank">Read more</a>-->
        </div>
            `
            // Create a card element and add news article data to it
            // const card = document.createElement('div');
            // card.classList.add('news-card');
            //
            // const title = document.createElement('h3');
            // title.textContent = article.title;
            // card.appendChild(title);

            // Add more content to the card if needed (e.g., description, image, etc.)

            // newsCardsContainer.appendChild(html);
        }
        newsCardsContainer.innerHTML = html;

    });
}

// Listen for submit event on the search form and fetch news articles accordingly
const searchForm = document.getElementById('searchForm');
searchForm.addEventListener('submit', fetchNews);

// Fetch initial news articles on page load
// fetchNews(url);
