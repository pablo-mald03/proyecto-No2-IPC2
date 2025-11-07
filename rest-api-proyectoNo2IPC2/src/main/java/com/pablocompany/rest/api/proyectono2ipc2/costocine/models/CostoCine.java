/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pablocompany.rest.api.proyectono2ipc2.costocine.models;

import java.time.LocalDate;

/**
 *
 * @author pablo
 */
//Clase delegada para representar al objeto que genera la trascendencia de precios de cine
public class CostoCine {
    
    private double costo;
    private LocalDate fechaModificacion;
    private String codigoCine;

    public CostoCine(double costo, LocalDate fechaModificacion, String codigoCine) {
        this.costo = costo;
        this.fechaModificacion = fechaModificacion;
        this.codigoCine = codigoCine;
    }
    
    public double getCosto() {
        return costo;
    }

    public void setCosto(double costo) {
        this.costo = costo;
    }

    public LocalDate getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(LocalDate fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public String getCodigoCine() {
        return codigoCine;
    }

    public void setCodigoCine(String codigoCine) {
        this.codigoCine = codigoCine;
    }
    
    
    
}
