import { Injectable } from "@angular/core";
import { RestConstants } from "../../shared/rest-constants";
import { HttpClient } from "@angular/common/http";
import { Usuario } from "../../models/usuarios/usuario";
import { Observable } from "rxjs";

@Injectable({
    providedIn: 'root'
})

export class UsuarioService {

    restConstants = new RestConstants();

    constructor(private httpClient: HttpClient) { }

    public crearNuevoUsuario(usuario: FormData): Observable<void> {
        return this.httpClient.post<void>(`${this.restConstants.getApiURL()}usuarios`, usuario);
    }
}