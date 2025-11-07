/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pablocompany.rest.api.proyectono2ipc2.cine.models;

import java.time.LocalDate;

/**
 *
 * @author pablo
 */
//Clase delegada para poder interactuar con el objeto para poder crear los cines 
public class Cine {

    private String codigo;
    private String nombre;
    private boolean estadoAnuncio;
    private double montoOcultacion;
    private LocalDate fechaCreacion;
    private String descripcion;
    private String ubicacion;

    public Cine(String codigo, String nombre, boolean estadoAnuncio, double montoOcultacion, LocalDate fechaCreacion, String descripcion, String ubicacion) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.estadoAnuncio = estadoAnuncio;
        this.montoOcultacion = montoOcultacion;
        this.fechaCreacion = fechaCreacion;
        this.descripcion = descripcion;
        this.ubicacion = ubicacion;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public boolean isEstadoAnuncio() {
        return estadoAnuncio;
    }

    public void setEstadoAnuncio(boolean estadoAnuncio) {
        this.estadoAnuncio = estadoAnuncio;
    }

    public double getMontoOcultacion() {
        return montoOcultacion;
    }

    public void setMontoOcultacion(double montoOcultacion) {
        this.montoOcultacion = montoOcultacion;
    }

    public LocalDate getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDate fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

}
