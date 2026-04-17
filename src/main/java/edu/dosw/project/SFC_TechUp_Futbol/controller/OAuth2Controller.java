package edu.dosw.project.SFC_TechUp_Futbol.controller;

import edu.dosw.project.SFC_TechUp_Futbol.controller.dto.response.OAuth2Response;
import edu.dosw.project.SFC_TechUp_Futbol.core.service.OAuth2Service;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Access", description = "Registration and login for all system actors.")
@RestController
@RequestMapping("/api/access")
public class OAuth2Controller {

    private final OAuth2Service oAuth2Service;

    public OAuth2Controller(OAuth2Service oAuth2Service) {
        this.oAuth2Service = oAuth2Service;
    }

    @Operation(summary = "Google OAuth2 callback",
               description = "Callback de Google OAuth2. Registra al usuario si no existe (rol FAMILIAR) y retorna un token JWT.")
    @GetMapping("/oauth2/google")
    public OAuth2Response googleCallback(OAuth2AuthenticationToken authentication) {
        return oAuth2Service.procesarCallback(authentication);
    }
}
