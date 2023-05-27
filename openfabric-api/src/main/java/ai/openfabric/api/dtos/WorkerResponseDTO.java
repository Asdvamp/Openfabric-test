package ai.openfabric.api.dtos;

import ai.openfabric.api.enums.ContainerStatus;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkerResponseDTO {
    private String containerId;
    private List<String> containerNames;
    private Long created;
    private String imageName;
    private ContainerStatus status;
    private Long port;
}
