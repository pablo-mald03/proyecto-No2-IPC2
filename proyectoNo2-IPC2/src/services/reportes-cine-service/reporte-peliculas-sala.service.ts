import { Inject, Injectable } from "@angular/core";
import { RestConstants } from "../../shared/rest-constants";
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";
import { ReporteSalaPeliculaProyectadaDTO } from "../../models/reportes-cine/reporte-sala-pelicula-proyectada-dto";
import { CantidadReportesDTO } from "../../models/reportes/cantidad-reportes-dto";

@Injectable({


    providedIn: 'root'
}
)

export class ReportePeliculasSalaService{


     restConstants = new RestConstants();

    constructor(private httpClient: HttpClient) { }



    //Endpoint que retorna las de salas con peliculas proyectadas en un intervalo de tiempo
    public reportesSalasPeliculaSinFiltro(fechaInicio: string, fechaFin: string, limite: number, inicio: number): Observable<ReporteSalaPeliculaProyectadaDTO []> {
        return this.httpClient.get<ReporteSalaPeliculaProyectadaDTO []>(`${this.restConstants.getApiURL()}reportes/peliculas/proyectadas/inicio/${fechaInicio}/fin/${fechaFin}/limit/${limite}/offset/${inicio}`);
    }

    //Endpoint que ayuda a obtener la cantidad de reportes de la sala de cine con peliculas proyectadas
    public cantidadReportesSinFiltro(fechaInicio: string, fechaFin: string): Observable<CantidadReportesDTO> {
        return this.httpClient.get<CantidadReportesDTO>(`${this.restConstants.getApiURL()}reportes/peliculas/proyectadas/cantidad/inicio/${fechaInicio}/fin/${fechaFin}`);
    }

    //Endpoint que retorna las de salas con peliculas proyectadas en un intervalo de tiempo con filtro
    public reportesSalasPeliculaConFiltro(fechaInicio: string, fechaFin: string, limite: number, tope: number, idSala: string): Observable<ReporteSalaPeliculaProyectadaDTO []> {
        return this.httpClient.get<ReporteSalaPeliculaProyectadaDTO []>(`${this.restConstants.getApiURL()}reportes/peliculas/proyectadas/inicio/${fechaInicio}/fin/${fechaFin}/filtro/${idSala}/limit/${limite}/offset/${tope}`);
    }

    //Endpoint que ayuda a obtener la cantidad de reportes de la sala de cine con peliculas proyectadas con filtro
    public cantidadReportesConFiltro(fechaInicio: string, fechaFin: string, idSala: string): Observable<CantidadReportesDTO> {
        return this.httpClient.get<CantidadReportesDTO>(`${this.restConstants.getApiURL()}reportes/peliculas/proyectadas/cantidad/filtro/${idSala}/inicio/${fechaInicio}/fin/${fechaFin}`);
    }

}