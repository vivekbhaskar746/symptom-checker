package com.symptomchecker.config;

import com.symptomchecker.entity.Pharmacy;
import com.symptomchecker.entity.User;
import com.symptomchecker.service.PharmacyService;
import com.symptomchecker.service.RAGService;
import com.symptomchecker.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final RAGService ragService;
    private final UserService userService;
    private final PharmacyService pharmacyService;

    public DataInitializer(RAGService ragService, UserService userService, PharmacyService pharmacyService) {
        this.ragService = ragService;
        this.userService = userService;
        this.pharmacyService = pharmacyService;
    }

    @Override
    public void run(String... args) throws Exception {
        ragService.initializeKnowledgeBase();
        initializeAdminUser();
        initializeSamplePharmacies();
    }

    private void initializeAdminUser() {
        if (!userService.existsByUsername("admin")) {
            User admin = new User("admin", "admin@symptomchecker.com", "Admin@123", "System Admin");
            admin.setRole(User.Role.ADMIN);
            userService.registerUser(admin);
        }
    }

    private void initializeSamplePharmacies() {
        if (pharmacyService.getTotalPharmacies() == 0) {
            pharmacyService.addPharmacy(new Pharmacy("City Medical Store", "12 MG Road, Bangalore", "080-12345678", "citymed@example.com", "Mon-Sat 8am-10pm"));
            pharmacyService.addPharmacy(new Pharmacy("Apollo Pharmacy", "45 Brigade Road, Bangalore", "080-87654321", "apollo@example.com", "24x7"));
            pharmacyService.addPharmacy(new Pharmacy("MedPlus", "78 Koramangala, Bangalore", "080-11223344", "medplus@example.com", "Mon-Sun 7am-11pm"));
        }
    }
}