/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pablocompany.rest.api.proyectono2ipc2.cine.models;

import com.pablocompany.rest.api.proyectono2ipc2.excepciones.FormatoInvalidoException;
import java.time.LocalDate;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author pablo
 */
//Clase delegada para poder interactuar con el objeto para poder crear los cines 
public class CineDTO {

    private String codigo;
    private String nombre;
    private boolean estadoAnuncio;
    private double montoOcultacion;
    private LocalDate fechaCreacion;
    private String descripcion;
    private String ubicacion;
    private double costoCine;

    //Constructor utilizado para crear los cines
    public CineDTO(String codigo, String nombre, boolean estadoAnuncio, double montoOcultacion, LocalDate fechaCreacion, String descripcion, String ubicacion, double costoCine) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.estadoAnuncio = estadoAnuncio;
        this.montoOcultacion = montoOcultacion;
        this.fechaCreacion = fechaCreacion;
        this.descripcion = descripcion;
        this.ubicacion = ubicacion;
        this.costoCine = costoCine;
    }
    
    //Constructor utilizado para consultas
    public CineDTO(String codigo, String nombre, boolean estadoAnuncio, double montoOcultacion, LocalDate fechaCreacion, String descripcion, String ubicacion) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.estadoAnuncio = estadoAnuncio;
        this.montoOcultacion = montoOcultacion;
        this.fechaCreacion = fechaCreacion;
        this.descripcion = descripcion;
        this.ubicacion = ubicacion;
        this.costoCine = 0;
    }

    public double getCostoCine() {
        return costoCine;
    }

    public void setCostoCine(double costoCine) {
        this.costoCine = costoCine;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public boolean isEstadoAnuncio() {
        return estadoAnuncio;
    }

    public void setEstadoAnuncio(boolean estadoAnuncio) {
        this.estadoAnuncio = estadoAnuncio;
    }

    public double getMontoOcultacion() {
        return montoOcultacion;
    }

    public void setMontoOcultacion(double montoOcultacion) {
        this.montoOcultacion = montoOcultacion;
    }

    public LocalDate getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDate fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    //Metodo que se encarga de poder validar las entradas del cine 
    public boolean validarCine() throws FormatoInvalidoException {
        if (StringUtils.isBlank(codigo)) {
            throw new FormatoInvalidoException("El código del cine no puede estar vacío");
        }

        if (!codigo.matches("^C-\\d{3}$")) {
            throw new FormatoInvalidoException("El código del cine no cumple con el formato 'C-XXX' (ej. C-001)");
        }

        if (StringUtils.isBlank(nombre)) {
            throw new FormatoInvalidoException("El nombre del cine no puede estar vacío");
        }

        if (nombre.length() < 3) {
            throw new FormatoInvalidoException("El nombre del cine debe tener al menos 3 caracteres");
        }

        if (montoOcultacion < 0) {
            throw new FormatoInvalidoException("El monto de ocultación no puede ser negativo");
        }
        
        if (costoCine < 0) {
            throw new FormatoInvalidoException("El costo del cine no puede ser negativo");
        }

        if (fechaCreacion == null) {
            throw new FormatoInvalidoException("La fecha de creación no puede ser nula");
        }

        if (StringUtils.isBlank(descripcion)) {
            throw new FormatoInvalidoException("La descripción no puede estar vacía");
        }

        if (StringUtils.isBlank(ubicacion)) {
            throw new FormatoInvalidoException("La ubicación no puede estar vacía");
        }

        return true;
    }

}
