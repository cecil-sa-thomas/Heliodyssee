document.getElementById("bookingForm").addEventListener("submit", function (e) {
    e.preventDefault();

    const formData = new FormData(this);

    const selectedCardInput = document.querySelector('input[name="selectedCard"]:checked');
    if (!selectedCardInput) {
        alert("Veuillez sélectionner une carte bancaire.");
        return;
    }

    const stripeId = selectedCardInput.value;
    const flightId = parseInt(document.getElementById("selectedFlightId").value);

    const bookingPayload = {
        flightId: parseInt(formData.get("flightId")),
        userId: 1, // ⚠️ à remplacer par utilisateur connecté
        firstNamePassenger: formData.get("firstNamePassenger"),
        lastNamePassenger: formData.get("lastNamePassenger"),
        passengerAge: formData.get("passengerAge"),
        gender: formData.get("gender") === "true",
        stripeId: stripeId  // 👈 Important
    };

    fetch("/bookings", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(bookingPayload)
    })
    .then(res => {
        if (!res.ok) throw new Error("Échec de la réservation");
        return res.json();
    })
    .then(data => {
        alert("✅ Réservation réussie !");
        document.getElementById("createBookingContainer").classList.add("hidden");
    })
    .catch(err => {
        console.error(err);
        alert("❌ Une erreur est survenue");
    });
});

// Affichage dynamique des cartes bancaires dans le formulaire de réservation
async function fetchAndDisplayCardsForBooking() {
    const cardListContainer = document.getElementById('cardListContainer');
    if (!cardListContainer) return;
    cardListContainer.innerHTML = '<p>Chargement...</p>';
    try {
        const res = await fetch('/paymentCard/me', { method: 'GET' });
        if (!res.ok) throw new Error('Impossible de récupérer les cartes');
        const cards = await res.json();
        if (!Array.isArray(cards) || cards.length === 0) {
            cardListContainer.innerHTML = '<p>Aucune carte enregistrée. <br> Ajoutez-en une depuis votre profil.</p>';
            return;
        }
        cardListContainer.innerHTML = cards.map(card => `
          <label style="display:block; margin-bottom:6px;">
            <input type="radio" name="selectedCard" value="${escapeHTML(card.stripeId)}" ${card.isDefault ? 'checked' : ''}>
            **** **** **** ${escapeHTML(card.lastDigit)} - ${escapeHTML(card.brand.toUpperCase())}
            (exp. ${String(card.expMonth).padStart(2, '0')}/${card.expYear})
            ${card.isDefault ? '<span class="default-badge">Principale</span>' : ''}
          </label>
        `).join('');

    } catch (err) {
        cardListContainer.innerHTML = `<p style="color:red">${err.message || 'Erreur lors du chargement des cartes.'}</p>`;
    }
}

// Appel au chargement du formulaire de réservation
// SUPPRIME : transitionend et appel automatique
// À la place, expose une fonction à appeler explicitement lors de l'ouverture du formulaire
window.openBookingForm = function() {
    const bookingContainer = document.getElementById('createBookingContainer');
    if (bookingContainer) {
        bookingContainer.classList.remove('hidden');
        fetchAndDisplayCardsForBooking();
    }
}

//Fonction anti-xss appeller lors de la recupération de cartes

function escapeHTML(str) {
  return String(str)
    .replace(/&/g, '&amp;')
    .replace(/</g, '&lt;')
    .replace(/>/g, '&gt;')
    .replace(/"/g, '&quot;')
    .replace(/'/g, '&#039;');
}
