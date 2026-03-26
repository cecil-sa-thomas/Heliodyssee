document.addEventListener('DOMContentLoaded', function() {
    const openBtn = document.getElementById('userBookingToggle');
    const modal = document.getElementById('userBookingModal');
    const closeBtn = document.getElementById('closeUserBookingModal');
    const bookingsList = document.getElementById('userUpcomingBookingsList');

    if (openBtn && modal && closeBtn && bookingsList) {
        openBtn.addEventListener('click', async () => {

            // Charger les bookings à venir
            const res = await fetch(`/booking/upcoming/user`);
            if (!res.ok) {
                bookingsList.innerHTML = "<div>Erreur lors du chargement des vols.</div>";
                modal.classList.remove('hidden');
                return;
            }
            const bookings = await res.json();
            if (!bookings.length) {
                bookingsList.innerHTML = "<div>Aucun vol à venir.</div>";
            } else {
                bookingsList.innerHTML = bookings.map(b => `
                    <div class="booking-card">
                        <div><b>Vol :</b> ${b.numFlight}</div>
                        <div><b>Départ :</b> ${b.departureSpaceportName} (${b.departurePlanetName})</div>
                        <div><b>Arrivée :</b> ${b.arrivalSpaceportName} (${b.arrivalPlanetName})</div>
                        <div><b>Date départ :</b> ${new Date(b.dateDeparture).toLocaleString('fr-FR')}</div>
                        <div><b>Date arrivée :</b> ${new Date(b.dateArrival).toLocaleString('fr-FR')}</div>
                    </div>
                `).join('');
            }
            modal.classList.remove('hidden');
        });

        closeBtn.addEventListener('click', () => modal.classList.add('hidden'));
        modal.addEventListener('click', e => { if (e.target === modal) modal.classList.add('hidden'); });
    }
});