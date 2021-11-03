import {appURL} from '../environment.js';

function resolveReimbursement(reimbId, managerId, status) {
    fetch(`${appURL}/reimbursements/resolve?id=${reimbId}&manager-id=${managerId}&status=${status}`, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json'
        }
    }).then(resp => {
        resp.json().then((json) => {
            if (resp.status === 200) {
                messageParagraph.textContent = "Reimbursement resolved.";
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
}

document.addEventListener("DOMContentLoaded", () => {
    const messageParagraph = document.getElementById("messageParagraph");
    const newUserButton = document.getElementById("newUserButton");
    const editUserButton = document.getElementById("editUserButton");
    const logoutButton = document.getElementById("logoutButton");

    const url = new URL(window.location.href);
    var reimbId = url.searchParams.get("id");
    var managerId = url.searchParams.get("manager-id");
    var status = url.searchParams.get("status");

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

    logoutButton.addEventListener("click", (event) => {
        event.preventDefault();
        messageParagraph.textContent = "Logging out...";
        window.location.href = `${appURL}`;
    });

    resolveReimbursement(reimbId, managerId, status);
});
