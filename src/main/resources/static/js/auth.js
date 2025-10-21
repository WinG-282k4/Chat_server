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
    errorMessage.textContent = ''; // Xóa lỗi cũ

    try {
        // <-- SỬA: Thêm URL đầy đủ
        const response = await fetch('http://localhost:8081/api/auth/login', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ username, password })
        });

        if (response.ok) {
            const data = await response.json();

            // <-- SỬA: Lưu userId và name (thay vì username)
            // Giả sử server trả về: { "token": "...", "userId": 123, "name": "Thanh" }
            localStorage.setItem('jwtToken', data.token);
            localStorage.setItem('userId', data.userId);
            localStorage.setItem('name', data.name);

            // Đăng nhập thành công, chuyển sang trang chat
            window.location.href = 'index.html';
        } else {
            const error = await response.json();
            errorMessage.textContent = error.message || 'Login failed!';
        }
    } catch (e) {
        errorMessage.textContent = 'Could not connect to server.';
    }
}

async function register(event) {
    event.preventDefault();
    const username = document.querySelector('#username').value.trim();
    const name = document.querySelector('#name').value.trim();
    const password = document.querySelector('#password').value.trim();
    const errorMessage = document.querySelector('#error-message');
    errorMessage.textContent = ''; // Xóa lỗi cũ

    try {
        // <-- SỬA: Thêm URL đầy đủ
        const response = await fetch('http://localhost:8081/api/auth/register', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ username, name, password }) // Đảm bảo DTO của bạn khớp
        });

        if (response.ok) {
            alert('Registration successful! Please login.');
            window.location.href = 'login.html'; // Chuyển về trang login
        } else {
            const errorData = await response.json();
            errorMessage.textContent = errorData.message || 'Registration failed.';
        }
    } catch (e) {
        errorMessage.textContent = 'Could not connect to server.';
    }
}