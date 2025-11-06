/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pablocompany.rest.api.proyectono2ipc2.reportesadminsistema.models;

import com.pablocompany.rest.api.proyectono2ipc2.reportesadmincine.models.SalaComentarioDTO;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author pablo
 */
//Clase delegada para poder operar con el modelo de las salas mas comentadas
public class SalaMasComenadaDTO {

    private String codigo;
    private String nombre;
    private String cineAsociado;
    private int filas;
    private int columnas;
    private String ubicacion;
    private List<SalaComentarioDTO> comentarios;

    public SalaMasComenadaDTO(String codigo, String nombre, String cineAsociado, int filas, int columnas, String ubicacion) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.cineAsociado = cineAsociado;
        this.filas = filas;
        this.columnas = columnas;
        this.ubicacion = ubicacion;
        this.comentarios = new ArrayList<>();
    }

    public String getCineAsociado() {
        return cineAsociado;
    }

    public void setCineAsociado(String cineAsociado) {
        this.cineAsociado = cineAsociado;
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

    public List<SalaComentarioDTO> getComentarios() {
        return comentarios;
    }

    public void setComentarios(List<SalaComentarioDTO> comentarios) {
        this.comentarios = comentarios;
    }

}
