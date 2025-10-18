import { Component, Input } from '@angular/core';
import { Usuario } from '../../../models/usuarios/usuario';

@Component({
  selector: 'app-admins-cine-cards',
  imports: [],
  templateUrl: './admins-cine-cards.component.html',
  styleUrl: './admins-cine-cards.component.scss'
})
export class AdminsCineCardsComponent {

  @Input() admin!: Usuario;

  get fotoUrlAdmin(): string {
    if (this.admin?.foto) {
      return `data:image/jpeg;base64,${this.admin.foto}`;
    }
    return 'icons-app/defalutUser.png';
  }


  get rolAdmin(): string {
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
