let usernameTextbox = document.querySelector('#email')
let lastCheckedValue = ""
usernameTextbox.addEventListener('blur', () => {
    const currentEmailValue = usernameTextbox.value;
    if (currentEmailValue !== lastCheckedValue) {
        lastCheckedValue = currentEmailValue;

        const user = {
            'email': usernameTextbox.value,
        }
        fetch('/users/exists', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(user)
        })
            .then((responseEntity) => responseEntity.json())
            .then((data) => {
                if (data === true) {
                    alert('User with the email address already exists.')
                }
            })
    }
})
document.getElementById('registrationForm').addEventListener('submit', function (event) {
    var password = document.getElementById('password').value;
    var confirmPassword = document.getElementById('confirmPassword').value;

    if (password !== confirmPassword) {
        alert('Passwords do not match!');
        event.preventDefault(); // Prevent form submission
    }
});
