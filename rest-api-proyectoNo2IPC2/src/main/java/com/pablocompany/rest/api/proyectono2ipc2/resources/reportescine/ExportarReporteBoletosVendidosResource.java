/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pablocompany.rest.api.proyectono2ipc2.resources.reportescine;

import com.pablocompany.rest.api.proyectono2ipc2.excepciones.ErrorInesperadoException;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.FormatoInvalidoException;
import com.pablocompany.rest.api.proyectono2ipc2.reportesadmincine.models.ReporteBoletosVendidosDTO;
import com.pablocompany.rest.api.proyectono2ipc2.reportesadmincine.services.ExportarBoletosVendidosService;
import com.pablocompany.rest.api.proyectono2ipc2.reportesadmincine.services.NombreReporteRandomService;
import com.pablocompany.rest.api.proyectono2ipc2.reportesadmincine.services.ReporteBoletosVendidosService;
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
@Path("reportes/boletos/vendidos/exportar")
public class ExportarReporteBoletosVendidosResource {

    //Endpoint que permite exportar el reporte de boletos vendidos sin filtro
    @GET
    @Path("/inicio/{fechaInicio}/fin/{fechaFin}/limit/{limite}/offset/{inicio}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response reporteBoletosVendidosSinFiltro(
            @PathParam("fechaInicio") String fechaInicio,
            @PathParam("fechaFin") String fechaFin,
            @PathParam("limite") String limite,
            @PathParam("inicio") String inicio) {

        ReporteBoletosVendidosService reporteBoletosVendidosService = new ReporteBoletosVendidosService();

        try {
            List<ReporteBoletosVendidosDTO> reporteSalasGustadasDto = reporteBoletosVendidosService.obtenerReporteBoletosVendidosSinFiltro(fechaInicio, fechaFin, limite, inicio);

            ExportarBoletosVendidosService exportarBoletosVendidosService = new ExportarBoletosVendidosService();

            byte[] pdfGenerado = exportarBoletosVendidosService.getReporteBoletos(reporteSalasGustadasDto);

            StreamingOutput stream = output -> {

                output.write(pdfGenerado);
                output.flush();
            };

            NombreReporteRandomService nombreReporteRandomService = new NombreReporteRandomService();

            String nombreRandom = nombreReporteRandomService.getNombre("ReporteBoletosVendidos");

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

    //Endpoint que permite exportar el reporte de los boletos vendidos con filtro
    @GET
    @Path("/inicio/{fechaInicio}/fin/{fechaFin}/filtro/{idSala}/limit/{limite}/offset/{inicio}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response reporteBoletosVendidosFiltro(
            @PathParam("fechaInicio") String fechaInicio,
            @PathParam("fechaFin") String fechaFin,
            @PathParam("limite") String limite,
            @PathParam("inicio") String inicio,
            @PathParam("idSala") String idSala) {

        ReporteBoletosVendidosService reporteBoletosVendidosService = new ReporteBoletosVendidosService();

        try {
            List<ReporteBoletosVendidosDTO> reporteBoletosVendidosDTo = reporteBoletosVendidosService.obtenerReporteBoletosVendidosConFiltro(fechaInicio, fechaFin, limite, inicio, idSala);

            ExportarBoletosVendidosService exportarBoletosVendidosService = new ExportarBoletosVendidosService();

            byte[] pdfGenerado = exportarBoletosVendidosService.getReporteBoletos(reporteBoletosVendidosDTo);

            StreamingOutput stream = output -> {

                output.write(pdfGenerado);
                output.flush();
            };

            NombreReporteRandomService nombreReporteRandomService = new NombreReporteRandomService();

            String nombreRandom = nombreReporteRandomService.getNombre("ReporteBoletosVendidos");

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
