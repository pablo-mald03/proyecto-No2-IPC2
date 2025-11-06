import { Injectable } from "@angular/core";
import { RestConstants } from "../../shared/rest-constants";
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";
import { CantidadReportesDTO } from "../../models/reportes/cantidad-reportes-dto";
import { ReporteAnuncioDTO } from "../../models/reportes/reporte-anuncio-dto";


@Injectable({

    providedIn: 'root'

})

export class ReporteAnunciosService {

    restConstants = new RestConstants();

    constructor(private httpClient: HttpClient) { }

    //Endpoint que retorna el listado de reportes de anuncios comprados Sin Filtro sin filtro 
    public reportesAnunciosCompradosSinFiltro(fechaInicio: string, fechaFin: string, limite: number, inicio: number): Observable<ReporteAnuncioDTO []> {
        return this.httpClient.get<ReporteAnuncioDTO []>(`${this.restConstants.getApiURL()}reportes/sistema/anuncios/comprados/inicio/${fechaInicio}/fin/${fechaFin}/limit/${limite}/offset/${inicio}`);
    }

    //Endpoint que ayuda a obtener la cantidad de reportes de anuncios comprados Sin Filtro
    public cantidadReportesSinFiltro(fechaInicio: string, fechaFin: string): Observable<CantidadReportesDTO> {
        return this.httpClient.get<CantidadReportesDTO>(`${this.restConstants.getApiURL()}reportes/sistema/anuncios/comprados/cantidad/inicio/${fechaInicio}/fin/${fechaFin}`);
    }

    //Endpoint que retorna los reportes de anuncios comprados conn Filtro
    public reportesAnunciosCompradosConFiltro(fechaInicio: string, fechaFin: string, limite: number, inicio: number, tipo: string): Observable<ReporteAnuncioDTO []> {
        return this.httpClient.get<ReporteAnuncioDTO []>(`${this.restConstants.getApiURL()}reportes/sistema/anuncios/comprados/inicio/${fechaInicio}/fin/${fechaFin}/filtro/${tipo}/limit/${limite}/offset/${inicio}`);
    }

    //Endpoint que ayuda a obtener la cantidad de reportes de anuncios comprados con Filtro
    public cantidadReportesConFiltro(fechaInicio: string, fechaFin: string, tipo: string): Observable<CantidadReportesDTO> {
        return this.httpClient.get<CantidadReportesDTO>(`${this.restConstants.getApiURL()}reportes/sistema/anuncios/comprados/cantidad/filtro/${tipo}/inicio/${fechaInicio}/fin/${fechaFin}`);
    }

}