package edu.dosw.project.SFC_TechUp_Futbol.core.model;

public abstract class Usuario {

    public enum TipoUsuario {
        ESTUDIANTE, GRADUADO, PROFESOR, PERSONAL_ADMIN, FAMILIAR
    }

    private Long id;
    private String name;
    private String email;
    private String password;
    private TipoUsuario userType;

    public Usuario() {

    }

    public Usuario(Long id, String name, String email, String password, TipoUsuario userType) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.userType = userType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public TipoUsuario getUserType() {
        return userType;
    }

    public void setUserType(TipoUsuario userType) {
        this.userType = userType;
    }
}

