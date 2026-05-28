package Gimnasio.Modelo;
public class Rutina {
    private int idRutina, duracionSemanas, idEntrenador;
    private String nombreRutina, objetivo, nivel, descripcion, nombreEntrenador;

    public Rutina() {}

    public Rutina(int idRutina, String nombreRutina, String objetivo, String nivel,
                  int duracionSemanas, String descripcion, int idEntrenador) {
        this.idRutina       = idRutina;
        this.nombreRutina   = nombreRutina;
        this.objetivo       = objetivo;
        this.nivel          = nivel;
        this.duracionSemanas = duracionSemanas;
        this.descripcion    = descripcion;
        this.idEntrenador   = idEntrenador;
    }
    public int getIdRutina()            { return idRutina; }
    public int getDuracionSemanas()     { return duracionSemanas; }
    public int getIdEntrenador()        { return idEntrenador; }
    public String getNombreRutina()     { return nombreRutina; }
    public String getObjetivo()         { return objetivo; }
    public String getNivel()            { return nivel; }
    public String getDescripcion()      { return descripcion; }
    public String getNombreEntrenador() { return nombreEntrenador; }
    public void setIdRutina(int v)              { idRutina = v; }
    public void setDuracionSemanas(int v)       { duracionSemanas = v; }
    public void setIdEntrenador(int v)          { idEntrenador = v; }
    public void setNombreRutina(String v)       { nombreRutina = v; }
    public void setObjetivo(String v)           { objetivo = v; }
    public void setNivel(String v)              { nivel = v; }
    public void setDescripcion(String v)        { descripcion = v; }
    public void setNombreEntrenador(String v)   { nombreEntrenador = v; }
}
