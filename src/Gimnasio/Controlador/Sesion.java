package Gimnasio.Controlador;
import Gimnasio.Conexion.Conexion;
import Gimnasio.Modelo.Usuario;
import java.sql.*;
import java.util.*;

public class Sesion {
    private static Usuario    usuarioActual;
    private static int        idClienteActual    = -1;
    private static int        idEntrenadorActual = -1;
    private static Set<String> modulosHabilitados = new HashSet<>();

    public boolean iniciarSesion(Usuario user) {
        String sql="SELECT u.id_usuario,u.usuario,u.estado,u.id_rol,r.nombre as rol "
                  +"FROM usuarios u JOIN roles r ON u.id_rol=r.id_rol "
                  +"WHERE u.usuario=? AND u.contrasena=? AND u.estado=1";
        try(PreparedStatement ps=Conexion.getConexion().prepareStatement(sql)){
            ps.setString(1,user.getNombreUsuario()); ps.setString(2,user.getContraseña());
            ResultSet rs=ps.executeQuery();
            if(rs.next()){
                user.setIdUsuario(rs.getInt("id_usuario")); user.setIdRol(rs.getInt("id_rol"));
                user.setNombreRol(rs.getString("rol")); user.setEstado(true);
                idClienteActual=-1; idEntrenadorActual=-1;
                // Actualizar estados de membresía antes de cargar permisos
                new PagoDAO().actualizarEstadosMembresia();
                resolverPersona(user);
                cargarPermisos(user.getIdUsuario());
                actualizarAcceso(user.getIdUsuario());
                usuarioActual=user; rs.close(); return true;
            }
            rs.close();
        } catch(Exception e){System.err.println("Login error: "+e.getMessage());}
        return false;
    }

    private void resolverPersona(Usuario u){
        try(PreparedStatement ps=Conexion.getConexion().prepareStatement(
                "SELECT id_cliente,nombres,apellidos FROM clientes WHERE id_usuario=?")){
            ps.setInt(1,u.getIdUsuario()); ResultSet rs=ps.executeQuery();
            if(rs.next()){idClienteActual=rs.getInt("id_cliente"); u.setNombres(rs.getString("nombres")); u.setApellidos(rs.getString("apellidos")); rs.close(); return;}
            rs.close();
        } catch(SQLException ex){}
        try(PreparedStatement ps=Conexion.getConexion().prepareStatement(
                "SELECT id_entrenador,nombres,apellidos FROM entrenadores WHERE id_usuario=?")){
            ps.setInt(1,u.getIdUsuario()); ResultSet rs=ps.executeQuery();
            if(rs.next()){idEntrenadorActual=rs.getInt("id_entrenador"); u.setNombres(rs.getString("nombres")); u.setApellidos(rs.getString("apellidos")); rs.close(); return;}
            rs.close();
        } catch(SQLException ex){}
        u.setNombres(u.getNombreUsuario()); u.setApellidos("");
    }

    private void cargarPermisos(int idUsuario){
        modulosHabilitados.clear();
        try(PreparedStatement ps=Conexion.getConexion().prepareStatement(
                "SELECT modulo FROM permisos_modulo WHERE id_usuario=? AND habilitado=1")){
            ps.setInt(1,idUsuario); ResultSet rs=ps.executeQuery();
            while(rs.next()) modulosHabilitados.add(rs.getString("modulo")); rs.close();
        } catch(SQLException e){}
    }

    private void actualizarAcceso(int id){
        try(PreparedStatement ps=Conexion.getConexion().prepareStatement(
                "UPDATE usuarios SET ultimo_acceso=CURRENT_TIMESTAMP WHERE id_usuario=?")){
            ps.setInt(1,id); ps.executeUpdate();
        } catch(SQLException e){}
    }

    public static Usuario  getUsuarioActual()    { return usuarioActual; }
    public static int      getIdCliente()        { return idClienteActual; }
    public static int      getIdEntrenador()     { return idEntrenadorActual; }

    public static String getNombreCompleto(){
        if(usuarioActual==null) return "Invitado";
        String n=usuarioActual.getNombres()!=null?usuarioActual.getNombres():"";
        String a=usuarioActual.getApellidos()!=null?usuarioActual.getApellidos():"";
        String full=(n+" "+a).trim(); return full.isEmpty()?usuarioActual.getNombreUsuario():full;
    }

    public static String  getRol()          { return usuarioActual!=null?usuarioActual.getRol():""; }
    /** Solo ADMIN (no Entrenador, no Recepcionista) */
    public static boolean esAdmin()         { return "ADMIN".equalsIgnoreCase(getRol()); }
    public static boolean esEntrenador()    { return "ENTRENADOR".equalsIgnoreCase(getRol()); }
    public static boolean esRecepcionista() { return "RECEPCIONISTA".equalsIgnoreCase(getRol()); }
    public static boolean esCliente()       { return "CLIENTE".equalsIgnoreCase(getRol()); }
    /** Personal del gym con acceso a panel admin */
    public static boolean esPersonalAdmin() { return esAdmin()||esRecepcionista(); }

    public static boolean tienePermiso(String modulo){
        if(esAdmin()) return true;
        return modulosHabilitados.contains(modulo);
    }
    public static Set<String> getModulosHabilitados(){ return Collections.unmodifiableSet(modulosHabilitados); }
    public static void cerrarSesion(){ usuarioActual=null; idClienteActual=-1; idEntrenadorActual=-1; modulosHabilitados.clear(); }
}
