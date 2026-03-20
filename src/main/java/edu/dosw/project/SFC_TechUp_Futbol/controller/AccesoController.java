package edu.dosw.project.SFC_TechUp_Futbol.controller;

import edu.dosw.project.SFC_TechUp_Futbol.core.service.AccesoService;
import edu.dosw.project.SFC_TechUp_Futbol.core.validator.AccesoValidator;
import edu.dosw.project.SFC_TechUp_Futbol.dto.request.LoginRequest;
import edu.dosw.project.SFC_TechUp_Futbol.dto.request.RegistroRequest;
import edu.dosw.project.SFC_TechUp_Futbol.dto.response.LoginResponse;
import edu.dosw.project.SFC_TechUp_Futbol.dto.response.UsuarioResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Acceso", description = "Registro e inicio de sesion")
@RestController
@RequestMapping("/api/acceso")
public class AccesoController {

    private final AccesoService authService;
    private final AccesoValidator authValidator;

    public AccesoController(AccesoService authService, AccesoValidator authValidator) {
        this.authService = authService;
        this.authValidator = authValidator;
    }

    @Operation(summary = "Registrar usuario")
    @PostMapping("/registro")
    public UsuarioResponse registrar(@RequestBody RegistroRequest request) {
        authValidator.validarRegistro(request);
        return authService.registrar(request);
    }

    @Operation(summary = "Iniciar sesion")
    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        authValidator.validarLogin(request);
        return authService.login(request);
    }
}
