package edu.dosw.project.SFC_TechUp_Futbol.core.util;

import edu.dosw.project.SFC_TechUp_Futbol.controller.dto.response.OAuth2Response;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.Usuario;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.UsuarioRegistrado;
import edu.dosw.project.SFC_TechUp_Futbol.core.service.OAuth2Service;
import edu.dosw.project.SFC_TechUp_Futbol.core.util.JwtFilter;
import edu.dosw.project.SFC_TechUp_Futbol.core.util.JwtService;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.entity.UsuarioRegistradoEntity;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.mapper.UsuarioRegistradoMapper;
import edu.dosw.project.SFC_TechUp_Futbol.TestMappers;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.repository.UsuarioRegistradoJpaRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OAuth2AuthTest {

    private JwtService jwtService;
    private UsuarioRegistradoJpaRepository repo;
    private UsuarioRegistradoMapper mapper;
    private OAuth2Service oAuth2Service;
    private JwtFilter jwtFilter;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
        mapper = TestMappers.usuarioRegistradoMapper();
        Map<String, UsuarioRegistradoEntity> store = new HashMap<>();
        repo = mock(UsuarioRegistradoJpaRepository.class);
        when(repo.save(any())).thenAnswer(inv -> {
            UsuarioRegistradoEntity e = inv.getArgument(0);
            if (e.getId() == null) e.setId(UUID.randomUUID().toString());
            store.put(e.getId(), e);
            return e;
        });
        when(repo.findByEmail(anyString())).thenAnswer(inv -> {
            String email = inv.getArgument(0);
            return store.values().stream().filter(e -> email.equals(e.getEmail())).findFirst();
        });
        when(repo.findAll()).thenAnswer(inv -> new ArrayList<>(store.values()));
        oAuth2Service = new OAuth2Service(repo, mapper, jwtService);
        jwtFilter = new JwtFilter(jwtService);
    }

    private OAuth2AuthenticationToken crearToken(String email, String nombre) {
        Map<String, Object> atributos = new HashMap<>();
        atributos.put("email", email);
        atributos.put("name", nombre);
        OAuth2User user = new DefaultOAuth2User(new ArrayList<>(), atributos, "email");
        return new OAuth2AuthenticationToken(user, new ArrayList<>(), "google");
    }

    @Test
    void oauth2_usuarioNuevo_seRegistraYRetornaToken() {
        OAuth2AuthenticationToken token = crearToken("nuevo@gmail.com", "Nuevo");
        OAuth2Response response = oAuth2Service.procesarCallback(token);
        assertNotNull(response.getToken());
        verify(repo, times(1)).save(any());
    }

    @Test
    void oauth2_usuarioExistente_seAutenticaSinRegistrar() {
        UsuarioRegistradoEntity existente = new UsuarioRegistradoEntity();
        existente.setId("uuid-1");
        existente.setName("Existente");
        existente.setEmail("existente@gmail.com");
        existente.setPassword("hash");
        existente.setUserType(Usuario.TipoUsuario.FAMILIAR);
        when(repo.findByEmail("existente@gmail.com")).thenReturn(Optional.of(existente));
        OAuth2AuthenticationToken token = crearToken("existente@gmail.com", "Existente");
        OAuth2Response response = oAuth2Service.procesarCallback(token);
        assertNotNull(response.getToken());
        verify(repo, never()).save(any());
    }

    @Test
    void oauth2_correoNoGmail_lanzaExcepcion() {
        OAuth2AuthenticationToken token = crearToken("usuario@escuelaing.edu.co", "Usuario");
        assertThrows(IllegalArgumentException.class, () -> oAuth2Service.procesarCallback(token));
    }

    @Test
    void oauth2_tokenRetornado_esJwtValido() {
        OAuth2AuthenticationToken token = crearToken("valido@gmail.com", "Valido");
        OAuth2Response response = oAuth2Service.procesarCallback(token);
        assertTrue(jwtService.esValido(response.getToken()));
        assertEquals("valido@gmail.com", jwtService.extraerEmail(response.getToken()));
    }

    @Test
    void jwtFilter_sinToken_continuaSinAutenticar() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain chain = mock(FilterChain.class);
        jwtFilter.doFilterInternal(request, response, chain);
        verify(chain, times(1)).doFilter(request, response);
        assertEquals(200, response.getStatus());
    }

    @Test
    void jwtFilter_tokenValido_continuaAutenticado() throws Exception {
        String token = jwtService.generarToken("test@escuelaing.edu.co", edu.dosw.project.SFC_TechUp_Futbol.core.model.RolFuncional.JUGADOR);
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer " + token);
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain chain = mock(FilterChain.class);
        jwtFilter.doFilterInternal(request, response, chain);
        verify(chain, times(1)).doFilter(request, response);
        assertEquals(200, response.getStatus());
    }

    @Test
    void jwtFilter_tokenInvalido_retorna401() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer token.invalido.xyz");
        MockHttpServletResponse response = new MockHttpServletResponse();
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        HttpServletResponse mockResponse = mock(HttpServletResponse.class);
        when(mockResponse.getWriter()).thenReturn(pw);
        FilterChain chain = mock(FilterChain.class);
        jwtFilter.doFilterInternal(request, mockResponse, chain);
        verify(mockResponse).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verify(chain, never()).doFilter(any(), any());
    }

    @Test
    void jwtFilter_tokenExpirado_retorna401() throws Exception {
        String tokenExpirado = io.jsonwebtoken.Jwts.builder()
                .setSubject("expirado@escuelaing.edu.co")
                .claim("rol", "ESTUDIANTE")
                .setIssuedAt(new java.util.Date(System.currentTimeMillis() - 7200_000))
                .setExpiration(new java.util.Date(System.currentTimeMillis() - 3600_000))
                .signWith(io.jsonwebtoken.SignatureAlgorithm.HS256,
                        io.jsonwebtoken.security.Keys.secretKeyFor(io.jsonwebtoken.SignatureAlgorithm.HS256))
                .compact();
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer " + tokenExpirado);
        MockHttpServletResponse response = new MockHttpServletResponse();
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        HttpServletResponse mockResponse = mock(HttpServletResponse.class);
        when(mockResponse.getWriter()).thenReturn(pw);
        FilterChain chain = mock(FilterChain.class);
        jwtFilter.doFilterInternal(request, mockResponse, chain);
        verify(mockResponse).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verify(chain, never()).doFilter(any(), any());
    }
}
