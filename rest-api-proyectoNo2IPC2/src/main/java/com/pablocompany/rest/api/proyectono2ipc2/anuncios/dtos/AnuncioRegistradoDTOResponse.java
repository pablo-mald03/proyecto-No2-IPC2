/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pablocompany.rest.api.proyectono2ipc2.anuncios.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.pablocompany.rest.api.proyectono2ipc2.anuncios.models.AnuncioRegistradoDTO;
import java.time.LocalDate;

/**
 *
 * @author pablo
 */
//Clase delegada para generar el response para poderlo enviar
public class AnuncioRegistradoDTOResponse {
    
    private String codigo;
    private boolean estado;
    private String nombre;
    private boolean caducacion;
    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate fechaExpiracion;
    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate fechaCompra;
    private String url;
    private String texto;
    private String foto;
    private int codigoTipo;
    private String idUsuario;
    private String nombreUsuario;

    public AnuncioRegistradoDTOResponse(AnuncioRegistradoDTO anuncioRegistrado) {
        this.codigo = anuncioRegistrado.getCodigo();
        this.estado = anuncioRegistrado.isEstado();
        this.nombre = anuncioRegistrado.getNombre();
        this.caducacion = anuncioRegistrado.isCaducacion();
        this.fechaExpiracion = anuncioRegistrado.getFechaExpiracion();
        this.fechaCompra = anuncioRegistrado.getFechaCompra();
        this.url = anuncioRegistrado.getUrl();
        this.texto = anuncioRegistrado.getTexto();
        this.foto = anuncioRegistrado.getFoto();
        this.codigoTipo = anuncioRegistrado.getCodigoTipo();
        this.idUsuario = anuncioRegistrado.getIdUsuario();
        this.nombreUsuario = anuncioRegistrado.getNombreUsuario();
    }
    
    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public boolean isCaducacion() {
        return caducacion;
    }

    public void setCaducacion(boolean caducacion) {
        this.caducacion = caducacion;
    }

    public LocalDate getFechaExpiracion() {
        return fechaExpiracion;
    }

    public void setFechaExpiracion(LocalDate fechaExpiracion) {
        this.fechaExpiracion = fechaExpiracion;
    }

    public LocalDate getFechaCompra() {
        return fechaCompra;
    }

    public void setFechaCompra(LocalDate fechaCompra) {
        this.fechaCompra = fechaCompra;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public int getCodigoTipo() {
        return codigoTipo;
    }

    public void setCodigoTipo(int codigoTipo) {
        this.codigoTipo = codigoTipo;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }
    
    
    
    
    
}
