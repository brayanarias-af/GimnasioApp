package Gimnasio.Controlador;

import Gimnasio.Conexion.Conexion;
import Gimnasio.Modelo.Pago;
import java.sql.*;
import java.time.LocalDate;
import java.util.*;

public class PagoDAO {

    // ── SELECT ─────────────────────────────────────────────────
    public List<Pago> listarTodos() {
        return ejecutarLista(
            "SELECT p.*,c.nombres||' '||c.apellidos as nc,m.nombre as nm "
           +"FROM pagos p JOIN clientes c ON p.id_cliente=c.id_cliente "
           +"JOIN membresias m ON p.id_membresia=m.id_membresia ORDER BY p.fecha_pago DESC",
            ps -> {});
    }

    public List<Pago> listarPorCliente(int idCliente) {
        return ejecutarLista(
            "SELECT p.*,c.nombres||' '||c.apellidos as nc,m.nombre as nm "
           +"FROM pagos p JOIN clientes c ON p.id_cliente=c.id_cliente "
           +"JOIN membresias m ON p.id_membresia=m.id_membresia "
           +"WHERE p.id_cliente=? ORDER BY p.fecha_pago DESC",
            ps -> ps.setInt(1, idCliente));
    }

    /** Pagos pendientes o vencidos (fecha_fin < hoy) de un cliente */
    public List<Pago> listarPendientesOVencidos(int idCliente) {
        String hoy = LocalDate.now().toString();
        return ejecutarLista(
            "SELECT p.*,c.nombres||' '||c.apellidos as nc,m.nombre as nm "
           +"FROM pagos p JOIN clientes c ON p.id_cliente=c.id_cliente "
           +"JOIN membresias m ON p.id_membresia=m.id_membresia "
           +"WHERE p.id_cliente=? AND (LOWER(p.estado)='pendiente' OR "
           +"(LOWER(p.estado)='pagado' AND p.fecha_fin IS NOT NULL AND p.fecha_fin < ?)) "
           +"ORDER BY p.fecha_pago DESC",
            ps -> { ps.setInt(1, idCliente); ps.setString(2, hoy); });
    }

    // ── INSERT ────────────────────────────────────────────────
    public int insertar(Pago p) {
        try (PreparedStatement ps = Conexion.getConexion().prepareStatement(
                "INSERT INTO pagos(fecha_pago,monto,metodo_pago,referencia_pago,estado,"
               +"id_cliente,id_membresia,fecha_inicio,fecha_fin) VALUES(?,?,?,?,?,?,?,?,?)",
                Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, p.getFechaPago());
            ps.setDouble(2, p.getMonto());
            ps.setString(3, p.getMetodoPago());
            ps.setString(4, p.getReferenciaPago());
            ps.setString(5, p.getEstado());
            ps.setInt(6, p.getIdCliente());
            ps.setInt(7, p.getIdMembresia());
            setNullable(ps, 8, p.getFechaInicio());
            setNullable(ps, 9, p.getFechaFin());
            ps.executeUpdate();
            ResultSet g = ps.getGeneratedKeys();
            int id = g.next() ? g.getInt(1) : -1;
            g.close();
            // Actualizar estado_membresia del cliente
            if (id > 0 && "Pagado".equalsIgnoreCase(p.getEstado())) {
                actualizarMembresia(p.getIdCliente());
            } else if (id > 0 && "Pendiente".equalsIgnoreCase(p.getEstado())) {
                actualizarEstadoClienteDirecto(p.getIdCliente(), "Pendiente");
            }
            return id;
        } catch (SQLException e) { e.printStackTrace(); return -1; }
    }

