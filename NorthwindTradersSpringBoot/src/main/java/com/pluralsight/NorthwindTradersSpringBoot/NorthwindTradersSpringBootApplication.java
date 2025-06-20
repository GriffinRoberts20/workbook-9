package com.pluralsight.NorthwindTradersSpringBoot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class NorthwindTradersSpringBootApplication {

	public static void main(String[] args) throws Exception{
		if (args.length != 2) {
			System.out.println("Usage: java -jar app.jar <username> <password>");
			System.exit(1);
		}

		System.setProperty("dbUsername", args[0]);
		System.setProperty("dbPassword", args[1]);


		SpringApplication.run(NorthwindTradersSpringBootApplication.class, args);
	}

}
