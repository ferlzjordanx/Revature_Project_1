import {appURL} from '../environment.js';

const getReimbursements = () => {
    return fetch(`${appURL}/reimbursements`, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json'
        }
    });
}

document.addEventListener("DOMContentLoaded", () => {
    const messageParagraph = document.getElementById("messageParagraph");
    const newUserButton = document.getElementById("newUserButton");
    const editUserButton = document.getElementById("editUserButton");
    const listUsersButton = document.getElementById("listUsersButton");
    const logoutButton = document.getElementById("logoutButton");

    const url = new URL(window.location.href);
    var managerId = url.searchParams.get("id");

    newUserButton.addEventListener("click", (event) => {
        event.preventDefault();
        messageParagraph.textContent = "New user...";
        window.location.href = `${appURL}/user/new-user.html?manager-id=${managerId}`;
    });

    editUserButton.addEventListener("click", (event) => {
        event.preventDefault();
        messageParagraph.textContent = "Editing user...";
        window.location.href = `${appURL}/user/edit-user.html?id=${managerId}&manager-id=${managerId}`;
    });

    listUsersButton.addEventListener("click", (event) => {
        event.preventDefault();
        messageParagraph.textContent = "Listing users...";
        window.location.href = `${appURL}/user/users-list.html`;
    });

    logoutButton.addEventListener("click", (event) => {
        event.preventDefault();
        messageParagraph.textContent = "Logging out...";
        window.location.href = `${appURL}`;
    });

    getReimbursements().then(resp => {
        resp.json().then((json) => {
            if (resp.status === 200) {
                messageParagraph.textContent = "Fetch successful.";
                let tableColumns = "";

                for (const reimb of json) {
                    tableColumns += `<tr><td>${reimb.id}</td> <td>${reimb.amount}</td> <td>${reimb.status}</td> <td>${reimb.employeeUsername}</td> <td>${reimb.managerUsername}</td> <td> <a href="${appURL}/reimbursement/resolve-reimbursement.html?id=${reimb.id}&manager-id=${managerId}&status=2">Resolve</a> | <a href="${appURL}/reimbursement/resolve-reimbursement.html?id=${reimb.id}&manager-id=${managerId}&status=3">Deny</a> </td> </tr>`;
                }

                const reimbursementsTBody = document.getElementById("reimbursementsTBody");
                reimbursementsTBody.innerHTML = tableColumns;
            } else {
                messageParagraph.textContent = json.error;
            }
        });
    });
});
