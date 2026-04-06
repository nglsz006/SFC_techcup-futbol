package edu.dosw.project.SFC_TechUp_Futbol.controller;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Pago;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.Partido;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.entity.*;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.mapper.*;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.repository.*;

import java.util.*;
import java.util.stream.Collectors;

import static org.mockito.Mockito.*;

class MockRepoHelper {

    static AdministradorJpaRepository adminRepo(Map<String, AdministradorEntity> store) {
        AdministradorJpaRepository repo = mock(AdministradorJpaRepository.class);
        when(repo.save(any())).thenAnswer(inv -> { AdministradorEntity e = inv.getArgument(0); if (e.getId() == null) e.setId(UUID.randomUUID().toString()); store.put(e.getId(), e); return e; });
        when(repo.findById(anyString())).thenAnswer(inv -> Optional.ofNullable(store.get(inv.<String>getArgument(0))));
        when(repo.findByEmail(anyString())).thenAnswer(inv -> store.values().stream().filter(e -> inv.<String>getArgument(0).equals(e.getEmail())).findFirst());
        when(repo.findAll()).thenAnswer(inv -> new ArrayList<>(store.values()));
        return repo;
    }

    static OrganizadorJpaRepository orgRepo(Map<String, OrganizadorEntity> store) {
        OrganizadorJpaRepository repo = mock(OrganizadorJpaRepository.class);
        when(repo.save(any())).thenAnswer(inv -> { OrganizadorEntity e = inv.getArgument(0); if (e.getId() == null) e.setId(UUID.randomUUID().toString()); store.put(e.getId(), e); return e; });
        when(repo.findById(anyString())).thenAnswer(inv -> Optional.ofNullable(store.get(inv.<String>getArgument(0))));
        when(repo.findByEmail(anyString())).thenAnswer(inv -> store.values().stream().filter(e -> inv.<String>getArgument(0).equals(e.getEmail())).findFirst());
        when(repo.findAll()).thenAnswer(inv -> new ArrayList<>(store.values()));
        return repo;
    }

    static ArbitroJpaRepository arbitroRepo(Map<String, ArbitroEntity> store) {
        ArbitroJpaRepository repo = mock(ArbitroJpaRepository.class);
        when(repo.save(any())).thenAnswer(inv -> { ArbitroEntity e = inv.getArgument(0); if (e.getId() == null) e.setId(UUID.randomUUID().toString()); store.put(e.getId(), e); return e; });
        when(repo.findById(anyString())).thenAnswer(inv -> Optional.ofNullable(store.get(inv.<String>getArgument(0))));
        when(repo.findByEmail(anyString())).thenAnswer(inv -> store.values().stream().filter(e -> inv.<String>getArgument(0).equals(e.getEmail())).findFirst());
        when(repo.findAll()).thenAnswer(inv -> new ArrayList<>(store.values()));
        return repo;
    }

    static CapitanJpaRepository capitanRepo(Map<String, CapitanEntity> store) {
        CapitanJpaRepository repo = mock(CapitanJpaRepository.class);
        when(repo.save(any())).thenAnswer(inv -> { CapitanEntity e = inv.getArgument(0); if (e.getId() == null) e.setId(UUID.randomUUID().toString()); store.put(e.getId(), e); return e; });
        when(repo.findById(anyString())).thenAnswer(inv -> Optional.ofNullable(store.get(inv.<String>getArgument(0))));
        when(repo.findByEmail(anyString())).thenAnswer(inv -> store.values().stream().filter(e -> inv.<String>getArgument(0).equals(e.getEmail())).findFirst());
        when(repo.findAll()).thenAnswer(inv -> new ArrayList<>(store.values()));
        return repo;
    }

    static JugadorJpaRepository jugadorRepo(Map<String, JugadorEntity> store) {
        JugadorJpaRepository repo = mock(JugadorJpaRepository.class);
        when(repo.save(any())).thenAnswer(inv -> { JugadorEntity e = inv.getArgument(0); if (e.getId() == null) e.setId(UUID.randomUUID().toString()); store.put(e.getId(), e); return e; });
        when(repo.findById(anyString())).thenAnswer(inv -> Optional.ofNullable(store.get(inv.<String>getArgument(0))));
        when(repo.findAll()).thenAnswer(inv -> new ArrayList<>(store.values()));
        return repo;
    }

