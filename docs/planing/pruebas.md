# Pruebas

El proyecto tiene 234 pruebas automatizadas y todas pasan. Se usaron JUnit 5, Mockito y Spring Boot Test para escribirlas, y dos herramientas para medir qué tanto del código está siendo probado: JaCoCo y SonarQube.

---

## Cobertura JaCoCo

JaCoCo mide qué partes del código se ejecutan cuando corren las pruebas. El resultado general del proyecto es **87.2% de cobertura** (4624 de 5304 instrucciones cubiertas), lo que es un número muy bueno y significa que casi todo el código tiene prueba que lo respalde.

Así quedó la cobertura por módulo:

| Módulo | Cobertura |
|---|---|
| Estados (State pattern) | ~98% — prácticamente todo cubierto |
| Excepciones | 100% — todos los errores están probados |
| Servicios | ~93% — la lógica de negocio bien cubierta |
| Validadores | ~95% — las reglas de negocio bien probadas |
| Repositorios | ~92% — el acceso a datos bien cubierto |
| Controladores | ~85% — los endpoints principales probados |
| Modelos | ~84% — las entidades del dominio probadas |
| Utilidades | ~88% — mapper y cifrado de contraseñas probados |

Lo que quedó sin cubrir al 100% son cosas puntuales: algunos getters/setters que no se usan en los tests, la clase `Subject` que tiene lógica de observer poco ejercitada, y el arranque de la aplicación (`SFCTechCupFutbolApiApplication`) que es difícil de probar directamente.

<img width="1917" height="871" alt="Reporte JaCoCo" src="https://github.com/user-attachments/assets/fa9b13af-e122-4bae-8144-e845e07acb14" />

---

## Cobertura SonarQube

SonarQube es una herramienta que analiza el código en busca de problemas de calidad, posibles bugs, código duplicado y vulnerabilidades de seguridad. Además muestra la cobertura de pruebas de una forma más visual y con más detalle que JaCoCo. En el reporte se puede ver el porcentaje de cobertura alcanzado, cuántas líneas fueron analizadas y si hay deuda técnica o problemas que atender. Un resultado limpio en SonarQube indica que el código no solo funciona sino que está bien escrito.

<img width="530" height="680" alt="Cobertura SonarQube" src="https://github.com/user-attachments/assets/541b9e72-7420-4731-9a75-bf89c3868339" />
