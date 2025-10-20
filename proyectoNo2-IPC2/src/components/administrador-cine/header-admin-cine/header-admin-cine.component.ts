import { Component, inject } from '@angular/core';
import { Master } from '../../../services/masterLog/master';
import { Router, RouterLink, RouterLinkActive } from '@angular/router';

@Component({
  selector: 'app-header-admin-cine',
  imports: [RouterLink, RouterLinkActive],
  templateUrl: './header-admin-cine.component.html',
  styleUrl: './header-admin-cine.component.scss'
})
export class HeaderAdminCineComponent {

loggedUsuario: string = '';

//Atributo que sirve para redireccionar
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
