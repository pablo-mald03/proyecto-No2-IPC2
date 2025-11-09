/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pablocompany.rest.api.proyectono2ipc2.resources.reportessistema;

import com.pablocompany.rest.api.proyectono2ipc2.excepciones.DatosNoEncontradosException;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.ErrorInesperadoException;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.FormatoInvalidoException;
import com.pablocompany.rest.api.proyectono2ipc2.reportesadmincine.models.CantidadReportesDTO;
import com.pablocompany.rest.api.proyectono2ipc2.reportesadminsistema.models.ReporteAnuncianteDTO;
import com.pablocompany.rest.api.proyectono2ipc2.reportesadminsistema.services.ReporteGananciasAnuncianteService;
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

       //Endpoint que permite obtener las ganancias por anunciante sin filtro
    @GET
    @Path("/inicio/{fechaInicio}/fin/{fechaFin}/limit/{limite}/offset/{inicio}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response reporteAnunciantesSinFiltro(
            @PathParam("fechaInicio") String fechaInicio,
            @PathParam("fechaFin") String fechaFin,
            @PathParam("limite") String limite,
            @PathParam("inicio") String inicio) {

        ReporteGananciasAnuncianteService reporteGananciasAnuncianteService = new ReporteGananciasAnuncianteService();

        try {
            List<ReporteAnuncianteDTO> reporteGananciasDto = reporteGananciasAnuncianteService.obtenerReporteSinFiltro(fechaInicio, fechaFin, limite, inicio);

            return Response.ok(reporteGananciasDto).build();

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
    public Response cantidadReportesAnuncinateSinFiltro(
            @PathParam("fechaInicio") String fechaInicio,
            @PathParam("fechaFin") String fechaFin) {

        ReporteGananciasAnuncianteService reporteGananciasAnuncianteService = new ReporteGananciasAnuncianteService();

        try {

            CantidadReportesDTO cantidadReportes = reporteGananciasAnuncianteService.cantidadReportesSinFiltro(fechaInicio, fechaFin);
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
    public Response reporteAnunciantesConFiltro(
            @PathParam("fechaInicio") String fechaInicio,
            @PathParam("fechaFin") String fechaFin,
            @PathParam("limite") String limite,
            @PathParam("inicio") String inicio,
            @PathParam("idUsuario") String idUsuario) {

        ReporteGananciasAnuncianteService reporteGananciasAnuncianteService = new ReporteGananciasAnuncianteService();

        try {
            List<ReporteAnuncianteDTO> reportePeliculaProyectadaDTO = reporteGananciasAnuncianteService.obtenerReporteConFiltro(idUsuario, fechaInicio, fechaFin, limite, inicio);

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
    public Response cantidadReportesAnuncinateConFiltro(
            @PathParam("fechaInicio") String fechaInicio,
            @PathParam("fechaFin") String fechaFin,
            @PathParam("idUsuario") String idUsuario) {

        ReporteGananciasAnuncianteService reporteGananciasAnuncianteService = new ReporteGananciasAnuncianteService();

        try {

            CantidadReportesDTO cantidadReportes = reporteGananciasAnuncianteService.cantidadReportesConFiltro(fechaInicio, fechaFin, idUsuario);
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
