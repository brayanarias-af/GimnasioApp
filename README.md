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
```bash
git clone https://github.com/brayanarias-af/GimnasioApp.git
cd GimnasioApp
```
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
```bash
java -jar GimnasioApp.jar
```

### Credenciales de Prueba
```
Admin:    usuario: admin      contraseña: gimnasio123
Cliente:  usuario: cliente1   contraseña: 12345
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

Proyecto académico desarrollado para la Programacion Orientada a Objetos de Tecnología de Desarrollo de Sistemas Informáticos — I Semestre 2026.
