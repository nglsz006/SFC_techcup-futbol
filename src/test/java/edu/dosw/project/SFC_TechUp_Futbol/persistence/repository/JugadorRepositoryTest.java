package edu.dosw.project.SFC_TechUp_Futbol.persistence.repository;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Jugador;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.Usuario;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.entity.JugadorEntity;
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
class JugadorRepositoryTest {

    @Autowired
    private JugadorJpaRepository jugadorJpaRepository;

    @PersistenceContext
    private EntityManager em;

    private JugadorEntity jugadorValido(String nombre, String email) {
        JugadorEntity j = new JugadorEntity();
        j.setId(UUID.randomUUID().toString());
        j.setName(nombre);
        j.setEmail(email);
        j.setPassword("password123");
        j.setUserType(Usuario.TipoUsuario.ESTUDIANTE);
        j.setPosition(Jugador.Posicion.PORTERO);
        j.setJerseyNumber(1);
        j.setAvailable(true);
        return j;
    }

    // RF_04 — Registrar jugador con datos válidos → persiste correctamente
    @Test
    void registrarJugador_valido_persisteCorrectamente() {
        JugadorEntity saved = jugadorJpaRepository.save(jugadorValido("Ana López", "ana@escuelaing.edu.co"));
        em.flush();
        em.clear();

        JugadorEntity found = em.find(JugadorEntity.class, saved.getId());
        assertThat(found).isNotNull();
        assertThat(found.getName()).isEqualTo("Ana López");
        assertThat(found.getEmail()).isEqualTo("ana@escuelaing.edu.co");
        assertThat(found.getPosition()).isEqualTo(Jugador.Posicion.PORTERO);
    }

    // RF_04 — Buscar jugador por email existente → lo retorna
    @Test
    void buscarJugadorPorEmail_existente_retornaResultado() {
        JugadorEntity j = jugadorValido("Luis Pérez", "luis@escuelaing.edu.co");
        em.persist(j);
        em.flush();

        Optional<JugadorEntity> result = jugadorJpaRepository.findByEmail("luis@escuelaing.edu.co");

        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("Luis Pérez");
    }

    // RF_04 — Dos jugadores con el mismo email → DataIntegrityViolationException (unique en usuario.email)
    //         Se usa saveAndFlush para que el proxy de Spring traduzca la excepción de Hibernate.
    @Test
    void registrarDosJugadores_mismoEmail_lanzaDataIntegrityViolation() {
        JugadorEntity j1 = jugadorValido("Jugador Uno", "dup@escuelaing.edu.co");
        jugadorJpaRepository.saveAndFlush(j1);

        JugadorEntity j2 = jugadorValido("Jugador Dos", "dup@escuelaing.edu.co");
        assertThatThrownBy(() -> jugadorJpaRepository.saveAndFlush(j2))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    // RF_04 — Registrar sin email → ConstraintViolationException (@NotNull en UsuarioEntity)
    @Test
    void registrarJugador_sinEmail_lanzaConstraintViolation() {
        JugadorEntity j = jugadorValido("Sin Email", null);

        assertThatThrownBy(() -> {
            jugadorJpaRepository.save(j);
            em.flush();
        }).isInstanceOf(ConstraintViolationException.class);
    }

    // RF_04 — Registrar sin nombre → ConstraintViolationException (@NotNull en UsuarioEntity)
    @Test
    void registrarJugador_sinNombre_lanzaConstraintViolation() {
        JugadorEntity j = jugadorValido(null, "sinombre@escuelaing.edu.co");

        assertThatThrownBy(() -> {
            jugadorJpaRepository.save(j);
            em.flush();
        }).isInstanceOf(ConstraintViolationException.class);
    }
}
