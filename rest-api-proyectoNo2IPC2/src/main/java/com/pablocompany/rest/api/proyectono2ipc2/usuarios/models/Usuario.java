/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pablocompany.rest.api.proyectono2ipc2.usuarios.models;

import java.io.InputStream;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;

/**
 *
 * @author pablo
 */

//Modelo encargado de manejar el request obtenido
public class Usuario {
    
    private String id;
    private String correo;
    private String nombre;
    private String password;
    private String telefono;
    private String pais;
    private String identificacion;
    private String codigoRol;
    
    private InputStream foto; 
    
    private FormDataContentDisposition formDetalle; 

    public Usuario(String id, String correo, String nombre, String password, String telefono, String pais, String identificacion, String codigoRol, InputStream foto, FormDataContentDisposition formDetalle) {
        this.id = id;
        this.correo = correo;
        this.nombre = nombre;
        this.password = password;
        this.telefono = telefono;
        this.pais = pais;
        this.identificacion = identificacion;
        this.codigoRol = codigoRol;
        this.foto = foto;
        this.formDetalle = formDetalle;
    }
    

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }

    public String getCodigoRol() {
        return codigoRol;
    }

    public void setCodigoRol(String codigoRol) {
        this.codigoRol = codigoRol;
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
    
    
    
}
