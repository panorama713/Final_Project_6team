async function updateProfileInfo() {
    const profileButton = document.getElementById('profile-button');
    const profileInfo = document.getElementById('profileInfo');

    // Check login status by calling the login check API
    const isLoggedIn = await checkLogin();

    if (isLoggedIn) {
        try {
            const userData = await getMyData();

            // Hide the login button and show profile info
            profileButton.style.display = 'none';
            profileInfo.style.display = 'block';

            // Update profile info
            const profileImage = document.querySelector('.profile-image');
            const profileName = document.querySelector('.profile-name');

            // Set profile image
            profileImage.style.backgroundImage = `url(${userData.profileimg})`;

            // Set profile name
            profileName.textContent = userData.realname;
        } catch (error) {
            console.error('Error fetching user profile data:', error);
        }
    } else {
        // Show the login button and hide profile info
        profileButton.style.display = 'block';
        profileInfo.style.display = 'none';
    }
}

// Call the function to update profile information
updateProfileInfo();