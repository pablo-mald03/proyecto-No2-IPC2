/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pablocompany.rest.api.proyectono2ipc2.reportesadmincine.models;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author pablo
 */
//Clase delegada para manejar el objeto de Reporte de boletos vendidos 
public class ReporteBoletosVendidosDTO {
    
    
   private String codigo;
   private String cineAsociado;
   private String nombre;
   private String ubicacion;
   private double total;
   private List< UsuarioBoletosCompradosDTO> usuarios;

    public ReporteBoletosVendidosDTO(String codigo, String cineAsociado, String nombre, String ubicacion, double total) {
        this.codigo = codigo;
        this.cineAsociado = cineAsociado;
        this.nombre = nombre;
        this.ubicacion = ubicacion;
        this.total = total;
        this.usuarios = new ArrayList<>();
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

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public List<UsuarioBoletosCompradosDTO> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<UsuarioBoletosCompradosDTO> usuarios) {
        this.usuarios = usuarios;
    }
   
   
   
    
}
