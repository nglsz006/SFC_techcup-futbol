package edu.dosw.project.SFC_TechUp_Futbol;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.Jugador;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.PerfilDeportivo;
import edu.dosw.project.SFC_TechUp_Futbol.core.validator.PerfilDeportivoValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DisplayName("PerfilDeportivo - pruebas unitarias")
class PerfilDeportivoTest {

    private PerfilDeportivoValidator validator;

    @BeforeEach
    void setUp() {
        validator = new PerfilDeportivoValidator();
    }

    @Nested
    @DisplayName("Creacion de perfil valido")
    class CreacionPerfil {

        @Test
        @DisplayName("Crea perfil con todos los atributos correctamente")
        void creacion_perfilCompleto_atributosCorrectos() {
            PerfilDeportivo perfil = new PerfilDeportivo(
                    "uuid-perfil-1", "uuid-jugador-10",
                    List.of(Jugador.Posicion.PORTERO, Jugador.Posicion.DEFENSA),
                    7, "foto.jpg", 22,
                    PerfilDeportivo.Genero.MASCULINO,
                    "1234567890", 5
            );

            assertThat(perfil.getId()).isEqualTo("uuid-perfil-1");
            assertThat(perfil.getJugadorId()).isEqualTo("uuid-jugador-10");
            assertThat(perfil.getPosiciones()).containsExactly(Jugador.Posicion.PORTERO, Jugador.Posicion.DEFENSA);
            assertThat(perfil.getDorsal()).isEqualTo(7);
            assertThat(perfil.getFoto()).isEqualTo("foto.jpg");
            assertThat(perfil.getEdad()).isEqualTo(22);
            assertThat(perfil.getGenero()).isEqualTo(PerfilDeportivo.Genero.MASCULINO);
            assertThat(perfil.getIdentificacion()).isEqualTo("1234567890");
            assertThat(perfil.getSemestre()).isEqualTo(5);
        }

        @Test
        @DisplayName("Crea perfil sin foto y sin semestre — ambos opcionales")
        void creacion_sinFotoNiSemestre_esValido() {
            PerfilDeportivo perfil = new PerfilDeportivo(
                    "uuid-perfil-1", "uuid-jugador-10",
                    List.of(Jugador.Posicion.DELANTERO),
                    9, null, 30,
                    PerfilDeportivo.Genero.FEMENINO,
                    "9876543210", null
            );

            assertThat(perfil.getFoto()).isNull();
            assertThat(perfil.getSemestre()).isNull();
        }

        @Test
        @DisplayName("Genero OTRO es un valor valido")
        void creacion_generoOtro_esValido() {
            PerfilDeportivo perfil = new PerfilDeportivo();
            perfil.setGenero(PerfilDeportivo.Genero.OTRO);
            assertThat(perfil.getGenero()).isEqualTo(PerfilDeportivo.Genero.OTRO);
        }
    }

    @Nested
    @DisplayName("Validacion de posiciones")
    class ValidacionPosiciones {

        @Test
        @DisplayName("Posicion valida no lanza excepcion")
        void posiciones_listaConUnaPos_noLanzaExcepcion() {
            assertThatNoException()
                    .isThrownBy(() -> validator.validarPosiciones(List.of(Jugador.Posicion.VOLANTE)));
        }

