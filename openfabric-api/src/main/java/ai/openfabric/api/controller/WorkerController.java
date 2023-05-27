package ai.openfabric.api.controller;

import ai.openfabric.api.dtos.ContainerListPageDTO;
import ai.openfabric.api.dtos.WorkerResponseDTO;
import ai.openfabric.api.dtos.WorkerStatsResponseDTO;
import ai.openfabric.api.exceptions.WorkerException;
import ai.openfabric.api.services.IWorkerService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${node.api.path}/worker")
public class WorkerController {
    
    @Autowired IWorkerService workerService;

    @GetMapping(path = "/info")
    public @ResponseBody WorkerResponseDTO workerInfo(@RequestBody String workerId) throws WorkerException {
        return workerService.getWorkerInformation(workerId);
    }

    @GetMapping(path = "/list")
    public @ResponseBody List<WorkerResponseDTO> workerList(@RequestBody ContainerListPageDTO pageInfo) throws WorkerException {
        return workerService.getContainerList(pageInfo);
    }

    @PostMapping(path = "/start")
    public @ResponseBody WorkerResponseDTO startWorker(@RequestBody String workerId) throws WorkerException {
        return workerService.startWorker(workerId);
    }

    @PostMapping(path = "/stop")
    public @ResponseBody WorkerResponseDTO stopWorker(@RequestBody String workerId) throws WorkerException {
        return workerService.stopWorker(workerId);
    }

    @PostMapping(path = "/stats")
    public @ResponseBody WorkerStatsResponseDTO workerStatus(@RequestBody String workerId) throws WorkerException {
        return workerService.getWorkerStatistics(workerId);
    }
}
