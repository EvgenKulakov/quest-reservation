document.addEventListener('DOMContentLoaded', function () {

    const currentDate = document.querySelector('#current-date')
    const chooseButton = document.querySelector('#choose-date')
    const startDateText = currentDate.textContent
    let selectedDateText = null

    const dateButton = document.querySelector('#date-button')
    dateButton.addEventListener('click', function () {
        document.querySelector('#input-date').focus()
        chooseButton.style.display = 'none'
    })

    const picker = new AirDatepicker('#input-date', {
        position: 'bottom right',
        buttons: [{
            content() {
                return 'Выбрать'
            },
            onClick() {
                document.querySelector('#date-form').submit()
            }
        }],
        onSelect(selectedDate, datepicker) {
            const date = selectedDate.date
            const day = date.getDate().toString().padStart(2, '0')
            const month = (date.getMonth() + 1).toString().padStart(2, '0')
            const year = date.getFullYear()
            const dayOfWeek = date.toLocaleDateString('ru-RU', {weekday: 'long'})
            selectedDateText = `${day}-${month}-${year} (${dayOfWeek})`
            currentDate.innerHTML = selectedDateText
        },
        onHide(isFinished) {
            if (selectedDateText && isFinished) {
                currentDate.innerHTML = startDateText
                chooseButton.textContent = 'Выбрать ' + selectedDateText
                chooseButton.style.display = 'inline'
            }
        }
    })
})
