import {appURL} from '../environment.js';

const getUsers = () => {
    return fetch(`${appURL}/users`, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json'
        }
    });
}

document.addEventListener("DOMContentLoaded", () => {
    const messageParagraph = document.getElementById("messageParagraph");
    
    getUsers().then(resp => {
        resp.json().then((json) => {
            if (resp.status === 200) {
                messageParagraph.textContent = "Fetch successful.";
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
});
