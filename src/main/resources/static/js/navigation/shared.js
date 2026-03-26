// Variables partagées
export let currentState = 'destination';
export let selectedDepartureValue = null;
export let selectedDestinationValue = null;
export let currentVisiblePlanet = 'mercure';
export let currentZ = 700;

// Configuration des positions des planètes
export const planetPositions = {
    'mercure': 0,
    'venus': -4000,
    'terre': -8000,
    'mars': -12000,
    'jupiter': -16000,
    'saturne': -20000,
    'uranus': -24000,
    'neptune': -28000,
    'pluton': -32000
};

// Fonction partagée pour déterminer la planète visible
export function getCurrentVisiblePlanet(zPosition) {
    const tolerance = 500;
    for (const [planet, position] of Object.entries(planetPositions)) {
        if (Math.abs(zPosition - position) <= tolerance) {
            return planet;
        }
    }
    return null;
}

// Fonction pour mettre à jour le bouton de sélection
export function updateSelectButton(selectButton, currentVisiblePlanet, currentState, selectedDepartureValue, selectedDestinationValue) {
    if (currentVisiblePlanet) {
        const planetName = currentVisiblePlanet.charAt(0).toUpperCase() + currentVisiblePlanet.slice(1);
        selectButton.textContent = `Sélectionner ${planetName}`;
        selectButton.disabled = false;

        if ((currentState === 'destination' && currentVisiblePlanet === selectedDepartureValue) ||
            (currentState === 'departure' && currentVisiblePlanet === selectedDestinationValue)) {
            selectButton.disabled = true;
        }
    } else {
        selectButton.textContent = 'Aucune planète visible';
        selectButton.disabled = true;
    }
}

// Setters pour mettre à jour les variables partagées
export function setCurrentState(state) {
    currentState = state;
}

export function setSelectedDepartureValue(value) {
    selectedDepartureValue = value;
}

export function setSelectedDestinationValue(value) {
    selectedDestinationValue = value;
}

export function setCurrentVisiblePlanet(planet) {
    currentVisiblePlanet = planet;
}

export function setCurrentZ(z) {
    currentZ = z;
}

// Fonction utilitaire pour gérer les panneaux
export function setupPanel(panelElement, toggleButton, chevronElement) {
    let isPanelHidden = false;

    toggleButton.addEventListener('click', function() {
        isPanelHidden = !isPanelHidden;

        if (isPanelHidden) {
            panelElement.classList.add('hidden');
            chevronElement.classList.add('flipped');
        } else {
            panelElement.classList.remove('hidden');
            panelElement.classList.add('showing');
            chevronElement.classList.remove('flipped');
            setTimeout(() => {
                panelElement.classList.remove('showing');
            }, 300);
        }
    });
}