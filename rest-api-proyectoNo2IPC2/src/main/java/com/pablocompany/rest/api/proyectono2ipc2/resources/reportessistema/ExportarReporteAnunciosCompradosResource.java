/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pablocompany.rest.api.proyectono2ipc2.resources.reportessistema;

import com.pablocompany.rest.api.proyectono2ipc2.excepciones.ErrorInesperadoException;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.FormatoInvalidoException;
import com.pablocompany.rest.api.proyectono2ipc2.reportesadmincine.services.NombreReporteRandomService;
import com.pablocompany.rest.api.proyectono2ipc2.reportesadminsistema.models.ReporteAnuncianteDTO;
import com.pablocompany.rest.api.proyectono2ipc2.reportesadminsistema.services.ExportarGananciasAnuncianteService;
import com.pablocompany.rest.api.proyectono2ipc2.reportesadminsistema.services.ReporteGananciasAnuncianteService;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.StreamingOutput;
import java.util.List;
import java.util.Map;

/**
 *
 * @author pablo
 */
@Path("reportes/sistema/anuncios/comprados/exportar")
public class ExportarReporteAnunciosCompradosResource {

    //Endpoint que sirve para exportar a pdf el listado de reporte de ganancias de anunciantes
    @GET
    @Path("/inicio/{fechaInicio}/fin/{fechaFin}/limit/{limite}/offset/{inicio}")
    @Produces("application/pdf")
    public Response reporteGananciasSinFiltro(
            @PathParam("fechaInicio") String fechaInicio,
            @PathParam("fechaFin") String fechaFin,
            @PathParam("limite") String limite,
            @PathParam("inicio") String inicio) {

        ReporteGananciasAnuncianteService reporteGananciasAnuncianteService = new ReporteGananciasAnuncianteService();

        try {
            List<ReporteAnuncianteDTO> reporteGananciasDto = reporteGananciasAnuncianteService.obtenerReporteSinFiltro(fechaInicio, fechaFin, limite, inicio);

            ExportarGananciasAnuncianteService exportarReporteService = new ExportarGananciasAnuncianteService();

            byte[] pdfData = exportarReporteService.getReporteGananciaAnunciate(reporteGananciasDto);

            StreamingOutput stream = output -> {

                output.write(pdfData);
                output.flush();
            };

            NombreReporteRandomService nombreReporteRandomService = new NombreReporteRandomService();

            String nombreReporte = nombreReporteRandomService.getNombre("ReporteGananciasAnunciante");

            return Response.ok(stream, MediaType.APPLICATION_OCTET_STREAM)
                    .header("Content-Disposition", "attachment; filename=\"" + nombreReporte + "\"")
                    .header("Access-Control-Expose-Headers", "Content-Disposition")
                    .build();

        } catch (FormatoInvalidoException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(Map.of("mensaje", ex.getMessage())).build();
        } catch (ErrorInesperadoException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Map.of("mensaje", ex.getMessage())).build();
        }

    }

    //Endpoint que sirve para exportar a pdf el listado de reporte de ganancias de anunciantes
    @GET
    @Path("/inicio/{fechaInicio}/fin/{fechaFin}/filtro/{idUsuario}/limit/{limite}/offset/{tope}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response reporteGananciasConFiltro(
            @PathParam("fechaInicio") String fechaInicio,
            @PathParam("fechaFin") String fechaFin,
            @PathParam("tipo") String tipo,
            @PathParam("limite") String limite,
            @PathParam("tope") String inicio) {

         ReporteGananciasAnuncianteService reporteGananciasAnuncianteService = new ReporteGananciasAnuncianteService();

        try {
            List<ReporteAnuncianteDTO> reporteGananciasDto = reporteGananciasAnuncianteService.obtenerReporteConFiltro(tipo, fechaInicio, fechaFin, limite, inicio);

            ExportarGananciasAnuncianteService exportarReporteService = new ExportarGananciasAnuncianteService();

            byte[] pdfData = exportarReporteService.getReporteGananciaAnunciate(reporteGananciasDto);

            StreamingOutput stream = output -> {

                output.write(pdfData);
                output.flush();
            };

            NombreReporteRandomService nombreReporteRandomService = new NombreReporteRandomService();

            String nombreReporte = nombreReporteRandomService.getNombre("ReporteGananciasAnunciante");

            return Response.ok(stream, MediaType.APPLICATION_OCTET_STREAM)
                    .header("Content-Disposition", "attachment; filename=\"" + nombreReporte + "\"")
                    .header("Access-Control-Expose-Headers", "Content-Disposition")
                    .build();

        } catch (FormatoInvalidoException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(Map.of("mensaje", ex.getMessage())).build();
        } catch (ErrorInesperadoException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Map.of("mensaje", ex.getMessage())).build();
        }

    }

}
