# 🏋️ Sistema de Gestión de Gimnasio

**Proyecto del Taller de Panel Administrativo**
Tecnología de Desarrollo de Sistemas Informáticos

📅 I Semestre 2026
👨‍🏫 Profesor: Mag. Carlos Adolfo Beltrán Castro
👨‍💻 Estudiantes:
- Brayan Sneyder Arias Bueno - 1096538470
- Jessica Lorena Arias Bueno - 1102636000
- Santiago Cuesta Naranjo  - 1096800383

---

![Pantalla Inicial]( ![alt text](capturas/login.png) )


---

## Descripción del Proyecto

Este proyecto consiste en un sistema de gestión de gimnasio desarrollado en **Java SE con interfaz gráfica (SWING)**, aplicando los principios de la **Programación Orientada a Objetos (POO)**. Permite administrar de manera eficiente la información de clientes, entrenadores, rutinas y equipos, facilitando el control de actividades, el seguimiento del progreso de los usuarios y la optimización de los recursos del gimnasio.

Muchos gimnasios pequeños aún manejan su información de forma manual, lo que genera desorden y pérdida de datos. Este sistema busca resolver esa problemática aplicando herramientas modernas de programación y buenas prácticas de diseño de software.

---

## Objetivos

### Objetivo Principal
Desarrollar un sistema de gestión de gimnasio basado en POO que permita administrar de manera eficiente clientes, entrenadores, rutinas y equipos.

### Objetivos Específicos
- Diseñar e implementar clases aplicando herencia, encapsulamiento y polimorfismo.
- Desarrollar funcionalidades para el registro, consulta y gestión de clientes y entrenadores.
- Crear un módulo de asignación y visualización de rutinas personalizadas.
- Implementar control básico del uso y disponibilidad de máquinas del gimnasio.
- Permitir el seguimiento del progreso físico de los clientes.
- Generar reportes simples sobre la información almacenada.

---
## diagrama de la base de datos 

![diagrama base de datos](![alt text](capturas/GymAPP DIAGRAMA.png))


## Estructura del Proyecto

```
GimnasioApp/
├── nbproject/
│   ├── Main.java 
├── src/
│   └── gimnasio/
│       ├── Main.java                          ← Punto de entrada
│       │
│       ├── modelo/                            ← Clases de datos (POO)
│       │   ├── Persona.java
│       │   ├── Cliente.java
│       │   ├── Entrenador.java
│       │   ├── Rutina.java
│       │   ├── Maquina.java
│       │   └── ProgresoFisico.java
│       │
│       ├── vista/                             ← Interfaz gráfica (SWING)
│       │   ├── MenuPrincipal.java
│       │   ├── VistaClientes.java
│       │   ├── VistaEntrenadores.java
│       │   ├── VistaRutinas.java
│       │   ├── VistaMaquinas.java
│       │   └── VistaProgreso.java
│       │
│       ├── controlador/                       ← Intermediario vista ↔ modelo
│       │   ├── ClienteControlador.java
│       │   ├── EntrenadorControlador.java
│       │   ├── RutinaControlador.java
│       │   └── MaquinaControlador.java
│       │
│       └── dao/                               ← Comunicación con la BD
│           ├── Conexion.java                  (abre/cierra la conexión)
│           ├── ClienteDAO.java
│           ├── EntrenadorDAO.java
│           ├── RutinaDAO.java
│           └── MaquinaDAO.java
│
├── lib/
│   └── mysql-connector.jar                    ← Driver JDBC
├── database/
│   └── gimnasio.sql                           ← Script para crear las tablas
├── assets/
│   └── screenshot-menu.png
├── README.md
└── GimnasioApp.jar
```

**Lista de Menú de Opciones** con logo e ítems de navegación:
- 👤 Clientes
- 🏃 Entrenadores
- 📋 Rutinas
- 🏋️ Máquinas
- 📊 Reportes
- 🚪 Salir

**Vistas - CRUD** con tablas de gestión en:
- Clientes (registrar, consultar, actualizar, eliminar)
- Entrenadores (registrar, gestionar, asignar)
- Rutinas (crear, asignar, consultar)
- Máquinas (registrar, control de disponibilidad)
- Progreso Físico (registrar peso, medidas)

**Salir** con mensaje informativo de cierre de sesión.

---

## Requerimientos Funcionales

| Módulo | Funcionalidad |
|--------|--------------|
| Clientes | Registrar, consultar, actualizar y eliminar |
| Entrenadores | Registrar, gestionar y asignar a clientes |
| Rutinas | Crear, asignar y consultar rutinas personalizadas |
| Máquinas | Registrar y controlar disponibilidad (ocupada/libre) |
| Progreso Físico | Registrar y consultar peso, medidas, etc. |
| Reportes | Generar reportes de clientes, rutinas y equipos |

---

## Requerimientos No Funcionales

- **Usabilidad:** Interfaz sencilla e intuitiva.
- **Rendimiento:** Respuestas rápidas en todas las operaciones.
- **Seguridad:** Autenticación mediante usuario y contraseña.
- **Disponibilidad:** Funcionamiento estable y continuo.
- **Mantenibilidad:** Código organizado con principios POO.
- **Escalabilidad:** Estructura que permite futuras mejoras.
- **Portabilidad:** Compatible con diferentes sistemas operativos (Windows, Linux, Mac).

---

## Tecnologías Usadas

| Tecnología | Uso |
|------------|-----|
| Java SE | Lenguaje principal de desarrollo |
| Java SWING | Interfaz gráfica de usuario (GUI) |
| Apache NetBeans | IDE principal de desarrollo |
| Visual Studio (opcional) | Apoyo para interfaces adicionales |
| GitHub | Control de versiones y trabajo colaborativo |

---

## 🔧 Instalación y Ejecución

### Requisitos Previos
- Java JDK 11 o superior instalado
- Apache NetBeans IDE (recomendado)
- Git instalado

### Pasos de Instalación

1. **Clonar el repositorio:**
```bash
git clone https://github.com/brayanarias-af/GimnasioApp.git
cd GimnasioApp
```

2. **Abrir en NetBeans:**
   - Ir a `File > Open Project`
   - Seleccionar la carpeta del proyecto

3. **Compilar y ejecutar:**
   - Presionar `F6` o clic en `Run Project`
   - También puede ejecutarse desde la terminal:

```bash
javac -d bin src/gimnasio/**/*.java
java -cp bin gimnasio.Main
```

4. **Ejecutar el JAR (si está disponible):**
```bash
java -jar GimnasioApp.jar
```

### Credenciales de Prueba
```
Usuario: admin
Contraseña: gimnasio123
```

---

## Integrantes del Grupo

| Nombre | Cédula | Rol |
|--------|--------|-----|
| Brayan Sneyder Arias Bueno | 1096538470 | Desarrollador |
| Jessica Lorena Arias Bueno | 1102636000  | Desarrollador |
| Santiago Cuesta Naranjo | 1096800383| Desarrollador |

---

## Licencia

Proyecto académico desarrollado para la asignatura de Tecnología de Desarrollo de Sistemas Informáticos — I Semestre 2026.
