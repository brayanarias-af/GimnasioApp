package Gimnasio.Controlador;

import Gimnasio.Conexion.Conexion;
import Gimnasio.Modelo.Maquina;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MaquinaDAO {

    public List<Maquina> listarTodas() {
        List<Maquina> lista = new ArrayList<>();
        String sql = "SELECT * FROM maquinas ORDER BY nombre";
        try (PreparedStatement ps = Conexion.getConexion().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            System.err.println("Error al listar máquinas: " + e.getMessage());
        }
        return lista;
    }

    public int insertar(Maquina m) {
        String sql = "INSERT INTO maquinas(nombre, tipo, marca, modelo, serial, estado, fecha_mantenimiento) "
                   + "VALUES(?,?,?,?,?,?,?)";
        try (PreparedStatement ps = Conexion.getConexion().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, m.getNombre());
            ps.setString(2, m.getTipo());
            ps.setString(3, m.getMarca());
            ps.setString(4, m.getModelo());
            ps.setString(5, m.getSerial());
            ps.setString(6, m.getEstado());
            ps.setString(7, m.getFechaMantenimiento());
            ps.executeUpdate();
            ResultSet gen = ps.getGeneratedKeys();
            int id = gen.next() ? gen.getInt(1) : -1;
            gen.close();
            return id;
        } catch (SQLException e) {
            System.err.println("Error al insertar máquina: " + e.getMessage());
        }
        return -1;
    }

    public boolean actualizar(Maquina m) {
        String sql = "UPDATE maquinas SET nombre=?, tipo=?, marca=?, modelo=?, serial=?, estado=?, fecha_mantenimiento=? "
                   + "WHERE id_maquina=?";
        try (PreparedStatement ps = Conexion.getConexion().prepareStatement(sql)) {
            ps.setString(1, m.getNombre());
            ps.setString(2, m.getTipo());
            ps.setString(3, m.getMarca());
            ps.setString(4, m.getModelo());
            ps.setString(5, m.getSerial());
            ps.setString(6, m.getEstado());
            ps.setString(7, m.getFechaMantenimiento());
            ps.setInt   (8, m.getIdMaquina());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar máquina: " + e.getMessage());
        }
        return false;
    }

    public boolean eliminar(int id) {
        try (PreparedStatement ps = Conexion.getConexion().prepareStatement(
                "DELETE FROM maquinas WHERE id_maquina = ?")) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar máquina: " + e.getMessage());
        }
        return false;
    }

    public int contarTotal() {
        try (Statement st = Conexion.getConexion().createStatement();
             ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM maquinas")) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) { /* ignore */ }
        return 0;
    }

    public int contarDisponibles() {
        try (PreparedStatement ps = Conexion.getConexion().prepareStatement(
                "SELECT COUNT(*) FROM maquinas WHERE LOWER(estado) = 'disponible'")) {
            ResultSet rs = ps.executeQuery();
            int n = rs.next() ? rs.getInt(1) : 0;
            rs.close(); return n;
        } catch (SQLException e) { /* ignore */ }
        return 0;
    }

    private Maquina mapear(ResultSet rs) throws SQLException {
        Maquina m = new Maquina();
        m.setIdMaquina        (rs.getInt   ("id_maquina"));
        m.setNombre           (rs.getString("nombre"));
        m.setTipo             (rs.getString("tipo"));
        m.setMarca            (rs.getString("marca"));
        m.setModelo           (rs.getString("modelo"));
        m.setSerial           (rs.getString("serial"));
        m.setEstado           (rs.getString("estado"));
        m.setFechaMantenimiento(rs.getString("fecha_mantenimiento"));
        return m;
    }
}
