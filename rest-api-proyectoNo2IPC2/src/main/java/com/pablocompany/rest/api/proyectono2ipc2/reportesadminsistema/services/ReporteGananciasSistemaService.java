/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pablocompany.rest.api.proyectono2ipc2.reportesadminsistema.services;

import com.pablocompany.rest.api.proyectono2ipc2.excepciones.DatosNoEncontradosException;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.ErrorInesperadoException;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.FormatoInvalidoException;
import com.pablocompany.rest.api.proyectono2ipc2.reportesadmincine.models.CantidadReportesDTO;
import com.pablocompany.rest.api.proyectono2ipc2.reportesadminsistema.database.ReporteAnuncianteDB;
import com.pablocompany.rest.api.proyectono2ipc2.reportesadminsistema.database.ReporteGananciasSistemaBD;
import com.pablocompany.rest.api.proyectono2ipc2.reportesadminsistema.dtos.ReporteAnuncianteRequest;
import com.pablocompany.rest.api.proyectono2ipc2.reportesadminsistema.dtos.ReporteSistemaRequest;
import com.pablocompany.rest.api.proyectono2ipc2.reportesadminsistema.models.GananciasSistemaDTO;
import com.pablocompany.rest.api.proyectono2ipc2.reportesadminsistema.models.ReporteAnuncianteDTO;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author pablo
 */
//Clase delegada para poder operar con el reporte de ganancias del sistema
public class ReporteGananciasSistemaService {

    //Metodo que sirve para obtener el reporte de ganancias con limite de fechas
    public GananciasSistemaDTO obtenerReporteGanancias(String fechaInicio, String fechaFin) throws FormatoInvalidoException, ErrorInesperadoException {

        ReporteSistemaRequest reporteRequest = extraerDatosReporte(fechaInicio, fechaFin);

        ReporteGananciasSistemaBD reporteGananciasDb = new ReporteGananciasSistemaBD();

        //Retorna todo el listado 
        if (reporteRequest.estaVacia()) {
            //return reporteAnunciante.obtenerReporteTodoAnunciante(reporteRequest);
        }

        if (reporteRequest.validarRequest()) {

            return reporteGananciasDb.obtenerReporteGanancias(reporteRequest);
        }

        throw new ErrorInesperadoException("No se ha podido procesar la solicitud del reporte");
    }

    //Metodo delegado para poder validar y extraer la solicitud de request
    private ReporteSistemaRequest extraerDatosReporte(String fechaInicio, String fechaFin) throws FormatoInvalidoException {

        if (StringUtils.isBlank(fechaInicio)) {
            throw new FormatoInvalidoException("La fecha de inicio esta vacia");
        }

        if (StringUtils.isBlank(fechaFin)) {
            throw new FormatoInvalidoException("La fecha final esta vacia");
        }

        if (fechaInicio.equals("null") && fechaFin.equals("null")) {
            return new ReporteSistemaRequest(0, 0);
        }

        try {
            // Parsear a formato ISO (yyyy-MM-dd)
            LocalDate inicio = LocalDate.parse(fechaInicio, DateTimeFormatter.ISO_LOCAL_DATE);
            LocalDate fin = LocalDate.parse(fechaFin, DateTimeFormatter.ISO_LOCAL_DATE);

            return new ReporteSistemaRequest(
                    inicio,
                    fin,
                    0,
                    0);

        } catch (DateTimeParseException e) {
            throw new FormatoInvalidoException("El formato de fecha solicitado debe ser ISO (yyyy-MM-dd)");
        } catch (NumberFormatException e) {
            throw new FormatoInvalidoException("Los limites de peticion no son numericos");
        }

    }

}
