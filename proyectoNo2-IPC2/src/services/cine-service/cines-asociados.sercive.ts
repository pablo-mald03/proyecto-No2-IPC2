import { Injectable } from "@angular/core";
import { RestConstants } from "../../shared/rest-constants";
import { HttpClient } from "@angular/common/http";
import { CineDTO } from "../../models/cines/cine-dto";
import { Observable } from "rxjs";
import { CantidadRegistrosDTO } from "../../models/usuarios/cantidad-registros-dto";

@Injectable({
    providedIn: 'root'
})

export class CinesAsociadosService {

    restConstants = new RestConstants();

    constructor(private httpClient: HttpClient) { }

    //Endpoint que retorna dinamicamente la cantidad de cines del administrador de cine
    public listadoRegistros(limite: number, inicio: number, idUsuario: string): Observable<CineDTO[]> {
        return this.httpClient.get<CineDTO[]>(`${this.restConstants.getApiURL()}administrador/cine/gestion/limit/${limite}/offset/${inicio}/id/${idUsuario}`);
    }

    //Endpoint que ayuda a obtener la cantidad total de registros de cines asociados en el sistema del administrador de cine
    public cantidadRegistros(idUsuario: string): Observable<CantidadRegistrosDTO> {
        return this.httpClient.get<CantidadRegistrosDTO>(`${this.restConstants.getApiURL()}administrador/cine/gestion/cantidad/${idUsuario}`);
    }

    //Endpoint que ayuda a obtener la cantidad total de registros de cines asociados en el sistema del administrador de cine
    public cinePorCodigo(idCine: string): Observable<CineDTO> {
        return this.httpClient.get<CineDTO>(`${this.restConstants.getApiURL()}administrador/cine/gestion/cine/codigo/${idCine}`);
    }


}