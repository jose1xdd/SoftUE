package com.backend.softue.repositories;

import com.backend.softue.models.DocumentoPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentoPlanRepository extends JpaRepository<DocumentoPlan, Integer> {

}
