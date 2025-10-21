import { Component, inject } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-acceso-denegado-page',
  imports: [],
  templateUrl: './acceso-denegado-page.component.html',
  styleUrl: './acceso-denegado-page.component.scss'
})
export class AccesoDenegadoPageComponent {

  router = inject(Router);

  //Metodo que permite regresar al menu principal cuando se denegan los permisos
  regresarMenu() {

    const datosLogged = localStorage.getItem("angularUserCinema");

    if (datosLogged != null) {
      this.cerrarSesionUsuario();
    }

    this.router.navigateByUrl('');
  }

  //Metodo que permite generar el logout para que se cierre la sesion manipulada
  cerrarSesionUsuario() {
    localStorage.removeItem("angularUserCinema");

  }

}
