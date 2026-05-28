package Gimnasio.Controlador;
import Gimnasio.Conexion.Conexion;
import Gimnasio.Modelo.Ejercicio;
import java.sql.*;
import java.util.*;

public class EjercicioDAO {

    public static final String[] DIAS = {"Lunes","Martes","Miércoles","Jueves","Viernes","Sábado","Domingo"};

    public List<Ejercicio> listarTodos() {
        List<Ejercicio> l = new ArrayList<>();
        try (PreparedStatement ps = Conexion.getConexion().prepareStatement(
                "SELECT * FROM ejercicios ORDER BY grupo_muscular, nombre");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) l.add(map(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return l;
    }

    public List<Object[]> listarParaCombo() {
        List<Object[]> l = new ArrayList<>();
        try (PreparedStatement ps = Conexion.getConexion().prepareStatement(
                "SELECT id_ejercicio, nombre, grupo_muscular FROM ejercicios ORDER BY grupo_muscular, nombre");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next())
                l.add(new Object[]{rs.getInt(1), rs.getString(2), rs.getString(3)});
        } catch (SQLException e) { e.printStackTrace(); }
        return l;
    }

    /** Obtiene ejercicios de una rutina agrupados por día */
    public Map<String, List<Ejercicio>> obtenerPorDia(int idRutina) {
        Map<String, List<Ejercicio>> mapa = new LinkedHashMap<>();
        for (String d : DIAS) mapa.put(d, new ArrayList<>());
        String sql = "SELECT e.*, re.dia_semana, re.orden, re.id as re_id "
                   + "FROM rutina_ejercicios re JOIN ejercicios e ON re.id_ejercicio=e.id_ejercicio "
                   + "WHERE re.id_rutina=? ORDER BY re.dia_semana, re.orden";
        try (PreparedStatement ps = Conexion.getConexion().prepareStatement(sql)) {
            ps.setInt(1, idRutina);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Ejercicio ej = map(rs);
                ej.setDiaSemana(rs.getString("dia_semana"));
                ej.setOrden(rs.getInt("orden"));
                ej.setIdRutinaEjercicio(rs.getInt("re_id"));
                String dia = rs.getString("dia_semana");
                mapa.computeIfAbsent(dia, k -> new ArrayList<>()).add(ej);
            }
            rs.close();
        } catch (SQLException e) { e.printStackTrace(); }
        return mapa;
    }

    /** Agrega un ejercicio a una rutina en un día específico */
    public boolean agregarADia(int idRutina, int idEjercicio, String dia) {
        // Calcular siguiente orden
        int orden = 1;
        try (PreparedStatement ps = Conexion.getConexion().prepareStatement(
                "SELECT COALESCE(MAX(orden),0)+1 FROM rutina_ejercicios WHERE id_rutina=? AND dia_semana=?")) {
            ps.setInt(1, idRutina); ps.setString(2, dia);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) orden = rs.getInt(1);
            rs.close();
        } catch (SQLException e) {}
        try (PreparedStatement ps = Conexion.getConexion().prepareStatement(
                "INSERT INTO rutina_ejercicios(id_rutina,id_ejercicio,dia_semana,orden) VALUES(?,?,?,?)")) {
            ps.setInt(1, idRutina); ps.setInt(2, idEjercicio);
            ps.setString(3, dia); ps.setInt(4, orden);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    /** Elimina un ejercicio de una rutina por id de la tabla rutina_ejercicios */
    public boolean quitarDeRutina(int idRutinaEjercicio) {
        try (PreparedStatement ps = Conexion.getConexion().prepareStatement(
                "DELETE FROM rutina_ejercicios WHERE id=?")) {
            ps.setInt(1, idRutinaEjercicio);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    /** Mueve un ejercicio hacia arriba en el orden del día */
    public void subirOrden(int idRutinaEjercicio, int idRutina, String dia, int ordenActual) {
        if (ordenActual <= 1) return;
        try (PreparedStatement ps = Conexion.getConexion().prepareStatement(
                "UPDATE rutina_ejercicios SET orden=? WHERE id_rutina=? AND dia_semana=? AND orden=?")) {
            ps.setInt(1, ordenActual); ps.setInt(2, idRutina); ps.setString(3, dia); ps.setInt(4, ordenActual-1);
            ps.executeUpdate();
        } catch (SQLException e) {}
        try (PreparedStatement ps = Conexion.getConexion().prepareStatement(
                "UPDATE rutina_ejercicios SET orden=? WHERE id=?")) {
            ps.setInt(1, ordenActual-1); ps.setInt(2, idRutinaEjercicio); ps.executeUpdate();
        } catch (SQLException e) {}
    }

    public int insertar(Ejercicio ej) {
        try (PreparedStatement ps = Conexion.getConexion().prepareStatement(
                "INSERT INTO ejercicios(nombre,grupo_muscular,descripcion,series,repeticiones,descanso_segundos) VALUES(?,?,?,?,?,?)",
                Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1,ej.getNombre()); ps.setString(2,ej.getGrupoMuscular());
            ps.setString(3,ej.getDescripcion()); ps.setInt(4,ej.getSeries());
            ps.setInt(5,ej.getRepeticiones()); ps.setInt(6,ej.getDescansoSegundos());
            ps.executeUpdate();
            ResultSet g = ps.getGeneratedKeys(); int id = g.next()?g.getInt(1):-1; g.close(); return id;
        } catch (SQLException e) { e.printStackTrace(); return -1; }
    }

    private Ejercicio map(ResultSet rs) throws SQLException {
        Ejercicio e = new Ejercicio();
        e.setIdEjercicio(rs.getInt("id_ejercicio")); e.setNombre(rs.getString("nombre"));
        e.setGrupoMuscular(rs.getString("grupo_muscular")); e.setDescripcion(rs.getString("descripcion"));
        e.setSeries(rs.getInt("series")); e.setRepeticiones(rs.getInt("repeticiones"));
        e.setDescansoSegundos(rs.getInt("descanso_segundos")); return e;
    }
}
