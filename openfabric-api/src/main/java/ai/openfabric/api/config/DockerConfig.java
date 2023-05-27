package ai.openfabric.api.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties("${docker-config}")
public class DockerConfig {
    private String dockerHost;
    private boolean tlsVerificationRequired;
    private String dockerCertPath;
    private String registryUsername;
    private String registryPassword;
    private String registryMail;
    private String registryUrl;
    private int dockerClientMaxConnections;
    private long dockerClientConnectionTimeout;
    private long dockerClientResponseTimeout;
}
