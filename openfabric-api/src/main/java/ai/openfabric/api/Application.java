package ai.openfabric.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@SpringBootApplication
@EnableTransactionManagement
public class Application {

    // add configruation paths.

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}