/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pablocompany.rest.api.proyectono2ipc2.cine.dtos;

import com.pablocompany.rest.api.proyectono2ipc2.cine.models.Cine;

/**
 *
 * @author pablo
 */
//Clase delegada para retornar los atributos del objeto necesarios que son editables
public class CineResponse {

    private String codigo;
    private String nombre;
    private double montoOcultacion;
    private String descripcion;
    private String ubicacion;

    public CineResponse(Cine cineDatos) {
        this.codigo = cineDatos.getCodigo();
        this.nombre = cineDatos.getNombre();
        this.montoOcultacion = cineDatos.getMontoOcultacion();
        this.descripcion = cineDatos.getDescripcion();
        this.ubicacion = cineDatos.getUbicacion();
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

    public double getMontoOcultacion() {
        return montoOcultacion;
    }

    public void setMontoOcultacion(double montoOcultacion) {
        this.montoOcultacion = montoOcultacion;
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
