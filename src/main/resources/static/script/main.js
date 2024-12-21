const toggleButton = document.getElementById('toggle-attendance-form');
const closeButton = document.getElementById('close-button');
const attendanceContainer = document.getElementById('add-attendance-container');
const tableRow = document.querySelectorAll('.table-row');

if (tableRow) {
    tableRow.forEach(row => {
        const id = row.getAttribute('data-attendance-id');

        const hourButtons = row.querySelectorAll('.hour-button')
        const deleteButton = row.querySelector('.delete-button')

        hourButtons[0].addEventListener('click', () => {
            console.log('inc ' + id)
            axiosModifyHours(id, 1)
        })

        hourButtons[1].addEventListener('click', () => {
            console.log('dec ' + id)
            axiosModifyHours(id, -1)
        })

        deleteButton.addEventListener('click', () => {
            const modal = document.querySelector(`.modal-overlay[data-attendance-id='${id}']`);
            modal.style.display = 'flex';

            const cancelButton = modal.querySelector('.cancelButton');
            cancelButton.addEventListener('click', () => {
                modal.style.display = 'none';
            });
        })
    })
}

if (toggleButton) {
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

const axiosModifyHours = (id, hours) => {
    axios.post(`/api/modifyhours/${id}/${hours}`)
        .then(response => {
            console.log('Post created:', response.data);
        })
        .catch(error => {
            console.error('Error:', error);
        });
}