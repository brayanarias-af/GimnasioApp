package Gimnasio.Modelo;
public class Usuario {
    private int idUsuario; private String nombreUsuario, contraseña, nombreRol, nombres, apellidos;
    private boolean estado; private int idRol;
    public Usuario() {}
    public Usuario(String u, String c) { this.nombreUsuario = u; this.contraseña = c; }
    public int getIdUsuario()          { return idUsuario; }
    public String getNombreUsuario()   { return nombreUsuario; }
    public String getContraseña()      { return contraseña; }
    public boolean isEstado()          { return estado; }
    public int getIdRol()              { return idRol; }
    public String getNombreRol()       { return nombreRol; }
    public String getRol()             { return nombreRol; }
    public String getNombres()         { return nombres; }
    public String getApellidos()       { return apellidos; }
    public void setIdUsuario(int v)          { idUsuario = v; }
    public void setNombreUsuario(String v)   { nombreUsuario = v; }
    public void setContraseña(String v)      { contraseña = v; }
    public void setEstado(boolean v)         { estado = v; }
    public void setIdRol(int v)              { idRol = v; }
    public void setNombreRol(String v)       { nombreRol = v; }
    public void setRol(String v)             { nombreRol = v; }
    public void setNombres(String v)         { nombres = v; }
    public void setApellidos(String v)       { apellidos = v; }
}
