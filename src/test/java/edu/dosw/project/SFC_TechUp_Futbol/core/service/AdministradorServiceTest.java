package edu.dosw.project.SFC_TechUp_Futbol.core.service;

import edu.dosw.project.SFC_TechUp_Futbol.controller.dto.request.RegistroAdministrativoRequest;
import edu.dosw.project.SFC_TechUp_Futbol.core.exception.AccesoDenegadoException;
import edu.dosw.project.SFC_TechUp_Futbol.core.exception.CorreoYaRegistradoException;
import edu.dosw.project.SFC_TechUp_Futbol.core.exception.RolNoPermitidoException;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.*;
import edu.dosw.project.SFC_TechUp_Futbol.core.repository.*;
import edu.dosw.project.SFC_TechUp_Futbol.core.service.AdministradorService;
import edu.dosw.project.SFC_TechUp_Futbol.core.service.AuditoriaService;
import edu.dosw.project.SFC_TechUp_Futbol.core.util.PasswordUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AdministradorServiceTest {

    private AdministradorService administradorService;
    private RegistroAuditoriaRepository auditoriaRepository;

    @BeforeEach
    void setUp() {
        Map<String, Administrador> adminStore = new HashMap<>();
        AdministradorRepository adminRepo = mock(AdministradorRepository.class);
        when(adminRepo.save(any())).thenAnswer(inv -> {
            Administrador a = inv.getArgument(0);
            if (a.getId() == null) a.setId(UUID.randomUUID().toString());
            adminStore.put(a.getId(), a);
            return a;
        });
        when(adminRepo.findById(anyString())).thenAnswer(inv -> Optional.ofNullable(adminStore.get(inv.<String>getArgument(0))));
        when(adminRepo.findByEmail(anyString())).thenAnswer(inv -> {
            String email = inv.getArgument(0);
            return adminStore.values().stream().filter(a -> email.equals(a.getEmail())).findFirst();
        });
        when(adminRepo.findAll()).thenAnswer(inv -> new ArrayList<>(adminStore.values()));

        Map<String, Organizador> orgStore = new HashMap<>();
        OrganizadorRepository orgRepo = mock(OrganizadorRepository.class);
        when(orgRepo.save(any())).thenAnswer(inv -> {
            Organizador o = inv.getArgument(0);
            if (o.getId() == null) o.setId(UUID.randomUUID().toString());
            orgStore.put(o.getId(), o);
            return o;
        });
        when(orgRepo.findByEmail(anyString())).thenAnswer(inv -> {
            String email = inv.getArgument(0);
            return orgStore.values().stream().filter(o -> email.equals(o.getEmail())).findFirst();
        });
        when(orgRepo.findAll()).thenAnswer(inv -> new ArrayList<>(orgStore.values()));

        Map<String, Arbitro> arbitroStore = new HashMap<>();
        ArbitroRepository arbitroRepo = mock(ArbitroRepository.class);
        when(arbitroRepo.save(any())).thenAnswer(inv -> {
            Arbitro a = inv.getArgument(0);
            if (a.getId() == null) a.setId(UUID.randomUUID().toString());
            arbitroStore.put(a.getId(), a);
            return a;
        });
        when(arbitroRepo.findByEmail(anyString())).thenAnswer(inv -> {
            String email = inv.getArgument(0);
            return arbitroStore.values().stream().filter(a -> email.equals(a.getEmail())).findFirst();
        });
        when(arbitroRepo.findAll()).thenAnswer(inv -> new ArrayList<>(arbitroStore.values()));

        UsuarioRegistradoRepository usuarioRepo = mock(UsuarioRegistradoRepository.class);
        when(usuarioRepo.findByEmail(anyString())).thenReturn(Optional.empty());

        List<RegistroAuditoria> auditoriaList = new ArrayList<>();
        auditoriaRepository = mock(RegistroAuditoriaRepository.class);
        when(auditoriaRepository.save(any())).thenAnswer(inv -> {
            RegistroAuditoria r = inv.getArgument(0);
            if (r.getId() == null) r.setId(UUID.randomUUID().toString());
            auditoriaList.add(r);
            return r;
        });
        when(auditoriaRepository.findAll()).thenAnswer(inv -> new ArrayList<>(auditoriaList));

        administradorService = new AdministradorService(adminRepo, orgRepo, arbitroRepo, usuarioRepo, new AuditoriaService(auditoriaRepository));
    }

    @Test
    void registrarUsuarioAdministrativo_conRolOrganizador_registraOrganizador() {
        Administrador administrador = administradorService.registrarAdministrador(crearRequest("Admin", "admin@escuelaing.edu.co", "ORGANIZADOR"));
        Usuario usuario = administradorService.registrarUsuarioAdministrativo(administrador.getId(), crearRequest("Org", "org@escuelaing.edu.co", "ORGANIZADOR"));
        assertInstanceOf(Organizador.class, usuario);
        assertEquals("org@escuelaing.edu.co", usuario.getEmail());
        assertTrue(PasswordUtil.verificar("password123", usuario.getPassword()));
        assertEquals(TipoAccionAuditoria.REGISTRO_ORGANIZADOR, auditoriaRepository.findAll().getFirst().getTipoAccion());
    }

    @Test
    void registrarUsuarioAdministrativo_conRolArbitro_registraArbitro() {
        Administrador administrador = administradorService.registrarAdministrador(crearRequest("Admin", "admin@escuelaing.edu.co", "ORGANIZADOR"));
        Usuario usuario = administradorService.registrarUsuarioAdministrativo(administrador.getId(), crearRequest("Arbitro", "arbitro@escuelaing.edu.co", "ARBITRO"));
        assertInstanceOf(Arbitro.class, usuario);
        assertEquals("arbitro@escuelaing.edu.co", usuario.getEmail());
        assertEquals(TipoAccionAuditoria.REGISTRO_ARBITRO, auditoriaRepository.findAll().getFirst().getTipoAccion());
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
