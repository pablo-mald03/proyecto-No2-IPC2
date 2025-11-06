import { Injectable } from "@angular/core";
import { RestConstants } from "../../shared/rest-constants";
import { HttpClient, HttpResponse } from "@angular/common/http";
import { Observable } from "rxjs";

@Injectable({

    providedIn: 'root'

})

export class ExportarGananciasAnuncianteService {

    restConstants = new RestConstants();

    constructor(private httpClient: HttpClient) { }

    //Endpoint que retorna el pdf del listado de ganancias por anunciante en un intervalo de tiempo
    public exportarReportesGananciaAnunciantesSinFiltro(fechaInicio: string, fechaFin: string, limite: number, inicio: number): Observable<HttpResponse<Blob>> {
        return this.httpClient.get(`${this.restConstants.getApiURL()}reportes/sistema/ganancias/anunciantes/exportar/inicio/${fechaInicio}/fin/${fechaFin}/limit/${limite}/offset/${inicio}`, {
            responseType: 'blob',
            observe: 'response'
        });
    }

    //Endpoint que retorna el pdf del listado de ganancias por anunciante en un intervalo de tiempo con filtro
    public exportarReportesGananciaAnunciantesConFiltro(fechaInicio: string, fechaFin: string, limite: number, tope: number, idUsuario: string): Observable<HttpResponse<Blob>> {
        return this.httpClient.get(`${this.restConstants.getApiURL()}reportes/sistema/ganancias/anunciantes/exportar/inicio/${fechaInicio}/fin/${fechaFin}/filtro/${idUsuario}/limit/${limite}/offset/${tope}`, {
            responseType: 'blob',
            observe: 'response'
        });
    }

}