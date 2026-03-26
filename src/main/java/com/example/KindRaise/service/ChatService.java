package com.example.KindRaise.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.KindRaise.dtos.ChatMessageDto;
import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;


@Service
public class ChatService {
    
    private Map<String, List<ChatMessageDto>> userChats = new HashMap<>();

    @Value("${spring.gemini.secret.key}")
    private String geminiSecretKey;
    
    @Value("${spring.gemini.model}")
    private String geminiModel;

    public String getReply(String message, String userId) {
    int maxRetries = 3;

        for (int attempt = 1; attempt <= maxRetries; attempt++) {

            try {
                return generateResponse(message, userId);

            } catch (Exception e) {

                System.out.println("Attempt " + attempt + " failed: " + e.getMessage());
                if (attempt == maxRetries) {
                    return "Server is busy right now 😔 Please try again in a few seconds.";
                }

                try {
                    Thread.sleep(1000 * attempt); // wait (1s, 2s, 3s)
                } catch (InterruptedException ignored) {}
            }
        }

        return "Unexpected error";
    }


    public String generateResponse(String message,String userId){
        String msg = message.toLowerCase();

            // ✅ Simple rule (optional)
            if (msg.contains("your name")) {
                return "I am KindRaise Admin Assistant 🤖";
            }

            // ✅ Get or create chat history
            List<ChatMessageDto> chatHistory =
                    userChats.computeIfAbsent(userId, k -> new ArrayList<>());

            // ✅ Add user message
            chatHistory.add(new ChatMessageDto("user", message));

            // ✅ Build conversation
            StringBuilder conversation = new StringBuilder();

            for (ChatMessageDto m : chatHistory) {
                conversation.append(m.getRole())
                        .append(": ")
                        .append(m.getContent())
                        .append("\n");
            }

            try {
                Client client = Client.builder()
                        .apiKey(this.geminiSecretKey)
                        .build();

                // ✅ Prompt with memory
                String prompt = """
                        You are KindRaise Admin Assistant 🤖.

                        Platform Rules:
                        - Users login using USERNAME and PASSWORD (not email)
                        - If password is forgotten → contact admin at +91 9961423960
                        - Admin Name -> Mohammed Fazil Place -> Trivandrum Kerala
                        - Only logged-in users can donate and view full campaign details
                        - Non-logged users can only see campaign cards
                        - Users CAN create campaigns themselves (no need to contact admin)

                        Navigation:
                        - After login → user goes to Dashboard

                        Instructions:
                        - Answer ONLY what the user asked
                        - Keep answers SHORT (1–3 lines max)
                        - Be context-aware (use conversation)
                        - If user says "ok", "thanks" → respond politely

                        Conversation:
                        """ + conversation.toString();

                GenerateContentResponse response = client.models.generateContent(
                        this.geminiModel,
                        prompt,
                        null
                );

                String reply = response.text();

                // ✅ Save AI reply
                chatHistory.add(new ChatMessageDto("assistant", reply));

                // ✅ Limit memory (VERY IMPORTANT)
                if (chatHistory.size() > 10) {
                    chatHistory.remove(0);
                }

                return reply;

            } catch (Exception e) {
                if (e.getMessage().contains("429")) {
                 return "Too many requests 🚫 Please wait 15 seconds and try again.";
            }
                System.out.println("ERROR: " + e.getMessage());
                return "Something went wrong. Please try again.";
            }
        }
    }