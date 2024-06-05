package com.examples;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.List;

import com.amazonaws.services.rekognition.model.DetectLabelsRequest;
import com.amazonaws.services.rekognition.model.DetectLabelsResult;
import com.amazonaws.services.rekognition.model.Image;
import com.amazonaws.services.rekognition.model.Label;
import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.AmazonRekognitionClientBuilder;
import com.amazonaws.util.IOUtils;


public class DetectLabels {

    public static String detectLabels(String photo) {
        ByteBuffer imageBytes = null;
        try (InputStream inputStream = new FileInputStream(photo)) {
            imageBytes = ByteBuffer.wrap(IOUtils.toByteArray(inputStream));
        } catch (Exception e) {
            System.out.println("Failed to load file " + photo);
            System.exit(1);
        }

        AmazonRekognition rekognitionClient = AmazonRekognitionClientBuilder.defaultClient();

        DetectLabelsRequest request = new DetectLabelsRequest()
                .withImage(new Image().withBytes(imageBytes))
                .withMaxLabels(10).withMinConfidence(75F);

        DetectLabelsResult result = rekognitionClient.detectLabels(request);

        return parseResult(result);
    }

    private static String parseResult(DetectLabelsResult result) {
        StringBuilder parsedResult = new StringBuilder();

        List<Label> labels = result.getLabels();
        parsedResult.append("Detected labels:\n\n");

        for (Label label : labels) {
            parsedResult.append(label.getName()).append(", Confidence: ").append(label.getConfidence()).append("\n");
        }

        return parsedResult.toString();
    }
}
