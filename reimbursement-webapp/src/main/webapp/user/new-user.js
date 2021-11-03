import { appURL } from '../environment.js';

const submitUser = (user) => {
    return fetch(`${appURL}/users`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(user)
    });
}

document.addEventListener("DOMContentLoaded", () => {
    const messageParagraph = document.getElementById("messageParagraph");
    const usernameInput = document.getElementById("usernameInput");
    const passwordInput = document.getElementById("passwordInput");
    const nameInput = document.getElementById("nameInput");
    const emailInput = document.getElementById("emailInput");
    const jobTitleInput = document.getElementById("jobTitleInput");
    const sendButton = document.getElementById("sendButton");

    const url = new URL(window.location.href);
    var userId = url.searchParams.get("id");
    var managerId = url.searchParams.get("manager-id");

    sendButton.addEventListener("click", (event) => {
        event.preventDefault();
        messageParagraph.textContent = "Sending user information...";

        const user = {
            username: usernameInput.value,
            password: passwordInput.value,
            name: nameInput.value,
            email: emailInput.value,
            jobTitle: jobTitleInput.value,
        };

        submitUser(user).then(resp => {
            resp.json().then((json) => {
                if (resp.status === 200) {
                    messageParagraph.textContent = "User request successful.";

                    if (managerId == undefined || managerId == null) {
                        window.location.href = `${appURL}/user/employee-home.html?id=${userId}`;
                    } else {
                        window.location.href = `${appURL}/user/manager-home.html?id=${managerId}`;
                    }
                } else {
                    messageParagraph.textContent = json.error;
                }
            });
        });
    });
});
