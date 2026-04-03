package edu.dosw.project.SFC_TechUp_Futbol.core.service;

import edu.dosw.project.SFC_TechUp_Futbol.controller.dto.request.RegistroAdministrativoRequest;
import edu.dosw.project.SFC_TechUp_Futbol.core.exception.AccesoDenegadoException;
import edu.dosw.project.SFC_TechUp_Futbol.core.exception.CorreoYaRegistradoException;
import edu.dosw.project.SFC_TechUp_Futbol.core.exception.RolNoPermitidoException;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.*;
import edu.dosw.project.SFC_TechUp_Futbol.core.service.AdministradorService;
import edu.dosw.project.SFC_TechUp_Futbol.core.service.AuditoriaService;
import edu.dosw.project.SFC_TechUp_Futbol.core.util.PasswordUtil;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.entity.*;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.mapper.*;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AdministradorServiceTest {

    private AdministradorService administradorService;
    private RegistroAuditoriaJpaRepository auditoriaRepository;
    private RegistroAuditoriaMapper auditoriaMapper;

    @BeforeEach
    void setUp() {
        Map<String, AdministradorEntity> adminStore = new HashMap<>();
        AdministradorJpaRepository adminRepo = mock(AdministradorJpaRepository.class);
        AdministradorMapper adminMapper = new AdministradorMapper();
        when(adminRepo.save(any())).thenAnswer(inv -> {
            AdministradorEntity e = inv.getArgument(0);
            if (e.getId() == null) e.setId(UUID.randomUUID().toString());
            adminStore.put(e.getId(), e);
            return e;
        });
        when(adminRepo.findById(anyString())).thenAnswer(inv -> Optional.ofNullable(adminStore.get(inv.<String>getArgument(0))));
        when(adminRepo.findByEmail(anyString())).thenAnswer(inv -> {
            String email = inv.getArgument(0);
            return adminStore.values().stream().filter(e -> email.equals(e.getEmail())).findFirst();
        });
        when(adminRepo.findAll()).thenAnswer(inv -> new ArrayList<>(adminStore.values()));

        Map<String, OrganizadorEntity> orgStore = new HashMap<>();
        OrganizadorJpaRepository orgRepo = mock(OrganizadorJpaRepository.class);
        TorneoJpaRepository torneoRepo = mock(TorneoJpaRepository.class);
        TorneoMapper torneoMapper = new TorneoMapper();
        OrganizadorMapper orgMapper = new OrganizadorMapper(torneoRepo, torneoMapper);
        when(orgRepo.save(any())).thenAnswer(inv -> {
            OrganizadorEntity e = inv.getArgument(0);
            if (e.getId() == null) e.setId(UUID.randomUUID().toString());
            orgStore.put(e.getId(), e);
            return e;
        });
        when(orgRepo.findByEmail(anyString())).thenAnswer(inv -> {
            String email = inv.getArgument(0);
            return orgStore.values().stream().filter(e -> email.equals(e.getEmail())).findFirst();
        });
        when(orgRepo.findAll()).thenAnswer(inv -> new ArrayList<>(orgStore.values()));

        Map<String, ArbitroEntity> arbitroStore = new HashMap<>();
        ArbitroJpaRepository arbitroRepo = mock(ArbitroJpaRepository.class);
        EquipoMapper equipoMapper = new EquipoMapper();
        JugadorMapper jugadorMapper = new JugadorMapper();
        TorneoMapper torneoMapper2 = new TorneoMapper();
        PartidoMapper partidoMapper = new PartidoMapper(torneoMapper2, equipoMapper, jugadorMapper);
        ArbitroMapper arbitroMapper = new ArbitroMapper(partidoMapper);
        when(arbitroRepo.save(any())).thenAnswer(inv -> {
            ArbitroEntity e = inv.getArgument(0);
            if (e.getId() == null) e.setId(UUID.randomUUID().toString());
            arbitroStore.put(e.getId(), e);
            return e;
        });
        when(arbitroRepo.findByEmail(anyString())).thenAnswer(inv -> {
            String email = inv.getArgument(0);
            return arbitroStore.values().stream().filter(e -> email.equals(e.getEmail())).findFirst();
        });
        when(arbitroRepo.findAll()).thenAnswer(inv -> new ArrayList<>(arbitroStore.values()));

        UsuarioRegistradoJpaRepository usuarioRepo = mock(UsuarioRegistradoJpaRepository.class);
        UsuarioRegistradoMapper usuarioMapper = new UsuarioRegistradoMapper();
        when(usuarioRepo.findByEmail(anyString())).thenReturn(Optional.empty());

        List<RegistroAuditoriaEntity> auditoriaList = new ArrayList<>();
        auditoriaRepository = mock(RegistroAuditoriaJpaRepository.class);
        auditoriaMapper = new RegistroAuditoriaMapper();
        when(auditoriaRepository.save(any())).thenAnswer(inv -> {
            RegistroAuditoriaEntity e = inv.getArgument(0);
            if (e.getId() == null) e.setId(UUID.randomUUID().toString());
            auditoriaList.add(e);
            return e;
        });
        when(auditoriaRepository.findAll()).thenAnswer(inv -> new ArrayList<>(auditoriaList));

        administradorService = new AdministradorService(
                adminRepo, adminMapper, orgRepo, orgMapper, arbitroRepo, arbitroMapper,
                usuarioRepo, new AuditoriaService(auditoriaRepository, auditoriaMapper));
    }

    @Test
    void registrarUsuarioAdministrativo_conRolOrganizador_registraOrganizador() {
        Administrador administrador = administradorService.registrarAdministrador(crearRequest("Admin", "admin@escuelaing.edu.co", "ORGANIZADOR"));
        Usuario usuario = administradorService.registrarUsuarioAdministrativo(administrador.getId(), crearRequest("Org", "org@escuelaing.edu.co", "ORGANIZADOR"));
        assertInstanceOf(Organizador.class, usuario);
        assertEquals("org@escuelaing.edu.co", usuario.getEmail());
        assertTrue(PasswordUtil.verificar("password123", usuario.getPassword()));
        assertEquals(TipoAccionAuditoria.REGISTRO_ORGANIZADOR, auditoriaMapper.toDomain(auditoriaRepository.findAll().getFirst()).getTipoAccion());
    }

    @Test
    void registrarUsuarioAdministrativo_conRolArbitro_registraArbitro() {
        Administrador administrador = administradorService.registrarAdministrador(crearRequest("Admin", "admin@escuelaing.edu.co", "ORGANIZADOR"));
        Usuario usuario = administradorService.registrarUsuarioAdministrativo(administrador.getId(), crearRequest("Arbitro", "arbitro@escuelaing.edu.co", "ARBITRO"));
        assertInstanceOf(Arbitro.class, usuario);
        assertEquals("arbitro@escuelaing.edu.co", usuario.getEmail());
        assertEquals(TipoAccionAuditoria.REGISTRO_ARBITRO, auditoriaMapper.toDomain(auditoriaRepository.findAll().getFirst()).getTipoAccion());
    }

    @Test
    void registrarUsuarioAdministrativo_sinAdministradorAutorizado_lanzaExcepcion() {
        assertThrows(AccesoDenegadoException.class,
                () -> administradorService.registrarUsuarioAdministrativo("id-inexistente", crearRequest("Org", "org@escuelaing.edu.co", "ORGANIZADOR")));
    }

    @Test
    void registrarUsuarioAdministrativo_conCorreoDuplicado_lanzaExcepcion() {
        Administrador administrador = administradorService.registrarAdministrador(crearRequest("Admin", "admin@escuelaing.edu.co", "ORGANIZADOR"));
        administradorService.registrarUsuarioAdministrativo(administrador.getId(), crearRequest("Org", "duplicado@escuelaing.edu.co", "ORGANIZADOR"));
        assertThrows(CorreoYaRegistradoException.class,
                () -> administradorService.registrarUsuarioAdministrativo(administrador.getId(), crearRequest("Arb", "duplicado@escuelaing.edu.co", "ARBITRO")));
    }

    @Test
    void registrarUsuarioAdministrativo_conRolNoPermitido_lanzaExcepcion() {
        Administrador administrador = administradorService.registrarAdministrador(crearRequest("Admin", "admin@escuelaing.edu.co", "ORGANIZADOR"));
        assertThrows(RolNoPermitidoException.class,
                () -> administradorService.registrarUsuarioAdministrativo(administrador.getId(), crearRequest("Supervisor", "sup@escuelaing.edu.co", "SUPERVISOR")));
    }

    private RegistroAdministrativoRequest crearRequest(String nombre, String email, String rol) {
        RegistroAdministrativoRequest request = new RegistroAdministrativoRequest();
        request.setNombre(nombre);
        request.setEmail(email);
        request.setPassword("password123");
        request.setTipoUsuario(Usuario.TipoUsuario.ESTUDIANTE);
        request.setRol(rol);
        return request;
    }
}
