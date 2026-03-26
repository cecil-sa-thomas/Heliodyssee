const toggleBtn = document.getElementById('userMenuToggle');
const dropdown = document.getElementById('userDropdown');

// Ouvre/ferme le menu au clic sur le bouton
toggleBtn.addEventListener('click', (e) => {
  e.stopPropagation();
  dropdown.classList.toggle('open');

  // Si on ferme le menu, retire le focus visuel du bouton
  if (!dropdown.classList.contains('open')) {
    toggleBtn.blur();
  }
});

// Referme le menu si clic ailleurs dans la page
document.addEventListener('click', (e) => {
  if (
    dropdown.classList.contains('open') &&
    !toggleBtn.contains(e.target) &&
    !dropdown.contains(e.target)
  ) {
    dropdown.classList.remove('open');
    toggleBtn.blur(); // Retire aussi le focus si on ferme en cliquant ailleurs
  }
});
