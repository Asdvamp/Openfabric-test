package ai.openfabric.api.controller;

import ai.openfabric.api.dtos.ContainerListPageDTO;
import ai.openfabric.api.dtos.WorkerResponseDTO;
import ai.openfabric.api.dtos.WorkerStatsResponseDTO;
import ai.openfabric.api.enums.ContainerStatus;
import ai.openfabric.api.exceptions.WorkerException;
import ai.openfabric.api.services.IWorkerService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${node.api.path}/worker")
public class WorkerController {
    
    @Autowired IWorkerService workerService;

    @GetMapping(path = "/{workerId}/info")
    public @ResponseBody WorkerResponseDTO workerInfo(@PathVariable String workerId) throws WorkerException {
        return workerService.getWorkerInformation(workerId);
    }

    @GetMapping(path = "/lists")
    public @ResponseBody List<WorkerResponseDTO> workerList(
        @RequestBody ContainerListPageDTO pageInfo) throws WorkerException {
        return workerService.getContainerList(pageInfo);
    }

    @PostMapping(path = "/{workerId}/update-status")
    public @ResponseBody WorkerResponseDTO updateStatus(
        @PathVariable String workerId, @RequestHeader ContainerStatus newStatus) throws WorkerException {
        return workerService.updateWorkerStatus(workerId, newStatus);
    }

    @GetMapping(path = "/{workerId}/stats")
    public @ResponseBody WorkerStatsResponseDTO workerStatus(@PathVariable String workerId) throws WorkerException {
        return workerService.getWorkerStatistics(workerId);
    }
}
