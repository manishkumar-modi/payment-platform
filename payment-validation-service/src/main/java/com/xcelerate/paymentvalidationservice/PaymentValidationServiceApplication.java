package com.xcelerate.paymentvalidationservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Entry point for the payment validation service.
 */
@SpringBootApplication
public class PaymentValidationServiceApplication {

	/**
	 * Starts the payment validation service.
	 *
	 * @param args application arguments supplied by the JVM
	 */
	public static void main(String[] args) {
		SpringApplication.run(PaymentValidationServiceApplication.class, args);
	}

}
