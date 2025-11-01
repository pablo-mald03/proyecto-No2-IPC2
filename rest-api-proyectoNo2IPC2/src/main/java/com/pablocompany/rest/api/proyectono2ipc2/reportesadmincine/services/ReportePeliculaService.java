/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pablocompany.rest.api.proyectono2ipc2.reportesadmincine.services;

import com.pablocompany.rest.api.proyectono2ipc2.excepciones.DatosNoEncontradosException;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.ErrorInesperadoException;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.FormatoInvalidoException;
import com.pablocompany.rest.api.proyectono2ipc2.reportesadmincine.database.ReportePeliculasDB;
import com.pablocompany.rest.api.proyectono2ipc2.reportesadmincine.dtos.ReporteRequest;
import com.pablocompany.rest.api.proyectono2ipc2.reportesadmincine.models.CantidadReportesDTO;
import com.pablocompany.rest.api.proyectono2ipc2.reportesadmincine.models.ReporteSalaPeliculaProyectadaDTO;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author pablo
 */
//Clase delegada para poder procesar las solicitudes para obtener el listado de peliculas proyectadas en salas de cine
public class ReportePeliculaService {
    
    
    //Metodo utilizado para poder retornar la cantidad de salas de cine con peliculas en un intervalo de tiempo SIN FILTRO
    public List<ReporteSalaPeliculaProyectadaDTO> obtenerReportePeliculaSinFiltro(String fechaInicio, String fechaFin, String limit, String offset) throws FormatoInvalidoException, ErrorInesperadoException {

        ReporteRequest reporteSalasCineComentarios = extraerDatosReporte(fechaInicio, fechaFin, limit, offset);

        if (reporteSalasCineComentarios.validarRequest()) {

            ReportePeliculasDB reporteSalaCineDb = new ReportePeliculasDB();
            return reporteSalaCineDb.obtenerReportePeliculas(reporteSalasCineComentarios);
        }

        throw new ErrorInesperadoException("No se ha podido procesar la solicitud del reporte");
    }
    
    //Metodo delegado para poder validar y extraer la solicitud de request
    private ReporteRequest extraerDatosReporte(String fechaInicio, String fechaFin, String limit, String offset) throws FormatoInvalidoException {

        if (StringUtils.isBlank(fechaInicio)) {
            throw new FormatoInvalidoException("La fecha de inicio esta vacia");
        }

        if (StringUtils.isBlank(fechaFin)) {
            throw new FormatoInvalidoException("La fecha final esta vacia");
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
    public CantidadReportesDTO cantidadReportesSinFIltro(String fechaInicio, String fechaFin) throws FormatoInvalidoException, ErrorInesperadoException, DatosNoEncontradosException {

        if (StringUtils.isBlank(fechaInicio)) {
            throw new FormatoInvalidoException("La fecha de inicio esta vacia");
        }

        if (StringUtils.isBlank(fechaFin)) {
            throw new FormatoInvalidoException("La fecha final esta vacia");
        }

        try {
            LocalDate inicio = LocalDate.parse(fechaInicio, DateTimeFormatter.ISO_LOCAL_DATE);
            LocalDate fin = LocalDate.parse(fechaFin, DateTimeFormatter.ISO_LOCAL_DATE);

            if (inicio.isAfter(fin)) {
                throw new FormatoInvalidoException("La fecha de inicio no puede ser posterior a la fecha de fin.");
            }

            ReporteRequest reporteRequest = new ReporteRequest(inicio, fin, 0, 0);

            ReportePeliculasDB reportePeliculasDb = new ReportePeliculasDB();

            int cantidad = reportePeliculasDb.cantidadReportesSinFiltro(reporteRequest);

            return new CantidadReportesDTO(cantidad);

        } catch (DateTimeParseException e) {
            throw new FormatoInvalidoException("El formato de fecha solicitado debe ser ISO (yyyy-MM-dd)");
        } catch (NumberFormatException e) {
            throw new FormatoInvalidoException("Los limites de peticion no son numericos");
        }
    }

}
