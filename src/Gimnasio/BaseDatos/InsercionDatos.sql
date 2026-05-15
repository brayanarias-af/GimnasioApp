-- ROLES

INSERT INTO roles(nombre)
VALUES
('ADMIN'),
('ENTRENADOR'),
('RECEPCIONISTA'),
('CLIENTE');

-- USUARIO ADMIN

INSERT INTO usuarios(usuario,contrasena,estado,ultimo_acceso,intentos_fallidos,id_rol)
VALUES('admin','gimnasio123',1,CURRENT_TIMESTAMP,0,1);

-- MEMBRESIAS

INSERT INTO membresias(nombre,precio,duracion_dias,descripcion)
VALUES
('Basica',50000,30,'Acceso general al gimnasio'),
('Premium',90000,30,'Acceso total + entrenador'),
('VIP',150000,30,'Acceso completo personalizado');

-- MAQUINAS

INSERT INTO maquinas(nombre,tipo,marca,modelo,serial,estado,fecha_mantenimiento)
VALUES
('Prensa de Pierna','Pierna','LifeFitness','LPX-500','SERIAL001','Disponible','2026-01-10'),
('Caminadora','Cardio','Technogym','RUN-900','SERIAL002','Disponible','2026-02-15');

-- USUARIO ENTRENADOR

INSERT INTO usuarios(usuario,contrasena,estado,ultimo_acceso,intentos_fallidos,id_rol)
VALUES('entrenador1','12345',1,CURRENT_TIMESTAMP,0,2);

-- ENTRENADOR

INSERT INTO entrenadores(cedula,nombres,apellidos,telefono,correo,especialidad,anios_experiencia,horario,salario,fecha_contratacion,id_usuario)
VALUES('100001','Carlos','Ramirez','3001112233','carlos@gym.com','Hipertrofia',5,'6AM - 2PM',2500000,'2025-01-10',2);

-- USUARIO CLIENTE

INSERT INTO usuarios(usuario,contrasena,estado,ultimo_acceso,intentos_fallidos,id_rol)
VALUES('cliente1','12345',1,CURRENT_TIMESTAMP,0,4);

-- CLIENTE

INSERT INTO clientes(cedula,nombres,apellidos,edad,sexo,telefono,telefono_emergencia,correo,direccion,eps,peso,altura,objetivo,fecha_ingreso,estado_membresia,observaciones,id_usuario)
VALUES('200001','Brayan','Arias',20,'Masculino','3009998888','3007776666','brayan@gmail.com','Santander','Nueva EPS',78,1.70,'Aumento de masa muscular','2026-05-08','Activa','Buen rendimiento',3);

-- RUTINA

INSERT INTO rutinas(nombre,objetivo,nivel,duracion_semanas,descripcion,id_entrenador)
VALUES('Hipertrofia Avanzada','Ganancia muscular','Intermedio',12,'Rutina enfocada en hipertrofia',1);

-- EJERCICIOS

INSERT INTO ejercicios(nombre,grupo_muscular,descripcion,series,repeticiones,descanso_segundos)
VALUES
('Press Banca','Pecho','Ejercicio de pecho',4,10,90),
('Sentadilla','Pierna','Ejercicio compuesto',4,12,120),
('Peso Muerto','Espalda','Ejercicio de fuerza',4,8,120);

-- RUTINA_EJERCICIOS

INSERT INTO rutina_ejercicios(id_rutina,id_ejercicio)
VALUES
(1,1),
(1,2),
(1,3);

-- CLIENTE_RUTINA

INSERT INTO cliente_rutina(id_cliente,id_rutina,fecha_asignacion)
VALUES(1,1,'2026-05-08');

-- PAGOS

INSERT INTO pagos(fecha_pago,monto,metodo_pago,referencia_pago,estado,id_cliente,id_membresia)
VALUES('2026-05-08',90000,'Transferencia','REF123456','Pagado',1,2);

-- ASISTENCIAS

INSERT INTO asistencias(fecha,hora_entrada,hora_salida,id_cliente)
VALUES('2026-05-08','06:00','08:00',1);

-- PROGRESO FISICO

INSERT INTO progreso_fisico(fecha_registro,peso,porcentaje_grasa,masa_muscular,imc,observaciones,id_cliente)
VALUES('2026-05-08',78,18,35,26.9,'Buen progreso fisico',1);