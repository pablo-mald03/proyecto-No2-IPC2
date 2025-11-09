/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pablocompany.rest.api.proyectono2ipc2.administradorsistema.models;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pablocompany.rest.api.proyectono2ipc2.cine.models.CineAsociadoDTO;
import java.io.InputStream;
import java.util.List;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;

/**
 *
 * @author pablo
 */
//Clase delegada para poder operar con el objeto para poder crear un neuvo adiministrador de cine 
public class AdministradorCineDTO {

    private String telefono;
    private String password;
    private String pais;
    private String nombre;
    private String identificacion;
    private String id;
    private String correo;
    private InputStream foto;
    private FormDataContentDisposition fotoDetalle;
    private String confirmpassword;
    private String codigosCine;

    public AdministradorCineDTO(String telefono, String password, String pais, String nombre, String identificacion, String id, String correo, InputStream foto, FormDataContentDisposition fotoDetalle, String confirmpassword, String codigosCine) {
        this.telefono = telefono;
        this.password = password;
        this.pais = pais;
        this.nombre = nombre;
        this.identificacion = identificacion;
        this.id = id;
        this.correo = correo;
        this.foto = foto;
        this.fotoDetalle = fotoDetalle;
        this.confirmpassword = confirmpassword;
        this.codigosCine = codigosCine;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
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

    public InputStream getFoto() {
        return foto;
    }

    public void setFoto(InputStream foto) {
        this.foto = foto;
    }

    public FormDataContentDisposition getFotoDetalle() {
        return fotoDetalle;
    }

    public void setFotoDetalle(FormDataContentDisposition fotoDetalle) {
        this.fotoDetalle = fotoDetalle;
    }

    public String getConfirmpassword() {
        return confirmpassword;
    }

    public void setConfirmpassword(String confirmpassword) {
        this.confirmpassword = confirmpassword;
    }

    public String getCodigosCine() {
        return codigosCine;
    }

    public void setCodigosCine(String codigosCine) {
        this.codigosCine = codigosCine;
    }

    //Metodo delegado especializado para retornar la lista de valores de asignaciones
    public List<CineAsociadoDTO> getListadoCinesAsociados() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        List<CineAsociadoDTO> asignaciones = mapper.readValue(
                codigosCine,
                new TypeReference<List<CineAsociadoDTO>>() {
        }
        );

        return asignaciones;
    }

}
