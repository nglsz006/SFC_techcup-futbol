package edu.dosw.project.SFC_TechUp_Futbol.service;

import edu.dosw.project.SFC_TechUp_Futbol.model.Organizador;
import edu.dosw.project.SFC_TechUp_Futbol.model.Torneo;
import edu.dosw.project.SFC_TechUp_Futbol.validators.UsuarioValidator;
import edu.dosw.project.SFC_TechUp_Futbol.validators.RegistroValidator;
import java.util.ArrayList;
import java.util.List;


@Service
public class OrganizadorService {

    private List<Organizador> organizadores = new ArrayList<>();
    private List<Torneo> torneos = new ArrayList<>();
    private UsuarioValidator usuarioValidator = new UsuarioValidator();
    private RegistroValidator registroValidator = new RegistroValidator();

    public void crearTorneo(Long organizadorId, Torneo torneo) {
        Organizador organizador = buscarOrganizadorPorId(organizadorId);

        if (organizador == null) {
            System.out.println("Organizador no encontrado");
            return;
        }

        if (!usuarioValidator.nombreValido(torneo.getName())) {
            System.out.println("El nombre del torneo no es válido");
            return;
        }

        torneo.setStatus("BORRADOR");
        torneos.add(torneo);
        organizador.setCurrentTournament(torneo);
        System.out.println("Torneo " + torneo.getName() + " creado correctamente");
    }

    public void configurarTorneo(Long organizadorId, String configuracion) {
        Organizador organizador = buscarOrganizadorPorId(organizadorId);

        if (organizador == null) {
            System.out.println("Organizador no encontrado");
            return;
        }

        if (organizador.getCurrentTournament() == null) {
            System.out.println("El organizador no tiene un torneo activo");
            return;
        }

        System.out.println("Torneo configurado: " + configuracion);
    }

    public void iniciarTorneo(Long organizadorId) {
        Organizador organizador = buscarOrganizadorPorId(organizadorId);

        if (organizador == null) {
            System.out.println("Organizador no encontrado");
            return;
        }

        if (organizador.getCurrentTournament() == null) {
            System.out.println("El organizador no tiene un torneo activo");
            return;
        }

        organizador.getCurrentTournament().setStatus("ACTIVO");
        System.out.println("Torneo iniciado correctamente");
    }

    public void cerrarInscripciones(Long organizadorId) {
        Organizador organizador = buscarOrganizadorPorId(organizadorId);

        if (organizador == null) {
            System.out.println("Organizador no encontrado");
            return;
        }

        if (organizador.getCurrentTournament() == null) {
            System.out.println("El organizador no tiene un torneo activo");
            return;
        }

        organizador.getCurrentTournament().setStatus("EN_PROGRESO");
        System.out.println("Inscripciones cerradas correctamente");
    }

    public void registrarResultado(Long organizadorId, Long partidoId, String resultado) {
        Organizador organizador = buscarOrganizadorPorId(organizadorId);

        if (organizador == null) {
            System.out.println("Organizador no encontrado");
            return;
        }

        if (resultado == null || resultado.trim().isEmpty()) {
            System.out.println("El resultado no puede estar vacío");
            return;
        }

        System.out.println("Resultado registrado para partido " + partidoId + ": " + resultado);
    }

    public void verificarPago(Long organizadorId, Long equipoId, boolean aprobado) {
        Organizador organizador = buscarOrganizadorPorId(organizadorId);

        if (organizador == null) {
            System.out.println("Organizador no encontrado");
            return;
        }

        if (equipoId == null) {
            System.out.println("El equipo no existe");
            return;
        }

        String estado = aprobado ? "APROBADO" : "RECHAZADO";
        System.out.println("Pago del equipo " + equipoId + ": " + estado);
    }

    public Organizador buscarOrganizadorPorId(Long id) {
        for (Organizador organizador : organizadores) {
            if (organizador.getId().equals(id)) {
                return organizador;
            }
        }
        return null;
    }

    public List<Torneo> getTorneos() {
        return torneos;
    }
}
