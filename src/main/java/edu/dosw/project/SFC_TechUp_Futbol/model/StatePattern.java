
package edu.dosw.project.SFC_TechUp_Futbol.model;

public class StatePattern {

    public static class EstadoTorneo {
        public EstadoTorneo iniciar(Torneo torneo) {
            return this;
        }

        public EstadoTorneo finalizar(Torneo torneo) {
            return this;
        }

        public boolean puedeInscribirEquipos() {
            return false;
        }
    }

    public static class TorneoCreado extends EstadoTorneo {
        @Override
        public EstadoTorneo iniciar(Torneo torneo) {
            torneo.setEstado(models.EstadoTorneo.EN_CURSO);
            return new TorneoEnCurso();
        }

        @Override
        public boolean puedeInscribirEquipos() {
            return true;
        }
    }

    public static class TorneoEnCurso extends EstadoTorneo {
        @Override
        public EstadoTorneo finalizar(Torneo torneo) {
            torneo.setEstado(models.EstadoTorneo.FINALIZADO);
            return new TorneoFinalizado();
        }
    }

    public static class TorneoFinalizado extends EstadoTorneo {
    }
}
