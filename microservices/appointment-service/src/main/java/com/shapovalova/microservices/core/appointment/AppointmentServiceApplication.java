package com.shapovalova.microservices.core.appointment;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
@ComponentScan("com.shapovalova")
public class AppointmentServiceApplication {

	private static final Logger LOG = LoggerFactory.getLogger(AppointmentServiceApplication.class);
	public static void main(String[] args) {
		ConfigurableApplicationContext ctx = SpringApplication.run(AppointmentServiceApplication.class, args);

		String mysqlUri = ctx.getEnvironment().getProperty("spring.datasource.url");
		LOG.info("Connected to MySQL: " + mysqlUri);
	}
}
