package ai.openfabric.api.client;

import ai.openfabric.api.config.DockerConfig;
import ai.openfabric.api.exceptions.WorkerErrorDetail;
import ai.openfabric.api.exceptions.WorkerException;
import ai.openfabric.api.model.Worker;
import ai.openfabric.api.services.mappers.WorkerMapper;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.api.model.Statistics;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.DockerClientImpl;
import com.github.dockerjava.core.InvocationBuilder;
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient;
import com.github.dockerjava.transport.DockerHttpClient;
import java.io.IOException;
import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

public class DockerClientUtils {

    @Autowired DockerConfig dockerConfig;
    
    private static DockerClientUtils client;
    private static com.github.dockerjava.api.DockerClient dockerClient; // for docker command execution.
    private static DockerHttpClient dockerHttpClient; // for docker raw queries.

    private DockerClientUtils() {
        init();
    }
    
    public static DockerClientUtils getInstance() {
        if (Objects.isNull(client)) {
            client = new DockerClientUtils();
        }
        return client;
    }

    private void init() {
        DockerClientConfig config = getDefaultClientConfiguration();
        dockerHttpClient = getDockerClient(getDefaultClientConfiguration());
        dockerClient = DockerClientImpl.getInstance(config, dockerHttpClient);
    }

    public List<Worker> currentContainers() {
        List<Container> containers = dockerClient.listContainersCmd().exec();
        return containers.stream()
            .map(WorkerMapper::toWorker)
            .collect(Collectors.toList());
    }

    public void startContainer(String containerId) {
        dockerClient.startContainerCmd(containerId).exec();
    }

    public Statistics getContainerStatistics(String containerId) throws WorkerException {
        InvocationBuilder.AsyncResultCallback<Statistics> callback = new InvocationBuilder.AsyncResultCallback<>();
        dockerClient.statsCmd(containerId).exec(callback);
        Statistics stats;
        try {
            stats = callback.awaitResult();
            callback.close();
        } catch (RuntimeException | IOException e) {
            throw new WorkerException(
                WorkerErrorDetail.builder().build(), 500);
        }
        return stats;
    }

    public Worker getContainerInfo(String containerId) {
        List<Container> containers =
            dockerClient.listContainersCmd()
                .withIdFilter(Collections.singletonList(containerId)).exec();
        return (StringUtils.isNotEmpty(containerId)) ? WorkerMapper.toWorker(containers.get(0)) : null;
    }

    public void stopContainer(String containerId) {
        dockerClient.stopContainerCmd(containerId).exec();
    }


    private DockerHttpClient getDockerClient(DockerClientConfig configuration) {
        return new ApacheDockerHttpClient.Builder()
            .dockerHost(configuration.getDockerHost())
            .sslConfig(configuration.getSSLConfig())
            .maxConnections(dockerConfig.getDockerClientMaxConnections())
            .connectionTimeout(Duration.ofSeconds(dockerConfig.getDockerClientConnectionTimeout()))
            .responseTimeout(Duration.ofSeconds(dockerConfig.getDockerClientResponseTimeout()))
            .build();
    }
    
    private DockerClientConfig getDefaultClientConfiguration() {
        return getClientConfiguration(dockerConfig);
    }
    
    private DockerClientConfig getClientConfiguration(DockerConfig dockerConfig) {
    return DefaultDockerClientConfig.createDefaultConfigBuilder()
        .withDockerHost(dockerConfig.getDockerHost())
        .withDockerTlsVerify(dockerConfig.isTlsVerificationRequired())
        .withDockerCertPath(dockerConfig.getDockerCertPath())
        .withRegistryUsername(dockerConfig.getRegistryUsername())
        .withRegistryPassword(dockerConfig.getRegistryPassword())
        .withRegistryEmail(dockerConfig.getRegistryMail())
        .withRegistryUrl(dockerConfig.getRegistryUrl())
        .build();
    }
}