    static EquipoJpaRepository equipoRepo(Map<String, EquipoEntity> store) {
        EquipoJpaRepository repo = mock(EquipoJpaRepository.class);
        when(repo.save(any())).thenAnswer(inv -> { EquipoEntity e = inv.getArgument(0); if (e.getId() == null) e.setId(UUID.randomUUID().toString()); store.put(e.getId(), e); return e; });
        when(repo.findById(anyString())).thenAnswer(inv -> Optional.ofNullable(store.get(inv.<String>getArgument(0))));
        when(repo.findAll()).thenAnswer(inv -> new ArrayList<>(store.values()));
        return repo;
    }

    static TorneoJpaRepository torneoRepo(Map<String, TorneoEntity> store) {
        TorneoJpaRepository repo = mock(TorneoJpaRepository.class);
        when(repo.save(any())).thenAnswer(inv -> { TorneoEntity e = inv.getArgument(0); if (e.getId() == null) e.setId(UUID.randomUUID().toString()); store.put(e.getId(), e); return e; });
        when(repo.findById(anyString())).thenAnswer(inv -> Optional.ofNullable(store.get(inv.<String>getArgument(0))));
        when(repo.findAll()).thenAnswer(inv -> new ArrayList<>(store.values()));
        return repo;
    }

    static PartidoJpaRepository partidoRepo(Map<String, PartidoEntity> store) {
        PartidoJpaRepository repo = mock(PartidoJpaRepository.class);
        when(repo.save(any())).thenAnswer(inv -> { PartidoEntity e = inv.getArgument(0); if (e.getId() == null) e.setId(UUID.randomUUID().toString()); store.put(e.getId(), e); return e; });
        when(repo.findById(anyString())).thenAnswer(inv -> Optional.ofNullable(store.get(inv.<String>getArgument(0))));
        when(repo.findByTorneoId(anyString())).thenAnswer(inv -> store.values().stream().filter(e -> e.getTorneo() != null && inv.<String>getArgument(0).equals(e.getTorneo().getId())).collect(Collectors.toList()));
        when(repo.findByEstado(any())).thenAnswer(inv -> store.values().stream().filter(e -> e.getEstado() == inv.<Partido.PartidoEstado>getArgument(0)).collect(Collectors.toList()));
        when(repo.findByEquipoLocalIdOrEquipoVisitanteId(anyString(), anyString())).thenAnswer(inv -> {
            String lid = inv.getArgument(0); String vid = inv.getArgument(1);
            return store.values().stream().filter(e -> (e.getEquipoLocal() != null && lid.equals(e.getEquipoLocal().getId())) || (e.getEquipoVisitante() != null && vid.equals(e.getEquipoVisitante().getId()))).collect(Collectors.toList());
        });
        return repo;
    }

    static PagoJpaRepository pagoRepo(Map<String, PagoEntity> store) {
        PagoJpaRepository repo = mock(PagoJpaRepository.class);
        when(repo.save(any())).thenAnswer(inv -> { PagoEntity e = inv.getArgument(0); if (e.getId() == null) e.setId(UUID.randomUUID().toString()); store.put(e.getId(), e); return e; });
        when(repo.findById(anyString())).thenAnswer(inv -> Optional.ofNullable(store.get(inv.<String>getArgument(0))));
        when(repo.findByEquipoId(anyString())).thenAnswer(inv -> store.values().stream().filter(e -> e.getEquipo() != null && inv.<String>getArgument(0).equals(e.getEquipo().getId())).collect(Collectors.toList()));
        when(repo.findByEstado(any())).thenAnswer(inv -> store.values().stream().filter(e -> e.getEstado() == inv.<Pago.PagoEstado>getArgument(0)).collect(Collectors.toList()));
        when(repo.findByEquipoIdAndEstado(anyString(), any())).thenAnswer(inv -> store.values().stream().filter(e -> e.getEquipo() != null && inv.<String>getArgument(0).equals(e.getEquipo().getId()) && e.getEstado() == inv.<Pago.PagoEstado>getArgument(1)).findFirst());
        when(repo.existsByEquipoIdAndEstado(anyString(), any())).thenAnswer(inv -> store.values().stream().anyMatch(e -> e.getEquipo() != null && inv.<String>getArgument(0).equals(e.getEquipo().getId()) && e.getEstado() == inv.<Pago.PagoEstado>getArgument(1)));
        return repo;
    }

