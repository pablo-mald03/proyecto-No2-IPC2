/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pablocompany.rest.api.proyectono2ipc2.reportesadminsistema.dtos;

import com.pablocompany.rest.api.proyectono2ipc2.excepciones.FormatoInvalidoException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author pablo
 */
//Clase delegada para procesar los request de los anuncios comprados y encargada de validarse a si misma 
public class ReporteAnuncioRequest {

    private String tipoAnuncio;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private int offset;
    private int limit;

    //Flag que permite saber si la sentencia esta vacia 
    private boolean estaVacia;

    //String utilizado sin el filtro
    public ReporteAnuncioRequest(LocalDate fechaInicio, LocalDate fechaFin, int offset, int limit) {
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.offset = offset;
        this.limit = limit;
        this.estaVacia = false;
    }

    //Constructor utilizado para el filtro
    public ReporteAnuncioRequest(String idUsuario, LocalDate fechaInicio, LocalDate fechaFin, int offset, int limit) {
        this.tipoAnuncio = idUsuario;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.offset = offset;
        this.limit = limit;
        this.estaVacia = false;
    }

    //Constructor utilizado simplemente para poder instanciar el request vacio 
    public ReporteAnuncioRequest(int offset, int limit) {
        this.offset = offset;
        this.limit = limit;
        this.estaVacia = true;
    }

    public boolean estaVacia() {
        return estaVacia;
    }

    public void setEstaVacia(boolean estaVacia) {
        this.estaVacia = estaVacia;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public String getTipoAnuncio() {
        return tipoAnuncio;
    }

    public void setTipoAnuncio(String tipoAnuncio) {
        this.tipoAnuncio = tipoAnuncio;
    }

    //Metodo delegado para poder validar el request
    public boolean validarRequest() throws FormatoInvalidoException {

        try {
            if (fechaInicio == null || fechaFin == null) {
                throw new FormatoInvalidoException("El intervalo de fechas se encuentra vacio");
            }

            DateTimeFormatter isoFormatter = DateTimeFormatter.ISO_LOCAL_DATE;
            try {
                LocalDate.parse(fechaInicio.toString(), isoFormatter);
                LocalDate.parse(fechaFin.toString(), isoFormatter);
            } catch (DateTimeParseException e) {
                throw new FormatoInvalidoException("Las fechas no poseen un formato ISO");
            }

            if (fechaInicio.isAfter(fechaFin)) {
                throw new FormatoInvalidoException("La fecha de inicio no puede ser posterior a la fecha de fin.");
            }

            return true;

        } catch (Exception e) {
            throw new FormatoInvalidoException("No se ha podido procesar la informacion enviada.");
        }

    }

    //Metodo que sirve para validar cuando hay filtro
    public boolean validarRequestFiltro() throws FormatoInvalidoException {

        try {
            if (fechaInicio == null || fechaFin == null) {
                throw new FormatoInvalidoException("El intervalo de fechas se encuentra vacio");
            }

            DateTimeFormatter isoFormatter = DateTimeFormatter.ISO_LOCAL_DATE;
            try {
                LocalDate.parse(fechaInicio.toString(), isoFormatter);
                LocalDate.parse(fechaFin.toString(), isoFormatter);
            } catch (DateTimeParseException e) {
                throw new FormatoInvalidoException("Las fechas no poseen un formato ISO");
            }

            if (fechaInicio.isAfter(fechaFin)) {
                throw new FormatoInvalidoException("La fecha de inicio no puede ser posterior a la fecha de fin.");
            }

            if (StringUtils.isEmpty(this.tipoAnuncio)) {
                throw new FormatoInvalidoException("El tipo del anuncio no es valido");
            }

            if (!esTipoValido()) {
                throw new FormatoInvalidoException("El tipo del anuncio no es valido");
            }

            return true;

        } catch (Exception e) {
            throw new FormatoInvalidoException("No se ha podido procesar la informacion enviada.");
        }

    }

    //Metodo que permite validar cuando se construye un request vacio pero con tipo
    public boolean validarVacio() throws FormatoInvalidoException {

        if (StringUtils.isEmpty(this.tipoAnuncio)) {
            throw new FormatoInvalidoException("El tipo del anuncio esta vacio");
        }

        if (!esTipoValido()) {
            throw new FormatoInvalidoException("El tipo del anuncio no es valido");
        }

        return true;

    }

    //Metodo delegado para poder validar si el tipo de anuncio es correcto
    private boolean esTipoValido() {

        return (this.tipoAnuncio.equals("ANUNCIO_TEXTO")
                || this.tipoAnuncio.equals("IMAGEN_TEXTO")
                || this.tipoAnuncio.equals("VIDEO_TEXTO"));
    }

}
