const firstPage = document.querySelector('#firstPage')
const secondPage = document.querySelector('#secondPage')
const onlySecondPageError = document.querySelector('#onlySecondPageError')

const slotlistForm = document.querySelector('#slotlistForm')
const oneDay = document.querySelector('.oneDay')
const titleDay = document.querySelector('.titleDay')
const oneSlotlist = document.querySelector('.oneSlotlist')
const oneRow = document.querySelector('.oneRow')
const slotlist = JSON.parse(document.querySelector('#slotlistJson').value)

const radioButtons = document.querySelectorAll('#radio-slotlist input');
const onward = document.querySelector('#onward')
const back = document.querySelector('#back')

let copyBuffer = null


const copyPasteFunctional = (titleDay, titleText) => {
    const buttonCopy = titleDay.querySelector('.button-copy')
    const buttonPaste = titleDay.querySelector('.button-paste')

    let caseForTitleText = titleText.toLowerCase()
    if (titleText === 'Среда' || titleText === 'Пятница' || titleText === 'Суббота') {
        caseForTitleText = caseForTitleText.substring(0, caseForTitleText.length - 1) + 'у'
    }

    buttonCopy.addEventListener('click', function (event) {
        buttonCopy.innerText = 'Скопировано'
        const oneDay = event.target.closest('.oneDay')
        const oneSlotlist = oneDay.querySelector('.oneSlotlist')
        copyBuffer = oneSlotlist.cloneNode(true)

        const allButtonsPaste = document.querySelectorAll('.button-paste')
        allButtonsPaste.forEach(button => {
            button.innerText = `Вставить ${caseForTitleText}`
            button.style.visibility = 'visible'
        })
    })

    buttonPaste.addEventListener('click', function (event) {
        const oneDay = event.target.closest('.oneDay')

        const currentSlotlist = oneDay.querySelector('.oneSlotlist')
        const textInName = currentSlotlist.querySelector('#input-time').name
        const currentDay = textInName.substring(textInName.indexOf('.') + 1, textInName.indexOf('['))
        currentSlotlist.remove()

        const copySlotlist = copyBuffer.cloneNode(true)
        const copyAllRows = copySlotlist.querySelectorAll('.oneRow')

        copyAllRows.forEach(copyRow => {

            const inputTime = copyRow.querySelector('#input-time')
            const inputPrice = copyRow.querySelector('#input-price')

            inputTime.name = inputTime.name.replace(/\.(.*?)\[/, `.${currentDay}[`)
            inputPrice.name = inputPrice.name.replace(/\.(.*?)\[/, `.${currentDay}[`)

            copyRow.querySelector('.plus-icon').addEventListener('click', addRow)
            copyRow.querySelector('.del-icon').addEventListener('click', delRow)
        })

        oneDay.appendChild(copySlotlist)
    })
}

const renderRow = (day, index, time, price) => {
    time = time ? time : ''
    price = price ? price : ''

    const timeName = `slotList.${day}[${index}].time`
    const priceName = `slotList.${day}[${index}].price`

    const newRow = oneRow.cloneNode(true)

    newRow.querySelector('#input-time').name = timeName
    newRow.querySelector('#input-time').value = time
    newRow.querySelector('#input-price').name = priceName
    newRow.querySelector('#input-price').value = price

    return newRow
}

const renderOneDay = (titleText, dayList, dayText) => {

    const newTitleDay = titleDay.cloneNode(true)
    newTitleDay.querySelector('.title-text').innerText = titleText

    const newOneSlotlist = oneSlotlist.cloneNode()
    if (dayList) {
        for (let i = 0; i < dayList.length; i++) {
            const newRow = renderRow(`${dayText}`, i, dayList[i].time, dayList[i].price)
            newOneSlotlist.appendChild(newRow)
        }
    } else {
        const newRow = renderRow(`${dayText}`, 0, null, null)
        newOneSlotlist.appendChild(newRow)
    }

    const newOneDay = oneDay.cloneNode()
    newOneDay.appendChild(newTitleDay)
    newOneDay.appendChild(newOneSlotlist)

    slotlistForm.appendChild(newOneDay)

    copyPasteFunctional(newTitleDay, titleText)
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

const delRow = (event) => {
    const row = event.target.closest('.oneRow');
    const parent = event.target.closest('.oneSlotlist')

    if (parent.children.length < 2) return

    row.remove()

    for (let i = 0; i < parent.children.length; i++) {
        const inputTime = parent.children[i].querySelector('#input-time')
        const inputPrice = parent.children[i].querySelector('#input-price')

        inputTime.name = inputTime.name.replace(/\[(\d+)]/, `[${i}]`)
        inputPrice.name = inputPrice.name.replace(/\[(\d+)]/, `[${i}]`)
    }
}

const addRow = (event) => {
    const thisRow = event.target.closest('.oneRow');
    const parent = event.target.closest('.oneSlotlist')
    const countRow = parent.children.length

    if (countRow > 27) return

    const textInName = thisRow.querySelector('#input-time').name
    const day = textInName.substring(textInName.indexOf('.') + 1, textInName.indexOf('['))

    const newRow = renderRow(day, countRow, null, null)

    newRow.querySelector('.plus-icon').addEventListener('click', addRow)
    newRow.querySelector('.del-icon').addEventListener('click', delRow)

    parent.insertBefore(newRow, thisRow.nextSibling);
}

const listenersForAddDeleteRow = () => {
    const delIcons = document.querySelectorAll('.del-icon')
    const plusIcons = document.querySelectorAll('.plus-icon')

    delIcons.forEach(function (delIcon) {
        delIcon.addEventListener('click', delRow)
    })

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
        listenersForAddDeleteRow()
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