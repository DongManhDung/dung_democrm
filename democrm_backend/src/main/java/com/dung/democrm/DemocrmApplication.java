package com.dung.democrm;

import com.dung.democrm.repository.impl.BaseRepositoryImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories(repositoryBaseClass = BaseRepositoryImpl.class)
@SpringBootApplication
public class DemocrmApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemocrmApplication.class, args);
	}

}
