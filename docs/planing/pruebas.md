# Pruebas

El proyecto tiene 639 pruebas automatizadas y todas pasan. Se usaron JUnit 5, Mockito y Spring Boot Test para escribirlas, y dos herramientas para medir qué tanto del código está siendo probado: JaCoCo y SonarCloud.

---

## Cobertura JaCoCo

JaCoCo mide qué partes del código se ejecutan cuando corren las pruebas. Los resultados actuales del proyecto son:

| Métrica | Cubierto | Total | Cobertura |
|---|---|---|---|
| Líneas | 1964 | 2188 | 90% |
| Ramas | 509 | 745 | 68% |
| Métodos | 744 | 794 | 94% |

Lo que quedó sin cubrir al 100% son cosas puntuales: algunas ramas de manejo de errores y flujos alternativos en lógica de negocio compleja (llaves eliminatorias, avance de fases), y el arranque de la aplicación (`SFCTechCupFutbolApiApplication`) que es difícil de probar directamente. Las capas de persistencia están excluidas del análisis por configuración.

<img width="1917" height="871" alt="Reporte JaCoCo" src="https://github.com/user-attachments/assets/fa9b13af-e122-4bae-8144-e845e07acb14" />

---

## Cobertura SonarCloud

SonarCloud es una herramienta que analiza el código en busca de problemas de calidad, posibles bugs, código duplicado y vulnerabilidades de seguridad. Además muestra la cobertura de pruebas de una forma más visual y con más detalle que JaCoCo. En el reporte se puede ver el porcentaje de cobertura alcanzado, cuántas líneas fueron analizadas y si hay deuda técnica o problemas que atender. Un resultado limpio en SonarQube indica que el código no solo funciona sino que está bien escrito.

<img width="530" height="680" alt="Cobertura SonarQube" src="https://github.com/user-attachments/assets/541b9e72-7420-4731-9a75-bf89c3868339" />
