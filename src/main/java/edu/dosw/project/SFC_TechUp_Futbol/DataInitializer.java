package edu.dosw.project.SFC_TechUp_Futbol;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final DataInitializerService dataInitializerService;

    public DataInitializer(DataInitializerService dataInitializerService) {
        this.dataInitializerService = dataInitializerService;
    }

    @Override
    public void run(String... args) {
        dataInitializerService.sembrarAdmin();
    }
}