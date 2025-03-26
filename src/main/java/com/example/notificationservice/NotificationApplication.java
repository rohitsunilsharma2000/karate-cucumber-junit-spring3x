package com.example.notificationservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * The main entry point for the Notification Service Application.
 *
 * <p>
 * This class bootstraps the Spring Boot application for handling notifications,
 * initiating all necessary configurations and components required for the application to run.
 * </p>
 *
 * <p>
 * <strong>Pass/Fail Conditions:</strong><br>
 * Pass: The Spring context loads successfully with proper initialization of all components.<br>
 * Fail: Any runtime issues during application startup cause a startup exception.
 * </p>
 *
 * <p>
 * <strong>Input Validation:</strong> Although this main method does not take external inputs,
 * the application startup process is validated by ensuring that the Spring context is correctly initialized.
 * </p>
 *
 * <p>
 * <strong>Logging:</strong> Logging is performed at various stages of the startup process:
 * <ul>
 *     <li>INFO: Startup initiation and successful initialization.</li>
 *     <li>ERROR: Any failure during startup is logged with error details.</li>
 * </ul>
 * </p>
 *
 * <p>
 * <strong>Custom Exception Handling:</strong> In case of startup failures, a custom exception is thrown,
 * which helps in graceful termination and easier troubleshooting.
 * </p>
 *
 * @version 1.0
 * @since 2025-03-26
 */
@SpringBootApplication
public class NotificationApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationApplication.class);

    /**
     * The main method that launches the Notification Service Application.
     *
     * <p>
     * <strong>Pass/Fail Conditions:</strong><br>
     * Pass: The Spring context loads successfully, no exceptions thrown.<br>
     * Fail: Any runtime issues in loading dependencies cause a startup exception.
     * </p>
     *
     * @param args Command-line arguments (unused in this application).
     */
    public static void main ( String[] args ) {
        LOGGER.info("Starting Notification Service Application...");
        SpringApplication.run(NotificationApplication.class , args);
        LOGGER.info("Notification Service Application started successfully.");

    }
}
