/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pablocompany.rest.api.proyectono2ipc2.resources;

import com.pablocompany.rest.api.proyectono2ipc2.excepciones.DatosNoEncontradosException;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.ErrorInesperadoException;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.FormatoInvalidoException;
import com.pablocompany.rest.api.proyectono2ipc2.reportesadmincine.models.CantidadReportesDTO;
import com.pablocompany.rest.api.proyectono2ipc2.reportesadmincine.models.ReporteSalasComentadasDTO;
import com.pablocompany.rest.api.proyectono2ipc2.reportesadmincine.services.ReporteComentariosSalaService;
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
@Path("reportes/salas/comentadas")
public class ReporteComentariosSalaResource {

    //Endpoint que sirve para obtener el listado de reporte de comentarios
    @GET
    @Path("/inicio/{fechaInicio}/fin/{fechaFin}/limit/{limite}/offset/{inicio}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response reporteComentarios(
            @PathParam("fechaInicio") String fechaInicio,
            @PathParam("fechaFin") String fechaFin,
            @PathParam("limite") String limite,
            @PathParam("inicio") String inicio) {

        ReporteComentariosSalaService reporteComentariosSalaService = new ReporteComentariosSalaService();

        try {
            List<ReporteSalasComentadasDTO> reporteSalasComentadasDTO = reporteComentariosSalaService.obtenerReporteSalaSinFiltro(fechaInicio, fechaFin, limite, inicio);

            return Response.ok(reporteSalasComentadasDTO).build();

        } catch (FormatoInvalidoException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(Map.of("mensaje", ex.getMessage())).build();
        } catch (ErrorInesperadoException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Map.of("mensaje", ex.getMessage())).build();
        }

    }

    //Endpoint que sirve para obtener la cantidad de reportes que hay
    @GET
    @Path("/cantidad/inicio/{fechaInicio}/fin/{fechaFin}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response cantidadComentarios(
            @PathParam("fechaInicio") String fechaInicio,
            @PathParam("fechaFin") String fechaFin) {

        ReporteComentariosSalaService reporteComentariosSalaService = new ReporteComentariosSalaService();

        try {
            CantidadReportesDTO cantidadReportes = reporteComentariosSalaService.cantidadReportesSinFiltro(fechaInicio, fechaFin);
            return Response.ok(cantidadReportes).build();

        } catch (FormatoInvalidoException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(Map.of("mensaje", ex.getMessage())).build();
        } catch (ErrorInesperadoException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Map.of("mensaje", ex.getMessage())).build();
        } catch (DatosNoEncontradosException ex) {
            return Response.status(Response.Status.NOT_FOUND).entity(Map.of("mensaje", ex.getMessage())).build();
        }
    }

    //Endpoint que sirve para obtener el listado de reporte de comentarios filtrando por id de sala
    @GET
    @Path("/inicio/{fechaInicio}/fin/{fechaFin}/filtro/{idSala}/limit/{limite}/offset/{tope}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response reporteComentariosFiltro(
            @PathParam("fechaInicio") String fechaInicio,
            @PathParam("fechaFin") String fechaFin,
            @PathParam("idSala") String idSala,
            @PathParam("limite") String limite,
            @PathParam("tope") String tope) {

        return Response.ok().build();

    }

    //Endpoint que sirve para obtener la cantidad de comentarios que hay en una fecha por sala
    @GET
    @Path("/cantidad/inicio/{fechaInicio}/fin/{fechaFin}/filtro/{idSala}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response cantidadComentariosFiltro(
            @PathParam("fechaInicio") String fechaInicio,
            @PathParam("fechaFin") String fechaFin,
            @PathParam("idSala") String idSala) {

        return Response.ok().build();

    }

}
