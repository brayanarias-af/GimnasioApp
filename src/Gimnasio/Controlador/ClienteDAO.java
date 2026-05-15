package Gimnasio.Controlador;

import Gimnasio.Conexion.Conexion;
import Gimnasio.Modelo.Cliente;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO de Clientes: todas las operaciones CRUD contra la tabla 'clientes' de SQLite.
 */
public class ClienteDAO {

    // ─────────────────── READ (listar todos) ───────────────────
    public List<Cliente> listarTodos() {
        List<Cliente> lista = new ArrayList<>();
        String sql = "SELECT * FROM clientes ORDER BY nombres, apellidos";
        try (PreparedStatement ps = Conexion.getConexion().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            System.err.println("Error al listar clientes: " + e.getMessage());
        }
        return lista;
    }

    // ─────────────────── READ (buscar) ───────────────────
    public List<Cliente> buscar(String filtro) {
        List<Cliente> lista = new ArrayList<>();
        String like = "%" + filtro.toLowerCase() + "%";
        String sql = "SELECT * FROM clientes "
                   + "WHERE LOWER(nombres) LIKE ? OR LOWER(apellidos) LIKE ? "
                   + "OR cedula LIKE ? OR LOWER(correo) LIKE ? "
                   + "ORDER BY nombres, apellidos";
        try (PreparedStatement ps = Conexion.getConexion().prepareStatement(sql)) {
            for (int i = 1; i <= 4; i++) ps.setString(i, like);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(mapear(rs));
            rs.close();
        } catch (SQLException e) {
            System.err.println("Error al buscar clientes: " + e.getMessage());
        }
        return lista;
    }

    // ─────────────────── READ (por ID) ───────────────────
    public Cliente buscarPorId(int id) {
        String sql = "SELECT * FROM clientes WHERE id_cliente = ?";
        try (PreparedStatement ps = Conexion.getConexion().prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapear(rs);
            rs.close();
        } catch (SQLException e) {
            System.err.println("Error al buscar cliente por id: " + e.getMessage());
        }
        return null;
    }

