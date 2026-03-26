import {
    planetPositions,
    getCurrentVisiblePlanet,
    updateSelectButton,
    setCurrentVisiblePlanet,
    setCurrentZ,
    currentVisiblePlanet,
    currentState,
    selectedDepartureValue,
    selectedDestinationValue,
    currentZ,
    setupPanel
} from './shared.js';

document.addEventListener('DOMContentLoaded', function() {
    // Éléments DOM
    const planetsGroup = document.getElementById('planets-group');
    const selectButton = document.getElementById('selectPlanetButton');
    const planetsPanel = document.getElementById('planetsPanel');
    const toggleButton = document.getElementById('toggleButton');
    const chevron = document.getElementById('chevron');

    // Configuration du panneau
    setupPanel(planetsPanel, toggleButton, chevron);

    // Fonction pour animer le déplacement vers une planète
    function animateToPlanet(planetName) {
        const targetZ = planetPositions[planetName];
        planetsGroup.style.transition = 'transform 1s ease-in-out';
        planetsGroup.style.transform = `translateZ(${-targetZ + 700}px)`;
        setCurrentZ(targetZ);

        setTimeout(() => {
            setCurrentVisiblePlanet(getCurrentVisiblePlanet(currentZ));
            updateSelectButton(selectButton, currentVisiblePlanet, currentState, selectedDepartureValue, selectedDestinationValue);
        }, 1000);
    }

    // Gestionnaires de clique pour les planètes
    const planetPanelItems = document.querySelectorAll('.planet-item');
    planetPanelItems.forEach(item => {
        item.addEventListener('click', () => {
            const planetId = item.id.replace('-panel', '');
            animateToPlanet(planetId);
        });
    });

    const celestialBodies = document.querySelectorAll('.celestial-body');
    celestialBodies.forEach(planet => {
        planet.addEventListener('click', () => {
            animateToPlanet(planet.id);
        });
    });

    // Initialisation
    planetsGroup.style.transform = `translateZ(${-planetPositions['mercure'] + 700}px)`;
}); 