        @Test
        @DisplayName("Lista nula lanza excepcion")
        void posiciones_listaNula_lanzaExcepcion() {
            assertThatThrownBy(() -> validator.validarPosiciones(null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("posicion");
        }

        @Test
        @DisplayName("Lista vacia lanza excepcion")
        void posiciones_listaVacia_lanzaExcepcion() {
            assertThatThrownBy(() -> validator.validarPosiciones(List.of()))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("posicion");
        }
    }

    @Nested
    @DisplayName("Validacion de dorsal")
    class ValidacionDorsal {

        @Test
        @DisplayName("Dorsal positivo no lanza excepcion")
        void dorsal_positivo_noLanzaExcepcion() {
            assertThatNoException()
                    .isThrownBy(() -> validator.validarDorsal(10));
        }

        @Test
        @DisplayName("Dorsal cero lanza excepcion")
        void dorsal_cero_lanzaExcepcion() {
            assertThatThrownBy(() -> validator.validarDorsal(0))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("positivo");
        }

        @Test
        @DisplayName("Dorsal negativo lanza excepcion")
        void dorsal_negativo_lanzaExcepcion() {
            assertThatThrownBy(() -> validator.validarDorsal(-1))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("positivo");
        }
    }
    @Nested
    @DisplayName("Validacion de edad")
    class ValidacionEdad {

        @Test
        @DisplayName("Edad valida no lanza excepcion")
        void edad_valida_noLanzaExcepcion() {
            assertThatNoException()
                    .isThrownBy(() -> validator.validarEdad(22));
        }

        @Test
        @DisplayName("Edad cero lanza excepcion")
        void edad_cero_lanzaExcepcion() {
            assertThatThrownBy(() -> validator.validarEdad(0))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("mayor a 0");
        }

        @Test
        @DisplayName("Edad negativa lanza excepcion")
        void edad_negativa_lanzaExcepcion() {
            assertThatThrownBy(() -> validator.validarEdad(-5))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("mayor a 0");
        }

        @Test
        @DisplayName("Edad fuera de rango lanza excepcion")
        void edad_fueraDeRango_lanzaExcepcion() {
            assertThatThrownBy(() -> validator.validarEdad(101))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("valida");
        }
    }

    @Nested
    @DisplayName("Validacion de genero")
    class ValidacionGenero {

        @Test
        @DisplayName("Genero MASCULINO es valido")
        void genero_masculino_noLanzaExcepcion() {
            assertThatNoException()
                    .isThrownBy(() -> validator.validarGenero(PerfilDeportivo.Genero.MASCULINO));
        }

        @Test
        @DisplayName("Genero FEMENINO es valido")
        void genero_femenino_noLanzaExcepcion() {
            assertThatNoException()
                    .isThrownBy(() -> validator.validarGenero(PerfilDeportivo.Genero.FEMENINO));
        }

        @Test
        @DisplayName("Genero OTRO es valido")
        void genero_otro_noLanzaExcepcion() {
            assertThatNoException()
                    .isThrownBy(() -> validator.validarGenero(PerfilDeportivo.Genero.OTRO));
        }

        @Test
        @DisplayName("Genero nulo lanza excepcion")
        void genero_nulo_lanzaExcepcion() {
            assertThatThrownBy(() -> validator.validarGenero(null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("genero");
        }
    }

    @Nested
    @DisplayName("Validacion de semestre")
    class ValidacionSemestre {

        @Test
        @DisplayName("Semestre nulo no lanza excepcion — es opcional")
        void semestre_nulo_noLanzaExcepcion() {
            assertThatNoException()
                    .isThrownBy(() -> validator.validarSemestre(null));
        }

        @Test
        @DisplayName("Semestre positivo no lanza excepcion")
        void semestre_positivo_noLanzaExcepcion() {
            assertThatNoException()
                    .isThrownBy(() -> validator.validarSemestre(4));
        }

        @Test
        @DisplayName("Semestre cero lanza excepcion")
        void semestre_cero_lanzaExcepcion() {
            assertThatThrownBy(() -> validator.validarSemestre(0))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("positivo");
        }
    }

    @Nested
    @DisplayName("Flujo alterno: datos invalidos")
    class FlujosAlternos {

        @Test
        @DisplayName("Perfil invalido — falla si faltan campos obligatorios")
        void perfilInvalido_camposObligatoriosFaltantes_lanzaExcepciones() {
            assertThatThrownBy(() -> validator.validarPerfil(
                    null, 0, 0, null, null))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("Perfil valido completo no lanza excepcion")
        void perfilValido_todosLosCampos_noLanzaExcepcion() {
            assertThatNoException().isThrownBy(() ->
                    validator.validarPerfil(
                            List.of(Jugador.Posicion.DELANTERO),
                            10, 25,
                            PerfilDeportivo.Genero.MASCULINO,
                            "1234567890"
                    ));
        }
    }
}
