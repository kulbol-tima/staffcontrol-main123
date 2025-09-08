package kg.mlsp.staffcontrol;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories("kg.mlsp.staffcontrol.repository")
@EntityScan("kg.mlsp.staffcontrol.model")
public class StaffControlApplication {
	public static void main(String[] args) {
        SpringApplication.run(StaffControlApplication.class, args);
	}
}
