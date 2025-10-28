import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { UserLoggedDTO } from '../../models/usuarios/user-logged-dto';
import { TipoUsuarioEnum } from '../../models/usuarios/tipo-usuario-enum';

export const rolAdminCineGuard: CanActivateFn = (route, state) => {


  const router = inject(Router);

  const usuarioStr = localStorage.getItem("angularUserCinema");

  if (!usuarioStr) {

    return router.createUrlTree(['/login']);
  }

  const usuario = JSON.parse(usuarioStr) as UserLoggedDTO;

  if (usuario.rol === TipoUsuarioEnum.ADMINISTRADOR_CINE) {
    return true;
  }
  
  return router.createUrlTree(['/acceso-denegado']);




};
