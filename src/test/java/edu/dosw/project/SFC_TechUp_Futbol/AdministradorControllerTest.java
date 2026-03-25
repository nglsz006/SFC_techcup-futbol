package edu.dosw.project.SFC_TechUp_Futbol;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.dosw.project.SFC_TechUp_Futbol.controller.AdministradorController;
import edu.dosw.project.SFC_TechUp_Futbol.controller.dto.request.LoginRequest;
import edu.dosw.project.SFC_TechUp_Futbol.controller.dto.request.RegistroAdministrativoRequest;
import edu.dosw.project.SFC_TechUp_Futbol.core.exception.ErrorHandler;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.Usuario;
import edu.dosw.project.SFC_TechUp_Futbol.core.repository.AdministradorRepositoryImpl;
import edu.dosw.project.SFC_TechUp_Futbol.core.repository.ArbitroRepositoryImpl;
import edu.dosw.project.SFC_TechUp_Futbol.core.repository.OrganizadorRepositoryImpl;
import edu.dosw.project.SFC_TechUp_Futbol.core.repository.RegistroAuditoriaRepositoryImpl;
import edu.dosw.project.SFC_TechUp_Futbol.core.repository.UsuarioRegistradoRepositoryImpl;
import edu.dosw.project.SFC_TechUp_Futbol.core.service.AdministradorService;
import edu.dosw.project.SFC_TechUp_Futbol.core.service.AuditoriaService;
import edu.dosw.project.SFC_TechUp_Futbol.core.service.AutenticacionAdministradorService;
import edu.dosw.project.SFC_TechUp_Futbol.core.validator.AdministradorValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Map;

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
        AdministradorRepositoryImpl administradorRepository = new AdministradorRepositoryImpl();
        OrganizadorRepositoryImpl organizadorRepository = new OrganizadorRepositoryImpl();
        ArbitroRepositoryImpl arbitroRepository = new ArbitroRepositoryImpl();
        UsuarioRegistradoRepositoryImpl usuarioRegistradoRepository = new UsuarioRegistradoRepositoryImpl();
        administradorService = new AdministradorService(
                administradorRepository,
                organizadorRepository,
                arbitroRepository,
                usuarioRegistradoRepository,
                new AuditoriaService(new RegistroAuditoriaRepositoryImpl())
        );
        autenticacionAdministradorService = new AutenticacionAdministradorService(administradorRepository);
        AdministradorValidator administradorValidator = new AdministradorValidator(
                administradorRepository,
                organizadorRepository,
                arbitroRepository,
                usuarioRegistradoRepository
        );
        mockMvc = MockMvcBuilders
                .standaloneSetup(new AdministradorController(
                        administradorService,
                        administradorValidator,
                        autenticacionAdministradorService,
                        new AuditoriaService(new RegistroAuditoriaRepositoryImpl())
                ))
                .setControllerAdvice(new ErrorHandler())
                .build();
    }

    @Test
    void registrarUsuario_conRolPermitido_retorna200() throws Exception {
        administradorService.registrarAdministrador(crearRequest("Admin", "admin@escuelaing.edu.co", "ORGANIZADOR"));
        String token = loginAdmin("admin@escuelaing.edu.co", "password123");

        mockMvc.perform(post("/api/admin/usuarios")
                        .header("X-Administrador-Id", 1)
                        .header("X-Administrador-Token", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(crearBody("Organizador", "organizador@escuelaing.edu.co", "ORGANIZADOR"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rol").value("ORGANIZADOR"))
                .andExpect(jsonPath("$.registradoPor").value(1));
    }

    @Test
    void registrarUsuario_conRolNoPermitido_retorna400() throws Exception {
        administradorService.registrarAdministrador(crearRequest("Admin", "admin@escuelaing.edu.co", "ORGANIZADOR"));
        String token = loginAdmin("admin@escuelaing.edu.co", "password123");

        mockMvc.perform(post("/api/admin/usuarios")
                        .header("X-Administrador-Id", 1)
                        .header("X-Administrador-Token", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(crearBody("Supervisor", "supervisor@escuelaing.edu.co", "SUPERVISOR"))))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.codigo").value(400));
    }

    @Test
    void registrarUsuario_sinTokenValido_retorna401() throws Exception {
        administradorService.registrarAdministrador(crearRequest("Admin", "admin@escuelaing.edu.co", "ORGANIZADOR"));

        mockMvc.perform(post("/api/admin/usuarios")
                        .header("X-Administrador-Id", 1)
                        .header("X-Administrador-Token", "token-invalido")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(crearBody("Organizador", "organizador2@escuelaing.edu.co", "ORGANIZADOR"))))
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
        return Map.of(
                "nombre", nombre,
                "email", email,
                "password", "password123",
                "tipoUsuario", "ESTUDIANTE",
                "rol", rol
        );
    }

    private String loginAdmin(String email, String password) {
        LoginRequest request = new LoginRequest();
        request.setEmail(email);
        request.setPassword(password);
        return autenticacionAdministradorService.login(request.getEmail(), request.getPassword());
    }
}
