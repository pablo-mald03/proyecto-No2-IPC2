import { Injectable } from "@angular/core";
import { RestConstants } from "../../shared/rest-constants";
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";
import { CantidadReportesDTO } from "../../models/reportes/cantidad-reportes-dto";
import { SalaMasComentadaDTO } from "../../models/reportes/sala-mas-comentada-dto";


@Injectable({
    providedIn: 'root'
})

export class ReporteSalaMasComentadaService {


    restConstants = new RestConstants();

    constructor(private httpClient: HttpClient) { }

    //Endpoint que retorna las de las 5 salas mas comentadas en cualquier intervalo limitado o ilimitado
    public reporteSalasComentadas(fechaInicio: string, fechaFin: string, limite: number, inicio: number): Observable<SalaMasComentadaDTO[]> {
        return this.httpClient.get<SalaMasComentadaDTO[]>(`${this.restConstants.getApiURL()}reportes/sistema/salas/comentadas/inicio/${fechaInicio}/fin/${fechaFin}/limit/${limite}/offset/${inicio}`);
    }

    //Endpoint que ayuda a obtener la cantidad de reportes de 5 salas mas comentadas
    public cantidadReportes(fechaInicio: string, fechaFin: string): Observable<CantidadReportesDTO> {
        return this.httpClient.get<CantidadReportesDTO>(`${this.restConstants.getApiURL()}reportes/sistema/salas/comentadas/cantidad/inicio/${fechaInicio}/fin/${fechaFin}`);
    }

}