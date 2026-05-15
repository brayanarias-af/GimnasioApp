/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Gimnasio.Modelo;

/**
 *
 * @author brian
 */
public class ProgresoFisico {

   
    private String codigoProgreso;
    private double peso;
    private double porcentajeGrasa;
    private double masaMuscular;
    private double imc;
    private String fechaRegistro;
    private String observaciones;
    private Cliente cliente;
    
     public ProgresoFisico() {
    }

    public ProgresoFisico(String codigoProgreso, double peso, double porcentajeGrasa, double masaMuscular, double imc, String fechaRegistro, String observaciones) {
        this.codigoProgreso = codigoProgreso;
        this.peso = peso;
        this.porcentajeGrasa = porcentajeGrasa;
        this.masaMuscular = masaMuscular;
        this.imc = imc;
        this.fechaRegistro = fechaRegistro;
        this.observaciones = observaciones;
    }

    public String getCodigoProgreso() {
        return codigoProgreso;
    }

    public double getPeso() {
        return peso;
    }

    public double getPorcentajeGrasa() {
        return porcentajeGrasa;
    }

    public double getMasaMuscular() {
        return masaMuscular;
    }

    public double getImc() {
        return imc;
    }

    public String getFechaRegistro() {
        return fechaRegistro;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setCodigoProgreso(String codigoProgreso) {
        this.codigoProgreso = codigoProgreso;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }

    public void setPorcentajeGrasa(double porcentajeGrasa) {
        this.porcentajeGrasa = porcentajeGrasa;
    }

    public void setMasaMuscular(double masaMuscular) {
        this.masaMuscular = masaMuscular;
    }

    public void setImc(double imc) {
        this.imc = imc;
    }

    public void setFechaRegistro(String fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
    
}
