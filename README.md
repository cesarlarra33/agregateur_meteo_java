

# Projet de PG203

Ce starter kit vous permet de démarrer un projet d'application en
ligne de commande Java. La gestion du build est effectuée par l'outil
Gradle. Deux exécutables sont fournis: `gradlew` pour Unix ou MacOS et
`gradlew.bat` pour Windows.

Le starter kit vient avec:

- le framework [`JUnit 5`](https://junit.org/junit5/docs/current/user-guide/) pour gérer les tests;

- la bibliothèque [`JSON-Java`](https://github.com/stleary/JSON-java)
  pour la manipulation de fichiers JSON;

- l'outil [`Jacoco`](https://www.jacoco.org/) pour la couverture du
  code par les tests.

Le starter-kit contient un fichier
`src/main/java/eirb/pg203/Main.java` qui contient un programme de
démonstration. Ce programme récupère via l'API [Chuck Norris
Joke](https://api.chucknorris.io/) une blague sur Chuck Norris au
format JSON. Cette blague est ensuite parsée par la librarie
`org-json` et affichée sur la console.

Le fichier `src/main/java/eirb/pg203/SampleTest.java` contient un
petit exemple de test unitaire de la fonction qui télécharge et parse
la blague en question.

Voici comment effectuer les différentes commandes importantes.

## Compilation

```bash
./gradlew build
```

## Lancement des tests

```bash
./gradlew test
```

## Génération du rapport de couverture

```bash
./gradlew jacocoTestReport
```

Le rapport se trouve dans `build/reports/jacoco/test/html/index.html`.

## Lancement du programme

```
./gradlew run --args="arg1 arg2"
```

## Documentation

- WeatherAPI : la [documentation](https://www.weatherapi.com/docs)
  ainsi qu'une [page de test](https://www.weatherapi.com/api-explorer.aspx)

- OpenMeteo : la [documentation](https://open-meteo.com/en/docs)

- OpenWeatherMap : la [documentation](https://openweathermap.org/api)

## Documentation donnée par Cédric et César

-Pour ce qui est de la compilation, du lancement des tests ou du programme il n'y a pas de changements. 

-Pour le lancement du programme, il faut utiliser --args="arg1 arg2" avec arg1 le nom d'une ville et arg2 le nombre de jours voulus

-Pour utiliser notre seurveur, il y a 2 manières: 
  -soit il faut décommentez la ligne dans le module display, dans le fichier "DisplayService" tout à la fin du fichier afin de lancer directement le serveur sur une page web lorsque l'on lance l'application. 
  
  -Sinon, si l'on ne décommente pas, on affichera les informations dans le terminal. On peut alors utiliser "node server.js" dans le terminal ce qui lancera le serveur en local sur le port 8080 par défaut. On peut alors copier l'adresse et la coller sur une page de navigation. 

## Installation des dépendances nécessaires

Pour exécuter correctement le serveur Node.js, il est probable que vous deviez installer les modules `express` et `cors`. Voici les commandes à exécuter sous Linux pour installer ces dépendances :

1. **Installer `express`** :
  ```bash
  npm install express
  ```

2. **Installer `cors`** :
  ```bash
  npm install cors
  ```
