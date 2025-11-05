import { Injectable } from "@angular/core";
import { RestConstants } from "../../shared/rest-constants";
import { HttpClient, HttpResponse } from "@angular/common/http";
import { Observable } from "rxjs";

@Injectable({

    providedIn: 'root'

})

export class ExportarReporteSalasMasGustadasService {

    restConstants = new RestConstants();

    constructor(private httpClient: HttpClient) { }

    //Endpoint que retorna el pdf de la cantidad de salas mas gustadas en un intervalo de tiempo
    public exportarReporteSalasGustadas(fechaInicio: string, fechaFin: string, limite: number, inicio: number): Observable<HttpResponse<Blob>> {
        return this.httpClient.get(`${this.restConstants.getApiURL()}reportes/sistema/salas/gustadas/exportar/inicio/${fechaInicio}/fin/${fechaFin}/limit/${limite}/offset/${inicio}`, {
            responseType: 'blob',
            observe: 'response'
        });
    }

}