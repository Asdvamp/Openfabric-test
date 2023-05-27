package ai.openfabric.api.services;

import ai.openfabric.api.dtos.ContainerListPageDTO;
import ai.openfabric.api.dtos.WorkerResponseDTO;
import ai.openfabric.api.dtos.WorkerStatsResponseDTO;
import ai.openfabric.api.enums.ContainerStatus;
import ai.openfabric.api.exceptions.WorkerException;

import java.util.List;

public interface IWorkerService {
    WorkerResponseDTO updateWorkerStatus(String workerId, ContainerStatus newStatus) throws WorkerException;

    WorkerResponseDTO getWorkerInformation(String workerId) throws WorkerException;

    List<WorkerResponseDTO> getContainerList(ContainerListPageDTO pageInfo) throws WorkerException;

    WorkerStatsResponseDTO getWorkerStatistics(String workerId) throws WorkerException;
}
