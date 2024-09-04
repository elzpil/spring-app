package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import java.io.File;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@SpringBootApplication
@PropertySource("file:${user.dir}/.env")
public class DemoApplication {
	private static final Logger logger = LogManager.getLogger(DemoApplication.class);
	public static void main(String[] args) {

		SpringApplication.run(DemoApplication.class, args);
		File logFile = new File("src/logs/app.log");
		if (logFile.exists()) {
			if (logFile.canWrite()) {
				System.out.println("File is writable.");
			} else {
				System.out.println("File is not writable.");
			}
		} else {
			System.out.println("File does not exist.");
		}


		logger.debug("This is a debug log message");
		logger.info("This is an info log message");
	}

}
