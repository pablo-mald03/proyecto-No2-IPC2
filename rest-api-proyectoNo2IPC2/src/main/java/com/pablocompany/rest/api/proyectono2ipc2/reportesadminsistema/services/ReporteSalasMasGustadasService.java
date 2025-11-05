/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pablocompany.rest.api.proyectono2ipc2.reportesadminsistema.services;

import com.pablocompany.rest.api.proyectono2ipc2.excepciones.DatosNoEncontradosException;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.ErrorInesperadoException;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.FormatoInvalidoException;
import com.pablocompany.rest.api.proyectono2ipc2.reportesadmincine.dtos.ReporteRequest;
import com.pablocompany.rest.api.proyectono2ipc2.reportesadmincine.models.CantidadReportesDTO;
import com.pablocompany.rest.api.proyectono2ipc2.reportesadminsistema.database.ReporteSalasMasGustadasDB;
import com.pablocompany.rest.api.proyectono2ipc2.reportesadminsistema.models.SalaMasGustadaDTO;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author pablo
 */
//Clase delegada para poder operar con los servicios que ofrece las salas mas gustadas 
public class ReporteSalasMasGustadasService {
    
    //Metodo utilizado para poder retornar la cantidad de salas de cine con peliculas en un intervalo de tiempo SIN FILTRO
    public List<SalaMasGustadaDTO> obtenerReporteSalaGustada(String fechaInicio, String fechaFin, String limit, String offset) throws FormatoInvalidoException, ErrorInesperadoException {

        ReporteRequest reporteRequest = extraerDatosReporte(fechaInicio, fechaFin, limit, offset);

        ReporteSalasMasGustadasDB reporteSalasGustadasDb = new ReporteSalasMasGustadasDB();

        if (reporteRequest.getOffset() >= 5) {
            reporteRequest.setLimit(5);
            reporteRequest.setOffset(0);
        }

        //Retorna todo el listado 
        if (reporteRequest.estaVacia()) {
            return reporteSalasGustadasDb.obtenerTodasSalas(reporteRequest);
        }

        if (reporteRequest.validarRequest()) {

            return reporteSalasGustadasDb.obtenerSalasFecha(reporteRequest);
        }

        throw new ErrorInesperadoException("No se ha podido procesar la solicitud del reporte de 5 salas mas gustadas");
    }

    //Metodo delegado para poder validar y extraer la solicitud de request
    private ReporteRequest extraerDatosReporte(String fechaInicio, String fechaFin, String limit, String offset) throws FormatoInvalidoException {

        if (StringUtils.isBlank(fechaInicio)) {
            throw new FormatoInvalidoException("La fecha de inicio no ha especificado ningun parametro");
        }

        if (StringUtils.isBlank(fechaFin)) {
            throw new FormatoInvalidoException("La fecha final no ha especificado ningun parametro");
        }

        if (StringUtils.isBlank(limit)) {
            throw new FormatoInvalidoException("El limite superior de la peticion vacio");
        }

        if (StringUtils.isBlank(offset)) {
            throw new FormatoInvalidoException("El limite inferior de la peticion vacio");
        }

        if (!StringUtils.isNumeric(limit)) {
            throw new FormatoInvalidoException("El limite superior de la peticion no es numerico");
        }

        if (!StringUtils.isNumeric(offset)) {
            throw new FormatoInvalidoException("El limite inferior de la peticion no es numerico");
        }

        if (fechaInicio.equals("null") && fechaFin.equals("null")) {
            return new ReporteRequest(
                    Integer.parseInt(offset),
                    Integer.parseInt(limit));
        }

        try {
            // Parsear a formato ISO (yyyy-MM-dd)
            LocalDate inicio = LocalDate.parse(fechaInicio, DateTimeFormatter.ISO_LOCAL_DATE);
            LocalDate fin = LocalDate.parse(fechaFin, DateTimeFormatter.ISO_LOCAL_DATE);

            return new ReporteRequest(
                    inicio,
                    fin,
                    Integer.parseInt(offset),
                    Integer.parseInt(limit));

        } catch (DateTimeParseException e) {
            throw new FormatoInvalidoException("El formato de fecha solicitado debe ser ISO (yyyy-MM-dd)");
        } catch (NumberFormatException e) {
            throw new FormatoInvalidoException("Los limites de peticion no son enteros");
        }

    }

    //Metodo que sirve para retornar la cantidad de reportes sin filtro
    public CantidadReportesDTO cantidadReportes(String fechaInicio, String fechaFin) throws FormatoInvalidoException, ErrorInesperadoException, DatosNoEncontradosException {

        if (StringUtils.isBlank(fechaInicio)) {
            throw new FormatoInvalidoException("La fecha de inicio esta vacia");
        }

        if (StringUtils.isBlank(fechaFin)) {
            throw new FormatoInvalidoException("La fecha final esta vacia");
        }

        try {

            ReporteSalasMasGustadasDB reporteSalasGustadasDb = new ReporteSalasMasGustadasDB();

            if (fechaInicio.equals("null") && fechaFin.equals("null")) {

                ReporteRequest reporteTodoRequest = new ReporteRequest(0, 0);

                int cantidad = reporteSalasGustadasDb.cantidadTodosReportes(reporteTodoRequest);

                return new CantidadReportesDTO(cantidad);

            }

            LocalDate inicio = LocalDate.parse(fechaInicio, DateTimeFormatter.ISO_LOCAL_DATE);
            LocalDate fin = LocalDate.parse(fechaFin, DateTimeFormatter.ISO_LOCAL_DATE);

            ReporteRequest reporteRequest = new ReporteRequest(inicio, fin, 0, 0);

            if (reporteRequest.validarRequest()) {

                int cantidad = reporteSalasGustadasDb.cantidadReportes(reporteRequest);

                return new CantidadReportesDTO(cantidad);

            }

        } catch (DateTimeParseException e) {
            throw new FormatoInvalidoException("El formato de fecha solicitado debe ser ISO (yyyy-MM-dd)");
        } catch (NumberFormatException e) {
            throw new FormatoInvalidoException("Los limites de peticion no son numericos");
        }

        throw new ErrorInesperadoException("No se pudo obtener la cantidad de salas mas gustadas.");
    }
    
}
