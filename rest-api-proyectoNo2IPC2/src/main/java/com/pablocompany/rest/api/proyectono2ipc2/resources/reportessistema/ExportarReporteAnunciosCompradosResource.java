/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pablocompany.rest.api.proyectono2ipc2.resources.reportessistema;

import com.pablocompany.rest.api.proyectono2ipc2.excepciones.ErrorInesperadoException;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.FormatoInvalidoException;
import com.pablocompany.rest.api.proyectono2ipc2.reportesadmincine.services.NombreReporteRandomService;
import com.pablocompany.rest.api.proyectono2ipc2.reportesadminsistema.models.ReporteAnuncianteDTO;
import com.pablocompany.rest.api.proyectono2ipc2.reportesadminsistema.models.ReporteAnuncioDTO;
import com.pablocompany.rest.api.proyectono2ipc2.reportesadminsistema.services.ExportarAnunciosCompradosService;
import com.pablocompany.rest.api.proyectono2ipc2.reportesadminsistema.services.ExportarGananciasAnuncianteService;
import com.pablocompany.rest.api.proyectono2ipc2.reportesadminsistema.services.ReporteAnunciosCompradosService;
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

    //Endpoint que sirve para exportar a pdf el listado de reporte de anuncios comprados
    @GET
    @Path("/inicio/{fechaInicio}/fin/{fechaFin}/limit/{limite}/offset/{inicio}")
    @Produces("application/pdf")
    public Response reporteAnunciosSinFiltro(
            @PathParam("fechaInicio") String fechaInicio,
            @PathParam("fechaFin") String fechaFin,
            @PathParam("limite") String limite,
            @PathParam("inicio") String inicio) {

        ReporteAnunciosCompradosService reporteAnunciosCompradosService = new ReporteAnunciosCompradosService();

        try {
            List<ReporteAnuncioDTO> reporteAnunciosDto = reporteAnunciosCompradosService.obtenerReporteAnunciosCompradosSinFiltro(fechaInicio, fechaFin, limite, inicio);

            ExportarAnunciosCompradosService exportarAnunciosCompradosService = new ExportarAnunciosCompradosService();

            byte[] pdfData = exportarAnunciosCompradosService.getReporteAnunciosComprados(reporteAnunciosDto);

            StreamingOutput stream = output -> {

                output.write(pdfData);
                output.flush();
            };

            NombreReporteRandomService nombreReporteRandomService = new NombreReporteRandomService();

            String nombreReporte = nombreReporteRandomService.getNombre("ReporteAnunciosComprados");

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

    //Endpoint que sirve para exportar a pdf el listado de reporte de anuncios comprados
    @GET
    @Path("/inicio/{fechaInicio}/fin/{fechaFin}/filtro/{idUsuario}/limit/{limite}/offset/{tope}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response reporteAnunciosConFiltro(
            @PathParam("fechaInicio") String fechaInicio,
            @PathParam("fechaFin") String fechaFin,
            @PathParam("tipo") String tipo,
            @PathParam("limite") String limite,
            @PathParam("tope") String inicio) {

        ReporteAnunciosCompradosService reporteAnunciosCompradosService = new ReporteAnunciosCompradosService();

        try {
            List<ReporteAnuncioDTO> reportePeliculaProyectadaDTO = reporteAnunciosCompradosService.obtenerReporteAnunciosCompradosConFiltro(fechaInicio, fechaFin, limite, inicio, tipo);

            ExportarAnunciosCompradosService exportarAnunciosCompradosService = new ExportarAnunciosCompradosService();

            byte[] pdfData = exportarAnunciosCompradosService.getReporteAnunciosComprados(reportePeliculaProyectadaDTO);

            StreamingOutput stream = output -> {

                output.write(pdfData);
                output.flush();
            };

            NombreReporteRandomService nombreReporteRandomService = new NombreReporteRandomService();

            String nombreReporte = nombreReporteRandomService.getNombre("ReporteAnunciosComprados");

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
