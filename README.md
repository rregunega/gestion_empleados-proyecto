# Gestión de Empleados y Proyectos

Aplicación Java para gestionar empleados, proyectos, tareas, asignaciones y costos dentro de un sistema de administración de obra o servicios.

## Descripción

Este proyecto implementa una solución orientada a objetos para administrar:

- Empleados de planta y contratados.
- Proyectos con domicilio, cliente y fechas.
- Tareas con duración, responsable y estado.
- Asignación y reasignación de empleados.
- Control de retrasos y cálculo de costos.
- Interfaz gráfica con Swing para la gestión del flujo principal.

La lógica principal está contenida en el paquete `entidades`, mientras que la interfaz de usuario se encuentra en `gui`.

## Funcionalidades principales

- Registrar empleados con salario o valor hora.
- Registrar empleados permanentes con categoría.
- Crear proyectos y asociar tareas.
- Asignar responsables a tareas.
- Reasignar empleados según disponibilidad o menos retraso.
- Registrar retrasos por tarea.
- Finalizar tareas y proyectos.
- Consultar empleados asignados, tareas sin asignar y proyectos activos/pendientes.
- Calcular costos del proyecto con y sin retrasos.

## Tecnologías

- Java
- Maven
- Swing
- JUnit 4

## Estructura del proyecto

```text
gestion_empleados-proyecto/
├── .idea/
├── src/
│   ├── main/
│   │   └── java/
│   │       ├── entidades/
│   │       │   ├── Categoria.java
│   │       │   ├── Cliente.java
│   │       │   ├── Contratado.java
│   │       │   ├── Empleado.java
│   │       │   ├── Estado.java
│   │       │   ├── HomeSolution.java
│   │       │   ├── IHomeSolution.java
│   │       │   ├── Permanente.java
│   │       │   ├── Proyecto.java
│   │       │   ├── Tarea.java
│   │       │   └── Tupla.java
│   │       ├── gui/
│   │       │   ├── FormularioEmpleado.java
│   │       │   ├── FormularioProyecto.java
│   │       │   ├── FormularioProyectoFinalizado.java
│   │       │   ├── FormularioTarea.java
│   │       │   ├── GestionEmpleados.java
│   │       │   ├── GestionProyectos.java
│   │       │   ├── InformacionProyecto.java
│   │       │   ├── ListaProyectos.java
│   │       │   ├── PanelManager.java
│   │       │   └── PanelPrincipal.java
│   │       └── Main.java
│   └── test/
│       └── java/
│           └── HomeSolutionTest.java
├── .gitignore
├── pom.xml
└── README.md
```

## Requisitos

- Java 25 (según la configuración de `pom.xml`)
- Maven 3.8 o superior

## Instalación

1. Clona el repositorio:

```bash
git clone https://github.com/rregunega/gestion_empleados-proyecto.git
cd gestion_empleados-proyecto
```

2. Compila el proyecto:

```bash
mvn clean compile
```

## Ejecución

La aplicación principal se inicia desde la clase `Main`.

Opción recomendada desde IDE:

- Abre el proyecto en IntelliJ IDEA o Eclipse.
- Ejecuta la clase `src/main/java/Main.java`.

Opción desde Maven:

```bash
mvn exec:java -Dexec.mainClass=Main
```

> Si tu entorno no reconoce el plugin `exec`, puedes ejecutar la clase `Main` directamente desde tu IDE o compilar y lanzar la clase con el comando equivalente de tu entorno.

## Pruebas

Para ejecutar la suite de pruebas:

```bash
mvn test
```

## Licencia

Este proyecto no especifica una licencia en el archivo `pom.xml` ni en el repositorio, por lo que se considera sin licencia declarada.

## Nota

El proyecto incluye una interfaz gráfica Swing y una lógica de negocio centralizada en `HomeSolution`, por lo que es apropiado para practicar conceptos de:

- programación orientada a objetos,
- colecciones,
- validación de datos,
- gestión de estados,
- relaciones entre clases,
- pruebas unitarias con JUnit.

Si quieres, también puedo dejarte una versión del README con un enfoque más profesional, más corto o en inglés.
