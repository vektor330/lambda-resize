package cz.vektor330;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.Map;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import net.coobird.thumbnailator.Thumbnails;

public class ImageResizeHandler implements RequestHandler<Map<String, String>, String> {

  @Override
  public String handleRequest(final Map<String, String> input, final Context context) {
    context.getLogger().log("Received event: " + input);

    final String imageBase64 = input.get("image");
    final int width = Integer.parseInt(input.get("width"));
    final int height = Integer.parseInt(input.get("height"));

    try {
      // Decode base64 image
      final byte[] imageBytes = Base64.getDecoder().decode(imageBase64);

      // Resize image
      final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
      Thumbnails.of(new ByteArrayInputStream(imageBytes))
          .size(width, height)
          .outputFormat("JPEG")
          .toOutputStream(outputStream);

      // Encode resized image to base64
      return Base64.getEncoder().encodeToString(outputStream.toByteArray());
    } catch (Exception e) {
      context.getLogger().log("Error processing image: " + e.getMessage());
      return "Error: " + e.getMessage();
    }
  }

}