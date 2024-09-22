# AWS Lambda Image Resizer

This project implements an AWS Lambda function that resizes images on demand. It's written in Java and uses the Thumbnailator library for image processing.

## Features

- Resizes images to specified dimensions
- Accepts Base64 encoded images as input
- Returns Base64 encoded resized images
- Deployed as an AWS Lambda function for scalable, serverless operation

## Prerequisites

- Java 11 or later
- Gradle
- AWS account with Lambda access
- AWS CLI configured with your credentials

## Building the Project

To build the project, run:

```bash
./gradlew build
```

This will compile the code, run tests, and create a JAR file in the `build/libs/` directory.

## Deploying to AWS Lambda

1. Build the project as described above.
2. In the AWS Lambda console, create a new function.
3. Choose "Java 11" (or your Java version) as the runtime.
4. Upload the JAR file from `build/libs/`.
5. Set the handler to: `cz.vektor330.ImageResizeHandler::handleRequest`

## Testing

### Unit Tests

To run unit tests:

```bash
./gradlew test
```

### Integration Tests

To run integration tests (which will invoke your deployed Lambda function):

1. Ensure your AWS credentials are properly configured.
2. Update the `FUNCTION_NAME` and `AWS_REGION` in `LambdaIntegrationTest.java`.
3. Run:

```bash
./gradlew test --tests LambdaIntegrationTest
```

## Usage

The Lambda function expects a JSON input with the following structure:

```json
{
  "image": "iVBORw0KGgoAAAANSUhEUgAAAAoAAAAKCAYAAACNMs+9AAAAFUlEQVR42mP8z8BQz0AEYBxVSF+FABJADveWkH6oAAAAAElFTkSuQmCC",
  "width": "300",
  "height": "200"
}
```

It will return a Base64 encoded string of the resized image.

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
