package edu.dosw.project.SFC_TechUp_Futbol.core.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Subject {
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

