/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pablocompany.rest.api.proyectono2ipc2.resources.reportessistema;

import com.pablocompany.rest.api.proyectono2ipc2.excepciones.DatosNoEncontradosException;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.ErrorInesperadoException;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.FormatoInvalidoException;
import com.pablocompany.rest.api.proyectono2ipc2.reportesadmincine.models.CantidadReportesDTO;
import com.pablocompany.rest.api.proyectono2ipc2.reportesadmincine.models.ReporteSalaPeliculaProyectadaDTO;
import com.pablocompany.rest.api.proyectono2ipc2.reportesadmincine.services.ReportePeliculaService;
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
@Path("reportes/sistema/ganancias/anunciantes")
public class ReporteGananciasAnuncianteResource {

    //MUY PENDIENTE
    
    //Endpoint que permite obtener las ganancias por anunciante sin filtro
    @GET
    @Path("/inicio/{fechaInicio}/fin/{fechaFin}/limit/{limite}/offset/{inicio}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response reportePeliculasSinFiltro(
            @PathParam("fechaInicio") String fechaInicio,
            @PathParam("fechaFin") String fechaFin,
            @PathParam("limite") String limite,
            @PathParam("inicio") String inicio) {

        ReportePeliculaService reportePeliculaService = new ReportePeliculaService();

        try {
            List<ReporteSalaPeliculaProyectadaDTO> reportePeliculaProyectadaDTO = reportePeliculaService.obtenerReportePeliculaSinFiltro(fechaInicio, fechaFin, limite, inicio);

            return Response.ok(reportePeliculaProyectadaDTO).build();

        } catch (FormatoInvalidoException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(Map.of("mensaje", ex.getMessage())).build();
        } catch (ErrorInesperadoException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Map.of("mensaje", ex.getMessage())).build();
        }

    }

    //Enpoint que permite obtener la cantidad de las ganancias por anunciante sin filtro
    @GET
    @Path("/cantidad/inicio/{fechaInicio}/fin/{fechaFin}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response cantidadPeliculasSinFiltro(
            @PathParam("fechaInicio") String fechaInicio,
            @PathParam("fechaFin") String fechaFin) {

        ReportePeliculaService reportePeliculasProyectadasResource = new ReportePeliculaService();

        try {

            CantidadReportesDTO cantidadReportes = reportePeliculasProyectadasResource.cantidadReportesSinFiltro(fechaInicio, fechaFin);
            return Response.ok(cantidadReportes).build();

        } catch (FormatoInvalidoException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(Map.of("mensaje", ex.getMessage())).build();
        } catch (ErrorInesperadoException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Map.of("mensaje", ex.getMessage())).build();
        } catch (DatosNoEncontradosException ex) {
            return Response.status(Response.Status.NOT_FOUND).entity(Map.of("mensaje", ex.getMessage())).build();
        }
    }

    //Endpoint que permite obtener las ganancias por anunciante con filtro
    @GET
    @Path("/inicio/{fechaInicio}/fin/{fechaFin}/filtro/{idUsuario}/limit/{limite}/offset/{inicio}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response reportePeliculasConFiltro(
            @PathParam("fechaInicio") String fechaInicio,
            @PathParam("fechaFin") String fechaFin,
            @PathParam("limite") String limite,
            @PathParam("inicio") String inicio,
            @PathParam("idUsuario") String idUsuario) {

        ReportePeliculaService reportePeliculaService = new ReportePeliculaService();

        try {
            List<ReporteSalaPeliculaProyectadaDTO> reportePeliculaProyectadaDTO = reportePeliculaService.obtenerReportePeliculaConFiltro(fechaInicio, fechaFin, limite, inicio, idUsuario);

            return Response.ok(reportePeliculaProyectadaDTO).build();

        } catch (FormatoInvalidoException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(Map.of("mensaje", ex.getMessage())).build();
        } catch (ErrorInesperadoException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Map.of("mensaje", ex.getMessage())).build();
        }

    }

    //Enpoint que permite obtener las ganancias por anunciante con filtro
    @GET
    @Path("/cantidad/filtro/{idUsuario}/inicio/{fechaInicio}/fin/{fechaFin}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response cantidadPeliculasConFiltro(
            @PathParam("fechaInicio") String fechaInicio,
            @PathParam("fechaFin") String fechaFin,
            @PathParam("idUsuario") String idUsuario) {

        ReportePeliculaService reportePeliculasProyectadasResource = new ReportePeliculaService();

        try {

            CantidadReportesDTO cantidadReportes = reportePeliculasProyectadasResource.cantidadReportesConFiltro(fechaInicio, fechaFin, idUsuario);
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
