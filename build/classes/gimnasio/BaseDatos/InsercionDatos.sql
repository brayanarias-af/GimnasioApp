-- ROLES
INSERT INTO roles(nombre) VALUES ('ADMIN'),('ENTRENADOR'),('RECEPCIONISTA'),('CLIENTE');

-- USUARIOS ADMIN
INSERT INTO usuarios(usuario,contrasena,estado,ultimo_acceso,intentos_fallidos,id_rol)
VALUES('admin','gimnasio123',1,CURRENT_TIMESTAMP,0,1);

-- MEMBRESIAS
INSERT INTO membresias(nombre,precio,duracion_dias,descripcion) VALUES
('Basica',50000,30,'Acceso general al gimnasio'),
('Premium',90000,30,'Acceso total + entrenador personal'),
('VIP',150000,30,'Acceso completo con seguimiento personalizado');

-- MAQUINAS
INSERT INTO maquinas(nombre,tipo,marca,modelo,serial,estado,fecha_mantenimiento) VALUES
('Prensa de Pierna','Fuerza','LifeFitness','LPX-500','SERIAL001','Disponible','2026-01-10'),
('Caminadora','Cardio','Technogym','RUN-900','SERIAL002','Disponible','2026-02-15'),
('Bicicleta Estática','Cardio','Technogym','BIKE-500','SERIAL003','Disponible','2026-03-01'),
('Polea Alta','Fuerza','BH Fitness','POL-200','SERIAL004','En uso','2026-01-20'),
('Remo','Cardio','Concept2','ROW-D','SERIAL005','Disponible','2026-02-10');

-- USUARIO ENTRENADOR
INSERT INTO usuarios(usuario,contrasena,estado,ultimo_acceso,intentos_fallidos,id_rol)
VALUES('entrenador1','12345',1,CURRENT_TIMESTAMP,0,2);

-- ENTRENADOR
INSERT INTO entrenadores(cedula,nombres,apellidos,telefono,correo,especialidad,anios_experiencia,horario,salario,fecha_contratacion,id_usuario)
VALUES('100001','Carlos','Ramirez','3001112233','carlos@gym.com','Hipertrofia y Fuerza',5,'6AM - 2PM',2500000,'2025-01-10',2);

-- USUARIO CLIENTE
INSERT INTO usuarios(usuario,contrasena,estado,ultimo_acceso,intentos_fallidos,id_rol)
VALUES('cliente1','12345',1,CURRENT_TIMESTAMP,0,4);

-- CLIENTE
INSERT INTO clientes(cedula,nombres,apellidos,edad,sexo,telefono,telefono_emergencia,correo,direccion,eps,peso,altura,objetivo,fecha_ingreso,estado_membresia,observaciones,id_usuario)
VALUES('200001','Brayan','Arias',20,'Masculino','3009998888','3007776666','brayan@gmail.com','Santander','Nueva EPS',78,1.70,'Aumento de masa muscular','2026-05-08','Activa','Buen rendimiento',3);

-- EJERCICIOS
INSERT INTO ejercicios(nombre,grupo_muscular,descripcion,series,repeticiones,descanso_segundos) VALUES
('Press Banca','Pecho','Empuje horizontal en banco plano',4,10,90),
('Press Inclinado','Pecho','Empuje en banco inclinado 45°',4,10,90),
('Aperturas','Pecho','Aperturas con mancuernas',3,12,60),
('Sentadilla','Piernas','Ejercicio compuesto de pierna',4,12,120),
('Prensa de Pierna','Piernas','Prensa en máquina',4,15,90),
('Extensión de Cuádriceps','Piernas','Extensión en máquina',3,15,60),
('Curl Femoral','Piernas','Curl acostado en máquina',3,12,60),
('Peso Muerto','Espalda','Levantamiento de peso muerto',4,8,120),
('Jalón al Pecho','Espalda','Jalón en polea alta',4,12,90),
('Remo con Barra','Espalda','Remo horizontal con barra',4,10,90),
('Press Militar','Hombros','Press sobre cabeza con barra',4,10,90),
('Elevaciones Laterales','Hombros','Elevaciones con mancuernas',3,15,60),
('Curl Bíceps','Bíceps','Curl con barra o mancuernas',4,12,60),
('Martillo','Bíceps','Curl martillo con mancuernas',3,12,60),
('Tríceps Polea','Tríceps','Extensión en polea alta',4,15,60),
('Fondos','Tríceps','Fondos en paralelas o banco',3,12,60),
('Plancha','Core','Plancha abdominal estática',3,60,45),
('Abdominales','Core','Crunches abdominales',4,20,45),
('Cardio HIIT','Cardio','Intervalos de alta intensidad',1,20,0),
('Elíptica','Cardio','Cardio en elíptica',1,30,0);

