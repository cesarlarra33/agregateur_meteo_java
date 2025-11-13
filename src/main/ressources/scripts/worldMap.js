async function getCoordinates(locationName) {
    try {
        const response = await fetch(`https://nominatim.openstreetmap.org/search?q=${encodeURIComponent(locationName)}&format=json&limit=1`);
        const data = await response.json();
        
        if (data.length > 0) {
            const latitude = parseFloat(data[0].lat);
            const longitude = parseFloat(data[0].lon);
            console.log("Latitude:", latitude, "Longitude:", longitude);
            return [latitude, longitude];
        } else {
            console.log("Lieu introuvable.");
            return null;
        }
    } catch (error) {
        console.error("Erreur :", error);
        return null;
    }
}

async function getCityName(latitude, longitude) {
    try {
        const response = await fetch(`https://nominatim.openstreetmap.org/reverse?lat=${latitude}&lon=${longitude}&format=json`);
        const data = await response.json();

        if (data && data.address) {
            const city = data.address.city || data.address.town || data.address.village || data.address.hamlet || "Localité inconnue";
            console.log("Nom de la ville :", city);
            return city;
        } else {
            console.log("Impossible de déterminer le nom de la ville.");
            return "Lieu introuvable";
        }
    } catch (error) {
        console.error("Erreur :", error);
        return "Erreur lors de la récupération des données";
    }
}

async function initMap() {
    const locationName = document.getElementById('ville').textContent;
    const coordinates = await getCoordinates(locationName);

    if (coordinates) {
        const [lat, lon] = coordinates;

        const map = L.map('map').setView([lat, lon], 5); 

        L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
            attribution: '© OpenStreetMap contributors',
            maxZoom: 18,
        }).addTo(map);

        // Création du marqueur draggable
        const marker = L.marker([lat, lon], { draggable: true }).addTo(map);
        marker.bindPopup(`Météo pour ${locationName}`).openPopup();

        // Écouteur pour capturer les nouvelles coordonnées après drag and drop
        marker.on('dragend', async function (event) {
            const newLat = event.target.getLatLng().lat;
            const newLon = event.target.getLatLng().lng;
            const nbJours = document.getElementById('nbjours-selector').value;
        
            // Obtenez le nom de la ville
            const cityName = await getCityName(newLat, newLon);
            
            console.log("Nouvelles coordonnées :", newLat, newLon);
            console.log(`Nouvelle ville sélectionnée : ${cityName}`);
        
            // Envoyer une requête POST au serveur pour exécuter la commande
            fetch('http://localhost:8080/execute', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ cityName , nbJours}),
            })
            .then(response => response.text())
            .then(data => {
                console.log(`Réponse du serveur : ${data}`);
                // Rafraîchir la page après l'exécution
                location.reload();
            })
            .catch(error => {
                console.error('Erreur lors de la requête :', error);
            });
        });
    } else {
        console.log("Erreur : les coordonnées n'ont pas pu être récupérées.");
    }
}




window.addEventListener('DOMContentLoaded', initMap);
