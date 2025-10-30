import { Injectable } from "@angular/core";
import { RestConstants } from "../../shared/rest-constants";
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";
import { CambioCredenciales } from "../../models/usuarios/cambio-credenciales";

@Injectable({
    providedIn: 'root'
})

export class UsuarioService {

    restConstants = new RestConstants();

    constructor(private httpClient: HttpClient) { }

    //Metodo que permite crear un nuevo usuario
    public crearNuevoUsuario(usuario: FormData): Observable<void> {
        return this.httpClient.post<void>(`${this.restConstants.getApiURL()}usuarios`, usuario);
    }

    //Endpoint que ayuda a cambiar las credenciales del usuario PENDIENTE CAMBIAR ENDPOINT
    public cambiarCredenciales(nuevasCredenciales: CambioCredenciales): Observable<void> {
        return this.httpClient.put<void>(`${this.restConstants.getApiURL()}reestablecer/credenciales`, nuevasCredenciales);
    }
}