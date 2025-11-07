/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pablocompany.rest.api.proyectono2ipc2.reportesadminsistema.models;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author pablo
 */
//Clase delegada para generar el listado de los costos que han tenido los cines 
public class CineCostoDTO {

    private String codigo;
    private String nombre;
    private double montoOcultacion;
    private LocalDate fechaCreacion;
    private List<CostoModificacionCineDTO> costosAsociados;

    public CineCostoDTO(String codigo, String nombre, double montoOcultacion, LocalDate fechaCreacion) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.montoOcultacion = montoOcultacion;
        this.fechaCreacion = fechaCreacion;
        this.costosAsociados = new ArrayList<>();
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

    public LocalDate getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDate fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public List<CostoModificacionCineDTO> getCostosAsociados() {
        return costosAsociados;
    }

    public void setCostosAsociados(List<CostoModificacionCineDTO> costosAsociados) {
        this.costosAsociados = costosAsociados;
    }

}
