import { Injectable } from "@angular/core";
import { RestConstants } from "../../shared/rest-constants";
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";
import { ReporteSalasComentadasDTO } from "../../models/reportes-cine/reporte-salas-comentarios-dto";

@Injectable({

    providedIn: 'root'

})

export class ExportarReporteComentariosSalaService {

    restConstants = new RestConstants();

    constructor(private httpClient: HttpClient) { }

    //Endpoint que retorna el pdf de la cantidad de salas comentadas en un intervalo de tiempo
    public exportarReportesSalasComentadasSinFiltro(fechaInicio: string, fechaFin: string, limite: number, inicio: number): Observable<ReporteSalasComentadasDTO []> {
        return this.httpClient.get<ReporteSalasComentadasDTO []>(`${this.restConstants.getApiURL()}reportes/salas/comentadas/exportar/inicio/${fechaInicio}/fin/${fechaFin}/limit/${limite}/offset/${inicio}`);
    }

    //Endpoint que retorna el pdf de la cantidad de salas comentadas en un intervalo de tiempo con filtro
    public exportarReportesSalasComentadasConFiltro(fechaInicio: string, fechaFin: string, limite: number, tope: number, idSala: string): Observable<ReporteSalasComentadasDTO []> {
        return this.httpClient.get<ReporteSalasComentadasDTO []>(`${this.restConstants.getApiURL()}reportes/salas/comentadas/exportar/inicio/${fechaInicio}/fin/${fechaFin}/filtro/${idSala}/limit/${limite}/offset/${tope}`);
    }

}