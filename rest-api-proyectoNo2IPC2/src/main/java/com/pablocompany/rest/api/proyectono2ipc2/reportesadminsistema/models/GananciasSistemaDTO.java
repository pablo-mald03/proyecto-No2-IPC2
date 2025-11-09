/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pablocompany.rest.api.proyectono2ipc2.reportesadminsistema.models;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author pablo
 */
//Clase delegada para operar con el objeto para poder crear el reporte de ganancias 
public class GananciasSistemaDTO {

    private List<CineCostoDTO> costosCine;
    private List<AnunciosCompradosDTO> anunciosComprados;
    private List<PagoCineAnuncioDTO> pagoBloqueoAnuncios;

    private double totalCostoCine;
    private double totalIngresos;
    private double totalGanancia;

    public GananciasSistemaDTO(double totalCostoCine, double totalIngresos, double totalGanancia) {
        this.totalCostoCine = totalCostoCine;
        this.totalIngresos = totalIngresos;
        this.totalGanancia = totalGanancia;
        this.costosCine = new ArrayList<>();
        this.pagoBloqueoAnuncios = new ArrayList<>();
        this.anunciosComprados = new ArrayList<>();
    }
    
    public GananciasSistemaDTO() {
        this.costosCine = new ArrayList<>();
        this.pagoBloqueoAnuncios = new ArrayList<>();
        this.anunciosComprados = new ArrayList<>();
    }

    public List<CineCostoDTO> getCostosCine() {
        return costosCine;
    }

    public void setCostosCine(List<CineCostoDTO> costosCine) {
        this.costosCine = costosCine;
    }

    public List<AnunciosCompradosDTO> getAnunciosComprados() {
        return anunciosComprados;
    }

    public void setAnunciosComprados(List<AnunciosCompradosDTO> anunciosComprados) {
        this.anunciosComprados = anunciosComprados;
    }

    public List<PagoCineAnuncioDTO> getPagoBloqueoAnuncios() {
        return pagoBloqueoAnuncios;
    }

    public void setPagoBloqueoAnuncios(List<PagoCineAnuncioDTO> pagoBloqueoAnuncios) {
        this.pagoBloqueoAnuncios = pagoBloqueoAnuncios;
    }

    public double getTotalCostoCine() {
        return totalCostoCine;
    }

    public void setTotalCostoCine(double totalCostoCine) {
        this.totalCostoCine = totalCostoCine;
    }

    public double getTotalIngresos() {
        return totalIngresos;
    }

    public void setTotalIngresos(double totalIngresos) {
        this.totalIngresos = totalIngresos;
    }

    public double getTotalGanancia() {
        return totalGanancia;
    }

    public void setTotalGanancia(double totalGanancia) {
        this.totalGanancia = totalGanancia;
    }

}
