package minggrosfou.eldoria.socketcontroller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class Socket_Handler {
    @MessageMapping("/public-chat")
    @SendTo("/chat/public-chat")
    public Chat_Message public_chat(Chat_Message message) {
        System.out.println(message.content);
        return message;
    }
}
