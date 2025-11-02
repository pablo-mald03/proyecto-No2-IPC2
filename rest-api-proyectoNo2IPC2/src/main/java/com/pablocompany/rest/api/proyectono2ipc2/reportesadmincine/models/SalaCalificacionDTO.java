/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pablocompany.rest.api.proyectono2ipc2.reportesadmincine.models;

import java.time.LocalDate;

/**
 *
 * @author pablo
 */
//Clase delegada para operar con los datos de la sala en donde se hizo la calificacion 
public class SalaCalificacionDTO {

    private String idUsuario;
    private int calificacion;
    private LocalDate fechaPosteo;

    public SalaCalificacionDTO(String idUsuario, int calificacion, LocalDate fechaPosteo) {
        this.idUsuario = idUsuario;
        this.calificacion = calificacion;
        this.fechaPosteo = fechaPosteo;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(int calificacion) {
        this.calificacion = calificacion;
    }

    public LocalDate getFechaPosteo() {
        return fechaPosteo;
    }

    public void setFechaPosteo(LocalDate fechaPosteo) {
        this.fechaPosteo = fechaPosteo;
    }

}
