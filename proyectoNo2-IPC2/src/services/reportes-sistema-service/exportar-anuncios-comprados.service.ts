import { Injectable } from "@angular/core";
import { RestConstants } from "../../shared/rest-constants";
import { HttpClient, HttpResponse } from "@angular/common/http";
import { Observable } from "rxjs";

@Injectable({

    providedIn: 'root'

})

export class ExportarAnunciosCompradosService {

    restConstants = new RestConstants();

    constructor(private httpClient: HttpClient) { }

    //Endpoint que retorna el pdf del listado de anuncios comprados en cierto intervalo de tiempo 
    public exportarReportesAnunciosSinFiltro(fechaInicio: string, fechaFin: string, limite: number, inicio: number): Observable<HttpResponse<Blob>> {
        return this.httpClient.get(`${this.restConstants.getApiURL()}reportes/sistema/anuncios/comprados/exportar/inicio/${fechaInicio}/fin/${fechaFin}/limit/${limite}/offset/${inicio}`, {
            responseType: 'blob',
            observe: 'response'
        });
    }

    //Endpoint que retorna el pdf del listado de anuncios comprados en cierto intervalo de tiempo
    public exportarReportesAnunciosConFiltro(fechaInicio: string, fechaFin: string, limite: number, tope: number, tipo: string): Observable<HttpResponse<Blob>> {
        return this.httpClient.get(`${this.restConstants.getApiURL()}reportes/sistema/anuncios/comprados/exportar/inicio/${fechaInicio}/fin/${fechaFin}/filtro/${tipo}/limit/${limite}/offset/${tope}`, {
            responseType: 'blob',
            observe: 'response'
        });
    }

}