    static UsuarioRegistradoJpaRepository usuarioRepo(Map<String, UsuarioRegistradoEntity> store) {
        UsuarioRegistradoJpaRepository repo = mock(UsuarioRegistradoJpaRepository.class);
        when(repo.save(any())).thenAnswer(inv -> { UsuarioRegistradoEntity e = inv.getArgument(0); if (e.getId() == null) e.setId(UUID.randomUUID().toString()); store.put(e.getId(), e); return e; });
        when(repo.findByEmail(anyString())).thenAnswer(inv -> store.values().stream().filter(e -> inv.<String>getArgument(0).equals(e.getEmail())).findFirst());
        when(repo.findAll()).thenAnswer(inv -> new ArrayList<>(store.values()));
        return repo;
    }

    static RegistroAuditoriaJpaRepository auditoriaRepo(List<RegistroAuditoriaEntity> store) {
        RegistroAuditoriaJpaRepository repo = mock(RegistroAuditoriaJpaRepository.class);
        when(repo.save(any())).thenAnswer(inv -> { RegistroAuditoriaEntity e = inv.getArgument(0); if (e.getId() == null) e.setId(UUID.randomUUID().toString()); store.add(e); return e; });
        when(repo.findAll()).thenAnswer(inv -> new ArrayList<>(store));
        return repo;
    }

    static PerfilDeportivoJpaRepository perfilRepo(Map<String, PerfilDeportivoEntity> store) {
        PerfilDeportivoJpaRepository repo = mock(PerfilDeportivoJpaRepository.class);
        when(repo.save(any())).thenAnswer(inv -> { PerfilDeportivoEntity e = inv.getArgument(0); if (e.getId() == null) e.setId(UUID.randomUUID().toString()); store.put(e.getId(), e); return e; });
        when(repo.findById(anyString())).thenAnswer(inv -> Optional.ofNullable(store.get(inv.<String>getArgument(0))));
        when(repo.findByJugadorId(anyString())).thenAnswer(inv -> store.values().stream().filter(e -> inv.<String>getArgument(0).equals(e.getJugadorId())).findFirst());
        return repo;
    }

    static AlineacionJpaRepository alineacionRepo(Map<String, AlineacionEntity> store) {
        AlineacionJpaRepository repo = mock(AlineacionJpaRepository.class);
        when(repo.save(any())).thenAnswer(inv -> { AlineacionEntity e = inv.getArgument(0); if (e.getId() == null) e.setId(UUID.randomUUID().toString()); store.put(e.getId(), e); return e; });
        when(repo.findById(anyString())).thenAnswer(inv -> Optional.ofNullable(store.get(inv.<String>getArgument(0))));
        when(repo.findAll()).thenAnswer(inv -> new ArrayList<>(store.values()));
        when(repo.findByPartidoIdAndEquipoId(anyString(), anyString())).thenAnswer(inv -> {
            String pid = inv.getArgument(0); String eid = inv.getArgument(1);
            return store.values().stream().filter(e -> pid.equals(e.getPartidoId()) && eid.equals(e.getEquipoId())).findFirst();
        });
        when(repo.findByPartidoId(anyString())).thenAnswer(inv -> {
            String pid = inv.getArgument(0);
            return store.values().stream().filter(e -> pid.equals(e.getPartidoId())).collect(java.util.stream.Collectors.toList());
        });
        return repo;
    }
}
