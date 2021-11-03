import { appURL } from '../environment.js';

const getUsers = () => {
    return fetch(`${appURL}/users`, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json'
        }
    });
}

const resetPassword = (userId) => {
    return fetch(`${appURL}/users/reset-password?id=${userId}`, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json'
        }
    });
}

document.addEventListener("DOMContentLoaded", () => {
    const messageParagraph = document.getElementById("messageParagraph");
    const url = new URL(window.location.href);
    var userId = url.searchParams.get("id");

    resetPassword(userId).then(resp => {
        resp.json().then((json) => {
            if (resp.status === 200) {
                messageParagraph.textContent = "Password reset successful.";
                
                getUsers().then(resp => {
                    resp.json().then((json) => {
                        if (resp.status === 200) {
                            messageParagraph.textContent = "Password reset successful.";
                            let tableColumns = "";

                            for (const user of json) {
                                tableColumns += `<tr><td>${user.id}</td> <td>${user.username}</td> <td>${user.name}</td> <td>${user.jobTitle}</td> <td>${user.email}</td> <td> <a href="${appURL}/user/reset-password.html?id=${user.id}">Reset</a></td> </tr>`;
                            }

                            const reimbursementsTBody = document.getElementById("reimbursementsTBody");
                            reimbursementsTBody.innerHTML = tableColumns;
                        } else {
                            messageParagraph.textContent = json.error;
                        }
                    });
                });
            } else {
                messageParagraph.textContent = json.error;
            }
        });
    });
});
