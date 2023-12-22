  document.addEventListener('DOMContentLoaded', () => {

        const slInput = document.querySelector('.datepicker');
        flatpickr('.datepicker', {
            onChange: function(selectedDates, dateStr, instance) {
                slInput.value = dateStr;
            }
        })
    })

document.addEventListener("DOMContentLoaded", function() {
    var editButtons = document.querySelectorAll(".edit-btn")
    editButtons.forEach(function(button) {
        button.addEventListener("click", function() {
            const performanceId = this.getAttribute("data-performance-id");
            const artistId = this.getAttribute("data-artist-id");
            const userId = this.getAttribute("data-user-id");
            openEditModal(button, userId, artistId, performanceId);
        })
    })
})

const modal = document.querySelector('.modal');
const overlay = document.querySelector('.overlay');

function openEditModal(button, userId, artistId, performanceId) {
    const form = document.querySelector('.performance-edit');
     const updatedActionUrl = `/users/${userId}/${artistId}/${performanceId}/edit`;
     form.setAttribute('action', updatedActionUrl);
     modal.classList.remove('hidden');
     overlay.classList.remove('hidden');
 }


    document.addEventListener('DOMContentLoaded', function() {
       const dateInput = document.querySelector('.datepicker');
       const attendanceInput = document.querySelector('#performance-attendance');
       const submitButton = document.querySelector('.add-performance-button');

       // Initially disable the submit button
       submitButton.disabled = true;

       // Function to check both inputs
       function updateButtonState() {
           submitButton.disabled = !dateInput.value || !attendanceInput.value;
       }

       // Event listeners for both inputs
       dateInput.addEventListener('input', updateButtonState);
       attendanceInput.addEventListener('input', updateButtonState);
   });


    const btnCloseModal = document.querySelector('.close-modal');
    const btnsOpenModal = document.querySelectorAll('.show-modal');

 function closeModal() {
        modal.classList.add('hidden');
        overlay.classList.add('hidden');
    }

    btnCloseModal.addEventListener('click', closeModal)

    overlay.addEventListener('click', closeModal)


