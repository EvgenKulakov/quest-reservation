const firstPage = document.querySelector('#firstPage')
const secondPage = document.querySelector('#secondPage')
const radioButtons = document.querySelectorAll('input[type=radio]');
const delIcons = document.querySelectorAll('.del-icon')
const plusIcons = document.querySelectorAll('.plus-icon')


function onward() {
    firstPage.style.display = 'none'
    secondPage.style.display = 'block'
}

function back() {
    secondPage.style.display = 'none'
    firstPage.style.display = 'block'
}

document.addEventListener('DOMContentLoaded', function () {

    radioButtons.forEach(function(radioButton) {
        radioButton.addEventListener('change', function() {
            if (this.checked) {
                switch (this.value) {
                    case 'equal-days':
                        console.log('render equal days')
                        break
                    case 'weekdays-weekends':
                        console.log('render weekdays weekend')
                        break
                    case 'different-days':
                        console.log('render different days')
                        break
                }
            }
        });
    });

    const delRow = (event) => {
        const row = event.target.closest('.row');
        const parent = event.target.closest('#slotlistForm')

        row.remove();

        for (let i = 0; i < parent.children.length; i++) {
            const inputTime = parent.children[i].querySelector('#input-time')
            const inputPrice = parent.children[i].querySelector('#input-price')
            inputTime.name = `slotList.monday[${i}].time`
            inputPrice.name = `slotList.monday[${i}].price`
        }
    }

    delIcons.forEach(function (delIcon) {
        delIcon.addEventListener('click', delRow)
    })

    const plusRow =  (event) => {
        const row = event.target.closest('.row');
        const parent = event.target.closest('#slotlistForm')
        const countRow = parent.children.length
        const timeField = `slotList.monday[${countRow}].time`
        const priceField = `slotList.monday[${countRow}].price`

        const newRow = document.createElement('div');
        newRow.className = 'row'
        newRow.innerHTML = row.innerHTML

        const inputTime = newRow.querySelector('#input-time')
        const inputPrice = newRow.querySelector('#input-price')
        inputTime.name = timeField
        inputPrice.name = priceField
        inputTime.value = null
        inputPrice.value = null

        const plusIcon = newRow.querySelector('.plus-icon')
        const delIcon = newRow.querySelector('.del-icon')
        plusIcon.addEventListener('click', plusRow)
        delIcon.addEventListener('click', delRow)

        parent.insertBefore(newRow, row.nextSibling);
    }

    plusIcons.forEach(function (plusIcon) {
        plusIcon.addEventListener('click', plusRow)
    })
})