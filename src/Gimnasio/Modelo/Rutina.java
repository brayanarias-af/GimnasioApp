package Gimnasio.Modelo;

public class Rutina {

    private int    idRutina;
    private String nombreRutina;
    private String objetivo;
    private String nivel;
    private int    duracionSemanas;
    private String descripcion;
    private int    idEntrenador;
    private String nombreEntrenador; // JOIN

    public Rutina() {}

    public Rutina(int idRutina, String nombre, String objetivo, String nivel,
                  int duracionSemanas, String descripcion, int idEntrenador) {
        this.idRutina = idRutina; this.nombreRutina = nombre; this.objetivo = objetivo;
        this.nivel = nivel; this.duracionSemanas = duracionSemanas;
        this.descripcion = descripcion; this.idEntrenador = idEntrenador;
    }

    public int    getIdRutina()         { return idRutina; }
    public String getNombreRutina()     { return nombreRutina; }
    public String getObjetivo()         { return objetivo; }
    public String getNivel()            { return nivel; }
    public int    getDuracionSemanas()  { return duracionSemanas; }
    public String getDescripcion()      { return descripcion; }
    public int    getIdEntrenador()     { return idEntrenador; }
    public String getNombreEntrenador() { return nombreEntrenador; }

    public void setIdRutina(int v)              { this.idRutina = v; }
    public void setNombreRutina(String v)       { this.nombreRutina = v; }
    public void setObjetivo(String v)           { this.objetivo = v; }
    public void setNivel(String v)              { this.nivel = v; }
    public void setDuracionSemanas(int v)       { this.duracionSemanas = v; }
    public void setDescripcion(String v)        { this.descripcion = v; }
    public void setIdEntrenador(int v)          { this.idEntrenador = v; }
    public void setNombreEntrenador(String v)   { this.nombreEntrenador = v; }
}
