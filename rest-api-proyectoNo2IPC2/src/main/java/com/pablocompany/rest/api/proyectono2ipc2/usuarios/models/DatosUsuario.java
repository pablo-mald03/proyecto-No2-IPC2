/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pablocompany.rest.api.proyectono2ipc2.usuarios.models;

import com.pablocompany.rest.api.proyectono2ipc2.excepciones.ErrorInesperadoException;
import com.pablocompany.rest.api.proyectono2ipc2.usuarios.services.FotoPerfilService;

/**
 *
 * @author pablo
 */
//Clase delegada para retornar desde backend sobre los datos del usuario
public class DatosUsuario {

    private String id;
    private String correo;
    private String nombre;
    private byte[] foto;
    private String telefono;
    private String pais;
    private String identificacion;
    private String rol;

    public DatosUsuario(String id, String correo, String nombre, byte[] foto, String telefono, String pais, String identificacion, String rol) {
        this.id = id;
        this.correo = correo;
        this.nombre = nombre;
        this.foto = foto;
        this.telefono = telefono;
        this.pais = pais;
        this.identificacion = identificacion;
        this.rol = rol;
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

    public byte[] getFoto() {
        return foto;
    }

    public void setFoto(byte[] foto) {
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

    //Metodo especalizado para poder retornar solo la referencia base64 de la foto
    public String getFotoPerfil() {
        FotoPerfilService fotoPerfilService = new FotoPerfilService();
        try {
            return fotoPerfilService.convertirFotoBase64(this.foto);
        } catch (ErrorInesperadoException ex) {
            return "";
        }
    }

}
