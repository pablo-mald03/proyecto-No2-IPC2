/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pablocompany.rest.api.proyectono2ipc2.resources.reportescine;

import com.pablocompany.rest.api.proyectono2ipc2.excepciones.ErrorInesperadoException;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.FormatoInvalidoException;
import com.pablocompany.rest.api.proyectono2ipc2.reportesadmincine.models.ReporteSalaPeliculaProyectadaDTO;
import com.pablocompany.rest.api.proyectono2ipc2.reportesadmincine.services.ExportarPeliculaSalaService;
import com.pablocompany.rest.api.proyectono2ipc2.reportesadmincine.services.NombreReporteRandomService;
import com.pablocompany.rest.api.proyectono2ipc2.reportesadmincine.services.ReportePeliculaService;
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
@Path("reportes/peliculas/proyectadas/exportar")
public class ExportarReportePeliculasProyectadasResource {

    //Endpoint que permite generar el reporte sin filtro de peliculas en salas de cine
    @GET
    @Path("/inicio/{fechaInicio}/fin/{fechaFin}/limit/{limite}/offset/{inicio}")
    @Produces("application/pdf")
    public Response exportarReportePeliculasSinFiltro(
            @PathParam("fechaInicio") String fechaInicio,
            @PathParam("fechaFin") String fechaFin,
            @PathParam("limite") String limite,
            @PathParam("inicio") String inicio) {

        ReportePeliculaService reportePeliculaService = new ReportePeliculaService();

        try {
            List<ReporteSalaPeliculaProyectadaDTO> reportePeliculaProyectadaDTO = reportePeliculaService.obtenerReportePeliculaSinFiltro(fechaInicio, fechaFin, limite, inicio);

            ExportarPeliculaSalaService exportarPeliculaSalaService = new ExportarPeliculaSalaService();

            byte[] pdfGenerado = exportarPeliculaSalaService.getReportePelicula(reportePeliculaProyectadaDTO);

            StreamingOutput stream = output -> {

                output.write(pdfGenerado);
                output.flush();
            };

            NombreReporteRandomService nombreReporteRandomService = new NombreReporteRandomService();

            String nombreRandom = nombreReporteRandomService.getNombre("ReportePeliculasSala");

            return Response.ok(stream, MediaType.APPLICATION_OCTET_STREAM)
                    .header("Content-Disposition", "attachment; filename=\"" + nombreRandom + "\"")
                    .header("Access-Control-Expose-Headers", "Content-Disposition")
                    .build();

        } catch (FormatoInvalidoException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(Map.of("mensaje", ex.getMessage())).build();
        } catch (ErrorInesperadoException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Map.of("mensaje", ex.getMessage())).build();
        }

    }

    //Endpoint que permite obtener las salas proyectadas con filtro
    @GET
    @Path("/inicio/{fechaInicio}/fin/{fechaFin}/filtro/{idSala}/limit/{limite}/offset/{inicio}")
    @Produces("application/pdf")
    public Response reportePeliculasConFiltro(
            @PathParam("fechaInicio") String fechaInicio,
            @PathParam("fechaFin") String fechaFin,
            @PathParam("limite") String limite,
            @PathParam("inicio") String inicio,
            @PathParam("idSala") String idSala) {

        ReportePeliculaService reportePeliculaService = new ReportePeliculaService();

        try {
            List<ReporteSalaPeliculaProyectadaDTO> reportePeliculaProyectadaDTO = reportePeliculaService.obtenerReportePeliculaConFiltro(fechaInicio, fechaFin, limite, inicio, idSala);

            ExportarPeliculaSalaService exportarPeliculaSalaService = new ExportarPeliculaSalaService();

            byte[] pdfGenerado = exportarPeliculaSalaService.getReportePelicula(reportePeliculaProyectadaDTO);

            StreamingOutput stream = output -> {

                output.write(pdfGenerado);
                output.flush();
            };

            NombreReporteRandomService nombreReporteRandomService = new NombreReporteRandomService();

            String nombreRandom = nombreReporteRandomService.getNombre("ReportePeliculasSala");

            return Response.ok(stream, MediaType.APPLICATION_OCTET_STREAM)
                    .header("Content-Disposition", "attachment; filename=\"" + nombreRandom + "\"")
                    .header("Access-Control-Expose-Headers", "Content-Disposition")
                    .build();

        } catch (FormatoInvalidoException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(Map.of("mensaje", ex.getMessage())).build();
        } catch (ErrorInesperadoException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Map.of("mensaje", ex.getMessage())).build();
        }

    }

}
