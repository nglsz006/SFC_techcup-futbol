package edu.dosw.project.SFC_TechUp_Futbol.controller.dto.response;

import java.util.List;

public class ConsultaAuditoriaResponse {

    private final String mensaje;
    private final List<RegistroAuditoriaResponse> registros;

    public ConsultaAuditoriaResponse(String mensaje, List<RegistroAuditoriaResponse> registros) {
        this.mensaje = mensaje;
        this.registros = registros;
    }

    public String getMensaje() {
        return mensaje;
    }

    public List<RegistroAuditoriaResponse> getRegistros() {
        return registros;
    }
}
