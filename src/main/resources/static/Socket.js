class Socket {
    constructor(url) {
        this.client = new StompJs.Client({
            brokerURL: url
        });

        const self = this;
        this.client.onConnect = function(frame) {
            self.client.subscribe("/chat/public-chat", function (message) {
                console.log(JSON.parse(message.body).content);
            });
        };

        document.querySelector("#chat-box-button").addEventListener("click", function () {
            const content = document.querySelector("#chat-box").value;
            self.client.publish({
                destination: "/app/public-chat",
                body: JSON.stringify({"content": content})
            });
        });

        this.client.activate();
    }
}

export default Socket;
