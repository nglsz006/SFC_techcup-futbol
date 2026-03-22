# Pruebas

El proyecto cuenta con una suite de pruebas unitarias e integración que cubre los principales módulos del sistema. Se usaron **JUnit 5**, **Mockito** y **Spring Boot Test** para las pruebas, y **JaCoCo** junto con **SonarQube** para medir la cobertura.

---

## Cobertura SonarQube

SonarQube es una herramienta de análisis estático de código que evalúa la calidad, seguridad y cobertura del proyecto. En el reporte se puede ver el porcentaje de cobertura de pruebas alcanzado, la cantidad de líneas analizadas, los code smells detectados y si hay vulnerabilidades o bugs reportados. Una buena cobertura en SonarQube indica que la mayor parte de la lógica de negocio está siendo validada por pruebas automatizadas.

<img width="530" height="680" alt="Cobertura SonarQube" src="https://github.com/user-attachments/assets/541b9e72-7420-4731-9a75-bf89c3868339" />

---

## Reporte JaCoCo

JaCoCo (Java Code Coverage) es la herramienta integrada en el proyecto para medir qué porcentaje del código fuente es ejecutado durante las pruebas. El reporte muestra la cobertura por paquete y por clase, indicando líneas cubiertas (verde), no cubiertas (rojo) y parcialmente cubiertas (amarillo). Se puede ver que los paquetes de `model`, `service`, `validator` y `controller` tienen alta cobertura, lo que significa que los casos de uso principales del sistema están siendo probados correctamente. El reporte se genera automáticamente al correr `mvn test`.

<img width="1917" height="871" alt="Reporte JaCoCo" src="https://github.com/user-attachments/assets/fa9b13af-e122-4bae-8144-e845e07acb14" />
