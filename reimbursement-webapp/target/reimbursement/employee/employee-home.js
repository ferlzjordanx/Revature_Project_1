import {appURL} from '../environment.js';

const getReimbursements = (employeeId) => {
    return fetch(`${appURL}/reimbursements?employee-id=${employeeId}`, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json'
        }
    });
}

document.addEventListener("DOMContentLoaded", () => {
    const messageParagraph = document.getElementById("messageParagraph");
    const newReimbursementButton = document.getElementById("newReimbursementButton");

    const url = new URL(window.location.href);
    var employeeId = url.searchParams.get("id");

    newReimbursementButton.addEventListener("click", (event) => {
        event.preventDefault();
        messageParagraph.textContent = "New reimbursement...";
        window.location.href = `${appURL}/reimbursement/new-reimbursement.html?employee-id=${employeeId}`;
    });

    editUserButton.addEventListener("click", (event) => {
        event.preventDefault();
        messageParagraph.textContent = "Editing user...";
        window.location.href = `${appURL}/user/edit-user.html?id=${employeeId}`;
    });

    logoutButton.addEventListener("click", (event) => {
        event.preventDefault();
        messageParagraph.textContent = "Logging out...";
        window.location.href = `${appURL}`;
    });

    getReimbursements(employeeId).then(resp => {
        resp.json().then((json) => {
            if (resp.status === 200) {
                messageParagraph.textContent = "Fetch successful.";
                let tableColumns = "";

                for (const reimb of json) {
                    tableColumns += `<tr><td>${reimb.id}</td> <td>${reimb.amount}</td> <td>${reimb.status}</td></tr>`;
                }

                const reimbursementsTBody = document.getElementById("reimbursementsTBody");
                reimbursementsTBody.innerHTML = tableColumns;
            } else {
                messageParagraph.textContent = json.error;
            }
        });
    });
});
