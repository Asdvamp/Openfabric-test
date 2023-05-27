package ai.openfabric.api.exceptions;

import org.springframework.web.bind.annotation.ExceptionHandler;


public class WorkerExceptionHandler {

    @ExceptionHandler(WorkerException.class)
    public WorkerErrorDetail handleException(WorkerException workerException) {
        return workerException.getErrorDetail();
    }
}
