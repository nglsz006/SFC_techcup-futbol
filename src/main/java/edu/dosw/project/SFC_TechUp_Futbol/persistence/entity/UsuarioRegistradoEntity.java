package edu.dosw.project.SFC_TechUp_Futbol.persistence.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "usuario_registrado")
@DiscriminatorValue("USUARIO_REGISTRADO")
public class UsuarioRegistradoEntity extends UsuarioEntity {

    public UsuarioRegistradoEntity() {}
}
