package sk.bacigala.hikeplanner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages={"sk.bacigala"})
public class HikePlannerApplication {

	public static void main(String[] args) {
		SpringApplication.run(HikePlannerApplication.class, args);
	}

}
