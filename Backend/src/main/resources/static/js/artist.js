const userId = document.querySelector('.performance-edit').getAttribute('data-user-id');
const artistId = document.querySelector('.performance-edit').getAttribute('data-artist-id');

  document.addEventListener('DOMContentLoaded', () => {

        const slInput = document.querySelector('.datepicker');
        flatpickr('.datepicker', {
            onChange: function(selectedDates, dateStr, instance) {
                slInput.value = dateStr;
            }
        })
    })


function loadPerformances(userId, artistId) {
    fetch(`/users/${userId}/${artistId}/performances`)
    .then(response => {
        console.log(response);
        return response.json();
    })
    .then(performances => {
        console.log(performances);
        displayPerformances(performances);
    })
    .catch(error => console.error('Error fetching performances: error'))
}

function displayPerformances(performances) {
    const tableBody = document.querySelector('.artist-performance-table tbody');
    tableBody.innerHTML = '';

    performances.forEach(performance => {
        const row = document.createElement('tr');
        row.className = 'display-item';

        row.innerHTML = `
        <td class='date-cell'>${performance.date}</td>
        <td class='attendance-cell'>${performance.attendance}</td>
        <td class='sales-cell'>$${performance.perPersonAverage}</td>
        <td>${performance.perfScore}</td>
        <td>
            <button data-performance-id="${performance.performanceId}" class="show-modal edit-btn">Edit</button>
        </td>
        `;
        tableBody.appendChild(row);
    })
}

document.addEventListener('DOMContentLoaded', function() {
    loadPerformances(userId, artistId)
})

document.addEventListener('DOMContentLoaded', function() {
    const tableContainer = document.querySelector('.artist-performance-table');

    tableContainer.addEventListener('click', function(event) {
        if (event.target.classList.contains('edit-btn')) {
            const button = event.target;
            openEditModal(button);
        }
    });
});

const modal = document.querySelector('.modal');
const overlay = document.querySelector('.overlay');

function openEditModal(button) {
    const performanceId = button.getAttribute('data-performance-id');
    fetch(`/users/${userId}/${artistId}/performances/${performanceId}`)
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(performance => {
            console.log(performance);
            populateModal(performance);
        })
        .catch(error => console.error('Error fetching performance details:', error));
}

function populateModal(performance) {
    const modalForm = document.querySelector('.performance-edit');
    const deleteForm = document.querySelector('.delete-form');
    modalForm.querySelector('#performance-date').value = performance.date;
    modalForm.querySelector('#performance-attendance').value = performance.attendance;
    modalForm.querySelector('#performance-sales').value = performance.sales;
    modalForm.querySelector('#performance-id').value = performance.performanceId;
    modalForm.querySelector('#performance-score').value = performance.perfScore;
    modalForm.querySelector('#performance-artist-id').value = performance.artistId;
    modalForm.action = `/users/${userId}/${artistId}/${performance.performanceId}/edit`;
    deleteForm.action = `/users/${userId}/${artistId}/${performance.performanceId}/delete`;

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


