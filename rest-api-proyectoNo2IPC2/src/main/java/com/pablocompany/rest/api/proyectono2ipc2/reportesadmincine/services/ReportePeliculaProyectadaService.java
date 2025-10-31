/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pablocompany.rest.api.proyectono2ipc2.reportesadmincine.services;

import com.pablocompany.rest.api.proyectono2ipc2.excepciones.FormatoInvalidoException;
import com.pablocompany.rest.api.proyectono2ipc2.reportesadmincine.models.CantidadReportesDTO;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author pablo
 */
//Clase delegada para poder procesar las solicitudes para obtener el listado de peliculas proyectadas en salas de cine
public class ReportePeliculaProyectadaService {

    //Metodo que sirve para retornar la cantidad de reportes sin filtro
    public CantidadReportesDTO cantidadReportesSinFIltro(String fechaInicio, String fechaFin) throws FormatoInvalidoException {

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

            ReporteSalasCineComentariosRequest reporteSalasCineComentariosRequest = new ReporteSalasCineComentariosRequest(inicio, fin, 0, 0);

            ReporteSalaCineDB reporteSalaCineDb = new ReporteSalaCineDB();

            int cantidad = reporteSalaCineDb.cantidadReportesSinFiltro(reporteSalasCineComentariosRequest);
            return new CantidadReportesDTO(cantidad);

        } catch (DateTimeParseException e) {
            throw new FormatoInvalidoException("El formato de fecha solicitado debe ser ISO (yyyy-MM-dd)");
        } catch (NumberFormatException e) {
            throw new FormatoInvalidoException("Los limites de peticion no son numericos");
        }
    }

}
