package com.dung.democrm;

import com.dung.democrm.repository.impl.BaseRepositoryImpl;
import io.github.cdimascio.dotenv.Dotenv;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories(repositoryBaseClass = BaseRepositoryImpl.class)
@SpringBootApplication
@RequiredArgsConstructor
public class DemocrmApplication {

	public static void main(String[] args) {
        SpringApplication.run(DemocrmApplication.class, args);
	}

}
