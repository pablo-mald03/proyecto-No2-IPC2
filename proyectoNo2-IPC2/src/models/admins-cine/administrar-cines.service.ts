import { HttpClient } from "@angular/common/http";
import { RestConstants } from "../../shared/rest-constants";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { CantidadRegistrosDTO } from "../usuarios/cantidad-registros-dto";
import { CineDTO } from "../cines/cine-dto";

@Injectable({
    providedIn: 'root'
})

export class CineInformacionService {

    restConstants = new RestConstants();

    constructor(private httpClient: HttpClient) { }

    //Endpoint que ayuda a obtener la cantidad total de registros de cines asociados hacia el administrador de cine
    public cantidadRegistros(idUsuario: string): Observable<CantidadRegistrosDTO> {
        return this.httpClient.get<CantidadRegistrosDTO>(`${this.restConstants.getApiURL()}administrador/cine/gestion/cantidad/${idUsuario}`);
    }

    //Endpoint que retorna dinamicamente la cantidad de cines asociados hacia el administrador de cine
    public listadoRegistrosPrincipal(limite: number, inicio: number,idUsuario: string): Observable<CineDTO[]> {
        return this.httpClient.get<CineDTO[]>(`${this.restConstants.getApiURL()}administrador/cine/gestion/limit/${limite}/offset/${inicio}/id/${idUsuario}`);
    }

}