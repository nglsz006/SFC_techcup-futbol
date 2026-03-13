package edu.dosw.project.SFC_TechUp_Futbol.model;

public enum Formacion {
    F_4_4_2("4-4-2"),
    F_4_3_3("4-3-3"),
    F_3_5_2("3-5-2"),
    F_4_5_1("4-5-1"),
    F_5_3_2("5-3-2");

    private String valor;

    Formacion(String valor) {
        this.valor = valor;
    }

    public String getValor() {
        return valor;
    }

    public static Formacion fromString(String texto) {
        for (Formacion f : Formacion.values()) {
            if (f.valor.equals(texto)) {
                return f;
            }
        }
        return null;
    }
}
