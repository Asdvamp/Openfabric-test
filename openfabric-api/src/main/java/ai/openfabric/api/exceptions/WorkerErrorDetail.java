package ai.openfabric.api.exceptions;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class WorkerErrorDetail {
    private String errorCode;
    private String errorMessage;
}
