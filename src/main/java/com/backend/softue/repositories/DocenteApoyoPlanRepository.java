package com.backend.softue.repositories;

import com.backend.softue.models.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocenteApoyoPlanRepository extends JpaRepository<DocenteApoyoPlan, DocentePlanKey> {
}
