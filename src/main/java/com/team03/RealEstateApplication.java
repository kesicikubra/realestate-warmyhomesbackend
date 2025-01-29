package com.team03;

import com.team03.initializer.DataLoader;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Profile;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableCaching
@EnableScheduling
public class RealEstateApplication implements CommandLineRunner {

	private final DataLoader dataLoader;

	public RealEstateApplication(DataLoader dataLoader) {
        this.dataLoader = dataLoader;
    }

	public static void main(String[] args) {
		SpringApplication.run(RealEstateApplication.class, args);
	}

	@Profile("prod")
	@Override
	public void run(String... args) throws Exception {

		dataLoader.allDataLoader();
	}
}
