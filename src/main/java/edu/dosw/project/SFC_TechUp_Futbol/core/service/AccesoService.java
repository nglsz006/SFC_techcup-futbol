package edu.dosw.project.SFC_TechUp_Futbol.core.service;

import edu.dosw.project.SFC_TechUp_Futbol.dto.request.LoginRequest;
import edu.dosw.project.SFC_TechUp_Futbol.dto.request.RegistroRequest;
import edu.dosw.project.SFC_TechUp_Futbol.dto.response.LoginResponse;
import edu.dosw.project.SFC_TechUp_Futbol.dto.response.UsuarioResponse;

public interface AccesoService {
    UsuarioResponse registrar(RegistroRequest request);
    LoginResponse login(LoginRequest request);
}
