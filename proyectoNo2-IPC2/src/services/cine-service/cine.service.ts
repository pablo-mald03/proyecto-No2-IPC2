import { Injectable } from "@angular/core";
import { RestConstants } from "../../shared/rest-constants";
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";
import { Cine } from "../../models/cines/cine";
import { CantidadRegistrosDTO } from "../../models/usuarios/cantidad-registros-dto";
import { CineDTO } from "../../models/cines/cine-dto";



@Injectable({
    providedIn: 'root'
})

export class CineService {

    restConstants = new RestConstants();

    constructor(private httpClient: HttpClient) { }

    //Metodo que permite crear un nuevo cine asociado
    public crearNuevoCine(cine: CineDTO): Observable<void> {
        return this.httpClient.post<void>(`${this.restConstants.getApiURL()}cines`, cine);
    }

    //Endpoint que retorna dinamicamente la cantidad de cines asociados
    public listadoRegistros(limite: number, inicio: number): Observable<CineDTO[]> {
        return this.httpClient.get<CineDTO[]>(`${this.restConstants.getApiURL()}cines/limit/${limite}/offset/${inicio}`);
    }

    //Endpoint que ayuda a obtener la cantidad total de registros de cines asociados en el sistema
    public cantidadRegistros(): Observable<CantidadRegistrosDTO> {
        return this.httpClient.get<CantidadRegistrosDTO>(`${this.restConstants.getApiURL()}cines/cantidad`);
    }

}