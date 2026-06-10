package com.symptomchecker.service;

import com.symptomchecker.entity.User;
import com.symptomchecker.entity.Appointment;
import com.symptomchecker.entity.Pharmacy;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

@Service
public class ToolService {

    private final AppointmentService appointmentService;
    private final UserService userService;
    private final PharmacyService pharmacyService;

    public ToolService(AppointmentService appointmentService, UserService userService, PharmacyService pharmacyService) {
        this.appointmentService = appointmentService;
        this.userService = userService;
        this.pharmacyService = pharmacyService;
    }

    public Map<String, Object> bookAppointment(String patientUsername, String urgency) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            User patient = userService.findByUsername(patientUsername);
            List<User> doctors = userService.getDoctors();
            
            if (doctors.isEmpty()) {
                result.put("success", false);
                result.put("message", "No doctors available");
                return result;
            }
            
            User doctor = doctors.get(0); // Simple selection
            LocalDateTime appointmentTime = getNextAvailableSlot(urgency);
            
            Appointment appointment = appointmentService.bookAppointment(patient, doctor, appointmentTime);
            
            result.put("success", true);
            result.put("message", "Appointment booked successfully");
            result.put("appointmentId", appointment.getId());
            result.put("doctorName", doctor.getFullName());
            result.put("appointmentTime", appointmentTime.toString());
            
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "Failed to book appointment: " + e.getMessage());
        }
        
        return result;
    }

    public Map<String, Object> checkEmergency(String symptoms) {
        Map<String, Object> result = new HashMap<>();
        
        String[] emergencyKeywords = {"chest pain", "difficulty breathing", "severe pain", 
                                    "unconscious", "bleeding", "stroke", "heart attack"};
        
        boolean isEmergency = false;
        for (String keyword : emergencyKeywords) {
            if (symptoms.toLowerCase().contains(keyword)) {
                isEmergency = true;
                break;
            }
        }
        
        result.put("isEmergency", isEmergency);
        result.put("recommendation", isEmergency ? 
            "URGENT: Seek immediate medical attention or call emergency services" :
            "Monitor symptoms and consider scheduling an appointment");
            
        return result;
    }

    public Map<String, Object> findNearbyPharmacy(String location) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<Pharmacy> pharmacies = pharmacyService.getPharmaciesByLocation(location);
            List<Map<String, String>> pharmacyList = pharmacies.stream().map(p -> {
                Map<String, String> m = new HashMap<>();
                m.put("name", p.getName());
                m.put("address", p.getAddress());
                m.put("phone", p.getPhone() != null ? p.getPhone() : "");
                m.put("operatingHours", p.getOperatingHours() != null ? p.getOperatingHours() : "");
                return m;
            }).collect(Collectors.toList());
            result.put("success", true);
            result.put("pharmacies", pharmacyList);
            result.put("location", location);
        } catch (Exception e) {
            result.put("success", false);
            result.put("error", e.getMessage());
        }
        return result;
    }

    private LocalDateTime getNextAvailableSlot(String urgency) {
        LocalDateTime now = LocalDateTime.now();
        
        if ("high".equalsIgnoreCase(urgency)) {
            return now.plusHours(2); // Same day for urgent
        } else {
            return now.plusDays(1).withHour(10).withMinute(0); // Next day
        }
    }
}