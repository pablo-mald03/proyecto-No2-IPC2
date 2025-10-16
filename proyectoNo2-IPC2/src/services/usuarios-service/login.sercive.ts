import { Injectable } from "@angular/core";
import { RestConstants } from "../../shared/rest-constants";
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";
import { LoginDTO } from "../../models/usuarios/login-dto";

@Injectable({

    providedIn: 'root'

})

export class LoginService {

    restConstants = new RestConstants();

    constructor(private httpClient: HttpClient) { }

    public autenticarUsuario(usuario: LoginDTO): Observable<void> {
        return this.httpClient.post<void>(`${this.restConstants.getApiURL()}login`, usuario);
    }

}