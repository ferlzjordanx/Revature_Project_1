import {appURL} from './environment.js';

const submitLogin = (loginRequest) => {
    return fetch(`${appURL}/login`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(loginRequest)
    });
}

document.addEventListener("DOMContentLoaded", () => {
    const messageParagraph = document.getElementById("messageParagraph");
    const usernameInput = document.getElementById("usernameInput");
    const passwordInput = document.getElementById("passwordInput");
    const loginButton = document.getElementById("loginButton");

    loginButton.addEventListener("click", (event) => {
        event.preventDefault();
        messageParagraph.textContent = "Sending login information...";

        const loginRequest = {
            username: usernameInput.value,
            password: passwordInput.value
        };

        submitLogin(loginRequest).then(resp => {
            resp.json().then((json) => {
                if (resp.status === 200) {
                    messageParagraph.textContent = "Login successful.";

                    if (json.jobTitle == 'EMPLOYEE') {
                        window.location.href = `${appURL}/user/employee-home.html?id=${json.id}`;
                    } else {
                        window.location.href = `${appURL}/user/manager-home.html?id=${json.id}`;
                    }
                } else {
                    messageParagraph.textContent = json.error;
                }
            });
        });
    });
});
