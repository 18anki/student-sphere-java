const API_BASE_URL = '/api';

// --- Helper Functions ---
function showError(elementId, message) {
    const el = document.getElementById(elementId);
    if (el) {
        el.textContent = message;
        el.style.display = 'block';
    }
}

function showSuccess(elementId, message) {
    const el = document.getElementById(elementId);
    if (el) {
        el.textContent = message;
        el.style.display = 'block';
    }
}

function clearMessages() {
    document.querySelectorAll('.error-message, .success-message').forEach(el => el.style.display = 'none');
}

async function parseResponse(response) {
    const ct = response.headers.get('content-type') || '';
    if (ct.includes('application/json')) {
        return response.json();
    }
    return response.text();
}

// --- Sign In Logic ---
async function handleSignIn(event) {
    event.preventDefault();
    clearMessages();

    const email = document.getElementById('email').value;
    const password = document.getElementById('password').value;

    try {
        const response = await fetch(`${API_BASE_URL}/auth/signin`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ email, password })
        });

        const data = await parseResponse(response);

        if (response.ok) {
            // If server returns AuthResponse JSON, store token and redirect
            if (data && data.accessToken) {
                localStorage.setItem('accessToken', data.accessToken);
                localStorage.setItem('refreshToken', data.refreshToken || '');

                // Store user details for dashboard
                const user = {
                    id: data.userId,
                    fullName: data.fullName,
                    collegeName: data.collegeName
                };
                localStorage.setItem('user', JSON.stringify(user));

                showSuccess('message', 'Login successful');
                // Redirect to dashboard
                setTimeout(() => window.location.href = '/home.html', 800);
            } else {
                showSuccess('message', typeof data === 'string' ? data : 'Login successful');
            }
        } else {
            const msg = typeof data === 'string' ? data : (data.message || JSON.stringify(data));
            showError('message', msg || 'Login failed');
        }
    } catch (error) {
        showError('message', 'An error occurred. Please try again.');
        console.error(error);
    }
}

// --- Sign Up Logic ---

// Load States
async function loadStates() {
    try {
        const response = await fetch(`${API_BASE_URL}/master/states`);
        const states = await response.json();
        const stateSelect = document.getElementById('state');

        stateSelect.innerHTML = '<option value="">Select State</option>';
        states.forEach(state => {
            const option = document.createElement('option');
            option.value = state.id;
            option.textContent = state.name;
            stateSelect.appendChild(option);
        });
    } catch (error) {
        console.error('Error loading states:', error);
    }
}

// Load Cities based on State
async function loadCities(stateId) {
    try {
        const response = await fetch(`${API_BASE_URL}/master/cities/${stateId}`);
        const cities = await response.json();
        const citySelect = document.getElementById('city');

        citySelect.innerHTML = '<option value="">Select City</option>';
        citySelect.disabled = false;
        cities.forEach(city => {
            const option = document.createElement('option');
            option.value = city.id;
            option.textContent = city.name;
            citySelect.appendChild(option);
        });

        // Reset College
        document.getElementById('college').innerHTML = '<option value="">Select College</option>';
        document.getElementById('college').disabled = true;
    } catch (error) {
        console.error('Error loading cities:', error);
    }
}

// Load Colleges based on City
async function loadColleges(cityId) {
    try {
        const response = await fetch(`${API_BASE_URL}/master/colleges/${cityId}`);
        const colleges = await response.json();
        const collegeSelect = document.getElementById('college');

        collegeSelect.innerHTML = '<option value="">Select College</option>';
        collegeSelect.disabled = false;
        colleges.forEach(college => {
            const option = document.createElement('option');
            option.value = college.id;
            option.textContent = college.name;
            option.dataset.domain = college.domain; // Store domain for validation hint
            collegeSelect.appendChild(option);
        });
    } catch (error) {
        console.error('Error loading colleges:', error);
    }
}

async function handleSignUp(event) {
    event.preventDefault();
    clearMessages();

    const fullName = document.getElementById('fullName').value;
    const email = document.getElementById('email').value;
    const password = document.getElementById('password').value;
    const confirmPassword = document.getElementById('confirmPassword').value;
    const studentCollegeEmail = document.getElementById('studentCollegeEmail').value;
    const collegeId = document.getElementById('college').value;

    if (password !== confirmPassword) {
        showError('message', 'Passwords do not match');
        return;
    }

    const requestBody = {
        fullName,
        email,
        password,
        confirmPassword,
        studentCollegeEmail,
        collegeId: parseInt(collegeId)
    };

    try {
        const response = await fetch(`${API_BASE_URL}/auth/signup`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(requestBody)
        });

        const data = await parseResponse(response);

        if (response.ok) {
            // Server now returns UserResponse JSON
            const successMsg = (data && data.fullName) ? `Registered ${data.fullName}` : 'Registration successful';
            showSuccess('message', successMsg);
            setTimeout(() => {
                window.location.href = '/index.html'; // Redirect to login
            }, 1200);
        } else {
            const msg = typeof data === 'string' ? data : (data.message || JSON.stringify(data));
            showError('message', msg || 'Registration failed');
        }
    } catch (error) {
        showError('message', 'An error occurred. Please try again.');
        console.error(error);
    }
}

// Event Listeners Initialization
document.addEventListener('DOMContentLoaded', () => {
    // Check which page we are on
    const signInForm = document.getElementById('signInForm');
    const signUpForm = document.getElementById('signUpForm');

    if (signInForm) {
        signInForm.addEventListener('submit', handleSignIn);
    }

    if (signUpForm) {
        loadStates();
        signUpForm.addEventListener('submit', handleSignUp);

        document.getElementById('state').addEventListener('change', (e) => {
            if (e.target.value) loadCities(e.target.value);
        });

        document.getElementById('city').addEventListener('change', (e) => {
            if (e.target.value) loadColleges(e.target.value);
        });

        // Optional: Show domain hint
        document.getElementById('college').addEventListener('change', (e) => {
             const selectedOption = e.target.options[e.target.selectedIndex];
             const domain = selectedOption.dataset.domain;
             if(domain) {
                 document.getElementById('emailHint').textContent = `Please use an email ending in @${domain}`;
                 document.getElementById('emailHint').style.display = 'block';
             } else {
                 document.getElementById('emailHint').style.display = 'none';
             }
        });
    }
});
