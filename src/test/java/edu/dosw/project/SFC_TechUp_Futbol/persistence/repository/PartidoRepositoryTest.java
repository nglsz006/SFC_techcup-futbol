package edu.dosw.project.SFC_TechUp_Futbol.persistence.repository;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Partido;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.Torneo;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.entity.EquipoEntity;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.entity.GolEntity;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.entity.PartidoEntity;
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
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class PartidoRepositoryTest {

    @Autowired
    private PartidoJpaRepository partidoJpaRepository;

    @PersistenceContext
    private EntityManager em;

    private EquipoEntity equipoValido(String nombre) {
        EquipoEntity e = new EquipoEntity();
        e.setId(UUID.randomUUID().toString());
        e.setNombre(nombre);
        e.setEscudo("");
        e.setColorPrincipal("rojo");
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

    private PartidoEntity partidoValido(TorneoEntity torneo, EquipoEntity local, EquipoEntity visitante) {
        PartidoEntity p = new PartidoEntity();
        p.setId(UUID.randomUUID().toString());
        p.setFecha(LocalDateTime.now().plusDays(1));
        p.setCancha("Estadio Central");
        p.setTorneo(torneo);
        p.setEquipoLocal(local);
        p.setEquipoVisitante(visitante);
        return p;
    }

    // RF_17 — Registrar partido válido con dos equipos → persiste con estado PROGRAMADO
    @Test
    void registrarPartido_valido_persisteConEstadoProgramado() {
        TorneoEntity torneo = torneoValido("Copa RF17-A");
        EquipoEntity local = equipoValido("Local RF17-A");
        EquipoEntity visitante = equipoValido("Visitante RF17-A");
        em.persist(torneo);
        em.persist(local);
        em.persist(visitante);
        em.flush();

        PartidoEntity saved = partidoJpaRepository.save(partidoValido(torneo, local, visitante));
        em.flush();
        em.clear();

        PartidoEntity found = em.find(PartidoEntity.class, saved.getId());
        assertThat(found).isNotNull();
        assertThat(found.getEstado()).isEqualTo(Partido.PartidoEstado.PROGRAMADO);
        assertThat(found.getMarcadorLocal()).isEqualTo(0);
        assertThat(found.getMarcadorVisitante()).isEqualTo(0);
        assertThat(found.getEquipoLocal().getId()).isEqualTo(local.getId());
        assertThat(found.getEquipoVisitante().getId()).isEqualTo(visitante.getId());
    }

    // RF_17 — Buscar partidos por torneo → retorna lista correcta
    @Test
    void buscarPartidosPorTorneo_retornaListaCorrecta() {
        TorneoEntity torneo1 = torneoValido("Copa RF17-B1");
        TorneoEntity torneo2 = torneoValido("Copa RF17-B2");
        EquipoEntity eq1 = equipoValido("Equipo RF17-B1");
        EquipoEntity eq2 = equipoValido("Equipo RF17-B2");
        EquipoEntity eq3 = equipoValido("Equipo RF17-B3");
        em.persist(torneo1);
        em.persist(torneo2);
        em.persist(eq1);
        em.persist(eq2);
        em.persist(eq3);
        em.flush();

        partidoJpaRepository.save(partidoValido(torneo1, eq1, eq2));
        partidoJpaRepository.save(partidoValido(torneo1, eq2, eq3));
        partidoJpaRepository.save(partidoValido(torneo2, eq1, eq3));
        em.flush();

        List<PartidoEntity> resultado = partidoJpaRepository.findByTorneoId(torneo1.getId());

        assertThat(resultado).hasSize(2);
        assertThat(resultado).allMatch(p -> p.getTorneo().getId().equals(torneo1.getId()));
    }

    // RF_17 — Buscar partidos por equipo (local o visitante) → retorna en ambos casos
    @Test
    void buscarPartidosPorEquipo_retornaComoLocalYComoVisitante() {
        TorneoEntity torneo = torneoValido("Copa RF17-C");
        EquipoEntity eq1 = equipoValido("Equipo RF17-C1");
        EquipoEntity eq2 = equipoValido("Equipo RF17-C2");
        EquipoEntity eq3 = equipoValido("Equipo RF17-C3");
        em.persist(torneo);
        em.persist(eq1);
        em.persist(eq2);
        em.persist(eq3);
        em.flush();

        // eq2 como local en partido1, eq2 como visitante en partido2
        partidoJpaRepository.save(partidoValido(torneo, eq2, eq3));
        partidoJpaRepository.save(partidoValido(torneo, eq1, eq2));
        partidoJpaRepository.save(partidoValido(torneo, eq1, eq3)); // no incluye eq2
        em.flush();

        List<PartidoEntity> resultado = partidoJpaRepository
                .findByEquipoLocalIdOrEquipoVisitanteId(eq2.getId(), eq2.getId());

        assertThat(resultado).hasSize(2);
    }

    // RF_17 — Registrar partido sin equipoLocal → lanza ConstraintViolationException
    // (PartidoEntity no tiene campo 'arbitro'; se valida la constraint de equipoLocal como relación obligatoria)
    @Test
    void registrarPartido_sinEquipoLocal_lanzaConstraintViolation() {
        TorneoEntity torneo = torneoValido("Copa RF17-D");
        EquipoEntity visitante = equipoValido("Visitante RF17-D");
        em.persist(torneo);
        em.persist(visitante);
        em.flush();

        PartidoEntity partido = new PartidoEntity();
        partido.setId(UUID.randomUUID().toString());
        partido.setFecha(LocalDateTime.now().plusDays(1));
        partido.setCancha("Estadio");
        partido.setTorneo(torneo);
        partido.setEquipoLocal(null); // equipoLocal nulo → constraint violation
        partido.setEquipoVisitante(visitante);

        assertThatThrownBy(() -> {
            partidoJpaRepository.save(partido);
            em.flush();
        }).isInstanceOf(ConstraintViolationException.class);
    }

    // RF_17 — Agregar goles al partido → persisten en cascada
    @Test
    void agregarGolesAlPartido_persistenEnCascada() {
        TorneoEntity torneo = torneoValido("Copa RF17-E");
        EquipoEntity local = equipoValido("Local RF17-E");
        EquipoEntity visitante = equipoValido("Visitante RF17-E");
        em.persist(torneo);
        em.persist(local);
        em.persist(visitante);
        em.flush();

        PartidoEntity partido = partidoValido(torneo, local, visitante);

        GolEntity gol1 = new GolEntity();
        gol1.setId(UUID.randomUUID().toString());
        gol1.setMinuto(15);
        gol1.setPartido(partido);

        GolEntity gol2 = new GolEntity();
        gol2.setId(UUID.randomUUID().toString());
        gol2.setMinuto(72);
        gol2.setPartido(partido);

        partido.getGoles().add(gol1);
        partido.getGoles().add(gol2);

        partidoJpaRepository.save(partido);
        em.flush();
        em.clear();

        PartidoEntity found = em.find(PartidoEntity.class, partido.getId());
        assertThat(found.getGoles()).hasSize(2);
        assertThat(found.getGoles()).extracting(GolEntity::getMinuto)
                .containsExactlyInAnyOrder(15, 72);
    }
}
