import { Injectable } from "@angular/core";
import { RestConstants } from "../../shared/rest-constants";
import { HttpClient } from "@angular/common/http";
import { ReporteSalasGustadasDTO } from "../../models/reportes-cine/reporte-sala-gustada-dto";
import { Observable } from "rxjs";
import { CantidadReportesDTO } from "../../models/reportes/cantidad-reportes-dto";


@Injectable({
    providedIn: 'root'
})

export class ReporteSalaGustadaService {


    restConstants = new RestConstants();

    constructor(private httpClient: HttpClient) { }

    //Endpoint que retorna las de las 5 salas mas gustadas en un intervalo de tiempo sin filtro
    public reporteSalasGustadasSinFiltro(fechaInicio: string, fechaFin: string, limite: number, inicio: number): Observable<ReporteSalasGustadasDTO[]> {
        return this.httpClient.get<ReporteSalasGustadasDTO[]>(`${this.restConstants.getApiURL()}reportes/salas/gustadas/inicio/${fechaInicio}/fin/${fechaFin}/limit/${limite}/offset/${inicio}`);
    }

    //Endpoint que ayuda a obtener la cantidad de reportes de 5 salas mas gustadas sin filtro
    public cantidadReportesSinFiltro(fechaInicio: string, fechaFin: string): Observable<CantidadReportesDTO> {
        return this.httpClient.get<CantidadReportesDTO>(`${this.restConstants.getApiURL()}reportes/salas/gustadas/cantidad/inicio/${fechaInicio}/fin/${fechaFin}`);
    }

    //Endpoint que retorna las de las 5 salas mas gustadas en un intervalo de tiempo con filtro
    public reporteSalasGustadasConFiltro(fechaInicio: string, fechaFin: string, limite: number, tope: number, idSala: string): Observable<ReporteSalasGustadasDTO[]> {
        return this.httpClient.get<ReporteSalasGustadasDTO[]>(`${this.restConstants.getApiURL()}reportes/salas/gustadas/inicio/${fechaInicio}/fin/${fechaFin}/filtro/${idSala}/limit/${limite}/offset/${tope}`);
    }

    //Endpoint que ayuda a obtener la cantidad de reportes de 5 salas mas gustadas con filtro
    public cantidadReportesConFiltro(fechaInicio: string, fechaFin: string, idSala: string): Observable<CantidadReportesDTO> {
        return this.httpClient.get<CantidadReportesDTO>(`${this.restConstants.getApiURL()}reportes/salas/gustadas/cantidad/filtro/${idSala}/inicio/${fechaInicio}/fin/${fechaFin}`);
    }


}