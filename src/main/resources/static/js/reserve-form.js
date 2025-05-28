const body = document.querySelector('body')
const reserveModal = document.querySelector('#reserve-modal')
const reserveContent = document.querySelector('#reserve-content')
const allInput = document.querySelectorAll('input')
const allSelect = document.querySelectorAll('select')
const allTextarea = document.querySelectorAll('textarea')

const questName = document.querySelector('#questName')
const slotDate = document.querySelector('#slotDate')
const slotTime = document.querySelector('#slotTime')
const slotPrice = document.querySelector('#slotPrice')

const selectStatus = document.querySelector('#selectStatus')
const firstName = document.querySelector('#firstName')
const lastName = document.querySelector('#lastName')
const phone = document.querySelector('#phone')
const email = document.querySelector('#email')
const selectCountPersons = document.querySelector('#countPersons')
const adminComment = document.querySelector('#adminComment')

let errorMessages = document.querySelectorAll('.error-message')
const changeStatus = document.querySelector('#change-status')
const changeCountPersons = document.querySelector('#change-count-persons')

const selectedSlot = document.querySelector('#selected-slot')
const errorSlot = document.querySelector('#error-slot')
const saveButton = document.querySelector('#saveButton')
const blockButton = document.querySelector('#blockButton')
//TODO: unBlockButton при нажатии на Enter
const unBlockButton = document.querySelector('#unBlockButton')

async function clickSlot(slotButton) {
    const slot = JSON.parse(slotButton)
    selectedSlot.value = slotButton

    body.style.overflowY = 'hidden'

    questName.textContent = slot.questName
    slotDate.textContent = 'Дата: ' + slot.date
    slotTime.textContent = 'Время: ' + slot.time
    slotPrice.textContent = 'Стоимость: ' + slot.price + '₽'

    if (slot.statusType.name !== 'BLOCK') {
        await showReservation(slot)
    } else {
        showBlockSlot()
    }
}

async function showReservation(slot) {
    const statuses = slot.statuses
    for (let i = 0; i < statuses.length; i++) {
        const newOption = document.createElement("option")
        newOption.value = statuses[i].name
        newOption.textContent = statuses[i].text
        selectStatus.appendChild(newOption);
    }

    for (let i = slot.minPersons; i <= slot.maxPersons; i++) {
        const newOption = document.createElement("option")
        newOption.value = i
        newOption.textContent = i
        selectCountPersons.appendChild(newOption);
    }

    if (errorMessages.length > 0) {
        selectStatus.value = changeStatus.value // TODO: обращение к slot ?
        selectCountPersons.value = changeCountPersons.value
        if (slot.statusType.name !== 'EMPTY') {
            blockButton.style.display = 'none'
        }
    } else if (slot.statusType.name !== 'EMPTY') {
        const reservation = await getReservation(slot.reservationId)
        if (!reservation) return // TODO: страница ошибки

        selectStatus.value = reservation.statusType.name
        firstName.value = reservation.firstName
        lastName.value = reservation.lastName
        phone.value = reservation.phone
        email.value = reservation.email
        selectCountPersons.value = reservation.countPersons
        adminComment.value = reservation.adminComment
        blockButton.style.display = 'none'
    }

    reserveModal.style.opacity = '1'
    reserveModal.style.visibility = 'visible'
    reserveContent.style.transform = 'translate(0px, 0px)'
    reserveContent.style.opacity = '1'
}

async function getReservation(reservationId) {
    try {
        const response = await fetch(`/reservation/${reservationId}`)
        if (!response.ok) {
            throw new Error('Ошибка сети')
        }
        const reservation = response.json()
        return reservation
    } catch (error) {
        console.error('Ошибка:', error)
    }
}

function showBlockSlot() {
    const newOption = document.createElement("option")
    newOption.textContent = 'Заблокирован'
    selectStatus.appendChild(newOption);

    allInput.forEach(el => {
        if (el.type !== 'hidden' && el.type !== 'submit') {
            el.disabled = true
        }
        if (el.name === 'phone') el.value = null
    })
    allSelect.forEach(select => select.disabled = true)
    allTextarea.forEach(el => el.disabled = true)

    saveButton.style.display = 'none'
    blockButton.style.display = 'none'
    unBlockButton.style.display = 'block'

    reserveModal.style.opacity = '1'
    reserveModal.style.visibility = 'visible'
    reserveContent.style.transform = 'translate(0px, 0px)'
    reserveContent.style.opacity = '1'
}

function closeSlot() {
    reserveModal.style.opacity = '0'
    reserveModal.style.visibility = 'hidden'
    reserveContent.style.transform = 'translate(0px, 100%)'
    reserveContent.style.opacity = '0'

    allInput.forEach(el => {
        if (el.type !== 'hidden' && el.type !== 'submit') {
            el.value = null
            el.disabled = false
        }
        if (el.name === 'phone') el.value = '+7'
    })
    allSelect.forEach(select => {
        select.disabled = false
        while (select.firstChild) {
            select.removeChild(select.firstChild);
        }
    })
    allTextarea.forEach(el => {
        el.value = null
        el.disabled = false
    })
    errorMessages.forEach(el => el.remove())
    errorMessages = []

    setTimeout(function () {
        saveButton.style.display = 'block'
        blockButton.style.display = 'block'
        unBlockButton.style.display = 'none'
        body.style.overflowY = 'auto'
    }, 400)
}

function saveSlot() {
    reserveModal.style.opacity = '0'
    reserveModal.style.visibility = 'hidden'
}

document.addEventListener('DOMContentLoaded', function () {
    if (errorMessages.length > 0) {
        clickSlot(errorSlot.value)
    }
})