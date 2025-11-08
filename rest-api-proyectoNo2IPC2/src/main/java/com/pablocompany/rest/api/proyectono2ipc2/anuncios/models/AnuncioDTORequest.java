/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pablocompany.rest.api.proyectono2ipc2.anuncios.models;

import java.io.InputStream;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;

/**
 *
 * @author pablo
 */
//Clase delegada para procesar los datos que vienen del Form data
public class AnuncioDTORequest {

    private String nombre;
    private String fechaCompra;
    private String url;
    private String texto;
    private InputStream foto;
    private FormDataContentDisposition formDetalle;
    private String codigoTipo;
    private String idUsuario;
    private String monto; 
    private String tipoTarifa; 
    

    public AnuncioDTORequest(String nombre, String fechaCompra, String url, String texto, InputStream foto, FormDataContentDisposition formDetalle, String codigoTipo, String idUsuario, String monto, String tipoTarifa) {
        this.nombre = nombre;
        this.fechaCompra = fechaCompra;
        this.url = url;
        this.texto = texto;
        this.foto = foto;
        this.formDetalle = formDetalle;
        this.codigoTipo = codigoTipo;
        this.idUsuario = idUsuario;
        this.monto = monto;
        this.tipoTarifa = tipoTarifa; 
    }

    public String getMonto() {
        return monto;
    }

    public void setMonto(String monto) {
        this.monto = monto;
    }

    public String getTipoTarifa() {
        return tipoTarifa;
    }

    public void setTipoTarifa(String tipoTarifa) {
        this.tipoTarifa = tipoTarifa;
    }
    
    

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFechaCompra() {
        return fechaCompra;
    }

    public void setFechaCompra(String fechaCompra) {
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

    public InputStream getFoto() {
        return foto;
    }

    public void setFoto(InputStream foto) {
        this.foto = foto;
    }

    public FormDataContentDisposition getFormDetalle() {
        return formDetalle;
    }

    public void setFormDetalle(FormDataContentDisposition formDetalle) {
        this.formDetalle = formDetalle;
    }

    public String getCodigoTipo() {
        return codigoTipo;
    }

    public void setCodigoTipo(String codigoTipo) {
        this.codigoTipo = codigoTipo;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    
    
    
}
