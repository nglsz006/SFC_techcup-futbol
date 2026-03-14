package edu.dosw.project.SFC_TechUp_Futbol.core.repository;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Tarjeta;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class TarjetaRepositoryImpl implements TarjetaRepository {

    private final List<Tarjeta> store = new ArrayList<>();

    @Override
    public Tarjeta save(Tarjeta tarjeta) {
        store.add(tarjeta);
        return tarjeta;
    }

    @Override
    public List<Tarjeta> findByPartidoId(Long partidoId) {
        return store.stream()
                .filter(t -> t.getPartido() != null && partidoId.equals(t.getPartido().getId()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Tarjeta> findByJugadorId(Long jugadorId) {
        return store.stream()
                .filter(t -> t.getJugador() != null && jugadorId.equals(t.getJugador().getId()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Tarjeta> findByPartidoIdAndTipo(Long partidoId, Tarjeta.TipoTarjeta tipo) {
        return store.stream()
                .filter(t -> t.getPartido() != null && partidoId.equals(t.getPartido().getId()) && t.getTipo() == tipo)
                .collect(Collectors.toList());
    }
}
