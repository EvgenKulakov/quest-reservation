document.addEventListener('DOMContentLoaded', function () {

    const dateButton = document.querySelector('#date-button')
    const inputDate = document.querySelector('#input-date')
    const dateForm = document.querySelector('#date-form')
    const backButton = document.querySelector('#backButton')
    const nextButton = document.querySelector('#nextButton')

    dateButton.addEventListener('click', function () {
        inputDate.focus()
    })

    const picker = new AirDatepicker('#input-date', {
        position: 'bottom right',
        buttons: [{
            content() {
                return 'Назад'
            },
            onClick() {
                const yesterday = new Date(backButton.value)
                picker.selectDate(yesterday)
            }
        }, {
            content() {
                return "Вперёд";
            },
            onClick() {
                const tomorrow = new Date(nextButton.value)
                picker.selectDate(tomorrow)
            }
        }],
        onSelect(selectedDate) {
            dateForm.submit()
        },
    })
})
