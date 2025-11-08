/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pablocompany.rest.api.proyectono2ipc2.anuncios.models;

import java.time.LocalDate;

/**
 *
 * @author pablo
 */
//Clase delegada para poder generar los reportes de anuncios Objeto utilizado para la web y para backend
public class AnuncioRegistradoDTO {

    private String codigo;
    private boolean estado;
    private String nombre;
    private boolean caducacion;
    private LocalDate fechaExpiracion;
    private LocalDate fechaCompra;
    private String url;
    private String texto;
    private String foto;
    private int codigoTipo;
    private String idUsuario;
    private String nombreUsuario;

    public AnuncioRegistradoDTO(String codigo, boolean estado, String nombre, boolean caducacion, LocalDate fechaExpiracion, LocalDate fechaCompra, String url, String texto, String foto, int codigoTipo, String idUsuario, String nombreUsuario) {
        this.codigo = codigo;
        this.estado = estado;
        this.nombre = nombre;
        this.caducacion = caducacion;
        this.fechaExpiracion = fechaExpiracion;
        this.fechaCompra = fechaCompra;
        this.url = url;
        this.texto = texto;
        this.foto = foto;
        this.codigoTipo = codigoTipo;
        this.idUsuario = idUsuario;
        this.nombreUsuario = nombreUsuario;
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
