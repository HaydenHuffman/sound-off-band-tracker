

document.addEventListener('DOMContentLoaded', () => {

    const slInput = document.querySelector('.datepicker');
    flatpickr('.datepicker', {
        onChange: function(selectedDates, dateStr, instance) {
            slInput.value = dateStr;
        }
    })
})


// function submitForm() {
//     let form = document.querySelector('.artist-edit')
//     form.action = "/users/" + userId + "/" + artistId;
//     form.submit();
//     }

// document.querySelector('.add-performance-button').addEventListener('click', addPerformance);

function getDayOfWeekScore(dateString) {
    const date = new Date(dateString);
    const daysOfWeekScore = [1, 1.3, 1.5, 1.4, 1.25, .67, .67]; // each score here represents a day of the week. Weekends are easier to book therefore receive a lower score
    const dayIndex = date.getDay();
    const dayOfWeekScore = daysOfWeekScore[dayIndex];
    return dayOfWeekScore;
}


document

// function addPerformance(event) {
// let date = document.querySelector('#performance-date').value
// let formattedDate = formatDateForJava(date)
// let attendance = document.querySelector('#performance-attendance').value
//         console.log(artistId)
//         let perfScore = attendance * getDayOfWeekScore(date)
//         console.log(perfScore)
//         let performance = {
//             "date": formattedDate,
//             "attendance": attendance,
//             "perfScore": perfScore,
//             "artistId": artistId
//         }
//         fetch(artistId + '/add',
//                     {
//                     method: 'POST',
//                     headers: {
//                     'Content-Type': 'application/json'
//                     },
//                     body: JSON.stringify(performance),
//                     responseType: 'json'
//                     })
//         .then((response) => {
//             if (!response.ok){
//                 throw new Error('Network response was not ok')
//             }
//             return response.json();
//             })
//             .finally( () => {
//                 date.value = ''
//                 attendance = ''
//             })
//         }

//  $(document).ready(function() {
//     $("#performance-date").datepicker();
//   });


// function formatDateForJava(dateString) {
//     const [month, day, year] = dateString.split('/');
//     const formattedDate = `${year}-${month.padStart(2, '0')}-${day.padStart(2, '0')}`;
//     return formattedDate;
// }


