/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pablocompany.rest.api.proyectono2ipc2.anuncios.models;

import com.pablocompany.rest.api.proyectono2ipc2.excepciones.FormatoInvalidoException;
import java.time.LocalDate;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author pablo
 */
//Clase principal que representa el objeto de anuncio
public class Anuncio {

    private String codigo;
    private boolean estado;
    private String nombre;
    private boolean caducacion;
    private LocalDate fechaExpiracion;
    private LocalDate fechaCompra;
    private String url;
    private String texto;
    private byte[] foto;
    private int codigoTipo;
    private String idUsuario;

    public Anuncio(String codigo, boolean estado, String nombre, boolean caducacion, LocalDate fechaExpiracion, LocalDate fechaCompra, String url, String texto, byte[] foto, int codigoTipo, String idUsuario) {
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

    public byte[] getFoto() {
        return foto;
    }

    public void setFoto(byte[] foto) {
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

    public boolean validarDatos() throws FormatoInvalidoException {

        if (StringUtils.isBlank(codigo)) {
            throw new FormatoInvalidoException("El código está vacío.");
        }

        if (!estado) {
            throw new FormatoInvalidoException("El estado debe ser true.");
        }

        if (StringUtils.isBlank(nombre)) {
            throw new FormatoInvalidoException("El nombre está vacío.");
        }

        if (fechaCompra == null) {
            throw new FormatoInvalidoException("La fecha de compra es nula.");
        }

        if (fechaExpiracion == null) {
            throw new FormatoInvalidoException("La fecha de expiración es nula.");
        }

        if (StringUtils.isBlank(texto)) {
            throw new FormatoInvalidoException("El texto del anuncio está vacío.");
        }

        if (foto == null || foto.length == 0) {
            throw new FormatoInvalidoException("La foto del anuncio está vacía.");
        }

        if (codigoTipo < 0) {
            throw new FormatoInvalidoException("El código de tipo debe ser mayor que cero.");
        }

        if (StringUtils.isBlank(idUsuario)) {
            throw new FormatoInvalidoException("El ID de usuario está vacío.");
        }

        return true;
    }

}
