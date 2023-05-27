package ai.openfabric.api.services.impl;

import ai.openfabric.api.client.DockerClientUtils;
import ai.openfabric.api.dtos.ContainerListPageDTO;
import ai.openfabric.api.dtos.PageFilters;
import ai.openfabric.api.dtos.WorkerResponseDTO;
import ai.openfabric.api.dtos.WorkerStatsResponseDTO;
import ai.openfabric.api.enums.ContainerStatus;
import ai.openfabric.api.exceptions.WorkerErrorDetail;
import ai.openfabric.api.exceptions.WorkerException;
import ai.openfabric.api.helper;
import ai.openfabric.api.model.Worker;
import ai.openfabric.api.repository.WorkerRepository;
import ai.openfabric.api.services.IWorkerService;
import ai.openfabric.api.services.mappers.WorkerMapper;
import com.github.dockerjava.api.model.Statistics;
import java.awt.print.Pageable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class WorkerServiceImpl implements IWorkerService {

    private DockerClientUtils dockerClientUtils = DockerClientUtils.getInstance();
    
    @Autowired WorkerRepository workerRepository;


    @Override
    public WorkerResponseDTO startWorker(String workerId) throws WorkerException {
        Worker worker = fetchAndPersistWorkerInfo(workerId);
        if (!worker.getStatus().equals(ContainerStatus.running)) {
            dockerClientUtils.startContainer(workerId);
            worker = fetchAndPersistWorkerInfo(workerId);
        }
        return WorkerMapper.toWorkerResponseDTO(worker);
    }

    @Override
    public WorkerResponseDTO stopWorker(String workerId) throws WorkerException {
        Worker worker = fetchAndPersistWorkerInfo(workerId);
        if (!worker.getStatus().equals(ContainerStatus.paused)) {
            dockerClientUtils.stopContainer(workerId);
            worker = fetchAndPersistWorkerInfo(workerId);
        }
        return WorkerMapper.toWorkerResponseDTO(worker);
    }

    @Override
    public WorkerResponseDTO getWorkerInformation(String workerId) throws WorkerException {
        Worker worker = fetchAndPersistWorkerInfo(workerId);
        return WorkerMapper.toWorkerResponseDTO(worker);
    }

    @Override
    public List<WorkerResponseDTO> getContainerList(ContainerListPageDTO pageInfo) {
        if (Objects.isNull(pageInfo)){
            return new ArrayList<>();
        }
        int pageSize = helper.getNullSafeObject(() -> pageInfo.getPageSize()).orElse(10);
        int pageNumber = helper.getNullSafeObject(() -> pageInfo.getPageNumber()).orElse(0);
        Pageable pageable = (Pageable) PageRequest.of(pageNumber, pageSize);

        List<Worker> filteredWorkers = new ArrayList<> ();
        List<Worker> syncedWorkers = dockerClientUtils.currentContainers();
        workerRepository.saveAll(syncedWorkers);

        if (Objects.nonNull(pageInfo.getPageFilters())) {
            PageFilters pageFilters = pageInfo.getPageFilters();
            if (Objects.nonNull(pageFilters.getContainerId())) {
                filteredWorkers = workerRepository.findAllById(pageFilters.getContainerId(), pageable);
            } else if (Objects.nonNull(pageFilters.getStatus())) {
                filteredWorkers = workerRepository.findAllByStatus(pageFilters.getStatus(), pageable);
            }
        } else {
            filteredWorkers = workerRepository.findAll(pageable);
        }
        return filteredWorkers.stream()
            .map(WorkerMapper::toWorkerResponseDTO)
            .collect(Collectors.toList());
    }

    @Override
    public WorkerStatsResponseDTO getWorkerStatistics(String workerId) throws WorkerException {
        Statistics stats = dockerClientUtils.getContainerStatistics(workerId);
        WorkerStatsResponseDTO workerStats = WorkerMapper.toWorkerStatsResponse(stats);
        workerStats.setWorkerId(workerId);
        return workerStats;
    }

    private Worker fetchAndPersistWorkerInfo(String workerId) throws WorkerException {
        Worker workerDetail = dockerClientUtils.getContainerInfo(workerId);
        if (Objects.isNull(workerDetail)) {
            throw new WorkerException(WorkerErrorDetail.builder()
                .errorCode("1234")
                .errorMessage("no worker with this Id.").build());
        }
        return workerRepository.save(workerDetail);
    }

    private void validateWorkerId(String workerId) throws WorkerException {

    }
}
