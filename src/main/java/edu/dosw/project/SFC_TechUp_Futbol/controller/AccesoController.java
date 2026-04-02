package edu.dosw.project.SFC_TechUp_Futbol.controller;

import edu.dosw.project.SFC_TechUp_Futbol.controller.dto.response.UsuarioResponse;
import edu.dosw.project.SFC_TechUp_Futbol.core.service.AccesoService;
import edu.dosw.project.SFC_TechUp_Futbol.core.validator.AccesoValidator;
import edu.dosw.project.SFC_TechUp_Futbol.controller.dto.request.LoginRequest;
import edu.dosw.project.SFC_TechUp_Futbol.controller.dto.request.RegistroRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Tag(name = "Access", description = "Registration and login for all system actors.")
@RestController
@RequestMapping("/api/access")
public class AccesoController {

    private final AccesoService authService;
    private final AccesoValidator authValidator;

    public AccesoController(AccesoService authService, AccesoValidator authValidator) {
        this.authService = authService;
        this.authValidator = authValidator;
    }

    @Operation(summary = "Register user")
    @PostMapping("/register")
    public UsuarioResponse registrar(@RequestBody RegistroRequest request) {
        authValidator.validarRegistro(request);
        return authService.registrar(request);
    }

    @Operation(summary = "Login")
    @PostMapping("/login")
    public Map<String, String> login(@RequestBody LoginRequest request) {
        authValidator.validarLogin(request);
        return Map.of("token", authService.login(request).getToken());
    }
}
