package edu.dosw.project.SFC_TechUp_Futbol.persistence.repository;

import edu.dosw.project.SFC_TechUp_Futbol.persistence.entity.UsuarioRegistradoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UsuarioRegistradoJpaRepository extends JpaRepository<UsuarioRegistradoEntity, String> {
    Optional<UsuarioRegistradoEntity> findByEmail(String email);
    boolean existsByEmail(String email);

    @Query(value = "SELECT COUNT(*) > 0 FROM usuario WHERE email = :email", nativeQuery = true)
    boolean existsEmailEnTablaUsuario(@Param("email") String email);
}
