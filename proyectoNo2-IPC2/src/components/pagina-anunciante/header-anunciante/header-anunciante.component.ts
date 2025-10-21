import { Component, inject } from '@angular/core';
import { Router, RouterLink, RouterLinkActive } from '@angular/router';
import { Master } from '../../../services/masterLog/master';

@Component({
  selector: 'app-header-anunciante',
  imports: [RouterLinkActive, RouterLink],
  templateUrl: './header-anunciante.component.html',
  styleUrl: './header-anunciante.component.scss'
})
export class HeaderAnuncianteComponent {

  loggedUsuario: string = '';

  router = inject(Router);

  constructor(private masterSercive: Master) {

    this.readDatosLoggeados();
    this.masterSercive.onLogin.subscribe(res => {
      this.readDatosLoggeados();
    })

  }


  //Metodo que permite verificar que el usuario este logueado
  readDatosLoggeados() {

    const datosLogged = localStorage.getItem("angularUserCinema");

    if (datosLogged != null) {

      this.loggedUsuario = datosLogged;
    }
  }

  //Metodo que permite generar el logout del administrador de cine
  onLogOutUsuario() {
    localStorage.removeItem("angularUserCinema");
    this.readDatosLoggeados();
    this.loggedUsuario = '';
    this.router.navigateByUrl("/login");

  }
}
