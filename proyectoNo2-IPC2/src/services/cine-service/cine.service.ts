import { Injectable } from "@angular/core";
import { RestConstants } from "../../shared/rest-constants";
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";
import { Cine } from "../../models/cines/cine";



@Injectable({
    providedIn: 'root'
})

export class CineService {

    restConstants = new RestConstants();

    constructor(private httpClient: HttpClient) { }

    //Metodo que permite crear un nuevo cine asociado
    public crearNuevoCine(cine: Cine): Observable<void> {
        return this.httpClient.post<void>(`${this.restConstants.getApiURL()}cines`, cine);
    }

    /*
    //Endpoint que ayuda a cambiar las credenciales del usuario PENDIENTE CAMBIAR ENDPOINT
    public cambiarCredenciales(nuevasCredenciales: CambioCredenciales): Observable<void> {
        return this.httpClient.put<void>(`${this.restConstants.getApiURL()}reestablecer/credenciales`, nuevasCredenciales);
    }

    //Metodo get que permite consultar el perfil al usuario
    public consultarPerfil(id: string): Observable<UsuarioDatosDTO> {
        return this.httpClient.get<UsuarioDatosDTO>(`${this.restConstants.getApiURL()}usuarios/${id}`);
    }
    */

}