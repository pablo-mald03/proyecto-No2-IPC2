/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pablocompany.rest.api.proyectono2ipc2.resources.reportessistema;

import com.pablocompany.rest.api.proyectono2ipc2.excepciones.ErrorInesperadoException;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.FormatoInvalidoException;
import com.pablocompany.rest.api.proyectono2ipc2.reportesadmincine.services.NombreReporteRandomService;
import com.pablocompany.rest.api.proyectono2ipc2.reportesadminsistema.models.SalaMasGustadaDTO;
import com.pablocompany.rest.api.proyectono2ipc2.reportesadminsistema.services.ExportarSalasMasGustadasService;
import com.pablocompany.rest.api.proyectono2ipc2.reportesadminsistema.services.ReporteSalasMasGustadasService;
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
@Path("reportes/sistema/salas/gustadas/exportar")
public class ExportarReporteSalasMasGustadasResource {

    
    
    //Endpoint que permite exportar el reporte de las 5 salas mas gustadas sin filtro
    @GET
    @Path("/inicio/{fechaInicio}/fin/{fechaFin}/limit/{limite}/offset/{inicio}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response reporteSalasGustadas(
            @PathParam("fechaInicio") String fechaInicio,
            @PathParam("fechaFin") String fechaFin,
            @PathParam("limite") String limite,
            @PathParam("inicio") String inicio) {

        ReporteSalasMasGustadasService reporteSalasGustadasService = new ReporteSalasMasGustadasService();

        try {
            List<SalaMasGustadaDTO> reporteSalasGustadasDto = reporteSalasGustadasService.obtenerReporteSalaGustada(fechaInicio, fechaFin, limite, inicio);

            ExportarSalasMasGustadasService exportarSalasGustadasService = new ExportarSalasMasGustadasService();

            byte[] pdfGenerado = exportarSalasGustadasService.getReporteSalaGustada(reporteSalasGustadasDto);

            StreamingOutput stream = output -> {

                output.write(pdfGenerado);
                output.flush();
            };

            NombreReporteRandomService nombreReporteRandomService = new NombreReporteRandomService();

            String nombreRandom = nombreReporteRandomService.getNombre("ReporteSalasMasGustadas");

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
