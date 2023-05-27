package ai.openfabric.api.services.mappers;

import ai.openfabric.api.dtos.WorkerResponseDTO;
import ai.openfabric.api.dtos.WorkerStatsResponseDTO;
import ai.openfabric.api.enums.ContainerStatus;
import ai.openfabric.api.model.Worker;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.api.model.Statistics;

import java.util.Arrays;


public class WorkerMapper {
    public static Worker toWorker(Container container) {
        if (container == null) {
            return null;
        }
    // handler npes.

    return Worker.builder()
        .id(container.getId())
        .workerNames(Arrays.asList(container.getNames()))
        .status(ContainerStatus.valueOf(container.getStatus()))
        .dockerImageName(container.getImage())
        .dockerImageId(container.getImageId())
        .created(container.getCreated())
        .build();
    }

    public static WorkerResponseDTO toWorkerResponseDTO(Worker worker) {
        if (worker == null) {
            return null;
        }
    return WorkerResponseDTO.builder()
        .containerId(worker.getId())
        .containerNames(worker.getWorkerNames())
        .imageName(worker.getDockerImageName())
        .port(worker.getPort())
        .status(worker.getStatus())
        .created(worker.getCreated())
        .build();
    }

    public static WorkerStatsResponseDTO toWorkerStatsResponse(Statistics statistics) {
        if (statistics == null) {
            return null;
        }
        return WorkerStatsResponseDTO.builder()
            .cpuUsage(statistics.getCpuStats().getCpuUsage().getTotalUsage())
            .memoryLimit(statistics.getMemoryStats().getLimit())
            .memoryUsage(statistics.getMemoryStats().getUsage())
            .pid(statistics.getPidsStats().getCurrent())
            .build();
    }
}
