package ai.openfabric.api.repository;

import ai.openfabric.api.enums.ContainerStatus;
import ai.openfabric.api.model.Worker;
import java.awt.print.Pageable;
import java.util.List;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface WorkerRepository extends PagingAndSortingRepository<Worker, String> { 

    List<Worker> saveAll(List<Worker> workers);

    Worker save(Worker worker);
    
    List<Worker> findAllByStatus(ContainerStatus status, Pageable pageable);

    List<Worker> findAll(Pageable pageable);

    List<Worker> findAllById(String id, Pageable pageable);
}
