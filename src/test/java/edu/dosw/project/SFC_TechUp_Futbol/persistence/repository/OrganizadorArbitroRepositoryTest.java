package edu.dosw.project.SFC_TechUp_Futbol.persistence.repository;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Usuario;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.entity.ArbitroEntity;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.entity.OrganizadorEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class OrganizadorArbitroRepositoryTest {

    @Autowired
    private OrganizadorJpaRepository organizadorJpaRepository;

    @Autowired
    private ArbitroJpaRepository arbitroJpaRepository;

    @PersistenceContext
    private EntityManager em;

    private OrganizadorEntity organizadorValido(String email) {
        OrganizadorEntity o = new OrganizadorEntity();
        o.setId(UUID.randomUUID().toString());
        o.setName("Organizador Test");
        o.setEmail(email);
        o.setPassword("password123");
        o.setUserType(Usuario.TipoUsuario.PERSONAL_ADMIN);
        return o;
    }

    private ArbitroEntity arbitroValido(String email) {
        ArbitroEntity a = new ArbitroEntity();
        a.setId(UUID.randomUUID().toString());
        a.setName("Arbitro Test");
        a.setEmail(email);
        a.setPassword("password123");
        a.setUserType(Usuario.TipoUsuario.PERSONAL_ADMIN);
        return a;
    }

    // RF_22 — Registrar organizador válido → persiste correctamente
    @Test
    void registrarOrganizador_valido_persisteCorrectamente() {
        OrganizadorEntity saved = organizadorJpaRepository.save(
                organizadorValido("organizador.rf22a@test.com"));
        em.flush();
        em.clear();

        OrganizadorEntity found = em.find(OrganizadorEntity.class, saved.getId());
        assertThat(found).isNotNull();
        assertThat(found.getEmail()).isEqualTo("organizador.rf22a@test.com");
        assertThat(found.getName()).isEqualTo("Organizador Test");
    }

    // RF_22 — Registrar árbitro válido → persiste correctamente
    @Test
    void registrarArbitro_valido_persisteCorrectamente() {
        ArbitroEntity saved = arbitroJpaRepository.save(
                arbitroValido("arbitro.rf22b@test.com"));
        em.flush();
        em.clear();

        ArbitroEntity found = em.find(ArbitroEntity.class, saved.getId());
        assertThat(found).isNotNull();
        assertThat(found.getEmail()).isEqualTo("arbitro.rf22b@test.com");
        assertThat(found.getName()).isEqualTo("Arbitro Test");
    }

    // RF_22 — Dos usuarios con el mismo email → DataIntegrityViolationException
    @Test
    void dosUsuarios_mismoEmail_lanzaDataIntegrityViolation() {
        String emailDuplicado = "duplicado.rf22c@test.com";
        organizadorJpaRepository.saveAndFlush(organizadorValido(emailDuplicado));

        assertThatThrownBy(() ->
                organizadorJpaRepository.saveAndFlush(organizadorValido(emailDuplicado))
        ).isInstanceOf(DataIntegrityViolationException.class);
    }

    // RF_22 — Buscar organizador por email existente → lo retorna
    @Test
    void buscarOrganizador_porEmailExistente_retornaResultado() {
        String email = "organizador.rf22d@test.com";
        organizadorJpaRepository.save(organizadorValido(email));
        em.flush();

        Optional<OrganizadorEntity> resultado = organizadorJpaRepository.findByEmail(email);

        assertThat(resultado).isPresent();
        assertThat(resultado.get().getEmail()).isEqualTo(email);
    }

    // RF_22 — Buscar árbitro por email inexistente → retorna Optional.empty()
    @Test
    void buscarArbitro_porEmailInexistente_retornaEmpty() {
        Optional<ArbitroEntity> resultado = arbitroJpaRepository
                .findByEmail("noexiste.rf22e@test.com");

        assertThat(resultado).isEmpty();
    }

    // RF_22 — Registrar organizador sin email → lanza ConstraintViolationException
    @Test
    void registrarOrganizador_sinEmail_lanzaConstraintViolation() {
        OrganizadorEntity sinEmail = organizadorValido(null);

        assertThatThrownBy(() -> {
            organizadorJpaRepository.save(sinEmail);
            em.flush();
        }).isInstanceOf(ConstraintViolationException.class);
    }
}
