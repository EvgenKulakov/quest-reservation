
document.addEventListener('DOMContentLoaded', function () {
    new AirDatepicker('#input-data', {
        dateFormat: 'dd-MM-yyyy',
        buttons: [{
            content() {
                return 'Выбрать'
            },
            onClick() {
                document.querySelector('#date-form').submit()
            }
        }],
    })

    const inputData = document.querySelector('#input-data')
    const dateButton = document.querySelector('#date-button')

    dateButton.addEventListener('click', function () {
        inputData.style = 'display: inline'
        inputData.focus()
    })
})