    /** Cancela (paga) un pago existente que estaba Pendiente o Vencido */
    public boolean cancelarPago(int idPago, String metodo, String ref, String fechaPago,
                                 String fechaInicio, String fechaFin) {
        try (PreparedStatement ps = Conexion.getConexion().prepareStatement(
                "UPDATE pagos SET estado='Pagado',metodo_pago=?,referencia_pago=?,"
               +"fecha_pago=?,fecha_inicio=?,fecha_fin=? WHERE id_pago=?")) {
            ps.setString(1, metodo);
            ps.setString(2, ref);
            ps.setString(3, fechaPago);
            setNullable(ps, 4, fechaInicio);
            setNullable(ps, 5, fechaFin);
            ps.setInt(6, idPago);
            boolean ok = ps.executeUpdate() > 0;
            if (ok) {
                // Obtener id_cliente del pago para actualizar membresía
                try (PreparedStatement ps2 = Conexion.getConexion().prepareStatement(
                        "SELECT id_cliente FROM pagos WHERE id_pago=?")) {
                    ps2.setInt(1, idPago);
                    ResultSet rs = ps2.executeQuery();
                    if (rs.next()) actualizarMembresia(rs.getInt(1));
                    rs.close();
                } catch (SQLException ex) {}
            }
            return ok;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public boolean eliminar(int id) {
        try (PreparedStatement ps = Conexion.getConexion().prepareStatement(
                "DELETE FROM pagos WHERE id_pago=?")) {
            ps.setInt(1, id); return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    // ── Actualización automática de membresías ─────────────────
    /**
     * Recorre todos los clientes y actualiza estado_membresia:
     * - Si tiene pago Pagado con fecha_fin >= hoy → Activa
     * - Si tiene pago Pendiente → Pendiente
     * - Si fecha_fin < hoy → Vencida
     * Llamar al iniciar sesión (desde Sesion.java)
     */
    public void actualizarEstadosMembresia() {
        String hoy = LocalDate.now().toString();
        // Marcar Activa: tiene pago pagado vigente
        try (PreparedStatement ps = Conexion.getConexion().prepareStatement(
                "UPDATE clientes SET estado_membresia='Activa' WHERE id_cliente IN ("
               +"SELECT DISTINCT id_cliente FROM pagos "
               +"WHERE LOWER(estado)='pagado' AND fecha_inicio IS NOT NULL AND fecha_fin IS NOT NULL "
               +"AND fecha_inicio <= ? AND fecha_fin >= ?)")) {
            ps.setString(1, hoy); ps.setString(2, hoy); ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }

        // Marcar Pendiente: tiene algún pago pendiente
        try (PreparedStatement ps = Conexion.getConexion().prepareStatement(
                "UPDATE clientes SET estado_membresia='Pendiente' WHERE id_cliente IN ("
               +"SELECT DISTINCT id_cliente FROM pagos WHERE LOWER(estado)='pendiente') "
               +"AND estado_membresia != 'Activa'")) {
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }

        // Marcar Vencida: su último pago pagado tiene fecha_fin < hoy
        try (PreparedStatement ps = Conexion.getConexion().prepareStatement(
                "UPDATE clientes SET estado_membresia='Vencida' "
               +"WHERE estado_membresia NOT IN ('Activa','Pendiente') "
               +"AND id_cliente IN ("
               +"SELECT id_cliente FROM pagos WHERE LOWER(estado)='pagado' "
               +"AND fecha_fin IS NOT NULL AND fecha_fin < ? "
               +"AND id_cliente NOT IN ("
               +"SELECT id_cliente FROM pagos WHERE LOWER(estado)='pagado' "
               +"AND fecha_fin IS NOT NULL AND fecha_fin >= ?))")) {
            ps.setString(1, hoy); ps.setString(2, hoy); ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    /** Actualiza el estado_membresia de un cliente específico según sus pagos */
    private void actualizarMembresia(int idCliente) {
        String hoy = LocalDate.now().toString();
        // ¿Tiene algún pago pagado vigente?
        try (PreparedStatement ps = Conexion.getConexion().prepareStatement(
                "SELECT COUNT(*) FROM pagos WHERE id_cliente=? AND LOWER(estado)='pagado' "
               +"AND fecha_inicio IS NOT NULL AND fecha_fin IS NOT NULL "
               +"AND fecha_inicio <= ? AND fecha_fin >= ?")) {
            ps.setInt(1, idCliente); ps.setString(2, hoy); ps.setString(3, hoy);
            ResultSet rs = ps.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                actualizarEstadoClienteDirecto(idCliente, "Activa");
                rs.close(); return;
            }
            rs.close();
        } catch (SQLException e) {}
        actualizarEstadoClienteDirecto(idCliente, "Activa");
    }

    private void actualizarEstadoClienteDirecto(int idCliente, String estado) {
        try (PreparedStatement ps = Conexion.getConexion().prepareStatement(
                "UPDATE clientes SET estado_membresia=? WHERE id_cliente=?")) {
            ps.setString(1, estado); ps.setInt(2, idCliente); ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    // ── Estadísticas ───────────────────────────────────────────
    public double totalIngresos() {
        try (ResultSet rs = Conexion.getConexion().createStatement().executeQuery(
                "SELECT COALESCE(SUM(monto),0) FROM pagos WHERE LOWER(estado)='pagado'")) {
            if (rs.next()) return rs.getDouble(1);
        } catch (SQLException e) {}
        return 0;
    }

    public int contarTotal() {
        try (ResultSet rs = Conexion.getConexion().createStatement().executeQuery(
                "SELECT COUNT(*) FROM pagos")) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {}
        return 0;
    }

    // ── Internos ───────────────────────────────────────────────
    @FunctionalInterface
    private interface ParamSetter {
        void set(PreparedStatement ps) throws SQLException;
    }

    private List<Pago> ejecutarLista(String sql, ParamSetter setter) {
        List<Pago> l = new ArrayList<>();
        try (PreparedStatement ps = Conexion.getConexion().prepareStatement(sql)) {
            setter.set(ps);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) l.add(map(rs));
            rs.close();
        } catch (SQLException e) { e.printStackTrace(); }
        return l;
    }

    private Pago map(ResultSet rs) throws SQLException {
        Pago p = new Pago();
        p.setIdPago(rs.getInt("id_pago"));
        p.setFechaPago(rs.getString("fecha_pago"));
        p.setMonto(rs.getDouble("monto"));
        p.setMetodoPago(rs.getString("metodo_pago"));
        p.setReferenciaPago(rs.getString("referencia_pago"));
        p.setEstado(rs.getString("estado"));
        p.setIdCliente(rs.getInt("id_cliente"));
        p.setIdMembresia(rs.getInt("id_membresia"));
        // Columnas nuevas: protegidas en caso de BD sin migrar
        try { p.setFechaInicio(rs.getString("fecha_inicio")); } catch (SQLException ignored) {}
        try { p.setFechaFin(rs.getString("fecha_fin"));       } catch (SQLException ignored) {}
        try { p.setNombreCliente(rs.getString("nc"));         } catch (SQLException ignored) {}
        try { p.setNombreMembresia(rs.getString("nm"));       } catch (SQLException ignored) {}
        return p;
    }

    private void setNullable(PreparedStatement ps, int idx, String val) throws SQLException {
        if (val != null && !val.isBlank()) ps.setString(idx, val);
        else ps.setNull(idx, Types.VARCHAR);
    }
}
