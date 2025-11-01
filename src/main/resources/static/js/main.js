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
let userId = null;
let name = null;
let selectedUserId = null;

function connect() {
    userId = localStorage.getItem('userId');
    name = localStorage.getItem('name');
    token = localStorage.getItem('jwtToken');

    if (!userId || !token) {
        window.location.href = 'login.html';
        return;
    }

    usernamePage.classList.add('hidden');
    chatPage.classList.remove('hidden');

    // Use relative endpoint to avoid hard-coding host/port
    const socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);

    const headers = { 'Authorization': `Bearer ${token}` };
    stompClient.connect(headers, onConnected, onError);
}

function onConnected() {
    // Subscribe to per-user queue (standard Spring destination, no userId in path)
    stompClient.subscribe('/user/queue/messages', onMessageReceived);
    // Subscribe to users topic (backend should send to /topic/users)
    stompClient.subscribe('/users/topic', onMessageReceived);

    // Notify backend user connected (align with @MessageMapping("/user.connect"))
    stompClient.send('/app/user.connect', {});

    document.querySelector('#connected-user-fullname').textContent = name;
    findAndDisplayConnectedUsers().then();
}

async function findAndDisplayConnectedUsers() {
    const headers = { 'Authorization': `Bearer ${token}` };
    // Match backend mapping: GET /api/users/user
    const connectedUsersResponse = await fetch('/api/users/user', { headers });
    let connectedUsers = await connectedUsersResponse.json();

    connectedUsers = connectedUsers.filter(user => user.userId?.toString() !== userId);

    const connectedUsersList = document.getElementById('connectedUsers');
    connectedUsersList.innerHTML = '';

    connectedUsers.forEach((user, idx) => {
        appendUserElement(user, connectedUsersList);
        if (idx < connectedUsers.length - 1) {
            const separator = document.createElement('li');
            separator.classList.add('separator');
            connectedUsersList.appendChild(separator);
        }
    });
}

function appendUserElement(user, connectedUsersList) {
    const listItem = document.createElement('li');
    listItem.classList.add('user-item');
    listItem.id = user.userId;

    const userImage = document.createElement('img');
    userImage.src = '/img/user_icon.png';
    userImage.alt = user.name;

    const usernameSpan = document.createElement('span');
    usernameSpan.textContent = user.name;

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
    document.querySelectorAll('.user-item').forEach(item => item.classList.remove('active'));
    messageForm.classList.remove('hidden');

    const clickedUser = event.currentTarget;
    clickedUser.classList.add('active');

    selectedUserId = clickedUser.getAttribute('id');
    fetchAndDisplayUserChat().then();

    const nbrMsg = clickedUser.querySelector('.nbr-msg');
    nbrMsg.classList.add('hidden');
    nbrMsg.textContent = '0';
}

function displayMessage(senderId, content) {
    const messageContainer = document.createElement('div');
    messageContainer.classList.add('message');

    if (senderId?.toString() === userId) {
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
    // Backend endpoint expects only receiverId path variable
    const userChatResponse = await fetch(`/api/messages/${selectedUserId}`, { headers });
    const userChat = await userChatResponse.json();
    chatArea.innerHTML = '';
    userChat.forEach(chat => {
        displayMessage(chat.senderId, chat.content);
    });
    chatArea.scrollTop = chatArea.scrollHeight;
}

function onError() {
    if (connectingElement) {
        connectingElement.textContent = 'Could not connect to WebSocket server. Please refresh this page to try again!';
        connectingElement.style.color = 'red';
    }
}

function sendMessage(event) {
    event.preventDefault();
    const messageContent = messageInput.value.trim();
    if (messageContent && stompClient) {
        const chatMessage = {
            senderId: userId,
            receiverId: selectedUserId,
            content: messageInput.value.trim(),
            timestamp: Date.now()
        };

        stompClient.send('/app/chat', {}, JSON.stringify(chatMessage));
        displayMessage(userId, messageInput.value.trim());
        messageInput.value = '';
    }
    chatArea.scrollTop = chatArea.scrollHeight;
}

async function onMessageReceived(payload) {
    await findAndDisplayConnectedUsers();

    const message = JSON.parse(payload.body);

    if (message.content) {
        if (selectedUserId && selectedUserId.toString() === message.senderId?.toString()) {
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
        stompClient.send('/app/user.disconnectUser', {});
        stompClient.disconnect();
    }

    await fetch('/api/auth/logout', {
        method: 'POST',
        headers: { 'Authorization': `Bearer ${token}` }
    });

    localStorage.removeItem('jwtToken');
    localStorage.removeItem('userId');
    localStorage.removeItem('name');
    window.location.href = 'login.html';
}

window.addEventListener('load', connect, true);
messageForm.addEventListener('submit', sendMessage, true);
logout.addEventListener('click', onLogout, true);