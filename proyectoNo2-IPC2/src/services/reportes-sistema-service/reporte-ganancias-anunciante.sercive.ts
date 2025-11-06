import { Injectable } from "@angular/core";
import { RestConstants } from "../../shared/rest-constants";
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";
import { CantidadReportesDTO } from "../../models/reportes/cantidad-reportes-dto";
import { ReporteAnuncianteDTO } from "../../models/reportes/anunciante-dto";


@Injectable({

    providedIn: 'root'

})

export class ReporteGananciasAnuncianteService {

    restConstants = new RestConstants();

    constructor(private httpClient: HttpClient) { }

    //Endpoint que retorna el listado de reportes de ganancias de anunciantes sin filtro 
    public reportesGananciasAuncianteSinFiltro(fechaInicio: string, fechaFin: string, limite: number, inicio: number): Observable<ReporteAnuncianteDTO []> {
        return this.httpClient.get<ReporteAnuncianteDTO []>(`${this.restConstants.getApiURL()}reportes/sistema/ganancias/anunciantes/inicio/${fechaInicio}/fin/${fechaFin}/limit/${limite}/offset/${inicio}`);
    }

    //Endpoint que ayuda a obtener la cantidad de reportes de ganancias de anuncinate sin filtro
    public cantidadReportesSinFiltro(fechaInicio: string, fechaFin: string): Observable<CantidadReportesDTO> {
        return this.httpClient.get<CantidadReportesDTO>(`${this.restConstants.getApiURL()}reportes/sistema/ganancias/anunciantes/cantidad/inicio/${fechaInicio}/fin/${fechaFin}`);
    }

    //Endpoint que retorna los reportes de ganancias con filtro
    public reportesGananciasAuncianteConFiltro(fechaInicio: string, fechaFin: string, limite: number, inicio: number, idUsuario: string): Observable<ReporteAnuncianteDTO []> {
        return this.httpClient.get<ReporteAnuncianteDTO []>(`${this.restConstants.getApiURL()}reportes/sistema/ganancias/anunciantes/inicio/${fechaInicio}/fin/${fechaFin}/filtro/${idUsuario}/limit/${limite}/offset/${inicio}`);
    }

    //Endpoint que ayuda a obtener la cantidad de reportes de ganancias que se tiene por anunciante con filtro
    public cantidadReportesConFiltro(fechaInicio: string, fechaFin: string, idUsuario: string): Observable<CantidadReportesDTO> {
        return this.httpClient.get<CantidadReportesDTO>(`${this.restConstants.getApiURL()}reportes/sistema/ganancias/anunciantes/cantidad/filtro/${idUsuario}/inicio/${fechaInicio}/fin/${fechaFin}`);
    }



}