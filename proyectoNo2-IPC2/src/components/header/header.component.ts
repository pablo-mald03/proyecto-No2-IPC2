import { Component, inject } from '@angular/core';
import { Router, RouterLink, RouterLinkActive } from '@angular/router';
import { Master } from '../../services/masterLog/master';

@Component({
  selector: 'app-header',
  imports: [RouterLink, RouterLinkActive],
  templateUrl: './header.component.html',
  styleUrl: './header.component.scss'
})
export class HeaderComponent {


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

    onLogOutUsuario(){
    localStorage.removeItem("angularUserCinema");
    this.readDatosLoggeados();
    this.loggedUsuario = '';
    this.router.navigateByUrl("/login");

  }
}
