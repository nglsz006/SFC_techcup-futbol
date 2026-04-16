package edu.dosw.project.SFC_TechUp_Futbol.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.dosw.project.SFC_TechUp_Futbol.controller.dto.response.OAuth2Response;
import edu.dosw.project.SFC_TechUp_Futbol.core.service.OAuth2Service;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final OAuth2Service oAuth2Service;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public OAuth2SuccessHandler(OAuth2Service oAuth2Service) {
        this.oAuth2Service = oAuth2Service;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;
        OAuth2Response loginResponse = oAuth2Service.procesarCallback(token);
        String frontUrl = "http://localhost:5173/oauth2/callback?token=" + loginResponse.getToken();
        response.sendRedirect(frontUrl);
    }
}
