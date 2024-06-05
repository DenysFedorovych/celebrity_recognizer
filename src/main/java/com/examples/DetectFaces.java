package com.examples;

import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.AmazonRekognitionClientBuilder;
import com.amazonaws.services.rekognition.model.*;
import com.amazonaws.util.IOUtils;

import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.List;

public class DetectFaces {

    public static String detectFaces(String photo) {
        ByteBuffer imageBytes = null;
        try (InputStream inputStream = new FileInputStream(photo)) {
            imageBytes = ByteBuffer.wrap(IOUtils.toByteArray(inputStream));
        } catch (Exception e) {
            System.out.println("Failed to load file " + photo);
            System.exit(1);
        }

        AmazonRekognition rekognitionClient = AmazonRekognitionClientBuilder.defaultClient();

        DetectFacesRequest request = new DetectFacesRequest()
                .withImage(new Image().withBytes(imageBytes))
                .withAttributes(Attribute.ALL); // Replace Attribute.ALL with Attribute.DEFAULT to get default values.

        DetectFacesResult result = rekognitionClient.detectFaces(request);

        return parseResult(result, request);
    }

    private static String parseResult(DetectFacesResult result, DetectFacesRequest request) {
        StringBuilder parsedResult = new StringBuilder();

        List<FaceDetail> faceDetails = result.getFaceDetails();

        if (faceDetails.isEmpty()) {
            return "No faces detected.";
        }

        for (FaceDetail face : faceDetails) {
            if (request.getAttributes().contains("ALL")) {
                AgeRange ageRange = face.getAgeRange();
                parsedResult.append("The detected face is estimated to be between ")
                        .append(ageRange.getLow()).append(" and ").append(ageRange.getHigh())
                        .append(" years old.\n");
            }

            Gender gender = face.getGender();
            parsedResult.append("Gender: ").append(gender.getValue()).append(" (Confidence: ").append(gender.getConfidence()).append("%)\n");

            parsedResult.append("Emotions:\n");
            List<Emotion> emotions = face.getEmotions();
            for (Emotion emotion : emotions) {
                parsedResult.append("  ").append(emotion.getType()).append(": ").append(emotion.getConfidence()).append("%\n");
            }

            Beard beard = face.getBeard();
            parsedResult.append("Beard: ").append(beard.getValue()).append(" (Confidence: ").append(beard.getConfidence()).append("%)\n");

            Mustache mustache = face.getMustache();
            parsedResult.append("Mustache: ").append(mustache.getValue()).append(" (Confidence: ").append(mustache.getConfidence()).append("%)\n");

            Eyeglasses eyeglasses = face.getEyeglasses();
            parsedResult.append("Eyeglasses: ").append(eyeglasses.getValue()).append(" (Confidence: ").append(eyeglasses.getConfidence()).append("%)\n");

            Sunglasses sunglasses = face.getSunglasses();
            parsedResult.append("Sunglasses: ").append(sunglasses.getValue()).append(" (Confidence: ").append(sunglasses.getConfidence()).append("%)\n");

            EyeOpen eyesOpen = face.getEyesOpen();
            parsedResult.append("Eyes Open: ").append(eyesOpen.getValue()).append(" (Confidence: ").append(eyesOpen.getConfidence()).append("%)\n");

            MouthOpen mouthOpen = face.getMouthOpen();
            parsedResult.append("Mouth Open: ").append(mouthOpen.getValue()).append(" (Confidence: ").append(mouthOpen.getConfidence()).append("%)\n");

            parsedResult.append("\n");
        }

        return parsedResult.toString();
    }
}
