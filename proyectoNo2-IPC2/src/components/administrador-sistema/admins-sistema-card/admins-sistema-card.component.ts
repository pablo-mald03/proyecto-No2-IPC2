import { Component, Input } from '@angular/core';
import { Usuario } from '../../../models/usuarios/usuario';

@Component({
  selector: 'app-admins-sistema-card',
  imports: [],
  templateUrl: './admins-sistema-card.component.html',
  styleUrl: './admins-sistema-card.component.scss'
})
export class AdminsSistemaCardComponent {

  @Input() admin!: Usuario;

  get fotoUrl(): string {
    if (this.admin?.foto) {
      return `data:image/jpeg;base64,${this.admin.foto}`;
    }
    return 'icons-app/defalutUser.png';
  }

  get rolNombre(): string {
    switch (this.admin.codigoRol) {
      case 'ROL-1':
        return 'Administrador del Sistema';
      case 'ROL-2':
        return 'Administrador de Cine';
      default:
        return 'Usuario';
    }
  }


}
