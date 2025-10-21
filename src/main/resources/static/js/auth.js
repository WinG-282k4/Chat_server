'use strict';

document.addEventListener('DOMContentLoaded', () => {
    const loginForm = document.querySelector('#loginForm');
    const registerForm = document.querySelector('#registerForm');

    if (loginForm) {
        loginForm.addEventListener('submit', login, true);
    }

    if (registerForm) {
        registerForm.addEventListener('submit', register, true);
    }
});

async function login(event) {
    event.preventDefault();
    const username = document.querySelector('#username').value.trim();
    const password = document.querySelector('#password').value.trim();
    const errorMessage = document.querySelector('#error-message');
    errorMessage.textContent = '';

    try {
        const response = await fetch('/api/auth/login', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ username, password })
        });

        if (response.ok) {
            const data = await response.json();
            localStorage.setItem('jwtToken', data.token);
            localStorage.setItem('userId', data.userId);
            localStorage.setItem('name', data.name);
            window.location.href = 'index.html';
        } else {
            let message = 'Login failed!';
            try {
                const error = await response.json();
                message = error.message || message;
            } catch (_) {}
            errorMessage.textContent = message;
        }
    } catch (e) {
        errorMessage.textContent = 'Could not connect to server.';
    }
}

async function register(event) {
    event.preventDefault();
    const username = document.querySelector('#username').value.trim();
    const name = document.querySelector('#name')?.value?.trim();
    const password = document.querySelector('#password').value.trim();
    const errorMessage = document.querySelector('#error-message');
    errorMessage.textContent = '';

    try {
        const response = await fetch('/api/auth/register', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ username, name, password })
        });

        if (response.ok) {
            alert('Registration successful! Please login.');
            window.location.href = 'login.html';
        } else {
            let message = 'Registration failed.';
            try {
                const errorData = await response.json();
                message = errorData.message || message;
            } catch (_) {}
            errorMessage.textContent = message;
        }
    } catch (e) {
        errorMessage.textContent = 'Could not connect to server.';
    }
}