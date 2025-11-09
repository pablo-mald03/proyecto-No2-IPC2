import { Injectable } from "@angular/core";
import { RestConstants } from "../../shared/rest-constants";
import { HttpClient, HttpResponse } from "@angular/common/http";
import { Observable } from "rxjs";

@Injectable({

    providedIn: 'root'

})

export class ExportarGananciasSistemaService {

    restConstants = new RestConstants();

    constructor(private httpClient: HttpClient) { }

    //Endpoint que retorna el pdf del listado de ganancias del sistema
    public exportarReporteGanancias(fechaInicio: string, fechaFin: string): Observable<HttpResponse<Blob>> {
        return this.httpClient.get(`${this.restConstants.getApiURL()}reportes/sistema/ganancias/sistema/exportar/inicio/${fechaInicio}/fin/${fechaFin}`, {
            responseType: 'blob',
            observe: 'response'
        });
    }



}