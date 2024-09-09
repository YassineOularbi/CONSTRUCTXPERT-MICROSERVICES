package com.user_service.repository;

import com.user_service.model.Supervisor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SupervisorRepository extends JpaRepository<Supervisor, Long> {
}
