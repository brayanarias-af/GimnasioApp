<<<<<<< HEAD
# 🏋️ Sistema de Gestión de Gimnasio — GimnasioApp

**Proyecto del Taller de Panel Administrativo**  
Tecnología de Desarrollo de Sistemas Informáticos — I Semestre 2026

👨‍🏫 **Profesor:** Mag. Carlos Adolfo Beltrán Castro  
👨‍💻 **Estudiantes:**
- Brayan Sneyder Arias Bueno — 1096538470
- Jessica Lorena Arias Bueno — 1102636000
- Santiago Cuesta Naranjo — 1096800383

---

## 📌 Descripción del Proyecto

**GimnasioApp** es una aplicación de escritorio desarrollada en **Java SE con interfaz gráfica SWING**, diseñada para digitalizar y optimizar la administración de un gimnasio pequeño o mediano. Aplica los principios fundamentales de la **Programación Orientada a Objetos (POO)** y sigue el patrón de arquitectura **MVC (Modelo-Vista-Controlador)**.

El problema que resuelve es concreto: muchos gimnasios gestionan su información de forma manual (cuadernos, hojas de cálculo, papel), lo cual genera desorden, pérdida de datos y dificultades para hacer seguimiento a los clientes. GimnasioApp centraliza toda esa información en una base de datos local SQLite, ofreciendo una interfaz visual intuitiva con tema oscuro y control de acceso por roles.

La aplicación permite:
- Gestionar clientes, entrenadores, rutinas, máquinas, pagos y membresías.
- Controlar asistencia y hacer seguimiento del progreso físico de los usuarios.
- Administrar permisos por rol: Administrador, Entrenador, Recepcionista y Cliente.
- Autenticación segura mediante usuario y contraseña.
- Navegación lateral con íconos cargados desde archivos PNG.

---

## 🖼️ Capturas del Sistema

### Vista Principal (Dashboard)

![Vista Principal](capturas/VISTA%20PRINCIPAL.png)

> Panel de inicio con acceso rápido a los módulos del sistema, navegación lateral con íconos y tarjetas de resumen para el administrador.

---

### Diagrama de Base de Datos

![Diagrama Base de Datos](capturas/DIAGRAMA%20BASE%20DE%20DATOS.png)

> Modelo relacional de la base de datos SQLite. Muestra las tablas principales y sus relaciones: `usuarios`, `clientes`, `membresias`, `pagos`, `asistencias`, `rutinas`, `ejercicios`, `maquinas` y `progreso`.

---

## 🎯 Objetivos

### Objetivo Principal
Desarrollar un sistema de gestión de gimnasio basado en POO que permita administrar de manera eficiente clientes, entrenadores, rutinas y equipos, con acceso controlado por roles.

### Objetivos Específicos
- Diseñar e implementar clases aplicando herencia, encapsulamiento y polimorfismo.
- Desarrollar funcionalidades CRUD para clientes, entrenadores, máquinas y rutinas.
- Implementar un sistema de autenticación con roles diferenciados.
- Crear un módulo de asignación y visualización de rutinas personalizadas.
- Controlar el uso y disponibilidad de máquinas del gimnasio.
- Registrar asistencia diaria y progreso físico de los clientes.
- Gestionar pagos, membresías e ingresos del gimnasio.

---

## 🗂️ Estructura del Proyecto

```
GimnasioApp/
├── src/Gimnasio/
│   ├── BaseDatos/                        ← Scripts SQL del proyecto
│   │   ├── CodigoTablas.sql              ← Creación de todas las tablas
│   │   └── InsercionDatos.sql            ← Datos iniciales de prueba
│   ├── Conexion/
│   │   └── Conexion.java                 ← Conexión a SQLite
│   ├── Controlador/                      ← Lógica de acceso a datos (DAOs)
│   │   ├── Sesion.java                   ← Control de sesión activa
│   │   ├── ClienteDAO.java               ← CRUD de clientes
│   │   ├── AsistenciaDAO.java            ← Registro de asistencias
│   │   ├── EjercicioDAO.java             ← Gestión de ejercicios
│   │   ├── MaquinaDAO.java               ← CRUD de máquinas
│   │   ├── MembresiaDAO.java             ← Gestión de membresías
│   │   ├── PagoDAO.java                  ← Registro y consulta de pagos
│   │   ├── PermisoDAO.java               ← Control de permisos por rol
│   │   ├── ProgresoDAO.java              ← Progreso físico de clientes
│   │   └── RutinaDAO.java                ← CRUD de rutinas
│   ├── Iconos/                           ← Íconos PNG de la navegación
│   │   ├── inicio.png
│   │   ├── clientes.png
│   │   ├── asistencias.png
│   │   ├── membresias.png
│   │   ├── pago.png
│   │   ├── rutina.png
│   │   ├── maquina.png
│   │   ├── permisos-de-usuario.png
│   │   └── ... (otros íconos)
│   ├── Vistas/                           ← Interfaz gráfica (SWING)
│   │   ├── LoginVista.java               ← Pantalla de inicio de sesión
│   │   ├── DashboardVista.java           ← Panel principal con resumen
│   │   ├── PanelAdminVista.java          ← Contenedor del panel admin
│   │   ├── ClientesVista.java            ← Gestión de clientes
│   │   ├── FormularioClienteVista.java   ← Formulario crear/editar cliente
│   │   ├── MaquinasVista.java            ← Control de máquinas
│   │   ├── RutinasVista.java             ← Asignación de rutinas
│   │   ├── NavLateral.java               ← Barra de navegación lateral
│   │   └── EstilosGym.java               ← Estilos visuales globales (tema oscuro)
│   └── Main/
│       └── Main.java                     ← Punto de entrada de la aplicación
├── capturas/
│   ├── VISTA PRINCIPAL.png               ← Captura del dashboard
│   └── DIAGRAMA BASE DE DATOS.png        ← Diagrama del modelo relacional
├── lib/
│   └── sqlite-jdbc.jar                   ← Driver de base de datos SQLite
└── README.md
```

