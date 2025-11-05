import { Component, Input } from '@angular/core';
import { Usuario } from '../../../models/usuarios/usuario';
import { UsuarioDatosDTO } from '../../../models/usuarios/usuario-datos-dto';

@Component({
  selector: 'app-admins-cine-cards',
  imports: [],
  templateUrl: './admins-cine-cards.component.html',
  styleUrl: './admins-cine-cards.component.scss'
})
export class AdminsCineCardsComponent {

  @Input() admin!: UsuarioDatosDTO;

  get fotoUrlAdmin(): string {
    if (this.admin?.foto) {
      return `data:image/jpeg;base64,${this.admin.foto}`;
    }
    return 'icons-app/defalutUser.png';
  }

  //Metodo get que ayuda a obtener los roles acorde al codigo enviado
  get rolAdmin(): string {
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
