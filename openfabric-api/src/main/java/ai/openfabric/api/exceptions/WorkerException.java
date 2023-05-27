package ai.openfabric.api.exceptions;

import ai.openfabric.api.helper;
import lombok.Getter;

@Getter
public class WorkerException extends Exception {
    private WorkerErrorDetail errorDetail;
    private int status;

    public WorkerException(String message, WorkerErrorDetail errorDetail, int status) {
        super(message);
        this.errorDetail = errorDetail;
        this.status = status;
    }

    public WorkerException(WorkerErrorDetail errorDetail, int status) {
        this(helper.writeToString(errorDetail), errorDetail, status);
    }

    public WorkerException(WorkerErrorDetail errorDetail) {
        this(errorDetail, 400);
    }
}
