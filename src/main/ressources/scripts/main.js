//récupère les sélecteurs d'onglets et les onglets
const selectors = document.querySelectorAll('.onglet-selector');
const onglets = document.querySelectorAll('.onglet');

window.addEventListener('DOMContentLoaded', () => {
    // On fait en sorte que 'l'onglet cliqué par défaut' soit le premier
    const defaultSelector = document.getElementById('weatherapi-selector');
    const defaultOnglet = document.getElementById('weatherapi');
    defaultSelector.style.zIndex = 20;
    defaultOnglet.style.zIndex = 10;

    // On fait disparaitre derriere les autres selecteurs 
    selectors.forEach((selector) => {
        if (selector !== defaultSelector) {
            selector.style.zIndex = 0; // z-index intermédiaire
        }
    });

    onglets.forEach((onglet) => {
        if (onglet !== defaultOnglet) {
            onglet.style.zIndex = 0; // z-index de base
        }
    });
});

// on ajoute un eventListener à chaque sélecteur pour qu'il passe au 1er plan si cliqué 
selectors.forEach((selector, index) => {
    selector.addEventListener('click', () => {

        // Récupérer l'ID de l'onglet dont le sélecteur est cliqué
        const targetId = selector.id.replace('-selector', '');
        const targetOnglet = document.getElementById(targetId);

        // Réinitialiser les z-index des sélecteurs et des onglets
        selectors.forEach((sel) => {
            sel.style.zIndex = 1; 
        });
        onglets.forEach((onglet) => {
            onglet.style.zIndex = 0; 
        });

        // sélecteur cliqué au premier plan
        selector.style.zIndex = 20;

        // l'onglet correspondant juste en dessous du sélecteur
        targetOnglet.style.zIndex = 10;

        // permet d'avoir un effet intercalaires de classeur
        selectors.forEach((sel, i) => {
            if (i !== index) {
                sel.style.zIndex = 0; // Z-index intermédiaire pour les autres sélecteurs
            }
        });
    });
});
