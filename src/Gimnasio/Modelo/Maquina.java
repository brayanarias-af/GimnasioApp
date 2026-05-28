package Gimnasio.Modelo;
public class Maquina {
    private int idMaquina;
    private String nombre, tipo, marca, modelo, serial, estado, fechaMantenimiento;

    public Maquina() {}

    public Maquina(String nombre, String tipo, String marca, String modelo,
                   String serial, String estado, String fechaMantenimiento) {
        this.nombre              = nombre;
        this.tipo                = tipo;
        this.marca               = marca;
        this.modelo              = modelo;
        this.serial              = serial;
        this.estado              = estado;
        this.fechaMantenimiento  = fechaMantenimiento;
    }
    public int getIdMaquina()            { return idMaquina; }
    public String getNombre()            { return nombre; }
    public String getTipo()              { return tipo; }
    public String getMarca()             { return marca; }
    public String getModelo()            { return modelo; }
    public String getSerial()            { return serial; }
    public String getEstado()            { return estado; }
    public String getFechaMantenimiento(){ return fechaMantenimiento; }
    public void setIdMaquina(int v)              { idMaquina = v; }
    public void setNombre(String v)              { nombre = v; }
    public void setTipo(String v)                { tipo = v; }
    public void setMarca(String v)               { marca = v; }
    public void setModelo(String v)              { modelo = v; }
    public void setSerial(String v)              { serial = v; }
    public void setEstado(String v)              { estado = v; }
    public void setFechaMantenimiento(String v)  { fechaMantenimiento = v; }
}
