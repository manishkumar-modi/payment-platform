package com.xcelerate.paymentprocessingservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Entry point for the payment processing service.
 */
@SpringBootApplication
public class PaymentProcessingServiceApplication {

	/**
	 * Starts the payment processing service.
	 *
	 * @param args application arguments supplied by the JVM
	 */
	public static void main(String[] args) {
		SpringApplication.run(PaymentProcessingServiceApplication.class, args);
	}

}
