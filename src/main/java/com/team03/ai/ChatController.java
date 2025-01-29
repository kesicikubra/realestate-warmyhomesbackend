package com.team03.ai;



import com.google.cloud.vertexai.api.GenerateContentResponse;
import com.google.cloud.vertexai.generativeai.ChatSession;
import com.google.cloud.vertexai.generativeai.ResponseHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping()
@RequiredArgsConstructor
public class ChatController {

    private final ChatSession chatSession;

    @PostMapping("/chat")
    public String createSubject(@RequestBody String message) throws IOException {

        String modifiedMessage = message + "Can you write the subject title of the message written so far in 2-5 words?" +
                "(Whatever language the message is in, the reply should be in that language.)";
        GenerateContentResponse response = this.chatSession.sendMessage(modifiedMessage);
        return ResponseHandler.getText(response);
    }


}

