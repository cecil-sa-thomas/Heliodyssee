// login-settings-box.js

// Récupération des conteneurs
const inscriptionContainer = document.getElementById('inscriptionContainer');
const connexionContainer = document.getElementById('connexionContainer');
const forgotContainer = document.getElementById('forgotContainer');
const settingsContainer = document.getElementById('settingsContainer');
const createBookingContainer = document.getElementById('createBookingContainer');

// Récupération des boutons d'ouverture
const inscriptionBtn = document.getElementById('inscriptionBtn');
const connexionBtn = document.getElementById('connexionBtn');
const forgotPassword = document.getElementById('forgotPassword');
const settingsBtn = document.getElementById('setting-btn');

// Récupération des boutons de fermeture
const closeFormButton = document.getElementById('closeFormButton');
const closeConnexionButton = document.getElementById('closeConnexionButton');
const closeForgotButton = document.getElementById('closeForgotButton');
const closeSettingsButton = document.getElementById('closeSettingsButton');
const closeBookingButton = document.getElementById('closeBookingFormButton');

console.log("Script inscription chargé !");

// Outils génériques d'ouverture / fermeture
function openForm(container) {
    [inscriptionContainer, connexionContainer, forgotContainer, settingsContainer, createBookingContainer].forEach(c => {
        if (c && c !== container) closeForm(c);
    });
    container.classList.remove('hidden');
    container.classList.add('fade-in');
}

function closeForm(container) {
    if (!container) return; // évite l'erreur si le conteneur est null
    container.classList.add('fade-out');
    setTimeout(() => {
        container.classList.add('hidden');
        container.classList.remove('fade-in', 'fade-out');
    }, 300);
}

// OUVERTURES DES FORMULAIRE D'AUTHENTIFICATION
if (inscriptionBtn) inscriptionBtn.addEventListener('click', () => openForm(inscriptionContainer));
if (connexionBtn) connexionBtn.addEventListener('click', () => openForm(connexionContainer));
if (forgotPassword) forgotPassword.addEventListener('click', e => {
    e.preventDefault();
    openForm(forgotContainer);
});
if (settingsBtn) settingsBtn.addEventListener('click', () => openForm(settingsContainer));

// FERMETURES DES FORMULAIRES (CORRECTION ICI) :
if (closeFormButton) closeFormButton.addEventListener('click', () => closeForm(inscriptionContainer));
if (closeConnexionButton) closeConnexionButton.addEventListener('click', () => closeForm(connexionContainer)); // ✅ Corrigé
if (closeForgotButton) closeForgotButton.addEventListener('click', () => closeForm(forgotContainer));
if (closeSettingsButton) closeSettingsButton.addEventListener('click', () => closeForm(settingsContainer));
if (closeBookingButton) closeBookingButton.addEventListener('click', () => closeForm(createBookingContainer));

[inscriptionContainer, connexionContainer, forgotContainer, settingsContainer, createBookingContainer].forEach(container => {
    if (container) {
        container.addEventListener('click', e => {
            if (e.target === container) closeForm(container);
        });
    }
});

document.addEventListener('keydown', e => {
    if (e.key === 'Escape') {
        [inscriptionContainer, connexionContainer, forgotContainer, settingsContainer, createBookingContainer].forEach(closeForm);
    }
});

//INSCRIPTION :
document.getElementById('inscriptionForm').addEventListener('submit', async (e) => {
    e.preventDefault();

    // Récupérer les champs du formulaire
    const nom = e.target.elements['nom'].value;
    const prenom = e.target.elements['prenom'].value;
    const email = e.target.elements['email'].value;
    const telephone = e.target.elements['telephone'].value;
    const password = e.target.elements['password'].value;
    const confirmation = e.target.elements['confirmation'].value;
    const dateNaissance = e.target.elements['dateNaissance'].value;

    // Vérifier si les mots de passe correspondent
    if (password !== confirmation) {
        alert("Les mots de passe ne correspondent pas.");
        return;
    }

    // Construire l'objet conforme à ton DTO
    const userData = {
        lastName: nom,
        firstName: prenom,
        email: email,
        dateOfBirth: dateNaissance, // ← maintenant dynamique
        phone: telephone,
        password: password,
        oldPassword: ""
    };

    try {
        const response = await fetch('/users/register', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(userData)
        });

        if (!response.ok) {
            const errorText = await response.text();
            console.error("Erreur : ", errorText);
            alert("Erreur lors de l'inscription.");
            return;
        }

        const data = await response.json();
        console.log("Utilisateur créé :", data);
        alert("Inscription réussie !");
        document.getElementById('inscriptionContainer').classList.add('hidden');

    } catch (error) {
        console.error("Erreur réseau :", error);
        alert("Erreur réseau ou serveur.");
    }
});