package edu.dosw.project.SFC_TechUp_Futbol.core.util;

import java.util.concurrent.atomic.AtomicInteger;

public class IdGeneratorUtil {
    
    private static final AtomicInteger torneoCounter = new AtomicInteger(1);
    private static final AtomicInteger equipoCounter = new AtomicInteger(1);
    private static final AtomicInteger jugadorCounter = new AtomicInteger(1);
    private static final AtomicInteger partidoCounter = new AtomicInteger(1);
    
    public static int generarIdTorneo() {
        return torneoCounter.getAndIncrement();
    }
    
    public static int generarIdEquipo() {
        return equipoCounter.getAndIncrement();
    }
    
    public static int generarIdJugador() {
        return jugadorCounter.getAndIncrement();
    }
    
    public static int generarIdPartido() {
        return partidoCounter.getAndIncrement();
    }
}

