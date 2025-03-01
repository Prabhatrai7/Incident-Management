package com.incidentmanagement.repository;

import com.incidentmanagement.model.Incident;
import com.incidentmanagement.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IncidentRepository extends JpaRepository<Incident, Long> {
    List<Incident> findByUser(User user);
    Optional<Incident> findByIncidentId(String incidentId);
    boolean existsByIncidentId(String incidentId);
}