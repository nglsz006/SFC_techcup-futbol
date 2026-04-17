package edu.dosw.project.SFC_TechUp_Futbol.persistence.repository;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Torneo;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.entity.TorneoEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class TorneoRepositoryTest {

    @Autowired
    private TorneoJpaRepository torneoJpaRepository;

    @PersistenceContext
    private EntityManager em;

    private TorneoEntity torneoValido(String nombre) {
        TorneoEntity t = new TorneoEntity();
        t.setId(UUID.randomUUID().toString());
        t.setNombre(nombre);
        t.setFechaInicio(LocalDateTime.of(2025, 9, 1, 10, 0));
        t.setFechaFin(LocalDateTime.of(2025, 9, 30, 18, 0));
        t.setCantidadEquipos(8);
        t.setCosto(50.0);
        t.setEstado(Torneo.EstadoTorneo.CREADO);
        return t;
    }

    // RF_01 — Torneo creado persiste con estado CREADO (equivalente a BORRADOR en el dominio)
    @Test
    void guardarTorneo_valido_persisteConEstadoCREADO() {
        TorneoEntity saved = torneoJpaRepository.save(torneoValido("Copa Test"));
        em.flush();
        em.clear();

        TorneoEntity found = em.find(TorneoEntity.class, saved.getId());
        assertThat(found).isNotNull();
        assertThat(found.getEstado()).isEqualTo(Torneo.EstadoTorneo.CREADO);
        assertThat(found.getNombre()).isEqualTo("Copa Test");
    }

    // RF_01 — Buscar por id existente retorna el torneo
    @Test
    void buscarTorneoPorId_existente_retornaResultado() {
        TorneoEntity t = torneoValido("Copa Busqueda");
        em.persist(t);
        em.flush();

        Optional<TorneoEntity> result = torneoJpaRepository.findById(t.getId());

        assertThat(result).isPresent();
        assertThat(result.get().getNombre()).isEqualTo("Copa Busqueda");
    }

    // RF_01 — Buscar por id inexistente retorna Optional.empty()
    @Test
    void buscarTorneoPorId_inexistente_retornaEmpty() {
        Optional<TorneoEntity> result = torneoJpaRepository.findById("id-inexistente-xyz-999");

        assertThat(result).isEmpty();
    }

    // RF_01 — Guardar sin fechaInicio lanza ConstraintViolationException (@NotNull)
    @Test
    void guardarTorneo_sinFechaInicio_lanzaConstraintViolation() {
        TorneoEntity t = torneoValido("Copa SinFecha");
        t.setFechaInicio(null);

        assertThatThrownBy(() -> {
            torneoJpaRepository.save(t);
            em.flush();
        }).isInstanceOf(ConstraintViolationException.class);
    }

    // RF_01 — fechaFin anterior a fechaInicio: la validación está en la capa de servicio.
    //         El repositorio acepta fechas invertidas sin excepción.
    @Test
    void guardarTorneo_conFechaFinAnterior_laConstraintEstaEnServicio() {
        TorneoEntity t = torneoValido("Copa FechaInvertida");
        t.setFechaFin(LocalDateTime.of(2024, 1, 1, 0, 0));

        TorneoEntity saved = torneoJpaRepository.save(t);
        em.flush();

        assertThat(saved.getId()).isNotNull();
    }

    // RF_02 — Cambiar estado de CREADO a EN_CURSO persiste correctamente
    @Test
    void cambiarEstado_deCREADO_aEN_CURSO_persiste() {
        TorneoEntity t = torneoValido("Copa Cambio");
        em.persist(t);
        em.flush();

        t.setEstado(Torneo.EstadoTorneo.EN_CURSO);
        torneoJpaRepository.save(t);
        em.flush();
        em.clear();

        TorneoEntity updated = em.find(TorneoEntity.class, t.getId());
        assertThat(updated.getEstado()).isEqualTo(Torneo.EstadoTorneo.EN_CURSO);
    }

    // RF_02 — Guardar con estado nulo lanza excepción (@NotNull en entity)
    @Test
    void guardarTorneo_conEstadoNulo_lanzaExcepcion() {
        TorneoEntity t = torneoValido("Copa EstadoNulo");
        t.setEstado(null);

        assertThatThrownBy(() -> {
            torneoJpaRepository.save(t);
            em.flush();
        }).isInstanceOfAny(
                ConstraintViolationException.class,
                org.springframework.dao.DataIntegrityViolationException.class
        );
    }

    // RF_02 — findByEstado filtra torneos por estado correctamente
    @Test
    void buscarTorneosPorEstado_retornaListaFiltrada() {
        TorneoEntity t1 = torneoValido("Copa A");
        em.persist(t1);
        TorneoEntity t2 = torneoValido("Copa B");
        t2.setEstado(Torneo.EstadoTorneo.EN_CURSO);
        em.persist(t2);
        em.flush();

        List<TorneoEntity> creados = torneoJpaRepository.findByEstado(Torneo.EstadoTorneo.CREADO);
        List<TorneoEntity> enCurso  = torneoJpaRepository.findByEstado(Torneo.EstadoTorneo.EN_CURSO);

        assertThat(creados).anyMatch(e -> e.getNombre().equals("Copa A"));
        assertThat(enCurso).anyMatch(e -> e.getNombre().equals("Copa B"));
    }
}
