package dev.matheuslf.desafio.inscritos;

import io.github.cdimascio.dotenv.Dotenv;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class InscritosApplication {

    public static void main(String[] args) {
        try {
            Dotenv dotenv = Dotenv.configure()
                    .directory("env")
                    .ignoreIfMalformed()
                    .ignoreIfMissing()
                    .load();

            dotenv.entries().forEach(entry ->
                    System.setProperty(entry.getKey(), entry.getValue())
            );
            SpringApplication.run(InscritosApplication.class, args);
        } catch (Exception e) {
            log.error("Error while running application: {}", e.getMessage());
        }
        log.info("DATASOURCE_URL = {}", System.getenv("DATASOURCE_URL"));
        log.info("DATASOURCE_USERNAME = {}", System.getenv("DATASOURCE_USERNAME"));
        log.info("DATASOURCE_PASSWORD = {}", System.getenv("DATASOURCE_PASSWORD"));
        log.info("GESTAO_PROJETOS_API_SECURITY_SECRET = {}", System.getenv("GESTAO_PROJETOS_API_SECURITY_SECRET"));
    }

}