> 📂 **Importante:** Los scripts SQL se encuentran en la carpeta `src/Gimnasio/BaseDatos/`. El archivo `CodigoTablas.sql` contiene la definición de todas las tablas y el archivo `InsercionDatos.sql` contiene los registros de ejemplo para pruebas. La base de datos `GymAPP.db` se genera automáticamente al ejecutar la aplicación.

---

## ⚙️ Funcionalidades por Módulo

| Módulo | Funcionalidades |
|--------|----------------|
| **Autenticación** | Login con usuario y contraseña, control de sesión activa, roles diferenciados |
| **Clientes** | Registrar, consultar, actualizar y eliminar clientes |
| **Asistencias** | Registrar entrada/salida diaria de clientes |
| **Membresías** | Gestión de tipos de membresía y asignación a clientes |
| **Pagos** | Registro de pagos, historial e ingresos |
| **Rutinas** | Crear y asignar rutinas personalizadas con ejercicios |
| **Máquinas** | Registrar equipos y controlar su disponibilidad |
| **Progreso** | Registrar y consultar peso, medidas y avance físico |
| **Permisos** | Administrar accesos por rol (Admin, Entrenador, Recepcionista, Cliente) |

---

## 🔐 Roles del Sistema

| Rol | Acceso |
|-----|--------|
| **Administrador** | Acceso total a todos los módulos |
| **Entrenador** | Clientes, rutinas, ejercicios, progreso |
| **Recepcionista** | Clientes, asistencias, pagos, membresías |
| **Cliente** | Consulta de su propia información y rutinas |

---

## 📋 Requerimientos No Funcionales

- **Usabilidad:** Interfaz oscura, moderna e intuitiva con íconos visuales.
- **Rendimiento:** Respuestas rápidas gracias al uso de SQLite local.
- **Seguridad:** Autenticación por credenciales y control de acceso por rol.
- **Mantenibilidad:** Código organizado con patrón MVC y principios POO.
- **Escalabilidad:** Arquitectura modular que permite agregar nuevas vistas o DAOs.
- **Portabilidad:** Compatible con Windows, Linux y macOS (requiere JDK 11+).

---

## 🛠️ Tecnologías Utilizadas

| Tecnología | Uso |
|------------|-----|
| **Java SE 11+** | Lenguaje principal de desarrollo |
| **Java SWING** | Interfaz gráfica de usuario (GUI) |
| **SQLite** | Base de datos local embebida |
| **sqlite-jdbc** | Driver JDBC para conectar Java con SQLite |
| **Apache NetBeans** | IDE principal de desarrollo |
| **Git / GitHub** | Control de versiones y trabajo colaborativo |

---

## 🚀 Instalación y Ejecución

### Requisitos Previos
- Java JDK 11 o superior
- Apache NetBeans IDE (recomendado)
- Git instalado

### Pasos

**1. Clonar el repositorio:**
=======
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

![diagrama base de datos](![alt text](capturas/GymAPPDIAGRAMA.png))


## Estructura del Proyecto

