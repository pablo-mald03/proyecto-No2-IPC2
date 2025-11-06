/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pablocompany.rest.api.proyectono2ipc2.resources.reportessistema;

import com.pablocompany.rest.api.proyectono2ipc2.excepciones.ErrorInesperadoException;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.FormatoInvalidoException;
import com.pablocompany.rest.api.proyectono2ipc2.reportesadmincine.models.ReporteSalasComentadasDTO;
import com.pablocompany.rest.api.proyectono2ipc2.reportesadmincine.services.ExportarSalaComentarosService;
import com.pablocompany.rest.api.proyectono2ipc2.reportesadmincine.services.NombreReporteRandomService;
import com.pablocompany.rest.api.proyectono2ipc2.reportesadmincine.services.ReporteComentariosSalaService;
import com.pablocompany.rest.api.proyectono2ipc2.reportesadminsistema.models.SalaMasComenadaDTO;
import com.pablocompany.rest.api.proyectono2ipc2.reportesadminsistema.services.ExportarSalasMasComentadasService;
import com.pablocompany.rest.api.proyectono2ipc2.reportesadminsistema.services.ReporteSalasMaComentadasService;
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
@Path("reportes/sistema/salas/comentadas/exportar")
public class ExportarReporteSalaMasComentadaResource {

    //Endpoint que sirve para exportar a pdf el listado de reporte de las 5 salas mas comentadas
    @GET
    @Path("/inicio/{fechaInicio}/fin/{fechaFin}/limit/{limite}/offset/{inicio}")
    @Produces("application/pdf")
    public Response reporteComentarios(
            @PathParam("fechaInicio") String fechaInicio,
            @PathParam("fechaFin") String fechaFin,
            @PathParam("limite") String limite,
            @PathParam("inicio") String inicio) {

        ReporteSalasMaComentadasService reporteSalaComentadaService = new ReporteSalasMaComentadasService();

        try {
            List<SalaMasComenadaDTO> reporteSalasComentadasDTO = reporteSalaComentadaService.obtenerReporteSala(fechaInicio, fechaFin, limite, inicio);

            ExportarSalasMasComentadasService exportarReporteService = new ExportarSalasMasComentadasService();

            byte[] pdfData = exportarReporteService.getReporteSalaMasComentada(reporteSalasComentadasDTO);

            StreamingOutput stream = output -> {

                output.write(pdfData);
                output.flush();
            };

            NombreReporteRandomService nombreReporteRandomService = new NombreReporteRandomService();

            String nombreReporte = nombreReporteRandomService.getNombre("ReporteSalasMasComentadas");

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
