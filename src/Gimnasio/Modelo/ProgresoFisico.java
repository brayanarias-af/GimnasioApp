package Gimnasio.Modelo;
public class ProgresoFisico {
    private int idProgreso, idCliente;
    private String fechaRegistro, observaciones, nombreCliente;
    private double peso, porcentajeGrasa, masaMuscular, imc;

    public ProgresoFisico() {}

    public ProgresoFisico(int idCliente, String fechaRegistro, double peso,
                          double porcentajeGrasa, double masaMuscular, double imc,
                          String observaciones) {
        this.idCliente       = idCliente;
        this.fechaRegistro   = fechaRegistro;
        this.peso            = peso;
        this.porcentajeGrasa = porcentajeGrasa;
        this.masaMuscular    = masaMuscular;
        this.imc             = imc;
        this.observaciones   = observaciones;
    }
    public int getIdProgreso()       { return idProgreso; }
    public int getIdCliente()        { return idCliente; }
    public String getFechaRegistro() { return fechaRegistro; }
    public String getObservaciones() { return observaciones; }
    public String getNombreCliente() { return nombreCliente; }
    public double getPeso()          { return peso; }
    public double getPorcentajeGrasa(){ return porcentajeGrasa; }
    public double getMasaMuscular()  { return masaMuscular; }
    public double getImc()           { return imc; }
    public void setIdProgreso(int v)          { idProgreso = v; }
    public void setIdCliente(int v)           { idCliente = v; }
    public void setFechaRegistro(String v)    { fechaRegistro = v; }
    public void setObservaciones(String v)    { observaciones = v; }
    public void setNombreCliente(String v)    { nombreCliente = v; }
    public void setPeso(double v)             { peso = v; }
    public void setPorcentajeGrasa(double v)  { porcentajeGrasa = v; }
    public void setMasaMuscular(double v)     { masaMuscular = v; }
    public void setImc(double v)              { imc = v; }
}