    // ─────────────────── CREATE ───────────────────
    /**
     * Inserta un cliente. Si usuarioNuevo es true también crea el registro en 'usuarios'.
     * Retorna el id_cliente generado, o -1 si falla.
     */
    public int insertar(Cliente c, String usuarioLogin, String contrasena) {
        Connection con = Conexion.getConexion();
        try {
            con.setAutoCommit(false);

            // 1. Crear usuario con rol CLIENTE (id_rol = 4)
            int idUsuario = -1;
            if (usuarioLogin != null && !usuarioLogin.isEmpty()) {
                String sqlU = "INSERT INTO usuarios(usuario, contrasena, estado, intentos_fallidos, id_rol) "
                            + "VALUES(?, ?, 1, 0, 4)";
                try (PreparedStatement ps = con.prepareStatement(sqlU, Statement.RETURN_GENERATED_KEYS)) {
                    ps.setString(1, usuarioLogin);
                    ps.setString(2, contrasena);
                    ps.executeUpdate();
                    ResultSet gen = ps.getGeneratedKeys();
                    if (gen.next()) idUsuario = gen.getInt(1);
                    gen.close();
                }
            }

            // 2. Insertar cliente
            String sqlC = "INSERT INTO clientes(cedula, nombres, apellidos, edad, sexo, "
                        + "telefono, telefono_emergencia, correo, direccion, eps, peso, altura, "
                        + "objetivo, fecha_ingreso, estado_membresia, observaciones, id_usuario) "
                        + "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            try (PreparedStatement ps = con.prepareStatement(sqlC, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, c.getCedula());
                ps.setString(2, c.getNombres());
                ps.setString(3, c.getApellidos());
                ps.setInt   (4, c.getEdad());
                ps.setString(5, c.getSexo());
                ps.setString(6, c.getTelefono());
                ps.setString(7, c.getTelefonoEmergencia());
                ps.setString(8, c.getCorreo());
                ps.setString(9, c.getDireccion());
                ps.setString(10, c.getEps());
                ps.setDouble(11, c.getPeso());
                ps.setDouble(12, c.getAltura());
                ps.setString(13, c.getObjetivo());
                ps.setString(14, c.getFechaIngreso());
                ps.setString(15, c.getEstadoMembresia());
                ps.setString(16, c.getObservaciones());
                if (idUsuario > 0) ps.setInt(17, idUsuario); else ps.setNull(17, Types.INTEGER);
                ps.executeUpdate();
                ResultSet gen = ps.getGeneratedKeys();
                int idGen = gen.next() ? gen.getInt(1) : -1;
                gen.close();
                con.commit();
                con.setAutoCommit(true);
                return idGen;
            }
        } catch (SQLException e) {
            System.err.println("Error al insertar cliente: " + e.getMessage());
            try { con.rollback(); con.setAutoCommit(true); } catch (SQLException ex) {}
        }
        return -1;
    }

    // ─────────────────── UPDATE ───────────────────
    public boolean actualizar(Cliente c) {
        String sql = "UPDATE clientes SET cedula=?, nombres=?, apellidos=?, edad=?, sexo=?, "
                   + "telefono=?, telefono_emergencia=?, correo=?, direccion=?, eps=?, peso=?, "
                   + "altura=?, objetivo=?, fecha_ingreso=?, estado_membresia=?, observaciones=? "
                   + "WHERE id_cliente=?";
        try (PreparedStatement ps = Conexion.getConexion().prepareStatement(sql)) {
            ps.setString(1, c.getCedula());
            ps.setString(2, c.getNombres());
            ps.setString(3, c.getApellidos());
            ps.setInt   (4, c.getEdad());
            ps.setString(5, c.getSexo());
            ps.setString(6, c.getTelefono());
            ps.setString(7, c.getTelefonoEmergencia());
            ps.setString(8, c.getCorreo());
            ps.setString(9, c.getDireccion());
            ps.setString(10, c.getEps());
            ps.setDouble(11, c.getPeso());
            ps.setDouble(12, c.getAltura());
            ps.setString(13, c.getObjetivo());
            ps.setString(14, c.getFechaIngreso());
            ps.setString(15, c.getEstadoMembresia());
            ps.setString(16, c.getObservaciones());
            ps.setInt   (17, c.getIdCliente());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar cliente: " + e.getMessage());
        }
        return false;
    }

    // ─────────────────── DELETE ───────────────────
    public boolean eliminar(int idCliente) {
        // Primero obtener id_usuario para borrarlo también si existe
        int idUsuario = -1;
        try (PreparedStatement ps = Conexion.getConexion().prepareStatement(
                "SELECT id_usuario FROM clientes WHERE id_cliente = ?")) {
            ps.setInt(1, idCliente);
            ResultSet rs = ps.executeQuery();
            if (rs.next() && !rs.wasNull()) idUsuario = rs.getInt(1);
            rs.close();
        } catch (SQLException e) { /* no crítico */ }

        try (PreparedStatement ps = Conexion.getConexion().prepareStatement(
                "DELETE FROM clientes WHERE id_cliente = ?")) {
            ps.setInt(1, idCliente);
            boolean ok = ps.executeUpdate() > 0;
            // Borrar usuario asociado si existe
            if (ok && idUsuario > 0) {
                try (PreparedStatement pu = Conexion.getConexion().prepareStatement(
                        "DELETE FROM usuarios WHERE id_usuario = ?")) {
                    pu.setInt(1, idUsuario);
                    pu.executeUpdate();
                } catch (SQLException e) { /* no crítico */ }
            }
            return ok;
        } catch (SQLException e) {
            System.err.println("Error al eliminar cliente: " + e.getMessage());
        }
        return false;
    }

    // ─────────────────── CONTEO ───────────────────
    public int contarTotal() {
        try (Statement st = Conexion.getConexion().createStatement();
             ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM clientes")) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) { /* ignore */ }
        return 0;
    }

    public int contarActivos() {
        try (PreparedStatement ps = Conexion.getConexion().prepareStatement(
                "SELECT COUNT(*) FROM clientes WHERE LOWER(estado_membresia) = 'activa'")) {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
            rs.close();
        } catch (SQLException e) { /* ignore */ }
        return 0;
    }

    // ─────────────────── MAPEO ───────────────────
    private Cliente mapear(ResultSet rs) throws SQLException {
        Cliente c = new Cliente();
        c.setIdCliente       (rs.getInt   ("id_cliente"));
        c.setCedula          (rs.getString("cedula"));
        c.setNombres         (rs.getString("nombres"));
        c.setApellidos       (rs.getString("apellidos"));
        c.setEdad            (rs.getInt   ("edad"));
        c.setSexo            (rs.getString("sexo"));
        c.setTelefono        (rs.getString("telefono"));
        c.setTelefonoEmergencia(rs.getString("telefono_emergencia"));
        c.setCorreo          (rs.getString("correo"));
        c.setDireccion       (rs.getString("direccion"));
        c.setEps             (rs.getString("eps"));
        c.setPeso            (rs.getDouble("peso"));
        c.setAltura          (rs.getDouble("altura"));
        c.setObjetivo        (rs.getString("objetivo"));
        c.setFechaIngreso    (rs.getString("fecha_ingreso"));
        c.setEstadoMembresia (rs.getString("estado_membresia"));
        c.setObservaciones   (rs.getString("observaciones"));
        c.setIdUsuario       (rs.getInt   ("id_usuario"));
        return c;
    }
}
