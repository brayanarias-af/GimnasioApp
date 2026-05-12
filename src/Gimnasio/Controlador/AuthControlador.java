package Gimnasio.Controlador;

import Gimnasio.Modelo.DAO.PersonaDAO;
import Gimnasio.Modelo.DAO.UsuarioDAO;
import Gimnasio.Modelo.Entidades.*;

public class AuthControlador {

   
    public String registrarUsuario(String nombreUsuario, String contrasena, String rol,
                                   String cedula, String nombres, String apellidos,
                                   String telefono, String correo, String direccion,
                                   String codigoCliente, double peso, double altura,
                                   String objetivo, String estadoMembresia, String fechaIngreso,
                                   String codigoEntrenador, String especialidad, int anosExperiencia,
                                   String horario, double salario, String fechaContratacion) {
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setNombreUsuario(nombreUsuario);
        nuevoUsuario.setContrasena(contrasena); // encriptar aquí
        nuevoUsuario.setEstado("activo");

        UsuarioDAO usuarioDAO = new UsuarioDAO();
        int idUsuario = usuarioDAO.insertarUsuario(nuevoUsuario, rol);
        if (idUsuario == -1) {
            return "Error: nombre de usuario ya existe o falló la creación.";
        }

        Persona persona;
        if ("entrenador".equalsIgnoreCase(rol)) {
            Entrenador e = new Entrenador();
            e.setIdUsuario(idUsuario);
            e.setCedula(cedula); e.setNombres(nombres); e.setApellidos(apellidos);
            e.setTelefono(telefono); e.setCorreo(correo); e.setDireccion(direccion);
            e.setRol(rol);
            e.setCodigoEntrenador(codigoEntrenador);
            e.setEspecialidad(especialidad);
            e.setAnosExperiencia(anosExperiencia);
            e.setHorario(horario);
            e.setSalario((int) salario);
            e.setFechaContratacion(fechaContratacion);
            persona = e;
        } else {
            Cliente c = new Cliente();
            c.setIdUsuario(idUsuario);
            c.setCedula(cedula); c.setNombres(nombres); c.setApellidos(apellidos);
            c.setTelefono(telefono); c.setCorreo(correo); c.setDireccion(direccion);
            c.setRol(rol);
            c.setCodigoCliente(codigoCliente);
            c.setPeso(peso);
            c.setAltura(altura);
            c.setObjetivo(objetivo);
            c.setEstadoMembresia(estadoMembresia);
            c.setFechaIngreso(fechaIngreso);
            persona = c;
        }

        PersonaDAO personaDAO = new PersonaDAO();
        if (personaDAO.insertarPersona(persona)) {
            return "Usuario registrado exitosamente.";
        } else {
            return "Error al guardar datos personales.";
        }
    }
}
