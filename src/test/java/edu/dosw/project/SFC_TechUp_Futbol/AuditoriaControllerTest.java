package edu.dosw.project.SFC_TechUp_Futbol;

import edu.dosw.project.SFC_TechUp_Futbol.controller.AuditoriaController;
import edu.dosw.project.SFC_TechUp_Futbol.controller.dto.request.RegistroAdministrativoRequest;
import edu.dosw.project.SFC_TechUp_Futbol.core.exception.ErrorHandler;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.TipoAccionAuditoria;
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
import edu.dosw.project.SFC_TechUp_Futbol.core.validator.AuditoriaValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

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
        AdministradorRepositoryImpl administradorRepository = new AdministradorRepositoryImpl();
        OrganizadorRepositoryImpl organizadorRepository = new OrganizadorRepositoryImpl();
        ArbitroRepositoryImpl arbitroRepository = new ArbitroRepositoryImpl();
        UsuarioRegistradoRepositoryImpl usuarioRegistradoRepository = new UsuarioRegistradoRepositoryImpl();
        RegistroAuditoriaRepositoryImpl registroAuditoriaRepository = new RegistroAuditoriaRepositoryImpl();

        auditoriaService = new AuditoriaService(registroAuditoriaRepository);
        administradorService = new AdministradorService(
                administradorRepository,
                organizadorRepository,
                arbitroRepository,
                usuarioRegistradoRepository,
                auditoriaService
        );
        autenticacionAdministradorService = new AutenticacionAdministradorService(administradorRepository);
        AdministradorValidator administradorValidator = new AdministradorValidator(
                administradorRepository,
                organizadorRepository,
                arbitroRepository,
                usuarioRegistradoRepository
        );

        mockMvc = MockMvcBuilders
                .standaloneSetup(new AuditoriaController(
                        auditoriaService,
                        new AuditoriaValidator(),
                        administradorValidator,
                        autenticacionAdministradorService
                ))
                .setControllerAdvice(new ErrorHandler())
                .build();
    }

    @Test
    void consultarHistorial_conFiltrosValidos_retorna200() throws Exception {
        administradorService.registrarAdministrador(crearAdmin("Admin", "admin@escuelaing.edu.co"));
        String token = autenticacionAdministradorService.login("admin@escuelaing.edu.co", "password123");
        auditoriaService.registrarEvento(1L, "organizador@escuelaing.edu.co", TipoAccionAuditoria.REGISTRO_ORGANIZADOR, "Registro administrativo");

        mockMvc.perform(get("/api/admin/auditoria")
                        .header("X-Administrador-Id", 1)
                        .header("X-Administrador-Token", token)
                        .param("usuario", "organizador")
                        .param("tipoAccion", "REGISTRO_ORGANIZADOR"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mensaje").value("Consulta realizada correctamente."))
                .andExpect(jsonPath("$.registros[0].tipoAccion").value("REGISTRO_ORGANIZADOR"));
    }

    @Test
    void consultarHistorial_conRangoInconsistente_retorna400() throws Exception {
        administradorService.registrarAdministrador(crearAdmin("Admin", "admin@escuelaing.edu.co"));
        String token = autenticacionAdministradorService.login("admin@escuelaing.edu.co", "password123");

        mockMvc.perform(get("/api/admin/auditoria")
                        .header("X-Administrador-Id", 1)
                        .header("X-Administrador-Token", token)
                        .param("fechaDesde", "2026-03-25")
                        .param("fechaHasta", "2026-03-20"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detalle").value("El rango de fechas es inconsistente."));
    }

    @Test
    void consultarHistorial_sinCoincidencias_retornaMensajeInformativo() throws Exception {
        administradorService.registrarAdministrador(crearAdmin("Admin", "admin@escuelaing.edu.co"));
        String token = autenticacionAdministradorService.login("admin@escuelaing.edu.co", "password123");

        mockMvc.perform(get("/api/admin/auditoria")
                        .header("X-Administrador-Id", 1)
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
