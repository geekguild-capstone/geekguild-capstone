console.log("we got here")
    function openTalkJSChat(friendId, friendUsername, friendImage, userId, userUsername, userImage, event) {

    event.stopPropagation();
    Talk.ready.then(function () {
    var me = new Talk.User({
    id: userId,
    name: userUsername,
    email: 'alice@example.com',
    photoUrl: userImage,
});
    window.talkSession = new Talk.Session({
    appId: 'tUloHvfB',
    me: me,
});
    var other = new Talk.User({
    id: friendId,
    name: friendUsername,
    email: 'Sebastian@example.com',
    photoUrl: friendImage,
});

    var conversation = talkSession.getOrCreateConversation(
    Talk.oneOnOneId(me, other)
    );
    conversation.setParticipant(me);
    conversation.setParticipant(other);

    var inbox = talkSession.createInbox({selected: conversation});
    inbox.mount(document.getElementById('talkjs-container'));

    // Show the chat window when opened
    showChatWindow();

});
}

    function showChatWindow() {
    const chatWindow = document.querySelector('.chat-window');
    chatWindow.classList.add('visible');
}

    function closeChatArea() {
    const chatArea = document.querySelector('.chat-window');
    chatArea.classList.remove('visible');
}


    // Click event listener on the window to close the chat area
    window.addEventListener('click', function (event) {
    const chatArea = document.querySelector('.chat-window');
    if (!chatArea.contains(event.target)) {
    closeChatArea();
}
});
