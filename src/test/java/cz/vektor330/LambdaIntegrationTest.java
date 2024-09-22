package cz.vektor330;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import org.junit.Before;
import org.junit.Test;

import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.lambda.AWSLambda;
import com.amazonaws.services.lambda.AWSLambdaClientBuilder;
import com.amazonaws.services.lambda.model.InvokeRequest;
import com.amazonaws.services.lambda.model.InvokeResult;

public class LambdaIntegrationTest {

  private AWSLambda lambdaClient;

  private static final String SAMPLE_IMAGE_BASE_64 = "iVBORw0KGgoAAAANSUhEUgAAAAoAAAAKCAYAAACNMs+9AAAAFUlEQVR42mP8z8BQz0AEYBxVSF+FABJADveWkH6oAAAAAElFTkSuQmCC";

  private static final String FUNCTION_NAME = "ImageResizer";

  private static final String AWS_REGION = "us-east-1"; // Replace with your AWS region

  @Before
  public void setUp() {
    lambdaClient = AWSLambdaClientBuilder.standard()
        .withCredentials(new ProfileCredentialsProvider())
        .withRegion(Regions.fromName(AWS_REGION))
        .build();
  }

  @Test
  public void testLambdaFunction() {
    final String testInput = """
        {
          "image": "%s",
          "width": "50",
          "height": "50"
        }""".formatted(SAMPLE_IMAGE_BASE_64);

    final InvokeRequest invokeRequest = new InvokeRequest().withFunctionName(FUNCTION_NAME)
        .withPayload(ByteBuffer.wrap(testInput.getBytes(StandardCharsets.UTF_8)));

    final InvokeResult invokeResult = lambdaClient.invoke(invokeRequest);

    final String result = new String(invokeResult.getPayload().array(), StandardCharsets.UTF_8);

    assertNotNull("Result must be present", result);
    assertTrue("We expect this to be a string in quote marks starting with a forward slash", result.startsWith("\"/"));
  }

}