-- RUTINA
INSERT INTO rutinas(nombre,objetivo,nivel,duracion_semanas,descripcion,id_entrenador)
VALUES('Hipertrofia 4 días','Ganancia muscular','Intermedio',12,'Rutina enfocada en hipertrofia con división Push/Pull/Legs',1);

-- RUTINA_EJERCICIOS (con días de semana)
INSERT INTO rutina_ejercicios(id_rutina,id_ejercicio,dia_semana,orden) VALUES
(1,1,'Lunes',1),(1,2,'Lunes',2),(1,3,'Lunes',3),
(1,4,'Miércoles',1),(1,5,'Miércoles',2),(1,6,'Miércoles',3),(1,7,'Miércoles',4),
(1,8,'Viernes',1),(1,9,'Viernes',2),(1,10,'Viernes',3),
(1,13,'Martes',1),(1,14,'Martes',2),(1,15,'Martes',3),
(1,17,'Jueves',1),(1,18,'Jueves',2),(1,19,'Jueves',3);

-- CLIENTE_RUTINA
INSERT INTO cliente_rutina(id_cliente,id_rutina,fecha_asignacion) VALUES(1,1,'2026-05-08');

-- PAGOS
INSERT INTO pagos(fecha_pago,monto,metodo_pago,referencia_pago,estado,id_cliente,id_membresia)
VALUES('2026-05-08',90000,'Transferencia','REF123456','Pagado',1,2);

-- Pago pendiente para demo
INSERT INTO pagos(fecha_pago,monto,metodo_pago,referencia_pago,estado,id_cliente,id_membresia)
VALUES('2026-06-08',90000,'Efectivo','','Pendiente',1,2);

-- ASISTENCIAS
INSERT INTO asistencias(fecha,hora_entrada,hora_salida,id_cliente)
VALUES('2026-05-08','06:00','08:00',1),('2026-05-10','07:00','09:00',1),('2026-05-12','06:30','08:30',1);

-- PROGRESO FISICO
INSERT INTO progreso_fisico(fecha_registro,peso,porcentaje_grasa,masa_muscular,imc,observaciones,id_cliente)
VALUES('2026-05-08',78,18.0,35.0,26.9,'Inicio del programa'),
      ('2026-05-15',77.5,17.5,35.5,26.7,'Buena evolución'),
      ('2026-05-22',77.0,17.0,36.0,26.5,'Progreso consistente');

-- PERMISOS POR DEFECTO para el entrenador (puede ver todo en su panel)
INSERT INTO permisos_modulo(id_usuario,modulo,habilitado) VALUES
(2,'Mis Rutinas',1),(2,'Máquinas',1),(2,'Clientes Asignados',1),(2,'Progreso Clientes',1);

-- PERMISOS POR DEFECTO para el cliente
INSERT INTO permisos_modulo(id_usuario,modulo,habilitado) VALUES
(3,'Mis Rutinas',1),(3,'Máquinas',1),(3,'Mis Pagos',1),(3,'Mi Progreso',1);
