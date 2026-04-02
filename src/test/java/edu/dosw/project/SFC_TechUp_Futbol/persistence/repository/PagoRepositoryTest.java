package edu.dosw.project.SFC_TechUp_Futbol.persistence.repository;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Pago;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.entity.EquipoEntity;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.entity.PagoEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class PagoRepositoryTest {

    @Autowired
    private PagoJpaRepository pagoJpaRepository;

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

    private PagoEntity pagoValido(EquipoEntity equipo, String comprobante) {
        PagoEntity p = new PagoEntity();
        p.setId(UUID.randomUUID().toString());
        p.setComprobante(comprobante);
        p.setFechaSubida(LocalDate.now());
        p.setEquipo(equipo);
        return p;
    }

    // RF_12 — Registrar pago válido → persiste con estado por defecto PENDIENTE
    @Test
    void registrarPago_valido_persisteConEstadoPendiente() {
        EquipoEntity equipo = equipoValido("Los Tigres RF12");
        em.persist(equipo);
        em.flush();

        PagoEntity saved = pagoJpaRepository.save(pagoValido(equipo, "comprobante_url.pdf"));
        em.flush();
        em.clear();

        PagoEntity found = em.find(PagoEntity.class, saved.getId());
        assertThat(found).isNotNull();
        assertThat(found.getEstado()).isEqualTo(Pago.PagoEstado.PENDIENTE);
        assertThat(found.getComprobante()).isEqualTo("comprobante_url.pdf");
    }

    // RF_12 — Buscar pagos por equipo → retorna lista correcta
    @Test
    void buscarPagosPorEquipo_retornaListaCorrecta() {
        EquipoEntity equipo1 = equipoValido("Equipo Alpha RF12");
        EquipoEntity equipo2 = equipoValido("Equipo Beta RF12");
        em.persist(equipo1);
        em.persist(equipo2);
        em.flush();

        pagoJpaRepository.save(pagoValido(equipo1, "comp1.pdf"));
        pagoJpaRepository.save(pagoValido(equipo1, "comp2.pdf"));
        pagoJpaRepository.save(pagoValido(equipo2, "comp3.pdf"));
        em.flush();

        List<PagoEntity> resultado = pagoJpaRepository.findByEquipoId(equipo1.getId());

        assertThat(resultado).hasSize(2);
        assertThat(resultado).allMatch(p -> p.getEquipo().getId().equals(equipo1.getId()));
    }

    // RF_12 — Buscar pagos por estado → retorna solo los del estado solicitado
    @Test
    void buscarPagosPorEstado_retornaSoloLosDelEstadoSolicitado() {
        EquipoEntity equipo = equipoValido("Equipo Gamma RF12");
        em.persist(equipo);
        em.flush();

        PagoEntity pendiente = pagoValido(equipo, "comp_pendiente.pdf");
        PagoEntity aprobado = pagoValido(equipo, "comp_aprobado.pdf");
        aprobado.setEstado(Pago.PagoEstado.APROBADO);

        pagoJpaRepository.save(pendiente);
        pagoJpaRepository.save(aprobado);
        em.flush();

        List<PagoEntity> resultado = pagoJpaRepository.findByEstado(Pago.PagoEstado.PENDIENTE);

        assertThat(resultado).isNotEmpty();
        assertThat(resultado).allMatch(p -> p.getEstado() == Pago.PagoEstado.PENDIENTE);
        assertThat(resultado).noneMatch(p -> p.getEstado() == Pago.PagoEstado.APROBADO);
    }

    // RF_12 — Registrar pago sin comprobante → lanza ConstraintViolationException
    @Test
    void registrarPago_sinComprobante_lanzaConstraintViolation() {
        EquipoEntity equipo = equipoValido("Equipo Delta RF12");
        em.persist(equipo);
        em.flush();

        PagoEntity pago = pagoValido(equipo, null);

        assertThatThrownBy(() -> {
            pagoJpaRepository.save(pago);
            em.flush();
        }).isInstanceOf(ConstraintViolationException.class);
    }

    // RF_12 — Registrar pago sin equipo asociado → lanza ConstraintViolationException
    @Test
    void registrarPago_sinEquipo_lanzaConstraintViolation() {
        PagoEntity pago = new PagoEntity();
        pago.setId(UUID.randomUUID().toString());
        pago.setComprobante("comp_sin_equipo.pdf");
        pago.setFechaSubida(LocalDate.now());
        // equipo intencionalmente nulo

        assertThatThrownBy(() -> {
            pagoJpaRepository.save(pago);
            em.flush();
        }).isInstanceOf(ConstraintViolationException.class);
    }
}
