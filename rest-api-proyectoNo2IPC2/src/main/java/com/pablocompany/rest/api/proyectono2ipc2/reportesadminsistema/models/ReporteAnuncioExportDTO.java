/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pablocompany.rest.api.proyectono2ipc2.reportesadminsistema.models;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author pablo
 */
//Clase delegada para poder generar los reportes de anuncios Objeto utilizado para poder generar los reportes
public class ReporteAnuncioExportDTO {

    private final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private String codigo;
    private boolean estado;
    private String nombre;
    private boolean caducacion;
    private LocalDate fechaExpiracion;
    private LocalDate fechaCompra;
    private int codigoTipo;
    private String idUsuario;
    private String nombreUsuario;

    public ReporteAnuncioExportDTO(ReporteAnuncioDTO reporteAnuncioDto) {
        this.codigo = reporteAnuncioDto.getCodigo();
        this.estado = reporteAnuncioDto.isEstado();
        this.nombre = reporteAnuncioDto.getNombre();
        this.caducacion = reporteAnuncioDto.isCaducacion();
        this.fechaExpiracion = reporteAnuncioDto.getFechaExpiracion();
        this.fechaCompra = reporteAnuncioDto.getFechaCompra();
        this.codigoTipo = reporteAnuncioDto.getCodigoTipo();
        this.idUsuario = reporteAnuncioDto.getIdUsuario();
        this.nombreUsuario = reporteAnuncioDto.getNombreUsuario();
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

    //Metodo delegado para obtener el tipo de anuncio 
    public String getTipoAnuncio() {
        switch (codigoTipo) {
            case 1:

                return "TEXTO";
            case 2:

                return "IMAGEN Y TEXTO";
            case 3:

                return "VIDEO Y TEXTO";

            default:
                return "Desconocido";
        }
    }

    //Obtiene la fecha de expiracion formateada
    public String getFechaExpiracionFormateada() {
        if (fechaExpiracion != null) {
            return fechaExpiracion.format(FORMATO);
        }
        return "";
    }

    //Obtiene la fecha de caducacion formateada
    public String getFechaCompraFormateada() {
        if (fechaCompra != null) {
            return fechaCompra.format(FORMATO);
        }
        return "";
    }

}
