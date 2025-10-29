/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pablocompany.rest.api.proyectono2ipc2.usuarios.dtos;

import com.pablocompany.rest.api.proyectono2ipc2.usuarios.models.DatosUsuario;

/**
 *
 * @author pablo
 */

//Clase que permite retornar los datos sobre la informacion perfil del usuario 
public class UsuarioDatosResponse {
    
    private String id;
    private String correo;
    private String nombre;
    private String foto;
    private String telefono;
    private String pais;
    private String identificacion;
    private String rol;

    public UsuarioDatosResponse(DatosUsuario datosUsuario) {
        this.id = datosUsuario.getId();
        this.correo = datosUsuario.getCorreo();
        this.nombre = datosUsuario.getNombre();
        this.foto = datosUsuario.getFotoPerfil();
        this.telefono = datosUsuario.getTelefono();
        this.pais = datosUsuario.getPais();
        this.identificacion = datosUsuario.getIdentificacion();
        this.rol = datosUsuario.getRol();
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

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
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

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }
    
    
}
