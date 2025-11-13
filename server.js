const express = require('express');
const { exec } = require('child_process');
const cors = require('cors');
const PORT = 8080;
const app = express();

app.use(express.json()); 
app.use(cors());

function page_index(req,res){ 
    res.sendFile(__dirname+"/src/main/ressources/index.html")
}

app.use(express.static(__dirname+'/src/main/ressources'));
//app.use('/', page_index); 

app.use((req, res, next) => {
    console.log(`Requête reçue : ${req.method} ${req.url}`);
    next();
});


app.post('/execute', (req, res) => {
    const cityName = req.body.cityName; 
    const nbJours = req.body.nbJours; 
    if (!cityName) {
        return res.status(400).send('Nom de la ville manquant.');
    }

    
    const command = `./gradlew run --args="'${cityName}' ${nbJours}"`;
    console.log(command); 
    exec(command, (error, stdout, stderr) => {
        if (error) {
            console.error(`Erreur lors de l'exécution : ${error.message}`);
            return res.status(500).send('Erreur lors de l\'exécution du programme.');
        }
        if (stderr) {
            console.error(`Erreur dans le programme Java : ${stderr}`);
        }
        console.log(`Sortie : ${stdout}`);
        res.send(`Programme exécuté avec succès. Sortie : ${stdout}`);
    });
});

// Démarrer le serveur
app.listen(PORT, () => {
    console.log(`Serveur démarré sur le http://localhost:${PORT}`);
});
