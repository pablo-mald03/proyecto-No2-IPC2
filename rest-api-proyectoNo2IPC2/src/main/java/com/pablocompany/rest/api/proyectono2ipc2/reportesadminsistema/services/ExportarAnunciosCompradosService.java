/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pablocompany.rest.api.proyectono2ipc2.reportesadminsistema.services;

import com.pablocompany.rest.api.proyectono2ipc2.excepciones.ErrorInesperadoException;
import com.pablocompany.rest.api.proyectono2ipc2.reportesadminsistema.models.ReporteAnuncioDTO;
import com.pablocompany.rest.api.proyectono2ipc2.reportesadminsistema.models.ReporteAnuncioExportDTO;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

/**
 *
 * @author pablo
 */
//Clase delegada que permite exportar los reportes de anuncios comprados en todo el sistema
public class ExportarAnunciosCompradosService {

    //Metodo utilizado para retornar el reporte de anuncios comprados en todo el sistema
    public byte[] getReporteAnunciosComprados(List<ReporteAnuncioDTO> anunciosComprados) throws ErrorInesperadoException {

        List<ReporteAnuncioExportDTO> listadoExportacion = getListaExportada(anunciosComprados);

        InputStream logoEmpresa = getClass().getClassLoader().getResourceAsStream("com/pablocompany/rest/api/reports/img/cinemaAppIcon.png");

        if (logoEmpresa == null) {
            throw new ErrorInesperadoException("No se ha podido obtener el logo de la empresa");
        }

        InputStream reporte = getClass().getClassLoader().getResourceAsStream("com/pablocompany/rest/api/reports/anuncioscompradossys/RepoteAnunciosComprados.jasper");

        if (reporte == null) {
            throw new ErrorInesperadoException("No se ha podido cargar el reporte Jasper");
        }

        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(listadoExportacion);

        Map<String, Object> params = new HashMap<>();

        params.put("logoEmpresa", logoEmpresa);
        try {
            JasperPrint jasperPrinter = JasperFillManager.fillReport(reporte, params, dataSource);

            return JasperExportManager.exportReportToPdf(jasperPrinter);

        } catch (JRException ex) {
            throw new ErrorInesperadoException("No se ha podido generar el reporte de 5 salas mas gustadas");
        }

    }

    //Metodo delegado para convertir la lista de entrada a un objeto para el reporte
    private List<ReporteAnuncioExportDTO> getListaExportada(List<ReporteAnuncioDTO> anunciosComprados) {

        List<ReporteAnuncioExportDTO> listaExportar = new ArrayList<>();
        for (ReporteAnuncioDTO anuncio : anunciosComprados) {
            listaExportar.add(new ReporteAnuncioExportDTO(anuncio));
        }

        return listaExportar;
    }
}
