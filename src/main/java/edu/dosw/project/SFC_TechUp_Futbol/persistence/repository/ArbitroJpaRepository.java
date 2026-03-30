package edu.dosw.project.SFC_TechUp_Futbol.persistence.repository;

import edu.dosw.project.SFC_TechUp_Futbol.persistence.entity.ArbitroEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ArbitroJpaRepository extends JpaRepository<ArbitroEntity, String> {
    Optional<ArbitroEntity> findByEmail(String email);
}
