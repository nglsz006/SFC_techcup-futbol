package edu.dosw.project.SFC_TechUp_Futbol.controller;

import edu.dosw.project.SFC_TechUp_Futbol.controller.AuditoriaController;
import edu.dosw.project.SFC_TechUp_Futbol.controller.dto.request.RegistroAdministrativoRequest;
import edu.dosw.project.SFC_TechUp_Futbol.controller.ErrorHandler;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.*;
import edu.dosw.project.SFC_TechUp_Futbol.core.service.AdministradorService;
import edu.dosw.project.SFC_TechUp_Futbol.core.service.AuditoriaService;
import edu.dosw.project.SFC_TechUp_Futbol.core.service.AutenticacionAdministradorService;
import edu.dosw.project.SFC_TechUp_Futbol.core.validator.AdministradorValidator;
import edu.dosw.project.SFC_TechUp_Futbol.core.validator.AuditoriaValidator;
import edu.dosw.project.SFC_TechUp_Futbol.TestMappers;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.entity.*;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.mapper.*;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.*;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AuditoriaControllerTest {

    private MockMvc mockMvc;
    private AdministradorService administradorService;
    private AutenticacionAdministradorService autenticacionAdministradorService;
    private AuditoriaService auditoriaService;

    @BeforeEach
    void setUp() {
        TorneoJpaRepository torneoRepo = mock(TorneoJpaRepository.class);
        TorneoMapper torneoMapper = TestMappers.torneoMapper();
        EquipoMapper equipoMapper = TestMappers.equipoMapper();
        JugadorMapper jugadorMapper = TestMappers.jugadorMapper();
        PartidoMapper partidoMapper = TestMappers.partidoMapper(jugadorMapper);
        AdministradorMapper adminMapper = TestMappers.administradorMapper();
        OrganizadorMapper orgMapper = TestMappers.organizadorMapper(torneoRepo, torneoMapper);
        ArbitroMapper arbitroMapper = TestMappers.arbitroMapper(partidoMapper);
        RegistroAuditoriaMapper auditoriaMapper = TestMappers.registroAuditoriaMapper();

        Map<String, AdministradorEntity> adminStore = new HashMap<>();
        AdministradorJpaRepository administradorRepository = mock(AdministradorJpaRepository.class);
        when(administradorRepository.save(any())).thenAnswer(inv -> {
            AdministradorEntity e = inv.getArgument(0);
            if (e.getId() == null) e.setId(UUID.randomUUID().toString());
            adminStore.put(e.getId(), e);
            return e;
        });
        when(administradorRepository.findById(anyString())).thenAnswer(inv -> Optional.ofNullable(adminStore.get(inv.<String>getArgument(0))));
        when(administradorRepository.findByEmail(anyString())).thenAnswer(inv -> {
            String email = inv.getArgument(0);
            return adminStore.values().stream().filter(e -> email.equals(e.getEmail())).findFirst();
        });
        when(administradorRepository.findAll()).thenAnswer(inv -> new ArrayList<>(adminStore.values()));

        Map<String, OrganizadorEntity> orgStore = new HashMap<>();
        OrganizadorJpaRepository organizadorRepository = mock(OrganizadorJpaRepository.class);
        when(organizadorRepository.save(any())).thenAnswer(inv -> {
            OrganizadorEntity e = inv.getArgument(0);
            if (e.getId() == null) e.setId(UUID.randomUUID().toString());
            orgStore.put(e.getId(), e);
            return e;
        });
        when(organizadorRepository.findByEmail(anyString())).thenAnswer(inv -> {
            String email = inv.getArgument(0);
            return orgStore.values().stream().filter(e -> email.equals(e.getEmail())).findFirst();
        });

        Map<String, ArbitroEntity> arbitroStore = new HashMap<>();
        ArbitroJpaRepository arbitroRepository = mock(ArbitroJpaRepository.class);
        when(arbitroRepository.save(any())).thenAnswer(inv -> {
            ArbitroEntity e = inv.getArgument(0);
            if (e.getId() == null) e.setId(UUID.randomUUID().toString());
            arbitroStore.put(e.getId(), e);
            return e;
        });
        when(arbitroRepository.findByEmail(anyString())).thenAnswer(inv -> {
            String email = inv.getArgument(0);
            return arbitroStore.values().stream().filter(e -> email.equals(e.getEmail())).findFirst();
        });

        UsuarioRegistradoJpaRepository usuarioRegistradoRepository = mock(UsuarioRegistradoJpaRepository.class);
        when(usuarioRegistradoRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        List<RegistroAuditoriaEntity> auditoriaList = new ArrayList<>();
        RegistroAuditoriaJpaRepository registroAuditoriaRepository = mock(RegistroAuditoriaJpaRepository.class);
        when(registroAuditoriaRepository.save(any())).thenAnswer(inv -> {
            RegistroAuditoriaEntity e = inv.getArgument(0);
            if (e.getId() == null) e.setId(UUID.randomUUID().toString());
            auditoriaList.add(e);
            return e;
        });
        when(registroAuditoriaRepository.findAll()).thenAnswer(inv -> new ArrayList<>(auditoriaList));

        auditoriaService = new AuditoriaService(registroAuditoriaRepository, auditoriaMapper);
        administradorService = new AdministradorService(
                administradorRepository, adminMapper, organizadorRepository, orgMapper,
                arbitroRepository, arbitroMapper, usuarioRegistradoRepository, auditoriaService);
        autenticacionAdministradorService = new AutenticacionAdministradorService(administradorRepository, adminMapper, registroAuditoriaRepository, auditoriaMapper);
        AdministradorValidator administradorValidator = new AdministradorValidator(
                administradorRepository, organizadorRepository, arbitroRepository, usuarioRegistradoRepository);

        mockMvc = MockMvcBuilders
                .standaloneSetup(new AuditoriaController(
                        auditoriaService, new AuditoriaValidator(),
                        administradorValidator, autenticacionAdministradorService))
                .setControllerAdvice(new ErrorHandler()).build();
    }

    @Test
    void consultarHistorial_conFiltrosValidos_retorna200() throws Exception {
        Administrador admin = administradorService.registrarAdministrador(crearAdmin("Admin", "admin@escuelaing.edu.co"));
        String token = autenticacionAdministradorService.login("admin@escuelaing.edu.co", "password123");
        auditoriaService.registrarEvento(admin.getId(), "organizador@escuelaing.edu.co", TipoAccionAuditoria.REGISTRO_ORGANIZADOR, "Registro administrativo");

        mockMvc.perform(get("/api/admin/audit")
                        .header("X-Administrador-Id", admin.getId())
                        .header("X-Administrador-Token", token)
                        .param("usuario", "organizador")
                        .param("tipoAccion", "REGISTRO_ORGANIZADOR"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mensaje").value("Consulta realizada correctamente."))
                .andExpect(jsonPath("$.registros[0].tipoAccion").value("REGISTRO_ORGANIZADOR"));
    }

    @Test
    void consultarHistorial_conRangoInconsistente_retorna400() throws Exception {
        Administrador admin = administradorService.registrarAdministrador(crearAdmin("Admin", "admin@escuelaing.edu.co"));
        String token = autenticacionAdministradorService.login("admin@escuelaing.edu.co", "password123");

        mockMvc.perform(get("/api/admin/audit")
                        .header("X-Administrador-Id", admin.getId())
                        .header("X-Administrador-Token", token)
                        .param("fechaDesde", "2026-03-25")
                        .param("fechaHasta", "2026-03-20"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detalle").value("El rango de fechas es inconsistente."));
    }

    @Test
    void consultarHistorial_sinCoincidencias_retornaMensajeInformativo() throws Exception {
        Administrador admin = administradorService.registrarAdministrador(crearAdmin("Admin", "admin@escuelaing.edu.co"));
        String token = autenticacionAdministradorService.login("admin@escuelaing.edu.co", "password123");

        mockMvc.perform(get("/api/admin/audit")
                        .header("X-Administrador-Id", admin.getId())
                        .header("X-Administrador-Token", token)
                        .param("usuario", "nadie"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mensaje").value("No se encontraron registros para los filtros indicados."))
                .andExpect(jsonPath("$.registros").isEmpty());
    }

    private RegistroAdministrativoRequest crearAdmin(String nombre, String email) {
        RegistroAdministrativoRequest request = new RegistroAdministrativoRequest();
        request.setNombre(nombre);
        request.setEmail(email);
        request.setPassword("password123");
        request.setTipoUsuario(Usuario.TipoUsuario.PERSONAL_ADMIN);
        request.setRol("ORGANIZADOR");
        return request;
    }
}
