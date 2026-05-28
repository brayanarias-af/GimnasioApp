package Gimnasio.Controlador;
import Gimnasio.Conexion.Conexion;
import Gimnasio.Modelo.Cliente;
import java.sql.*;
import java.util.*;

public class ClienteDAO {
    public List<Cliente> listarTodos() {
        List<Cliente> l = new ArrayList<>();
        try (PreparedStatement ps = Conexion.getConexion().prepareStatement(
                "SELECT * FROM clientes ORDER BY nombres,apellidos");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) l.add(map(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return l;
    }

    public List<Cliente> buscar(String f) {
        List<Cliente> l = new ArrayList<>();
        String like = "%" + f.toLowerCase() + "%";
        try (PreparedStatement ps = Conexion.getConexion().prepareStatement(
                "SELECT * FROM clientes WHERE LOWER(nombres) LIKE ? OR LOWER(apellidos) LIKE ? OR cedula LIKE ? ORDER BY nombres")) {
            for (int i=1;i<=3;i++) ps.setString(i, like);
            ResultSet rs = ps.executeQuery(); while (rs.next()) l.add(map(rs)); rs.close();
        } catch (SQLException e) { e.printStackTrace(); }
        return l;
    }

    /** Busca un cliente exactamente por su número de cédula */
    public Cliente buscarPorCedula(String cedula) {
        try (PreparedStatement ps = Conexion.getConexion().prepareStatement(
                "SELECT * FROM clientes WHERE cedula=?")) {
            ps.setString(1, cedula.trim());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) { Cliente c = map(rs); rs.close(); return c; }
            rs.close();
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    public Cliente buscarPorId(int id) {
        try (PreparedStatement ps = Conexion.getConexion().prepareStatement("SELECT * FROM clientes WHERE id_cliente=?")) {
            ps.setInt(1, id); ResultSet rs = ps.executeQuery();
            if (rs.next()) return map(rs); rs.close();
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    public int insertar(Cliente c, String usuario, String clave) {
        Connection con = Conexion.getConexion();
        try {
            con.setAutoCommit(false);
            int idU = -1;
            if (usuario != null && !usuario.isEmpty()) {
                try (PreparedStatement ps = con.prepareStatement(
                        "INSERT INTO usuarios(usuario,contrasena,estado,intentos_fallidos,id_rol) VALUES(?,?,1,0,4)",
                        Statement.RETURN_GENERATED_KEYS)) {
                    ps.setString(1,usuario); ps.setString(2,clave); ps.executeUpdate();
                    ResultSet g = ps.getGeneratedKeys(); if (g.next()) idU = g.getInt(1); g.close();
                }
            }
            try (PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO clientes(cedula,nombres,apellidos,edad,sexo,telefono,telefono_emergencia,"
                  + "correo,direccion,eps,peso,altura,objetivo,fecha_ingreso,estado_membresia,observaciones,id_usuario) "
                  + "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1,c.getCedula()); ps.setString(2,c.getNombres()); ps.setString(3,c.getApellidos());
                ps.setInt(4,c.getEdad()); ps.setString(5,c.getSexo()); ps.setString(6,c.getTelefono());
                ps.setString(7,c.getTelefonoEmergencia()); ps.setString(8,c.getCorreo());
                ps.setString(9,c.getDireccion()); ps.setString(10,c.getEps());
                ps.setDouble(11,c.getPeso()); ps.setDouble(12,c.getAltura());
                ps.setString(13,c.getObjetivo()); ps.setString(14,c.getFechaIngreso());
                ps.setString(15,c.getEstadoMembresia()); ps.setString(16,c.getObservaciones());
                if (idU>0) ps.setInt(17,idU); else ps.setNull(17,Types.INTEGER);
                ps.executeUpdate();
                ResultSet g = ps.getGeneratedKeys(); int id = g.next() ? g.getInt(1) : -1; g.close();
                con.commit(); con.setAutoCommit(true); return id;
            }
        } catch (SQLException e) {
            try { con.rollback(); con.setAutoCommit(true); } catch (SQLException ex) {}
            e.printStackTrace();
        }
        return -1;
    }

    public boolean actualizar(Cliente c) {
        try (PreparedStatement ps = Conexion.getConexion().prepareStatement(
                "UPDATE clientes SET cedula=?,nombres=?,apellidos=?,edad=?,sexo=?,telefono=?,"
               +"telefono_emergencia=?,correo=?,direccion=?,eps=?,peso=?,altura=?,objetivo=?,"
               +"fecha_ingreso=?,estado_membresia=?,observaciones=? WHERE id_cliente=?")) {
            ps.setString(1,c.getCedula()); ps.setString(2,c.getNombres()); ps.setString(3,c.getApellidos());
            ps.setInt(4,c.getEdad()); ps.setString(5,c.getSexo()); ps.setString(6,c.getTelefono());
            ps.setString(7,c.getTelefonoEmergencia()); ps.setString(8,c.getCorreo());
            ps.setString(9,c.getDireccion()); ps.setString(10,c.getEps());
            ps.setDouble(11,c.getPeso()); ps.setDouble(12,c.getAltura());
            ps.setString(13,c.getObjetivo()); ps.setString(14,c.getFechaIngreso());
            ps.setString(15,c.getEstadoMembresia()); ps.setString(16,c.getObservaciones());
            ps.setInt(17,c.getIdCliente());
            return ps.executeUpdate()>0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    public boolean eliminar(int id) {
        int idU = -1;
        try (PreparedStatement ps = Conexion.getConexion().prepareStatement(
                "SELECT id_usuario FROM clientes WHERE id_cliente=?")) {
            ps.setInt(1,id); ResultSet rs = ps.executeQuery();
            if (rs.next()) idU = rs.getInt(1); rs.close();
        } catch (SQLException e) {}
        try (PreparedStatement ps = Conexion.getConexion().prepareStatement(
                "DELETE FROM clientes WHERE id_cliente=?")) {
            ps.setInt(1,id); boolean ok = ps.executeUpdate()>0;
            if (ok && idU>0) try (PreparedStatement pu = Conexion.getConexion().prepareStatement(
                    "DELETE FROM usuarios WHERE id_usuario=?")) { pu.setInt(1,idU); pu.executeUpdate(); }
            catch (SQLException e) {}
            return ok;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    public int contarTotal() {
        try (ResultSet rs = Conexion.getConexion().createStatement().executeQuery("SELECT COUNT(*) FROM clientes")) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {}
        return 0;
    }
    public int contarActivos() {
        try (PreparedStatement ps = Conexion.getConexion().prepareStatement(
                "SELECT COUNT(*) FROM clientes WHERE LOWER(estado_membresia)='activa'")) {
            ResultSet rs = ps.executeQuery(); int n = rs.next() ? rs.getInt(1) : 0; rs.close(); return n;
        } catch (SQLException e) {} return 0;
    }
    public List<Object[]> listarParaCombo() {
        List<Object[]> l = new ArrayList<>();
        try (PreparedStatement ps = Conexion.getConexion().prepareStatement(
                "SELECT id_cliente, nombres||' '||apellidos FROM clientes ORDER BY nombres");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) l.add(new Object[]{rs.getInt(1), rs.getString(2)});
        } catch (SQLException e) {}
        return l;
    }

    private Cliente map(ResultSet rs) throws SQLException {
        Cliente c = new Cliente();
        c.setIdCliente(rs.getInt("id_cliente")); c.setCedula(rs.getString("cedula"));
        c.setNombres(rs.getString("nombres")); c.setApellidos(rs.getString("apellidos"));
        c.setEdad(rs.getInt("edad")); c.setSexo(rs.getString("sexo"));
        c.setTelefono(rs.getString("telefono")); c.setTelefonoEmergencia(rs.getString("telefono_emergencia"));
        c.setCorreo(rs.getString("correo")); c.setDireccion(rs.getString("direccion"));
        c.setEps(rs.getString("eps")); c.setPeso(rs.getDouble("peso")); c.setAltura(rs.getDouble("altura"));
        c.setObjetivo(rs.getString("objetivo")); c.setFechaIngreso(rs.getString("fecha_ingreso"));
        c.setEstadoMembresia(rs.getString("estado_membresia")); c.setObservaciones(rs.getString("observaciones"));
        c.setIdUsuario(rs.getInt("id_usuario"));
        return c;
    }
}
