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
//Sub-clase que esta contenida dentro del reporte de salas comentadas
public class SalaComentarioDTO {
    
    private String idUsuario;
    private String contenido; 
    private LocalDate fechaPosteo;

    public SalaComentarioDTO(String idUsuario, String contenido, LocalDate fechaPosteo) {
        this.idUsuario = idUsuario;
        this.contenido = contenido;
        this.fechaPosteo = fechaPosteo;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public LocalDate getFechaPosteo() {
        return fechaPosteo;
    }

    public void setFechaPosteo(LocalDate fechaPosteo) {
        this.fechaPosteo = fechaPosteo;
    }
    
}
