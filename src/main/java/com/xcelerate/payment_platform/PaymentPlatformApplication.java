package com.xcelerate.payment_platform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * PaymentPlatformApplication is the main entry point for the Payment Platform Spring Boot application.
 * 
 * This application provides REST APIs and services for handling payment transactions, including
 * payment processing, transaction management, and related financial operations.
 * 
 * The application is configured with Spring Boot auto-configuration and includes all necessary
 * components for a web-based payment processing system.
 * 
 * @author Xcelerate Payment Platform Team
 * @version 1.0
 * @since 1.0
 */
@SpringBootApplication
public class PaymentPlatformApplication {

	/**
	 * Main method to start the Payment Platform Spring Boot application.
	 * 
	 * This method serves as the entry point for the application and initializes the Spring
	 * application context with all configured beans and auto-configurations.
	 * 
	 * @param args Command line arguments passed to the application (optional)
	 */
	public static void main(String[] args) {
		SpringApplication.run(PaymentPlatformApplication.class, args);
	}

}
