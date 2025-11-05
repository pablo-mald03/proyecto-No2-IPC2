import { Injectable } from "@angular/core";
import { RestConstants } from "../../shared/rest-constants";
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";
import { UsuarioDatosDTO } from "../../models/usuarios/usuario-datos-dto";
import { CantidadRegistrosDTO } from "../../models/usuarios/cantidad-registros-dto";

@Injectable({

    providedIn: 'root'
})

export class UsuariosSistemaService {

    restConstants = new RestConstants();

    constructor(private httpClient: HttpClient) { }

    //Metodo que permite crear un nuevo usuario administrador de sistema
    public crearAdministradorSistema(usuario: FormData): Observable<void> {
        return this.httpClient.post<void>(`${this.restConstants.getApiURL()}administrador/usuarios/sistema`, usuario);
    }

    //Endpoint que retorna dinamicamente la cantidad de administradores de sistema
    public listadoRegistros(limite: number, inicio: number): Observable<UsuarioDatosDTO[]> {
        return this.httpClient.get<UsuarioDatosDTO[]>(`${this.restConstants.getApiURL()}administrador/usuarios/sistema/limit/${limite}/offset/${inicio}`);
    }

    //Endpoint que ayuda a obtener la cantidad total de registros de adminsitradores de sistema
    public cantidadRegistros(): Observable<CantidadRegistrosDTO> {
        return this.httpClient.get<CantidadRegistrosDTO>(`${this.restConstants.getApiURL()}administrador/usuarios/sistema/cantidad`);
    }
}