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

const slotInfo = document.querySelector('#slotInfo')
const saveButton = document.querySelector('#saveButton')
const blockButton = document.querySelector('#blockButton')
const unBlockButton = document.querySelector('#unBlockButton')

function clickSlot(slotButton) {
    const slot = JSON.parse(slotButton.value)
    const quest = slot.quest

    body.style.overflowY = 'hidden'

    questName.textContent = quest.questName
    slotDate.textContent = 'Дата: ' + slot.date
    slotTime.textContent = 'Время: ' + slot.time
    slotPrice.textContent = 'Стоимость: ' + slot.price + '₽'

    if (slot.statusType !== 'BLOCK') {
        showReservation(slot, quest)
    } else {
        showBlockSlot()
    }

    slotInfo.value = JSON.stringify(slot)

    reserveModal.style.opacity = '1'
    reserveModal.style.visibility = 'visible'
    reserveContent.style.transform = 'translate(0px, 0px)'
    reserveContent.style.opacity = '1'
}

function showReservation(slot, quest) {
    const statuses = quest.statuses
    for (let i = 0; i < statuses.length; i++) {
        const newOption = document.createElement("option")
        newOption.value = statuses[i].type
        newOption.textContent = statuses[i].name
        selectStatus.appendChild(newOption);
    }

    for (let i = quest.minPersons; i <= quest.maxPersons; i++) {
        const newOption = document.createElement("option")
        newOption.value = i
        newOption.textContent = i
        selectCountPersons.appendChild(newOption);
    }

    if (slot.reservation) {
        const reservation = slot.reservation
        const client = reservation.client

        selectStatus.value = reservation.statusType
        firstName.value = client.firstName
        lastName.value = client.lastName
        phone.value = client.phone
        email.value = client.email
        selectCountPersons.value = reservation.countPersons

        blockButton.style.display = 'none'
    } else {
        phone.value = '+7'
    }
}

function showBlockSlot() {
    const newOption = document.createElement("option")
    newOption.textContent = 'Заблокирован'
    selectStatus.appendChild(newOption);

    allInput.forEach(el => {
        if (el.name !== '_csrf' && el.name !== 'slot') {
            el.disabled = true
        }
    })
    allSelect.forEach(select => select.disabled = true)
    allTextarea.forEach(el => el.disabled = true)

    saveButton.style.display = 'none'
    blockButton.style.display = 'none'
    unBlockButton.style.display = 'block'
}

function closeSlot() {
    reserveModal.style.opacity = '0'
    reserveModal.style.visibility = 'hidden'
    reserveContent.style.transform = 'translate(0px, 100%)'
    reserveContent.style.opacity = '0'

    allInput.forEach(el => {
        if (el.name !== '_csrf') {
            el.value = null
            el.disabled = false
        }
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