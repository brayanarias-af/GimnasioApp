package Gimnasio.Modelo;
public class Membresia {
    private int idMembresia, duracionDias;
    private double precio;
    private String nombre, descripcion;

    public Membresia() {}

    public Membresia(int idMembresia, String nombre, double precio, int duracionDias, String descripcion) {
        this.idMembresia  = idMembresia;
        this.nombre       = nombre;
        this.precio       = precio;
        this.duracionDias = duracionDias;
        this.descripcion  = descripcion;
    }
    public int getIdMembresia()     { return idMembresia; }
    public int getDuracionDias()    { return duracionDias; }
    public double getPrecio()       { return precio; }
    public String getNombre()       { return nombre; }
    public String getDescripcion()  { return descripcion; }
    public void setIdMembresia(int v)    { idMembresia = v; }
    public void setDuracionDias(int v)   { duracionDias = v; }
    public void setPrecio(double v)      { precio = v; }
    public void setNombre(String v)      { nombre = v; }
    public void setDescripcion(String v) { descripcion = v; }
}
