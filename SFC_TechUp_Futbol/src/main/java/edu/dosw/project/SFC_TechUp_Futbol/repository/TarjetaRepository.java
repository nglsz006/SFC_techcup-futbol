package edu.dosw.project.SFC_TechUp_Futbol.repository;

import edu.dosw.project.SFC_TechUp_Futbol.model.Tarjeta;

import java.util.List;

public interface TarjetaRepository {
    Tarjeta save(Tarjeta tarjeta);
    List<Tarjeta> findByPartidoId(Long partidoId);
    List<Tarjeta> findByJugadorId(Long jugadorId);
    List<Tarjeta> findByPartidoIdAndTipo(Long partidoId, Tarjeta.TipoTarjeta tipo);
}
