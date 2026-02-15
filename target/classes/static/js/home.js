const API_BASE_URL = '/api';

// Check if user is logged in
function checkAuth() {
    const token = localStorage.getItem('accessToken');
    if (!token) {
        window.location.href = '/index.html';
    }
    return token;
}

// Initialize Home Page
document.addEventListener('DOMContentLoaded', () => {
    checkAuth();

    // Load User Info from LocalStorage
    const userJson = localStorage.getItem('user');
    if (userJson) {
        const user = JSON.parse(userJson);

        // Update "Create Post" avatar if user has one
        if (user.profilePictureUrl) {
            const avatarImg = document.getElementById('currentUserAvatar');
            if (avatarImg) {
                avatarImg.src = user.profilePictureUrl;
            }
        }

        // We can also update the header or welcome message if needed
        // For now, the header just has the logo and chat icon
    }

    // Highlight active tab (Home)
    const navItems = document.querySelectorAll('.nav-item');
    navItems.forEach(item => {
        if (item.getAttribute('href') === 'home.html') {
            item.classList.add('active');
        } else {
            item.classList.remove('active');
        }
    });
});

// Logout function (can be called from Profile page)
function logout() {
    localStorage.removeItem('accessToken');
    localStorage.removeItem('refreshToken');
    localStorage.removeItem('user');
    window.location.href = '/index.html';
}
