/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pablocompany.rest.api.proyectono2ipc2.reportesadmincine.models;

/**
 *
 * @author pablo
 */
//Clase delegada para poder gestionar el modelo de retorno de salas comentadas
public class ReporteSalasComentadasDTO {
    
    private String codigo;
    private String cineAsociado;
    private String nombre; 
    private int filas;
    private int columnas;
    private String ubicacion;
    private SalaComentarioDTO comentarios;

    public ReporteSalasComentadasDTO(String codigo, String cineAsociado, String nombre, int filas, int columnas, String ubicacion, SalaComentarioDTO comentarios) {
        this.codigo = codigo;
        this.cineAsociado = cineAsociado;
        this.nombre = nombre;
        this.filas = filas;
        this.columnas = columnas;
        this.ubicacion = ubicacion;
        this.comentarios = comentarios;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getCineAsociado() {
        return cineAsociado;
    }

    public void setCineAsociado(String cineAsociado) {
        this.cineAsociado = cineAsociado;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getFilas() {
        return filas;
    }

    public void setFilas(int filas) {
        this.filas = filas;
    }

    public int getColumnas() {
        return columnas;
    }

    public void setColumnas(int columnas) {
        this.columnas = columnas;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public SalaComentarioDTO getComentarios() {
        return comentarios;
    }

    public void setComentarios(SalaComentarioDTO comentarios) {
        this.comentarios = comentarios;
    }
    
    
    
}
