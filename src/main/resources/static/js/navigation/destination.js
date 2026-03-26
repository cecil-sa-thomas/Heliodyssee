// Choix du voyage
// Affichage liste des vols

import {
    currentState,
    currentVisiblePlanet,
    selectedDepartureValue,
    selectedDestinationValue,
    updateSelectButton,
    setCurrentState,
    setSelectedDepartureValue,
    setSelectedDestinationValue,
    setupPanel
} from './shared.js';

document.addEventListener('DOMContentLoaded', function() {
    // Éléments DOM
    const panelTitle = document.getElementById('panelTitle');
    const selectionsList = document.getElementById('selectionsList');
    const selectButton = document.getElementById('selectPlanetButton');
    const launchButton = document.getElementById('launchButton');
    const destinationPanel = document.getElementById('destinationPanel');
    const destinationToggleButton = document.getElementById('destinationToggleButton');
    const destinationChevron = document.getElementById('destinationChevron');

    let isPanelHidden = false;

    // Fonction pour créer un élément de sélection
    function createSelectionItem(type, value) {
        const item = document.createElement('div');
        item.className = 'selection-item';

        const typeSpan = document.createElement('span');
        typeSpan.className = 'selection-type';
        typeSpan.textContent = type;

        const valueSpan = document.createElement('span');
        valueSpan.className = 'selection-value';
        valueSpan.textContent = value.charAt(0).toUpperCase() + value.slice(1);

        const deleteButton = document.createElement('button');
        deleteButton.className = 'delete-button';
        deleteButton.textContent = '✕';
        deleteButton.onclick = () => {
            // Corrigé : on teste bien sur 'Départ' ou 'Destination' uniquement
            if (type === 'Départ') {
                setSelectedDepartureValue(null);
            } else if (type === 'Destination') {
                setSelectedDestinationValue(null);
            }

            // Mise à jour de l'état (corrigé la syntaxe des guillemets)
            if (!selectedDestinationValue && !selectedDepartureValue) {
                setCurrentState('destination');
            } else if (!selectedDestinationValue) {
                setCurrentState('destination');
            } else if (!selectedDepartureValue) {
                setCurrentState('departure');
            }

            updatePanelTitle();
            updateSelectionsList();
            updateSelectButton(selectButton, currentVisiblePlanet, currentState, selectedDepartureValue, selectedDestinationValue);
            updateLaunchButton();
        };

        item.appendChild(typeSpan);
        item.appendChild(valueSpan);
        item.appendChild(deleteButton);

        return item;
    }

    // Fonction pour mettre à jour le bouton de lancement
    function updateLaunchButton() {
        if (selectedDestinationValue && selectedDepartureValue) {
            launchButton.classList.remove('hidden');
        } else {
            launchButton.classList.add('hidden');
        }
    }

    // Fonction pour mettre à jour la liste des sélections
    function updateSelectionsList() {
        selectionsList.innerHTML = '';

        // Corrigé : plus de tiret ni espace dans le type
        if (selectedDestinationValue) {
            selectionsList.appendChild(
                createSelectionItem('Destination', selectedDestinationValue)
            );
        }

        if (selectedDepartureValue) {
            selectionsList.appendChild(
                createSelectionItem('Départ', selectedDepartureValue)
            );
        }

        selectionsList.style.minHeight =
            (selectedDestinationValue || selectedDepartureValue) ?
            `${selectionsList.scrollHeight}px` : '0';

        updateLaunchButton();
    }

    // Fonction pour mettre à jour le titre du panneau
    function updatePanelTitle() {
        if (!selectedDestinationValue) {
            panelTitle.textContent = 'Choix de destination';
            panelTitle.style.color = '#00F86B'; // vert fluo
            panelTitle.style.textShadow = '0 0 3px #00F86B, 0 0 8px #00F86B, 0 0 16px #00F86B, 0 0 32px #00F86B';
        } else if (!selectedDepartureValue) {
            panelTitle.textContent = 'Choix départ';
            panelTitle.style.color = '#3A9BC8'; // bleu
            panelTitle.style.textShadow = '0 0 3px #0084F8, 0 0 8px #0084F8, 0 0 16px #0084F8, 0 0 32px #0084F8';
        } else {
            panelTitle.textContent = 'Voyage planifié';
            panelTitle.style.color = '#FEB063';
            panelTitle.style.textShadow = '0 0 3px #F83A00, 0 0 8px #F83A00, 0 0 16px #F83A00, 0 0 32px #F83A00';
        }
    }

    // Gestionnaire de clic pour le bouton de sélection
    selectButton.addEventListener('click', () => {
        if (!currentVisiblePlanet) return;

        if (currentState === 'destination') {
            setSelectedDestinationValue(currentVisiblePlanet);
            setCurrentState('departure');
        } else if (currentState === 'departure') {
            setSelectedDepartureValue(currentVisiblePlanet);
        }

        updateSelectionsList();
        updatePanelTitle();
        updateSelectButton(selectButton, currentVisiblePlanet, currentState, selectedDepartureValue, selectedDestinationValue);
        updateLaunchButton();
    });

    //Planet en id
    function planetNameToId(name) {
        const planetMap = {
            mercure: 1,
            venus: 2,
            terre: 3,
            mars: 4,
            jupiter: 5,
            saturne: 6,
            uranus: 7,
            neptune: 8,
            pluton: 9
        };
        return planetMap[name.toLowerCase()];
    }

    // Gestionnaire de clic pour le bouton de lancement
    launchButton.addEventListener('click', () => {
        console.log("Départ sélectionné :", selectedDepartureValue);
        console.log("Destination sélectionnée :", selectedDestinationValue);

        fetch(`/flights/filter?departurePlanetId=${planetNameToId(selectedDepartureValue)}&arrivalPlanetId=${planetNameToId(selectedDestinationValue)}`)
            .then(response => {
                if (!response.ok) throw new Error("Erreur lors du chargement des vols");
                return response.json();
            })
            .then(data => {
                console.log("✈️ Vols récupérés :", data);
                displayFlights(data); // 👈 Injecte les données ici
            })
            .catch(error => {
                console.error("❌ Erreur lors de la récupération des vols :", error);
            });
    });

    // Configuration du panneau
    setupPanel(destinationPanel, destinationToggleButton, destinationChevron);

    // Gestionnaire du toggle panel destination
    destinationToggleButton.addEventListener('click', function() {
        isPanelHidden = !isPanelHidden;

        if (isPanelHidden) {
            destinationPanel.classList.add('hidden');
            destinationChevron.classList.add('flipped');
        } else {
            destinationPanel.classList.remove('hidden');
            destinationPanel.classList.add('showing');
            destinationChevron.classList.remove('flipped');
            setTimeout(() => {
                destinationPanel.classList.remove('showing');
            }, 300);
        }
    });

    // Initialisation
    updatePanelTitle();
    updateSelectButton(selectButton, currentVisiblePlanet, currentState, selectedDepartureValue, selectedDestinationValue);
    updateLaunchButton();
});

