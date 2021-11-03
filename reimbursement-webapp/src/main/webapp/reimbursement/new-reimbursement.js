import {appURL} from '../environment.js';

const submitReimbursement = (reimbursement) => {
    return fetch(`${appURL}/reimbursements`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(reimbursement)
    });
}

document.addEventListener("DOMContentLoaded", () => {
    const messageParagraph = document.getElementById("messageParagraph");
    const amountInput = document.getElementById("amountInput");
    const sendButton = document.getElementById("sendButton");

    const url = new URL(window.location.href);
    var employeeId = url.searchParams.get("employee-id");

    sendButton.addEventListener("click", (event) => {
        event.preventDefault();
        messageParagraph.textContent = "Sending reimbursement information...";

        const reimbursement = {
            amount: amountInput.value,
            status: 'PENDING',
            employeeId: employeeId
        };

        submitReimbursement(reimbursement).then(resp => {
            resp.json().then((json) => {
                if (resp.status === 200) {
                    messageParagraph.textContent = "Reimbursement request successful.";
                    window.location.href = `${appURL}/user/employee-home.html?id=${employeeId}`;
                } else {
                    messageParagraph.textContent = json.error;
                }
            });
        });
    });
});
