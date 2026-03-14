package edu.dosw.project.SFC_TechUp_Futbol.core.validator;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Formacion;
import java.util.List;
import java.util.Map;

public class ValidacionAlineacion implements Validacion {
    @Override
    public void validar(Map<String, Object> datos) {
        String formacionStr = (String) datos.get("formacion");
        Formacion formacion = Formacion.fromString(formacionStr);
        
        if (formacion == null) {
            throw new IllegalArgumentException("Formacion invalida. Use: 4-4-2, 4-3-3, 3-5-2, 4-5-1 o 5-3-2");
        }

        List<Integer> titulares = (List<Integer>) datos.get("titulares");
        if (titulares == null || titulares.size() != 11) {
            throw new IllegalArgumentException("Debe haber exactamente 11 titulares");
        }

        List<Integer> reservas = (List<Integer>) datos.get("reservas");
        if (reservas != null && reservas.size() > 7) {
            throw new IllegalArgumentException("No puede haber mas de 7 reservas");
        }
    }
}