// Liste des vol
function displayFlights(flights) {
    const container = document.getElementById("flightsListContainer");
    const list = document.getElementById("flightsList");
    const closeButton = document.getElementById("closeFlightsButton");

    list.innerHTML = ''; // Reset

    flights.forEach(flight => {
        const dDepart = new Date(flight.dateDeparture);
        const dArrive = new Date(flight.dateArrival);

        const jourDepart = dDepart.toLocaleDateString();
        const heureDepart = dDepart.toLocaleTimeString([], { hour: "2-digit", minute: "2-digit" });

        const jourArrive = dArrive.toLocaleDateString();
        const heureArrive = dArrive.toLocaleTimeString([], { hour: "2-digit", minute: "2-digit" });

        const item = document.createElement('div');
        item.classList.add('flight-card');
        item.style.display = '';
        item.style.flexDirection = '';
        item.style.alignItems = '';
        item.style.margin = '';

        item.innerHTML = `
            <div class="row row-1">
                <div class="cell left">${jourDepart}</div>
                <div class="cell left">${jourArrive}</div>
                <div class="cell spacer"></div>
                <div class="cell status right">${flight.status}</div>
                <div class="cell"></div>
            </div>
            <div class="row row-2">
                <div class="cell left">${heureDepart}</div>
                <div class="cell left">${heureArrive}</div>
                <div class="cell spacer"></div>
                <div class="cell right">${flight.seatsAvailable} dispo</div>
                <div class="cell price-cell right"></div>
            </div>
            <div class="row row-3">
                <div class="cell left">Vol n° ${flight.numFlight}</div>
                <div class="cell"></div>
                <div class="cell spacer"></div>
                <div class="cell right">${flight.seats} total</div>
                <div class="cell"></div>
            </div>
        `;

        // Ajout du bouton prix
        const priceCell = item.querySelector('.price-cell');
        const priceButton = document.createElement('button');
        priceButton.className = 'form-button validate-button flight-price-button';
        priceButton.textContent = `${flight.price} crédits`;
        priceButton.onclick = async () => {
            const bookingContainer = document.getElementById("createBookingContainer");
            // bookingContainer.classList.remove("hidden"); // On laisse openBookingForm() gérer l'ouverture

            // Tu peux stocker l'ID du vol dans un champ caché par exemple
            document.getElementById("selectedFlightId").value = flight.idFlight;

            // ✅ Ouvre le formulaire et fetch les cartes
            openBookingForm();

            // Optionnel : focus sur le champ prénom
            document.querySelector("#createBookingContainer input[name='prenom']")?.focus();
        };
        priceCell.appendChild(priceButton);

        list.appendChild(item);
    });

    container.classList.remove("hidden");
    closeButton.onclick = () => container.classList.add("hidden");
}