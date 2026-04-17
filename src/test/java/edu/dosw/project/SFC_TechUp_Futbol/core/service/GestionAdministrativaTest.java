package edu.dosw.project.SFC_TechUp_Futbol.core.service;

import edu.dosw.project.SFC_TechUp_Futbol.controller.dto.request.ConsultaAuditoriaRequest;
import edu.dosw.project.SFC_TechUp_Futbol.controller.dto.request.RegistroAdministrativoRequest;
import edu.dosw.project.SFC_TechUp_Futbol.core.exception.AccesoDenegadoException;
import edu.dosw.project.SFC_TechUp_Futbol.core.exception.AutenticacionAdminException;
import edu.dosw.project.SFC_TechUp_Futbol.core.exception.CorreoYaRegistradoException;
import edu.dosw.project.SFC_TechUp_Futbol.core.exception.FiltroAuditoriaInvalidoException;
import edu.dosw.project.SFC_TechUp_Futbol.core.exception.RolNoPermitidoException;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.*;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.entity.*;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.mapper.*;
import edu.dosw.project.SFC_TechUp_Futbol.TestMappers;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GestionAdministrativaTest {

    private AdministradorService administradorService;
    private AutenticacionAdministradorService autenticacionService;
    private AuditoriaService auditoriaService;
    private RegistroAuditoriaMapper auditoriaMapper;
    private RegistroAuditoriaJpaRepository auditoriaRepo;

    @BeforeEach
    void setUp() {
        TorneoMapper torneoMapper = TestMappers.torneoMapper();
        EquipoMapper equipoMapper = TestMappers.equipoMapper();
        JugadorMapper jugadorMapper = TestMappers.jugadorMapper();
        PartidoMapper partidoMapper = TestMappers.partidoMapper(jugadorMapper);
        AdministradorMapper adminMapper = TestMappers.administradorMapper();
        OrganizadorMapper orgMapper = TestMappers.organizadorMapper(mock(TorneoJpaRepository.class), torneoMapper);
        ArbitroMapper arbitroMapper = TestMappers.arbitroMapper(partidoMapper);
        auditoriaMapper = TestMappers.registroAuditoriaMapper();

        Map<String, AdministradorEntity> adminStore = new HashMap<>();
        AdministradorJpaRepository adminRepo = mock(AdministradorJpaRepository.class);
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

        Map<String, OrganizadorEntity> orgStore = new HashMap<>();
        OrganizadorJpaRepository orgRepo = mock(OrganizadorJpaRepository.class);
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

        Map<String, ArbitroEntity> arbitroStore = new HashMap<>();
        ArbitroJpaRepository arbitroRepo = mock(ArbitroJpaRepository.class);
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

        UsuarioRegistradoJpaRepository usuarioRepo = mock(UsuarioRegistradoJpaRepository.class);
        when(usuarioRepo.findByEmail(anyString())).thenReturn(Optional.empty());

        List<RegistroAuditoriaEntity> auditoriaList = new ArrayList<>();
        auditoriaRepo = mock(RegistroAuditoriaJpaRepository.class);
        when(auditoriaRepo.save(any())).thenAnswer(inv -> {
            RegistroAuditoriaEntity e = inv.getArgument(0);
            if (e.getId() == null) e.setId(UUID.randomUUID().toString());
            auditoriaList.add(e);
            return e;
        });
        when(auditoriaRepo.findAll()).thenAnswer(inv -> new ArrayList<>(auditoriaList));

        auditoriaService = new AuditoriaService(auditoriaRepo, auditoriaMapper);
        administradorService = new AdministradorService(adminRepo, adminMapper, orgRepo, orgMapper,
                arbitroRepo, arbitroMapper, usuarioRepo, auditoriaService);
        autenticacionService = new AutenticacionAdministradorService(adminRepo, adminMapper, auditoriaRepo, auditoriaMapper);
    }

    private Administrador crearAdmin() {
        return administradorService.registrarAdministrador(request("Admin", "admin@escuelaing.edu.co", "ORGANIZADOR"));
    }

    private RegistroAdministrativoRequest request(String nombre, String email, String rol) {
        RegistroAdministrativoRequest r = new RegistroAdministrativoRequest();
        r.setNombre(nombre);
        r.setEmail(email);
        r.setPassword("password123");
        r.setTipoUsuario(Usuario.TipoUsuario.ESTUDIANTE);
        r.setRol(rol);
        return r;
    }

    // --- Creacion y gestion de actores administrativos ---

    @Test
    void registrarOrganizador_datosValidos_retornaOrganizador() {
        Administrador admin = crearAdmin();
        Usuario u = administradorService.registrarUsuarioAdministrativo(admin.getId(), request("Org", "org@escuelaing.edu.co", "ORGANIZADOR"));
        assertInstanceOf(Organizador.class, u);
        assertEquals("org@escuelaing.edu.co", u.getEmail());
    }

    @Test
    void registrarArbitro_datosValidos_retornaArbitro() {
        Administrador admin = crearAdmin();
        Usuario u = administradorService.registrarUsuarioAdministrativo(admin.getId(), request("Arb", "arb@escuelaing.edu.co", "ARBITRO"));
        assertInstanceOf(Arbitro.class, u);
        assertEquals("arb@escuelaing.edu.co", u.getEmail());
    }

    @Test
    void registrarActor_administradorInexistente_lanzaExcepcion() {
        assertThrows(AccesoDenegadoException.class, () ->
                administradorService.registrarUsuarioAdministrativo("id-inexistente", request("Org", "org@escuelaing.edu.co", "ORGANIZADOR")));
    }

    @Test
    void registrarActor_correoYaRegistrado_lanzaExcepcion() {
        Administrador admin = crearAdmin();
        administradorService.registrarUsuarioAdministrativo(admin.getId(), request("Org", "duplicado@escuelaing.edu.co", "ORGANIZADOR"));
        assertThrows(CorreoYaRegistradoException.class, () ->
                administradorService.registrarUsuarioAdministrativo(admin.getId(), request("Arb", "duplicado@escuelaing.edu.co", "ARBITRO")));
    }

    @Test
    void registrarActor_rolNoPermitido_lanzaExcepcion() {
        Administrador admin = crearAdmin();
        assertThrows(RolNoPermitidoException.class, () ->
                administradorService.registrarUsuarioAdministrativo(admin.getId(), request("Sup", "sup@escuelaing.edu.co", "SUPERVISOR")));
    }

    @Test
    void registrarActor_informacionIncompleta_nombreVacio_lanzaExcepcion() {
        Administrador admin = crearAdmin();
        assertThrows(IllegalArgumentException.class, () ->
                administradorService.registrarUsuarioAdministrativo(admin.getId(), request(" ", "org@escuelaing.edu.co", "ORGANIZADOR")));
    }

    @Test
    void registrarActor_rolVacio_lanzaExcepcion() {
        Administrador admin = crearAdmin();
        assertThrows(IllegalArgumentException.class, () ->
                administradorService.registrarUsuarioAdministrativo(admin.getId(), request("Org", "org@escuelaing.edu.co", " ")));
    }

    // --- Autenticacion administrativa ---

    @Test
    void login_credencialesValidas_retornaToken() {
        crearAdmin();
        String token = autenticacionService.login("admin@escuelaing.edu.co", "password123");
        assertNotNull(token);
        assertFalse(token.isBlank());
    }

    @Test
    void login_passwordIncorrecta_lanzaExcepcion() {
        crearAdmin();
        assertThrows(AutenticacionAdminException.class, () ->
                autenticacionService.login("admin@escuelaing.edu.co", "wrongpass"));
    }

    @Test
    void login_correoInexistente_lanzaExcepcion() {
        assertThrows(AutenticacionAdminException.class, () ->
                autenticacionService.login("noexiste@escuelaing.edu.co", "password123"));
    }

    @Test
    void validarSesion_tokenValido_noLanzaExcepcion() {
        Administrador admin = crearAdmin();
        String token = autenticacionService.login("admin@escuelaing.edu.co", "password123");
        assertDoesNotThrow(() -> autenticacionService.validarSesion(admin.getId(), token));
    }

    @Test
    void validarSesion_tokenInvalido_lanzaExcepcion() {
        Administrador admin = crearAdmin();
        assertThrows(AutenticacionAdminException.class, () ->
                autenticacionService.validarSesion(admin.getId(), "token-invalido"));
    }

    @Test
    void validarSesion_tokenNulo_lanzaExcepcion() {
        Administrador admin = crearAdmin();
        assertThrows(AutenticacionAdminException.class, () ->
                autenticacionService.validarSesion(admin.getId(), null));
    }

    // --- Registro de auditoria ---

    @Test
    void login_exitoso_registraAuditoriaLoginAdmin() {
        crearAdmin();
        autenticacionService.login("admin@escuelaing.edu.co", "password123");
        List<RegistroAuditoriaEntity> registros = auditoriaRepo.findAll();
        assertTrue(registros.stream().anyMatch(r -> TipoAccionAuditoria.LOGIN_ADMIN.name().equals(r.getTipoAccion().name())));
    }

    @Test
    void registrarOrganizador_registraAuditoriaCorrecta() {
        Administrador admin = crearAdmin();
        administradorService.registrarUsuarioAdministrativo(admin.getId(), request("Org", "org@escuelaing.edu.co", "ORGANIZADOR"));
        List<RegistroAuditoriaEntity> registros = auditoriaRepo.findAll();
        assertTrue(registros.stream().anyMatch(r -> TipoAccionAuditoria.REGISTRO_ORGANIZADOR.name().equals(r.getTipoAccion().name())));
    }

    @Test
    void registrarArbitro_registraAuditoriaCorrecta() {
        Administrador admin = crearAdmin();
        administradorService.registrarUsuarioAdministrativo(admin.getId(), request("Arb", "arb@escuelaing.edu.co", "ARBITRO"));
        List<RegistroAuditoriaEntity> registros = auditoriaRepo.findAll();
        assertTrue(registros.stream().anyMatch(r -> TipoAccionAuditoria.REGISTRO_ARBITRO.name().equals(r.getTipoAccion().name())));
    }

    @Test
    void registrarEvento_informacionMinima_persisteCorrectamente() {
        RegistroAuditoria r = auditoriaService.registrarEvento("admin-1", "org@escuelaing.edu.co", TipoAccionAuditoria.REGISTRO_ORGANIZADOR, "Registro de prueba");
        assertNotNull(r.getId());
        assertEquals(TipoAccionAuditoria.REGISTRO_ORGANIZADOR, r.getTipoAccion());
        assertNotNull(r.getFecha());
    }

    // --- Consulta de auditoria ---

    @Test
    void consultarHistorial_sinFiltros_retornaRegistros() {
        Administrador admin = crearAdmin();
        administradorService.registrarUsuarioAdministrativo(admin.getId(), request("Org", "org@escuelaing.edu.co", "ORGANIZADOR"));
        List<RegistroAuditoria> registros = auditoriaService.consultarHistorial(new ConsultaAuditoriaRequest());
        assertFalse(registros.isEmpty());
    }

    @Test
    void consultarHistorial_sinRegistros_retornaListaVacia() {
        ConsultaAuditoriaRequest req = new ConsultaAuditoriaRequest();
        req.setUsuario("nadie");
        assertTrue(auditoriaService.consultarHistorial(req).isEmpty());
    }

    @Test
    void consultarHistorial_filtroTipoAccion_retornaFiltrado() {
        Administrador admin = crearAdmin();
        administradorService.registrarUsuarioAdministrativo(admin.getId(), request("Org", "org@escuelaing.edu.co", "ORGANIZADOR"));
        administradorService.registrarUsuarioAdministrativo(admin.getId(), request("Arb", "arb@escuelaing.edu.co", "ARBITRO"));
        ConsultaAuditoriaRequest req = new ConsultaAuditoriaRequest();
        req.setTipoAccion("REGISTRO_ORGANIZADOR");
        List<RegistroAuditoria> registros = auditoriaService.consultarHistorial(req);
        assertTrue(registros.stream().allMatch(r -> r.getTipoAccion() == TipoAccionAuditoria.REGISTRO_ORGANIZADOR));
    }

    @Test
    void consultarHistorial_filtroFechas_retornaOrdenadoMasReciente() throws InterruptedException {
        Administrador admin = crearAdmin();
        administradorService.registrarUsuarioAdministrativo(admin.getId(), request("Org", "org@escuelaing.edu.co", "ORGANIZADOR"));
        Thread.sleep(5);
        administradorService.registrarUsuarioAdministrativo(admin.getId(), request("Arb", "arb@escuelaing.edu.co", "ARBITRO"));
        ConsultaAuditoriaRequest req = new ConsultaAuditoriaRequest();
        req.setFechaDesde(LocalDate.now().minusDays(1));
        req.setFechaHasta(LocalDate.now().plusDays(1));
        List<RegistroAuditoria> registros = auditoriaService.consultarHistorial(req);
        assertTrue(registros.size() >= 2);
        assertTrue(registros.get(0).getFecha().isAfter(registros.get(1).getFecha()) ||
                registros.get(0).getFecha().isEqual(registros.get(1).getFecha()));
    }

    @Test
    void consultarHistorial_tipoAccionInvalido_lanzaExcepcion() {
        ConsultaAuditoriaRequest req = new ConsultaAuditoriaRequest();
        req.setTipoAccion("ACCION_INEXISTENTE");
        assertThrows(FiltroAuditoriaInvalidoException.class, () -> auditoriaService.consultarHistorial(req));
    }

    @Test
    void consultarHistorial_rangoFechasInconsistente_lanzaExcepcion() {
        ConsultaAuditoriaRequest req = new ConsultaAuditoriaRequest();
        req.setFechaDesde(LocalDate.now());
        req.setFechaHasta(LocalDate.now().minusDays(1));
        assertThrows(FiltroAuditoriaInvalidoException.class, () -> auditoriaService.consultarHistorial(req));
    }

    @Test
    void consultarHistorial_requestNulo_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> auditoriaService.consultarHistorial(null));
    }

    // --- Integridad funcional ---

    @Test
    void flujoCompleto_login_registroActores_auditoriaCoherente() {
        Administrador admin = crearAdmin();
        String token = autenticacionService.login("admin@escuelaing.edu.co", "password123");
        assertNotNull(token);

        administradorService.registrarUsuarioAdministrativo(admin.getId(), request("Org", "org@escuelaing.edu.co", "ORGANIZADOR"));
        administradorService.registrarUsuarioAdministrativo(admin.getId(), request("Arb", "arb@escuelaing.edu.co", "ARBITRO"));

        ConsultaAuditoriaRequest req = new ConsultaAuditoriaRequest();
        List<RegistroAuditoria> registros = auditoriaService.consultarHistorial(req);

        assertTrue(registros.stream().anyMatch(r -> r.getTipoAccion() == TipoAccionAuditoria.LOGIN_ADMIN));
        assertTrue(registros.stream().anyMatch(r -> r.getTipoAccion() == TipoAccionAuditoria.REGISTRO_ORGANIZADOR));
        assertTrue(registros.stream().anyMatch(r -> r.getTipoAccion() == TipoAccionAuditoria.REGISTRO_ARBITRO));
    }
}
