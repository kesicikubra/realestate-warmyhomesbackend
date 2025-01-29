package com.team03.service.helper;

import org.springframework.stereotype.Component;

import java.text.Normalizer;


@Component
public class SlugHelper {
    public String createSlug(String text) {
        // Türkçe karakterleri ASCII karakterlere dönüştür
        String normalizedText = Normalizer.normalize(text, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "");

        // Boşlukları ve özel karakterleri uygun şekilde değiştir
        return normalizedText.trim().toLowerCase()
                .replaceAll("\\s+", "-") // Boşlukları tire ile değiştir
                .replaceAll("[^a-z0-9-]", ""); // Özel karakterleri kaldır
    }
}
