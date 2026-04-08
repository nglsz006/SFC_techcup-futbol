package edu.dosw.project.SFC_TechUp_Futbol.persistence.mapper;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.PerfilDeportivo;
import edu.dosw.project.SFC_TechUp_Futbol.core.util.Base64Util;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.entity.PerfilDeportivoEntity;
import org.springframework.stereotype.Component;

@Component
public class PerfilDeportivoMapper {

    public PerfilDeportivoEntity toEntity(PerfilDeportivo perfil) {
        if (perfil == null) {
            return null;
        }
        PerfilDeportivoEntity entity = new PerfilDeportivoEntity();
        entity.setId(perfil.getId());
        entity.setJugadorId(perfil.getJugadorId());
        entity.setPosiciones(perfil.getPosiciones());
        entity.setDorsal(perfil.getDorsal());
        entity.setFoto(perfil.getFoto());
        entity.setEdad(perfil.getEdad());
        entity.setGenero(perfil.getGenero());
        entity.setIdentificacion(Base64Util.encode(perfil.getIdentificacion()));
        entity.setSemestre(perfil.getSemestre());
        return entity;
    }

    public PerfilDeportivo toDomain(PerfilDeportivoEntity entity) {
        if (entity == null) {
            return null;
        }
        PerfilDeportivo perfil = new PerfilDeportivo();
        perfil.setId(entity.getId());
        perfil.setJugadorId(entity.getJugadorId());
        perfil.setPosiciones(entity.getPosiciones());
        perfil.setDorsal(entity.getDorsal());
        perfil.setFoto(entity.getFoto());
        perfil.setEdad(entity.getEdad());
        perfil.setGenero(entity.getGenero());
        perfil.setIdentificacion(Base64Util.decode(entity.getIdentificacion()));
        perfil.setSemestre(entity.getSemestre());
        return perfil;
    }
}
