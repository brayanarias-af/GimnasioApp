PRAGMA foreign_keys = ON;

-- TABLA ROLES
CREATE TABLE roles(
id_rol INTEGER PRIMARY KEY AUTOINCREMENT,
nombre VARCHAR(30) NOT NULL UNIQUE
);

-- TABLA USUARIOS
CREATE TABLE usuarios(
id_usuario INTEGER PRIMARY KEY AUTOINCREMENT,
usuario VARCHAR(50) NOT NULL UNIQUE,
contrasena VARCHAR(255) NOT NULL,
estado BOOLEAN DEFAULT 1,
ultimo_acceso DATETIME,
intentos_fallidos INTEGER DEFAULT 0,
id_rol INTEGER NOT NULL,
FOREIGN KEY(id_rol) REFERENCES roles(id_rol)
);

-- TABLA CLIENTES
CREATE TABLE clientes(
id_cliente INTEGER PRIMARY KEY AUTOINCREMENT,
cedula VARCHAR(20) NOT NULL UNIQUE,
nombres VARCHAR(100) NOT NULL,
apellidos VARCHAR(100) NOT NULL,
edad INTEGER,
sexo VARCHAR(20),
telefono VARCHAR(20),
telefono_emergencia VARCHAR(20),
correo VARCHAR(100),
direccion TEXT,
eps VARCHAR(100),
peso REAL,
altura REAL,
objetivo VARCHAR(100),
fecha_ingreso DATE,
estado_membresia VARCHAR(30),
observaciones TEXT,
id_usuario INTEGER,
FOREIGN KEY(id_usuario) REFERENCES usuarios(id_usuario)
);

-- TABLA ENTRENADORES
CREATE TABLE entrenadores(
id_entrenador INTEGER PRIMARY KEY AUTOINCREMENT,
cedula VARCHAR(20) NOT NULL UNIQUE,
nombres VARCHAR(100) NOT NULL,
apellidos VARCHAR(100) NOT NULL,
telefono VARCHAR(20),
correo VARCHAR(100),
especialidad VARCHAR(100),
anios_experiencia INTEGER,
horario VARCHAR(100),
salario REAL,
fecha_contratacion DATE,
id_usuario INTEGER,
FOREIGN KEY(id_usuario) REFERENCES usuarios(id_usuario)
);

-- TABLA MEMBRESIAS
CREATE TABLE membresias(
id_membresia INTEGER PRIMARY KEY AUTOINCREMENT,
nombre VARCHAR(50) NOT NULL,
precio REAL NOT NULL,
duracion_dias INTEGER NOT NULL,
descripcion TEXT
);

-- TABLA PAGOS
CREATE TABLE pagos(
id_pago INTEGER PRIMARY KEY AUTOINCREMENT,
fecha_pago DATE NOT NULL,
monto REAL NOT NULL,
metodo_pago VARCHAR(50),
referencia_pago VARCHAR(100),
estado VARCHAR(30),
fecha_inicio DATE,
fecha_fin DATE,
id_cliente INTEGER NOT NULL,
id_membresia INTEGER NOT NULL,
FOREIGN KEY(id_cliente) REFERENCES clientes(id_cliente),
FOREIGN KEY(id_membresia) REFERENCES membresias(id_membresia)
);

-- TABLA ASISTENCIAS
CREATE TABLE asistencias(
id_asistencia INTEGER PRIMARY KEY AUTOINCREMENT,
fecha DATE NOT NULL,
hora_entrada TIME,
hora_salida TIME,
id_cliente INTEGER NOT NULL,
FOREIGN KEY(id_cliente) REFERENCES clientes(id_cliente)
);

-- TABLA PROGRESO FISICO
CREATE TABLE progreso_fisico(
id_progreso INTEGER PRIMARY KEY AUTOINCREMENT,
fecha_registro DATE NOT NULL,
peso REAL,
porcentaje_grasa REAL,
masa_muscular REAL,
imc REAL,
observaciones TEXT,
id_cliente INTEGER NOT NULL,
FOREIGN KEY(id_cliente) REFERENCES clientes(id_cliente)
);

-- TABLA MAQUINAS
CREATE TABLE maquinas(
id_maquina INTEGER PRIMARY KEY AUTOINCREMENT,
nombre VARCHAR(100) NOT NULL,
tipo VARCHAR(100),
marca VARCHAR(100),
modelo VARCHAR(100),
serial VARCHAR(100),
estado VARCHAR(50),
fecha_mantenimiento DATE
);

-- TABLA RUTINAS
CREATE TABLE rutinas(
id_rutina INTEGER PRIMARY KEY AUTOINCREMENT,
nombre VARCHAR(100) NOT NULL,
objetivo VARCHAR(100),
nivel VARCHAR(30),
duracion_semanas INTEGER,
descripcion TEXT,
id_entrenador INTEGER NOT NULL,
FOREIGN KEY(id_entrenador) REFERENCES entrenadores(id_entrenador)
);

-- TABLA EJERCICIOS
CREATE TABLE ejercicios(
id_ejercicio INTEGER PRIMARY KEY AUTOINCREMENT,
nombre VARCHAR(100) NOT NULL,
grupo_muscular VARCHAR(100),
descripcion TEXT,
series INTEGER,
repeticiones INTEGER,
descanso_segundos INTEGER
);

-- TABLA RUTINA_EJERCICIOS (con dia de semana)
CREATE TABLE rutina_ejercicios(
id INTEGER PRIMARY KEY AUTOINCREMENT,
id_rutina INTEGER NOT NULL,
id_ejercicio INTEGER NOT NULL,
dia_semana VARCHAR(10) NOT NULL DEFAULT 'Lunes',
orden INTEGER DEFAULT 1,
FOREIGN KEY(id_rutina) REFERENCES rutinas(id_rutina),
FOREIGN KEY(id_ejercicio) REFERENCES ejercicios(id_ejercicio)
);

-- TABLA CLIENTE_RUTINA
CREATE TABLE cliente_rutina(
id_cliente INTEGER NOT NULL,
id_rutina INTEGER NOT NULL,
fecha_asignacion DATE,
PRIMARY KEY(id_cliente,id_rutina),
FOREIGN KEY(id_cliente) REFERENCES clientes(id_cliente),
FOREIGN KEY(id_rutina) REFERENCES rutinas(id_rutina)
);

-- TABLA PERMISOS_MODULO (controla qué módulos ve cada usuario)
CREATE TABLE permisos_modulo(
id_permiso INTEGER PRIMARY KEY AUTOINCREMENT,
id_usuario INTEGER NOT NULL,
modulo VARCHAR(50) NOT NULL,
habilitado INTEGER DEFAULT 1,
FOREIGN KEY(id_usuario) REFERENCES usuarios(id_usuario),
UNIQUE(id_usuario, modulo)
);
