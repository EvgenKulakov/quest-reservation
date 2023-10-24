const firstPage = document.querySelector('#firstPage')
const secondPage = document.querySelector('#secondPage')
const onlySecondPageError = document.querySelector('#onlySecondPageError')

const slotlistForm = document.querySelector('#slotlistForm')
const slotlistEl = document.querySelector('#slotlistJson')
const slotlist = JSON.parse(slotlistEl.value)

const radioButtons = document.querySelectorAll('input[type=radio]');
const onward = document.querySelector('#onward')
const back = document.querySelector('#back')

const renderRow = (day, index, time, price) => {
    time = time ? time : ''
    price = price ? price : ''

    return `<label for="input-time" class="col-2 col-form-label">Время:</label>
                <div class="col-3">
                    <input type="time" class="form-control" id="input-time" 
                        name="slotList.${day}[${index}].time" value="${time}">
                </div>
                
                <label for="input-price" class="col-2-5 col-form-label">Стоимость:</label>
                <div class="col-2-5">
                    <input type="number" min="0" max="100000" class="form-control" id="input-price" 
                        name="slotList.${day}[${index}].price" value="${price}">
                </div>
                
                <div class="col-1">
                    <img class="plus-icon" src="/images/Pictogrammers-Material-Playlist-plus.48.png" 
                        width="24" height="24">
                </div>
                
                <div class="col-1">
                    <img class="del-icon" src="/images/Pictogrammers-Material-Delete-alert.48.png" 
                        width="24" height="24">
                </div>`
}

const renderOneDay = (titleText, dayList, dayText) => {

    const title = document.createElement('div')
    title.innerHTML = `<h4 class="title-form">${titleText}</h4>`
    slotlistForm.appendChild(title)

    const oneDayForm = document.createElement('div')
    oneDayForm.id = 'oneDayForm'

    if (dayList) {
        for (let i = 0; i < dayList.length; i++) {
            const row = document.createElement('div')
            row.className = 'row'
            row.innerHTML = renderRow(`${dayText}`, i, dayList[i].time, dayList[i].price)
            oneDayForm.appendChild(row)
        }
    } else {
        const row = document.createElement('div')
        row.className = 'row'
        row.innerHTML = renderRow(`${dayText}`, 0, null, null)
        oneDayForm.appendChild(row)
    }

    slotlistForm.appendChild(oneDayForm)
}

const renderEqualDays = () => {
    slotlistForm.innerHTML = ''
    renderOneDay('Все дни недели', slotlist.monday, 'monday')
}

const renderWeekdaysWeekends = () => {
    slotlistForm.innerHTML = ''
    renderOneDay('Будние дни', slotlist.monday, 'monday')
    renderOneDay('Выходные дни', slotlist.saturday, 'saturday')
}

const renderDifferentDays = () => {
    slotlistForm.innerHTML = ''
    renderOneDay('Понедельник', slotlist.monday, 'monday')
    renderOneDay('Вторник', slotlist.tuesday, 'tuesday')
    renderOneDay('Среда', slotlist.wednesday, 'wednesday')
    renderOneDay('Четверг', slotlist.thursday, 'thursday')
    renderOneDay('Пятница', slotlist.friday, 'friday')
    renderOneDay('Суббота', slotlist.saturday, 'saturday')
    renderOneDay('Воскресенье', slotlist.sunday, 'sunday')
}

const addOrDelRow = () => {
    const delIcons = document.querySelectorAll('.del-icon')
    const plusIcons = document.querySelectorAll('.plus-icon')

    const delRow = (event) => {
        const row = event.target.closest('.row');
        const parent = event.target.closest('#oneDayForm')

        if (parent.children.length < 2) return

        row.remove()

        for (let i = 0; i < parent.children.length; i++) {
            const inputTime = parent.children[i].querySelector('#input-time')
            const inputPrice = parent.children[i].querySelector('#input-price')

            inputTime.name = inputTime.name.replace(/\[(\d+)]/, `[${i}]`)
            inputPrice.name = inputPrice.name.replace(/\[(\d+)]/, `[${i}]`)
        }
    }

    delIcons.forEach(function (delIcon) {
        delIcon.addEventListener('click', delRow)
    })

    const addRow = (event) => {
        const row = event.target.closest('.row');
        const parent = event.target.closest('#oneDayForm')
        const countRow = parent.children.length

        if (countRow > 27) return

        const newRow = document.createElement('div');
        newRow.className = 'row'
        newRow.innerHTML = row.innerHTML

        const inputTime = newRow.querySelector('#input-time')
        const inputPrice = newRow.querySelector('#input-price')

        inputTime.name = inputTime.name.replace(/\[(\d+)]/, `[${countRow}]`);
        inputPrice.name = inputPrice.name.replace(/\[(\d+)]/, `[${countRow}]`)
        inputTime.value = ''
        inputPrice.value = ''

        const plusIcon = newRow.querySelector('.plus-icon')
        const delIcon = newRow.querySelector('.del-icon')
        plusIcon.addEventListener('click', addRow)
        delIcon.addEventListener('click', delRow)

        parent.insertBefore(newRow, row.nextSibling);
    }

    plusIcons.forEach(function (plusIcon) {
        plusIcon.addEventListener('click', addRow)
    })
}

const switchSlotlist = (radioButton) => {
    if (radioButton.checked) {
        switch (radioButton.value) {
            case 'EQUAL_DAYS':
                renderEqualDays()
                break
            case 'WEEKDAYS_WEEKENDS':
                renderWeekdaysWeekends()
                break
            case 'DIFFERENT_DAYS':
                renderDifferentDays()
                break
        }
        addOrDelRow()
    }
}

const clickBack = () => {
    secondPage.style.display = 'none'
    firstPage.style.display = 'block'
}

const clickOnward = () => {
    firstPage.style.display = 'none'
    secondPage.style.display = 'block'
}

document.addEventListener('DOMContentLoaded', function () {

    back.addEventListener('click', clickBack)
    onward.addEventListener('click', clickOnward)

    if (onlySecondPageError.value === 'true') clickOnward()

    radioButtons.forEach(function (radioButton) {
        switchSlotlist(radioButton)
        radioButton.addEventListener('change', function () {
            switchSlotlist(radioButton)
        })
    })
})