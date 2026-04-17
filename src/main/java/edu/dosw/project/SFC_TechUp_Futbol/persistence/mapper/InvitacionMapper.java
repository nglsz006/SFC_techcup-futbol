package edu.dosw.project.SFC_TechUp_Futbol.persistence.mapper;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Invitacion;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.entity.InvitacionEntity;
import org.springframework.stereotype.Component;

@Component
public class InvitacionMapper {

    public Invitacion toDomain(InvitacionEntity e) {
        Invitacion i = new Invitacion();
        i.setId(e.getId());
        i.setJugadorId(e.getJugadorId());
        i.setEquipoId(e.getEquipoId());
        i.setPosicion(e.getPosicion());
        i.setFecha(e.getFecha());
        i.setEstado(e.getEstado());
        return i;
    }

    public InvitacionEntity toEntity(Invitacion i) {
        InvitacionEntity e = new InvitacionEntity();
        e.setId(i.getId());
        e.setJugadorId(i.getJugadorId());
        e.setEquipoId(i.getEquipoId());
        e.setPosicion(i.getPosicion());
        e.setFecha(i.getFecha());
        e.setEstado(i.getEstado());
        return e;
    }
}
