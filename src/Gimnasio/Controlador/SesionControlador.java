package Gimnasio.Controlador;

import Gimnasio.Modelo.DAO.PersonaDAO;
import Gimnasio.Modelo.DAO.UsuarioDAO;
import Gimnasio.Modelo.Entidades.Persona;
import Gimnasio.Modelo.Entidades.Usuario;

public class SesionControlador {
    private static SesionControlador instancia;
    private Persona usuarioActual;

    private SesionControlador() {}

    public static SesionControlador getInstance() {
        if (instancia == null) {
            instancia = new SesionControlador();
        }
        return instancia;
    }

    public boolean iniciarSesion(String nombreUsuario, String contrasena) {
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        Usuario usuario = usuarioDAO.iniciarSesion(nombreUsuario, contrasena);
        if (usuario != null) {
            PersonaDAO personaDAO = new PersonaDAO();
            Persona persona = personaDAO.obtenerPorIdUsuario(usuario.getIdUsuario(), usuario.getNombreRol());
            if (persona != null) {
                this.usuarioActual = persona;
                return true;
            }
        }
        return false;
    }

    public void cerrarSesion() {
        usuarioActual = null;
    }

    public Persona getUsuarioActual() {
        return usuarioActual;
    }

    public String getNombreCompleto() {
        if (usuarioActual == null) return "Invitado";
        return usuarioActual.getNombres() + " " + usuarioActual.getApellidos();
    }

    public boolean esAdmin() {
        return usuarioActual != null && "admin".equalsIgnoreCase(usuarioActual.getRol());
    }

    public boolean esEntrenador() {
        return usuarioActual != null && "entrenador".equalsIgnoreCase(usuarioActual.getRol());
    }

    public boolean esCliente() {
        return usuarioActual != null && "cliente".equalsIgnoreCase(usuarioActual.getRol());
    }
}