package ai.openfabric.api.dtos;

import lombok.Data;

@Data
public class ContainerListPageDTO {
    public int pageSize;
    public int pageNumber;
    public PageFilters pageFilters;
}
