package edu.dosw.project.SFC_TechUp_Futbol.controller.dto.response;

public class OAuth2Response {
    private String token;

    public OAuth2Response(String token) {
        this.token = token;
    }

    public String getToken() { return token; }
}
