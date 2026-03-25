package edu.dosw.project.SFC_TechUp_Futbol.core.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<Map<String, Object>> handleRecursoNoEncontrado(RecursoNoEncontradoException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(armarError(404, ex.getMessage()));
    }

    @ExceptionHandler(ReglaNegocioException.class)
    public ResponseEntity<Map<String, Object>> handleReglaNegocio(ReglaNegocioException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(armarError(409, ex.getMessage()));
    }

    @ExceptionHandler(AccesoDenegadoException.class)
    public ResponseEntity<Map<String, Object>> handleAccesoDenegado(AccesoDenegadoException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(armarError(403, ex.getMessage()));
    }

    @ExceptionHandler(AutenticacionAdminException.class)
    public ResponseEntity<Map<String, Object>> handleAutenticacionAdmin(AutenticacionAdminException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(armarError(401, ex.getMessage()));
    }

    @ExceptionHandler(CorreoYaRegistradoException.class)
    public ResponseEntity<Map<String, Object>> handleCorreoYaRegistrado(CorreoYaRegistradoException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(armarError(409, ex.getMessage()));
    }

    @ExceptionHandler(RolNoPermitidoException.class)
    public ResponseEntity<Map<String, Object>> handleRolNoPermitido(RolNoPermitidoException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(armarError(400, ex.getMessage()));
    }

    @ExceptionHandler(FiltroAuditoriaInvalidoException.class)
    public ResponseEntity<Map<String, Object>> handleFiltroAuditoriaInvalido(FiltroAuditoriaInvalidoException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(armarError(400, ex.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(armarError(400, ex.getMessage()));
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalState(IllegalStateException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(armarError(409, ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneral(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(armarError(500, "Algo salio mal, intenta de nuevo"));
    }

    private Map<String, Object> armarError(int codigo, String detalle) {
        Map<String, Object> error = new LinkedHashMap<>();
        error.put("codigo", codigo);
        error.put("detalle", detalle);
        return error;
    }
}
