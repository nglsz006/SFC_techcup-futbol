package edu.dosw.project.SFC_TechUp_Futbol;

import edu.dosw.project.SFC_TechUp_Futbol.core.exception.ErrorHandler;
import edu.dosw.project.SFC_TechUp_Futbol.core.exception.AutenticacionAdminException;
import edu.dosw.project.SFC_TechUp_Futbol.core.exception.CorreoYaRegistradoException;
import edu.dosw.project.SFC_TechUp_Futbol.core.exception.FiltroAuditoriaInvalidoException;
import edu.dosw.project.SFC_TechUp_Futbol.core.exception.RecursoNoEncontradoException;
import edu.dosw.project.SFC_TechUp_Futbol.core.exception.ReglaNegocioException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ExceptionHandlerTest {

    private final ErrorHandler handler = new ErrorHandler();

    @Test
    void recursoNoEncontrado_retorna404() {
        ResponseEntity<Map<String, Object>> resp = handler.handleRecursoNoEncontrado(new RecursoNoEncontradoException("no existe"));
        assertEquals(HttpStatus.NOT_FOUND, resp.getStatusCode());
        assertEquals(404, resp.getBody().get("codigo"));
        assertEquals("no existe", resp.getBody().get("detalle"));
    }

    @Test
    void reglaNegocio_retorna409() {
        ResponseEntity<Map<String, Object>> resp = handler.handleReglaNegocio(new ReglaNegocioException("conflicto"));
        assertEquals(HttpStatus.CONFLICT, resp.getStatusCode());
        assertEquals(409, resp.getBody().get("codigo"));
    }

    @Test
    void autenticacionAdmin_retorna401() {
        ResponseEntity<Map<String, Object>> resp = handler.handleAutenticacionAdmin(new AutenticacionAdminException("sin sesion"));
        assertEquals(HttpStatus.UNAUTHORIZED, resp.getStatusCode());
        assertEquals(401, resp.getBody().get("codigo"));
    }

    @Test
    void correoYaRegistrado_retorna409() {
        ResponseEntity<Map<String, Object>> resp = handler.handleCorreoYaRegistrado(new CorreoYaRegistradoException("duplicado"));
        assertEquals(HttpStatus.CONFLICT, resp.getStatusCode());
        assertEquals(409, resp.getBody().get("codigo"));
    }

    @Test
    void filtroAuditoriaInvalido_retorna400() {
        ResponseEntity<Map<String, Object>> resp = handler.handleFiltroAuditoriaInvalido(new FiltroAuditoriaInvalidoException("filtro invalido"));
        assertEquals(HttpStatus.BAD_REQUEST, resp.getStatusCode());
        assertEquals(400, resp.getBody().get("codigo"));
    }

    @Test
    void illegalArgument_retorna400() {
        ResponseEntity<Map<String, Object>> resp = handler.handleIllegalArgument(new IllegalArgumentException("arg invalido"));
        assertEquals(HttpStatus.BAD_REQUEST, resp.getStatusCode());
        assertEquals(400, resp.getBody().get("codigo"));
    }

    @Test
    void illegalState_retorna409() {
        ResponseEntity<Map<String, Object>> resp = handler.handleIllegalState(new IllegalStateException("estado invalido"));
        assertEquals(HttpStatus.CONFLICT, resp.getStatusCode());
        assertEquals(409, resp.getBody().get("codigo"));
    }

    @Test
    void exceptionGeneral_retorna500() {
        ResponseEntity<Map<String, Object>> resp = handler.handleGeneral(new Exception("error inesperado"));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, resp.getStatusCode());
        assertEquals(500, resp.getBody().get("codigo"));
    }

    @Test
    void recursoNoEncontradoException_mensaje_correcto() {
        RecursoNoEncontradoException ex = new RecursoNoEncontradoException("recurso X no encontrado");
        assertEquals("recurso X no encontrado", ex.getMessage());
    }

    @Test
    void reglaNegocioException_mensaje_correcto() {
        ReglaNegocioException ex = new ReglaNegocioException("regla violada");
        assertEquals("regla violada", ex.getMessage());
    }
}
