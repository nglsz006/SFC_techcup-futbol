package edu.dosw.project.SFC_TechUp_Futbol.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.dosw.project.SFC_TechUp_Futbol.controller.AdministradorController;
import edu.dosw.project.SFC_TechUp_Futbol.controller.dto.request.LoginRequest;
import edu.dosw.project.SFC_TechUp_Futbol.controller.dto.request.RegistroAdministrativoRequest;
import edu.dosw.project.SFC_TechUp_Futbol.controller.ErrorHandler;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.*;
import edu.dosw.project.SFC_TechUp_Futbol.core.service.AdministradorService;
import edu.dosw.project.SFC_TechUp_Futbol.core.service.AuditoriaService;
import edu.dosw.project.SFC_TechUp_Futbol.core.service.AutenticacionAdministradorService;
import edu.dosw.project.SFC_TechUp_Futbol.core.util.Base64Util;
import edu.dosw.project.SFC_TechUp_Futbol.core.validator.AdministradorValidator;
import edu.dosw.project.SFC_TechUp_Futbol.TestMappers;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.entity.*;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.mapper.*;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.*;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AdministradorControllerTest {

    private MockMvc mockMvc;
    private AdministradorService administradorService;
    private AutenticacionAdministradorService autenticacionAdministradorService;
    private final ObjectMapper mapper = new ObjectMapper();

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
        UsuarioRegistradoMapper usuarioMapper = TestMappers.usuarioRegistradoMapper();
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
        when(organizadorRepository.findAll()).thenAnswer(inv -> new ArrayList<>(orgStore.values()));

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
        when(arbitroRepository.findAll()).thenAnswer(inv -> new ArrayList<>(arbitroStore.values()));

        UsuarioRegistradoJpaRepository usuarioRegistradoRepository = mock(UsuarioRegistradoJpaRepository.class);
        when(usuarioRegistradoRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(usuarioRegistradoRepository.findAll()).thenReturn(new ArrayList<>());

        List<RegistroAuditoriaEntity> auditoriaList1 = new ArrayList<>();
        RegistroAuditoriaJpaRepository auditoriaRepo1 = mock(RegistroAuditoriaJpaRepository.class);
        when(auditoriaRepo1.save(any())).thenAnswer(inv -> {
            RegistroAuditoriaEntity e = inv.getArgument(0);
            if (e.getId() == null) e.setId(UUID.randomUUID().toString());
            auditoriaList1.add(e);
            return e;
        });
        when(auditoriaRepo1.findAll()).thenAnswer(inv -> new ArrayList<>(auditoriaList1));

        List<RegistroAuditoriaEntity> auditoriaList2 = new ArrayList<>();
        RegistroAuditoriaJpaRepository auditoriaRepo2 = mock(RegistroAuditoriaJpaRepository.class);
        when(auditoriaRepo2.save(any())).thenAnswer(inv -> {
            RegistroAuditoriaEntity e = inv.getArgument(0);
            if (e.getId() == null) e.setId(UUID.randomUUID().toString());
            auditoriaList2.add(e);
            return e;
        });
        when(auditoriaRepo2.findAll()).thenAnswer(inv -> new ArrayList<>(auditoriaList2));

        administradorService = new AdministradorService(
                administradorRepository, adminMapper, organizadorRepository, orgMapper,
                arbitroRepository, arbitroMapper, usuarioRegistradoRepository,
                new AuditoriaService(auditoriaRepo1, auditoriaMapper));
        autenticacionAdministradorService = new AutenticacionAdministradorService(administradorRepository, adminMapper, auditoriaRepo2, auditoriaMapper);
        AdministradorValidator administradorValidator = new AdministradorValidator(
                administradorRepository, organizadorRepository, arbitroRepository, usuarioRegistradoRepository);
        mockMvc = MockMvcBuilders
                .standaloneSetup(new AdministradorController(
                        administradorService, administradorValidator,
                        autenticacionAdministradorService, new AuditoriaService(auditoriaRepo2, auditoriaMapper)))
                .setControllerAdvice(new ErrorHandler()).build();
    }

    @Test
    void registrarUsuario_conRolPermitido_retorna200() throws Exception {
        Administrador admin = administradorService.registrarAdministrador(crearRequest("Admin", "admin@escuelaing.edu.co", "ORGANIZADOR"));
        String token = loginAdmin("admin@escuelaing.edu.co", "password123");
        mockMvc.perform(post("/api/admin/users")
                        .header("X-Administrador-Id", admin.getId())
                        .header("X-Administrador-Token", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(crearBody("Organizador", "organizador@escuelaing.edu.co", "ORGANIZADOR"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rol").value("ORGANIZADOR"))
                .andExpect(jsonPath("$.registradoPor").value(admin.getId()));
    }

    @Test
    void registrarUsuario_conRolNoPermitido_retorna400() throws Exception {
        Administrador admin = administradorService.registrarAdministrador(crearRequest("Admin", "admin@escuelaing.edu.co", "ORGANIZADOR"));
        String token = loginAdmin("admin@escuelaing.edu.co", "password123");
        mockMvc.perform(post("/api/admin/users")
                        .header("X-Administrador-Id", admin.getId())
                        .header("X-Administrador-Token", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(crearBody("Supervisor", "supervisor@escuelaing.edu.co", "SUPERVISOR"))))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.codigo").value(400));
    }

    @Test
    void registrarUsuario_sinTokenValido_retorna401() throws Exception {
        Administrador admin = administradorService.registrarAdministrador(crearRequest("Admin", "admin@escuelaing.edu.co", "ORGANIZADOR"));
        mockMvc.perform(post("/api/admin/users")
                        .header("X-Administrador-Id", admin.getId())
                        .header("X-Administrador-Token", "token-invalido")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(crearBody("Org", "org2@escuelaing.edu.co", "ORGANIZADOR"))))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.codigo").value(401));
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

    private Map<String, Object> crearBody(String nombre, String email, String rol) {
        return Map.of("nombre", nombre, "email", email, "password", "password123", "tipoUsuario", "ESTUDIANTE", "rol", rol);
    }

    private String loginAdmin(String email, String password) {
        LoginRequest request = new LoginRequest();
        request.setEmail(email);
        request.setPassword(password);
        return autenticacionAdministradorService.login(request.getEmail(), request.getPassword());
    }
}
