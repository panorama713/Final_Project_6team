async function loadUserProfile() {
    try {
        const response = await fetch("/api/v1/users/my-profile");

        if (response.ok) {
            const data = await response.json()

            const profileImage = document.getElementById('p-img')
            const followerCount = document.getElementById('follower-count')
            const followingCount = document.getElementById('following-count')
            const username = document.getElementById('username')
            const realName = document.getElementById('real-name')
            const email = document.getElementById('email')
            const writtenArticles = document.getElementById('written-articles')
            const writtenComments = document.getElementById('written-comments')

            if (data) {
                profileImage.src = data.profileImg
                username.textContent = data.username
                realName.textContent = data.realName
                email.textContent = data.email
                followerCount.textContent = data.followerCount
                followingCount.textContent = data.followingCount
                writtenArticles.textContent = data.numberOfWrittenArticle
                writtenComments.textContent = data.numberOfWrittenComment
            }
        } else {
            console.error("프로필 불러오기 실패")
            return null;
        }
    } catch (error) {
        console.error("프로필 에러", error)
        return null;
    }
}
