/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pablocompany.rest.api.proyectono2ipc2.reportesadmincine.dtos;

import com.pablocompany.rest.api.proyectono2ipc2.excepciones.FormatoInvalidoException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author pablo
 */
//Clase delegada para poder manejar los request que vienen de fechas 
public class ReporteRequest {

    private String idSala;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private int offset;
    private int limit;

    //String utilizado sin el filtro
    public ReporteRequest(LocalDate fechaInicio, LocalDate fechaFin, int offset, int limit) {
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.offset = offset;
        this.limit = limit;
    }

    //Constructor utilizado para el filtro
    public ReporteRequest(String idSala, LocalDate fechaInicio, LocalDate fechaFin, int offset, int limit) {
        this.idSala = idSala;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.offset = offset;
        this.limit = limit;
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

    public String getIdSala() {
        return idSala;
    }

    public void setIdSala(String idSala) {
        this.idSala = idSala;
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
            
            if(StringUtils.isEmpty(this.idSala)){
                throw new FormatoInvalidoException("El id de la sala esta vacio");
            }

            return true;

        } catch (Exception e) {
            throw new FormatoInvalidoException("No se ha podido procesar la informacion enviada.");
        }

    }

}
