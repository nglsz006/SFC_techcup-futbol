package edu.dosw.project.SFC_TechUp_Futbol.persistence.repository;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Partido;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.Torneo;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.entity.EquipoEntity;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.entity.PartidoEntity;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.entity.TorneoEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

/**
 * RF_19 — Tabla de posiciones.
 *
 * No existe una entidad propia de tabla de posiciones: el cálculo se realiza
 * en TorneoController.tablaPosiciones() a partir de los partidos FINALIZADOS
 * de un torneo obtenidos via PartidoJpaRepository.findByTorneoId().
 * Estos tests validan la query de persistencia que sustenta ese cálculo.
 */
@SpringBootTest
@Transactional
class TablaPosicionesRepositoryTest {

    @Autowired
    private PartidoJpaRepository partidoJpaRepository;

    @PersistenceContext
    private EntityManager em;

    private EquipoEntity equipoValido(String nombre) {
        EquipoEntity e = new EquipoEntity();
        e.setId(UUID.randomUUID().toString());
        e.setNombre(nombre);
        e.setEscudo("");
        e.setColorPrincipal("azul");
        e.setColorSecundario("blanco");
        e.setCapitanId(UUID.randomUUID().toString());
        return e;
    }

    private TorneoEntity torneoValido(String nombre) {
        TorneoEntity t = new TorneoEntity();
        t.setId(UUID.randomUUID().toString());
        t.setNombre(nombre);
        t.setFechaInicio(LocalDateTime.now());
        t.setFechaFin(LocalDateTime.now().plusDays(30));
        t.setEstado(Torneo.EstadoTorneo.CREADO);
        return t;
    }

    private PartidoEntity partidoFinalizado(TorneoEntity torneo,
                                             EquipoEntity local,
                                             EquipoEntity visitante,
                                             int marcadorLocal,
                                             int marcadorVisitante) {
        PartidoEntity p = new PartidoEntity();
        p.setId(UUID.randomUUID().toString());
        p.setFecha(LocalDateTime.now().minusDays(1));
        p.setCancha("Estadio Central");
        p.setTorneo(torneo);
        p.setEquipoLocal(local);
        p.setEquipoVisitante(visitante);
        p.setMarcadorLocal(marcadorLocal);
        p.setMarcadorVisitante(marcadorVisitante);
        p.setEstado(Partido.PartidoEstado.FINALIZADO);
        return p;
    }

    // RF_19 — Tres partidos finalizados de un torneo → findByTorneoId los retorna todos
    @Test
    void tresPartidosFinalizados_findByTorneoId_retornaTodos() {
        TorneoEntity torneo = torneoValido("Copa RF19-A");
        EquipoEntity eq1 = equipoValido("Equipo RF19-A1");
        EquipoEntity eq2 = equipoValido("Equipo RF19-A2");
        EquipoEntity eq3 = equipoValido("Equipo RF19-A3");
        em.persist(torneo);
        em.persist(eq1);
        em.persist(eq2);
        em.persist(eq3);
        em.flush();

        // victoria local, empate, victoria visitante
        em.persist(partidoFinalizado(torneo, eq1, eq2, 3, 1));
        em.persist(partidoFinalizado(torneo, eq1, eq3, 2, 2));
        em.persist(partidoFinalizado(torneo, eq2, eq3, 0, 2));
        em.flush();

        List<PartidoEntity> resultado = partidoJpaRepository.findByTorneoId(torneo.getId());

        assertThat(resultado).hasSize(3);
        assertThat(resultado).allMatch(p -> p.getEstado() == Partido.PartidoEstado.FINALIZADO);
    }

    // RF_19 — Partidos de otro torneo no aparecen en la consulta del primero
    @Test
    void partidosDeOtroTorneo_noAparecenEnConsulta() {
        TorneoEntity torneo1 = torneoValido("Copa RF19-B1");
        TorneoEntity torneo2 = torneoValido("Copa RF19-B2");
        EquipoEntity eq1 = equipoValido("Equipo RF19-B1");
        EquipoEntity eq2 = equipoValido("Equipo RF19-B2");
        em.persist(torneo1);
        em.persist(torneo2);
        em.persist(eq1);
        em.persist(eq2);
        em.flush();

        em.persist(partidoFinalizado(torneo1, eq1, eq2, 1, 0));
        em.persist(partidoFinalizado(torneo2, eq1, eq2, 3, 3));
        em.persist(partidoFinalizado(torneo2, eq2, eq1, 2, 1));
        em.flush();

        List<PartidoEntity> resultado = partidoJpaRepository.findByTorneoId(torneo1.getId());

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getTorneo().getId()).isEqualTo(torneo1.getId());
    }

    // RF_19 — Los campos de marcador y estado persisten correctamente para el cálculo
    @Test
    void camposDeMarcadorYEstado_persistenCorrectamente() {
        TorneoEntity torneo = torneoValido("Copa RF19-C");
        EquipoEntity local = equipoValido("Local RF19-C");
        EquipoEntity visitante = equipoValido("Visitante RF19-C");
        em.persist(torneo);
        em.persist(local);
        em.persist(visitante);
        em.flush();

        em.persist(partidoFinalizado(torneo, local, visitante, 4, 2));
        em.flush();
        em.clear();

        List<PartidoEntity> resultado = partidoJpaRepository.findByTorneoId(torneo.getId());

        assertThat(resultado).hasSize(1);
        PartidoEntity p = resultado.get(0);
        assertThat(p.getMarcadorLocal()).isEqualTo(4);
        assertThat(p.getMarcadorVisitante()).isEqualTo(2);
        assertThat(p.getEstado()).isEqualTo(Partido.PartidoEstado.FINALIZADO);
        assertThat(p.getEquipoLocal()).isNotNull();
        assertThat(p.getEquipoVisitante()).isNotNull();
    }
}
