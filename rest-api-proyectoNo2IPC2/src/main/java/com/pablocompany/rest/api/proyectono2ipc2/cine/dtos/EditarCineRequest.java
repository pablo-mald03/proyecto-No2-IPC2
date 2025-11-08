/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pablocompany.rest.api.proyectono2ipc2.cine.dtos;


/**
 *
 * @author pablo
 */
//Clase utilizada para poder procesar el request que se enviara para editar el cine 
public class EditarCineRequest {

    private String codigo;
    private String nombre;
    private String montoOcultacion;
    private String descripcion;
    private String ubicacion;

    public EditarCineRequest() {
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

    public String getMontoOcultacion() {
        return montoOcultacion;
    }

    public void setMontoOcultacion(String montoOcultacion) {
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
