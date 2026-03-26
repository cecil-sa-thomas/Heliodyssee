document.getElementById('setting-btn').addEventListener('click', async () => {
    try {
        const response = await fetch('/users/me', {
            cache: 'no-store'
        });
        
        if (!response.ok) throw new Error('Échec de la récupération des données utilisateur');

        const data = await response.json();

        // Pré-remplir le formulaire
        document.getElementById('firstName').value = data.firstName || '';
        document.getElementById('lastName').value = data.lastName || '';
        document.getElementById('email').value = data.email || '';
        document.getElementById('phone').value = data.phone || '';

        // Afficher le conteneur
        openForm(document.getElementById('settingsContainer'));
    } catch (error) {
        console.error('Erreur lors du chargement des paramètres :', error);
        alert('Impossible de charger vos informations.');
    }
});

// Ferme le formulaire
document.getElementById('closeSettingsButton').addEventListener('click', () => {
    closeForm(settingsContainer);
});
// Affiche un message si les mots de passe ne correspondent pas
function checkPasswordsMatch() {
    if (password.value && confirmation.value && password.value !== confirmation.value) {
        mismatchMessage.style.display = "block";
        confirmation.style.borderColor = "red";
    } else {
        mismatchMessage.style.display = "none";
        confirmation.style.borderColor = "";
    }
}
password.addEventListener("input", checkPasswordsMatch);
confirmation.addEventListener("input", checkPasswordsMatch);

// Soumission du formulaire
settingsForm.addEventListener('submit', async (e) => {
    e.preventDefault();
    if (!oldPassword.value) {
        alert("Veuillez entrer votre mot de passe actuel.");
        return;
    }
    if (password.value && password.value !== confirmation.value) {
        alert("Les mots de passe ne correspondent pas.");
        return;
    }
    if (password.value && password.value.length < 6) {
        alert("Le nouveau mot de passe est trop court.");
        return;
    }
    const formData = {
        firstName: document.getElementById('firstName').value,
        lastName: document.getElementById('lastName').value,
        email: document.getElementById('email').value,
        phone: document.getElementById('phone').value,
        password: password.value,
        oldPassword: oldPassword.value
    };
    try {
        const response = await fetch('/users/test-me', {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(formData)
        });
        if (!response.ok) throw new Error("Erreur lors de la mise à jour.");
        const updatedUser = await response.json();

        // Met à jour le formulaire avec les nouvelles infos
        document.getElementById('firstName').value = updatedUser.firstName || '';
        document.getElementById('lastName').value = updatedUser.lastName || '';
        document.getElementById('email').value = updatedUser.email || '';
        document.getElementById('phone').value = updatedUser.phone || '';
        password.value = '';
        confirmation.value = '';
        oldPassword.value = '';

        alert("Modifications enregistrées !");
        closeForm(settingsContainer);
    } catch (error) {
        console.error("Erreur de mise à jour :", error);
        alert("Échec de la mise à jour : " + error.message);
    }
});

