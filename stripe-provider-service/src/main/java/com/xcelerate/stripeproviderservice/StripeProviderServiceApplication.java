package com.xcelerate.stripeproviderservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Entry point for the Stripe provider service.
 */
@SpringBootApplication
public class StripeProviderServiceApplication {

	/**
	 * Starts the Stripe provider service.
	 *
	 * @param args application arguments supplied by the JVM
	 */
	public static void main(String[] args) {
		SpringApplication.run(StripeProviderServiceApplication.class, args);
	}

}
