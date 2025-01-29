package com.team03.ai;

import com.google.cloud.vertexai.api.GenerateContentResponse;
import com.google.cloud.vertexai.generativeai.ContentMaker;
import com.google.cloud.vertexai.generativeai.GenerativeModel;
import com.google.cloud.vertexai.generativeai.PartMaker;
import com.google.cloud.vertexai.generativeai.ResponseHandler;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/gemini-pro-vision")
public class PhotoController {

    private final GenerativeModel generativeModel;

    public PhotoController(@Qualifier("geminiProVisionGenerativeModel") GenerativeModel generativeModel) {
        this.generativeModel = generativeModel;
    }

    public boolean checkPhotoSuitability(MultipartFile imageFile) {
        try {
            GenerateContentResponse generateContentResponse = this.generativeModel.generateContent(
                    ContentMaker.fromMultiModalData(
                            PartMaker.fromMimeTypeAndData(imageFile.getContentType(), imageFile.getBytes()),
                            "Is this photo a photo that can be uploaded to a real estate website?\n" +
                                    "Returns False value for blurry, distorted photos and photos with text on them. (only answer true or false)"
                    )
            );
            String responseText = ResponseHandler.getText(generateContentResponse);
            return Boolean.parseBoolean(responseText.trim().toLowerCase());
        } catch (IOException e) {
            e.printStackTrace();
            return true; // Varsayılan olarak uygun
        }
    }

    @PostMapping
    public String file(@RequestParam("file") MultipartFile file) throws IOException {
        boolean isPhotoSuitable = checkPhotoSuitability(file);
        return isPhotoSuitable ? "Photo is suitable for upload." : "Photo is not suitable for upload.";
    }
}

