/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pablocompany.rest.api.proyectono2ipc2.resources.reportescine;

import com.pablocompany.rest.api.proyectono2ipc2.excepciones.ErrorInesperadoException;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.FormatoInvalidoException;
import com.pablocompany.rest.api.proyectono2ipc2.reportesadmincine.models.ReporteSalasComentadasDTO;
import com.pablocompany.rest.api.proyectono2ipc2.reportesadmincine.services.ExportarSalaComentarosService;
import com.pablocompany.rest.api.proyectono2ipc2.reportesadmincine.services.NombreReporteRandomService;
import com.pablocompany.rest.api.proyectono2ipc2.reportesadmincine.services.ReporteComentariosSalaService;
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
@Path("reportes/salas/comentadas/exportar")
public class ExportarReporteComentariosSalaResource {

    //Endpoint que sirve para exportar a pdf el listado de reporte de comentarios
    @GET
    @Path("/inicio/{fechaInicio}/fin/{fechaFin}/limit/{limite}/offset/{inicio}")
    @Produces("application/pdf")
    public Response reporteComentarios(
            @PathParam("fechaInicio") String fechaInicio,
            @PathParam("fechaFin") String fechaFin,
            @PathParam("limite") String limite,
            @PathParam("inicio") String inicio) {

        ReporteComentariosSalaService reporteComentariosSalaService = new ReporteComentariosSalaService();

        try {
            List<ReporteSalasComentadasDTO> reporteSalasComentadasDTO = reporteComentariosSalaService.obtenerReporteSalaSinFiltro(fechaInicio, fechaFin, limite, inicio);

            ExportarSalaComentarosService exportarReporteService = new ExportarSalaComentarosService();

            byte[] pdfData = exportarReporteService.generarReporteSinFiltro(reporteSalasComentadasDTO);

            StreamingOutput stream = output -> {

                output.write(pdfData);
                output.flush();
            };

            NombreReporteRandomService nombreReporteRandomService = new NombreReporteRandomService();

            String nombreReporte = nombreReporteRandomService.getNombre("ReporteSalaComentada");

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

    //Endpoint que sirve para exportar a pdf el listado de reporte de comentarios filtrando por id de sala
    @GET
    @Path("/inicio/{fechaInicio}/fin/{fechaFin}/filtro/{idSala}/limit/{limite}/offset/{tope}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response reporteComentariosFiltro(
            @PathParam("fechaInicio") String fechaInicio,
            @PathParam("fechaFin") String fechaFin,
            @PathParam("idSala") String idSala,
            @PathParam("limite") String limite,
            @PathParam("tope") String inicio) {

        ReporteComentariosSalaService reporteComentariosSalaService = new ReporteComentariosSalaService();

        try {
            List<ReporteSalasComentadasDTO> reporteSalasComentadasDTO = reporteComentariosSalaService.obtenerReporteSalaConFiltro(idSala, fechaInicio, fechaFin, limite, inicio);

            ExportarSalaComentarosService exportarReporteService = new ExportarSalaComentarosService();

            byte[] pdfData = exportarReporteService.generarReporteSinFiltro(reporteSalasComentadasDTO);
            

            StreamingOutput stream = output -> {

                output.write(pdfData);
                output.flush();
            };

            NombreReporteRandomService nombreReporteRandomService = new NombreReporteRandomService();

            String nombreReporte = nombreReporteRandomService.getNombre("ReporteSalaComentada");

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
