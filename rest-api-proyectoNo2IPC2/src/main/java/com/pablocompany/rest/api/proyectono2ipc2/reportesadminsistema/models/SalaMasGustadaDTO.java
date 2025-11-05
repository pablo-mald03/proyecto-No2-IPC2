/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pablocompany.rest.api.proyectono2ipc2.reportesadminsistema.models;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author pablo
 */
//Clase delegada para poder operar con el objeto de reportes de salas gustadas 
public class SalaMasGustadaDTO {

    private String codigo;
    private String nombre;
    private int filas;
    private int columnas;
    private String ubicacion;
    private double totalVentasBoleto;
    private List<UsuarioReporteDTO> usuarios;

    public SalaMasGustadaDTO(String codigo, String nombre, int filas, int columnas, String ubicacion, double totalVentasBoleto) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.filas = filas;
        this.columnas = columnas;
        this.ubicacion = ubicacion;
        this.totalVentasBoleto = totalVentasBoleto;
        this.usuarios = new ArrayList<>();
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

    public double getTotalVentasBoleto() {
        return totalVentasBoleto;
    }

    public void setTotalVentasBoleto(double totalVentasBoleto) {
        this.totalVentasBoleto = totalVentasBoleto;
    }

    public List<UsuarioReporteDTO> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<UsuarioReporteDTO> usuarios) {
        this.usuarios = usuarios;
    }

}
