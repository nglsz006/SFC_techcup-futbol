package edu.dosw.project.SFC_TechUp_Futbol.controller;

import edu.dosw.project.SFC_TechUp_Futbol.core.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<Map<String, Object>> handleRecursoNoEncontrado(RecursoNoEncontradoException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(armarError(404, "NOT_FOUND", ex.getMessage(),
                "El recurso solicitado no existe en el sistema. Verifica el identificador enviado."));
    }

    @ExceptionHandler(ReglaNegocioException.class)
    public ResponseEntity<Map<String, Object>> handleReglaNegocio(ReglaNegocioException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(armarError(409, "BUSINESS_RULE_VIOLATION", ex.getMessage(),
                "La operacion viola una regla de negocio del sistema. Revisa las condiciones requeridas antes de intentarlo."));
    }

    @ExceptionHandler(AccesoDenegadoException.class)
    public ResponseEntity<Map<String, Object>> handleAccesoDenegado(AccesoDenegadoException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(armarError(403, "ACCESS_DENIED", ex.getMessage(),
                "No tienes autorizacion para realizar esta accion. Verifica tus credenciales o contacta al administrador."));
    }

    @ExceptionHandler(AutenticacionAdminException.class)
    public ResponseEntity<Map<String, Object>> handleAutenticacionAdmin(AutenticacionAdminException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(armarError(401, "AUTHENTICATION_FAILED", ex.getMessage(),
                "La autenticacion del administrador fallo. Verifica el email, la contrasena y que la sesion este activa."));
    }

    @ExceptionHandler(CorreoYaRegistradoException.class)
    public ResponseEntity<Map<String, Object>> handleCorreoYaRegistrado(CorreoYaRegistradoException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(armarError(409, "EMAIL_ALREADY_REGISTERED", ex.getMessage(),
                "El correo electronico ya esta registrado en el sistema. Usa otro correo o inicia sesion con el existente."));
    }

    @ExceptionHandler(RolNoPermitidoException.class)
    public ResponseEntity<Map<String, Object>> handleRolNoPermitido(RolNoPermitidoException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(armarError(400, "ROLE_NOT_ALLOWED", ex.getMessage(),
                "El rol especificado no esta permitido para esta operacion. Solo se permiten los roles ORGANIZADOR y ARBITRO."));
    }

    @ExceptionHandler(FiltroAuditoriaInvalidoException.class)
    public ResponseEntity<Map<String, Object>> handleFiltroAuditoriaInvalido(FiltroAuditoriaInvalidoException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(armarError(400, "INVALID_AUDIT_FILTER", ex.getMessage(),
                "Los filtros de consulta de auditoria son invalidos. Verifica el rango de fechas y los valores enviados."));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(armarError(400, "INVALID_ARGUMENT", ex.getMessage(),
                "Uno o mas argumentos enviados son invalidos. Revisa los datos del cuerpo de la solicitud."));
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalState(IllegalStateException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(armarError(409, "INVALID_STATE", ex.getMessage(),
                "La operacion no es valida en el estado actual del recurso. Verifica el estado antes de intentar la accion."));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Map<String, Object>> handleAccessDenied(AccessDeniedException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(armarError(403, "ACCESS_DENIED", "Acceso denegado.",
                "No tienes el rol necesario para acceder a este recurso. Asegurate de estar autenticado con el rol correcto."));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntime(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(armarError(500, "INTERNAL_ERROR", ex.getMessage(),
                "Ocurrio un error inesperado en el servidor. Si el problema persiste, contacta al equipo de soporte."));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneral(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(armarError(500, "INTERNAL_ERROR",
                "Error interno del servidor.",
                "Ocurrio un error no controlado en el servidor. Revisa los datos enviados o contacta al equipo de soporte si el problema persiste."));
    }

    private Map<String, Object> armarError(int codigo, String tipo, String detalle, String descripcion) {
        Map<String, Object> error = new LinkedHashMap<>();
        error.put("timestamp", LocalDateTime.now());
        error.put("codigo", codigo);
        error.put("tipo", tipo);
        error.put("detalle", detalle);
        error.put("descripcion", descripcion);
        return error;
    }
}
