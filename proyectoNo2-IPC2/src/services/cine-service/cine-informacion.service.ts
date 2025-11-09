import { Injectable } from "@angular/core";
import { RestConstants } from "../../shared/rest-constants";
import { HttpClient } from "@angular/common/http";
import { CinesAsociadosDTO } from "../../models/cines/cines-asociados-dto";
import { Observable } from "rxjs";
import { CantidadRegistrosDTO } from "../../models/usuarios/cantidad-registros-dto";
import { CineInformacionDTO } from "../../models/cines/cine-informacion-dto";

@Injectable({
    providedIn: 'root'
})

export class CineInformacionService {

    restConstants = new RestConstants();

    constructor(private httpClient: HttpClient) { }

    //Endpoint que retorna dinamicamente la cantidad de cines asociados llave valor
    public listadoRegistros(limite: number, inicio: number): Observable<CinesAsociadosDTO[]> {
        return this.httpClient.get<CinesAsociadosDTO[]>(`${this.restConstants.getApiURL()}cines/informacion/llave/limit/${limite}/offset/${inicio}`);
    }

    //Endpoint que ayuda a obtener la cantidad total de registros de cines asociados en el sistema
    public cantidadRegistros(): Observable<CantidadRegistrosDTO> {
        return this.httpClient.get<CantidadRegistrosDTO>(`${this.restConstants.getApiURL()}cines/informacion/cantidad`);
    }

    //Endpoint que retorna dinamicamente la cantidad de cines asociados para el menu principal
    public listadoRegistrosPrincipal(limite: number, inicio: number): Observable<CineInformacionDTO[]> {
        return this.httpClient.get<CineInformacionDTO[]>(`${this.restConstants.getApiURL()}cines/informacion/principal/limit/${limite}/offset/${inicio}`);
    }

    //Endpoint que retorna la informacion de un cine en especifico
    public informacionCinePorCodigo(idCine: string): Observable<CineInformacionDTO> {
        return this.httpClient.get<CineInformacionDTO>(`${this.restConstants.getApiURL()}cines/informacion/principal/cine/${idCine}`);
    }

    

}