package edu.dosw.project.SFC_TechUp_Futbol;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.dosw.project.SFC_TechUp_Futbol.controller.AdministradorController;
import edu.dosw.project.SFC_TechUp_Futbol.controller.dto.request.LoginRequest;
import edu.dosw.project.SFC_TechUp_Futbol.controller.dto.request.RegistroAdministrativoRequest;
import edu.dosw.project.SFC_TechUp_Futbol.controller.ErrorHandler;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.*;
import edu.dosw.project.SFC_TechUp_Futbol.core.repository.*;
import edu.dosw.project.SFC_TechUp_Futbol.core.service.AdministradorService;
import edu.dosw.project.SFC_TechUp_Futbol.core.service.AuditoriaService;
import edu.dosw.project.SFC_TechUp_Futbol.core.service.AutenticacionAdministradorService;
import edu.dosw.project.SFC_TechUp_Futbol.core.validator.AdministradorValidator;
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
        Map<String, Administrador> adminStore = new HashMap<>();
        AdministradorRepository administradorRepository = mock(AdministradorRepository.class);
        when(administradorRepository.save(any())).thenAnswer(inv -> {
            Administrador a = inv.getArgument(0);
            if (a.getId() == null) a.setId(UUID.randomUUID().toString());
            adminStore.put(a.getId(), a);
            return a;
        });
        when(administradorRepository.findById(anyString())).thenAnswer(inv -> Optional.ofNullable(adminStore.get(inv.<String>getArgument(0))));
        when(administradorRepository.findByEmail(anyString())).thenAnswer(inv -> {
            String email = inv.getArgument(0);
            return adminStore.values().stream().filter(a -> email.equals(a.getEmail())).findFirst();
        });
        when(administradorRepository.findAll()).thenAnswer(inv -> new ArrayList<>(adminStore.values()));

        Map<String, Organizador> orgStore = new HashMap<>();
        OrganizadorRepository organizadorRepository = mock(OrganizadorRepository.class);
        when(organizadorRepository.save(any())).thenAnswer(inv -> {
            Organizador o = inv.getArgument(0);
            if (o.getId() == null) o.setId(UUID.randomUUID().toString());
            orgStore.put(o.getId(), o);
            return o;
        });
        when(organizadorRepository.findByEmail(anyString())).thenAnswer(inv -> {
            String email = inv.getArgument(0);
            return orgStore.values().stream().filter(o -> email.equals(o.getEmail())).findFirst();
        });
        when(organizadorRepository.findAll()).thenAnswer(inv -> new ArrayList<>(orgStore.values()));

        Map<String, Arbitro> arbitroStore = new HashMap<>();
        ArbitroRepository arbitroRepository = mock(ArbitroRepository.class);
        when(arbitroRepository.save(any())).thenAnswer(inv -> {
            Arbitro a = inv.getArgument(0);
            if (a.getId() == null) a.setId(UUID.randomUUID().toString());
            arbitroStore.put(a.getId(), a);
            return a;
        });
        when(arbitroRepository.findByEmail(anyString())).thenAnswer(inv -> {
            String email = inv.getArgument(0);
            return arbitroStore.values().stream().filter(a -> email.equals(a.getEmail())).findFirst();
        });
        when(arbitroRepository.findAll()).thenAnswer(inv -> new ArrayList<>(arbitroStore.values()));

        UsuarioRegistradoRepository usuarioRegistradoRepository = mock(UsuarioRegistradoRepository.class);
        when(usuarioRegistradoRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(usuarioRegistradoRepository.findAll()).thenReturn(new ArrayList<>());

        List<RegistroAuditoria> auditoriaList1 = new ArrayList<>();
        RegistroAuditoriaRepository auditoriaRepo1 = mock(RegistroAuditoriaRepository.class);
        when(auditoriaRepo1.save(any())).thenAnswer(inv -> {
            RegistroAuditoria r = inv.getArgument(0);
            if (r.getId() == null) r.setId(UUID.randomUUID().toString());
            auditoriaList1.add(r);
            return r;
        });
        when(auditoriaRepo1.findAll()).thenAnswer(inv -> new ArrayList<>(auditoriaList1));

        List<RegistroAuditoria> auditoriaList2 = new ArrayList<>();
        RegistroAuditoriaRepository auditoriaRepo2 = mock(RegistroAuditoriaRepository.class);
        when(auditoriaRepo2.save(any())).thenAnswer(inv -> {
            RegistroAuditoria r = inv.getArgument(0);
            if (r.getId() == null) r.setId(UUID.randomUUID().toString());
            auditoriaList2.add(r);
            return r;
        });
        when(auditoriaRepo2.findAll()).thenAnswer(inv -> new ArrayList<>(auditoriaList2));

        administradorService = new AdministradorService(
                administradorRepository, organizadorRepository, arbitroRepository,
                usuarioRegistradoRepository, new AuditoriaService(auditoriaRepo1));
        autenticacionAdministradorService = new AutenticacionAdministradorService(administradorRepository);
        AdministradorValidator administradorValidator = new AdministradorValidator(
                administradorRepository, organizadorRepository, arbitroRepository, usuarioRegistradoRepository);
        mockMvc = MockMvcBuilders
                .standaloneSetup(new AdministradorController(
                        administradorService, administradorValidator,
                        autenticacionAdministradorService, new AuditoriaService(auditoriaRepo2)))
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
