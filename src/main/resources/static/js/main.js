'use strict';

// Vẫn giữ nguyên các querySelector
const usernamePage = document.querySelector('#username-page');
const chatPage = document.querySelector('#chat-page');
// const usernameForm = document.querySelector('#usernameForm'); // Không cần nữa
const messageForm = document.querySelector('#messageForm');
const messageInput = document.querySelector('#message');
const connectingElement = document.querySelector('.connecting');
const chatArea = document.querySelector('#chat-messages');
const logout = document.querySelector('#logout');

let stompClient = null;
let username = null; // Đổi 'nickname' thành 'username'
let token = null;    // Thêm biến chứa token
let selectedUserId = null;

// Sửa hàm connect: không cần event, nó sẽ được gọi tự động
function connect() {
    // Lấy thông tin từ localStorage
    username = localStorage.getItem('username');
    token = localStorage.getItem('jwtToken');

    // Nếu không có token, quay về trang login
    if (!username || !token) {
        window.location.href = 'login.html';
        return;
    }

    usernamePage.classList.add('hidden'); // Ẩn form login cũ
    chatPage.classList.remove('hidden');  // Hiện trang chat

    const socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);

    // *** THAY ĐỔI QUAN TRỌNG NHẤT ***
    // Thêm JWT vào header khi kết nối WebSocket
    const headers = {
        'Authorization': `Bearer ${token}`
    };

    stompClient.connect(headers, onConnected, onError); // Truyền headers vào
}


function onConnected() {
    // Subscribe vào queue riêng (dùng username đã xác thực)
    stompClient.subscribe(`/user/${username}/queue/messages`, onMessageReceived);

    // Subscribe vào topic chung (để nhận thông báo ai online/offline)
    // Sửa '/user/public' thành '/topic/users' cho đúng chuẩn
    stompClient.subscribe(`/topic/users`, onMessageReceived);

    // *** THAY ĐỔI QUAN TRỌNG VỀ BẢO MẬT ***
    // Gửi tin nhắn "connect", backend sẽ tự biết user là ai qua Principal
    // (Như chúng ta đã thảo luận ở các câu hỏi trước)
    stompClient.send("/app/user.connect", {}); // Không cần gửi payload

    // Hiển thị tên user
    document.querySelector('#connected-user-fullname').textContent = username;
    findAndDisplayConnectedUsers().then();
}

async function findAndDisplayConnectedUsers() {
    // Phải gửi JWT kèm theo khi gọi API
    const headers = { 'Authorization': `Bearer ${token}` };

    // Giả sử bạn có 1 API '/api/users' để lấy user online
    const connectedUsersResponse = await fetch('/api/users', { headers });
    let connectedUsers = await connectedUsersResponse.json();

    // Lọc chính mình ra khỏi danh sách
    connectedUsers = connectedUsers.filter(user => user.username !== username);
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
    listItem.id = user.username; // Dùng username

    const userImage = document.createElement('img');
    userImage.src = '../img/user_icon.png';
    userImage.alt = user.name; // Giả sử user có trường 'name'

    const usernameSpan = document.createElement('span');
    usernameSpan.textContent = user.name; // Hiển thị tên đầy đủ

    const receivedMsgs = document.createElement('span');
    receivedMsgs.textContent = '0';
    receivedMsgs.classList.add('nbr-msg', 'hidden');

    listItem.appendChild(userImage);
    listItem.appendChild(usernameSpan);
    listItem.appendChild(receivedMsgs);

    listItem.addEventListener('click', userItemClick);

    connectedUsersList.appendChild(listItem);
}

// Hàm này giữ nguyên
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

// Sửa lại hàm này, dùng 'username' thay vì 'nickname'
function displayMessage(senderId, content) {
    const messageContainer = document.createElement('div');
    messageContainer.classList.add('message');
    if (senderId === username) { // Dùng 'username'
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
    // Phải gửi JWT kèm theo khi gọi API
    const headers = { 'Authorization': `Bearer ${token}` };

    const userChatResponse = await fetch(`/api/messages/${username}/${selectedUserId}`, { headers });
    const userChat = await userChatResponse.json();
    chatArea.innerHTML = '';
    userChat.forEach(chat => {
        displayMessage(chat.senderId, chat.content);
    });
    chatArea.scrollTop = chatArea.scrollHeight;
}

// Hàm này giữ nguyên
function onError() {
    connectingElement.textContent = 'Could not connect to WebSocket server. Please refresh this page to try again!';
    connectingElement.style.color = 'red';
}

// Sửa hàm này, dùng 'username'
function sendMessage(event) {
    const messageContent = messageInput.value.trim();
    if (messageContent && stompClient) {
        const chatMessage = {
            senderId: username, // Dùng 'username'
            recipientId: selectedUserId,
            content: messageInput.value.trim(),
            timestamp: new Date()
        };
        // Giả sử API của bạn là '/app/chat'
        stompClient.send("/app/chat", {}, JSON.stringify(chatMessage));
        displayMessage(username, messageInput.value.trim()); // Dùng 'username'
        messageInput.value = '';
    }
    chatArea.scrollTop = chatArea.scrollHeight;
    event.preventDefault();
}


async function onMessageReceived(payload) {
    // Có tin nhắn mới hoặc thông báo user, tải lại danh sách
    await findAndDisplayConnectedUsers();

    const message = JSON.parse(payload.body);

    // Kiểm tra xem đây là tin nhắn chat hay thông báo user
    // (Bạn cần chuẩn hóa cấu trúc tin nhắn trả về từ server)
    if (message.content) { // Đây là tin nhắn chat
        if (selectedUserId && selectedUserId === message.senderId) {
            displayMessage(message.senderId, message.content);
            chatArea.scrollTop = chatArea.scrollHeight;
        }

        const notifiedUser = document.querySelector(`#${message.senderId}`);
        if (notifiedUser && !notifiedUser.classList.contains('active')) {
            const nbrMsg = notifiedUser.querySelector('.nbr-msg');
            nbrMsg.classList.remove('hidden');
            nbrMsg.textContent = ''; // Hoặc tăng số đếm
        }
    }
    // Nếu là thông báo (ví dụ: UserDTO từ @SendTo), nó sẽ chỉ cập nhật danh sách
    // mà không hiển thị trong chat area.
}

// Sửa hàm onLogout để gọi API /logout
async function onLogout() {
    if (stompClient) {
        // Gửi tin nhắn disconnect, backend sẽ xử lý qua Principal
        stompClient.send("/app/user.disconnect", {});
        stompClient.disconnect();
    }

    // Gọi API logout của bạn
    await fetch('/api/auth/logout', {
        method: 'POST',
        headers: { 'Authorization': `Bearer ${token}` }
    });

    // Xóa token và quay về trang login
    localStorage.removeItem('jwtToken');
    localStorage.removeItem('username');
    window.location.href = 'login.html';
}

// Thay đổi event listener
// Xóa listener cũ
// usernameForm.addEventListener('submit', connect, true);
// Thêm listener mới: Tự động kết nối khi tải trang
window.addEventListener('load', connect, true);

messageForm.addEventListener('submit', sendMessage, true);
logout.addEventListener('click', onLogout, true);
// Xóa onbeforeunload vì nó phức tạp và onLogout đã xử lý
// window.onbeforeunload = () => onLogout();