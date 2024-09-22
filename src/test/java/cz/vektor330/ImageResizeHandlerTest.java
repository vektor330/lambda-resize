package cz.vektor330;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ImageResizeHandlerTest {

  private ImageResizeHandler handler;

  @Mock
  private Context context;

  @Mock
  private LambdaLogger logger;

  @Before
  public void setUp() {
    MockitoAnnotations.openMocks(this);
    handler = new ImageResizeHandler();

    // Mock the context to return our mocked logger
    when(context.getLogger()).thenReturn(logger);
    // If needed, you can add behavior to the logger
    doNothing().when(logger).log(anyString());
  }

  @Test
  public void testHandleRequest() throws Exception {
    // Create a simple test image
    final BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
    final ByteArrayOutputStream baos = new ByteArrayOutputStream();
    ImageIO.write(image, "jpg", baos);
    final String base64Image = Base64.getEncoder().encodeToString(baos.toByteArray());

    // Prepare input
    final Map<String, String> input = new HashMap<>();
    input.put("image", base64Image);
    input.put("width", "50");
    input.put("height", "50");

    // Call the handler
    final String result = handler.handleRequest(input, context);

    // Verify the result
    assertNotNull("", result);
    assertTrue("Base64 images typically start with \"/\"", result.startsWith("/"));

    // Decode and check the resized image
    final byte[] resizedImageBytes = Base64.getDecoder().decode(result);
    final BufferedImage resizedImage = ImageIO.read(new ByteArrayInputStream(resizedImageBytes));
    assertEquals("Unexpected image width", 50, resizedImage.getWidth());
    assertEquals("Unexpected image height", 50, resizedImage.getHeight());
  }

  @Test
  public void testHandleRequestWithInvalidInput() {
    final Map<String, String> input = new HashMap<>();
    input.put("image", "not a valid base64 image");
    input.put("width", "50");
    input.put("height", "50");

    final String result = handler.handleRequest(input, context);

    assertTrue("Should be error", result.startsWith("Error:"));
    verify(logger, atLeastOnce()).log(anyString());
  }
}