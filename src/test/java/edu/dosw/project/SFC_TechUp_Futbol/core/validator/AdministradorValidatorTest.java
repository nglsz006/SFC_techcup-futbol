package edu.dosw.project.SFC_TechUp_Futbol.core.validator;

import edu.dosw.project.SFC_TechUp_Futbol.controller.dto.request.RegistroAdministrativoRequest;
import edu.dosw.project.SFC_TechUp_Futbol.core.exception.CorreoYaRegistradoException;
import edu.dosw.project.SFC_TechUp_Futbol.core.exception.RolNoPermitidoException;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.Organizador;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.Usuario;
import edu.dosw.project.SFC_TechUp_Futbol.core.validator.AdministradorValidator;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.entity.AdministradorEntity;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.entity.ArbitroEntity;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.entity.OrganizadorEntity;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.entity.UsuarioRegistradoEntity;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.mapper.EquipoMapper;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.mapper.OrganizadorMapper;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.mapper.TorneoMapper;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.repository.AdministradorJpaRepository;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.repository.ArbitroJpaRepository;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.repository.OrganizadorJpaRepository;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.repository.TorneoJpaRepository;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.repository.UsuarioRegistradoJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class AdministradorValidatorTest {

    private AdministradorValidator administradorValidator;
    private OrganizadorJpaRepository organizadorRepository;
    private OrganizadorMapper orgMapper;

    @BeforeEach
    void setUp() {
        TorneoJpaRepository torneoRepo = mock(TorneoJpaRepository.class);
        TorneoMapper torneoMapper = new TorneoMapper();
        orgMapper = new OrganizadorMapper(torneoRepo, torneoMapper);

        Map<String, OrganizadorEntity> orgStore = new HashMap<>();
        organizadorRepository = mock(OrganizadorJpaRepository.class);
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

        AdministradorJpaRepository adminRepo = mock(AdministradorJpaRepository.class);
        when(adminRepo.findByEmail(anyString())).thenReturn(Optional.empty());
        when(adminRepo.findAll()).thenReturn(new ArrayList<>());

        ArbitroJpaRepository arbitroRepo = mock(ArbitroJpaRepository.class);
        when(arbitroRepo.findByEmail(anyString())).thenReturn(Optional.empty());
        when(arbitroRepo.findAll()).thenReturn(new ArrayList<>());

        UsuarioRegistradoJpaRepository usuarioRepo = mock(UsuarioRegistradoJpaRepository.class);
        when(usuarioRepo.findByEmail(anyString())).thenReturn(Optional.empty());
        when(usuarioRepo.findAll()).thenReturn(new ArrayList<>());

        administradorValidator = new AdministradorValidator(adminRepo, organizadorRepository, arbitroRepo, usuarioRepo);
    }

    @Test
    void validarRegistro_cuandoEsValido_noLanzaExcepcion() {
        assertDoesNotThrow(() -> administradorValidator.validarRegistro(crearRequest("Organizador", "organizador@escuelaing.edu.co", "ORGANIZADOR")));
    }

    @Test
    void validarRegistro_cuandoNombreEsVacio_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> administradorValidator.validarRegistro(crearRequest(" ", "organizador@escuelaing.edu.co", "ORGANIZADOR")));
    }

    @Test
    void validarRegistro_cuandoCorreoYaExiste_lanzaExcepcion() {
        OrganizadorEntity entity = new OrganizadorEntity();
        entity.setId(UUID.randomUUID().toString());
        entity.setName("Existente");
        entity.setEmail("duplicado@escuelaing.edu.co");
        entity.setPassword("password123");
        entity.setUserType(Usuario.TipoUsuario.ESTUDIANTE);
        organizadorRepository.save(entity);
        assertThrows(CorreoYaRegistradoException.class,
                () -> administradorValidator.validarRegistro(crearRequest("Nuevo", "duplicado@escuelaing.edu.co", "ORGANIZADOR")));
    }

    @Test
    void validarRegistro_cuandoRolNoEsPermitido_lanzaExcepcion() {
        assertThrows(RolNoPermitidoException.class,
                () -> administradorValidator.validarRegistro(crearRequest("Supervisor", "supervisor@escuelaing.edu.co", "SUPERVISOR")));
    }

    @Test
    void validarRegistro_cuandoSolicitudEsNula_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> administradorValidator.validarRegistro(null));
    }

    @Test
    void validarRegistro_cuandoRolEsVacio_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class,
                () -> administradorValidator.validarRegistro(crearRequest("Organizador", "org2@escuelaing.edu.co", " ")));
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
