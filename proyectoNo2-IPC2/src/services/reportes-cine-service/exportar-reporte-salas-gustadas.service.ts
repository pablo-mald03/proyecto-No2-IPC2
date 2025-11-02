import { Injectable } from "@angular/core";
import { RestConstants } from "../../shared/rest-constants";
import { HttpClient, HttpResponse } from "@angular/common/http";
import { Observable } from "rxjs";

@Injectable({

    providedIn: 'root'

})

export class ExportarReporteSalasGustadasService {

    restConstants = new RestConstants();

    constructor(private httpClient: HttpClient) { }

    //Endpoint que retorna el pdf de la cantidad de salas mas gustadas en un intervalo de tiempo
    public exportarReporteSalasGustadasSinFiltro(fechaInicio: string, fechaFin: string, limite: number, inicio: number): Observable<HttpResponse<Blob>> {
        return this.httpClient.get(`${this.restConstants.getApiURL()}reportes/salas/gustadas/exportar/inicio/${fechaInicio}/fin/${fechaFin}/limit/${limite}/offset/${inicio}`, {
            responseType: 'blob',
            observe: 'response'
        });
    }

    //Endpoint que retorna el pdf de la cantidad de salas mas gustadas en un intervalo de tiempo con filtro
    public exportarReporteSalasGustadasConFiltro(fechaInicio: string, fechaFin: string, limite: number, tope: number, idSala: string): Observable<HttpResponse<Blob>> {
        return this.httpClient.get(`${this.restConstants.getApiURL()}reportes/salas/gustadas/exportar/inicio/${fechaInicio}/fin/${fechaFin}/filtro/${idSala}/limit/${limite}/offset/${tope}`, {
            responseType: 'blob',
            observe: 'response'
        });
    }

}