package edu.dosw.project.SFC_TechUp_Futbol.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.dosw.project.SFC_TechUp_Futbol.controller.dto.response.OAuth2Response;
import edu.dosw.project.SFC_TechUp_Futbol.core.service.OAuth2Service;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;

import static org.mockito.Mockito.*;

class OAuth2SuccessHandlerTest {

    @Test
    void onAuthenticationSuccess_escribeRespuestaJson() throws Exception {
        OAuth2Service oAuth2Service = mock(OAuth2Service.class);
        OAuth2SuccessHandler handler = new OAuth2SuccessHandler(oAuth2Service);

        OAuth2User user = mock(OAuth2User.class);
        when(user.getAttributes()).thenReturn(Map.of("email", "test@test.com", "name", "Test"));

        OAuth2AuthenticationToken token = mock(OAuth2AuthenticationToken.class);
        when(token.getPrincipal()).thenReturn(user);

        OAuth2Response response = new OAuth2Response("jwt-token");
        when(oAuth2Service.procesarCallback(token)).thenReturn(response);

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse httpResponse = mock(HttpServletResponse.class);
        StringWriter sw = new StringWriter();
        when(httpResponse.getWriter()).thenReturn(new PrintWriter(sw));

        handler.onAuthenticationSuccess(request, httpResponse, token);

        verify(httpResponse).setContentType("application/json");
        verify(httpResponse).setCharacterEncoding("UTF-8");
        verify(oAuth2Service).procesarCallback(token);
    }
}
