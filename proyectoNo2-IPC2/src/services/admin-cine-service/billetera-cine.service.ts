import { Injectable } from "@angular/core";
import { RestConstants } from "../../shared/rest-constants";
import { HttpClient } from "@angular/common/http";
import { BilleteraCineDTO } from "../../models/admins-cine/billetera-cine-dto";
import { Observable } from "rxjs";
import { CantidadRegistrosDTO } from "../../models/usuarios/cantidad-registros-dto";

@Injectable({
    providedIn: 'root'
})

export class BilleteraCineService {

    restConstants = new RestConstants();

    constructor(private httpClient: HttpClient) { }

    //Endpoint que retorna dinamicamente la cantidad dde billeteras que pose el cine donde el usuario esta relacionado
    public listadoBilleteras(limite: number, inicio: number, idUsuario: string): Observable<BilleteraCineDTO[]> {
        return this.httpClient.get<BilleteraCineDTO[]>(`${this.restConstants.getApiURL()}cines/billetera/asociada/limit/${limite}/offset/${inicio}/usuario/${idUsuario}`);
    }

    //Endpoint que ayuda a obtener la cantidad total de registros de cines asociados en el sistema del administrador de cine
    public cantidadRegistros(idUsuario: string): Observable<CantidadRegistrosDTO> {
        return this.httpClient.get<CantidadRegistrosDTO>(`${this.restConstants.getApiURL()}cines/billetera/cantidad/${idUsuario}`);
    }


}