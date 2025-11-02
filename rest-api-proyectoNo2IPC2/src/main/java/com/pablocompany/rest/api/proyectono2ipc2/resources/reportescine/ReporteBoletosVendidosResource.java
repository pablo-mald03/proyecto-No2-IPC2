/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pablocompany.rest.api.proyectono2ipc2.resources.reportescine;

import com.pablocompany.rest.api.proyectono2ipc2.excepciones.DatosNoEncontradosException;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.ErrorInesperadoException;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.FormatoInvalidoException;
import com.pablocompany.rest.api.proyectono2ipc2.reportesadmincine.models.CantidadReportesDTO;
import com.pablocompany.rest.api.proyectono2ipc2.reportesadmincine.models.ReporteBoletosVendidosDTO;
import com.pablocompany.rest.api.proyectono2ipc2.reportesadmincine.services.ReporteBoletosVendidosService;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.Map;

/**
 *
 * @author pablo
 */
@Path("reportes/boletos/vendidos")
public class ReporteBoletosVendidosResource {

    //Endpoint que permite obtener el reporte de boletos vendidos sin filtro
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

            return Response.ok(reporteSalasGustadasDto).build();

        } catch (FormatoInvalidoException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(Map.of("mensaje", ex.getMessage())).build();
        } catch (ErrorInesperadoException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Map.of("mensaje", ex.getMessage())).build();
        }

    }

    //Enpoint que permite obtener la cantidad de boletos vendidos sin filtro
    @GET
    @Path("/cantidad/inicio/{fechaInicio}/fin/{fechaFin}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response cantidadReportesSinFiltro(
            @PathParam("fechaInicio") String fechaInicio,
            @PathParam("fechaFin") String fechaFin) {

        ReporteBoletosVendidosService reporteBoletosVendidosService = new ReporteBoletosVendidosService();

        try {

            CantidadReportesDTO cantidadReportes = reporteBoletosVendidosService.cantidadReportesSinFiltro(fechaInicio, fechaFin);
            return Response.ok(cantidadReportes).build();

        } catch (FormatoInvalidoException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(Map.of("mensaje", ex.getMessage())).build();
        } catch (ErrorInesperadoException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Map.of("mensaje", ex.getMessage())).build();
        } catch (DatosNoEncontradosException ex) {
            return Response.status(Response.Status.NOT_FOUND).entity(Map.of("mensaje", ex.getMessage())).build();
        }
    }

    //Endpoint que permite obtener el reporte de boletos vendidos con filtro
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
            List<ReporteBoletosVendidosDTO> reporteSalasGustadasDto = reporteBoletosVendidosService.obtenerReporteBoletosVendidosConFiltro(fechaInicio, fechaFin, limite, inicio, idSala);

            return Response.ok(reporteSalasGustadasDto).build();

        } catch (FormatoInvalidoException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(Map.of("mensaje", ex.getMessage())).build();
        } catch (ErrorInesperadoException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Map.of("mensaje", ex.getMessage())).build();
        }

    }

    //Enpoint que permite obtener la cantidad de boletos vendidos con filtro
    @GET
    @Path("/cantidad/filtro/{idSala}/inicio/{fechaInicio}/fin/{fechaFin}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response cantidadReportesConFiltro(
            @PathParam("fechaInicio") String fechaInicio,
            @PathParam("fechaFin") String fechaFin,
            @PathParam("idSala") String idSala) {

        ReporteBoletosVendidosService reporteBoletosVendidosService = new ReporteBoletosVendidosService();

        try {

            CantidadReportesDTO cantidadReportes = reporteBoletosVendidosService.cantidadReportesConFiltro(fechaInicio, fechaFin, idSala);
            return Response.ok(cantidadReportes).build();

        } catch (FormatoInvalidoException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(Map.of("mensaje", ex.getMessage())).build();
        } catch (ErrorInesperadoException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Map.of("mensaje", ex.getMessage())).build();
        } catch (DatosNoEncontradosException ex) {
            return Response.status(Response.Status.NOT_FOUND).entity(Map.of("mensaje", ex.getMessage())).build();
        }
    }

}
