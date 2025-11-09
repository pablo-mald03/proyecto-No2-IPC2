import { Injectable } from "@angular/core";
import { RestConstants } from "../../shared/rest-constants";
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";
import { CantidadReportesDTO } from "../../models/reportes/cantidad-reportes-dto";
import { ReporteAnuncianteDTO } from "../../models/reportes/anunciante-dto";


@Injectable({

    providedIn: 'root'

})

export class GananciasSistemaService {

    restConstants = new RestConstants();

    constructor(private httpClient: HttpClient) { }

    //Endpoint que retorna el listado de reportes de ganancias del sistema
    public reportesGananciasSistema(fechaInicio: string, fechaFin: string, limite: number, inicio: number): Observable<ReporteAnuncianteDTO []> {
        return this.httpClient.get<ReporteAnuncianteDTO []>(`${this.restConstants.getApiURL()}reportes/sistema/ganancias/sistema/inicio/${fechaInicio}/fin/${fechaFin}/limit/${limite}/offset/${inicio}`);
    }





}