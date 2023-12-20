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
            const performanceId = button.getAttribute("data-performance-id");
            openEditModal(button, performanceId);
//            const form = document.querySelector(".artist-edit-form");
//            const actionUrl = form.getAttribute("action");
//            const updatedActionUrl = actionUrl.replace("{performanceId}", performanceId);
//            form.setAttribute("action", updatedActionUrl);
        })
    })
})

const modal = document.querySelector('.modal');
const overlay = document.querySelector('.overlay');

function openEditModal(button, performanceId) {


    const editButtonInModal = document.querySelector('.show-modal .edit-btn');
    const form = document.querySelector('.performance-edit');

    button.setAttribute('data-performance-id', performanceId);

     const actionUrl = form.getAttribute('action');
     const updatedActionUrl = actionUrl.replace('{performanceId}', performanceId);
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


