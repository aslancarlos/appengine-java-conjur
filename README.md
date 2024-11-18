# Spring Boot Application with Conjur Authn-GCP Integration

$1

**Note**: This project is intended as a proof of concept and test for this integration. It does not have official support from CyberArk.

## Features

- **Spring Boot**: Utilizes the Spring Boot framework for building RESTful APIs.
- **Conjur Integration**: Securely manages secrets using Conjur, an enterprise-grade secret management tool.
- **Authn-GCP Authentication**: Authenticates with Conjur via Google Cloud Platform (GCP) using JSON Web Tokens (JWT).
- **Environment-specific Secret Management**: Supports different environments for credential retrieval, allowing dynamic and secure configuration.

## Prerequisites

- Java 11 or higher
- Maven 3.6 or higher
- Google Cloud Platform account with the necessary IAM roles
- Conjur configured with Authn-GCP integration

## Getting Started

### Clone the Repository
```sh
git clone https://github.com/your-username/your-repository-name.git
cd your-repository-name
```

### Configuration

1. **Conjur Configuration**:
   - Set up Conjur and enable the `authn-gcp` authenticator.
   - Define the required policies and load the secrets.

2. **Google Cloud Configuration**:
   - Ensure that the GCP service account being used has the necessary permissions to generate identity tokens.
   - Add the GCP service account to the Conjur policy to allow authentication.

3. **Application Properties**:
   - Update `src/main/resources/application.properties` with your configuration values.
     ```properties
     conjur.auth.url=https://your-conjur-instance/authn-gcp/your-account-name
     conjur.secrets.url=https://your-conjur-instance/secrets/your-account-name/variable/
     gcp.project-id=your-gcp-project-id
     gcp.service-account-email=your-service-account-email
     ```

   - Replace the placeholders with your actual configuration values.

### Building the Project

```sh
mvn clean install
```

### Running the Application

```sh
mvn spring-boot:run
```

The application will start on port `8080` by default. You can access it at `http://localhost:8080/conjur`.

## Usage

Once the application is running, it will authenticate with Conjur using the provided GCP JWT and retrieve the configured secrets.

## Important Parameters to Update

Make sure to update the following parameters before running the application:

- Update the values of secrets in `ConjurController.java` to ensure the correct secret paths are used.
- `conjur.auth.url`: The Conjur authentication URL, including the Authn-GCP endpoint.
- `conjur.secrets.url`: The URL for accessing secrets in Conjur.
- `gcp.project-id`: The Google Cloud project ID where the service account resides.
- `gcp.service-account-email`: The email of the service account being used for authentication.

## Troubleshooting

- **Authentication Errors**: Make sure that the GCP service account has the correct permissions and that the Conjur policy allows it to authenticate.
- **Secret Retrieval Errors**: Verify that the secret paths in Conjur are correct and that the application has the right permissions to access them.

## Contributing

Contributions are welcome! Please fork this repository and open a pull request to propose any changes.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Contact

If you have any questions or need further assistance, feel free to contact us.

