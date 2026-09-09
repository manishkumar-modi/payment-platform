package com.xcelerate.paypalproviderservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Entry point for the PayPal provider service.
 */
@SpringBootApplication
public class PaypalProviderServiceApplication {

	/**
	 * Starts the PayPal provider service.
	 *
	 * @param args application arguments supplied by the JVM
	 */
	public static void main(String[] args) {
		SpringApplication.run(PaypalProviderServiceApplication.class, args);
	}

}
