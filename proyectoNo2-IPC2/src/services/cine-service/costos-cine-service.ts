import { Injectable } from "@angular/core";
import { RestConstants } from "../../shared/rest-constants";
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";
import { CantidadRegistrosDTO } from "../../models/usuarios/cantidad-registros-dto";
import { CineDTO } from "../../models/cines/cine-dto";
import { CostoModificacionCineDTO } from "../../models/reportes/costo-modificacion-cine-dto";
import { CostoModificacionDTO } from "../../models/cines/costo-modificacion.dto";
import { CostoCineDTO } from "../../models/cines/costo-cine-dto";



@Injectable({
    providedIn: 'root'
})

export class CostosCineService {

    restConstants = new RestConstants();

    constructor(private httpClient: HttpClient) { }

    //Metodo que permite crear un nuevo cine asociado
    public registrarNuevoCosto(cine: CostoCineDTO): Observable<void> {
        return this.httpClient.post<void>(`${this.restConstants.getApiURL()}cines/modificar/costos`, cine);
    }

    //Endpoint que retorna el listado de historial de costos del cine
    public listadoCostos(codigoCine: string): Observable<CostoModificacionDTO[]> {
        return this.httpClient.get<CostoModificacionDTO[]>(`${this.restConstants.getApiURL()}cines/modificar/costos/${codigoCine}`);
    }


}