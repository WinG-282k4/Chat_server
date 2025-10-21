'use strict';

const usernamePage = document.querySelector('#username-page');
const chatPage = document.querySelector('#chat-page');
const messageForm = document.querySelector('#messageForm');
const messageInput = document.querySelector('#message');
const connectingElement = document.querySelector('.connecting');
const chatArea = document.querySelector('#chat-messages');
const logout = document.querySelector('#logout');

let stompClient = null;
let token = null;
let userId = null; // <-- SỬA: Dùng userId
let name = null;   // <-- SỬA: Dùng name
let selectedUserId = null;

function connect() {
    // <-- SỬA: Lấy userId và name
    userId = localStorage.getItem('userId');
    name = localStorage.getItem('name');
    token = localStorage.getItem('jwtToken');

    // <-- SỬA: Kiểm tra bằng userId
    if (!userId || !token) {
        window.location.href = 'login.html';
        return;
    }

    usernamePage.classList.add('hidden');
    chatPage.classList.remove('hidden');

    // <-- SỬA: Thêm URL đầy đủ
    const socket = new SockJS('http://localhost:8081/ws');
    stompClient = Stomp.over(socket);

    const headers = {
        'Authorization': `Bearer ${token}`
    };

    stompClient.connect(headers, onConnected, onError);
}


function onConnected() {
    // <-- SỬA: Subscribe bằng userId
    stompClient.subscribe(`/user/${userId}/queue/messages`, onMessageReceived);
    stompClient.subscribe(`/topic/users`, onMessageReceived);

    // Gửi tin nhắn "connect", backend sẽ tự biết user là ai qua Principal
    stompClient.send("/app/user.connect", {});

    // <-- SỬA: Hiển thị tên (name)
    document.querySelector('#connected-user-fullname').textContent = name;
    findAndDisplayConnectedUsers().then();
}

async function findAndDisplayConnectedUsers() {
    const headers = { 'Authorization': `Bearer ${token}` };

    // <-- SỬA: Thêm URL đầy đủ (Giả sử API là /api/users)
    const connectedUsersResponse = await fetch('http://localhost:8081/api/users', { headers });
    let connectedUsers = await connectedUsersResponse.json();

    // <-- SỬA: Lọc chính mình bằng userId
    connectedUsers = connectedUsers.filter(user => user.userId.toString() !== userId);

    const connectedUsersList = document.getElementById('connectedUsers');
    connectedUsersList.innerHTML = '';

    connectedUsers.forEach(user => {
        appendUserElement(user, connectedUsersList);
        if (connectedUsers.indexOf(user) < connectedUsers.length - 1) {
            const separator = document.createElement('li');
            separator.classList.add('separator');
            connectedUsersList.appendChild(separator);
        }
    });
}

function appendUserElement(user, connectedUsersList) {
    const listItem = document.createElement('li');
    listItem.classList.add('user-item');
    // <-- SỬA: Dùng userId làm ID
    listItem.id = user.userId;

    const userImage = document.createElement('img');
    userImage.src = '../img/user_icon.png';
    userImage.alt = user.name;

    const usernameSpan = document.createElement('span');
    usernameSpan.textContent = user.name; // Hiển thị tên

    const receivedMsgs = document.createElement('span');
    receivedMsgs.textContent = '0';
    receivedMsgs.classList.add('nbr-msg', 'hidden');

    listItem.appendChild(userImage);
    listItem.appendChild(usernameSpan);
    listItem.appendChild(receivedMsgs);

    listItem.addEventListener('click', userItemClick);

    connectedUsersList.appendChild(listItem);
}

function userItemClick(event) {
    document.querySelectorAll('.user-item').forEach(item => {
        item.classList.remove('active');
    });
    messageForm.classList.remove('hidden');

    const clickedUser = event.currentTarget;
    clickedUser.classList.add('active');

    selectedUserId = clickedUser.getAttribute('id');
    fetchAndDisplayUserChat().then();

    const nbrMsg = clickedUser.querySelector('.nbr-msg');
    nbrMsg.classList.add('hidden');
    nbrMsg.textContent = '0';
}

// <-- SỬA: Hàm này giờ nhận senderId
function displayMessage(senderId, content) {
    const messageContainer = document.createElement('div');
    messageContainer.classList.add('message');

    // <-- SỬA: So sánh với userId (dưới dạng chuỗi cho an toàn)
    if (senderId.toString() === userId) {
        messageContainer.classList.add('sender');
    } else {
        messageContainer.classList.add('receiver');
    }
    const message = document.createElement('p');
    message.textContent = content;
    messageContainer.appendChild(message);
    chatArea.appendChild(messageContainer);
}

async function fetchAndDisplayUserChat() {
    const headers = { 'Authorization': `Bearer ${token}` };

    // <-- SỬA: Thêm URL đầy đủ và dùng userId
    const userChatResponse = await fetch(`http://localhost:8081/api/messages/${userId}/${selectedUserId}`, { headers });
    const userChat = await userChatResponse.json();
    chatArea.innerHTML = '';
    userChat.forEach(chat => {
        // Giả sử API trả về { senderId: ..., content: ... }
        displayMessage(chat.senderId, chat.content);
    });
    chatArea.scrollTop = chatArea.scrollHeight;
}

function onError() {
    connectingElement.textContent = 'Could not connect to WebSocket server. Please refresh this page to try again!';
    connectingElement.style.color = 'red';
}

function sendMessage(event) {
    event.preventDefault();
    const messageContent = messageInput.value.trim();
    if (messageContent && stompClient) {
        const chatMessage = {
            // <-- SỬA: Gửi userId
            senderId: userId,
            recipientId: selectedUserId,
            content: messageInput.value.trim(),
            timestamp: new Date()
        };

        stompClient.send("/app/chat", {}, JSON.stringify(chatMessage));

        // <-- SỬA: Hiển thị tin nhắn của mình bằng userId
        displayMessage(userId, messageInput.value.trim());
        messageInput.value = '';
    }
    chatArea.scrollTop = chatArea.scrollHeight;
}


async function onMessageReceived(payload) {
    await findAndDisplayConnectedUsers();

    const message = JSON.parse(payload.body);

    // Kiểm tra xem đây là tin nhắn chat (có content) hay thông báo user
    if (message.content) {
        if (selectedUserId && selectedUserId.toString() === message.senderId.toString()) {
            displayMessage(message.senderId, message.content);
            chatArea.scrollTop = chatArea.scrollHeight;
        }

        const notifiedUser = document.querySelector(`#${message.senderId}`);
        if (notifiedUser && !notifiedUser.classList.contains('active')) {
            const nbrMsg = notifiedUser.querySelector('.nbr-msg');
            nbrMsg.classList.remove('hidden');
            nbrMsg.textContent = '';
        }
    }
}

async function onLogout() {
    if (stompClient) {
        stompClient.send("/app/user.disconnect", {});
        stompClient.disconnect();
    }

    // <-- SỬA: Thêm URL đầy đủ
    await fetch('http://localhost:8081/api/auth/logout', {
        method: 'POST',
        headers: { 'Authorization': `Bearer ${token}` }
    });

    // <-- SỬA: Xóa userId và name
    localStorage.removeItem('jwtToken');
    localStorage.removeItem('userId');
    localStorage.removeItem('name');
    window.location.href = 'login.html';
}

// Tự động kết nối khi tải trang
window.addEventListener('load', connect, true);

messageForm.addEventListener('submit', sendMessage, true);
logout.addEventListener('click', onLogout, true);