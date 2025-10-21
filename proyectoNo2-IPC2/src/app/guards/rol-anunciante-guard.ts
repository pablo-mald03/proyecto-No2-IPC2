import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { UserLoggedDTO } from '../../models/usuarios/user-logged-dto';
import { TipoUsuarioEnum } from '../../models/usuarios/tipo-usuario-enum';

export const rolAnuncianteGuard: CanActivateFn = (route, state) => {


  const router = inject(Router);

  const usuarioStr = localStorage.getItem("angularUserCinema");

  if (!usuarioStr) {

    return router.createUrlTree(['/login']);
  }

  const usuario = JSON.parse(usuarioStr) as UserLoggedDTO;

  if (usuario.rol === TipoUsuarioEnum.USUARIO_ESPECIAL) {
    return true;
  }
  
  return router.createUrlTree(['/acceso-denegado']);

};
