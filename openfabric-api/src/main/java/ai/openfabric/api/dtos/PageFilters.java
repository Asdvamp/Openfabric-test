package ai.openfabric.api.dtos;

import ai.openfabric.api.enums.ContainerStatus;
import lombok.Data;

@Data
public class PageFilters {
    ContainerStatus status;
    Long created;
    String containerId;
}
