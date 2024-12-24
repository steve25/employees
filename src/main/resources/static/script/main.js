const toggleButton = document.getElementById('toggle-attendance-form');
const closeButton = document.getElementById('close-button');
const attendanceContainer = document.getElementById('add-attendance-container');
const deleteButtons = document.querySelectorAll('.deleteButton');
const modals = document.querySelectorAll('.modal-overlay');
const cancelButtons = document.querySelectorAll('.cancelButton');


deleteButtons.forEach(button => {
    button.addEventListener('click', () => {

        const attendanceId = button.getAttribute('data-id');

        const modal = document.querySelector(`.modal-overlay[data-id='${attendanceId}']`);
        modal.style.display = 'flex';

        const cancelButton = modal.querySelector('.cancelButton');
        cancelButton.addEventListener('click', () => {
            modal.style.display = 'none';
        });
    });
});

if (toggleButton ) {
    toggleButton.addEventListener('click', () => {
        if (attendanceContainer.classList.contains('active')) {
            closeForm();
        } else {
            attendanceContainer.style.display = 'block';
            setTimeout(() => attendanceContainer.classList.add('active'), 10);
        }
    });
}

if (closeButton) {
    closeButton.addEventListener('click', () => {
        closeForm();
    });
}

const closeForm = () => {
    attendanceContainer.classList.remove('active');
    setTimeout(() => {
        attendanceContainer.style.display = 'none';
    }, 500);
};
