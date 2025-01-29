
package com.team03.ai;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.vertexai.VertexAI;
import com.google.cloud.vertexai.generativeai.ChatSession;
import com.google.cloud.vertexai.generativeai.GenerativeModel;
import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.ClassPathResource;

import org.springframework.web.context.annotation.SessionScope;

import java.io.IOException;

@Configuration(proxyBeanMethods = false)
@RequiredArgsConstructor
public class GeminiConfiguration {
    @Lazy
    @Bean
    GoogleCredentials googleCredentials() throws IOException {
        return GoogleCredentials.fromStream(new ClassPathResource("keyFile.json").getInputStream())
                .createScoped("https://www.googleapis.com/auth/cloud-platform", "https://www.googleapis.com/auth/cloud-platform.read-only");
    }

    @Bean
    public VertexAI vertexAI(GoogleCredentials googleCredentials) {
        return new VertexAI("real-estate-415918", "europe-west4", googleCredentials);
    }


    @Bean
    public GenerativeModel geminiProVisionGenerativeModel(VertexAI vertexAI) {
        return new GenerativeModel("gemini-pro-vision", vertexAI);
    }

    @Bean
    public GenerativeModel geminiProGenerativeModel(VertexAI vertexAI) {
        return new GenerativeModel("gemini-pro", vertexAI);
    }

    @Bean
    @SessionScope
    public ChatSession chatSession(@Qualifier("geminiProGenerativeModel") GenerativeModel generativeModel) {
        return new ChatSession(generativeModel);
    }


}
