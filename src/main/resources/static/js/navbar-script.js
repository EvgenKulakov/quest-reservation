document.addEventListener('DOMContentLoaded', function () {

    const currentURL = document.location.href.split('/')[3]
    const reserveURL = 'reservations'
    const questURL = 'quests'
    const userURL = 'users'

    let modifyElement
    switch (currentURL) {
        case reserveURL:
            modifyElement = document.getElementById(reserveURL)
            break
        case questURL:
            modifyElement = document.getElementById(questURL)
            break
        case userURL:
            modifyElement = document.getElementById(userURL)
            break
    }

    modifyElement.className += ' active'
})