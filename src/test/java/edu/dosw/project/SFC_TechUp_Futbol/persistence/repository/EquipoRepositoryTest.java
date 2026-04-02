package edu.dosw.project.SFC_TechUp_Futbol.persistence.repository;

import edu.dosw.project.SFC_TechUp_Futbol.persistence.entity.EquipoEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class EquipoRepositoryTest {

    @Autowired
    private EquipoJpaRepository equipoJpaRepository;

    @PersistenceContext
    private EntityManager em;

    private EquipoEntity equipoValido(String nombre, String capitanId) {
        EquipoEntity e = new EquipoEntity();
        e.setId(UUID.randomUUID().toString());
        e.setNombre(nombre);
        e.setEscudo("");
        e.setColorPrincipal("azul");
        e.setColorSecundario("blanco");
        e.setCapitanId(capitanId);
        return e;
    }

    // RF_08 — Crear equipo válido → persiste correctamente
    @Test
    void crearEquipo_valido_persisteCorrectamente() {
        String capId = UUID.randomUUID().toString();
        EquipoEntity saved = equipoJpaRepository.save(equipoValido("Los Tigres", capId));
        em.flush();
        em.clear();

        EquipoEntity found = em.find(EquipoEntity.class, saved.getId());
        assertThat(found).isNotNull();
        assertThat(found.getNombre()).isEqualTo("Los Tigres");
        assertThat(found.getCapitanId()).isEqualTo(capId);
    }

    // RF_08 — Buscar equipos por capitanId → retorna lista correcta
    //         (La relación con torneo se gestiona en la capa de servicio,
    //          no existe torneoId en EquipoEntity; capitanId es el campo de referencia real.)
    @Test
    void buscarEquiposPorCapitanId_retornaResultados() {
        String capId = UUID.randomUUID().toString();
        EquipoEntity e1 = equipoValido("Los Leones", capId);
        EquipoEntity e2 = equipoValido("Los Pumas", UUID.randomUUID().toString());
        em.persist(e1);
        em.persist(e2);
        em.flush();

        List<EquipoEntity> resultado = equipoJpaRepository.findByCapitanId(capId);

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getNombre()).isEqualTo("Los Leones");
    }

    // RF_08 — Dos equipos con el mismo nombre → DataIntegrityViolationException (unique en nombre)
    //         Nota: sin torneoId en la entidad, la unicidad se aplica globalmente sobre nombre.
    @Test
    void crearDosEquipos_mismoNombre_lanzaDataIntegrityViolation() {
        equipoJpaRepository.saveAndFlush(equipoValido("Los Bravos", UUID.randomUUID().toString()));

        assertThatThrownBy(() ->
                equipoJpaRepository.saveAndFlush(equipoValido("Los Bravos", UUID.randomUUID().toString()))
        ).isInstanceOf(DataIntegrityViolationException.class);
    }

    // RF_08 — Crear equipo sin nombre → ConstraintViolationException (@NotNull en nombre)
    @Test
    void crearEquipo_sinNombre_lanzaConstraintViolation() {
        EquipoEntity e = equipoValido(null, UUID.randomUUID().toString());

        assertThatThrownBy(() -> {
            equipoJpaRepository.save(e);
            em.flush();
        }).isInstanceOf(ConstraintViolationException.class);
    }

    // RF_08 — Buscar equipo por id inexistente → Optional.empty()
    //         (La constraint "sin torneo" es de negocio en la capa de servicio;
    //          EquipoEntity no tiene torneoId — no se puede validar a nivel de repositorio.)
    @Test
    void buscarEquipoPorId_inexistente_retornaEmpty() {
        Optional<EquipoEntity> result = equipoJpaRepository.findById("id-inexistente-xyz-888");

        assertThat(result).isEmpty();
    }
}
