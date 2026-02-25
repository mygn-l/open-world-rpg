const stompClient = new StompJs.Client({
    brokerURL: "ws://localhost:8080/ws"
});

stompClient.onConnect = function (frame) {
    console.log("Connected: " + frame);

    stompClient.subscribe("/chat/public-chat", (message) => {
        console.log(JSON.parse(message.body).content);
    });
};

document.querySelector("#chat-box-button").addEventListener("click", function () {
    const content = document.querySelector("#chat-box").value;
    stompClient.publish({
        destination: "/app/public-chat",
        body: JSON.stringify({"content": content})
    });
})

stompClient.activate();