```
 src/Gimnasio/
  ├── Conexion/
  │   └── Conexion.java         
  ├── Controlador/
  │   ├── Sesion.java            
  │   ├── ClienteDAO.java        
  │   ├── MaquinaDAO.java        
  │   └── RutinaDAO.java        
  ├── Modelo/
  │   ├── Cliente.java           
  │   ├── Entrenador.java        
  │   ├── Maquina.java           
  │   ├── Rutina.java           
  │   └── Usuario.java          
  ├── Vistas/
  │   ├── LoginVista.java        
  │   ├── PanelAdminVista.java   
  │   ├── PanelUsuarioVista.java 
  │   ├── DashboardVista.java    
  │   ├── ClientesVista.java    
  │   ├── FormularioClienteVista.java ← Form crear/editar
  │   ├── MaquinasVista.java     
  │   ├── RutinasVista.java      
  │   ├── EstilosGym.java        
  │   └── NavLateral.java        
  └── Main/
      └── Main.java              ← Arranca LoginVista
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
>>>>>>> 6ddca5054a3befbecf0aebbdfe24682ca604e4fb
```bash
git clone https://github.com/brayanarias-af/GimnasioApp.git
cd GimnasioApp
```
<<<<<<< HEAD

**2. Agregar el driver SQLite:**
- Descarga `sqlite-jdbc-3.51.0.0.jar` (o la versión disponible).
- Cópialo a la carpeta `lib/` del proyecto y renómbralo a `sqlite-jdbc.jar`.
- Ruta final: `GimnasioApp/lib/sqlite-jdbc.jar`

**3. Abrir en NetBeans:**
- `File → Open Project` → selecciona la carpeta `GimnasioApp`

**4. Verificar la librería:**
- Clic derecho en el proyecto → `Properties → Libraries`
- Verifica que aparezca `sqlite-jdbc.jar`
- Si no aparece: `Add JAR/Folder` → selecciona `lib/sqlite-jdbc.jar`

**5. Ejecutar:**
- `Run → Run Project (F6)`
- La base de datos `GymAPP.db` se crea **automáticamente** en la raíz del proyecto con datos de ejemplo.

**Ejecutar el JAR directamente (si está disponible):**
=======
PASO 1: Agregar el driver de SQLite
─────────────────────────────────────
  1. Descarga: sqlite-jdbc-3.51.0.0.jar  (ya lo tienes)
  2. Cópialo a la carpeta: GimnasioApp/lib/
     Renómbralo a: sqlite-jdbc.jar
     (Ruta final: GimnasioApp/lib/sqlite-jdbc.jar)
     
PASO 2: Abrir en NetBeans
────────────────────────────
  File → Open Project → selecciona la carpeta GimnasioApp

PASO 3: Verificar librería en NetBeans
────────────────────────────────────────
  - Click derecho en el proyecto → Properties
  - Libraries → verifica que aparezca sqlite-jdbc.jar
  - Si no aparece: Add JAR/Folder → selecciona lib/sqlite-jdbc.jar

PASO 4: Ejecutar
──────────────────
  Run → Run Project (F6)
  La base de datos GymAPP.db se crea AUTOMÁTICAMENTE en la raíz
  del proyecto con datos de ejemplo.
  
```
**Ejecutar el JAR (si está disponible):**
>>>>>>> 6ddca5054a3befbecf0aebbdfe24682ca604e4fb
```bash
java -jar GimnasioApp.jar
```

### Credenciales de Prueba
```
<<<<<<< HEAD
Administrador:  usuario: admin      contraseña: gimnasio123
Cliente:        usuario: cliente1   contraseña: 12345
=======
Admin:    usuario: admin      contraseña: gimnasio123
Cliente:  usuario: cliente1   contraseña: 12345
>>>>>>> 6ddca5054a3befbecf0aebbdfe24682ca604e4fb
```

---

<<<<<<< HEAD
## 📚 Referencias y Fuentes

El proyecto fue desarrollado tomando como base los siguientes recursos académicos y técnicos:

- **Oracle Java Documentation** — Documentación oficial de Java SE y SWING:  
  https://docs.oracle.com/en/java/

- **SQLite JDBC (Xerial)** — Driver oficial para conectar Java con SQLite:  
  https://github.com/xerial/sqlite-jdbc

- **SQLite Official Documentation** — Referencia del lenguaje SQL de SQLite:  
  https://www.sqlite.org/docs.html

- **NetBeans IDE Documentation** — Guía de uso del IDE Apache NetBeans:  
  https://netbeans.apache.org/front/main/index.html

- **Refactoring Guru — Patrones de Diseño** — Referencia sobre el patrón MVC:  
  https://refactoring.guru/es/design-patterns

- **Baeldung — Java SWING Tutorials** — Tutoriales prácticos de interfaz gráfica en Java:  
  https://www.baeldung.com/java-swing

---

## 👥 Integrantes del Grupo
=======
## Integrantes del Grupo
>>>>>>> 6ddca5054a3befbecf0aebbdfe24682ca604e4fb

| Nombre | Cédula | Rol |
|--------|--------|-----|
| Brayan Sneyder Arias Bueno | 1096538470 | Desarrollador |
<<<<<<< HEAD
| Jessica Lorena Arias Bueno | 1102636000 | Desarrollador |
| Santiago Cuesta Naranjo | 1096800383 | Desarrollador |

---

## 📄 Licencia

Proyecto académico desarrollado para la asignatura de Programación Orientada a Objetos — Tecnología de Desarrollo de Sistemas Informáticos, I Semestre 2026.
=======
| Jessica Lorena Arias Bueno | 1102636000  | Desarrollador |
| Santiago Cuesta Naranjo | 1096800383| Desarrollador |

---

## Licencia

Proyecto académico desarrollado para la Programacion Orientada a Objetos de Tecnología de Desarrollo de Sistemas Informáticos — I Semestre 2026.
>>>>>>> 6ddca5054a3befbecf0aebbdfe24682ca604e4fb
