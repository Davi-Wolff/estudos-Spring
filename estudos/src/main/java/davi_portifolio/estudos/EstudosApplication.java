package davi_portifolio.estudos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAutoConfiguration
@EnableAsync
@SpringBootApplication
public class EstudosApplication {
	static void main(String[] args) {
		SpringApplication.run(EstudosApplication.class, args);
	}
}
