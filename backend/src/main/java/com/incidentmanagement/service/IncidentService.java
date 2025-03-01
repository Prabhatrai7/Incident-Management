package com.incidentmanagement.service;

import com.incidentmanagement.dto.IncidentDTO;
import com.incidentmanagement.model.Incident;
import com.incidentmanagement.model.User;
import com.incidentmanagement.repository.IncidentRepository;
import com.incidentmanagement.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.Year;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class IncidentService {

    private final IncidentRepository incidentRepository;
    private final UserRepository userRepository;

    public IncidentService(IncidentRepository incidentRepository, UserRepository userRepository) {
        this.incidentRepository = incidentRepository;
        this.userRepository = userRepository;
    }

    public List<IncidentDTO> getIncidentsByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return incidentRepository.findByUser(user).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public IncidentDTO getIncidentById(String incidentId, Long userId) {
        Incident incident = incidentRepository.findByIncidentId(incidentId)
                .orElseThrow(() -> new RuntimeException("Incident not found"));

        if (!incident.getUser().getId().equals(userId)) {
            throw new RuntimeException("You don't have permission to view this incident");
        }

        return convertToDTO(incident);
    }

    public IncidentDTO createIncident(IncidentDTO incidentDTO, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Incident incident = new Incident();
        incident.setIncidentId(generateUniqueIncidentId());
        incident.setReporterName(incidentDTO.getReporterName());
        incident.setDetails(incidentDTO.getDetails());
        incident.setReportedDate(LocalDateTime.now());
        incident.setPriority(incidentDTO.getPriority());
        incident.setStatus("Open");
        incident.setUserType(incidentDTO.getUserType());
        incident.setUser(user);

        Incident savedIncident = incidentRepository.save(incident);
        return convertToDTO(savedIncident);
    }

    public IncidentDTO updateIncident(String incidentId, IncidentDTO incidentDTO, Long userId) {
        Incident incident = incidentRepository.findByIncidentId(incidentId)
                .orElseThrow(() -> new RuntimeException("Incident not found"));

        if (!incident.getUser().getId().equals(userId)) {
            throw new RuntimeException("You don't have permission to update this incident");
        }

        if ("Closed".equals(incident.getStatus())) {
            throw new RuntimeException("Closed incidents cannot be edited");
        }

        incident.setDetails(incidentDTO.getDetails());
        incident.setPriority(incidentDTO.getPriority());
        incident.setStatus(incidentDTO.getStatus());

        Incident updatedIncident = incidentRepository.save(incident);
        return convertToDTO(updatedIncident);
    }

    private String generateUniqueIncidentId() {
        Random random = new Random();
        String incidentId;
        do {
            int randomNumber = 10000 + random.nextInt(90000); // 5-digit number
            int currentYear = Year.now().getValue();
            incidentId = "RMG" + randomNumber + currentYear;
        } while (incidentRepository.existsByIncidentId(incidentId));
        
        return incidentId;
    }

    private IncidentDTO convertToDTO(Incident incident) {
        IncidentDTO dto = new IncidentDTO();
        dto.setId(incident.getId());
        dto.setIncidentId(incident.getIncidentId());
        dto.setReporterName(incident.getReporterName());
        dto.setDetails(incident.getDetails());
        dto.setReportedDate(incident.getReportedDate());
        dto.setPriority(incident.getPriority());
        dto.setStatus(incident.getStatus());
        dto.setUserType(incident.getUserType());
        dto.setUserId(incident.getUser().getId());
        return dto;
    }
}