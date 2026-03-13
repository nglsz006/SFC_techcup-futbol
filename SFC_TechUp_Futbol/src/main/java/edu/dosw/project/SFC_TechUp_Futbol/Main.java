import controllers.TorneoController;
import controllers.EquipoController;
import controllers.AlineacionController;
import models.Torneo;
import models.Equipo;
import models.Alineacion;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        System.out.println("\nsistema de torneos\n");

        TorneoController ctrlTorneo = new TorneoController();
        EquipoController ctrlEquipo = new EquipoController();
        AlineacionController ctrlAlineacion = new AlineacionController();

        // crear torneo
        Map<String, Object> datosTorneo = new HashMap<>();
        datosTorneo.put("nombre", "Copa Primavera 2024");
        datosTorneo.put("fechaInicio", LocalDateTime.now().plusDays(15));
        datosTorneo.put("fechaFin", LocalDateTime.now().plusDays(45));
        datosTorneo.put("cantidadEquipos", 8);
        datosTorneo.put("costo", 3500.0);

        Torneo torneo = ctrlTorneo.crearTorneo(datosTorneo);
        System.out.println("torneo: " + torneo.getNombre() + " (id: " + torneo.getId() + ")");

        // equipos
        Map<String, Object> datosEq1 = new HashMap<>();
        datosEq1.put("nombre", "Dynamo FC");
        datosEq1.put("escudo", "dynamo.png");
        datosEq1.put("colorPrincipal", "Azul");
        datosEq1.put("colorSecundario", "Blanco");
        datosEq1.put("capitanId", 101);
        Equipo equipo1 = ctrlEquipo.crearEquipo(datosEq1);

        Map<String, Object> datosEq2 = new HashMap<>();
        datosEq2.put("nombre", "Golden Eagles");
        datosEq2.put("escudo", "eagles.png");
        datosEq2.put("colorPrincipal", "Dorado");
        datosEq2.put("colorSecundario", "Negro");
        datosEq2.put("capitanId", 102);
        Equipo equipo2 = ctrlEquipo.crearEquipo(datosEq2);

        Map<String, Object> datosEq3 = new HashMap<>();
        datosEq3.put("nombre", "United FC");
        datosEq3.put("escudo", "united.png");
        datosEq3.put("colorPrincipal", "Rojo");
        datosEq3.put("colorSecundario", "Blanco");
        datosEq3.put("capitanId", 103);
        Equipo equipo3 = ctrlEquipo.crearEquipo(datosEq3);

        System.out.println("equipos creados: " + ctrlEquipo.listarEquipos().size());

        // agregar jugadores al primer equipo
        for (int j = 1; j <= 18; j++) {
            ctrlEquipo.agregarJugador(equipo1.getId(), j);
        }

        // alineacion
        Map<String, Object> datosAlineacion = new HashMap<>();
        datosAlineacion.put("equipoId", equipo1.getId());
        datosAlineacion.put("partidoId", 1);
        datosAlineacion.put("formacion", "4-4-2");
        datosAlineacion.put("titulares", List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11));
        datosAlineacion.put("reservas", List.of(12, 13, 14, 15, 16, 17, 18));
        Alineacion alineacion = ctrlAlineacion.crearAlineacion(datosAlineacion);

        System.out.println("alineacion lista: " + alineacion.getFormacion().getValor());

        // iniciar torneo
        ctrlTorneo.iniciarTorneo(torneo.getId());
        System.out.println("estado: " + torneo.getEstado());

        System.out.println("\nlisto\n");
    }
}
