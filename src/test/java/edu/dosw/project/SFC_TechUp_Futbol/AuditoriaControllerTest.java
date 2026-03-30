package edu.dosw.project.SFC_TechUp_Futbol;

import edu.dosw.project.SFC_TechUp_Futbol.controller.AuditoriaController;
import edu.dosw.project.SFC_TechUp_Futbol.controller.dto.request.RegistroAdministrativoRequest;
import edu.dosw.project.SFC_TechUp_Futbol.core.exception.ErrorHandler;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.*;
import edu.dosw.project.SFC_TechUp_Futbol.core.repository.*;
import edu.dosw.project.SFC_TechUp_Futbol.core.service.AdministradorService;
import edu.dosw.project.SFC_TechUp_Futbol.core.service.AuditoriaService;
import edu.dosw.project.SFC_TechUp_Futbol.core.service.AutenticacionAdministradorService;
import edu.dosw.project.SFC_TechUp_Futbol.core.validator.AdministradorValidator;
import edu.dosw.project.SFC_TechUp_Futbol.core.validator.AuditoriaValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

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
        Map<Long, Administrador> adminStore = new HashMap<>();
        AtomicLong adminIdGen = new AtomicLong(1);
        AdministradorRepository administradorRepository = mock(AdministradorRepository.class);
        when(administradorRepository.save(any())).thenAnswer(inv -> {
            Administrador a = inv.getArgument(0);
            if (a.getId() == null) a.setId(adminIdGen.getAndIncrement());
            adminStore.put(a.getId(), a);
            return a;
        });
        when(administradorRepository.findById(anyLong())).thenAnswer(inv -> Optional.ofNullable(adminStore.get(inv.<Long>getArgument(0))));
        when(administradorRepository.findByEmail(anyString())).thenAnswer(inv -> {
            String email = inv.getArgument(0);
            return adminStore.values().stream().filter(a -> email.equals(a.getEmail())).findFirst();
        });
        when(administradorRepository.findAll()).thenAnswer(inv -> new ArrayList<>(adminStore.values()));

        Map<Long, Organizador> orgStore = new HashMap<>();
        AtomicLong orgIdGen = new AtomicLong(1);
        OrganizadorRepository organizadorRepository = mock(OrganizadorRepository.class);
        when(organizadorRepository.save(any())).thenAnswer(inv -> {
            Organizador o = inv.getArgument(0);
            if (o.getId() == null) o.setId(orgIdGen.getAndIncrement());
            orgStore.put(o.getId(), o);
            return o;
        });
        when(organizadorRepository.findByEmail(anyString())).thenAnswer(inv -> {
            String email = inv.getArgument(0);
            return orgStore.values().stream().filter(o -> email.equals(o.getEmail())).findFirst();
        });
        when(organizadorRepository.findAll()).thenAnswer(inv -> new ArrayList<>(orgStore.values()));

        Map<Long, Arbitro> arbitroStore = new HashMap<>();
        AtomicLong arbitroIdGen = new AtomicLong(1);
        ArbitroRepository arbitroRepository = mock(ArbitroRepository.class);
        when(arbitroRepository.save(any())).thenAnswer(inv -> {
            Arbitro a = inv.getArgument(0);
            if (a.getId() == null) a.setId(arbitroIdGen.getAndIncrement());
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

        java.util.List<RegistroAuditoria> auditoriaList = new ArrayList<>();
        AtomicLong auditoriaIdGen = new AtomicLong(1);
        RegistroAuditoriaRepository registroAuditoriaRepository = mock(RegistroAuditoriaRepository.class);
        when(registroAuditoriaRepository.save(any())).thenAnswer(inv -> {
            RegistroAuditoria r = inv.getArgument(0);
            if (r.getId() == null) r.setId(auditoriaIdGen.getAndIncrement());
            auditoriaList.add(r);
            return r;
        });
        when(registroAuditoriaRepository.findAll()).thenAnswer(inv -> new ArrayList<>(auditoriaList));

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
