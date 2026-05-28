package Gimnasio.Modelo;
public class Ejercicio {
    private int    idEjercicio, series, repeticiones, descansoSegundos, orden, idRutinaEjercicio;
    private String nombre, grupoMuscular, descripcion, diaSemana;

    public Ejercicio() {}

    public Ejercicio(String nombre, String grupoMuscular, String descripcion,
                     int series, int repeticiones, int descansoSegundos,
                     int orden, String diaSemana) {
        this.nombre            = nombre;
        this.grupoMuscular     = grupoMuscular;
        this.descripcion       = descripcion;
        this.series            = series;
        this.repeticiones      = repeticiones;
        this.descansoSegundos  = descansoSegundos;
        this.orden             = orden;
        this.diaSemana         = diaSemana;
    }
    public int    getIdEjercicio()       { return idEjercicio; }
    public int    getSeries()            { return series; }
    public int    getRepeticiones()      { return repeticiones; }
    public int    getDescansoSegundos()  { return descansoSegundos; }
    public int    getOrden()             { return orden; }
    public int    getIdRutinaEjercicio() { return idRutinaEjercicio; }
    public String getNombre()            { return nombre; }
    public String getGrupoMuscular()     { return grupoMuscular; }
    public String getDescripcion()       { return descripcion; }
    public String getDiaSemana()         { return diaSemana; }
    public void setIdEjercicio(int v)        { idEjercicio = v; }
    public void setSeries(int v)             { series = v; }
    public void setRepeticiones(int v)       { repeticiones = v; }
    public void setDescansoSegundos(int v)   { descansoSegundos = v; }
    public void setOrden(int v)              { orden = v; }
    public void setIdRutinaEjercicio(int v)  { idRutinaEjercicio = v; }
    public void setNombre(String v)          { nombre = v; }
    public void setGrupoMuscular(String v)   { grupoMuscular = v; }
    public void setDescripcion(String v)     { descripcion = v; }
    public void setDiaSemana(String v)       { diaSemana = v; }
    @Override public String toString()       { return nombre != null ? nombre : ""; }
}
