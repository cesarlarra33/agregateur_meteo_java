
const conditionImages = {
    'Sunny': 'images/sunny.jpeg',                
    'Partly Cloudy': 'images/partlycloudy.jpeg',
    'Heavy snow': 'images/heavysnow.jpeg',
    'Light snow': 'images/lightsnow.jpeg',
    'Light freezing rain': 'images/lightfreezingrain.jpeg',
    'Cloudy': 'images/cloudy.jpeg',
    'Overcast': 'images/cloudy.jpeg',
    'Rain': 'images/rain.jpeg',
    'Snow': 'images/snow.jpeg',
    'Thunderstorm': 'images/thunderstorm.jpeg',
    'Fog': 'images/fog.jpeg',
    'Patchy rain nearby': 'images/patchyrain.jpeg',
    'Moderate rain': 'images/moderaterain.jpeg',
    'Heavy rain': 'images/heavyrain.jpeg'
};


function updateWeatherBackgrounds() {

    const conditionElements = document.querySelectorAll('.condition');

    conditionElements.forEach((element) => {

        const conditionText = element.querySelector('span').innerText.trim();
        const backgroundImage = conditionImages[conditionText];

        if (backgroundImage) {

            element.style.backgroundImage = `url(${backgroundImage})`;
            element.style.backgroundSize = 'cover'; 
            element.style.backgroundPosition = 'center'; 
            element.style.backgroundRepeat = 'no-repeat'; 
        }
    });
}

document.addEventListener('DOMContentLoaded', updateWeatherBackgrounds);