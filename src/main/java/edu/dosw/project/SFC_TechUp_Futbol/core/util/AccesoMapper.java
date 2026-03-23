package edu.dosw.project.SFC_TechUp_Futbol.core.util;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.UsuarioRegistrado;
import edu.dosw.project.SFC_TechUp_Futbol.controller.dto.request.RegistroRequest;
import edu.dosw.project.SFC_TechUp_Futbol.controller.dto.response.LoginResponse;
import edu.dosw.project.SFC_TechUp_Futbol.controller.dto.response.UsuarioResponse;

public class AccesoMapper {

    public static UsuarioRegistrado toModelo(RegistroRequest request) {
        return new UsuarioRegistrado(
            null,
            request.getNombre(),
            request.getEmail(),
            PasswordUtil.cifrar(request.getPassword()),
            request.getTipoUsuario()
        );
    }

    public static UsuarioResponse toUsuarioResponse(UsuarioRegistrado usuario) {
        return new UsuarioResponse(
            usuario.getId(),
            usuario.getName(),
            usuario.getEmail(),
            usuario.getUserType()
        );
    }

    public static LoginResponse toLoginResponse(UsuarioRegistrado usuario, String token) {
        return new LoginResponse(
            token,
            usuario.getName(),
            usuario.getEmail(),
            usuario.getUserType()
        );
    }
}
