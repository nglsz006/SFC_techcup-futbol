package edu.dosw.project.SFC_TechUp_Futbol.controller;

import edu.dosw.project.SFC_TechUp_Futbol.core.service.AccesoService;
import edu.dosw.project.SFC_TechUp_Futbol.core.validator.AccesoValidator;
import edu.dosw.project.SFC_TechUp_Futbol.controller.dto.request.LoginRequest;
import edu.dosw.project.SFC_TechUp_Futbol.controller.dto.request.RegistroRequest;
import edu.dosw.project.SFC_TechUp_Futbol.controller.dto.response.LoginResponse;
import edu.dosw.project.SFC_TechUp_Futbol.controller.dto.response.UsuarioResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Acceso", description = "Registro e inicio de sesión para todos los actores del sistema.")
@RestController
@RequestMapping("/api/acceso")
public class AccesoController {

    private final AccesoService authService;
    private final AccesoValidator authValidator;

    public AccesoController(AccesoService authService, AccesoValidator authValidator) {
        this.authService = authService;
        this.authValidator = authValidator;
    }

    @Operation(summary = "Registrar usuario", description = "Crea una nueva cuenta en el sistema. Disponible para Jugadores y Capitanes.")
    @PostMapping("/registro")
    public UsuarioResponse registrar(@RequestBody RegistroRequest request) {
        authValidator.validarRegistro(request);
        return authService.registrar(request);
    }

    @Operation(summary = "Iniciar sesión", description = "Autentica al usuario y retorna sus datos de sesión.")
    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        authValidator.validarLogin(request);
        return authService.login(request);
    }
}
