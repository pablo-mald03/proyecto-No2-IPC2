/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pablocompany.rest.api.proyectono2ipc2.reportesadmincine.services;

import com.pablocompany.rest.api.proyectono2ipc2.excepciones.ErrorInesperadoException;
import com.pablocompany.rest.api.proyectono2ipc2.reportesadmincine.models.ReporteSalasComentadasDTO;
import java.io.InputStream;
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
//Clase delegada para procesar toda la logica de negocio para poder retornar el reporte
public class ExportarSalaComentarosService {

    //Metodo encargado para generar el reporte y retornarlo
    public byte[] generarReporteSinFiltro(List<ReporteSalasComentadasDTO> reporteSalasComentadasDTO) throws ErrorInesperadoException {

        InputStream logoEmpresa = getClass().getClassLoader().getResourceAsStream("com/pablocompany/rest/api/reports/img/cinemaAppIcon.png");

        if (logoEmpresa == null) {
            throw new ErrorInesperadoException("No se ha podido obtener el logo de la empresa");
        }

        InputStream reporte = getClass().getClassLoader().getResourceAsStream("com/pablocompany/rest/api/reports/comentarios/ReporteComentariosSalasCIne.jasper");

        if (reporte == null) {
            throw new ErrorInesperadoException("No se ha podido cargar el reporte Jasper");
        }

        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(reporteSalasComentadasDTO);

        Map<String, Object> params = new HashMap<>();

        params.put("logoEmpresa", logoEmpresa);
        try {
            JasperPrint jasperPrinter = JasperFillManager.fillReport(reporte, params, dataSource);

            return JasperExportManager.exportReportToPdf(jasperPrinter);

        } catch (JRException ex) {
            throw new ErrorInesperadoException("No se ha podido generar el reporte de comentarios en salas de cine");
        }

    }
    
}
