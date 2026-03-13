
package edu.dosw.project.SFC_TechUp_Futbol.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ObserverPattern {

    public static class Observer {
        public void actualizar(String evento, Map<String, Object> datos) {
        }
    }

    public static class Subject {
        private List<Observer> observers;

        public Subject() {
            this.observers = new ArrayList<>();
        }

        public void agregarObserver(Observer observer) {
            this.observers.add(observer);
        }

        public void removerObserver(Observer observer) {
            this.observers.remove(observer);
        }

        public void notificar(String evento, Map<String, Object> datos) {
            for (Observer observer : observers) {
                observer.actualizar(evento, datos);
            }
        }
    }

    public static class NotificadorTorneo extends Observer {
        @Override
        public void actualizar(String evento, Map<String, Object> datos) {
            System.out.println("[NOTIFICACIÓN] " + evento + ": " + datos);
        }
    }

    public static class LoggerObserver extends Observer {
        private List<Map<String, Object>> logs;

        public LoggerObserver() {
            this.logs = new ArrayList<>();
        }

        @Override
        public void actualizar(String evento, Map<String, Object> datos) {
            Map<String, Object> log = new HashMap<>();
            log.put("evento", evento);
            log.put("datos", datos);
            this.logs.add(log);
        }

        public List<Map<String, Object>> getLogs() {
            return logs;
        }
    }
}
