import { Injectable } from "@angular/core";
import { RestConstants } from "../../shared/rest-constants";
import { HttpClient, HttpResponse } from "@angular/common/http";
import { Observable } from "rxjs";

@Injectable({

    providedIn: 'root'

})

export class ExportarReporteBoletosVendidosService{

    restConstants = new RestConstants();

    constructor(private httpClient: HttpClient){}


     //Endpoint que retorna el pdf del reporte de los boletos vendidos en un intervalo de tiempo
    public exportarReporteBoletosVendidosSinFiltro(fechaInicio: string, fechaFin: string, limite: number, inicio: number): Observable<HttpResponse<Blob>> {
        return this.httpClient.get(`${this.restConstants.getApiURL()}reportes/boletos/vendidos/exportar/inicio/${fechaInicio}/fin/${fechaFin}/limit/${limite}/offset/${inicio}`, {
            responseType: 'blob',
            observe: 'response'
        });
    }

    //Endpoint que retorna el pdf del reporte de los boletos vendidos en un intervalo de tiempo con filtro
    public exportarReporteBoletosVendidosConFiltro(fechaInicio: string, fechaFin: string, limite: number, tope: number, idSala: string): Observable<HttpResponse<Blob>> {
        return this.httpClient.get(`${this.restConstants.getApiURL()}reportes/boletos/vendidos/exportar/inicio/${fechaInicio}/fin/${fechaFin}/filtro/${idSala}/limit/${limite}/offset/${tope}`, {
            responseType: 'blob',
            observe: 'response'
        });
    }

}