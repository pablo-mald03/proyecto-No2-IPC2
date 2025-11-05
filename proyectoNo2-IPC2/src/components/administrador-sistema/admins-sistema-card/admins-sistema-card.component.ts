import { Component, Input } from '@angular/core';
import { Usuario } from '../../../models/usuarios/usuario';
import { UsuarioDatosDTO } from '../../../models/usuarios/usuario-datos-dto';

@Component({
  selector: 'app-admins-sistema-card',
  imports: [],
  templateUrl: './admins-sistema-card.component.html',
  styleUrl: './admins-sistema-card.component.scss'
})
export class AdminsSistemaCardComponent {

  @Input() admin!: UsuarioDatosDTO;

  get fotoUrl(): string {
    if (this.admin?.foto) {
      return `data:image/jpeg;base64,${this.admin.foto}`;
    }
    return 'icons-app/defalutUser.png';
  }

  //Metodo que ayuda a mostrar el nombre de los roles
  get rolNombre(): string {
    switch (this.admin.rol) {
      case '3':
        return 'Administrador del Sistema';
      case '4':
        return 'Administrador de Cine';
      default:
        return 'Usuario';
    }
  }


}
