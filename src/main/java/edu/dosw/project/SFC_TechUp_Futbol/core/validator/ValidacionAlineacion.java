package edu.dosw.project.SFC_TechUp_Futbol.core.validator;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Alineacion;
import java.util.List;
import java.util.Map;

public class ValidacionAlineacion implements Validacion {
    @Override
    public void validar(Map<String, Object> datos) {
        String formacionStr = (String) datos.get("formacion");
        Alineacion.Formacion formacion = Alineacion.Formacion.fromString(formacionStr);

        if (formacion == null) {
            throw new IllegalArgumentException("Formacion invalida. Use: 4-4-2, 4-3-3, 3-5-2, 4-5-1 o 5-3-2");
        }

        Object titularesObj = datos.get("titulares");
        if (!(titularesObj instanceof List<?> titulares) || titulares.size() != 11) {
            throw new IllegalArgumentException("Debe haber exactamente 11 titulares");
        }

        Object reservasObj = datos.get("reservas");
        if (reservasObj instanceof List<?> reservas && reservas.size() > 7) {
            throw new IllegalArgumentException("No puede haber mas de 7 reservas");
        }
    }
}
