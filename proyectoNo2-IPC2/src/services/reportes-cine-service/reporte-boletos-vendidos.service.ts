import { Injectable } from "@angular/core";
import { RestConstants } from "../../shared/rest-constants";
import { HttpClient } from "@angular/common/http";
import { ReporteSalasGustadasDTO } from "../../models/reportes-cine/reporte-sala-gustada-dto";
import { Observable } from "rxjs";
import { CantidadReportesDTO } from "../../models/reportes/cantidad-reportes-dto";
import { ReporteBoletosVendidosDTO } from "../../models/reportes-cine/reporte-boletos-vendidos-dto";


@Injectable({
    providedIn: 'root'
})

export class ReporteBoletosVendidosService {


    restConstants = new RestConstants();

    constructor(private httpClient: HttpClient) { }

    //Endpoint que retorna los reportes de boletos vendidos en un intervalo de tiempo sin filtro
    public reporteBoletosVendidosSinFiltro(fechaInicio: string, fechaFin: string, limite: number, inicio: number): Observable<ReporteBoletosVendidosDTO[]> {
        return this.httpClient.get<ReporteBoletosVendidosDTO[]>(`${this.restConstants.getApiURL()}reportes/boletos/vendidos/inicio/${fechaInicio}/fin/${fechaFin}/limit/${limite}/offset/${inicio}`);
    }

    //Endpoint que ayuda a obtener la cantidad de boletos vendidos sin filtro
    public cantidadReportesSinFiltro(fechaInicio: string, fechaFin: string): Observable<CantidadReportesDTO> {
        return this.httpClient.get<CantidadReportesDTO>(`${this.restConstants.getApiURL()}reportes/boletos/vendidos/cantidad/inicio/${fechaInicio}/fin/${fechaFin}`);
    }

    //Endpoint que retorna las de boletos vendidos en un intervalo de tiempo con filtro
    public reporteBoletosVendidosConFiltro(fechaInicio: string, fechaFin: string, limite: number, tope: number, idSala: string): Observable<ReporteBoletosVendidosDTO[]> {
        return this.httpClient.get<ReporteBoletosVendidosDTO[]>(`${this.restConstants.getApiURL()}reportes/boletos/vendidos/inicio/${fechaInicio}/fin/${fechaFin}/filtro/${idSala}/limit/${limite}/offset/${tope}`);
    }

    //Endpoint que ayuda a obtener la cantidad de reportes de boletos vendidos con filtro
    public cantidadReportesConFiltro(fechaInicio: string, fechaFin: string, idSala: string): Observable<CantidadReportesDTO> {
        return this.httpClient.get<CantidadReportesDTO>(`${this.restConstants.getApiURL()}reportes/boletos/vendidos/cantidad/filtro/${idSala}/inicio/${fechaInicio}/fin/${fechaFin}`);
    }



}