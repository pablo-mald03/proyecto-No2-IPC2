/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pablocompany.rest.api.proyectono2ipc2.administradorsistema.models;

import java.time.LocalDate;

/**
 *
 * @author pablo
 */
public class PagoBloqueoAnuncios {

    private boolean estado;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private String codigoCine;

    public PagoBloqueoAnuncios(boolean estado, LocalDate fechaInicio, String codigoCine) {
        this.estado = estado;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaInicio.plusDays(5);
        this.codigoCine = codigoCine;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public String getCodigoCine() {
        return codigoCine;
    }

    public void setCodigoCine(String codigoCine) {
        this.codigoCine = codigoCine;
    }

}
