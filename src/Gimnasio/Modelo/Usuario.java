package Gimnasio.Modelo;

public class Usuario {

    private int    idUsuario;
    private String nombreUsuario;
    private String contraseña;
    private boolean estado;
    private String ultimoAcceso;
    private int    intentosFallidos;
    private int    idRol;
    private String nombreRol;     // JOIN con roles
    // Datos de persona relacionada (cliente o entrenador)
    private String nombres;
    private String apellidos;

    public Usuario() {}

    /** Constructor mínimo usado en el Login */
    public Usuario(String usuario, String clave) {
        this.nombreUsuario = usuario;
        this.contraseña    = clave;
    }

    // Getters
    public int     getIdUsuario()       { return idUsuario; }
    public String  getNombreUsuario()   { return nombreUsuario; }
    public String  getContraseña()      { return contraseña; }
    public boolean isEstado()           { return estado; }
    public String  getUltimoAcceso()    { return ultimoAcceso; }
    public int     getIntentosFallidos(){ return intentosFallidos; }
    public int     getIdRol()           { return idRol; }
    public String  getNombreRol()       { return nombreRol; }
    public String  getNombres()         { return nombres; }
    public String  getApellidos()       { return apellidos; }
    /** Retorna el nombre del rol para comparaciones (ej. "ADMIN") */
    public String  getRol()             { return nombreRol; }

    // Setters
    public void setIdUsuario(int v)          { this.idUsuario = v; }
    public void setNombreUsuario(String v)   { this.nombreUsuario = v; }
    public void setContraseña(String v)      { this.contraseña = v; }
    public void setEstado(boolean v)         { this.estado = v; }
    public void setUltimoAcceso(String v)    { this.ultimoAcceso = v; }
    public void setIntentosFallidos(int v)   { this.intentosFallidos = v; }
    public void setIdRol(int v)              { this.idRol = v; }
    public void setNombreRol(String v)       { this.nombreRol = v; }
    public void setNombres(String v)         { this.nombres = v; }
    public void setApellidos(String v)       { this.apellidos = v; }
    // Alias para compatibilidad con código anterior
    public void setRol(String v)             { this.nombreRol = v; }
    public void setIdPersona(int v)          { /* no aplica en este modelo */ }
    public void setCedula(String v)          { /* no aplica en este modelo */ }
}
