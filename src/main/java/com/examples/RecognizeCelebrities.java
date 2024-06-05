package com.examples;

import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.AmazonRekognitionClientBuilder;
import com.amazonaws.services.rekognition.model.*;

import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import com.amazonaws.util.IOUtils;
import java.util.List;


public class RecognizeCelebrities {

    public static String recogizeCelebrities(String photo) {
        ByteBuffer imageBytes=null;
        try (InputStream inputStream = new FileInputStream(photo)) {
            imageBytes = ByteBuffer.wrap(IOUtils.toByteArray(inputStream));
        } catch(Exception e) {
            System.out.println("Failed to load file " + photo);
            System.exit(1);
        }

        AmazonRekognition rekognitionClient = AmazonRekognitionClientBuilder.defaultClient();

        RecognizeCelebritiesRequest request = new RecognizeCelebritiesRequest()
                .withImage(new Image()
                        .withBytes(imageBytes));

        RecognizeCelebritiesResult result=rekognitionClient.recognizeCelebrities(request);

        return parseResult(result);
    }

    private static String parseResult(RecognizeCelebritiesResult result) {
        StringBuilder parsedResult = new StringBuilder();

        List<Celebrity> celebs = result.getCelebrityFaces();
        parsedResult.append(celebs.size()).append(" celebrity(s) were recognized.\n\n");

        for (Celebrity celebrity : celebs) {
            parsedResult.append("Celebrity recognized: ").append(celebrity.getName()).append(", Confidence: ").append(celebrity.getMatchConfidence()).append("\n");

            parsedResult.append("Further information (if available):\n");
            for (String url : celebrity.getUrls()) {
                parsedResult.append(url).append("\n");
            }

            parsedResult.append("Emotions:\n");
            List<Emotion> emotions = celebrity.getFace().getEmotions();
            for (Emotion emotion : emotions) {
                parsedResult.append(emotion.getType()).append(": ").append(emotion.getConfidence()).append("%\n");
            }

            parsedResult.append("\n");
        }

        parsedResult.append(result.getUnrecognizedFaces().size()).append(" face(s) were unrecognized.\n");

        return parsedResult.toString();
    }

}
