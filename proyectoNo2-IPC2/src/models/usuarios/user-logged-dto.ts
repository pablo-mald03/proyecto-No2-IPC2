import { TipoUsuarioEnum } from "./tipo-usuario-enum";

export interface UserLoggedDTO{

    id: string,
    rol: TipoUsuarioEnum,
}