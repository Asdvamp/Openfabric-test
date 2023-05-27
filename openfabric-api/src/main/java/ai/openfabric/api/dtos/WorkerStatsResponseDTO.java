package ai.openfabric.api.dtos;


import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class WorkerStatsResponseDTO {

    private String workerId;
    private String workerName;
    private double cpuUsage;
    private double memoryUsage;
    private double memoryLimit;
    private double netIO;
    private double blockIO;
    private double pid;
}
