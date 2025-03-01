package com.incidentmanagement.controller;

import com.incidentmanagement.dto.IncidentDTO;
import com.incidentmanagement.security.JwtTokenProvider;
import com.incidentmanagement.service.IncidentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/incidents")
public class IncidentController {

    private final IncidentService incidentService;
    private final JwtTokenProvider jwtTokenProvider;

    public IncidentController(IncidentService incidentService, JwtTokenProvider jwtTokenProvider) {
        this.incidentService = incidentService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @GetMapping
    public ResponseEntity<List<IncidentDTO>> getAllIncidents(HttpServletRequest request) {
        String token = jwtTokenProvider.resolveToken(request);
        Long userId = jwtTokenProvider.getUserId(token);
        List<IncidentDTO> incidents = incidentService.getIncidentsByUserId(userId);
        return ResponseEntity.ok(incidents);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getIncidentById(@PathVariable("id") String incidentId, HttpServletRequest request) {
        try {
            String token = jwtTokenProvider.resolveToken(request);
            Long userId = jwtTokenProvider.getUserId(token);
            IncidentDTO incident = incidentService.getIncidentById(incidentId, userId);
            return ResponseEntity.ok(incident);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchIncidentById(@RequestParam("incidentId") String incidentId, HttpServletRequest request) {
        try {
            String token = jwtTokenProvider.resolveToken(request);
            Long userId = jwtTokenProvider.getUserId(token);
            IncidentDTO incident = incidentService.getIncidentById(incidentId, userId);
            return ResponseEntity.ok(incident);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<?> createIncident(@RequestBody IncidentDTO incidentDTO, HttpServletRequest request) {
        try {
            String token = jwtTokenProvider.resolveToken(request);
            Long userId = jwtTokenProvider.getUserId(token);
            IncidentDTO createdIncident = incidentService.createIncident(incidentDTO, userId);
            return ResponseEntity.ok(createdIncident);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateIncident(@PathVariable("id") String incidentId, 
                                           @RequestBody IncidentDTO incidentDTO, 
                                           HttpServletRequest request) {
        try {
            String token = jwtTokenProvider.resolveToken(request);
            Long userId = jwtTokenProvider.getUserId(token);
            IncidentDTO updatedIncident = incidentService.updateIncident(incidentId, incidentDTO, userId);
            return ResponseEntity.ok(updatedIncident);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}