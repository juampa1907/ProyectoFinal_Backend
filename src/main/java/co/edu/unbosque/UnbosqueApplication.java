package co.edu.unbosque;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@Slf4j
@SpringBootApplication
public class UnbosqueApplication {

	public static void main(String[] args) {
		SpringApplication.run(UnbosqueApplication.class, args);
	}

	@EventListener(ApplicationReadyEvent.class)
	public void onReady() {
		log.info("========================================");
		log.info("  ProyectoFinal Backend iniciado");
		log.info("  Context Path: /ProyectoFinal_Backend");
		log.info("========================================");
	}

}
