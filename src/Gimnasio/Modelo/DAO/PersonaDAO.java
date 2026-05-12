package Gimnasio.Modelo.DAO;

import Gimnasio.Conexion.Conexion;
import Gimnasio.Modelo.Entidades.Cliente;
import Gimnasio.Modelo.Entidades.Entrenador;
import Gimnasio.Modelo.Entidades.Persona;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class PersonaDAO {

  
    public Persona obtenerPorIdUsuario(int idUsuario, String rol) {
        
        String sql = "SELECT * FROM clientes WHERE id_usuario = ?";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = Conexion.getConexion();
            ps = con.prepareStatement(sql);
            ps.setInt(1, idUsuario);
            rs = ps.executeQuery();

            if (rs.next()) {
                if ("cliente".equalsIgnoreCase(rol) || "admin".equalsIgnoreCase(rol)) {
                    
                    Cliente c = new Cliente();
                    c.setIdUsuario(rs.getInt("id_usuario"));
                    c.setCedula(rs.getString("cedula"));
                    c.setNombres(rs.getString("nombres"));
                    c.setApellidos(rs.getString("apellidos"));
                    c.setTelefono(rs.getString("telefono"));
                    c.setCorreo(rs.getString("correo"));
                    c.setRol(rol);
                    c.setCodigoCliente(rs.getString("codigo_cliente")); 
                    c.setPeso(rs.getDouble("peso"));
                    c.setAltura(rs.getDouble("altura"));
                    c.setObjetivo(rs.getString("objetivo"));
                    c.setEstadoMembresia(rs.getString("estado_membresia"));
                    c.setFechaIngreso(rs.getString("fecha_ingreso"));
                    return c;
                } else if ("entrenador".equalsIgnoreCase(rol)) {
                    Entrenador e = new Entrenador();
                    e.setIdUsuario(rs.getInt("id_usuario"));
                    e.setCedula(rs.getString("cedula"));
                    e.setNombres(rs.getString("nombres"));
                    e.setApellidos(rs.getString("apellidos"));
                    e.setTelefono(rs.getString("telefono"));
                    e.setCorreo(rs.getString("correo"));
                    e.setRol(rol);
                    e.setCodigoEntrenador(rs.getString("codigo_entrenador"));
                    e.setEspecialidad(rs.getString("especialidad"));
                    e.setAnosExperiencia(rs.getInt("anos_experiencia"));
                    e.setHorario(rs.getString("horario"));
                    e.setSalario(rs.getInt("salario"));
                    e.setFechaContratacion(rs.getString("fecha_contratacion"));
                    return e;
                }
            }
        } catch (Exception e) {
            System.out.println("Error al obtener datos personales: " + e.getMessage());
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (ps != null) ps.close(); } catch (Exception e) {}
            try { if (con != null) con.close(); } catch (Exception e) {}
        }
        return null;
    }
        
    public boolean insertarPersona(Persona persona) {
    String sql = "INSERT INTO clientes (id_usuario, cedula, nombres, apellidos, telefono, correo, direccion, "
               + "codigo_cliente, peso, altura, objetivo, estado_membresia, fecha_ingreso, "
               + "codigo_entrenador, especialidad, anos_experiencia, horario, salario, fecha_contratacion) "
               + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    Connection con = null;
    PreparedStatement ps = null;
    try {
        con = Conexion.getConexion();
        ps = con.prepareStatement(sql);
        ps.setInt(1, persona.getIdUsuario());
        ps.setString(2, persona.getCedula());
        ps.setString(3, persona.getNombres());
        ps.setString(4, persona.getApellidos());
        ps.setString(5, persona.getTelefono());
        ps.setString(6, persona.getCorreo());
        ps.setString(7, persona.getDireccion());

        if (persona instanceof Gimnasio.Modelo.Entidades.Cliente) {
            Gimnasio.Modelo.Entidades.Cliente c = (Gimnasio.Modelo.Entidades.Cliente) persona;
            ps.setString(8, c.getCodigoCliente());
            ps.setDouble(9, c.getPeso());
            ps.setDouble(10, c.getAltura());
            ps.setString(11, c.getObjetivo());
            ps.setString(12, c.getEstadoMembresia());
            ps.setString(13, c.getFechaIngreso());
            // campos de entrenador en null
            ps.setNull(14, java.sql.Types.VARCHAR);
            ps.setNull(15, java.sql.Types.VARCHAR);
            ps.setNull(16, java.sql.Types.INTEGER);
            ps.setNull(17, java.sql.Types.VARCHAR);
            ps.setNull(18, java.sql.Types.DOUBLE);
            ps.setNull(19, java.sql.Types.VARCHAR);
        } else if (persona instanceof Gimnasio.Modelo.Entidades.Entrenador) {
            Gimnasio.Modelo.Entidades.Entrenador e = (Gimnasio.Modelo.Entidades.Entrenador) persona;
            ps.setNull(8, java.sql.Types.VARCHAR);
            ps.setNull(9, java.sql.Types.DOUBLE);
            ps.setNull(10, java.sql.Types.DOUBLE);
            ps.setNull(11, java.sql.Types.VARCHAR);
            ps.setNull(12, java.sql.Types.VARCHAR);
            ps.setNull(13, java.sql.Types.VARCHAR);
            ps.setString(14, e.getCodigoEntrenador());
            ps.setString(15, e.getEspecialidad());
            ps.setInt(16, e.getAnosExperiencia());
            ps.setString(17, e.getHorario());
            ps.setDouble(18, e.getSalario());
            ps.setString(19, e.getFechaContratacion());
        } else {
            return false;
        }
        return ps.executeUpdate() > 0;
    } catch (Exception e) {
        System.out.println("Error al insertar persona: " + e.getMessage());
        return false;
    } finally {
        try { if (ps != null) ps.close(); } catch (Exception e) {}
        try { if (con != null) con.close(); } catch (Exception e) {}
    }
}
}