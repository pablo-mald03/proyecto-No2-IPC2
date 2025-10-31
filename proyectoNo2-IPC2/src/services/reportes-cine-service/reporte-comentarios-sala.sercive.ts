import { Injectable } from "@angular/core";
import { RestConstants } from "../../shared/rest-constants";
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";
import { ReporteSalasComentadasDTO } from "../../models/reportes-cine/reporte-salas-comentarios-dto";
import { CantidadReportesDTO } from "../../models/reportes/cantidad-reportes-dto";


@Injectable({

    providedIn: 'root'

})

export class ReporteComentariosSalaService {

    restConstants = new RestConstants();

    constructor(private httpClient: HttpClient) { }

    //Endpoint que retorna la cantidad de salas comentadas en un intervalo de tiempo
    public reportesSalasComentadasSinFiltro(fechaInicio: string, fechaFin: string, limite: number, inicio: number): Observable<ReporteSalasComentadasDTO []> {
        return this.httpClient.get<ReporteSalasComentadasDTO []>(`${this.restConstants.getApiURL()}reportes/salas/comentadas/inicio/${fechaInicio}/fin/${fechaFin}/limit/${limite}/offset/${inicio}`);
    }

    //Endpoint que ayuda a obtener la cantidad de reportes de la sala de cine
    public cantidadReportesSinFiltro(fechaInicio: string, fechaFin: string): Observable<CantidadReportesDTO> {
        return this.httpClient.get<CantidadReportesDTO>(`${this.restConstants.getApiURL()}reportes/salas/comentadas/cantidad/inicio/${fechaInicio}/fin/${fechaFin}`);
    }



}