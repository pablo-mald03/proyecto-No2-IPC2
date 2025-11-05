import { Injectable } from "@angular/core";
import { RestConstants } from "../../shared/rest-constants";
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";
import { CantidadReportesDTO } from "../../models/reportes/cantidad-reportes-dto";
import { SalaMasGustadaDTO } from "../../models/reportes/sala-mas-gustada-dto";


@Injectable({
    providedIn: 'root'
})

export class ReporteSalaMasGustadaService {


    restConstants = new RestConstants();

    constructor(private httpClient: HttpClient) { }

    //Endpoint que retorna las de las 5 salas mas gustadas en un intervalo de tiempo sin filtro
    public reporteSalasGustadas(fechaInicio: string, fechaFin: string, limite: number, inicio: number): Observable<SalaMasGustadaDTO[]> {
        return this.httpClient.get<SalaMasGustadaDTO[]>(`${this.restConstants.getApiURL()}reportes/sistema/salas/gustadas/inicio/${fechaInicio}/fin/${fechaFin}/limit/${limite}/offset/${inicio}`);
    }

    //Endpoint que ayuda a obtener la cantidad de reportes de 5 salas mas gustadas sin filtro
    public cantidadReportes(fechaInicio: string, fechaFin: string): Observable<CantidadReportesDTO> {
        return this.httpClient.get<CantidadReportesDTO>(`${this.restConstants.getApiURL()}reportes/sistema/salas/gustadas/cantidad/inicio/${fechaInicio}/fin/${fechaFin}`);
    }

}