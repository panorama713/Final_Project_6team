function getProfileInfo() {
    const profileButton = document.getElementById('profile-button')
    const profileInfo = document.getElementById('profileInfo')

    const isLoggedIn = checkLogin();

    if (isLoggedIn) {
        getMyData().then(userData => {
            profileButton.style.display = 'none'
            profileInfo.style.display = 'flex'

            const profileImage = document.querySelector('.profile-image')
            const profileName = document.querySelector('.profile-username')

            profileImage.style.backgroundImage = `url(${userData.profileImg})`
            profileName.textContent = userData.username
        })
    } else {
        profileButton.style.display = 'block'
        profileInfo.style.display = 'none'
    }
}