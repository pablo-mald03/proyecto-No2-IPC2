import { Component, inject } from '@angular/core';
import { Router } from '@angular/router';
import { Master } from '../../../services/masterLog/master';

@Component({
  selector: 'app-header-admin',
  imports: [],
  templateUrl: './header-admin.component.html',
  styleUrl: './header-admin.component.scss'
})
export class HeaderAdminComponent {


 loggedUsuario: string = '';

  router = inject(Router);

  constructor(private masterSercive: Master) {

    this.readDatosLoggeados();
    this.masterSercive.onLogin.subscribe(res => {
      this.readDatosLoggeados();
    })

  }

  readDatosLoggeados() {

    const datosLogged = localStorage.getItem("angularUserCinema");

    if (datosLogged != null) {

      this.loggedUsuario = datosLogged;
    }
  }

  onLogOutUsuario() {
    localStorage.removeItem("angularUserCinema");
    this.readDatosLoggeados();
    this.loggedUsuario = '';
    this.router.navigateByUrl("/login");

  }

}
