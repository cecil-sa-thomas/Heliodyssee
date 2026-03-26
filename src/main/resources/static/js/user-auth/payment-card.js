document.addEventListener('DOMContentLoaded', function() {
  // Sélecteurs modale
  const cardPaymentBtn = document.getElementById('card-payment-btn');
  const cardListModal = document.getElementById('cardListModal');
  const cardListOverlay = document.getElementById('cardListOverlay');
  const closeCardListButton = document.getElementById('closeCardListButton');
  const cardList = document.getElementById('cardList');
  const addCardBtn = document.getElementById('addCardBtn');
  const addCardFormContainer = document.getElementById('addCardFormContainer');
  const addCardForm = document.getElementById('addCardForm');
  const cancelAddCardBtn = document.getElementById('cancelAddCardBtn');
  const cardErrors = document.getElementById('card-errors');

  // Stripe
  let stripe, elements, card;
  if (typeof Stripe !== 'undefined') {
    stripe = Stripe('pk_test_51ReCXUPuzhCkvgCrZXNc6qLnU4Ns56Hzlmk8UxUrLowiDVBF9G5z9RaFsZOpjY5cWeQcCJsksTKEqoHbXenMlnhu00U0taWNjE');
    elements = stripe.elements();
    card = elements.create('card');
    card.mount('#card-element');
    card.on('change', (event) => {
      cardErrors.textContent = event.error?.message || '';
    });
  }

  // Ouvre la modale et fetch les cartes
  if (cardPaymentBtn && cardListModal) {
    cardPaymentBtn.addEventListener('click', async () => {
      cardListModal.classList.remove('hidden');
      await fetchAndDisplayCards();
    });
  }
  // Ferme la modale
  [cardListOverlay, closeCardListButton].forEach(btn => {
    if (btn) btn.addEventListener('click', () => cardListModal.classList.add('hidden'));
  });
  document.addEventListener('keydown', function(e) {
    if (e.key === 'Escape' && !cardListModal.classList.contains('hidden')) {
      cardListModal.classList.add('hidden');
    }
  });

  // Affiche le formulaire d'ajout
  if (addCardBtn && addCardFormContainer) {
    addCardBtn.addEventListener('click', () => {
      addCardFormContainer.classList.remove('hidden');
    });
  }
  // Annule ajout carte
  if (cancelAddCardBtn && addCardFormContainer) {
    cancelAddCardBtn.addEventListener('click', () => {
      addCardFormContainer.classList.add('hidden');
      cardErrors.textContent = '';
    });
  }

  // Soumission ajout carte (Stripe)
  if (addCardForm) {
    addCardForm.addEventListener('submit', async function(e) {
      e.preventDefault();
      if (!stripe || !card) {
        cardErrors.textContent = 'Stripe non initialisé';
        return;
      }
      const cardHolderName = document.getElementById('card-holder-name').value;
      const { error, paymentMethod } = await stripe.createPaymentMethod({
        type: 'card',
        card: card,
        billing_details: { name: cardHolderName }
      });
      if (error) {
        cardErrors.textContent = error.message;
        return;
      }
      try {
        const response = await fetch('/paymentCard', {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({ paymentMethodId: paymentMethod.id })
        });
        const result = await response.json();
        if (!response.ok) throw new Error(result.message || 'Erreur lors de l\'ajout');
        addCardForm.reset();
        card.clear();
        addCardFormContainer.classList.add('hidden');
        cardErrors.textContent = '';
        await fetchAndDisplayCards();
        alert('Carte ajoutée !');
      } catch (err) {
        cardErrors.textContent = err.message || 'Erreur lors de l\'ajout';
      }
    });
  }

  // Fetch et affichage des cartes
  async function fetchAndDisplayCards() {
    cardList.innerHTML = '<p>Chargement...</p>';
    try {
      const res = await fetch('/paymentCard/me', { method: 'GET' });
      if (!res.ok) throw new Error('Impossible de récupérer les cartes');
      const cards = await res.json();
      if (!Array.isArray(cards) || cards.length === 0) {
        cardList.innerHTML = '<p>Aucune carte enregistrée.</p>';
        return;
      }
      cardList.innerHTML = cards.map(card => `
        <div class="carte${card.isDefault ? ' default-card' : ''}">
          <span>**** **** **** ${card.lastDigit} - ${card.brand.toUpperCase()} (exp. ${String(card.expMonth).padStart(2, '0')}/${card.expYear})
            ${card.isDefault ? '<span class="default-badge">Principale</span>' : ''}
          </span>
          <div class="carte-actions">
            ${!card.isDefault ? `<button class="form-button" id="cardFormButtons" data-default="${card.idPaymentCard}">Définir par défaut</button>` : ''}
            <button class="form-button delete-btn" data-delete="${card.idPaymentCard}">Supprimer</button>
          </div>
        </div>
      `).join('');
      // Ajout listeners pour les boutons
      cardList.querySelectorAll('[data-default]').forEach(btn => {
        btn.addEventListener('click', async e => {
          const id = btn.getAttribute('data-default');
          await setDefaultCard(id);
        });
      });
      cardList.querySelectorAll('[data-delete]').forEach(btn => {
        btn.addEventListener('click', async e => {
          const id = btn.getAttribute('data-delete');
          if (confirm('Supprimer cette carte ?')) await deleteCard(id);
        });
      });
    } catch (err) {
      cardList.innerHTML = `<p style="color:red">${err.message || 'Erreur lors du chargement des cartes.'}</p>`;
    }
  }

  // PUT: set default
  async function setDefaultCard(id) {
    try {
      const res = await fetch(`/paymentCard/default/set/${id}`, { method: 'PUT' });
      if (!res.ok) throw new Error('Erreur lors de la mise à jour');
      await fetchAndDisplayCards();
      alert('Carte définie comme principale.');
    } catch (err) {
      alert(err.message || 'Erreur lors de la mise à jour.');
    }
  }
  // DELETE: remove card
  async function deleteCard(id) {
    try {
      const res = await fetch(`/paymentCard/${id}`, { method: 'DELETE' });
      if (!res.ok) throw new Error('Erreur lors de la suppression');
      await fetchAndDisplayCards();
      alert('Carte supprimée.');
    } catch (err) {
      alert(err.message || 'Erreur lors de la suppression.');
    }
  }
});


