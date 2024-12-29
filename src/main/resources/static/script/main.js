const toggleButton = document.getElementById('toggle-add-form');
const closeButton = document.getElementById('close-button');
const addFormContainer = document.querySelector('.add-form-container');
const deleteButtons = document.querySelectorAll('.deleteButton');
const modal = document.querySelector('.modal-overlay');
const cancelButton = document.querySelector('.modal .cancelButton');

if (deleteButtons) {
    deleteButtons.forEach((button) => {
        button.addEventListener('click', () => {
            const id = button.getAttribute('data-id');
            const endpoint = button.getAttribute('data-endpoint');
            const inputId = document.querySelector(`.modal-overlay input[name='id']`);
            const inputForm = document.querySelector('.modal-overlay form');

            console.log(endpoint)
            inputForm.action = `${endpoint}/delete`;
            inputId.value = id;
            modal.style.display = 'flex';
        });
    });
}

if (cancelButton) {
    cancelButton.addEventListener('click', () => {
        modal.style.display = 'none';
        console.log(document.querySelector(`.modal-overlay input[name='id'].value`));
    });
}

if (toggleButton) {
    toggleButton.addEventListener('click', () => {
        if (addFormContainer.classList.contains('active')) {
            closeForm();
        } else {
            addFormContainer.style.display = 'block';
            setTimeout(() => addFormContainer.classList.add('active'), 10);
        }
    });
}

if (closeButton) {
    closeButton.addEventListener('click', () => {
        closeForm();
    });
}

const closeForm = () => {
    addFormContainer.classList.remove('active');
    setTimeout(() => {
        addFormContainer.style.display = 'none';
    }, 500);
};
